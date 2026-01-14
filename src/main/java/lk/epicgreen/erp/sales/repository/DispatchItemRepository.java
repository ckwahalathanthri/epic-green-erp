package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.DispatchItem;
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
 * Repository interface for DispatchItem entity
 * Based on ACTUAL database schema: dispatch_items table
 * 
 * Fields: dispatch_id (BIGINT), order_item_id (BIGINT), product_id (BIGINT),
 *         batch_number, quantity_dispatched, uom_id (BIGINT),
 *         location_id (BIGINT), remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface DispatchItemRepository extends JpaRepository<DispatchItem, Long>, JpaSpecificationExecutor<DispatchItem> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all items for a dispatch note
     */
    List<DispatchItem> findByDispatchId(Long dispatchId);
    
    /**
     * Find all items for a dispatch note with pagination
     */
    Page<DispatchItem> findByDispatchId(Long dispatchId, Pageable pageable);
    
    /**
     * Find all items for an order item
     */
    List<DispatchItem> findByOrderItemId(Long orderItemId);
    
    /**
     * Find all items for a product
     */
    List<DispatchItem> findByProductId(Long productId);
    
    /**
     * Find all items for a product with pagination
     */
    Page<DispatchItem> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find items by dispatch and product
     */
    List<DispatchItem> findByDispatchIdAndProductId(Long dispatchId, Long productId);
    
    /**
     * Find items by batch number
     */
    List<DispatchItem> findByBatchNumber(String batchNumber);
    
    /**
     * Find items by location
     */
    List<DispatchItem> findByLocationId(Long locationId);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count items for a dispatch note
     */
    long countByDispatchId(Long dispatchId);
    
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
     * Delete all items for a dispatch note
     */
    @Modifying
    @Query("DELETE FROM DispatchItem di WHERE di.dispatch.id = :dispatchId")
    void deleteAllByDispatchId(@Param("dispatchId") Long dispatchId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total quantity dispatched for a product
     */
    @Query("SELECT SUM(di.quantityDispatched) FROM DispatchItem di WHERE di.product.id = :productId")
    BigDecimal getTotalQuantityDispatchedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total quantity dispatched for an order item
     */
    @Query("SELECT SUM(di.quantityDispatched) FROM DispatchItem di WHERE di.orderItem.id = :orderItemId")
    BigDecimal getTotalQuantityDispatchedByOrderItem(@Param("orderItemId") Long orderItemId);
    
    /**
     * Get total quantity for a dispatch
     */
    @Query("SELECT SUM(di.quantityDispatched) FROM DispatchItem di WHERE di.dispatch.id = :dispatchId")
    BigDecimal getTotalQuantityByDispatch(@Param("dispatchId") Long dispatchId);
    
    /**
     * Get dispatch item statistics by product
     */
    @Query("SELECT di.product.id, COUNT(di) as dispatchCount, " +
           "SUM(di.quantityDispatched) as totalQuantity " +
           "FROM DispatchItem di GROUP BY di.product.id ORDER BY totalQuantity DESC")
    List<Object[]> getDispatchItemStatisticsByProduct();
    
    /**
     * Get dispatch item statistics by location
     */
    @Query("SELECT di.location.id, COUNT(di) as dispatchCount, " +
           "SUM(di.quantityDispatched) as totalQuantity " +
           "FROM DispatchItem di WHERE di.location.id IS NOT NULL " +
           "GROUP BY di.location.id ORDER BY totalQuantity DESC")
    List<Object[]> getDispatchItemStatisticsByLocation();
    
    /**
     * Find items by dispatch and batch
     */
    List<DispatchItem> findByDispatchIdAndBatchNumber(Long dispatchId, String batchNumber);
    
    /**
     * Find items by product and batch
     */
    List<DispatchItem> findByProductIdAndBatchNumber(Long productId, String batchNumber);
    
    /**
     * Find items with null location
     */
    @Query("SELECT di FROM DispatchItem di WHERE di.location.id IS NULL")
    List<DispatchItem> findItemsWithoutLocation();
    
    /**
     * Find all items ordered by dispatch
     */
    @Query("SELECT di FROM DispatchItem di ORDER BY di.dispatch.id, di.product.id")
    List<DispatchItem> findAllOrderedByDispatch();
    
    /**
     * Get top dispatched products
     */
    @Query("SELECT di.product.id, SUM(di.quantityDispatched) as totalQuantity " +
           "FROM DispatchItem di GROUP BY di.product.id ORDER BY totalQuantity DESC")
    List<Object[]> getTopDispatchedProducts(Pageable pageable);
}
