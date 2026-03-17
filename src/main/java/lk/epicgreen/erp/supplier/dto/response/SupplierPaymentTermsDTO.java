package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPaymentTermsDTO {
    private Long id;
    private String termsCode;
    private String termsName;
    private Integer days;
    private String description;
    private Double discountPercentage;
    private Integer discountDays;
    private Boolean isActive;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
