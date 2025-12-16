package lk.epicgreen.erp.purchase.service;

import lk.epicgreen.erp.purchase.dto.PurchaseOrderRequest;
import lk.epicgreen.erp.purchase.entity.PurchaseOrder;
import lk.epicgreen.erp.purchase.repository.PurchaseOrderRepository;
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
 * PurchaseOrder Service Implementation
 * Implementation of purchase order service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public abstract class PurchaseOrderServiceImpl implements PurchaseOrderService {
    
    private final PurchaseOrderRepository purchaseOrderRepository;
    
    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request) {
        log.info("Creating purchase order for supplier: {}", request.getSupplierId());
        
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderNumber(generateOrderNumber());
        purchaseOrder.setSupplierId(request.getSupplierId());
        purchaseOrder.setSupplierName(request.getSupplierName());
        purchaseOrder.setWarehouseId(request.getWarehouseId());
        purchaseOrder.setWarehouseName(request.getWarehouseName());
        purchaseOrder.setOrderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now());
        purchaseOrder.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        purchaseOrder.setOrderType(request.getOrderType() != null ? request.getOrderType() : "STANDARD");
        purchaseOrder.setPriority(request.getPriority() != null ? request.getPriority() : "NORMAL");
        purchaseOrder.setStatus("DRAFT");
        purchaseOrder.setPaymentStatus("UNPAID");
        purchaseOrder.setIsApproved(false);
        purchaseOrder.setIsReceived(false);
        purchaseOrder.setIsPaid(false);
        purchaseOrder.setSubtotalAmount(request.getSubtotalAmount());
        purchaseOrder.setTaxAmount(request.getTaxAmount());
        purchaseOrder.setDiscountAmount(request.getDiscountAmount() != null ? BigDecimal.valueOf(request.getDiscountAmount()) : BigDecimal.ZERO);
        purchaseOrder.setShippingAmount(request.getShippingAmount());
        purchaseOrder.setTotalAmount(calculateTotal(request));
        purchaseOrder.setPaidAmount(BigDecimal.ZERO);
        purchaseOrder.setBalanceAmount(purchaseOrder.getTotalAmount());
        purchaseOrder.setReceivedQuantity(BigDecimal.ZERO);
        purchaseOrder.setNotes(request.getNotes());
        
        return purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrderRequest request) {
        log.info("Updating purchase order: {}", id);
        PurchaseOrder existing = getPurchaseOrderById(id);
        
        if (existing.getIsApproved() || "ORDERED".equals(existing.getStatus())) {
            throw new RuntimeException("Cannot update approved or ordered purchase order");
        }
        
        existing.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        existing.setPriority(request.getPriority());
        existing.setSubtotalAmount(request.getSubtotalAmount());
        existing.setTaxAmount(request.getTaxAmount());
        existing.setDiscountAmount(request.getDiscountAmount() != null ? BigDecimal.valueOf(request.getDiscountAmount()) : BigDecimal.ZERO);
        existing.setShippingAmount(request.getShippingAmount());
        existing.setTotalAmount(calculateTotal(request));
        BigDecimal balance = existing.getTotalAmount().subtract(existing.getPaidAmount());
        existing.setBalanceAmount(balance);
        existing.setNotes(request.getNotes());
        
        return purchaseOrderRepository.save(existing);
    }
    
    @Override
    public void deletePurchaseOrder(Long id) {
        log.info("Deleting purchase order: {}", id);
        PurchaseOrder purchaseOrder = getPurchaseOrderById(id);
        
        if (purchaseOrder.getIsApproved() || purchaseOrder.getIsReceived()) {
            throw new RuntimeException("Cannot delete approved or received purchase order");
        }
        
        purchaseOrderRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Purchase order not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public PurchaseOrder getPurchaseOrderByNumber(String poNumber) {
        // Try to find by orderNumber or poNumber
        return purchaseOrderRepository.findAll().stream()
            .filter(po -> poNumber.equals(po.getPoNumber()) || poNumber.equals(po.getOrderNumber()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Purchase order not found with number: " + poNumber));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> getAllPurchaseOrders(Pageable pageable) {
        return purchaseOrderRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> searchPurchaseOrders(String searchTerm, Pageable pageable) {
        return purchaseOrderRepository.searchPurchaseOrders(searchTerm, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getPurchaseOrdersBySupplier(Long supplierId) {
        return purchaseOrderRepository.findBySupplierId(supplierId);
    }
    
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getPurchaseOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        // Filter manually if repository method doesn't exist
        return purchaseOrderRepository.findAll().stream()
            .filter(po -> {
                LocalDate orderDate = po.getOrderDate();
                return orderDate != null && 
                       !orderDate.isBefore(startDate) && 
                       !orderDate.isAfter(endDate);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public PurchaseOrder approvePurchaseOrder(Long id, Long approvedByUserId, String notes) {
        log.info("Approving purchase order: {}", id);
        PurchaseOrder purchaseOrder = getPurchaseOrderById(id);
        
        if (purchaseOrder.getIsApproved()) {
            throw new RuntimeException("Purchase order already approved");
        }
        
        purchaseOrder.setIsApproved(true);
        purchaseOrder.setApprovalDate(LocalDate.now()); // Use setApprovalDate (not setApprovedDate)
        purchaseOrder.setApprovedByUserId(approvedByUserId);
        purchaseOrder.setApprovalNotes(notes);
        purchaseOrder.setStatus("APPROVED");
        
        return purchaseOrderRepository.save(purchaseOrder);
    }
    
    public PurchaseOrder placeOrder(Long id) {
        log.info("Placing order for purchase order: {}", id);
        PurchaseOrder purchaseOrder = getPurchaseOrderById(id);
        
        if (!purchaseOrder.getIsApproved()) {
            throw new RuntimeException("Purchase order must be approved first");
        }
        
        purchaseOrder.setOrderedDate(LocalDate.now());
        purchaseOrder.setStatus("ORDERED");
        
        return purchaseOrderRepository.save(purchaseOrder);
    }
    
    public PurchaseOrder markAsReceived(Long id) {
        log.info("Marking purchase order as received: {}", id);
        PurchaseOrder purchaseOrder = getPurchaseOrderById(id);
        
        purchaseOrder.setIsReceived(true);
        purchaseOrder.setReceivedDate(LocalDate.now());
        purchaseOrder.setStatus("RECEIVED");
        
        return purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public PurchaseOrder cancelPurchaseOrder(Long id, String reason) {
        log.info("Cancelling purchase order: {}", id);
        PurchaseOrder purchaseOrder = getPurchaseOrderById(id);
        
        if (purchaseOrder.getIsReceived()) {
            throw new RuntimeException("Cannot cancel received purchase order");
        }
        
        purchaseOrder.setCancellationReason(reason);
        purchaseOrder.setCancelledDate(LocalDate.now());
        purchaseOrder.setStatus("CANCELLED");
        
        return purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public PurchaseOrder rejectPurchaseOrder(Long id, String reason) {
        log.info("Rejecting purchase order: {}", id);
        PurchaseOrder purchaseOrder = getPurchaseOrderById(id);
        
        purchaseOrder.setRejectionReason(reason);
        purchaseOrder.setRejectedDate(LocalDate.now());
        purchaseOrder.setStatus("REJECTED");
        
        return purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public void updateReceivedQuantity(Long purchaseOrderId, Double quantity) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        BigDecimal currentQty = purchaseOrder.getReceivedQuantity();
        BigDecimal additionalQty = BigDecimal.valueOf(quantity != null ? quantity : 0.0);
        purchaseOrder.setReceivedQuantity(currentQty.add(additionalQty));
        purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public void markAsPartialReceived(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        purchaseOrder.setStatus("PARTIAL_RECEIVED");
        purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public void updatePaymentStatus(Long purchaseOrderId, String paymentStatus) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        purchaseOrder.setPaymentStatus(paymentStatus);
        
        if ("PAID".equals(paymentStatus)) {
            purchaseOrder.setIsPaid(true);
            purchaseOrder.setPaidDate(LocalDate.now());
        }
        
        purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public void recordPayment(Long purchaseOrderId, Double paidAmount) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        BigDecimal currentPaid = purchaseOrder.getPaidAmount();
        BigDecimal additionalPaid = BigDecimal.valueOf(paidAmount != null ? paidAmount : 0.0);
        BigDecimal newPaidAmount = currentPaid.add(additionalPaid);
        
        purchaseOrder.setPaidAmount(newPaidAmount);
        BigDecimal balance = purchaseOrder.getTotalAmount().subtract(newPaidAmount);
        purchaseOrder.setBalanceAmount(balance);
        
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            purchaseOrder.setPaymentStatus("PAID");
            purchaseOrder.setIsPaid(true);
            purchaseOrder.setPaidDate(LocalDate.now());
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            purchaseOrder.setPaymentStatus("PARTIAL");
        }
        
        purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public void markAsPaid(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        purchaseOrder.setIsPaid(true);
        purchaseOrder.setPaidDate(LocalDate.now());
        purchaseOrder.setPaymentStatus("PAID");
        purchaseOrder.setPaidAmount(purchaseOrder.getTotalAmount());
        purchaseOrder.setBalanceAmount(BigDecimal.ZERO);
        
        purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getDraftPurchaseOrders() {
        return purchaseOrderRepository.findDraftPurchaseOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getPendingPurchaseOrders() {
        return purchaseOrderRepository.findPendingPurchaseOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getApprovedPurchaseOrders() {
        return purchaseOrderRepository.findApprovedPurchaseOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getOrderedPurchaseOrders() {
        return purchaseOrderRepository.findOrderedPurchaseOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getPartialReceivedPurchaseOrders() {
        return purchaseOrderRepository.findPartialReceivedPurchaseOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getReceivedPurchaseOrders() {
        return purchaseOrderRepository.findReceivedPurchaseOrders();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getCancelledPurchaseOrders() {
        return purchaseOrderRepository.findCancelledPurchaseOrders();
    }
    
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getOverduePurchaseOrders() {
        // Filter manually if repository method doesn't exist
        return purchaseOrderRepository.findAll().stream()
            .filter(po -> {
                LocalDate expectedDate = po.getExpectedDeliveryDate();
                return expectedDate != null && 
                       expectedDate.isBefore(LocalDate.now()) &&
                       !"RECEIVED".equals(po.getStatus()) &&
                       !"CANCELLED".equals(po.getStatus());
            })
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getUpcomingDeliveries(int daysAhead) {
        LocalDate endDate = LocalDate.now().plusDays(daysAhead);
        // Filter manually if repository method doesn't exist
        return purchaseOrderRepository.findAll().stream()
            .filter(po -> {
                LocalDate expectedDate = po.getExpectedDeliveryDate();
                return expectedDate != null &&
                       !expectedDate.isBefore(LocalDate.now()) &&
                       !expectedDate.isAfter(endDate) &&
                       !"RECEIVED".equals(po.getStatus()) &&
                       !"CANCELLED".equals(po.getStatus());
            })
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Long countPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.countByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public Long countPurchaseOrdersBySupplier(Long supplierId) {
        return purchaseOrderRepository.countBySupplierId(supplierId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getRecentPurchaseOrders(int limit) {
        return purchaseOrderRepository.findRecentPurchaseOrders(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validatePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrder.getSupplierId() != null &&
               purchaseOrder.getWarehouseId() != null &&
               purchaseOrder.getOrderDate() != null &&
               purchaseOrder.getTotalAmount() != null &&
               purchaseOrder.getTotalAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canApprovePurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        return !purchaseOrder.getIsApproved() && !"CANCELLED".equals(purchaseOrder.getStatus());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canCancelPurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        return !purchaseOrder.getIsReceived();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canMarkAsReceived(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        return purchaseOrder.getIsApproved() && 
               ("ORDERED".equals(purchaseOrder.getStatus()) || 
                "PARTIAL_RECEIVED".equals(purchaseOrder.getStatus()));
    }
    
    @Override
    public void calculateOrderTotals(PurchaseOrder purchaseOrder) {
        BigDecimal subtotal = purchaseOrder.getSubtotalAmount();
        BigDecimal tax = purchaseOrder.getTaxAmount();
        BigDecimal discount = purchaseOrder.getDiscountAmount();
        BigDecimal shipping = purchaseOrder.getShippingAmount();
        
        BigDecimal total = subtotal.add(tax).subtract(discount).add(shipping);
        purchaseOrder.setTotalAmount(total);
        
        BigDecimal balance = total.subtract(purchaseOrder.getPaidAmount());
        purchaseOrder.setBalanceAmount(balance);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateSubtotal(PurchaseOrder purchaseOrder) {
        BigDecimal subtotal = purchaseOrder.getSubtotalAmount();
        return subtotal != null ? subtotal.doubleValue() : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalTax(PurchaseOrder purchaseOrder) {
        BigDecimal tax = purchaseOrder.getTaxAmount();
        return tax != null ? tax.doubleValue() : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateTotalDiscount(PurchaseOrder purchaseOrder) {
        BigDecimal discount = purchaseOrder.getDiscountAmount();
        return discount != null ? discount.doubleValue() : 0.0;
    }
    
    @Override
    public void updateBalanceAmount(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        BigDecimal balance = purchaseOrder.getTotalAmount().subtract(purchaseOrder.getPaidAmount());
        purchaseOrder.setBalanceAmount(balance);
        purchaseOrderRepository.save(purchaseOrder);
    }
    
    @Override
    public List<PurchaseOrder> createBulkPurchaseOrders(List<PurchaseOrderRequest> requests) {
        return requests.stream()
            .map(this::createPurchaseOrder)
            .collect(Collectors.toList());
    }
    
    @Override
    public int deleteBulkPurchaseOrders(List<Long> ids) {
        int count = 0;
        for (Long id : ids) {
            try {
                deletePurchaseOrder(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting purchase order {}: {}", id, e.getMessage());
            }
        }
        return count;
    }
    
    public List<PurchaseOrder> approveBulkPurchaseOrders(List<Long> ids, Long approvedByUserId, String notes) {
        return ids.stream()
            .map(id -> approvePurchaseOrder(id, approvedByUserId, notes))
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPurchaseOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", purchaseOrderRepository.count());
        stats.put("draft", countPurchaseOrdersByStatus("DRAFT"));
        stats.put("pending", countPurchaseOrdersByStatus("PENDING_APPROVAL"));
        stats.put("approved", countPurchaseOrdersByStatus("APPROVED"));
        stats.put("ordered", countPurchaseOrdersByStatus("ORDERED"));
        stats.put("received", countPurchaseOrdersByStatus("RECEIVED"));
        stats.put("cancelled", countPurchaseOrdersByStatus("CANCELLED"));
        return stats;
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getPurchaseOrderSummary(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("id", purchaseOrder.getId());
        summary.put("orderNumber", purchaseOrder.getOrderNumber());
        summary.put("supplierId", purchaseOrder.getSupplierId());
        summary.put("warehouseId", purchaseOrder.getWarehouseId());
        summary.put("orderDate", purchaseOrder.getOrderDate());
        summary.put("status", purchaseOrder.getStatus());
        summary.put("isApproved", purchaseOrder.getIsApproved());
        summary.put("isReceived", purchaseOrder.getIsReceived());
        summary.put("isPaid", purchaseOrder.getIsPaid());
        
        BigDecimal subtotal = purchaseOrder.getSubtotalAmount();
        BigDecimal tax = purchaseOrder.getTaxAmount();
        BigDecimal shipping = purchaseOrder.getShippingAmount();
        BigDecimal total = purchaseOrder.getTotalAmount();
        BigDecimal paid = purchaseOrder.getPaidAmount();
        
        summary.put("subtotal", subtotal != null ? subtotal.doubleValue() : 0.0);
        summary.put("tax", tax != null ? tax.doubleValue() : 0.0);
        summary.put("shipping", shipping != null ? shipping.doubleValue() : 0.0);
        summary.put("total", total != null ? total.doubleValue() : 0.0);
        summary.put("paid", paid != null ? paid.doubleValue() : 0.0);
        summary.put("balance", total != null && paid != null ? total.subtract(paid).doubleValue() : 0.0);
        
        return summary;
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPurchaseOrdersBySupplierSummary(Long supplierId) {
        return getPurchaseOrdersBySupplier(supplierId).stream()
            .map(po -> {
                Map<String, Object> summary = new HashMap<>();
                summary.put("id", po.getId());
                summary.put("orderNumber", po.getOrderNumber());
                summary.put("orderDate", po.getOrderDate());
                summary.put("status", po.getStatus());
                summary.put("total", po.getTotalAmount() != null ? po.getTotalAmount().doubleValue() : 0.0);
                return summary;
            })
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Double getTotalPurchaseAmount(Long supplierId, LocalDate startDate, LocalDate endDate) {
        List<PurchaseOrder> orders;
        if (supplierId != null) {
            orders = purchaseOrderRepository.findBySupplierId(supplierId);
        } else {
            orders = purchaseOrderRepository.findAll();
        }
        
        return orders.stream()
            .filter(po -> {
                LocalDate poDate = po.getOrderDate();
                return poDate != null && 
                       !poDate.isBefore(startDate) && 
                       !poDate.isAfter(endDate);
            })
            .map(PurchaseOrder::getTotalAmount)
            .filter(Objects::nonNull)
            .map(BigDecimal::doubleValue)
            .reduce(0.0, Double::sum);
    }
    
    @Transactional(readOnly = true)
    public Double getOutstandingBalance(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = getPurchaseOrderById(purchaseOrderId);
        BigDecimal balance = purchaseOrder.getBalanceAmount();
        return balance != null ? balance.doubleValue() : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getOnTimeDeliveryRate() {
        List<PurchaseOrder> completedOrders = purchaseOrderRepository.findAll().stream()
            .filter(po -> "RECEIVED".equals(po.getStatus()))
            .collect(Collectors.toList());
        
        if (completedOrders.isEmpty()) {
            return 0.0;
        }
        
        long onTimeDeliveries = completedOrders.stream()
            .filter(po -> {
                LocalDate expectedDate = po.getExpectedDeliveryDate();
                LocalDate receivedDate = po.getReceivedDate();
                return expectedDate != null && receivedDate != null && 
                       !receivedDate.isAfter(expectedDate);
            })
            .count();
        
        return (onTimeDeliveries * 100.0) / completedOrders.size();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageOrderValue() {
        List<PurchaseOrder> allOrders = purchaseOrderRepository.findAll();
        
        if (allOrders.isEmpty()) {
            return 0.0;
        }
        
        Double totalValue = allOrders.stream()
            .map(PurchaseOrder::getTotalAmount)
            .filter(Objects::nonNull)
            .map(BigDecimal::doubleValue)
            .reduce(0.0, Double::sum);
        
        return totalValue / allOrders.size();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Basic counts
        stats.put("totalOrders", purchaseOrderRepository.count());
        stats.put("draftOrders", countPurchaseOrdersByStatus("DRAFT"));
        stats.put("pendingApproval", countPurchaseOrdersByStatus("PENDING_APPROVAL"));
        stats.put("approvedOrders", countPurchaseOrdersByStatus("APPROVED"));
        stats.put("orderedOrders", countPurchaseOrdersByStatus("ORDERED"));
        stats.put("receivedOrders", countPurchaseOrdersByStatus("RECEIVED"));
        stats.put("cancelledOrders", countPurchaseOrdersByStatus("CANCELLED"));
        
        // Overdue and upcoming
        stats.put("overdueOrders", getOverduePurchaseOrders().size());
        stats.put("upcomingDeliveries", getUpcomingDeliveries(7).size());
        
        // Financial stats
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        stats.put("monthlyPurchaseAmount", getTotalPurchaseAmount(null, startOfMonth, endOfMonth));
        
        return stats;
    }
    
    private String generateOrderNumber() {
        String prefix = "PO";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "-" + timestamp.substring(timestamp.length() - 10);
    }
    
    private BigDecimal calculateTotal(PurchaseOrderRequest request) {
        BigDecimal subtotal = request.getSubtotalAmount() != null ? 
            BigDecimal.valueOf(request.getSubtotalAmount()) : BigDecimal.ZERO;
        BigDecimal tax = request.getTaxAmount() != null ? 
            BigDecimal.valueOf(request.getTaxAmount()) : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscountAmount() != null ? 
            BigDecimal.valueOf(request.getDiscountAmount()) : BigDecimal.ZERO;
        BigDecimal shipping = request.getShippingAmount() != null ? 
            BigDecimal.valueOf(request.getShippingAmount()) : BigDecimal.ZERO;
        
        return subtotal.add(tax).subtract(discount).add(shipping);
    }
}
