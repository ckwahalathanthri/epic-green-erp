package lk.epicgreen.erp.admin.dto.request;

import java.math.BigDecimal;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitOfMeasureRequest{
    private String UomCode;
    private String UomName;
    private String UomType;
    private String description;
    private Boolean baseUnit;
    private BigDecimal conversionFactor;

}