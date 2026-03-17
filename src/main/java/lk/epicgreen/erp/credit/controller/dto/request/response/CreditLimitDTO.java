package lk.epicgreen.erp.credit.controller.dto.request.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditLimitDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private BigDecimal creditLimit;
    private BigDecimal creditUsed;
    private BigDecimal creditAvailable;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private Boolean isActive;
    private String approvalStatus;
    private String approvedBy;
    private LocalDateTime approvalDate;
    private String notes;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
