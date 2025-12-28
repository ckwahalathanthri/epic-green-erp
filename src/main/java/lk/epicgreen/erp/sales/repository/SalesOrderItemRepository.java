package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.SalesOrderItem;
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
 * Repository interface for SalesOrderItem entity
 * Based on ACTUAL database schema: sales_order_items table
 * 
 * Fields: order_id (BIGINT), product_id (BIGINT), batch_number,
 *         quantity_ordered, quantity_delivered, quantity_pending (GENERATED/COMPUTED),
 *         uom_id (BIGINT), unit_price, discount_percentage, discount_amount,
 *         tax_rate_id (BIGINT), tax_amount, line_total, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Long>, JpaSpecificationExecutor<SalesOrderItem> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all items for an order
     */
    List<SalesOrderItem> findByOrderId(Long orderId);
    
    /**
     * Find all items for an order with pagination
     */
    Page<SalesOrderItem> findByOrderId(Long orderId, Pageable pageable);
    
    /**
     * Find all items for a product
     */
    List<SalesOrderItem> findByProductId(Long productId);
    
    /**
     * Find all items for a product with pagination
     */
    Page<SalesOrderItem> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find items by order and product
     */
    List<SalesOrderItem> findByOrderIdAndProductId(Long orderId, Long productId);
    
    /**
     * Find items by batch number
     */
    List<SalesOrderItem> findByBatchNumber(String batchNumber);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count items for an order
     */
    long countByOrderId(Long orderId);
    
    /**
     * Count items for a product
     */
    long countByProductId(Long productId);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all items for an order
     */
    @Modifying
    @Query("DELETE FROM SalesOrderItem soi WHERE soi.orderId = :orderId")
    void deleteAllByOrderId(@Param("orderId") Long orderId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total quantity ordered for a product
     */
    @Query("SELECT SUM(soi.quantityOrdered) FROM SalesOrderItem soi WHERE soi.productId = :productId")
    BigDecimal getTotalQuantityOrderedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total quantity delivered for a product
     */
    @Query("SELECT SUM(soi.quantityDelivered) FROM SalesOrderItem soi WHERE soi.productId = :productId")
    BigDecimal getTotalQuantityDeliveredByProduct(@Param("productId") Long productId);
    
    /**
     * Get total quantity pending for a product
     */
    @Query("SELECT SUM(soi.quantityOrdered - soi.quantityDelivered) FROM SalesOrderItem soi " +
           "WHERE soi.productId = :productId")
    BigDecimal getTotalQuantityPendingByProduct(@Param("productId") Long productId);
    
    /**
     * Get total value for an order
     */
    @Query("SELECT SUM(soi.lineTotal) FROM SalesOrderItem soi WHERE soi.orderId = :orderId")
    BigDecimal getTotalValueByOrder(@Param("orderId") Long orderId);
    
    /**
     * Get total quantity for an order
     */
    @Query("SELECT SUM(soi.quantityOrdered) FROM SalesOrderItem soi WHERE soi.orderId = :orderId")
    BigDecimal getTotalQuantityByOrder(@Param("orderId") Long orderId);
    
    /**
     * Find items with pending delivery
     */
    @Query("SELECT soi FROM SalesOrderItem soi WHERE soi.quantityOrdered > soi.quantityDelivered")
    List<SalesOrderItem> findItemsWithPendingDelivery();
    
    /**
     * Find items with pending delivery for an order
     */
    @Query("SELECT soi FROM SalesOrderItem soi WHERE soi.orderId = :orderId " +
           "AND soi.quantityOrdered > soi.quantityDelivered")
    List<SalesOrderItem> findPendingItemsByOrder(@Param("orderId") Long orderId);
    
    /**
     * Find fully delivered items
     */
    @Query("SELECT soi FROM SalesOrderItem soi WHERE soi.quantityOrdered = soi.quantityDelivered")
    List<SalesOrderItem> findFullyDeliveredItems();
    
    /**
     * Find partially delivered items
     */
    @Query("SELECT soi FROM SalesOrderItem soi WHERE soi.quantityDelivered > 0 " +
           "AND soi.quantityOrdered > soi.quantityDelivered")
    List<SalesOrderItem> findPartiallyDeliveredItems();
    
    /**
     * Get order item statistics by product
     */
    @Query("SELECT soi.productId, COUNT(soi) as orderCount, " +
           "SUM(soi.quantityOrdered) as totalQuantity, SUM(soi.lineTotal) as totalValue " +
           "FROM SalesOrderItem soi GROUP BY soi.productId ORDER BY totalValue DESC")
    List<Object[]> getOrderItemStatisticsByProduct();
    
    /**
     * Get top selling products
     */
    @Query("SELECT soi.productId, SUM(soi.quantityOrdered) as totalQuantity " +
           "FROM SalesOrderItem soi GROUP BY soi.productId ORDER BY totalQuantity DESC")
    List<Object[]> getTopSellingProducts(Pageable pageable);
    
    /**
     * Find items by order and batch
     */
    List<SalesOrderItem> findByOrderIdAndBatchNumber(Long orderId, String batchNumber);
    
    /**
     * Find items with discount
     */
    @Query("SELECT soi FROM SalesOrderItem soi WHERE soi.discountPercentage > 0 OR soi.discountAmount > 0")
    List<SalesOrderItem> findItemsWithDiscount();
    
    /**
     * Find all items ordered by order
     */
    @Query("SELECT soi FROM SalesOrderItem soi ORDER BY soi.orderId, soi.productId")
    List<SalesOrderItem> findAllOrderedByOrder();
}
