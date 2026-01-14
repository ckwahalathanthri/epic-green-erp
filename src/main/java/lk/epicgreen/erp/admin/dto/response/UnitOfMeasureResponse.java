package lk.epicgreen.erp.admin.dto.response;

import java.math.BigDecimal;

import lombok.*;

@Data
@Builder
public class UnitOfMeasureResponse{
    private Long id;
    private String uomCode;
    private String uomName;
    private String uomType;
    private String description;
    private Boolean baseUnit;
    private BigDecimal conversionFactor;
    private Long baseUnitId;
    private Boolean isActive;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}