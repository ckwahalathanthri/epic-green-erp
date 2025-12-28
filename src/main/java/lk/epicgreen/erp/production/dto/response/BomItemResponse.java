package lk.epicgreen.erp.production.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for BOM item response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomItemResponse {

    private Long id;
    private Long bomId;
    private Long rawMaterialId;
    private String rawMaterialCode;
    private String rawMaterialName;
    private BigDecimal quantityRequired;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private BigDecimal wastagePercentage;
    private BigDecimal standardCost;
    private BigDecimal totalCost;
    private Integer sequenceNumber;
    private Boolean isCritical;
    private String remarks;
}
