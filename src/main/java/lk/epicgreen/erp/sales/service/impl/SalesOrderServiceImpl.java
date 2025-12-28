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
import java.util.List;
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

    @Override
    @Transactional
    public void confirmSalesOrder(Long id) {
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
    public void approveSalesOrder(Long id, Long approvedBy) {
        log.info("Approving Sales Order: {} by user: {}", id, approvedBy);

        SalesOrder order = findSalesOrderById(id);

        if (!"PENDING_APPROVAL".equals(order.getStatus())) {
            throw new InvalidOperationException(
                "Cannot approve Sales Order. Current status: " + order.getStatus() + 
                ". Only PENDING_APPROVAL orders can be approved.");
        }

        order.setStatus("APPROVED");
        order.setApprovedBy(approvedBy);
        order.setApprovedAt(LocalDateTime.now());
        salesOrderRepository.save(order);

        log.info("Sales Order approved successfully: {}", id);
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
    public void markAsDispatched(Long id) {
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
    public void cancelSalesOrder(Long id, String reason) {
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

    @Override
    public PageResponse<SalesOrderResponse> getSalesOrdersByOrderType(String orderType, Pageable pageable) {
        Page<SalesOrder> orderPage = salesOrderRepository.findByOrderType(orderType, pageable);
        return createPageResponse(orderPage);
    }

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
        Page<SalesOrder> orderPage = salesOrderRepository.searchOrders(keyword, pageable);
        return createPageResponse(orderPage);
    }

    @Override
    public BigDecimal getTotalSalesByCustomer(Long customerId) {
        return salesOrderRepository.sumTotalAmountByCustomer(customerId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesOrderRepository.sumTotalAmountByDateRange(startDate, endDate)
            .orElse(BigDecimal.ZERO);
    }

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

    private SalesOrderItem createOrderItem(SalesOrderItemRequest request) {
        Product product = findProductById(request.getProductId());
        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());

        SalesOrderItem item = salesOrderItemMapper.toEntity(request);
        item.setProduct(product);
        item.setUom(uom);

        if (request.getTaxRateId() != null) {
            TaxRate taxRate = findTaxRateById(request.getTaxRateId());
            item.setTaxRate(taxRate);
        }

        return item;
    }

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
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private CustomerAddress findCustomerAddressById(Long id) {
        return customerAddressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer Address not found: " + id));
    }

    private Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

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
