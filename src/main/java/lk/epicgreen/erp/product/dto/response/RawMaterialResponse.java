package lk.epicgreen.erp.product.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RawMaterialResponse extends ProductDtoResponse {
    private String originCountry;
    private String supplierCode;
    private String qualityGrade;
    private Integer shelfLifeDays;
}
