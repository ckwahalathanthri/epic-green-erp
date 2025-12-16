package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionOutput;
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
 * ProductionOutput Repository
 * Repository for production output data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ProductionOutputRepository extends JpaRepository<ProductionOutput, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find production output by output number
     */
    Optional<ProductionOutput> findByOutputNumber(String outputNumber);
    
    /**
     * Check if production output exists by output number
     */
    boolean existsByOutputNumber(String outputNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find production outputs by work order ID
     */
    List<ProductionOutput> findByWorkOrderId(Long workOrderId);
    
    /**
     * Find production outputs by work order ID with pagination
     */
    Page<ProductionOutput> findByWorkOrderId(Long workOrderId, Pageable pageable);
    
    /**
     * Find production outputs by product ID
     */
    List<ProductionOutput> findByProductId(Long productId);
    
    /**
     * Find production outputs by product ID with pagination
     */
    Page<ProductionOutput> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find production outputs by warehouse ID
     */
    List<ProductionOutput> findByWarehouseId(Long warehouseId);
    
    /**
     * Find production outputs by production line ID
     */
    List<ProductionOutput> findByProductionLineId(Long productionLineId);
    
    /**
     * Find production outputs by status
     */
    List<ProductionOutput> findByStatus(String status);
    
    /**
     * Find production outputs by status with pagination
     */
    Page<ProductionOutput> findByStatus(String status, Pageable pageable);
    
    /**
     * Find production outputs by output type
     */
    List<ProductionOutput> findByOutputType(String outputType);
    
    /**
     * Find production outputs by supervisor ID
     */
    List<ProductionOutput> findBySupervisorId(Long supervisorId);
    
    /**
     * Find production outputs by recorded by user
     */
    List<ProductionOutput> findByRecordedByUserId(Long userId);
    
    /**
     * Find production outputs by verified by user
     */
    List<ProductionOutput> findByVerifiedByUserId(Long userId);
    
    /**
     * Find production outputs by is verified
     */
    List<ProductionOutput> findByIsVerified(Boolean isVerified);
    
    /**
     * Find production outputs by is posted to inventory
     */
    List<ProductionOutput> findByIsPostedToInventory(Boolean isPostedToInventory);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find production outputs by production date between dates
     */
    List<ProductionOutput> findByProductionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find production outputs by production date between dates with pagination
     */
    Page<ProductionOutput> findByProductionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find production outputs by verified date between dates
     */
    List<ProductionOutput> findByVerifiedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find production outputs by created at between dates
     */
    List<ProductionOutput> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find production outputs by work order ID and status
     */
    List<ProductionOutput> findByWorkOrderIdAndStatus(Long workOrderId, String status);
    
    /**
     * Find production outputs by product ID and status
     */
    List<ProductionOutput> findByProductIdAndStatus(Long productId, String status);
    
    /**
     * Find production outputs by warehouse ID and status
     */
    List<ProductionOutput> findByWarehouseIdAndStatus(Long warehouseId, String status);
    
    /**
     * Find production outputs by production line and status
     */
    List<ProductionOutput> findByProductionLineIdAndStatus(Long productionLineId, String status);
    
    /**
     * Find production outputs by is verified and status
     */
    List<ProductionOutput> findByIsVerifiedAndStatus(Boolean isVerified, String status);
    
    /**
     * Find production outputs by is posted and status
     */
    List<ProductionOutput> findByIsPostedToInventoryAndStatus(Boolean isPosted, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search production outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE " +
           "LOWER(po.outputNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(po.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(po.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ProductionOutput> searchProductionOutputs(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find pending production outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.status = 'PENDING' " +
           "ORDER BY po.productionDate DESC")
    List<ProductionOutput> findPendingProductionOutputs();
    
    /**
     * Find verified production outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.status = 'VERIFIED' " +
           "AND po.isVerified = true " +
           "ORDER BY po.verifiedDate DESC")
    List<ProductionOutput> findVerifiedProductionOutputs();
    
    /**
     * Find posted production outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.status = 'POSTED' " +
           "AND po.isPostedToInventory = true " +
           "ORDER BY po.productionDate DESC")
    List<ProductionOutput> findPostedProductionOutputs();
    
    /**
     * Find rejected production outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.status = 'REJECTED' " +
           "ORDER BY po.productionDate DESC")
    List<ProductionOutput> findRejectedProductionOutputs();
    
    /**
     * Find unverified production outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.isVerified = false " +
           "AND po.status NOT IN ('REJECTED') " +
           "ORDER BY po.productionDate ASC")
    List<ProductionOutput> findUnverifiedProductionOutputs();
    
    /**
     * Find unposted production outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.isPostedToInventory = false " +
           "AND po.isVerified = true " +
           "AND po.status = 'VERIFIED' " +
           "ORDER BY po.verifiedDate ASC")
    List<ProductionOutput> findUnpostedProductionOutputs();
    
    /**
     * Find today's production outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.productionDate = :today " +
           "ORDER BY po.createdAt DESC")
    List<ProductionOutput> findTodaysProductionOutputs(@Param("today") LocalDate today);
    
    /**
     * Find production line outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.productionLineId = :productionLineId " +
           "ORDER BY po.productionDate DESC")
    List<ProductionOutput> findProductionLineOutputs(@Param("productionLineId") Long productionLineId);
    
    /**
     * Find supervisor outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.supervisorId = :supervisorId " +
           "ORDER BY po.productionDate DESC")
    List<ProductionOutput> findSupervisorOutputs(@Param("supervisorId") Long supervisorId);
    
    /**
     * Find recent production outputs
     */
    @Query("SELECT po FROM ProductionOutput po ORDER BY po.productionDate DESC, po.createdAt DESC")
    List<ProductionOutput> findRecentProductionOutputs(Pageable pageable);
    
    /**
     * Find product recent outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.productId = :productId " +
           "ORDER BY po.productionDate DESC, po.createdAt DESC")
    List<ProductionOutput> findProductRecentOutputs(@Param("productId") Long productId, Pageable pageable);
    
    /**
     * Find work order recent outputs
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.workOrderId = :workOrderId " +
           "ORDER BY po.productionDate DESC, po.createdAt DESC")
    List<ProductionOutput> findWorkOrderRecentOutputs(@Param("workOrderId") Long workOrderId, Pageable pageable);
    
    /**
     * Find production outputs by date range and status
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.productionDate BETWEEN :startDate AND :endDate " +
           "AND po.status = :status ORDER BY po.productionDate DESC")
    List<ProductionOutput> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("status") String status);
    
    /**
     * Get total output by work order
     */
    @Query("SELECT SUM(po.outputQuantity) FROM ProductionOutput po " +
           "WHERE po.workOrderId = :workOrderId AND po.status NOT IN ('REJECTED')")
    Double getTotalOutputByWorkOrder(@Param("workOrderId") Long workOrderId);
    
    /**
     * Get total good quantity by work order
     */
    @Query("SELECT SUM(po.goodQuantity) FROM ProductionOutput po " +
           "WHERE po.workOrderId = :workOrderId AND po.status NOT IN ('REJECTED')")
    Double getTotalGoodQuantityByWorkOrder(@Param("workOrderId") Long workOrderId);
    
    /**
     * Get total rejected quantity by work order
     */
    @Query("SELECT SUM(po.rejectedQuantity) FROM ProductionOutput po " +
           "WHERE po.workOrderId = :workOrderId")
    Double getTotalRejectedQuantityByWorkOrder(@Param("workOrderId") Long workOrderId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count production outputs by work order
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.workOrderId = :workOrderId")
    Long countByWorkOrderId(@Param("workOrderId") Long workOrderId);
    
    /**
     * Count production outputs by product
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.productId = :productId")
    Long countByProductId(@Param("productId") Long productId);
    
    /**
     * Count production outputs by status
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count unverified production outputs
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.isVerified = false " +
           "AND po.status NOT IN ('REJECTED')")
    Long countUnverifiedProductionOutputs();
    
    /**
     * Count unposted production outputs
     */
    @Query("SELECT COUNT(po) FROM ProductionOutput po WHERE po.isPostedToInventory = false " +
           "AND po.isVerified = true")
    Long countUnpostedProductionOutputs();
    
    /**
     * Get output type distribution
     */
    @Query("SELECT po.outputType, COUNT(po) as outputCount FROM ProductionOutput po " +
           "GROUP BY po.outputType ORDER BY outputCount DESC")
    List<Object[]> getOutputTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT po.status, COUNT(po) as outputCount FROM ProductionOutput po " +
           "GROUP BY po.status ORDER BY outputCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get monthly production output
     */
    @Query("SELECT YEAR(po.productionDate) as year, MONTH(po.productionDate) as month, " +
           "COUNT(po) as outputCount, SUM(po.outputQuantity) as totalQuantity FROM ProductionOutput po " +
           "WHERE po.productionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(po.productionDate), MONTH(po.productionDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyProductionOutput(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
    
    /**
     * Get total output quantity
     */
    @Query("SELECT SUM(po.outputQuantity) FROM ProductionOutput po " +
           "WHERE po.status NOT IN ('REJECTED')")
    Double getTotalOutputQuantity();
    
    /**
     * Get total good quantity
     */
    @Query("SELECT SUM(po.goodQuantity) FROM ProductionOutput po " +
           "WHERE po.status NOT IN ('REJECTED')")
    Double getTotalGoodQuantity();
    
    /**
     * Get total rejected quantity
     */
    @Query("SELECT SUM(po.rejectedQuantity) FROM ProductionOutput po")
    Double getTotalRejectedQuantity();
    
    /**
     * Get quality rate
     */
    @Query("SELECT " +
           "(SELECT SUM(po.goodQuantity) FROM ProductionOutput po WHERE po.status NOT IN ('REJECTED')) * 100.0 / " +
           "(SELECT SUM(po.outputQuantity) FROM ProductionOutput po WHERE po.status NOT IN ('REJECTED')) " +
           "FROM ProductionOutput po WHERE po.status NOT IN ('REJECTED')")
    Double getQualityRate();
    
    /**
     * Get rejection rate
     */
    @Query("SELECT " +
           "(SELECT SUM(po.rejectedQuantity) FROM ProductionOutput po) * 100.0 / " +
           "(SELECT SUM(po.outputQuantity) FROM ProductionOutput po WHERE po.status NOT IN ('REJECTED')) " +
           "FROM ProductionOutput po WHERE po.status NOT IN ('REJECTED')")
    Double getRejectionRate();
    
    /**
     * Get production by product
     */
    @Query("SELECT po.productId, po.productName, COUNT(po) as outputCount, " +
           "SUM(po.outputQuantity) as totalQuantity FROM ProductionOutput po " +
           "WHERE po.status NOT IN ('REJECTED') " +
           "GROUP BY po.productId, po.productName ORDER BY totalQuantity DESC")
    List<Object[]> getProductionByProduct();
    
    /**
     * Get production by production line
     */
    @Query("SELECT po.productionLineId, po.productionLineName, COUNT(po) as outputCount, " +
           "SUM(po.outputQuantity) as totalQuantity FROM ProductionOutput po " +
           "WHERE po.status NOT IN ('REJECTED') AND po.productionLineId IS NOT NULL " +
           "GROUP BY po.productionLineId, po.productionLineName ORDER BY totalQuantity DESC")
    List<Object[]> getProductionByProductionLine();
    
    /**
     * Find production outputs by tags
     */
    @Query("SELECT po FROM ProductionOutput po WHERE po.tags LIKE CONCAT('%', :tag, '%')")
    List<ProductionOutput> findByTag(@Param("tag") String tag);
}
