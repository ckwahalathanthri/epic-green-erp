package lk.epicgreen.erp.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Payment Request DTO
 * DTO for payment operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotBlank(message = "Payment number is required")
    private String paymentNumber;
    
    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;
    
    @NotBlank(message = "Payment type is required")
    private String paymentType; // CUSTOMER_PAYMENT, SUPPLIER_PAYMENT
    
    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, MOBILE_PAYMENT
    
    @NotNull(message = "Amount is required")
    private Double amount;
    
    private Long customerId;
    
    private String customerName;
    
    private Long supplierId;
    
    private String supplierName;
    
    private String referenceType; // INVOICE, PURCHASE_ORDER, SALES_ORDER
    
    private Long referenceId;
    
    private String referenceNumber;
    
    // Cheque Details
    private String chequeNumber;
    
    private LocalDate chequeDate;
    
    private String bankName;
    
    private String bankBranch;
    
    private String accountNumber;
    
    // Bank Transfer Details
    private String transactionReference;
    
    private String fromBank;
    
    private String toBank;
    
    private String fromAccountNumber;
    
    private String toAccountNumber;
    
    // Credit Card Details
    private String cardNumber;
    
    private String cardHolderName;
    
    private String cardType;
    
    private String authorizationCode;
    
    // Mobile Payment Details
    private String mobileNumber;
    
    private String mobileProvider;
    
    private String mobileTransactionId;
    
    // General Fields
    @NotBlank(message = "Description is required")
    private String description;
    
    private String notes;
    
    private String receiptNumber;
    
    private LocalDate receiptDate;
    
    private Double discountAmount;
    
    private Double discountPercentage;
    
    private String currency;
    
    private Double exchangeRate;
    
    private Long createdByUserId;
    
    private Long approvedByUserId;
    
    private String approvalNotes;
    
    /**
     * Paid amount (alias for amount)
     */
    private Double paidAmount;
    
    /**
     * Bank account ID (for linking to bank account entity)
     */
    private Long bankAccountId;
    
    /**
     * Cheque ID (for linking to cheque entity)
     */
    private Long chequeId;
}
