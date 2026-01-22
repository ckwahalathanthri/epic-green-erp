package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.InvoiceItem;
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
 * Repository interface for InvoiceItem entity
 * Based on ACTUAL database schema: invoice_items table
 * 
 * Fields: invoice_id (BIGINT), order_item_id (BIGINT), product_id (BIGINT),
 *         batch_number, quantity, uom_id (BIGINT), unit_price,
 *         discount_percentage, discount_amount, tax_rate_id (BIGINT),
 *         tax_amount, line_total
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long>, JpaSpecificationExecutor<InvoiceItem> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all items for an invoice
     */
    List<InvoiceItem> findByInvoiceId(Long invoiceId);
    
    /**
     * Find all items for an invoice with pagination
     */
    Page<InvoiceItem> findByInvoiceId(Long invoiceId, Pageable pageable);
    
    /**
     * Find all items for a product
     */
    List<InvoiceItem> findByProductId(Long productId);
    
    /**
     * Find all items for a product with pagination
     */
    Page<InvoiceItem> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find all items for an order item
     */
    List<InvoiceItem> findByOrderItemId(Long orderItemId);
    
    /**
     * Find items by invoice and product
     */
    List<InvoiceItem> findByInvoiceIdAndProductId(Long invoiceId, Long productId);
    
    /**
     * Find items by batch number
     */
    List<InvoiceItem> findByBatchNumber(String batchNumber);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count items for an invoice
     */
    long countByInvoiceId(Long invoiceId);
    
    /**
     * Count items for a product
     */
    long countByProductId(Long productId);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all items for an invoice
     */
    @Modifying
    @Query("DELETE FROM InvoiceItem ii WHERE ii.invoice.id = :invoiceId")
    void deleteAllByInvoiceId(@Param("invoiceId") Long invoiceId);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Get total quantity invoiced for a product
     */
    @Query("SELECT SUM(ii.quantity) FROM InvoiceItem ii WHERE ii.product.id = :productId")
    BigDecimal getTotalQuantityInvoicedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total value for an invoice
     */
    @Query("SELECT SUM(ii.lineTotal) FROM InvoiceItem ii WHERE ii.invoice.id = :invoiceId")
    BigDecimal getTotalValueByInvoice(@Param("invoiceId") Long invoiceId);
    
    /**
     * Get total quantity for an invoice
     */
    @Query("SELECT SUM(ii.quantity) FROM InvoiceItem ii WHERE ii.invoice.id = :invoiceId")
    BigDecimal getTotalQuantityByInvoice(@Param("invoiceId") Long invoiceId);
    
    /**
     * Get total tax for an invoice
     */
    @Query("SELECT SUM(ii.taxAmount) FROM InvoiceItem ii WHERE ii.invoice.id = :invoiceId")
    BigDecimal getTotalTaxByInvoice(@Param("invoiceId") Long invoiceId);
    
    /**
     * Get total discount for an invoice
     */
    @Query("SELECT SUM(ii.discountAmount) FROM InvoiceItem ii WHERE ii.invoice.id = :invoiceId")
    BigDecimal getTotalDiscountByInvoice(@Param("invoiceId") Long invoiceId);
    
    /**
     * Get invoice item statistics by product
     */
    @Query("SELECT ii.product.id, COUNT(ii) as invoiceCount, " +
           "SUM(ii.quantity) as totalQuantity, SUM(ii.lineTotal) as totalValue " +
           "FROM InvoiceItem ii GROUP BY ii.product.id ORDER BY totalValue DESC")
    List<Object[]> getInvoiceItemStatisticsByProduct();
    
    /**
     * Get top invoiced products
     */
    @Query("SELECT ii.product.id, SUM(ii.quantity) as totalQuantity, SUM(ii.lineTotal) as totalValue " +
           "FROM InvoiceItem ii GROUP BY ii.product.id ORDER BY totalValue DESC")
    List<Object[]> getTopInvoicedProducts(Pageable pageable);
    
    /**
     * Get top revenue generating products
     */
    @Query("SELECT ii.product.id, SUM(ii.lineTotal) as totalRevenue " +
           "FROM InvoiceItem ii GROUP BY ii.product.id ORDER BY totalRevenue DESC")
    List<Object[]> getTopRevenueProducts(Pageable pageable);
    
    /**
     * Find items by invoice and batch
     */
    List<InvoiceItem> findByInvoiceIdAndBatchNumber(Long invoiceId, String batchNumber);
    
    /**
     * Find items by product and batch
     */
    List<InvoiceItem> findByProductIdAndBatchNumber(Long productId, String batchNumber);
    
    /**
     * Find items with discount
     */
    @Query("SELECT ii FROM InvoiceItem ii WHERE ii.discountPercentage > 0 OR ii.discountAmount > 0")
    List<InvoiceItem> findItemsWithDiscount();
    
    /**
     * Find items with tax
     */
    @Query("SELECT ii FROM InvoiceItem ii WHERE ii.taxAmount > 0")
    List<InvoiceItem> findItemsWithTax();
    
    /**
     * Find all items ordered by invoice
     */
    @Query("SELECT ii FROM InvoiceItem ii ORDER BY ii.invoice.id, ii.product.id")
    List<InvoiceItem> findAllOrderedByInvoice();
    
    /**
     * Get invoice items by tax rate
     */
    List<InvoiceItem> findByTaxRateId(Long taxRateId);
    
    /**
     * Get items grouped by tax rate
     */
    @Query("SELECT ii.taxRate.id, COUNT(ii) as itemCount, SUM(ii.taxAmount) as totalTax " +
           "FROM InvoiceItem ii WHERE ii.taxRate.id IS NOT NULL " +
           "GROUP BY ii.taxRate.id ORDER BY totalTax DESC")
    List<Object[]> getItemsByTaxRate();
}
