package lk.epicgreen.erp.payment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Create Payment Request DTO
 * Request object for creating payments
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {
    
    /**
     * Payment date
     */
    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;
    
    /**
     * Customer ID
     */
    @NotNull(message = "Customer is required")
    private Long customerId;
    
    /**
     * Payment method
     */
    @NotBlank(message = "Payment method is required")
    @Size(max = 30, message = "Payment method must not exceed 30 characters")
    private String paymentMethod;
    
    /**
     * Payment amount
     */
    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Payment amount must be positive")
    @Digits(integer = 13, fraction = 2, message = "Invalid payment amount format")
    private BigDecimal paymentAmount;
    
    /**
     * Currency
     */
    @Size(max = 10, message = "Currency must not exceed 10 characters")
    private String currency;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Exchange rate must be positive")
    @Digits(integer = 9, fraction = 6, message = "Invalid exchange rate format")
    private BigDecimal exchangeRate;
    
    /**
     * Reference details
     */
    @Size(max = 100, message = "Reference number must not exceed 100 characters")
    private String referenceNumber;
    
    /**
     * Bank details (for bank transfers, cheques)
     */
    @Size(max = 200, message = "Bank name must not exceed 200 characters")
    private String bankName;
    
    @Size(max = 50, message = "Bank account must not exceed 50 characters")
    private String bankAccount;
    
    @Size(max = 100, message = "Bank branch must not exceed 100 characters")
    private String bankBranch;
    
    /**
     * Cheque ID (if payment method is CHEQUE)
     */
    private Long chequeId;
    
    /**
     * Receipt details
     */
    @Size(max = 50, message = "Received by must not exceed 50 characters")
    private String receivedBy;
    
    @Size(max = 50, message = "Receipt number must not exceed 50 characters")
    private String receiptNumber;
    
    /**
     * Description and notes
     */
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
    
    @Size(max = 5000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    /**
     * Payment allocations (optional, can be done later)
     */
    @Valid
    private List<CreatePaymentAllocationRequest> allocations;
    
    /**
     * Create Payment Allocation Request
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatePaymentAllocationRequest {
        
        /**
         * Invoice ID
         */
        @NotNull(message = "Invoice is required")
        private Long invoiceId;
        
        /**
         * Allocated amount
         */
        @NotNull(message = "Allocated amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Allocated amount must be positive")
        @Digits(integer = 13, fraction = 2, message = "Invalid allocated amount format")
        private BigDecimal allocatedAmount;
        
        /**
         * Notes
         */
        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;
    }
}
