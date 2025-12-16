package lk.epicgreen.erp.purchase.repository;

import lk.epicgreen.erp.purchase.entity.GoodsReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * GoodsReceipt Repository
 * Repository for goods receipt data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find goods receipt by receipt number
     */
    Optional<GoodsReceipt> findByReceiptNumber(String receiptNumber);
    
    /**
     * Check if goods receipt exists by receipt number
     */
    boolean existsByReceiptNumber(String receiptNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find goods receipts by purchase order ID
     */
    List<GoodsReceipt> findByPurchaseOrderId(Long purchaseOrderId);
    
    /**
     * Find goods receipts by purchase order ID with pagination
     */
    Page<GoodsReceipt> findByPurchaseOrderId(Long purchaseOrderId, Pageable pageable);
    
    /**
     * Find goods receipts by supplier ID
     */
    List<GoodsReceipt> findBySupplierId(Long supplierId);
    
    /**
     * Find goods receipts by warehouse ID
     */
    List<GoodsReceipt> findByWarehouseId(Long warehouseId);
    
    /**
     * Find goods receipts by status
     */
    List<GoodsReceipt> findByStatus(String status);
    
    /**
     * Find goods receipts by status with pagination
     */
    Page<GoodsReceipt> findByStatus(String status, Pageable pageable);
    
    /**
     * Find goods receipts by receipt type
     */
    List<GoodsReceipt> findByReceiptType(String receiptType);
    
    /**
     * Find goods receipts by received by user
     */
    List<GoodsReceipt> findByReceivedByUserId(Long userId);
    
    /**
     * Find goods receipts by verified by user
     */
    List<GoodsReceipt> findByVerifiedByUserId(Long userId);
    
    /**
     * Find goods receipts by is verified
     */
    List<GoodsReceipt> findByIsVerified(Boolean isVerified);
    
    /**
     * Find goods receipts by is posted to inventory
     */
    List<GoodsReceipt> findByIsPostedToInventory(Boolean isPostedToInventory);
    
    /**
     * Find goods receipts by has discrepancy
     */
    List<GoodsReceipt> findByHasDiscrepancy(Boolean hasDiscrepancy);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find goods receipts by receipt date between dates
     */
    List<GoodsReceipt> findByReceiptDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find goods receipts by receipt date between dates with pagination
     */
    Page<GoodsReceipt> findByReceiptDateBetween(LocalDate startDate, LocalDate endDate, 
                                                 Pageable pageable);
    
    /**
     * Find goods receipts by verified date between dates
     */
    List<GoodsReceipt> findByVerifiedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find goods receipts by created at between dates
     */
    List<GoodsReceipt> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find goods receipts by purchase order ID and status
     */
    List<GoodsReceipt> findByPurchaseOrderIdAndStatus(Long purchaseOrderId, String status);
    
    /**
     * Find goods receipts by supplier ID and status
     */
    List<GoodsReceipt> findBySupplierIdAndStatus(Long supplierId, String status);
    
    /**
     * Find goods receipts by warehouse ID and status
     */
    List<GoodsReceipt> findByWarehouseIdAndStatus(Long warehouseId, String status);
    
    /**
     * Find goods receipts by is verified and status
     */
    List<GoodsReceipt> findByIsVerifiedAndStatus(Boolean isVerified, String status);
    
    /**
     * Find goods receipts by is posted and status
     */
    List<GoodsReceipt> findByIsPostedToInventoryAndStatus(Boolean isPosted, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE " +
           "LOWER(gr.receiptNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(gr.supplierName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(gr.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<GoodsReceipt> searchGoodsReceipts(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find pending goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.status = 'PENDING' " +
           "ORDER BY gr.receiptDate DESC")
    List<GoodsReceipt> findPendingGoodsReceipts();
    
    /**
     * Find verified goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.status = 'VERIFIED' " +
           "AND gr.isVerified = true " +
           "ORDER BY gr.verifiedDate DESC")
    List<GoodsReceipt> findVerifiedGoodsReceipts();
    
    /**
     * Find posted goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.status = 'POSTED' " +
           "AND gr.isPostedToInventory = true " +
           "ORDER BY gr.receiptDate DESC")
    List<GoodsReceipt> findPostedGoodsReceipts();
    
    /**
     * Find rejected goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.status = 'REJECTED' " +
           "ORDER BY gr.receiptDate DESC")
    List<GoodsReceipt> findRejectedGoodsReceipts();
    
    /**
     * Find unverified goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.isVerified = false " +
           "AND gr.status NOT IN ('REJECTED') " +
           "ORDER BY gr.receiptDate ASC")
    List<GoodsReceipt> findUnverifiedGoodsReceipts();
    
    /**
     * Find unposted goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.isPostedToInventory = false " +
           "AND gr.isVerified = true " +
           "AND gr.status = 'VERIFIED' " +
           "ORDER BY gr.verifiedDate ASC")
    List<GoodsReceipt> findUnpostedGoodsReceipts();
    
    /**
     * Find goods receipts with discrepancies
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.hasDiscrepancy = true " +
           "ORDER BY gr.receiptDate DESC")
    List<GoodsReceipt> findGoodsReceiptsWithDiscrepancies();
    
    /**
     * Find today's goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.receiptDate = :today " +
           "ORDER BY gr.createdAt DESC")
    List<GoodsReceipt> findTodaysGoodsReceipts(@Param("today") LocalDate today);
    
    /**
     * Find recent goods receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr ORDER BY gr.receiptDate DESC, gr.createdAt DESC")
    List<GoodsReceipt> findRecentGoodsReceipts(Pageable pageable);
    
    /**
     * Find purchase order recent receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.purchaseOrderId = :purchaseOrderId " +
           "ORDER BY gr.receiptDate DESC, gr.createdAt DESC")
    List<GoodsReceipt> findPurchaseOrderRecentReceipts(@Param("purchaseOrderId") Long purchaseOrderId,
                                                        Pageable pageable);
    
    /**
     * Find supplier recent receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.supplierId = :supplierId " +
           "ORDER BY gr.receiptDate DESC, gr.createdAt DESC")
    List<GoodsReceipt> findSupplierRecentReceipts(@Param("supplierId") Long supplierId,
                                                   Pageable pageable);
    
    /**
     * Find warehouse recent receipts
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.warehouseId = :warehouseId " +
           "ORDER BY gr.receiptDate DESC, gr.createdAt DESC")
    List<GoodsReceipt> findWarehouseRecentReceipts(@Param("warehouseId") Long warehouseId,
                                                    Pageable pageable);
    
    /**
     * Find goods receipts by date range and status
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.receiptDate BETWEEN :startDate AND :endDate " +
           "AND gr.status = :status ORDER BY gr.receiptDate DESC")
    List<GoodsReceipt> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("status") String status);
    
    /**
     * Get total received quantity by purchase order
     */
    @Query("SELECT SUM(gr.receivedQuantity) FROM GoodsReceipt gr " +
           "WHERE gr.purchaseOrderId = :purchaseOrderId " +
           "AND gr.status NOT IN ('REJECTED')")
    Double getTotalReceivedQuantityByPurchaseOrder(@Param("purchaseOrderId") Long purchaseOrderId);
    
    /**
     * Get total accepted quantity by purchase order
     */
    @Query("SELECT SUM(gr.acceptedQuantity) FROM GoodsReceipt gr " +
           "WHERE gr.purchaseOrderId = :purchaseOrderId " +
           "AND gr.status NOT IN ('REJECTED')")
    Double getTotalAcceptedQuantityByPurchaseOrder(@Param("purchaseOrderId") Long purchaseOrderId);
    
    /**
     * Get total rejected quantity by purchase order
     */
    @Query("SELECT SUM(gr.rejectedQuantity) FROM GoodsReceipt gr " +
           "WHERE gr.purchaseOrderId = :purchaseOrderId")
    Double getTotalRejectedQuantityByPurchaseOrder(@Param("purchaseOrderId") Long purchaseOrderId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count goods receipts by purchase order
     */
    @Query("SELECT COUNT(gr) FROM GoodsReceipt gr WHERE gr.purchaseOrderId = :purchaseOrderId")
    Long countByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
    
    /**
     * Count goods receipts by supplier
     */
    @Query("SELECT COUNT(gr) FROM GoodsReceipt gr WHERE gr.supplierId = :supplierId")
    Long countBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Count goods receipts by warehouse
     */
    @Query("SELECT COUNT(gr) FROM GoodsReceipt gr WHERE gr.warehouseId = :warehouseId")
    Long countByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Count goods receipts by status
     */
    @Query("SELECT COUNT(gr) FROM GoodsReceipt gr WHERE gr.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count unverified goods receipts
     */
    @Query("SELECT COUNT(gr) FROM GoodsReceipt gr WHERE gr.isVerified = false " +
           "AND gr.status NOT IN ('REJECTED')")
    Long countUnverifiedGoodsReceipts();
    
    /**
     * Count unposted goods receipts
     */
    @Query("SELECT COUNT(gr) FROM GoodsReceipt gr WHERE gr.isPostedToInventory = false " +
           "AND gr.isVerified = true")
    Long countUnpostedGoodsReceipts();
    
    /**
     * Count goods receipts with discrepancies
     */
    @Query("SELECT COUNT(gr) FROM GoodsReceipt gr WHERE gr.hasDiscrepancy = true")
    Long countGoodsReceiptsWithDiscrepancies();
    
    /**
     * Get receipt type distribution
     */
    @Query("SELECT gr.receiptType, COUNT(gr) as receiptCount FROM GoodsReceipt gr " +
           "GROUP BY gr.receiptType ORDER BY receiptCount DESC")
    List<Object[]> getReceiptTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT gr.status, COUNT(gr) as receiptCount FROM GoodsReceipt gr " +
           "GROUP BY gr.status ORDER BY receiptCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get monthly goods receipt count
     */
    @Query("SELECT YEAR(gr.receiptDate) as year, MONTH(gr.receiptDate) as month, " +
           "COUNT(gr) as receiptCount, SUM(gr.receivedQuantity) as totalQuantity " +
           "FROM GoodsReceipt gr " +
           "WHERE gr.receiptDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(gr.receiptDate), MONTH(gr.receiptDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyGoodsReceiptCount(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
    
    /**
     * Get total received quantity
     */
    @Query("SELECT SUM(gr.receivedQuantity) FROM GoodsReceipt gr " +
           "WHERE gr.status NOT IN ('REJECTED')")
    Double getTotalReceivedQuantity();
    
    /**
     * Get total accepted quantity
     */
    @Query("SELECT SUM(gr.acceptedQuantity) FROM GoodsReceipt gr " +
           "WHERE gr.status NOT IN ('REJECTED')")
    Double getTotalAcceptedQuantity();
    
    /**
     * Get total rejected quantity
     */
    @Query("SELECT SUM(gr.rejectedQuantity) FROM GoodsReceipt gr")
    Double getTotalRejectedQuantity();
    
    /**
     * Get acceptance rate
     */
    @Query("SELECT " +
           "(SELECT SUM(gr.acceptedQuantity) FROM GoodsReceipt gr WHERE gr.status NOT IN ('REJECTED')) * 100.0 / " +
           "(SELECT SUM(gr.receivedQuantity) FROM GoodsReceipt gr WHERE gr.status NOT IN ('REJECTED')) " +
           "FROM GoodsReceipt gr WHERE gr.status NOT IN ('REJECTED')")
    Double getAcceptanceRate();
    
    /**
     * Get rejection rate
     */
    @Query("SELECT " +
           "(SELECT SUM(gr.rejectedQuantity) FROM GoodsReceipt gr) * 100.0 / " +
           "(SELECT SUM(gr.receivedQuantity) FROM GoodsReceipt gr WHERE gr.status NOT IN ('REJECTED')) " +
           "FROM GoodsReceipt gr WHERE gr.status NOT IN ('REJECTED')")
    Double getRejectionRate();
    
    /**
     * Get receipts by supplier
     */
    @Query("SELECT gr.supplierId, gr.supplierName, COUNT(gr) as receiptCount, " +
           "SUM(gr.receivedQuantity) as totalReceived FROM GoodsReceipt gr " +
           "WHERE gr.status NOT IN ('REJECTED') " +
           "GROUP BY gr.supplierId, gr.supplierName ORDER BY totalReceived DESC")
    List<Object[]> getReceiptsBySupplier();
    
    /**
     * Get receipts by warehouse
     */
    @Query("SELECT gr.warehouseId, gr.warehouseName, COUNT(gr) as receiptCount, " +
           "SUM(gr.receivedQuantity) as totalReceived FROM GoodsReceipt gr " +
           "WHERE gr.status NOT IN ('REJECTED') " +
           "GROUP BY gr.warehouseId, gr.warehouseName ORDER BY totalReceived DESC")
    List<Object[]> getReceiptsByWarehouse();
    
    /**
     * Find goods receipts by tags
     */
    @Query("SELECT gr FROM GoodsReceipt gr WHERE gr.tags LIKE CONCAT('%', :tag, '%')")
    List<GoodsReceipt> findByTag(@Param("tag") String tag);
}
