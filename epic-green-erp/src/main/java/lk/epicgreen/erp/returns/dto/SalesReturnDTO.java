package lk.epicgreen.erp.returns.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Sales Return DTO
 * Data transfer object for SalesReturn entity
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
public class SalesReturnDTO {
    
    private Long id;
    
    private String returnNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate returnDate;
    
    /**
     * Customer information
     */
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    /**
     * Invoice information
     */
    private Long invoiceId;
    
    private String invoiceNumber;
    
    /**
     * Warehouse information
     */
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    /**
     * Return details
     */
    private String returnType;
    
    private String returnReason;
    
    private String returnReasonDescription;
    
    private String customerReference;
    
    /**
     * Financial
     */
    private String currency;
    
    private BigDecimal exchangeRate;
    
    private BigDecimal subtotal;
    
    private BigDecimal discountAmount;
    
    private BigDecimal discountPercentage;
    
    private BigDecimal taxAmount;
    
    private BigDecimal taxPercentage;
    
    private BigDecimal totalAmount;
    
    /**
     * Status
     */
    private String status;
    
    /**
     * Quality inspection
     */
    private Boolean qualityInspectionRequired;
    
    private String qualityInspectionStatus;
    
    private String qualityInspector;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate qualityInspectionDate;
    
    private String qualityRemarks;
    
    /**
     * Received details
     */
    private String receivedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate receivedDate;
    
    /**
     * Approval
     */
    private String approvedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate approvalDate;
    
    /**
     * Posting
     */
    private Boolean isPosted;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate postedDate;
    
    private String postedBy;
    
    /**
     * Credit note
     */
    private Boolean creditNoteGenerated;
    
    /**
     * Notes
     */
    private String notes;
    
    private String internalNotes;
    
    private Integer totalItems;
    
    /**
     * Return items
     */
    private List<SalesReturnItemDTO> items;
    
    /**
     * Statistics
     */
    private BigDecimal totalAcceptedQuantity;
    
    private BigDecimal totalRejectedQuantity;
    
    private Integer totalCreditNotes;
    
    /**
     * Computed properties
     */
    private Boolean isQualityInspectionComplete;
    
    private Boolean canPost;
    
    private Boolean canEdit;
    
    private Boolean requiresApproval;
    
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
     * Sales Return Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SalesReturnItemDTO {
        
        private Long id;
        
        /**
         * References
         */
        private Long invoiceItemId;
        
        private Long productId;
        
        private String productCode;
        
        private String productName;
        
        private Long locationId;
        
        private String locationCode;
        
        private String locationName;
        
        /**
         * Description
         */
        private String itemDescription;
        
        /**
         * Batch/Serial
         */
        private String batchNumber;
        
        private String serialNumber;
        
        /**
         * Quantities
         */
        private BigDecimal returnQuantity;
        
        private BigDecimal acceptedQuantity;
        
        private BigDecimal rejectedQuantity;
        
        private String unit;
        
        /**
         * Pricing
         */
        private BigDecimal unitPrice;
        
        private BigDecimal discountPercentage;
        
        private BigDecimal discountAmount;
        
        private BigDecimal taxPercentage;
        
        private BigDecimal taxAmount;
        
        private BigDecimal lineTotal;
        
        private BigDecimal netAmount;
        
        /**
         * Return details
         */
        private String returnReason;
        
        private String returnAction;
        
        /**
         * Quality
         */
        private String qualityStatus;
        
        private String qualityRemarks;
        
        private String notes;
        
        /**
         * Computed properties
         */
        private Boolean passedQualityInspection;
        
        private Boolean failedQualityInspection;
        
        private BigDecimal acceptancePercentage;
    }
}
