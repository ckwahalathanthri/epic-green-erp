package lk.epicgreen.erp.purchase.service;

import lk.epicgreen.erp.purchase.dto.PurchaseOrderRequest;
import lk.epicgreen.erp.purchase.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * PurchaseOrder Service Interface
 * Service for purchase order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface PurchaseOrderService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    PurchaseOrder createPurchaseOrder(PurchaseOrderRequest request);
    PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrderRequest request);
    void deletePurchaseOrder(Long id);
    PurchaseOrder getPurchaseOrderById(Long id);
    PurchaseOrder getPurchaseOrderByNumber(String orderNumber);
    List<PurchaseOrder> getAllPurchaseOrders();
    Page<PurchaseOrder> getAllPurchaseOrders(Pageable pageable);
    Page<PurchaseOrder> searchPurchaseOrders(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    PurchaseOrder approvePurchaseOrder(Long id, Long approvedByUserId, String approvalNotes);
    PurchaseOrder markAsOrdered(Long id);
    PurchaseOrder markAsReceived(Long id, LocalDate receivedDate);
    PurchaseOrder cancelPurchaseOrder(Long id, String cancellationReason);
    PurchaseOrder rejectPurchaseOrder(Long id, String rejectionReason);
    
    // ===================================================================
    // RECEIVING OPERATIONS
    // ===================================================================
    
    void updateReceivedQuantity(Long purchaseOrderId, Double quantity);
    void markAsPartialReceived(Long purchaseOrderId);
    
    // ===================================================================
    // PAYMENT OPERATIONS
    // ===================================================================
    
    void updatePaymentStatus(Long purchaseOrderId, String paymentStatus);
    void recordPayment(Long purchaseOrderId, Double paidAmount);
    void markAsPaid(Long purchaseOrderId);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<PurchaseOrder> getDraftPurchaseOrders();
    List<PurchaseOrder> getPendingPurchaseOrders();
    List<PurchaseOrder> getApprovedPurchaseOrders();
    List<PurchaseOrder> getOrderedPurchaseOrders();
    List<PurchaseOrder> getPartialReceivedPurchaseOrders();
    List<PurchaseOrder> getReceivedPurchaseOrders();
    List<PurchaseOrder> getCancelledPurchaseOrders();
    List<PurchaseOrder> getPurchaseOrdersPendingApproval();
    List<PurchaseOrder> getOverdueDeliveries();
    List<PurchaseOrder> getHighPriorityPurchaseOrders();
    List<PurchaseOrder> getUnpaidPurchaseOrders();
    List<PurchaseOrder> getPartiallyPaidPurchaseOrders();
    List<PurchaseOrder> getUnreceivedPurchaseOrders();
    List<PurchaseOrder> getTodaysExpectedDeliveries();
    List<PurchaseOrder> getPurchaseOrdersRequiringAction();
    List<PurchaseOrder> getPurchaseOrdersBySupplier(Long supplierId);
    List<PurchaseOrder> getPurchaseOrdersByWarehouse(Long warehouseId);
    List<PurchaseOrder> getPurchaseOrdersByDateRange(LocalDate startDate, LocalDate endDate);
    List<PurchaseOrder> getRecentPurchaseOrders(int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validatePurchaseOrder(PurchaseOrder purchaseOrder);
    boolean canApprovePurchaseOrder(Long purchaseOrderId);
    boolean canCancelPurchaseOrder(Long purchaseOrderId);
    boolean canMarkAsReceived(Long purchaseOrderId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    void calculateOrderTotals(PurchaseOrder purchaseOrder);
    Double calculateSubtotal(PurchaseOrder purchaseOrder);
    Double calculateTotalTax(PurchaseOrder purchaseOrder);
    Double calculateTotalDiscount(PurchaseOrder purchaseOrder);
    void updateBalanceAmount(Long purchaseOrderId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<PurchaseOrder> createBulkPurchaseOrders(List<PurchaseOrderRequest> requests);
    int approveBulkPurchaseOrders(List<Long> purchaseOrderIds, Long approvedByUserId);
    int deleteBulkPurchaseOrders(List<Long> purchaseOrderIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getPurchaseOrderStatistics();
    List<Map<String, Object>> getOrderTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getPriorityDistribution();
    List<Map<String, Object>> getPaymentStatusDistribution();
    List<Map<String, Object>> getMonthlyPurchaseOrderCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getTotalOrderValueBySupplier();
    List<Map<String, Object>> getTopSuppliers(int limit);
    Double getTotalOrderValue();
    Double getTotalPaidAmount();
    Double getTotalOutstandingAmount();
    Double getAverageOrderValue();
    Double getOnTimeDeliveryRate();
    Map<String, Object> getDashboardStatistics();
}
