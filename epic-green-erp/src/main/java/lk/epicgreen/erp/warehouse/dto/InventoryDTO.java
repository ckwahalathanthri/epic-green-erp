package lk.epicgreen.erp.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Inventory DTO
 * Data transfer object for Inventory entity
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
public class InventoryDTO {
    
    private Long id;
    
    /**
     * Product information
     */
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    private String productSku;
    
    /**
     * Warehouse information
     */
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    /**
     * Location information
     */
    private Long locationId;
    
    private String locationCode;
    
    private String locationName;
    
    private String locationPath;
    
    /**
     * Batch/Serial tracking
     */
    private String batchNumber;
    
    private String serialNumber;
    
    /**
     * Quantities
     */
    private BigDecimal availableQuantity;
    
    private BigDecimal allocatedQuantity;
    
    private BigDecimal inTransitQuantity;
    
    private BigDecimal damagedQuantity;
    
    private BigDecimal totalQuantity;
    
    private BigDecimal unallocatedQuantity;
    
    private String unit;
    
    /**
     * Dates
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate manufacturingDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate expiryDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate lastStockCountDate;
    
    /**
     * Costing
     */
    private BigDecimal costPerUnit;
    
    private BigDecimal stockValue;
    
    private String currency;
    
    /**
     * Quality
     */
    private String qualityStatus;
    
    /**
     * Notes
     */
    private String notes;
    
    /**
     * Computed properties
     */
    private Boolean isExpired;
    
    private Boolean isNearExpiry;
    
    private Long daysUntilExpiry;
    
    private Boolean isAvailable;
    
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
