package lk.epicgreen.erp.production.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * BOM (Bill of Materials) DTO
 * Data transfer object for BillOfMaterials entity
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
public class BomDTO {
    
    private Long id;
    
    private String bomNumber;
    
    private String bomName;
    
    private String version;
    
    /**
     * Product information (finished good)
     */
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    /**
     * Output
     */
    private BigDecimal outputQuantity;
    
    private String outputUnit;
    
    /**
     * Type and description
     */
    private String bomType;
    
    private String description;
    
    private String productionInstructions;
    
    /**
     * Time and costs
     */
    private Integer processingTimeMinutes;
    
    private BigDecimal laborCost;
    
    private BigDecimal overheadCost;
    
    private BigDecimal totalMaterialCost;
    
    private BigDecimal totalCost;
    
    private BigDecimal costPerUnit;
    
    private String currency;
    
    /**
     * Validity
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate validFrom;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate validTo;
    
    /**
     * Status and approval
     */
    private String status;
    
    private Boolean isDefault;
    
    private String approvedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate approvalDate;
    
    private String notes;
    
    private Integer totalItems;
    
    /**
     * BOM items (ingredients)
     */
    private List<BomItemDTO> items;
    
    /**
     * Computed properties
     */
    private Boolean isActive;
    
    private Boolean canEdit;
    
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
     * BOM Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BomItemDTO {
        
        private Long id;
        
        /**
         * Product information (raw material)
         */
        private Long productId;
        
        private String productCode;
        
        private String productName;
        
        private String itemDescription;
        
        /**
         * Quantity
         */
        private BigDecimal quantity;
        
        private String unit;
        
        private BigDecimal wastagePercentage;
        
        private BigDecimal actualQuantity;
        
        /**
         * Cost
         */
        private BigDecimal unitCost;
        
        private BigDecimal totalCost;
        
        private String currency;
        
        /**
         * Additional info
         */
        private Integer sequenceNumber;
        
        private String itemType;
        
        private Boolean isOptional;
        
        private String preparationInstructions;
        
        private String notes;
    }
}
