package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.StockAdjustmentItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for StockAdjustmentItem entity
 * Based on ACTUAL database schema: stock_adjustment_items table
 * 
 * Fields: adjustment_id (BIGINT), product_id (BIGINT), batch_number,
 *         location_id (BIGINT), quantity_adjusted, unit_cost, total_value, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface StockAdjustmentItemRepository extends JpaRepository<StockAdjustmentItem, Long>, JpaSpecificationExecutor<StockAdjustmentItem> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all items for an adjustment
     */
    List<StockAdjustmentItem> findByAdjustmentId(Long adjustmentId);
    
    /**
     * Find all items for an adjustment with pagination
     */
    Page<StockAdjustmentItem> findByAdjustmentId(Long adjustmentId, Pageable pageable);
    
    /**
     * Find all items for a product
     */
    List<StockAdjustmentItem> findByProductId(Long productId);
    
    /**
     * Find all items for a product with pagination
     */
    Page<StockAdjustmentItem> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find items by batch number
     */
    List<StockAdjustmentItem> findByBatchNumber(String batchNumber);
    
    /**
     * Find items by location
     */
    List<StockAdjustmentItem> findByLocationId(Long locationId);
    
    /**
     * Find items by adjustment and product
     */
    List<StockAdjustmentItem> findByAdjustmentIdAndProductId(Long adjustmentId, Long productId);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count items for an adjustment
     */
    long countByAdjustmentId(Long adjustmentId);
    
    /**
     * Count items for a product
     */
    long countByProductId(Long productId);
    
    /**
     * Count items at a location
     */
    long countByLocationId(Long locationId);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all items for an adjustment
     */
    @Modifying
    @Query("DELETE FROM StockAdjustmentItem sai WHERE sai.adjustment.id = :adjustmentId")
    void deleteAllByAdjustmentId(@Param("adjustmentId") Long adjustmentId);
    
    /**
     * Delete specific item by adjustment and product
     */
    void deleteByAdjustmentIdAndProductId(Long adjustmentId, Long productId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total quantity adjusted for a product
     */
    @Query("SELECT SUM(sai.quantityAdjusted) FROM StockAdjustmentItem sai WHERE sai.product.id = :productId")
    BigDecimal getTotalQuantityAdjustedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total value for an adjustment
     */
    @Query("SELECT SUM(sai.totalValue) FROM StockAdjustmentItem sai WHERE sai.adjustment.id = :adjustmentId")
    BigDecimal getTotalValueByAdjustment(@Param("adjustmentId") Long adjustmentId);
    
    /**
     * Get total quantity for an adjustment
     */
    @Query("SELECT SUM(sai.quantityAdjusted) FROM StockAdjustmentItem sai WHERE sai.adjustment.id = :adjustmentId")
    BigDecimal getTotalQuantityByAdjustment(@Param("adjustmentId") Long adjustmentId);
    
    /**
     * Find items with quantity adjusted greater than threshold
     */
    @Query("SELECT sai FROM StockAdjustmentItem sai WHERE ABS(sai.quantityAdjusted) > :threshold")
    List<StockAdjustmentItem> findItemsAboveThreshold(@Param("threshold") BigDecimal threshold);
    
    /**
     * Find items with positive adjustments (surplus)
     */
    @Query("SELECT sai FROM StockAdjustmentItem sai WHERE sai.quantityAdjusted > 0")
    List<StockAdjustmentItem> findPositiveAdjustments();
    
    /**
     * Find items with negative adjustments (deficit)
     */
    @Query("SELECT sai FROM StockAdjustmentItem sai WHERE sai.quantityAdjusted < 0")
    List<StockAdjustmentItem> findNegativeAdjustments();
    
    /**
     * Get adjustment item statistics by product
     */
    @Query("SELECT sai.product.id, COUNT(sai) as adjustmentCount, " +
           "SUM(sai.quantityAdjusted) as totalQuantityAdjusted, SUM(sai.totalValue) as totalValue " +
           "FROM StockAdjustmentItem sai GROUP BY sai.product.id ORDER BY adjustmentCount DESC")
    List<Object[]> getAdjustmentStatisticsByProduct();
    
    /**
     * Get adjustment item statistics by location
     */
    @Query("SELECT sai.location.id, COUNT(sai) as adjustmentCount, " +
           "SUM(sai.quantityAdjusted) as totalQuantityAdjusted " +
           "FROM StockAdjustmentItem sai WHERE sai.location.id IS NOT NULL " +
           "GROUP BY sai.location.id ORDER BY adjustmentCount DESC")
    List<Object[]> getAdjustmentStatisticsByLocation();
    
    /**
     * Find items by adjustment and batch
     */
    List<StockAdjustmentItem> findByAdjustmentIdAndBatchNumber(Long adjustmentId, String batchNumber);
    
    /**
     * Find items by product and batch
     */
    List<StockAdjustmentItem> findByProductIdAndBatchNumber(Long productId, String batchNumber);
    
    /**
     * Find items with null batch number
     */
    @Query("SELECT sai FROM StockAdjustmentItem sai WHERE sai.batchNumber IS NULL")
    List<StockAdjustmentItem> findItemsWithoutBatchNumber();
    
    /**
     * Find items with null location
     */
    @Query("SELECT sai FROM StockAdjustmentItem sai WHERE sai.location.id IS NULL")
    List<StockAdjustmentItem> findItemsWithoutLocation();
    
    /**
     * Find all items ordered by adjustment
     */
    @Query("SELECT sai FROM StockAdjustmentItem sai ORDER BY sai.adjustment.id, sai.product.id")
    List<StockAdjustmentItem> findAllOrderedByAdjustment();
    
    /**
     * Get top adjusted products
     */
//    @Query("SELECT sai.product.id, SUM(ABS(sai.quantityAdjusted)) as totalAdjusted " +
//           "FROM StockAdjustmentItem sai GROUP BY sai.product.id" +
//           "ORDER totalAdjusted DESC")
//    List<Object[]> findTopAdjustedProducts(Pageable pageable);
}
