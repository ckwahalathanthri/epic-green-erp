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
 * Invoice DTO
 * Data transfer object for Invoice entity
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
public class InvoiceDTO {
    
    private Long id;
    
    private String invoiceNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate invoiceDate;
    
    /**
     * References
     */
    private Long salesOrderId;
    
    private String salesOrderNumber;
    
    private Long dispatchNoteId;
    
    private String dispatchNumber;
    
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    /**
     * Invoice details
     */
    private String invoiceType;
    
    private String customerReference;
    
    /**
     * Billing address
     */
    private Long billingAddressId;
    
    private String billingAddressText;
    
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
    
    private BigDecimal paidAmount;
    
    private BigDecimal balanceAmount;
    
    /**
     * Payment
     */
    private String paymentTerms;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate dueDate;
    
    /**
     * Status
     */
    private String status;
    
    private String paymentStatus;
    
    /**
     * Posting
     */
    private Boolean isPosted;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate postedDate;
    
    /**
     * Terms and notes
     */
    private String termsAndConditions;
    
    private String notes;
    
    private String internalNotes;
    
    private Integer totalItems;
    
    /**
     * Invoice items
     */
    private List<InvoiceItemDTO> items;
    
    /**
     * Computed properties
     */
    private Boolean isOverdue;
    
    private Long daysOverdue;
    
    private Boolean isFullyPaid;
    
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
     * Invoice Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InvoiceItemDTO {
        
        private Long id;
        
        /**
         * References
         */
        private Long orderItemId;
        
        private Long dispatchItemId;
        
        private Long productId;
        
        private String productCode;
        
        private String productName;
        
        /**
         * Description
         */
        private String itemDescription;
        
        /**
         * Quantity
         */
        private BigDecimal quantity;
        
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
    }
}
