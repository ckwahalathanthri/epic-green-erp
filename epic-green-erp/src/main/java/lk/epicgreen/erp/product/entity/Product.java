package lk.epicgreen.erp.product.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Product entity
 * Represents finished spice products ready for sale
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_code", columnList = "product_code"),
    @Index(name = "idx_product_name", columnList = "product_name"),
    @Index(name = "idx_product_category", columnList = "category_id"),
    @Index(name = "idx_product_status", columnList = "status"),
    @Index(name = "idx_product_barcode", columnList = "barcode"),
    @Index(name = "idx_product_sku", columnList = "sku")
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
    @Column(name = "product_code", nullable = false, unique = true, length = 20)
    private String productCode;
    
    /**
     * Product name
     */
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;
    
    /**
     * Product description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Product category
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_category"))
    private ProductCategory category;
    
    /**
     * SKU (Stock Keeping Unit)
     */
    @Column(name = "sku", unique = true, length = 50)
    private String sku;
    
    /**
     * Barcode (EAN-13, UPC, etc.)
     */
    @Column(name = "barcode", unique = true, length = 50)
    private String barcode;
    
    /**
     * HSN code (Harmonized System of Nomenclature) for taxation
     */
    @Column(name = "hsn_code", length = 20)
    private String hsnCode;
    
    /**
     * Product type (FINISHED_GOOD, SEMI_FINISHED, RAW_MATERIAL)
     */
    @Column(name = "product_type", nullable = false, length = 30)
    private String productType;
    
    /**
     * Brand name
     */
    @Column(name = "brand", length = 100)
    private String brand;
    
    /**
     * Manufacturer/Producer
     */
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;
    
    /**
     * Country of origin
     */
    @Column(name = "country_of_origin", length = 100)
    private String countryOfOrigin;
    
    /**
     * Base unit of measurement (KG, G, L, ML, PCS, etc.)
     */
    @Column(name = "base_unit", nullable = false, length = 10)
    private String baseUnit;
    
    /**
     * Package size/weight (e.g., 100 for 100g)
     */
    @Column(name = "package_size", precision = 10, scale = 3)
    private BigDecimal packageSize;
    
    /**
     * Package unit (G, KG, ML, L)
     */
    @Column(name = "package_unit", length = 10)
    private String packageUnit;
    
    /**
     * Package type (POUCH, BOTTLE, JAR, BOX, BAG)
     */
    @Column(name = "package_type", length = 50)
    private String packageType;
    
    /**
     * Units per case/carton
     */
    @Column(name = "units_per_case")
    private Integer unitsPerCase;
    
    /**
     * Shelf life in days
     */
    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;
    
    /**
     * Storage conditions
     */
    @Column(name = "storage_conditions", length = 500)
    private String storageConditions;
    
    /**
     * Cost price (per unit)
     */
    @Column(name = "cost_price", precision = 15, scale = 2)
    private BigDecimal costPrice;
    
    /**
     * Selling price (per unit)
     */
    @Column(name = "selling_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal sellingPrice;
    
    /**
     * MRP (Maximum Retail Price)
     */
    @Column(name = "mrp", precision = 15, scale = 2)
    private BigDecimal mrp;
    
    /**
     * Wholesale price
     */
    @Column(name = "wholesale_price", precision = 15, scale = 2)
    private BigDecimal wholesalePrice;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Tax rate (percentage)
     */
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate;
    
    /**
     * Is taxable
     */
    @Column(name = "is_taxable")
    private Boolean isTaxable;
    
    /**
     * Current stock quantity
     */
    @Column(name = "current_stock", precision = 15, scale = 3)
    private BigDecimal currentStock;
    
    /**
     * Minimum stock level (reorder point)
     */
    @Column(name = "minimum_stock", precision = 15, scale = 3)
    private BigDecimal minimumStock;
    
    /**
     * Maximum stock level
     */
    @Column(name = "maximum_stock", precision = 15, scale = 3)
    private BigDecimal maximumStock;
    
    /**
     * Reorder quantity
     */
    @Column(name = "reorder_quantity", precision = 15, scale = 3)
    private BigDecimal reorderQuantity;
    
    /**
     * Stock location
     */
    @Column(name = "stock_location", length = 100)
    private String stockLocation;
    
    /**
     * Minimum order quantity
     */
    @Column(name = "minimum_order_quantity", precision = 15, scale = 3)
    private BigDecimal minimumOrderQuantity;
    
    /**
     * Maximum order quantity
     */
    @Column(name = "maximum_order_quantity", precision = 15, scale = 3)
    private BigDecimal maximumOrderQuantity;
    
    /**
     * Quality specifications (JSON or text)
     */
    @Column(name = "quality_specs", columnDefinition = "TEXT")
    private String qualitySpecs;
    
    /**
     * Moisture content percentage
     */
    @Column(name = "moisture_content", precision = 5, scale = 2)
    private BigDecimal moistureContent;
    
    /**
     * Purity percentage
     */
    @Column(name = "purity", precision = 5, scale = 2)
    private BigDecimal purity;
    
    /**
     * Product weight (net weight)
     */
    @Column(name = "weight", precision = 10, scale = 3)
    private BigDecimal weight;
    
    /**
     * Weight unit (KG, G)
     */
    @Column(name = "weight_unit", length = 10)
    private String weightUnit;
    
    /**
     * Dimensions (L x W x H in cm)
     */
    @Column(name = "dimensions", length = 50)
    private String dimensions;
    
    /**
     * Product image URL
     */
    @Column(name = "image_url", length = 255)
    private String imageUrl;
    
    /**
     * Product images (multiple, comma-separated URLs or JSON)
     */
    @Column(name = "images", columnDefinition = "TEXT")
    private String images;
    
    /**
     * Is featured product
     */
    @Column(name = "is_featured")
    private Boolean isFeatured;
    
    /**
     * Is active for sale
     */
    @Column(name = "is_active_for_sale")
    private Boolean isActiveForSale;
    
    /**
     * Is active for purchase/production
     */
    @Column(name = "is_active_for_purchase")
    private Boolean isActiveForPurchase;
    
    /**
     * Status (ACTIVE, INACTIVE, DISCONTINUED, OUT_OF_STOCK)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Discontinued date
     */
    @Column(name = "discontinued_date")
    private LocalDate discontinuedDate;
    
    /**
     * Launch date
     */
    @Column(name = "launch_date")
    private LocalDate launchDate;
    
    /**
     * Batch tracking enabled
     */
    @Column(name = "batch_tracking_enabled")
    private Boolean batchTrackingEnabled;
    
    /**
     * Serial number tracking enabled
     */
    @Column(name = "serial_tracking_enabled")
    private Boolean serialTrackingEnabled;
    
    /**
     * Meta keywords (for online catalog)
     */
    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;
    
    /**
     * Meta description (for online catalog)
     */
    @Column(name = "meta_description", length = 500)
    private String metaDescription;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Category ID (denormalized for performance)
     */
    @Column(name = "category_id_direct")
    private Long categoryId;
    
    /**
     * Short description (brief product summary)
     */
    @Column(name = "short_description", length = 500)
    private String shortDescription;
    
    /**
     * Unit of measure (KG, G, L, ML, PCS, BOX, etc.)
     * Alias for baseUnit for API compatibility
     */
    @Transient
    private String unitOfMeasure;
    
    /**
     * Discount percentage
     */
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    /**
     * Discount amount
     */
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;
    
    /**
     * Tax percentage
     */
    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;
    
    /**
     * Is active (product is available for transactions)
     * Alias for status-based active check
     */
    @Transient
    private Boolean isActive;
    
    /**
     * Can be sold (product can be sold to customers)
     * Alias for isActiveForSale
     */
    @Transient
    private Boolean canBeSold;
    
    /**
     * Can be purchased (product can be purchased from suppliers)
     * Alias for isActiveForPurchase
     */
    @Transient
    private Boolean canBePurchased;
    
    /**
     * Discontinued reason
     */
    @Column(name = "discontinued_reason", length = 500)
    private String discontinuedReason;
    
    /**
     * Standard cost (for costing purposes)
     * Alias for costPrice
     */
    @Transient
    private BigDecimal standardCost;
    
    /**
     * Minimum price (lowest selling price allowed)
     * Alias for wholesalePrice
     */
    @Transient
    private BigDecimal minimumPrice;
    
    /**
     * Reorder level (minimum stock before reordering)
     * Alias for minimumStock
     */
    @Transient
    private BigDecimal reorderLevel;
    
    /**
     * Available stock (for display, same as currentStock)
     */
    @Transient
    private BigDecimal availableStock;
    
    /**
     * Reserved stock (allocated but not yet dispatched)
     */
    @Column(name = "reserved_stock", precision = 15, scale = 3)
    private BigDecimal reservedStock;
    
    /**
     * Gets category ID
     */
    @Transient
    public Long getCategoryId() {
        return category != null ? category.getId() : categoryId;
    }
    
    /**
     * Sets category ID (for denormalized access)
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * Gets unit of measure (alias for baseUnit)
     */
    public String getUnitOfMeasure() {
        return this.baseUnit;
    }
    
    /**
     * Sets unit of measure (alias for baseUnit)
     */
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.baseUnit = unitOfMeasure;
        this.unitOfMeasure = unitOfMeasure;
    }
    
    /**
     * Gets is active status
     */
    public Boolean getIsActive() {
        return "ACTIVE".equals(status) && Boolean.TRUE.equals(isActiveForSale);
    }
    
    /**
     * Sets is active status
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        if (isActive != null) {
            this.status = isActive ? "ACTIVE" : "INACTIVE";
            this.isActiveForSale = isActive;
        }
    }
    
    /**
     * Gets can be sold (alias for isActiveForSale)
     */
    public Boolean getCanBeSold() {
        return this.isActiveForSale;
    }
    
    /**
     * Sets can be sold (alias for isActiveForSale)
     */
    public void setCanBeSold(Boolean canBeSold) {
        this.isActiveForSale = canBeSold;
        this.canBeSold = canBeSold;
    }
    
    /**
     * Gets can be purchased (alias for isActiveForPurchase)
     */
    public Boolean getCanBePurchased() {
        return this.isActiveForPurchase;
    }
    
    /**
     * Sets can be purchased (alias for isActiveForPurchase)
     */
    public void setCanBePurchased(Boolean canBePurchased) {
        this.isActiveForPurchase = canBePurchased;
        this.canBePurchased = canBePurchased;
    }
    
    /**
     * Gets standard cost (alias for costPrice)
     */
    public BigDecimal getStandardCost() {
        return this.costPrice;
    }
    
    /**
     * Sets standard cost (alias for costPrice)
     */
    public void setStandardCost(BigDecimal standardCost) {
        this.costPrice = standardCost;
        this.standardCost = standardCost;
    }
    
    /**
     * Gets minimum price (alias for wholesalePrice)
     */
    public BigDecimal getMinimumPrice() {
        return this.wholesalePrice;
    }
    
    /**
     * Sets minimum price (alias for wholesalePrice)
     */
    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.wholesalePrice = minimumPrice;
        this.minimumPrice = minimumPrice;
    }
    
    /**
     * Gets reorder level (alias for minimumStock)
     */
    public BigDecimal getReorderLevel() {
        return this.minimumStock;
    }
    
    /**
     * Sets reorder level (alias for minimumStock)
     */
    public void setReorderLevel(BigDecimal reorderLevel) {
        this.minimumStock = reorderLevel;
        this.reorderLevel = reorderLevel;
    }
    
    /**
     * Gets available stock (currentStock - reservedStock)
     */
    public BigDecimal getAvailableStock() {
        if (currentStock == null) {
            return BigDecimal.ZERO;
        }
        if (reservedStock == null) {
            return currentStock;
        }
        return currentStock.subtract(reservedStock);
    }
    
    /**
     * Sets available stock (for convenience, sets currentStock)
     */
    public void setAvailableStock(BigDecimal availableStock) {
        this.availableStock = availableStock;
        // Note: This doesn't actually set anything persistent
    }
    
    /**
     * Gets discount amount (handles null)
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }
    
    /**
     * Gets tax percentage (handles null)
     */
    public BigDecimal getTaxPercentage() {
        return taxPercentage != null ? taxPercentage : BigDecimal.ZERO;
    }
    
    /**
     * Gets profit margin
     */
    @Transient
    public BigDecimal getProfitMargin() {
        if (costPrice == null || sellingPrice == null) {
            return BigDecimal.ZERO;
        }
        if (costPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal profit = sellingPrice.subtract(costPrice);
        return profit.divide(costPrice, 4, RoundingMode.HALF_UP)
                     .multiply(new BigDecimal("100"));
    }
    
    /**
     * Checks if stock is below minimum level
     */
    @Transient
    public boolean isLowStock() {
        if (currentStock == null || minimumStock == null) {
            return false;
        }
        return currentStock.compareTo(minimumStock) <= 0;
    }
    
    /**
     * Checks if out of stock
     */
    @Transient
    public boolean isOutOfStock() {
        return currentStock == null || currentStock.compareTo(BigDecimal.ZERO) <= 0;
    }
    
    /**
     * Checks if product is active
     */
    @Transient
    public boolean isActive() {
        return "ACTIVE".equals(status) && 
               (isActiveForSale != null && isActiveForSale);
    }
    
    /**
     * Gets full product name with package size
     */
    @Transient
    public String getFullProductName() {
        StringBuilder fullName = new StringBuilder(productName);
        if (packageSize != null && packageUnit != null) {
            fullName.append(" - ")
                    .append(packageSize)
                    .append(packageUnit);
        }
        return fullName.toString();
    }
    
    /**
     * Gets stock value (current stock * cost price)
     */
    @Transient
    public BigDecimal getStockValue() {
        if (currentStock == null || costPrice == null) {
            return BigDecimal.ZERO;
        }
        return currentStock.multiply(costPrice);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "ACTIVE";
        }
        if (isActiveForSale == null) {
            isActiveForSale = true;
        }
        if (isActiveForPurchase == null) {
            isActiveForPurchase = true;
        }
        if (isTaxable == null) {
            isTaxable = true;
        }
        if (isFeatured == null) {
            isFeatured = false;
        }
        if (batchTrackingEnabled == null) {
            batchTrackingEnabled = false;
        }
        if (serialTrackingEnabled == null) {
            serialTrackingEnabled = false;
        }
        if (currentStock == null) {
            currentStock = BigDecimal.ZERO;
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (productType == null) {
            productType = "FINISHED_GOOD";
        }
        
        // Initialize new fields
        if (discountPercentage == null) {
            discountPercentage = BigDecimal.ZERO;
        }
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        if (taxPercentage == null) {
            taxPercentage = BigDecimal.ZERO;
        }
        if (reservedStock == null) {
            reservedStock = BigDecimal.ZERO;
        }
        
        // Sync category ID
        if (category != null && categoryId == null) {
            categoryId = category.getId();
        }
        
        // Sync alias fields
        if (unitOfMeasure == null && baseUnit != null) {
            unitOfMeasure = baseUnit;
        }
        if (baseUnit == null && unitOfMeasure != null) {
            baseUnit = unitOfMeasure;
        }
        
        isActive = "ACTIVE".equals(status) && Boolean.TRUE.equals(isActiveForSale);
        canBeSold = isActiveForSale;
        canBePurchased = isActiveForPurchase;
        standardCost = costPrice;
        minimumPrice = wholesalePrice;
        reorderLevel = minimumStock;
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
