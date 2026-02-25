package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierTypeDTO {
    private Long id;
    private String typeCode;
    private String typeName;
    private String description;
    private Boolean isActive;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
