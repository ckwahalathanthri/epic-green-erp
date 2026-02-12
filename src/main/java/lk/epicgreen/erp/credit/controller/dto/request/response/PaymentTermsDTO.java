package lk.epicgreen.erp.credit.controller.dto.request.response;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTermsDTO {
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
