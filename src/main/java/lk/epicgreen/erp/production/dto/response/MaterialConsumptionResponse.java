package lk.epicgreen.erp.production.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Material Consumption response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialConsumptionResponse {

    private Long id;
    private Long woId;
    private String woNumber;
    private Long woItemId;
    private Long rawMaterialId;
    private String rawMaterialCode;
    private String rawMaterialName;
    private LocalDate consumptionDate;
    private String batchNumber;
    private BigDecimal quantityConsumed;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private Long consumedBy;
    private String consumedByName;
    private String remarks;
    private LocalDateTime createdAt;
}
