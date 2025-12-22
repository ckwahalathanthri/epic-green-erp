package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.SalesOrderRequest;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.sales.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SalesOrder Service Implementation
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public abstract class SalesOrderServiceImpl implements SalesOrderService {
    
    private final SalesOrderRepository salesOrderRepository;
    
    @Override
    public SalesOrder createSalesOrder(SalesOrderRequest request) {
        log.info("Creating sales order for customer: {}", request.getCustomerId());
        
        SalesOrder order = new SalesOrder();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomerId(request.getCustomerId());
        order.setCustomerName(request.getCustomerName());
        
        order.setOrderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now());
        order.setDeliveryDate(request.getDeliveryDate() != null ? request.getDeliveryDate() : LocalDate.now().plusDays(7));
        order.setRequiredDate(request.getRequiredDate());
        order.setOrderType(request.getOrderType());
        order.setPriority(request.getPriority() != null ? request.getPriority() : "NORMAL");
        order.setStatus("PENDING");
        order.setDeliveryStatus("PENDING");
        order.setPaymentStatus("UNPAID");
        
        // Sales rep info
        order.setSalesRepId(request.getSalesRepId());
        order.setSalesRepName(request.getSalesRepName());
        
        // Boolean flags - use setters with "is" prefix
        order.setIsApproved(false);
        order.setIsDispatched(false);
        order.setIsInvoiced(false);
        order.setIsPaid(false);
        
        // Calculate amounts - proper BigDecimal handling
        BigDecimal subtotal = request.getSubtotalAmount() != null ? 
            request.getSubtotalAmount() : BigDecimal.ZERO;
        BigDecimal tax = request.getTaxAmount() != null ? 
            request.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscountAmount() != null ? 
            request.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal shipping = request.getShippingAmount() != null ? 
            request.getShippingAmount() : BigDecimal.ZERO;
        
        order.setSubtotalAmount(subtotal);
        order.setTaxAmount(tax);
        order.setDiscountAmount(discount);
        order.setShippingAmount(shipping);
        order.setTotalAmount(calculateTotal(request));
        
        order.setNotes(request.getNotes());
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder updateSalesOrder(Long id, SalesOrderRequest request) {
        log.info("Updating sales order: {}", id);
        SalesOrder existing = getSalesOrderById(id);
        
        // Only allow updates if not approved
        if (existing.getIsApproved()) {
            throw new RuntimeException("Cannot update approved sales order");
        }
        
        existing.setOrderDate(request.getOrderDate());
        existing.setDeliveryDate(request.getDeliveryDate());
        existing.setRequiredDate(request.getRequiredDate());
        existing.setOrderType(request.getOrderType());
        existing.setPriority(request.getPriority());
        existing.setNotes(request.getNotes());
        existing.setShippingAddress(request.getShippingAddress());
        existing.setBillingAddress(request.getBillingAddress());
        
        // Update amounts
        BigDecimal subtotal = request.getSubtotalAmount() != null ? 
            request.getSubtotalAmount() : BigDecimal.ZERO;
        BigDecimal tax = request.getTaxAmount() != null ? 
            request.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscountAmount() != null ? 
            request.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal shipping = request.getShippingAmount() != null ? 
            request.getShippingAmount() : BigDecimal.ZERO;
        
        existing.setSubtotalAmount(subtotal);
        existing.setTaxAmount(tax);
        existing.setDiscountAmount(discount);
        existing.setShippingAmount(shipping);
        existing.setTotalAmount(calculateTotal(request));
        
        return salesOrderRepository.save(existing);
    }
    
    @Override
    public void deleteSalesOrder(Long id) {
        log.info("Deleting sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        if (order.getIsApproved() || order.getIsDispatched() || order.getIsInvoiced()) {
            throw new RuntimeException("Cannot delete approved, dispatched, or invoiced sales order");
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
    
    @Transactional(readOnly = true)
    public Page<SalesOrder> searchSalesOrders(String searchTerm, Pageable pageable) {
        return salesOrderRepository.searchSalesOrders(searchTerm, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<SalesOrder> getSalesOrdersByCustomer(Long customerId) {
        return salesOrderRepository.findByCustomerId(customerId);
    }
    
    @Transactional(readOnly = true)
    public List<SalesOrder> getSalesOrdersByStatus(String status) {
        return salesOrderRepository.findByStatus(status);
    }
    
    @Override
    public SalesOrder approveSalesOrder(Long id, Long approvedByUserId, String notes) {
        log.info("Approving sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        if (order.getIsApproved()) {
            throw new RuntimeException("Sales order already approved");
        }
        
        order.setIsApproved(true);
        order.setStatus("APPROVED");
        order.setApprovalDate(LocalDate.now());
        order.setApprovedBy(String.valueOf(approvedByUserId));
        order.setApprovalNotes(notes);
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder rejectSalesOrder(Long id, String reason) {
        log.info("Rejecting sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setStatus("REJECTED");
        order.setRejectionReason(reason);
        order.setRejectedDate(LocalDate.now());
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder dispatchSalesOrder(Long id, Long dispatchNoteId) {
        log.info("Dispatching sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        if (!order.getIsApproved()) {
            throw new RuntimeException("Cannot dispatch unapproved sales order");
        }
        
        order.setIsDispatched(true);
        order.setDispatchNoteId(dispatchNoteId);
        order.setDispatchedDate(LocalDate.now());
        order.setDeliveryStatus("DISPATCHED");
        order.setStatus("DISPATCHED");
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder markAsDelivered(Long id) {
        log.info("Marking sales order as delivered: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setDeliveryStatus("DELIVERED");
        order.setStatus("DELIVERED");
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder generateInvoice(Long id, Long invoiceId) {
        log.info("Generating invoice for sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        order.setIsInvoiced(true);
        order.setInvoiceId(invoiceId);
        order.setInvoicedDate(LocalDate.now());
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder updatePaymentStatus(Long salesOrderId, String paymentStatus) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        order.setPaymentStatus(paymentStatus);
        
        if ("PAID".equals(paymentStatus)) {
            order.setIsPaid(true);
            order.setPaidDate(LocalDate.now());
        }
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public SalesOrder markAsPaid(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        order.setIsPaid(true);
        order.setPaidDate(LocalDate.now());
        order.setPaymentStatus("PAID");
        order.setPaidAmount(order.getTotalAmount());
        
        return salesOrderRepository.save(order);
    }
    
    @Override
    public void recordPayment(Long salesOrderId, Double paidAmount) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        BigDecimal payment = BigDecimal.valueOf(paidAmount != null ? paidAmount : 0.0);
        BigDecimal currentPaid = order.getPaidAmount() != null ? order.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal newPaidAmount = currentPaid.add(payment);
        
        order.setPaidAmount(newPaidAmount);
        
        if (newPaidAmount.compareTo(order.getTotalAmount()) >= 0) {
            order.setPaymentStatus("PAID");
            order.setIsPaid(true);
            order.setPaidDate(LocalDate.now());
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            order.setPaymentStatus("PARTIAL");
        }
        
        salesOrderRepository.save(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateSubtotal(SalesOrder order) {
        BigDecimal subtotal = order.getSubtotalAmount();
        return subtotal != null ? subtotal.doubleValue() : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalTax(SalesOrder order) {
        BigDecimal tax = order.getTaxAmount();
        return tax != null ? tax.doubleValue() : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalDiscount(SalesOrder order) {
        BigDecimal discount = order.getDiscountAmount();
        return discount != null ? discount.doubleValue() : 0.0;
    }
    
    @Override
    public SalesOrder cancelSalesOrder(Long id, String reason) {
        log.info("Cancelling sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);
        
        if (order.getIsDispatched() && order.getIsInvoiced()) {
            throw new RuntimeException("Cannot cancel dispatched and invoiced sales order");
        }
        
        order.setCancellationReason(reason);
        order.setCancelledDate(LocalDate.now());
        order.setStatus("CANCELLED");
        
        return salesOrderRepository.save(order);
    }
    
    @Transactional(readOnly = true)
    public List<SalesOrder> getPendingSalesOrders() {
        // Manual filtering if repository method doesn't exist
        return salesOrderRepository.findAll().stream()
            .filter(order -> "PENDING".equals(order.getStatus()))
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SalesOrder> getApprovedSalesOrders() {
        // Manual filtering if repository method doesn't exist
        return salesOrderRepository.findAll().stream()
            .filter(SalesOrder::getIsApproved)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SalesOrder> getDispatchedSalesOrders() {
        // Manual filtering if repository method doesn't exist
        return salesOrderRepository.findAll().stream()
            .filter(SalesOrder::getIsDispatched)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SalesOrder> getInvoicedSalesOrders() {
        // Manual filtering if repository method doesn't exist
        return salesOrderRepository.findAll().stream()
            .filter(SalesOrder::getIsInvoiced)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SalesOrder> getRecentSalesOrders(int limit) {
        // Manual sorting and limiting if repository method doesn't exist
        return salesOrderRepository.findAll().stream()
            .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Long countSalesOrdersByStatus(String status) {
        return salesOrderRepository.countByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public Long countSalesOrdersByCustomer(Long customerId) {
        return salesOrderRepository.countByCustomerId(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateSalesOrder(SalesOrder order) {
        // Validate required fields
        Long customerId = order.getCustomerId();
        BigDecimal total = order.getTotalAmount();
        
        return customerId != null &&
               order.getOrderDate() != null &&
               total != null &&
               total.compareTo(BigDecimal.ZERO) > 0;
    }
    
    @Transactional(readOnly = true)
    public boolean canApproveSalesOrder(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        return !order.getIsApproved() && !"CANCELLED".equals(order.getStatus());
    }
    
    @Transactional(readOnly = true)
    public boolean canCancelSalesOrder(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        return !(order.getIsDispatched() && order.getIsInvoiced());
    }
    
    @Transactional(readOnly = true)
    public boolean canDispatchSalesOrder(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        return order.getIsApproved() && !order.getIsDispatched();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSalesOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", salesOrderRepository.count());
        stats.put("pending", countSalesOrdersByStatus("PENDING"));
        stats.put("approved", countSalesOrdersByStatus("APPROVED"));
        stats.put("dispatched", countSalesOrdersByStatus("DISPATCHED"));
        stats.put("delivered", countSalesOrdersByStatus("DELIVERED"));
        stats.put("cancelled", countSalesOrdersByStatus("CANCELLED"));
        return stats;
    }
    
    @Transactional
    public List<SalesOrder> createBulkSalesOrders(List<SalesOrderRequest> requests) {
        return requests.stream()
            .map(this::createSalesOrder)
            .collect(Collectors.toList());
    }
    
    @Override
    public int approveBulkSalesOrders(List<Long> ids, Long approvedByUserId) {
        int count = 0;
        for (Long id : ids) {
            try {
                approveSalesOrder(id, approvedByUserId, null);
                count++;
            } catch (Exception e) {
                log.error("Error approving sales order {}: {}", id, e.getMessage());
            }
        }
        return count;
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getSalesOrderSummary(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("id", order.getId());
        summary.put("orderNumber", order.getOrderNumber());
        summary.put("customerId", order.getCustomerId());
        summary.put("customerName", order.getCustomerName());
        summary.put("orderDate", order.getOrderDate());
        summary.put("status", order.getStatus());
        summary.put("isApproved", order.getIsApproved());
        summary.put("isDispatched", order.getIsDispatched());
        summary.put("isInvoiced", order.getIsInvoiced());
        summary.put("isPaid", order.getIsPaid());
        
        BigDecimal subtotal = order.getSubtotalAmount();
        BigDecimal tax = order.getTaxAmount();
        BigDecimal total = order.getTotalAmount();
        
        summary.put("subtotal", subtotal != null ? subtotal.doubleValue() : 0.0);
        summary.put("tax", tax != null ? tax.doubleValue() : 0.0);
        summary.put("total", total != null ? total.doubleValue() : 0.0);
        
        return summary;
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSalesOrdersByCustomerSummary(Long customerId) {
        return getSalesOrdersByCustomer(customerId).stream()
            .map(order -> {
                Map<String, Object> summary = new HashMap<>();
                summary.put("id", order.getId());
                summary.put("orderNumber", order.getOrderNumber());
                summary.put("orderDate", order.getOrderDate());
                summary.put("status", order.getStatus());
                summary.put("total", order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0);
                return summary;
            })
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Double getTotalSalesAmount(Long customerId, LocalDate startDate, LocalDate endDate) {
        List<SalesOrder> orders;
        if (customerId != null) {
            orders = salesOrderRepository.findByCustomerId(customerId);
        } else {
            orders = salesOrderRepository.findAll();
        }
        
        return orders.stream()
            .filter(order -> {
                LocalDate orderDate = order.getOrderDate();
                return orderDate != null && 
                       !orderDate.isBefore(startDate) && 
                       !orderDate.isAfter(endDate);
            })
            .map(SalesOrder::getTotalAmount)
            .filter(Objects::nonNull)
            .map(BigDecimal::doubleValue)
            .reduce(0.0, Double::sum);
    }
    
    @Transactional(readOnly = true)
    public Double getOutstandingBalance(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        BigDecimal total = order.getTotalAmount();
        BigDecimal paid = order.getPaidAmount() != null ? order.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal balance = total.subtract(paid);
        return balance.doubleValue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageOrderValue() {
        List<SalesOrder> allOrders = salesOrderRepository.findAll();
        
        if (allOrders.isEmpty()) {
            return 0.0;
        }
        
        Double totalValue = allOrders.stream()
            .map(SalesOrder::getTotalAmount)
            .filter(Objects::nonNull)
            .map(BigDecimal::doubleValue)
            .reduce(0.0, Double::sum);
        
        return totalValue / allOrders.size();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalOrders", salesOrderRepository.count());
        stats.put("pendingOrders", countSalesOrdersByStatus("PENDING"));
        stats.put("approvedOrders", countSalesOrdersByStatus("APPROVED"));
        stats.put("dispatchedOrders", countSalesOrdersByStatus("DISPATCHED"));
        stats.put("deliveredOrders", countSalesOrdersByStatus("DELIVERED"));
        stats.put("averageOrderValue", getAverageOrderValue());
        
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        stats.put("monthlySalesAmount", getTotalSalesAmount(null, startOfMonth, endOfMonth));
        
        return stats;
    }
    
    private String generateOrderNumber() {
        String prefix = "SO";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "-" + timestamp.substring(timestamp.length() - 10);
    }
    
    private BigDecimal calculateTotal(SalesOrderRequest request) {
        BigDecimal subtotal = request.getSubtotalAmount() != null ? 
            request.getSubtotalAmount() : BigDecimal.ZERO;
        BigDecimal tax = request.getTaxAmount() != null ? 
            request.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscountAmount() != null ? 
            request.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal shipping = request.getShippingAmount() != null ? 
            request.getShippingAmount() : BigDecimal.ZERO;
        
        return subtotal.add(tax).subtract(discount).add(shipping);
    }

}
