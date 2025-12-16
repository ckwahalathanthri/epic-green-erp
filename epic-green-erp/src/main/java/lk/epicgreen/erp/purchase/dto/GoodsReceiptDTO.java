package lk.epicgreen.erp.purchase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * GoodsReceipt DTO
 * Data transfer object for GoodsReceiptNote entity
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
public class GoodsReceiptDTO {
    
    private Long id;
    
    private String grnNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate grnDate;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime grnTimestamp;
    
    /**
     * Purchase order information
     */
    private Long purchaseOrderId;
    
    private String poNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate poDate;
    
    /**
     * Supplier information
     */
    private Long supplierId;
    
    private String supplierCode;
    
    private String supplierName;
    
    /**
     * Warehouse information
     */
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    /**
     * Supplier document details
     */
    private String supplierInvoiceNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate supplierInvoiceDate;
    
    private String deliveryNoteNumber;
    
    /**
     * Delivery details
     */
    private String vehicleNumber;
    
    private String driverName;
    
    private String driverContact;
    
    /**
     * Personnel
     */
    private String receivedBy;
    
    private String inspectedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate inspectionDate;
    
    /**
     * Inspection
     */
    private String inspectionStatus;
    
    private String inspectionRemarks;
    
    /**
     * Quantities
     */
    private Integer totalItems;
    
    private BigDecimal totalReceivedQuantity;
    
    private BigDecimal totalAcceptedQuantity;
    
    private BigDecimal totalRejectedQuantity;
    
    /**
     * Status
     */
    private String status;
    
    private Boolean isPosted;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate postedDate;
    
    private String postedBy;
    
    private String notes;
    
    /**
     * GRN items
     */
    private List<GrnItemDTO> items;
    
    /**
     * Computed properties
     */
    private Boolean isPendingInspection;
    
    private Boolean isInspectionPassed;
    
    private Boolean isInspectionFailed;
    
    private Boolean canPost;
    
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
     * GRN Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GrnItemDTO {
        
        private Long id;
        
        /**
         * References
         */
        private Long poItemId;
        
        private Long productId;
        
        private String productCode;
        
        private String productName;
        
        /**
         * Location
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
        private BigDecimal orderedQuantity;
        
        private BigDecimal receivedQuantity;
        
        private BigDecimal acceptedQuantity;
        
        private BigDecimal rejectedQuantity;
        
        private BigDecimal damagedQuantity;
        
        private String unit;
        
        /**
         * Pricing
         */
        private BigDecimal unitPrice;
        
        private BigDecimal totalValue;
        
        private BigDecimal acceptedValue;
        
        /**
         * Quality details
         */
        @JsonFormat(pattern = AppConstants.DATE_FORMAT)
        private LocalDate manufacturingDate;
        
        @JsonFormat(pattern = AppConstants.DATE_FORMAT)
        private LocalDate expiryDate;
        
        private String qualityStatus;
        
        private String qualityRemarks;
        
        private BigDecimal moistureContent;
        
        private BigDecimal purity;
        
        private BigDecimal sampleWeight;
        
        private String rejectionReason;
        
        private String notes;
        
        /**
         * Computed properties
         */
        private BigDecimal varianceQuantity;
        
        private BigDecimal variancePercentage;
        
        private Boolean isFullyReceived;
        
        private Boolean hasRejection;
        
        private Boolean isQualityAcceptable;
    }
}
