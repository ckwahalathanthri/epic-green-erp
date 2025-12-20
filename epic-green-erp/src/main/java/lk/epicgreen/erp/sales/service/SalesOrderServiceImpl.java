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
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;

    @Override
    public SalesOrder createSalesOrder(SalesOrderRequest request) {
        log.info("Creating sales order for customer: {}", request.getCustomerId());

        SalesOrder order = new SalesOrder();
        order.setOrderNumber(generateOrderNumber());

        // Customer info - use object references or denormalized fields
        if (request.getCustomerId() != null) {
            order.setCustomer(request.getCustomerId()); // May need Customer entity reference
        }
        if (request.getCustomerName() != null) {
            // order.setCustomerName(request.getCustomerName()); // Only if denormalized
        }

        order.setOrderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now());
        order.setDeliveryDate(request.getDeliveryDate() != null ? request.getDeliveryDate() : LocalDate.now().plusDays(7));
        order.setOrderType(request.getOrderType());
        order.setPriority(request.getPriority() != null ? request.getPriority() : "NORMAL");
        order.setStatus("PENDING");
        order.setDeliveryState("PENDING");
        order.setPaymentState("UNPAID");

        // Sales rep info
        if (request.getSalesRepId() != null) {
            order.setSalesRep(request.getSalesRepId()); // May need User entity reference
        }

        // Boolean flags - remove "Is" prefix
        order.setApproved(false);
        order.setDispatched(false);
        order.setInvoiced(false);
        order.setPaid(false);

        // Calculate amounts - proper BigDecimal handling
        BigDecimal subtotal = request.getSubtotal() != null ?
                BigDecimal.valueOf(request.getSubtotal()) : BigDecimal.ZERO;
        BigDecimal tax = request.getTax() != null ?
                BigDecimal.valueOf(request.getTax()) : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscount() != null ?
                BigDecimal.valueOf(request.getDiscount()) : BigDecimal.ZERO;

        order.setSubtotal(subtotal);
        order.setTaxAmount(tax);
        order.setDiscountAmount(discount);
        order.setTotalAmount(calculateTotal(request));

        order.setNotes(request.getNotes());

        return salesOrderRepository.save(order);
    }

    @Override
    public SalesOrder updateSalesOrder(Long id, SalesOrderRequest request) {
        log.info("Updating sales order: {}", id);
        SalesOrder existing = getSalesOrderById(id);

        // Only allow updates if not approved
        if (existing.getApproved()) {
            throw new RuntimeException("Cannot update approved sales order");
        }

        existing.setOrderDate(request.getOrderDate());
        existing.setDeliveryDate(request.getDeliveryDate());
        existing.setOrderType(request.getOrderType());
        existing.setPriority(request.getPriority());
        existing.setNotes(request.getNotes());

        // Update amounts
        BigDecimal subtotal = request.getSubtotal() != null ?
                BigDecimal.valueOf(request.getSubtotal()) : BigDecimal.ZERO;
        BigDecimal tax = request.getTax() != null ?
                BigDecimal.valueOf(request.getTax()) : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscount() != null ?
                BigDecimal.valueOf(request.getDiscount()) : BigDecimal.ZERO;

        existing.setSubtotal(subtotal);
        existing.setTaxAmount(tax);
        existing.setDiscountAmount(discount);
        existing.setTotalAmount(calculateTotal(request));

        return salesOrderRepository.save(existing);
    }

    @Override
    public void deleteSalesOrder(Long id) {
        log.info("Deleting sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);

        if (order.getApproved() || order.getDispatched() || order.getInvoiced()) {
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

    @Override
    @Transactional(readOnly = true)
    public Page<SalesOrder> searchSalesOrders(String searchTerm, Pageable pageable) {
        return salesOrderRepository.searchSalesOrders(searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getSalesOrdersByCustomer(Long customerId) {
        return salesOrderRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getSalesOrdersByStatus(String status) {
        return salesOrderRepository.findByStatus(status);
    }

    @Override
    public SalesOrder approveSalesOrder(Long id, Long approvedByUserId, String notes) {
        log.info("Approving sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);

        if (order.getApproved()) {
            throw new RuntimeException("Sales order already approved");
        }

        order.setApproved(true);
        order.setStatus("APPROVED");
        order.setApprovalDate(LocalDate.now());
        order.setApprovedBy(approvedByUserId);
        order.setApprovalNote(notes);

        return salesOrderRepository.save(order);
    }

    @Override
    public SalesOrder rejectSalesOrder(Long id, String reason) {
        log.info("Rejecting sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);

        order.setStatus("REJECTED");
        order.setRejectReason(reason);
        order.setRejectedAt(LocalDate.now());

        return salesOrderRepository.save(order);
    }

    @Override
    public SalesOrder dispatchSalesOrder(Long id, Long dispatchNoteId) {
        log.info("Dispatching sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);

        if (!order.getApproved()) {
            throw new RuntimeException("Cannot dispatch unapproved sales order");
        }

        order.setDispatched(true);
        order.setDispatchNote(dispatchNoteId);
        order.setDispatchedAt(LocalDate.now());
        order.setDeliveryState("DISPATCHED");
        order.setStatus("DISPATCHED");

        return salesOrderRepository.save(order);
    }

    @Override
    public SalesOrder markAsDelivered(Long id) {
        log.info("Marking sales order as delivered: {}", id);
        SalesOrder order = getSalesOrderById(id);

        order.setDeliveryState("DELIVERED");
        order.setStatus("DELIVERED");

        return salesOrderRepository.save(order);
    }

    @Override
    public SalesOrder generateInvoice(Long id, Long invoiceId) {
        log.info("Generating invoice for sales order: {}", id);
        SalesOrder order = getSalesOrderById(id);

        order.setInvoiced(true);
        order.setInvoice(invoiceId);
        order.setInvoicedAt(LocalDate.now());

        return salesOrderRepository.save(order);
    }

    @Override
    public void updatePaymentStatus(Long salesOrderId, String paymentStatus) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        order.setPaymentState(paymentStatus);

        if ("PAID".equals(paymentStatus)) {
            order.setPaid(true);
            order.setPaidAt(LocalDate.now());
        }

        salesOrderRepository.save(order);
    }

    @Override
    public void markAsPaid(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        order.setPaid(true);
        order.setPaidAt(LocalDate.now());
        order.setPaymentState("PAID");
        order.setPaidAmount(order.getTotalAmount());

        salesOrderRepository.save(order);
    }

    @Override
    public void recordPayment(Long salesOrderId, Double paidAmount) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        BigDecimal payment = BigDecimal.valueOf(paidAmount != null ? paidAmount : 0.0);
        BigDecimal currentPaid = order.getPaidAmount() != null ? order.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal newPaidAmount = currentPaid.add(payment);

        order.setPaidAmount(newPaidAmount);

        if (newPaidAmount.compareTo(order.getTotalAmount()) >= 0) {
            order.setPaymentState("PAID");
            order.setPaid(true);
            order.setPaidAt(LocalDate.now());
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            order.setPaymentState("PARTIAL");
        }

        salesOrderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateSubtotal(SalesOrder order) {
        BigDecimal subtotal = order.getSubtotal();
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

        if (order.getDispatched() && order.getInvoiced()) {
            throw new RuntimeException("Cannot cancel dispatched and invoiced sales order");
        }

        order.setCancelReason(reason);
        order.setCancelledAt(LocalDate.now());
        order.setStatus("CANCELLED");

        return salesOrderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getPendingSalesOrders() {
        return salesOrderRepository.findPendingSalesOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getApprovedSalesOrders() {
        return salesOrderRepository.findApprovedSalesOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getDispatchedSalesOrders() {
        return salesOrderRepository.findDispatchedSalesOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getInvoicedSalesOrders() {
        return salesOrderRepository.findInvoicedSalesOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrder> getRecentSalesOrders(int limit) {
        return salesOrderRepository.findRecentSalesOrders(PageRequest.of(0, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countSalesOrdersByStatus(String status) {
        return salesOrderRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countSalesOrdersByCustomer(Long customerId) {
        return salesOrderRepository.countByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateSalesOrder(SalesOrder order) {
        // Validate required fields
        Long customerId = getCustomerIdFromOrder(order);
        BigDecimal total = order.getTotalAmount();

        return customerId != null &&
                order.getOrderDate() != null &&
                total != null &&
                total.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canApproveSalesOrder(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        return !order.getApproved() && !"CANCELLED".equals(order.getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canCancelSalesOrder(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        return !(order.getDispatched() && order.getInvoiced());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDispatchSalesOrder(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);
        return order.getApproved() && !order.getDispatched();
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

    @Override
    public List<SalesOrder> createBulkSalesOrders(List<SalesOrderRequest> requests) {
        return requests.stream()
                .map(this::createSalesOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesOrder> approveBulkSalesOrders(List<Long> ids, Long approvedByUserId) {
        return ids.stream()
                .map(id -> approveSalesOrder(id, approvedByUserId, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSalesOrderSummary(Long salesOrderId) {
        SalesOrder order = getSalesOrderById(salesOrderId);

        Map<String, Object> summary = new HashMap<>();
        summary.put("id", order.getId());
        summary.put("orderNumber", order.getOrderNumber());
        summary.put("customerId", getCustomerIdFromOrder(order));
        summary.put("orderDate", order.getOrderDate());
        summary.put("status", order.getStatus());
        summary.put("isApproved", order.getApproved());
        summary.put("isDispatched", order.getDispatched());
        summary.put("isInvoiced", order.getInvoiced());
        summary.put("isPaid", order.getPaid());

        BigDecimal subtotal = order.getSubtotal();
        BigDecimal tax = order.getTaxAmount();
        BigDecimal total = order.getTotalAmount();

        summary.put("subtotal", subtotal != null ? subtotal.doubleValue() : 0.0);
        summary.put("tax", tax != null ? tax.doubleValue() : 0.0);
        summary.put("total", total != null ? total.doubleValue() : 0.0);

        return summary;
    }

    @Override
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

    @Override
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

    @Override
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
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalOrders", salesOrderRepository.count());
        stats.put("pendingOrders", countSalesOrdersByStatus("PENDING"));
        stats.put("approvedOrders", countSalesOrdersByStatus("APPROVED"));
        stats.put("dispatchedOrders", countSalesOrdersByStatus("DISPATCHED"));
        stats.put("deliveredOrders", countSalesOrdersByStatus("DELIVERED"));

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
        BigDecimal subtotal = request.getSubtotal() != null ?
                BigDecimal.valueOf(request.getSubtotal()) : BigDecimal.ZERO;
        BigDecimal tax = request.getTax() != null ?
                BigDecimal.valueOf(request.getTax()) : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscount() != null ?
                BigDecimal.valueOf(request.getDiscount()) : BigDecimal.ZERO;

        return subtotal.add(tax).subtract(discount);
    }

    // Helper method to get customer ID (might be from relationship or denormalized field)
    private Long getCustomerIdFromOrder(SalesOrder order) {
        // If Customer is a relationship object
        if (order.getCustomer() != null) {
            return order.getCustomer().getId();
        }
        // If customerId is denormalized field
        try {
            return (Long) order.getClass().getMethod("getCustomerId").invoke(order);
        } catch (Exception e) {
            return null;
        }
    }
}