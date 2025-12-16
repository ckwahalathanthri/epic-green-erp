package lk.epicgreen.erp.product.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.AppConstants;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Create Product Request DTO
 * Request object for creating a new product
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {
    
    /**
     * Product code (unique)
     */
    @NotBlank(message = "Product code is required")
    @Size(min = 2, max = 20, message = ValidationMessages.CODE_SIZE)
    @Pattern(regexp = AppConstants.CODE_PATTERN, message = ValidationMessages.CODE_INVALID)
    private String productCode;
    
    /**
     * Product name
     */
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = ValidationMessages.NAME_SIZE)
    private String productName;
    
    /**
     * Product description
     */
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
    
    /**
     * Category ID
     */
    @NotNull(message = "Category is required")
    private Long categoryId;
    
    /**
     * SKU (Stock Keeping Unit)
     */
    @Size(max = 50, message = "SKU must not exceed 50 characters")
    private String sku;
    
    /**
     * Barcode
     */
    @Size(max = 50, message = "Barcode must not exceed 50 characters")
    private String barcode;
    
    /**
     * HSN code
     */
    @Size(max = 20, message = "HSN code must not exceed 20 characters")
    private String hsnCode;
    
    /**
     * Product type
     */
    @NotBlank(message = "Product type is required")
    private String productType;
    
    /**
     * Brand name
     */
    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;
    
    /**
     * Manufacturer
     */
    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;
    
    /**
     * Country of origin
     */
    @Size(max = 100, message = "Country of origin must not exceed 100 characters")
    private String countryOfOrigin;
    
    /**
     * Base unit of measurement
     */
    @NotBlank(message = "Base unit is required")
    @Size(max = 10, message = "Base unit must not exceed 10 characters")
    private String baseUnit;
    
    /**
     * Package size
     */
    @DecimalMin(value = "0.001", message = "Package size must be greater than 0")
    @Digits(integer = 7, fraction = 3, message = "Invalid package size format")
    private BigDecimal packageSize;
    
    /**
     * Package unit
     */
    @Size(max = 10, message = "Package unit must not exceed 10 characters")
    private String packageUnit;
    
    /**
     * Package type
     */
    @Size(max = 50, message = "Package type must not exceed 50 characters")
    private String packageType;
    
    /**
     * Units per case
     */
    @Min(value = 1, message = "Units per case must be at least 1")
    private Integer unitsPerCase;
    
    /**
     * Shelf life in days
     */
    @Min(value = 0, message = "Shelf life must be non-negative")
    private Integer shelfLifeDays;
    
    /**
     * Storage conditions
     */
    @Size(max = 500, message = "Storage conditions must not exceed 500 characters")
    private String storageConditions;
    
    /**
     * Cost price
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Cost price must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid cost price format")
    private BigDecimal costPrice;
    
    /**
     * Selling price
     */
    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.01", message = "Selling price must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Invalid selling price format")
    private BigDecimal sellingPrice;
    
    /**
     * MRP (Maximum Retail Price)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "MRP must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid MRP format")
    private BigDecimal mrp;
    
    /**
     * Wholesale price
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Wholesale price must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid wholesale price format")
    private BigDecimal wholesalePrice;
    
    /**
     * Currency
     */
    @Size(max = 10, message = "Currency must not exceed 10 characters")
    private String currency;
    
    /**
     * Tax rate
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax rate must be non-negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Tax rate cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Invalid tax rate format")
    private BigDecimal taxRate;
    
    /**
     * Is taxable
     */
    private Boolean isTaxable;
    
    /**
     * Current stock
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Current stock must be non-negative")
    @Digits(integer = 12, fraction = 3, message = "Invalid stock format")
    private BigDecimal currentStock;
    
    /**
     * Minimum stock
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum stock must be non-negative")
    @Digits(integer = 12, fraction = 3, message = "Invalid stock format")
    private BigDecimal minimumStock;
    
    /**
     * Maximum stock
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum stock must be non-negative")
    @Digits(integer = 12, fraction = 3, message = "Invalid stock format")
    private BigDecimal maximumStock;
    
    /**
     * Reorder quantity
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Reorder quantity must be non-negative")
    @Digits(integer = 12, fraction = 3, message = "Invalid quantity format")
    private BigDecimal reorderQuantity;
    
    /**
     * Stock location
     */
    @Size(max = 100, message = "Stock location must not exceed 100 characters")
    private String stockLocation;
    
    /**
     * Minimum order quantity
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum order quantity must be non-negative")
    @Digits(integer = 12, fraction = 3, message = "Invalid quantity format")
    private BigDecimal minimumOrderQuantity;
    
    /**
     * Maximum order quantity
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum order quantity must be non-negative")
    @Digits(integer = 12, fraction = 3, message = "Invalid quantity format")
    private BigDecimal maximumOrderQuantity;
    
    /**
     * Quality specifications
     */
    @Size(max = 5000, message = "Quality specs must not exceed 5000 characters")
    private String qualitySpecs;
    
    /**
     * Moisture content percentage
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Moisture content must be non-negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Moisture content cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Invalid moisture content format")
    private BigDecimal moistureContent;
    
    /**
     * Purity percentage
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Purity must be non-negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Purity cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Invalid purity format")
    private BigDecimal purity;
    
    /**
     * Product weight
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Weight must be non-negative")
    @Digits(integer = 7, fraction = 3, message = "Invalid weight format")
    private BigDecimal weight;
    
    /**
     * Weight unit
     */
    @Size(max = 10, message = "Weight unit must not exceed 10 characters")
    private String weightUnit;
    
    /**
     * Dimensions (L x W x H)
     */
    @Size(max = 50, message = "Dimensions must not exceed 50 characters")
    private String dimensions;
    
    /**
     * Image URL
     */
    @Size(max = 255, message = ValidationMessages.URL_MAX_LENGTH)
    private String imageUrl;
    
    /**
     * Multiple images (JSON or comma-separated)
     */
    @Size(max = 2000, message = "Images data must not exceed 2000 characters")
    private String images;
    
    /**
     * Is featured product
     */
    private Boolean isFeatured;
    
    /**
     * Is active for sale
     */
    private Boolean isActiveForSale;
    
    /**
     * Is active for purchase
     */
    private Boolean isActiveForPurchase;
    
    /**
     * Status
     */
    private String status;
    
    /**
     * Launch date
     */
    private LocalDate launchDate;
    
    /**
     * Batch tracking enabled
     */
    private Boolean batchTrackingEnabled;
    
    /**
     * Serial number tracking enabled
     */
    private Boolean serialTrackingEnabled;
    
    /**
     * Meta keywords
     */
    @Size(max = 500, message = "Meta keywords must not exceed 500 characters")
    private String metaKeywords;
    
    /**
     * Meta description
     */
    @Size(max = 500, message = "Meta description must not exceed 500 characters")
    private String metaDescription;
    
    /**
     * Notes
     */
    @Size(max = 5000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    /**
     * Custom validation: MRP should be >= selling price
     */
    @AssertTrue(message = "MRP must be greater than or equal to selling price")
    public boolean isMrpValid() {
        if (mrp == null || sellingPrice == null) {
            return true;
        }
        return mrp.compareTo(sellingPrice) >= 0;
    }
    
    /**
     * Custom validation: Maximum stock should be >= minimum stock
     */
    @AssertTrue(message = "Maximum stock must be greater than or equal to minimum stock")
    public boolean isMaxStockValid() {
        if (maximumStock == null || minimumStock == null) {
            return true;
        }
        return maximumStock.compareTo(minimumStock) >= 0;
    }
    
    /**
     * Custom validation: Maximum order quantity should be >= minimum order quantity
     */
    @AssertTrue(message = "Maximum order quantity must be greater than or equal to minimum order quantity")
    public boolean isMaxOrderQuantityValid() {
        if (maximumOrderQuantity == null || minimumOrderQuantity == null) {
            return true;
        }
        return maximumOrderQuantity.compareTo(minimumOrderQuantity) >= 0;
    }
}
