package lk.epicgreen.erp.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity
 * Represents products (raw materials, finished goods, semi-finished, packaging)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_code", columnList = "product_code"),
    @Index(name = "idx_product_name", columnList = "product_name"),
    @Index(name = "idx_product_type", columnList = "product_type"),
    @Index(name = "idx_barcode", columnList = "barcode"),
    @Index(name = "idx_category_id", columnList = "category_id"),
    @Index(name = "idx_deleted_at", columnList = "deleted_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Product code (unique identifier)
     */
    @NotBlank(message = "Product code is required")
    @Size(max = 30)
    @Column(name = "product_code", nullable = false, unique = true, length = 30)
    private String productCode;
    
    /**
     * Product name
     */
    @NotBlank(message = "Product name is required")
    @Size(max = 200)
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;
    
    /**
     * Product type (RAW_MATERIAL, FINISHED_GOOD, SEMI_FINISHED, PACKAGING)
     */
    @NotBlank(message = "Product type is required")
    @Column(name = "product_type", nullable = false, length = 20)
    private String productType;
    
    /**
     * Product category
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_product_category"))
    private ProductCategory category;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Base unit of measure
     */
    @NotNull(message = "Base UOM is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_uom_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_base_uom"))
    private UnitOfMeasure baseUom;
    
    /**
     * Barcode (unique)
     */
    @Size(max = 50)
    @Column(name = "barcode", unique = true, length = 50)
    private String barcode;
    
    /**
     * SKU - Stock Keeping Unit (unique)
     */
    @Size(max = 50)
    @Column(name = "sku", unique = true, length = 50)
    private String sku;
    
    /**
     * HSN Code (Harmonized System of Nomenclature - for tax purposes)
     */
    @Size(max = 20)
    @Column(name = "hsn_code", length = 20)
    private String hsnCode;
    
    /**
     * Reorder level (trigger for purchase)
     */
    @PositiveOrZero(message = "Reorder level must be positive or zero")
    @Column(name = "reorder_level", precision = 10, scale = 2)
    private BigDecimal reorderLevel;
    
    /**
     * Minimum stock level
     */
    @PositiveOrZero(message = "Minimum stock level must be positive or zero")
    @Column(name = "minimum_stock_level", precision = 10, scale = 2)
    private BigDecimal minimumStockLevel;
    
    /**
     * Maximum stock level
     */
    @PositiveOrZero(message = "Maximum stock level must be positive or zero")
    @Column(name = "maximum_stock_level", precision = 10, scale = 2)
    private BigDecimal maximumStockLevel;
    
    /**
     * Standard cost (average cost)
     */
    @PositiveOrZero(message = "Standard cost must be positive or zero")
    @Column(name = "standard_cost", precision = 15, scale = 2)
    private BigDecimal standardCost;
    
    /**
     * Selling price
     */
    @PositiveOrZero(message = "Selling price must be positive or zero")
    @Column(name = "selling_price", precision = 15, scale = 2)
    private BigDecimal sellingPrice;
    
    /**
     * Shelf life in days
     */
    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Product image URL
     */
    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    /**
     * Soft delete timestamp
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive) && deletedAt == null;
    }
    
    /**
     * Check if deleted
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
    
    /**
     * Check if raw material
     */
    @Transient
    public boolean isRawMaterial() {
        return "RAW_MATERIAL".equals(productType);
    }
    
    /**
     * Check if finished good
     */
    @Transient
    public boolean isFinishedGood() {
        return "FINISHED_GOOD".equals(productType);
    }
    
    /**
     * Check if semi-finished
     */
    @Transient
    public boolean isSemiFinished() {
        return "SEMI_FINISHED".equals(productType);
    }
    
    /**
     * Check if packaging material
     */
    @Transient
    public boolean isPackaging() {
        return "PACKAGING".equals(productType);
    }
    
    /**
     * Check if product needs reordering (based on current stock)
     */
    @Transient
    public boolean needsReorder(BigDecimal currentStock) {
        if (currentStock == null || reorderLevel == null) {
            return false;
        }
        return currentStock.compareTo(reorderLevel) <= 0;
    }
    
    /**
     * Check if stock is below minimum
     */
    @Transient
    public boolean isBelowMinimum(BigDecimal currentStock) {
        if (currentStock == null || minimumStockLevel == null) {
            return false;
        }
        return currentStock.compareTo(minimumStockLevel) < 0;
    }
    
    /**
     * Check if stock is above maximum
     */
    @Transient
    public boolean isAboveMaximum(BigDecimal currentStock) {
        if (currentStock == null || maximumStockLevel == null) {
            return false;
        }
        return currentStock.compareTo(maximumStockLevel) > 0;
    }
    
    /**
     * Calculate profit margin percentage
     */
    @Transient
    public BigDecimal getProfitMargin() {
        if (sellingPrice == null || standardCost == null || standardCost.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal profit = sellingPrice.subtract(standardCost);
        return profit.divide(sellingPrice, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }
    
    /**
     * Soft delete product
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.isActive = false;
    }
    
    /**
     * Restore soft deleted product
     */
    public void restore() {
        this.deletedAt = null;
        this.isActive = true;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (reorderLevel == null) {
            reorderLevel = BigDecimal.ZERO;
        }
        if (minimumStockLevel == null) {
            minimumStockLevel = BigDecimal.ZERO;
        }
        if (maximumStockLevel == null) {
            maximumStockLevel = BigDecimal.ZERO;
        }
        if (standardCost == null) {
            standardCost = BigDecimal.ZERO;
        }
        if (sellingPrice == null) {
            sellingPrice = BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
