package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierBankAccountDTO {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private String bankName;
    private String branchName;
    private String accountHolderName;
    private String accountNumber;
    private String accountType;
    private String swiftCode;
    private String iban;
    private String currency;
    private Boolean isPrimary;
    private Boolean isActive;
    private String notes;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
