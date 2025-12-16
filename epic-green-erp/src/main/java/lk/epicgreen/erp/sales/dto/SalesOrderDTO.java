package lk.epicgreen.erp.sales.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Sales Order DTO
 * Data transfer object for SalesOrder entity
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
public class SalesOrderDTO {
    
    private Long id;
    
    private String orderNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate orderDate;
    
    /**
     * Customer information
     */
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    /**
     * Warehouse information
     */
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    /**
     * Order details
     */
    private String orderType;
    
    private String customerReference;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate expectedDeliveryDate;
    
    private Long deliveryAddressId;
    
    private String deliveryInstructions;
    
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
    
    private BigDecimal shippingCharges;
    
    private BigDecimal otherCharges;
    
    private BigDecimal totalAmount;
    
    /**
     * Status
     */
    private String status;
    
    private String paymentTerms;
    
    private String paymentMethod;
    
    /**
     * Sales information
     */
    private String salesRepresentative;
    
    private String priority;
    
    /**
     * Approval
     */
    private String approvedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate approvalDate;
    
    /**
     * Notes
     */
    private String notes;
    
    private String internalNotes;
    
    private Integer totalItems;
    
    /**
     * Order items
     */
    private List<SalesOrderItemDTO> items;
    
    /**
     * Statistics
     */
    private BigDecimal totalDispatchedQuantity;
    
    private BigDecimal totalPendingQuantity;
    
    private Integer totalDispatches;
    
    private Integer totalInvoices;
    
    /**
     * Computed properties
     */
    private Boolean isFullyDispatched;
    
    private Boolean isPendingApproval;
    
    private Boolean isApproved;
    
    private Boolean canEdit;
    
    private Boolean canDispatch;
    
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
     * Sales Order Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SalesOrderItemDTO {
        
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
        
        private BigDecimal dispatchedQuantity;
        
        private BigDecimal invoicedQuantity;
        
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
        
        private String notes;
        
        /**
         * Computed properties
         */
        private Boolean isFullyDispatched;
        
        private Boolean isPartiallyDispatched;
        
        private BigDecimal dispatchPercentage;
    }
}
