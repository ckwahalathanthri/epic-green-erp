package lk.epicgreen.erp.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationDTO {
    private Long id;
    private Long productId;
    private String specName;
    private String specValue;
    private String specUnit;
    private Integer displayOrder;
    private Boolean isRequired;
}
