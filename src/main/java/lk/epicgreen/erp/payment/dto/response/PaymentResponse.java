package lk.epicgreen.erp.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Payment response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private String paymentNumber;
    private LocalDate paymentDate;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String paymentMode;
    private BigDecimal totalAmount;
    private BigDecimal allocatedAmount;
    private BigDecimal unallocatedAmount;
    private String status;
    
    // Bank/Cheque specific fields
    private String bankName;
    private String bankBranch;
    private String chequeNumber;
    private LocalDate chequeDate;
    private LocalDate chequeClearanceDate;
    private String bankReferenceNumber;
    
    // Collection details
    private Long collectedBy;
    private String collectedByName;
    private LocalDateTime collectedAt;
    
    // Approval details
    private Long approvedBy;
    private String approvedByName;
    private LocalDateTime approvedAt;
    
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    
    // Payment allocations
    private List<PaymentAllocationResponse> allocations;
}
