package lk.epicgreen.erp.product.dto.response;

import lombok.*;

@Data

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FinishedProductResponse extends ProductDtoResponse {
    private Integer batchSize;
    private Integer productionTimeMinutes;
    private String packagingType;
    private Integer expiryDays;
}
