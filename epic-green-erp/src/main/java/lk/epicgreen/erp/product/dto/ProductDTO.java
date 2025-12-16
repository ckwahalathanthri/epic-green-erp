package lk.epicgreen.erp.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Product DTO
 * Data transfer object for Product entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    
    private Long id;
    
    private String productCode;
    
    private String productName;
    
    private String description;
    
    /**
     * Category information
     */
    private Long categoryId;
    
    private String categoryCode;
    
    private String categoryName;
    
    private String categoryPath;
    
    /**
     * Identification codes
     */
    private String sku;
    
    private String barcode;
    
    private String hsnCode;
    
    /**
     * Classification
     */
    private String productType;
    
    private String brand;
    
    private String manufacturer;
    
    private String countryOfOrigin;
    
    /**
     * Packaging details
     */
    private String baseUnit;
    
    private BigDecimal packageSize;
    
    private String packageUnit;
    
    private String packageType;
    
    private Integer unitsPerCase;
    
    /**
     * Storage & shelf life
     */
    private Integer shelfLifeDays;
    
    private String storageConditions;
    
    /**
     * Pricing
     */
    private BigDecimal costPrice;
    
    private BigDecimal sellingPrice;
    
    private BigDecimal mrp;
    
    private BigDecimal wholesalePrice;
    
    private String currency;
    
    private BigDecimal taxRate;
    
    private Boolean isTaxable;
    
    /**
     * Stock information
     */
    private BigDecimal currentStock;
    
    private BigDecimal minimumStock;
    
    private BigDecimal maximumStock;
    
    private BigDecimal reorderQuantity;
    
    private String stockLocation;
    
    /**
     * Order quantities
     */
    private BigDecimal minimumOrderQuantity;
    
    private BigDecimal maximumOrderQuantity;
    
    /**
     * Quality specifications
     */
    private String qualitySpecs;
    
    private BigDecimal moistureContent;
    
    private BigDecimal purity;
    
    /**
     * Physical specifications
     */
    private BigDecimal weight;
    
    private String weightUnit;
    
    private String dimensions;
    
    /**
     * Media
     */
    private String imageUrl;
    
    private String images;
    
    /**
     * Status flags
     */
    private Boolean isFeatured;
    
    private Boolean isActiveForSale;
    
    private Boolean isActiveForPurchase;
    
    private String status;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate discontinuedDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate launchDate;
    
    /**
     * Tracking
     */
    private Boolean batchTrackingEnabled;
    
    private Boolean serialTrackingEnabled;
    
    /**
     * SEO
     */
    private String metaKeywords;
    
    private String metaDescription;
    
    /**
     * Notes
     */
    private String notes;
    
    /**
     * Computed properties
     */
    private String fullProductName;
    
    private BigDecimal profitMargin;
    
    private BigDecimal stockValue;
    
    private Boolean isLowStock;
    
    private Boolean isOutOfStock;
    
    private Boolean isActive;
    
    /**
     * Statistics
     */
    private BigDecimal totalSalesQuantity;
    
    private BigDecimal totalSalesAmount;
    
    private BigDecimal totalPurchaseQuantity;
    
    private Long orderCount;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
}
