package lk.epicgreen.erp.sales.service.impl;

import lk.epicgreen.erp.sales.dto.request.SalesOrderRequest;
import lk.epicgreen.erp.sales.dto.request.SalesOrderItemRequest;
import lk.epicgreen.erp.sales.dto.response.SalesOrderResponse;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.sales.entity.SalesOrderItem;
import lk.epicgreen.erp.sales.mapper.SalesOrderMapper;
import lk.epicgreen.erp.sales.mapper.SalesOrderItemMapper;
import lk.epicgreen.erp.sales.repository.SalesOrderRepository;
import lk.epicgreen.erp.sales.repository.SalesOrderItemRepository;
import lk.epicgreen.erp.sales.service.SalesOrderService;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerAddress;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.repository.CustomerAddressRepository;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.entity.TaxRate;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UnitOfMeasureRepository;
import lk.epicgreen.erp.admin.repository.TaxRateRepository;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of SalesOrderService interface
 * 
 * Sales Order Status Workflow:
 * DRAFT → CONFIRMED → PENDING_APPROVAL → APPROVED → PROCESSING → PACKED → DISPATCHED → DELIVERED
 * Can be CANCELLED from any status
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final TaxRateRepository taxRateRepository;
    private final UserRepository userRepository;
    private final SalesOrderMapper salesOrderMapper;
    private final SalesOrderItemMapper salesOrderItemMapper;

    @Override
    @Transactional
    public SalesOrderResponse createSalesOrder(SalesOrderRequest request) {
        log.info("Creating new Sales Order: {}", request.getOrderNumber());

        // Validate unique constraint
        validateUniqueOrderNumber(request.getOrderNumber(), null);

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Verify warehouse exists
        Warehouse warehouse = findWarehouseById(request.getWarehouseId());

        // Create order entity
        SalesOrder order = salesOrderMapper.toEntity(request);
        order.setCustomer(customer);
        order.setWarehouse(warehouse);

        // Set addresses
        if (request.getBillingAddressId() != null) {
            CustomerAddress billingAddress = findCustomerAddressById(request.getBillingAddressId());
            order.setBillingAddress(billingAddress);
        }

        if (request.getShippingAddressId() != null) {
            CustomerAddress shippingAddress = findCustomerAddressById(request.getShippingAddressId());
            order.setShippingAddress(shippingAddress);
        }

        // Set sales representative
        if (request.getSalesRepId() != null) {
            User salesRep = findUserById(request.getSalesRepId());
            order.setSalesRep(salesRep);
        }

        // Create order items
        List<SalesOrderItem> items = new ArrayList<>();
        for (SalesOrderItemRequest itemRequest : request.getItems()) {
            SalesOrderItem item = createOrderItem(itemRequest);
            item.setOrder(order);
            items.add(item);
        }
        order.setItems(items);

        SalesOrder savedOrder = salesOrderRepository.save(order);
        log.info("Sales Order created successfully: {}", savedOrder.getOrderNumber());

        return salesOrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public SalesOrderResponse updateSalesOrder(Long id, SalesOrderRequest request) {
        log.info("Updating Sales Order: {}", id);

        SalesOrder order = findSalesOrderById(id);

        // Can only update DRAFT orders
        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Sales Order. Current status: " + order.getStatus() + 
                ". Only DRAFT orders can be updated.");
        }

        // Validate unique constraint if order number changed
        if (!order.getOrderNumber().equals(request.getOrderNumber())) {
            validateUniqueOrderNumber(request.getOrderNumber(), id);
        }

        // Update basic fields
        salesOrderMapper.updateEntityFromRequest(request, order);

        // Update customer if changed
        if (!order.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = findCustomerById(request.getCustomerId());
            order.setCustomer(customer);
        }

        // Update warehouse if changed
        if (!order.getWarehouse().getId().equals(request.getWarehouseId())) {
            Warehouse warehouse = findWarehouseById(request.getWarehouseId());
            order.setWarehouse(warehouse);
        }

        // Update addresses
        if (request.getBillingAddressId() != null) {
            CustomerAddress billingAddress = findCustomerAddressById(request.getBillingAddressId());
            order.setBillingAddress(billingAddress);
        } else {
            order.setBillingAddress(null);
        }

        if (request.getShippingAddressId() != null) {
            CustomerAddress shippingAddress = findCustomerAddressById(request.getShippingAddressId());
            order.setShippingAddress(shippingAddress);
        } else {
            order.setShippingAddress(null);
        }

        // Update sales rep
        if (request.getSalesRepId() != null) {
            User salesRep = findUserById(request.getSalesRepId());
            order.setSalesRep(salesRep);
        } else {
            order.setSalesRep(null);
        }

        // Delete existing items and create new ones
        salesOrderItemRepository.deleteAll(order.getItems());
        order.getItems().clear();

        List<SalesOrderItem> newItems = new ArrayList<>();
        for (SalesOrderItemRequest itemRequest : request.getItems()) {
            SalesOrderItem item = createOrderItem(itemRequest);
            item.setOrder(order);
            newItems.add(item);
        }
        order.setItems(newItems);

        SalesOrder updatedOrder = salesOrderRepository.save(order);
        log.info("Sales Order updated successfully: {}", updatedOrder.getOrderNumber());

        return salesOrderMapper.toResponse(updatedOrder);
    }

    private SalesOrderItem createOrderItem(SalesOrderItemRequest itemRequest) {
        SalesOrderItem item = salesOrderItemMapper.toEntity(itemRequest);

        // Verify product exists
        Product product = findProductById(itemRequest.getProductId());
        item.setProduct(product);

        // Verify unit of measure exists
//        UnitOfMeasure uom = findUnitOfMeasureById(itemRequest.getUnitOfMeasureId());
//        item.setUnitOfMeasure(uom);

        // Verify tax rate exists if provided
        if (itemRequest.getTaxRateId() != null) {
            TaxRate taxRate = findTaxRateById(itemRequest.getTaxRateId());
            item.setTaxRate(taxRate);
        }

        return item;
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }



    @Override
    @Transactional
    public SalesOrder confirmSalesOrder(Long id) {
        log.info("Confirming Sales Order: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"DRAFT".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot confirm Sales Order. Current status: " + order.getStatus() + 
                ". Only DRAFT orders can be confirmed.");
        }

        order.setStatus("CONFIRMED");
        salesOrderRepository.save(order);

        log.info("Sales Order confirmed successfully: {}", id);
        return order;
    }

    @Transactional
    public SalesOrder processSalesOrder(Long id){
        log.info("Processing Sales Order: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"APPROVED".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot process Sales Order. Current status: " + order.getStatus() +
                ". Only APPROVED orders can be processed.");
        }

        order.setStatus("PROCESSING");
        salesOrderRepository.save(order);

        log.info("Sales Order processed successfully: {}", id);
        return order;
    }

    public SalesOrder completeSalesOrder(Long id){
        log.info("Completing Sales Order: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"DISPATCHED".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot complete Sales Order. Current status: " + order.getStatus() +
                ". Only DISPATCHED orders can be completed.");
        }

        order.setStatus("DELIVERED");
        salesOrderRepository.save(order);

        log.info("Sales Order completed successfully: {}", id);
        return order;
    }

    @Override
    @Transactional
    public void submitForApproval(Long id) {
        log.info("Submitting Sales Order for approval: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"CONFIRMED".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot submit for approval. Current status: " + order.getStatus() + 
                ". Only CONFIRMED orders can be submitted for approval.");
        }

        order.setStatus("PENDING_APPROVAL");
        salesOrderRepository.save(order);

        log.info("Sales Order submitted for approval successfully: {}", id);
    }

    @Override
    @Transactional
    public SalesOrder approveSalesOrder(Long id, Long approvedBy,String approvalNotes) {
        log.info("Approving Sales Order: {} by user: {}", id, approvedBy);

        SalesOrder order = findSalesOrderById(id);

        if (!"PENDING_APPROVAL".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot approve Sales Order. Current status: " + order.getStatus() + 
                ". Only PENDING_APPROVAL orders can be approved.");
        }

        order.setStatus("APPROVED");
        User approvedUser=userRepository.findById(approvedBy).orElseThrow(()->new ResourceNotFoundException("User not found: "+approvedBy));
        order.setApprovedBy(approvedUser);
        order.setApprovedAt(LocalDateTime.now());
        salesOrderRepository.save(order);

        log.info("Sales Order approved successfully: {}", id);
        return order;
    }

    @Override
    @Transactional
    public void startProcessing(Long id) {
        log.info("Starting processing for Sales Order: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"APPROVED".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot start processing. Current status: " + order.getStatus() + 
                ". Only APPROVED orders can be processed.");
        }

        order.setStatus("PROCESSING");
        salesOrderRepository.save(order);

        log.info("Sales Order processing started successfully: {}", id);
    }

    @Override
    @Transactional
    public void markAsPacked(Long id) {
        log.info("Marking Sales Order as PACKED: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"PROCESSING".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as packed. Current status: " + order.getStatus() + 
                ". Only PROCESSING orders can be marked as packed.");
        }

        order.setStatus("PACKED");
        salesOrderRepository.save(order);

        log.info("Sales Order marked as PACKED successfully: {}", id);
    }

    @Override
    @Transactional
    public SalesOrder markAsDispatched(Long id) {
        log.info("Marking Sales Order as DISPATCHED: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"PACKED".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as dispatched. Current status: " + order.getStatus() + 
                ". Only PACKED orders can be marked as dispatched.");
        }

        order.setStatus("DISPATCHED");
        salesOrderRepository.save(order);

        log.info("Sales Order marked as DISPATCHED successfully: {}", id);
        return order;
    }
    @Transactional
    public SalesOrder updateDeliveryStatus(Long id, String deliveryStatus){
        log.info("Updating delivery status for Sales Order: {} to {}", id, deliveryStatus);

        SalesOrder order = findSalesOrderById(id);

        order.setDeliveryStatus(deliveryStatus);
        salesOrderRepository.save(order);

        log.info("Sales Order delivery status updated successfully: {}", id);
        return order;
    }

    public SalesOrder markAsInvoiced(Long id,Long invoiceId){
        log.info("Marking Sales Order as INVOICED: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"DELIVERED".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as invoiced. Current status: " + order.getStatus() +
                ". Only DELIVERED orders can be marked as invoiced.");
        }

        order.setStatus("INVOICED");
//        order.setInvoiceId(invoiceId);
        salesOrderRepository.save(order);

        log.info("Sales Order marked as INVOICED successfully: {}", id);
        return order;
    }

    @Transactional
    public SalesOrder updatePaymentStatus(Long id,String paymentStatus){
        log.info("Updating payment status for Sales Order: {} to {}", id, paymentStatus);

        SalesOrder order = findSalesOrderById(id);

        order.setPaymentStatus(paymentStatus);
        salesOrderRepository.save(order);

        log.info("Sales Order payment status updated successfully: {}", id);
        return order;
    }

    @Transactional
    public SalesOrder markAsPaid(Long id){
        log.info("Marking Sales Order as PAID: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"INVOICED".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as paid. Current status: " + order.getStatus() +
                ". Only INVOICED orders can be marked as paid.");
        }

        order.setStatus("PAID");
        salesOrderRepository.save(order);

        log.info("Sales Order marked as PAID successfully: {}", id);
        return order;
    }

    @Transactional
    public List<SalesOrder> getDraftOrders(){
        log.info("Retrieving DRAFT Sales Orders");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("DRAFT");

        log.info("Retrieved {} DRAFT Sales Orders", orders.size());
        return orders;
    }

    @Transactional
    public  List<SalesOrder>getPendingOrders(){
        log.info("Retrieving PENDING_APPROVAL Sales Orders");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("PENDING_APPROVAL");

        log.info("Retrieved {} PENDING_APPROVAL Sales Orders", orders.size());
        return orders;
    }
    @Transactional
    public  List<SalesOrder> getConfirmedOrders(){
        log.info("Retrieving CONFIRMED Sales Orders");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("CONFIRMED");

        log.info("Retrieved {} CONFIRMED Sales Orders", orders.size());
        return orders;
    }

    @Transactional
    public List<SalesOrder> getProcessingOrders(){
        log.info("Retrieving PROCESSING Sales Orders");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("PROCESSING");

        log.info("Retrieved {} PROCESSING Sales Orders", orders.size());
        return orders;
    }


    @Override
    @Transactional
    public void markAsDelivered(Long id) {
        log.info("Marking Sales Order as DELIVERED: {}", id);

        SalesOrder order = findSalesOrderById(id);

        if (!"DISPATCHED".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as delivered. Current status: " + order.getStatus() + 
                ". Only DISPATCHED orders can be marked as delivered.");
        }

        order.setStatus("DELIVERED");
        salesOrderRepository.save(order);

        log.info("Sales Order marked as DELIVERED successfully: {}", id);
    }

    @Override
    @Transactional
    public SalesOrder cancelSalesOrder(Long id, String reason) {
        log.info("Cancelling Sales Order: {} with reason: {}", id, reason);

        SalesOrder order = findSalesOrderById(id);

        if ("CANCELLED".equals(order.getStatus())) {
            throw new InvalidOperationException("Sales Order is already cancelled.");
        }

        if ("DELIVERED".equals(order.getStatus())) {
            throw new InvalidOperationException("Cannot cancel a delivered order.");
        }

        order.setStatus("CANCELLED");
        order.setRemarks(order.getRemarks() != null ? 
            order.getRemarks() + "\nCancelled: " + reason : 
            "Cancelled: " + reason);
        salesOrderRepository.save(order);

        log.info("Sales Order cancelled successfully: {}", id);
        return order;
    }

    @Transactional
    public List<SalesOrder> getCompletedOrders(){
        log.info("Retrieving COMPLETED Sales Orders");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("DELIVERED");

        log.info("Retrieved {} COMPLETED Sales Orders", orders.size());
        return orders;
    }

    @Transactional
    public List<SalesOrder> getCancelledOrders(){
        log.info("Retrieving CANCELLED Sales Orders");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("CANCELLED");

        log.info("Retrieved {} CANCELLED Sales Orders", orders.size());
        return orders;
    }

    @Transactional
    public List<SalesOrder> getOrdersPendingApproval(){
        log.info("Retrieving Sales Orders pending approval");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("PENDING_APPROVAL");

        log.info("Retrieved {} Sales Orders pending approval", orders.size());
        return orders;
    }

    @Transactional
    public List<SalesOrder>  getOrdersPendingDispatch(){
        log.info("Retrieving Sales Orders pending dispatch");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("PACKED");

        log.info("Retrieved {} Sales Orders pending dispatch", orders.size());
        return orders;
    }

    @Transactional
    public List<SalesOrder> getOrdersPendingInvoicing(){
        log.info("Retrieving Sales Orders pending invoicing");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("DELIVERED");

        log.info("Retrieved {} Sales Orders pending invoicing", orders.size());
        return orders;
    }

    @Transactional
    public List<SalesOrder> getUnpaidOrders(){
        log.info("Retrieving UNPAID Sales Orders");

        List<SalesOrder> orders = salesOrderRepository.findByStatus("INVOICED");

        log.info("Retrieved {} UNPAID Sales Orders", orders.size());
        return orders;
    }

    @Transactional
    public List<SalesOrder> getOverdueDeliveries(){
        log.info("Retrieving Sales Orders with overdue deliveries");

        List<SalesOrder> orders = salesOrderRepository.findOverdueOrders(LocalDate.now());

        log.info("Retrieved {} Sales Orders with overdue deliveries", orders.size());
        return orders;
    }

    @Transactional
    public List<SalesOrder> getHighPriorityOrders(){
        log.info("Retrieving HIGH PRIORITY Sales Orders");

        List<SalesOrder> orders = salesOrderRepository.findByPriority("HIGH");

        log.info("Retrieved {} HIGH PRIORITY Sales Orders", orders.size());
        return orders;
    }

    @Transactional
    public  List<SalesOrder> getOrdersRequiringAction(){
        log.info("Retrieving Sales Orders requiring action");

        List<SalesOrder> orders = salesOrderRepository.findOrdersRequiringAction(LocalDate.now());

        log.info("Retrieved {} Sales Orders requiring action", orders.size());
        return orders;
    }

    @Transactional
    public Page<SalesOrder> getOrdersByCustomer(Long customerId,Pageable pageable){
        log.info("Retrieving Sales Orders for customer: {}", customerId);

        Page<SalesOrder> orders = salesOrderRepository.findByCustomerId(customerId, pageable);

        log.info("Retrieved {} Sales Orders for customer: {}", orders.getTotalElements(), customerId);
        return orders;
    }

    @Transactional
    public  List<SalesOrder> getOrdersBySalesRep(Long salesRepId){
        log.info("Retrieving Sales Orders for sales rep: {}", salesRepId);

        List<SalesOrder> orders = salesOrderRepository.findBySalesRepId(salesRepId);

        log.info("Retrieved {} Sales Orders for sales rep: {}", orders.size(), salesRepId);
        return orders;
    }

    @Transactional
    public List<SalesOrder> getOrdersByDateRange(LocalDate startDate,LocalDate endDate){
        log.info("Retrieving Sales Orders from {} to {}", startDate, endDate);

        List<SalesOrder> orders = salesOrderRepository.findByOrderDateBetween(startDate, endDate);

        log.info("Retrieved {} Sales Orders from {} to {}", orders.size(), startDate, endDate);
        return orders;
    }

    @Transactional
    public List<SalesOrder> getCustomerRecentOrders(Long customerId,Pageable limit){
        log.info("Retrieving recent {} Sales Orders for customer: {}", limit, customerId);

        List<SalesOrder> orders = salesOrderRepository.findTopByCustomerIdOrderByOrderDateDesc(customerId, limit);

        log.info("Retrieved {} recent Sales Orders for customer: {}", orders.size(), customerId);
        return orders;
    }

    @Transactional
    public  boolean canConfirmOrder(Long id){
        SalesOrder order = findSalesOrderById(id);
        return "DRAFT".equals(order.getStatus());
    }

    @Transactional
    public boolean canCancelOrder(Long id){
        SalesOrder order = findSalesOrderById(id);
        return !"DELIVERED".equals(order.getStatus()) && !"CANCELLED".equals(order.getStatus());
    }
    @Transactional
    public  boolean canApproveOrder(Long id){
        SalesOrder order = findSalesOrderById(id);
        return "PENDING_APPROVAL".equals(order.getStatus());
    }

    @Transactional
    public double calculateSubtotal(Long id){
        SalesOrder order = findSalesOrderById(id);
        return order.getItems().stream()
            .mapToDouble(item -> item.getUnitPrice().doubleValue() * item.getQuantity())
            .sum();
    }

    @Transactional
    public double calculateTotalTax(Long id){
        SalesOrder order = findSalesOrderById(id);
        return order.getItems().stream()
            .mapToDouble(item -> {
                BigDecimal taxRate = item.getTaxRate() != null ? item.getTaxRate().getRate() : BigDecimal.ZERO;
                return item.getUnitPrice().doubleValue() * item.getQuantity() * taxRate.doubleValue() / 100;
            })
            .sum();
    }

    @Transactional
    public double calculateTotalDiscount(Long id){
        SalesOrder order = findSalesOrderById(id);
        return order.getItems().stream()
            .mapToDouble(item -> {
                BigDecimal discount = item.getDiscount() != null ? item.getDiscount() : BigDecimal.ZERO;
                return discount.doubleValue();
            })
            .sum();
    }

    @Transactional
    public Map<String,Object> getSalesOrderStatistics(){
        log.info("Calculating Sales Order statistics");

        long totalOrders = salesOrderRepository.count();
        long pendingOrders = salesOrderRepository.countByStatus("PENDING_APPROVAL");
        long completedOrders = salesOrderRepository.countByStatus("DELIVERED");

        log.info("Sales Order statistics calculated successfully");

        Map<String,Object> map=new HashMap<>();
        map.put("totalOrders",totalOrders);
        map.put("pendingOrders",pendingOrders);
        map.put("completedOrders",completedOrders);

        return map;
    }

    public  Map<String,Object> getOrderTypeDistribution(){
        log.info("Calculating Sales Order type distribution");

        List<Object[]> distributionData = salesOrderRepository.countOrdersByType();

        Map<String,Object> distributionMap = new HashMap<>();
        for (Object[] data : distributionData) {
            String orderType = (String) data[0];
            Long count = (Long) data[1];
            distributionMap.put(orderType, count);
        }

        log.info("Sales Order type distribution calculated successfully");
        return distributionMap;
    }

    @Transactional
    public List<Map<String,Object>> getDeliveryStatusDistribution(){
        log.info("Calculating Sales Order delivery status distribution");

        List<Object[]> distributionData = salesOrderRepository.countOrdersByDeliveryStatus();

        List<Map<String,Object>> distributionList = new ArrayList<>();
        for (Object[] data : distributionData) {
            String deliveryStatus = (String) data[0];
            Long count = (Long) data[1];
            Map<String,Object> statusMap = new HashMap<>();
            statusMap.put("deliveryStatus", deliveryStatus);
            statusMap.put("count", count);
            distributionList.add(statusMap);
        }

        log.info("Sales Order delivery status distribution calculated successfully");
        return distributionList;
    }

    @Transactional
    public double getTotalOrderValue(){
        log.info("Calculating total Sales Order value");

        Double totalValue = salesOrderRepository.sumTotalOrderValue()
            .orElse(0.0);

        log.info("Total Sales Order value calculated successfully: {}", totalValue);
        return totalValue;
    }

    @Transactional
    public double getAverageOrderValue(){
        log.info("Calculating average Sales Order value");

        Double averageValue = salesOrderRepository.averageOrderValue()
            .orElse(0.0);

        log.info("Average Sales Order value calculated successfully: {}", averageValue);
        return averageValue;
    }

    @Transactional
    public Map<String,Object> getDashboardStatistics(){
        log.info("Calculating Sales Order dashboard statistics");

        long totalOrders = salesOrderRepository.count();
        long pendingOrders = salesOrderRepository.countByStatus("PENDING_APPROVAL");
        double totalSalesValue = salesOrderRepository.sumTotalOrderValue()
            .orElse(0.0);
        double averageOrderValue = salesOrderRepository.averageOrderValue()
            .orElse(0.0);

        Map<String,Object> statsMap = new HashMap<>();
        statsMap.put("totalOrders", totalOrders);
        statsMap.put("pendingOrders", pendingOrders);
        statsMap.put("totalSalesValue", totalSalesValue);
        statsMap.put("averageOrderValue", averageOrderValue);

        log.info("Sales Order dashboard statistics calculated successfully");
        return statsMap;
    }

    @Transactional
    public List<SalesOrder> getOrdersByCustomer(Long customerId){
        log.info("Retrieving Sales Orders for customer: {}", customerId);

        List<SalesOrder> orders = salesOrderRepository.findByCustomerId(customerId);

        log.info("Retrieved {} Sales Orders for customer: {}", orders.size(), customerId);
        return orders;
    }

    @Transactional
    public SalesOrder rejectSalesOrder(Long id, String rejectionReason){
        log.info("Rejecting Sales Order: {} with reason: {}", id, rejectionReason);

        SalesOrder order = findSalesOrderById(id);

        if (!"PENDING_APPROVAL".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot reject Sales Order. Current status: " + order.getStatus() +
                ". Only PENDING_APPROVAL orders can be rejected.");
        }

        order.setStatus("DRAFT");
        order.setRemarks(order.getRemarks() != null ?
            order.getRemarks() + "\nRejection: " + rejectionReason :
            "Rejection: " + rejectionReason);
        salesOrderRepository.save(order);

        log.info("Sales Order rejected successfully: {}", id);
        return order;
    }

    @Override
    @Transactional
    public void deleteSalesOrder(Long id) {
        log.info("Deleting Sales Order: {}", id);

        if (!canDelete(id)) {
            SalesOrder order = findSalesOrderById(id);
            throw new InvalidOperationException(
                "Cannot delete Sales Order. Current status: " + order.getStatus() + 
                ". Only DRAFT orders can be deleted.");
        }

        salesOrderRepository.deleteById(id);
        log.info("Sales Order deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void assignSalesRep(Long orderId, Long salesRepId) {
        log.info("Assigning sales rep {} to order {}", salesRepId, orderId);

        SalesOrder order = findSalesOrderById(orderId);
        User salesRep = findUserById(salesRepId);

        order.setSalesRep(salesRep);
        salesOrderRepository.save(order);

        log.info("Sales rep assigned successfully");
    }

    @Override
    public SalesOrderResponse getSalesOrderById(Long id) {
        SalesOrder order = findSalesOrderById(id);
        return salesOrderMapper.toResponse(order);
    }

    @Override
    public SalesOrderResponse getSalesOrderByNumber(String orderNumber) {
        SalesOrder order = salesOrderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found: " + orderNumber));
        return salesOrderMapper.toResponse(order);
    }

    @Override
    public PageResponse<SalesOrderResponse> getAllSalesOrders(Pageable pageable) {
        Page<SalesOrder> orderPage = salesOrderRepository.findAll(pageable);
        return createPageResponse(orderPage);
    }

    @Override
    public PageResponse<SalesOrderResponse> getSalesOrdersByStatus(String status, Pageable pageable) {
        Page<SalesOrder> orderPage = salesOrderRepository.findByStatus(status, pageable);
        return createPageResponse(orderPage);
    }

    @Override
    public List<SalesOrderResponse> getSalesOrdersByCustomer(Long customerId) {
        List<SalesOrder> orders = salesOrderRepository.findByCustomerId(customerId);
        return orders.stream()
            .map(salesOrderMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SalesOrderResponse> getSalesOrdersBySalesRep(Long salesRepId) {
        List<SalesOrder> orders = salesOrderRepository.findBySalesRepId(salesRepId);
        return orders.stream()
            .map(salesOrderMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SalesOrderResponse> getSalesOrdersByWarehouse(Long warehouseId) {
        List<SalesOrder> orders = salesOrderRepository.findByWarehouseId(warehouseId);
        return orders.stream()
            .map(salesOrderMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SalesOrderResponse> getSalesOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        List<SalesOrder> orders = salesOrderRepository.findByOrderDateBetween(startDate, endDate);
        return orders.stream()
            .map(salesOrderMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get Sales Orders by order type
     *
     * @param orderType
     * @param pageable
     */
    @Override
    public PageResponse<SalesOrderResponse> getSalesOrdersByOrderType(String orderType, Pageable pageable) {
        return null;
    }

//    @Override
//    public PageResponse<SalesOrderResponse> getSalesOrdersByOrderType(String orderType, Pageable pageable) {
//        Page<SalesOrder> orderPage = salesOrderRepository.findByOrderType(orderType);
//        return createPageResponse(orderPage);
//    }

    @Override
    public List<SalesOrderResponse> getPendingApprovalOrders() {
        List<SalesOrder> orders = salesOrderRepository.findByStatus("PENDING_APPROVAL");
        return orders.stream()
            .map(salesOrderMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SalesOrderResponse> getOverdueOrders() {
        List<SalesOrder> orders = salesOrderRepository.findOverdueOrders(LocalDate.now());
        return orders.stream()
            .map(salesOrderMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SalesOrderResponse> searchSalesOrders(String keyword, Pageable pageable) {
        Page<SalesOrder> orderPage = salesOrderRepository.searchOrders(keyword,null,null,null,null,null,null,pageable);
        return createPageResponse(orderPage);
    }

    /**
     * Get total sales amount for a customer
     *
     * @param customerId
     */
    @Override
    public BigDecimal getTotalSalesByCustomer(Long customerId) {
        return null;
    }

    /**
     * Get total sales amount for a date range
     *
     * @param startDate
     * @param endDate
     */
    @Override
    public BigDecimal getTotalSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return null;
    }

//    @Override
//    public BigDecimal getTotalSalesByCustomer(Long customerId) {
//        return salesOrderRepository.sumTotalAmountByCustomer(customerId)
//            .orElse(BigDecimal.ZERO);
//    }

//    @Override
//    public BigDecimal getTotalSalesByDateRange(LocalDate startDate, LocalDate endDate) {
//        return salesOrderRepository.sumTotalAmountByDateRange(startDate, endDate)
//            .orElse(BigDecimal.ZERO);
//    }

    @Override
    public boolean canDelete(Long id) {
        SalesOrder order = findSalesOrderById(id);
        return "DRAFT".equals(order.getStatus());
    }

    @Override
    public boolean canUpdate(Long id) {
        SalesOrder order = findSalesOrderById(id);
        return "DRAFT".equals(order.getStatus());
    }

    // ==================== PRIVATE HELPER METHODS ====================

//    private SalesOrderItem createOrderItem(SalesOrderItemRequest request) {
//        Product product = findProductById(request.getProductId());
//        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());
//
//        SalesOrderItem item = salesOrderItemMapper.toEntity(request);
//        item.setProduct(product);
//        item.setUom(uom);
//
//        if (request.getTaxRateId() != null) {
//            TaxRate taxRate = findTaxRateById(request.getTaxRateId());
//            item.setTaxRate(taxRate);
//        }
//
//        return item;
//    }

    private void validateUniqueOrderNumber(String orderNumber, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = salesOrderRepository.existsByOrderNumberAndIdNot(orderNumber, excludeId);
        } else {
            exists = salesOrderRepository.existsByOrderNumber(orderNumber);
        }

        if (exists) {
            throw new DuplicateResourceException("Sales Order with number '" + orderNumber + "' already exists");
        }
    }

    private SalesOrder findSalesOrderById(Long id) {
        return salesOrderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id).get();
    }

    private CustomerAddress findCustomerAddressById(Long id) {
        return customerAddressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer Address not found: " + id));
    }

    private Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }

//    private Product findProductById(Long id) {
//        return productRepository.findByIdAndDeletedAtIsNull(id)
//            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
//    }

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of Measure not found: " + id));
    }

    private TaxRate findTaxRateById(Long id) {
        return taxRateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tax Rate not found: " + id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private PageResponse<SalesOrderResponse> createPageResponse(Page<SalesOrder> orderPage) {
        List<SalesOrderResponse> content = orderPage.getContent().stream()
            .map(salesOrderMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<SalesOrderResponse>builder()
            .content(content)
            .pageNumber(orderPage.getNumber())
            .pageSize(orderPage.getSize())
            .totalElements(orderPage.getTotalElements())
            .totalPages(orderPage.getTotalPages())
            .last(orderPage.isLast())
            .first(orderPage.isFirst())
            .empty(orderPage.isEmpty())
            .build();
    }
}
