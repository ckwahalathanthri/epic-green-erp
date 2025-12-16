package lk.epicgreen.erp.purchase.service;

import lk.epicgreen.erp.purchase.dto.GoodsReceiptRequest;
import lk.epicgreen.erp.purchase.entity.GoodsReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GoodsReceipt Service Interface
 * Service for goods receipt operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface GoodsReceiptService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    GoodsReceipt createGoodsReceipt(GoodsReceiptRequest request);
    GoodsReceipt updateGoodsReceipt(Long id, GoodsReceiptRequest request);
    void deleteGoodsReceipt(Long id);
    GoodsReceipt getGoodsReceiptById(Long id);
    GoodsReceipt getGoodsReceiptByNumber(String receiptNumber);
    List<GoodsReceipt> getAllGoodsReceipts();
    Page<GoodsReceipt> getAllGoodsReceipts(Pageable pageable);
    Page<GoodsReceipt> searchGoodsReceipts(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    GoodsReceipt verifyGoodsReceipt(Long id, Long verifiedByUserId, String verificationNotes);
    GoodsReceipt postToInventory(Long id);
    GoodsReceipt rejectGoodsReceipt(Long id, String rejectionReason);
    
    // ===================================================================
    // QUALITY OPERATIONS
    // ===================================================================
    
    void recordQualityInspection(Long receiptId, Double acceptedQuantity, Double rejectedQuantity);
    void updateQuantities(Long receiptId, Double receivedQuantity, Double acceptedQuantity, 
                         Double rejectedQuantity);
    void markDiscrepancy(Long receiptId, String discrepancyNotes);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<GoodsReceipt> getPendingGoodsReceipts();
    List<GoodsReceipt> getVerifiedGoodsReceipts();
    List<GoodsReceipt> getPostedGoodsReceipts();
    List<GoodsReceipt> getRejectedGoodsReceipts();
    List<GoodsReceipt> getUnverifiedGoodsReceipts();
    List<GoodsReceipt> getUnpostedGoodsReceipts();
    List<GoodsReceipt> getGoodsReceiptsWithDiscrepancies();
    List<GoodsReceipt> getTodaysGoodsReceipts();
    List<GoodsReceipt> getGoodsReceiptsByPurchaseOrder(Long purchaseOrderId);
    List<GoodsReceipt> getGoodsReceiptsBySupplier(Long supplierId);
    List<GoodsReceipt> getGoodsReceiptsByWarehouse(Long warehouseId);
    List<GoodsReceipt> getGoodsReceiptsByDateRange(LocalDate startDate, LocalDate endDate);
    List<GoodsReceipt> getRecentGoodsReceipts(int limit);
    Double getTotalReceivedQuantityByPurchaseOrder(Long purchaseOrderId);
    Double getTotalAcceptedQuantityByPurchaseOrder(Long purchaseOrderId);
    Double getTotalRejectedQuantityByPurchaseOrder(Long purchaseOrderId);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateGoodsReceipt(GoodsReceipt goodsReceipt);
    boolean canVerifyGoodsReceipt(Long receiptId);
    boolean canPostToInventory(Long receiptId);
    boolean canRejectGoodsReceipt(Long receiptId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    Double calculateAcceptanceRate(Long receiptId);
    Double calculateRejectionRate(Long receiptId);
    Map<String, Object> calculateReceiptMetrics(Long receiptId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<GoodsReceipt> createBulkGoodsReceipts(List<GoodsReceiptRequest> requests);
    int verifyBulkGoodsReceipts(List<Long> receiptIds, Long verifiedByUserId);
    int postBulkToInventory(List<Long> receiptIds);
    int deleteBulkGoodsReceipts(List<Long> receiptIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getGoodsReceiptStatistics();
    List<Map<String, Object>> getReceiptTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getMonthlyGoodsReceiptCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getReceiptsBySupplier();
    List<Map<String, Object>> getReceiptsByWarehouse();
    Double getTotalReceivedQuantity();
    Double getTotalAcceptedQuantity();
    Double getTotalRejectedQuantity();
    Double getAcceptanceRate();
    Double getRejectionRate();
    Map<String, Object> getDashboardStatistics();
}
