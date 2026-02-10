package lk.epicgreen.erp.product.dto.response;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDTO {
    private Long id;
    private String name;
    private String code;
    private String categoryName;
    private String productType;
    private Boolean isActive;
    private Double price;


}
