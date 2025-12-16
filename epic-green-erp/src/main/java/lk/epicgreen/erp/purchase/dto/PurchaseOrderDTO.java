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
 * PurchaseOrder DTO
 * Data transfer object for PurchaseOrder entity
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
public class PurchaseOrderDTO {
    
    private Long id;
    
    private String poNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate poDate;
    
    /**
     * Supplier information
     */
    private Long supplierId;
    
    private String supplierCode;
    
    private String supplierName;
    
    private String supplierEmail;
    
    private String supplierPhone;
    
    /**
     * Delivery details
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate expectedDeliveryDate;
    
    private String deliveryAddress;
    
    private String deliveryContactPerson;
    
    private String deliveryContactNumber;
    
    /**
     * Type and currency
     */
    private String poType;
    
    private String currency;
    
    private BigDecimal exchangeRate;
    
    /**
     * Financial details
     */
    private BigDecimal subtotal;
    
    private BigDecimal discountAmount;
    
    private BigDecimal discountPercentage;
    
    private BigDecimal taxAmount;
    
    private BigDecimal taxPercentage;
    
    private BigDecimal shippingCost;
    
    private BigDecimal otherCharges;
    
    private BigDecimal totalAmount;
    
    /**
     * Payment terms
     */
    private String paymentTerms;
    
    private Integer paymentDays;
    
    /**
     * Status and workflow
     */
    private String status;
    
    private String preparedBy;
    
    private String approvedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate approvalDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate sentDate;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate acknowledgedDate;
    
    /**
     * Additional info
     */
    private String termsAndConditions;
    
    private String notes;
    
    private String internalNotes;
    
    private Integer totalItems;
    
    /**
     * PO items
     */
    private List<PurchaseOrderItemDTO> items;
    
    /**
     * Statistics
     */
    private Integer totalGrns;
    
    private BigDecimal totalReceivedQuantity;
    
    private BigDecimal totalPendingQuantity;
    
    private BigDecimal receivedPercentage;
    
    /**
     * Computed properties
     */
    private Boolean isPendingApproval;
    
    private Boolean isApproved;
    
    private Boolean canEdit;
    
    private Boolean canApprove;
    
    private Boolean canReceiveGoods;
    
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
     * Purchase Order Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PurchaseOrderItemDTO {
        
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
        private BigDecimal orderedQuantity;
        
        private BigDecimal receivedQuantity;
        
        private BigDecimal pendingQuantity;
        
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
         * Delivery
         */
        @JsonFormat(pattern = AppConstants.DATE_FORMAT)
        private LocalDate expectedDeliveryDate;
        
        private String notes;
        
        /**
         * Computed properties
         */
        private Boolean isFullyReceived;
        
        private Boolean isPartiallyReceived;
        
        private BigDecimal receivedPercentage;
    }
}
