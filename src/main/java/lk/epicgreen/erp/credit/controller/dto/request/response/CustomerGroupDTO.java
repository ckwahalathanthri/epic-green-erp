package lk.epicgreen.erp.credit.controller.dto.request.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGroupDTO {
    private Long id;
    private String groupCode;
    private String groupName;
    private String description;
    private Double discountPercentage;
    private Double creditLimitDefault;
    private Integer paymentTermsDaysDefault;
    private Boolean isActive;
    private Integer memberCount;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
