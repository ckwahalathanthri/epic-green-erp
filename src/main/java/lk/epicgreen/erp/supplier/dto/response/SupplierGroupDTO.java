package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierGroupDTO {
    private Long id;
    private String groupCode;
    private String groupName;
    private String description;
    private Double discountPercentage;
    private Integer creditDaysDefault;
    private Integer paymentTermsDaysDefault;
    private Integer leadTimeDaysDefault;
    private BigDecimal minimumOrderValue;
    private Boolean isActive;
    private Integer memberCount;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
