package lk.epicgreen.erp.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * StockAdjustment DTO
 * Data transfer object for StockAdjustment entity
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
public class StockAdjustmentDTO {
    
    private Long id;
    
    private String adjustmentNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate adjustmentDate;
    
    private String adjustmentType;
    
    /**
     * Warehouse information
     */
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    /**
     * Details
     */
    private String reason;
    
    private String description;
    
    private String status;
    
    /**
     * Approval workflow
     */
    private String preparedBy;
    
    private String approvedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate approvalDate;
    
    private String completedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate completionDate;
    
    /**
     * Summary
     */
    private Integer totalItems;
    
    private String notes;
    
    /**
     * Adjustment items
     */
    private List<StockAdjustmentItemDTO> items;
    
    /**
     * Computed properties
     */
    private Boolean isPendingApproval;
    
    private Boolean isApproved;
    
    private Boolean isCompleted;
    
    private Boolean canEdit;
    
    private Boolean canApprove;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
    
    /**
     * Stock Adjustment Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StockAdjustmentItemDTO {
        
        private Long id;
        
        /**
         * Product information
         */
        private Long productId;
        
        private String productCode;
        
        private String productName;
        
        /**
         * Location information
         */
        private Long locationId;
        
        private String locationCode;
        
        private String locationName;
        
        /**
         * Batch/Serial
         */
        private String batchNumber;
        
        private String serialNumber;
        
        /**
         * Quantities
         */
        private BigDecimal systemQuantity;
        
        private BigDecimal physicalQuantity;
        
        private BigDecimal adjustmentQuantity;
        
        private String unit;
        
        /**
         * Costing
         */
        private BigDecimal costPerUnit;
        
        private BigDecimal totalValue;
        
        private String currency;
        
        /**
         * Details
         */
        private String reason;
        
        private String notes;
        
        /**
         * Computed properties
         */
        private BigDecimal variancePercentage;
        
        private Boolean hasDiscrepancy;
        
        private Boolean isSurplus;
        
        private Boolean isShortage;
    }
}
