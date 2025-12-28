package lk.epicgreen.erp.production.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Work Order Item response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderItemResponse {

    private Long id;
    private Long woId;
    private Long rawMaterialId;
    private String rawMaterialCode;
    private String rawMaterialName;
    private BigDecimal plannedQuantity;
    private BigDecimal consumedQuantity;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private String status;
    private Long issuedFromWarehouseId;
    private String issuedFromWarehouseCode;
    private LocalDateTime issuedAt;
    private Long issuedBy;
    private String issuedByName;
}
