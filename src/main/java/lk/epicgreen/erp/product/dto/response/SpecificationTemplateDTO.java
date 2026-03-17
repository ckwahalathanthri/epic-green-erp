package lk.epicgreen.erp.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationTemplateDTO {
    private Long id;
    private String templateName;
    private String specName;
    private String specUnit;
    private String dataType;
    private Boolean isRequired;
    private String defaultValue;
    private Integer displayOrder;
    private Boolean isActive;
}
