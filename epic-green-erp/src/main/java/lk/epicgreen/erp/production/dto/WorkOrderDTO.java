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
 * WorkOrder DTO
 * Data transfer object for WorkOrder entity
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
public class WorkOrderDTO {
    
    private Long id;
    
    private String woNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate woDate;
    
    /**
     * BOM information
     */
    private Long bomId;
    
    private String bomNumber;
    
    private String bomName;
    
    /**
     * Warehouse information
     */
    private Long outputWarehouseId;
    
    private String outputWarehouseCode;
    
    private String outputWarehouseName;
    
    private Long materialWarehouseId;
    
    private String materialWarehouseCode;
    
    private String materialWarehouseName;
    
    /**
     * Quantities
     */
    private BigDecimal plannedQuantity;
    
    private BigDecimal producedQuantity;
    
    private String unit;
    
    private BigDecimal completionPercentage;
    
    /**
     * Schedule
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate plannedStartDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate plannedEndDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate actualStartDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate actualEndDate;
    
    /**
     * Assignment
     */
    private String priority;
    
    private String status;
    
    private String supervisor;
    
    private String shift;
    
    private String batchNumber;
    
    /**
     * Costs
     */
    private BigDecimal plannedMaterialCost;
    
    private BigDecimal actualMaterialCost;
    
    private BigDecimal plannedLaborCost;
    
    private BigDecimal actualLaborCost;
    
    private BigDecimal plannedOverheadCost;
    
    private BigDecimal actualOverheadCost;
    
    private BigDecimal totalPlannedCost;
    
    private BigDecimal totalActualCost;
    
    private String currency;
    
    /**
     * Quality
     */
    private Boolean qualityCheckRequired;
    
    private String qualityCheckStatus;
    
    private String qualityRemarks;
    
    /**
     * Instructions and notes
     */
    private String productionInstructions;
    
    private String notes;
    
    /**
     * Work order items
     */
    private List<WorkOrderItemDTO> items;
    
    /**
     * Statistics
     */
    private Integer totalMaterialConsumptions;
    
    private Integer totalProductionOutputs;
    
    private BigDecimal totalConsumedQuantity;
    
    private BigDecimal totalOutputQuantity;
    
    /**
     * Computed properties
     */
    private Boolean isCompleted;
    
    private Boolean canStart;
    
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
     * Work Order Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class WorkOrderItemDTO {
        
        private Long id;
        
        /**
         * Product information
         */
        private Long productId;
        
        private String productCode;
        
        private String productName;
        
        private String itemDescription;
        
        /**
         * Quantities
         */
        private BigDecimal plannedQuantity;
        
        private BigDecimal producedQuantity;
        
        private BigDecimal goodQuantity;
        
        private BigDecimal rejectedQuantity;
        
        private BigDecimal pendingQuantity;
        
        private String unit;
        
        /**
         * Cost
         */
        private BigDecimal unitCost;
        
        private BigDecimal totalCost;
        
        private String currency;
        
        private String notes;
        
        /**
         * Computed properties
         */
        private BigDecimal completionPercentage;
        
        private BigDecimal qualityPassPercentage;
        
        private Boolean isFullyProduced;
    }
}
