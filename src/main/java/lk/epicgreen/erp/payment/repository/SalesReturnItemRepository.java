package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.SalesReturnItem;
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
 * Repository interface for SalesReturnItem entity
 * Based on ACTUAL database schema: sales_return_items table
 * 
 * Fields: return_id (BIGINT), product_id (BIGINT), batch_number,
 *         quantity_returned, uom_id (BIGINT), unit_price,
 *         tax_rate_id (BIGINT), tax_amount, line_total, return_reason,
 *         disposition (ENUM: RESALEABLE, DAMAGED, EXPIRED, SCRAP), remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SalesReturnItemRepository extends JpaRepository<SalesReturnItem, Long>, JpaSpecificationExecutor<SalesReturnItem> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all items for a return
     */
//    List<SalesReturnItem> findByReturnId(Long returnId);
//
//    /**
//     * Find all items for a return with pagination
//     */
//    Page<SalesReturnItem> findByReturnId(Long returnId, Pageable pageable);
    
    /**
     * Find all items for a product
     */
    List<SalesReturnItem> findByProductId(Long productId);
    
    /**
     * Find all items for a product with pagination
     */
    Page<SalesReturnItem> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find items by return and product
     */
//    List<SalesReturnItem> findByReturnIdAndProductId(Long returnId, Long productId);
    
    /**
     * Find items by batch number
     */
    List<SalesReturnItem> findByBatchNumber(String batchNumber);
    
    /**
     * Find items by disposition
     */
//    List<SalesReturnItem> findByDisposition(String disposition);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count items for a return
     */
//    long countByReturnId(Long returnId);
//
//    /**
//     * Count items for a product
//     */
//    long countByProductId(Long productId);
//
//    /**
//     * Count items by disposition
//     */
//    long countByDisposition(String disposition);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all items for a return
     */
    @Modifying
    @Query("DELETE FROM SalesReturnItem sri WHERE sri.id = :returnId")
    void deleteAllByReturnId(@Param("returnId") Long returnId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total quantity returned for a product
     */
    @Query("SELECT SUM(sri.quantityReturned) FROM SalesReturnItem sri WHERE sri.product.id = :productId")
    BigDecimal getTotalQuantityReturnedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total value for a return
     */
    @Query("SELECT SUM(sri.lineTotal) FROM SalesReturnItem sri WHERE sri.id = :returnId")
    BigDecimal getTotalValueByReturn(@Param("returnId") Long returnId);
    
    /**
     * Get total quantity for a return
     */
    @Query("SELECT SUM(sri.quantityReturned) FROM SalesReturnItem sri WHERE sri.id = :returnId")
    BigDecimal getTotalQuantityByReturn(@Param("returnId") Long returnId);
    
    /**
     * Get total tax for a return
     */
    @Query("SELECT SUM(sri.taxAmount) FROM SalesReturnItem sri WHERE sri.id = :returnId")
    BigDecimal getTotalTaxByReturn(@Param("returnId") Long returnId);
    
    /**
     * Find resaleable items
     */
    @Query("SELECT sri FROM SalesReturnItem sri WHERE sri.disposition = 'RESALEABLE'")
    List<SalesReturnItem> findResaleableItems();
    
    /**
     * Find damaged items
     */
    @Query("SELECT sri FROM SalesReturnItem sri WHERE sri.disposition = 'DAMAGED'")
    List<SalesReturnItem> findDamagedItems();
    
    /**
     * Find expired items
     */
    @Query("SELECT sri FROM SalesReturnItem sri WHERE sri.disposition = 'EXPIRED'")
    List<SalesReturnItem> findExpiredItems();
    
    /**
     * Find scrap items
     */
    @Query("SELECT sri FROM SalesReturnItem sri WHERE sri.disposition = 'SCRAP'")
    List<SalesReturnItem> findScrapItems();
    
    /**
     * Get return item statistics by product
     */
    @Query("SELECT sri.product.id, COUNT(sri) as returnCount, " +
           "SUM(sri.quantityReturned) as totalQuantity, SUM(sri.lineTotal) as totalValue " +
           "FROM SalesReturnItem sri GROUP BY sri.product.id ORDER BY totalValue DESC")
    List<Object[]> getReturnItemStatisticsByProduct();
    
    /**
     * Get return item statistics by disposition
     */
    @Query("SELECT sri.disposition, COUNT(sri) as returnCount, " +
           "SUM(sri.quantityReturned) as totalQuantity, SUM(sri.lineTotal) as totalValue " +
           "FROM SalesReturnItem sri GROUP BY sri.disposition ORDER BY totalValue DESC")
    List<Object[]> getReturnItemStatisticsByDisposition();
    
    /**
     * Get top returned products
     */
    @Query("SELECT sri.product.id, SUM(sri.quantityReturned) as totalQuantity, SUM(sri.lineTotal) as totalValue " +
           "FROM SalesReturnItem sri GROUP BY sri.product.id ORDER BY totalValue DESC")
    List<Object[]> getTopReturnedProducts(Pageable pageable);
    
    /**
     * Find items by return and batch
     */
//    List<SalesReturnItem> findByReturnIdAndBatchNumber(Long returnId, String batchNumber);
    
    /**
     * Find items by product and batch
     */
//    List<SalesReturnItem> findByProductIdAndBatchNumber(Long productId, String batchNumber);
    
    /**
     * Find all items ordered by return
     */
    @Query("SELECT sri FROM SalesReturnItem sri ORDER BY sri.id, sri.id")
    List<SalesReturnItem> findAllOrderedByReturn();
    
    /**
     * Get return value by disposition
     */
    @Query("SELECT SUM(sri.lineTotal) FROM SalesReturnItem sri WHERE sri.disposition = :disposition")
    BigDecimal getTotalValueByDisposition(@Param("disposition") String disposition);
}
