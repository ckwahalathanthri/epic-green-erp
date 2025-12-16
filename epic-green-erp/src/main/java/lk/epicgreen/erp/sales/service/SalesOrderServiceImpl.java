package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.SalesOrderRequest;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.sales.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SalesOrder Service Implementation
 * Implementation of sales order service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {
    
    private final SalesOrderRepository salesOrderRepository;
    
    @Override
    public SalesOrder createSalesOrder(SalesOrderRequest request) {
        log.info("Creating sales order for customer: {}", request.getCustomerId());
        
        SalesOrder order = new SalesOrder();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomerId(request.getCustomerId());
        order.setCustomerName(request.getCustomerName());
        order.setOrderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setOrderType(request.getOrderType());
        order.setPriority(request.getPriority() != null ? request.getPriority() : "NORMAL");
        order.setStatus("DRAFT");
        order.setDeliveryStatus("PENDING");
        order.setPaymentStatus("UNPAID");
        order.setPaymentTerms(request.getPaymentTerms());
        order.setSalesRepId(request.getSalesRepId());
        order.setSalesRepName(request.getSalesRepName());
        order.setIsApproved(false);
        order.setIsDispatched(false);
        order.setIsInvoiced(false);
        order.setIsPaid(false);
        order.setNotes(request.getNotes());
        
        // Calculate totals
        order.setSubtotalAmount(request.getSubtotalAmount() != null ? request.getSubtotalAmount() : 0.0);
        order.setTaxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : 0.0);
        order.setDiscountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : 0.0);
        order.setTotalAmount(calculateTotal(order));
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder updateSalesOrder(Long id, SalesOrderRequest request) {
        log.info("Updating sales order: {}", id);
        SalesOrder existing = getSalesOrderById(id);
        
        if (existing.getIsApproved()) {
            throw new RuntimeException("Cannot update approved sales order");
        }
        
        existing.setOrderDate(request.getOrderDate());
        existing.setDeliveryDate(request.getDeliveryDate());
        existing.setOrderType(request.getOrderType());
        existing.setPriority(request.getPriority());
        existing.setPaymentTerms(request.getPaymentTerms());
        existing.setNotes(request.getNotes());
        
        // Recalculate totals
        existing.setSubtotalAmount(request.getSubtotalAmount());
        existing.setTaxAmount(request.getTaxAmount());
        existing.setDiscountAmount(request.getDiscountAmount());
        existing.setTotalAmount(calculateTotal(existing));
        
        return salesOrderRepository.save(existing);
    }
    
    @Override
    public void deleteSalesOrder(Long id) {
        log.info("Deleting sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        if (order.getIsApproved() || order.getIsDispatched() || order.getIsInvoiced()) {
            throw new RuntimeException("Cannot delete approved, dispatched or invoiced order");
        }
        
        salesOrderRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SalesOrder getSalesOrderById(Long id) {
        return salesOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sales order not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public SalesOrder getSalesOrderByNumber(String orderNumber) {
        return salesOrderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new RuntimeException("Sales order not found with number: " + orderNumber));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SalesOrder> getAllSalesOrders(Pageable pageable) {
        return salesOrderRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SalesOrder> searchSalesOrders(String keyword, Pageable pageable) {
        return salesOrderRepository.searchSalesOrders(keyword, pageable);
    }
    
    @Override
    public SalesOrder confirmSalesOrder(Long id) {
        log.info("Confirming sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        if (!order.getIsApproved()) {
            throw new RuntimeException("Order must be approved before confirmation");
        }
        
        order.setStatus("CONFIRMED");
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder processSalesOrder(Long id) {
        log.info("Processing sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setStatus("PROCESSING");
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder completeSalesOrder(Long id) {
        log.info("Completing sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        if (!order.getIsDispatched()) {
            throw new RuntimeException("Order must be dispatched before completion");
        }
        
        order.setStatus("COMPLETED");
        order.setDeliveryStatus("DELIVERED");
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder cancelSalesOrder(Long id, String cancellationReason) {
        log.info("Cancelling sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        if (order.getIsDispatched() || order.getIsInvoiced()) {
            throw new RuntimeException("Cannot cancel dispatched or invoiced order");
        }
        
        order.setStatus("CANCELLED");
        order.setCancellationReason(cancellationReason);
        order.setCancelledDate(LocalDate.now());
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder approveSalesOrder(Long id, Long approvedByUserId, String approvalNotes) {
        log.info("Approving sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setIsApproved(true);
        order.setApprovedDate(LocalDate.now());
        order.setApprovedByUserId(approvedByUserId);
        order.setApprovalNotes(approvalNotes);
        order.setStatus("CONFIRMED");
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder rejectSalesOrder(Long id, String rejectionReason) {
        log.info("Rejecting sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setStatus("CANCELLED");
        order.setRejectionReason(rejectionReason);
        order.setRejectedDate(LocalDate.now());
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder markAsDispatched(Long id, Long dispatchNoteId) {
        log.info("Marking sales order as dispatched: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setIsDispatched(true);
        order.setDispatchNoteId(dispatchNoteId);
        order.setDispatchedDate(LocalDate.now());
        order.setDeliveryStatus("DISPATCHED");
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder updateDeliveryStatus(Long id, String deliveryStatus) {
        log.info("Updating delivery status for order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setDeliveryStatus(deliveryStatus);
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder markAsInvoiced(Long id, Long invoiceId) {
        log.info("Marking sales order as invoiced: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setIsInvoiced(true);
        order.setInvoiceId(invoiceId);
        order.setInvoicedDate(LocalDate.now());
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder updatePaymentStatus(Long id, String paymentStatus) {
        log.info("Updating payment status for order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setPaymentStatus(paymentStatus);
        
        if ("PAID".equals(paymentStatus)) {
            order.setIsPaid(true);
            order.setPaidDate(LocalDate.now());
        }
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder markAsPaid(Long id) {
        log.info("Marking sales order as paid: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setIsPaid(true);
        order.setPaidDate(LocalDate.now());
        order.setPaymentStatus("PAID");
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public void calculateOrderTotals(Long orderId) {
        SalesOrder order = getSalesOrderById(orderId);
        order.setTotalAmount(calculateTotal(order));
        salesOrderRepository.save(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateSubtotal(Long orderId) {
        SalesOrder order = getSalesOrderById(orderId);
        return order.getSubtotalAmount();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalTax(Long orderId) {
        SalesOrder order = getSalesOrderById(orderId);
        return order.getTaxAmount();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalDiscount(Long orderId) {
        SalesOrder order = getSalesOrderById(orderId);
        return order.getDiscountAmount();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getDraftOrders() {
        return salesOrderRepository.findDraftOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getPendingOrders() {
        return salesOrderRepository.findPendingOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getConfirmedOrders() {
        return salesOrderRepository.findConfirmedOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getProcessingOrders() {
        return salesOrderRepository.findProcessingOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getCompletedOrders() {
        return salesOrderRepository.findCompletedOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getCancelledOrders() {
        return salesOrderRepository.findCancelledOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getOrdersPendingApproval() {
        return salesOrderRepository.findOrdersPendingApproval();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getOrdersPendingDispatch() {
        return salesOrderRepository.findOrdersPendingDispatch();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getOrdersPendingInvoicing() {
        return salesOrderRepository.findOrdersPendingInvoicing();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getUnpaidOrders() {
        return salesOrderRepository.findUnpaidOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getOverdueDeliveries() {
        return salesOrderRepository.findOverdueDeliveries(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getHighPriorityOrders() {
        return salesOrderRepository.findHighPriorityOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getOrdersRequiringAction() {
        return salesOrderRepository.findOrdersRequiringAction(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getOrdersByCustomer(Long customerId) {
        return salesOrderRepository.findByCustomerId(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<SalesOrder> getOrdersByCustomer(Long customerId, Pageable pageable) {
        return salesOrderRepository.findByCustomerId(customerId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getOrdersBySalesRep(Long salesRepId) {
        return salesOrderRepository.findBySalesRepId(salesRepId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesOrderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getRecentOrders(int limit) {
        return salesOrderRepository.findRecentOrders(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getCustomerRecentOrders(Long customerId, int limit) {
        return salesOrderRepository.findCustomerRecentOrders(customerId, PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateSalesOrder(SalesOrder order) {
        return order.getCustomerId() != null &&
               order.getOrderDate() != null &&
               order.getTotalAmount() != null &&
               order.getTotalAmount() > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canConfirmOrder(Long orderId) {
        SalesOrder order = getSalesOrderById(orderId);
        return order.getIsApproved() && "PENDING".equals(order.getStatus());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canCancelOrder(Long orderId) {
        SalesOrder order = getSalesOrderById(orderId);
        return !order.getIsDispatched() && !order.getIsInvoiced();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canApproveOrder(Long orderId) {
        SalesOrder order = getSalesOrderById(orderId);
        return !order.getIsApproved() && !"CANCELLED".equals(order.getStatus());
    }
    
    @Override
    public List<SalesOrder> createBulkSalesOrders(List<SalesOrderRequest> requests) {
        return requests.stream()
            .map(this::createSalesOrder)
            .collect(Collectors.toList());
    }
    
    @Override
    public int approveBulkSalesOrders(List<Long> orderIds, Long approvedByUserId) {
        int count = 0;
        for (Long id : orderIds) {
            try {
                approveSalesOrder(id, approvedByUserId, "Bulk approval");
                count++;
            } catch (Exception e) {
                log.error("Error approving order: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkSalesOrders(List<Long> orderIds) {
        int count = 0;
        for (Long id : orderIds) {
            try {
                deleteSalesOrder(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting order: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSalesOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalOrders", salesOrderRepository.count());
        stats.put("pendingOrders", salesOrderRepository.countPendingOrders());
        stats.put("ordersPendingApproval", salesOrderRepository.countOrdersPendingApproval());
        stats.put("ordersPendingDispatch", salesOrderRepository.countOrdersPendingDispatch());
        stats.put("unpaidOrders", salesOrderRepository.countUnpaidOrders());
        stats.put("totalOrderValue", getTotalOrderValue());
        stats.put("averageOrderValue", getAverageOrderValue());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getOrderTypeDistribution() {
        List<Object[]> results = salesOrderRepository.getOrderTypeDistribution();
        return convertToMapList(results, "orderType", "orderCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStatusDistribution() {
        List<Object[]> results = salesOrderRepository.getStatusDistribution();
        return convertToMapList(results, "status", "orderCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPriorityDistribution() {
        List<Object[]> results = salesOrderRepository.getPriorityDistribution();
        return convertToMapList(results, "priority", "orderCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDeliveryStatusDistribution() {
        List<Object[]> results = salesOrderRepository.getDeliveryStatusDistribution();
        return convertToMapList(results, "deliveryStatus", "orderCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentStatusDistribution() {
        List<Object[]> results = salesOrderRepository.getPaymentStatusDistribution();
        return convertToMapList(results, "paymentStatus", "orderCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyOrderCount(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = salesOrderRepository.getMonthlyOrderCount(startDate, endDate);
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("year", result[0]);
                map.put("month", result[1]);
                map.put("orderCount", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTotalOrderValueByCustomer() {
        List<Object[]> results = salesOrderRepository.getTotalOrderValueByCustomer();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("customerId", result[0]);
                map.put("customerName", result[1]);
                map.put("totalValue", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTotalOrderValueBySalesRep() {
        List<Object[]> results = salesOrderRepository.getTotalOrderValueBySalesRep();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("salesRepId", result[0]);
                map.put("salesRepName", result[1]);
                map.put("totalValue", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTopCustomers(int limit) {
        List<Object[]> results = salesOrderRepository.getTopCustomers(PageRequest.of(0, limit));
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("customerId", result[0]);
                map.put("customerName", result[1]);
                map.put("orderCount", result[2]);
                map.put("totalValue", result[3]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalOrderValue() {
        Double total = salesOrderRepository.getTotalOrderValue();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageOrderValue() {
        Double average = salesOrderRepository.getAverageOrderValue();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getSalesOrderStatistics());
        dashboard.put("typeDistribution", getOrderTypeDistribution());
        dashboard.put("statusDistribution", getStatusDistribution());
        dashboard.put("priorityDistribution", getPriorityDistribution());
        
        return dashboard;
    }
    
    private String generateOrderNumber() {
        return "SO-" + System.currentTimeMillis();
    }
    
    private Double calculateTotal(SalesOrder order) {
        return order.getSubtotalAmount() + order.getTaxAmount() - order.getDiscountAmount();
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
