package lk.epicgreen.erp.production.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Work Order response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderResponse {

    private Long id;
    private String woNumber;
    private LocalDate woDate;
    private Long bomId;
    private String bomCode;
    private Long finishedProductId;
    private String finishedProductCode;
    private String finishedProductName;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private BigDecimal plannedQuantity;
    private BigDecimal actualQuantity;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private String batchNumber;
    private LocalDate manufacturingDate;
    private LocalDate expectedCompletionDate;
    private LocalDate actualCompletionDate;
    private String status;
    private String priority;
    private Long supervisorId;
    private String supervisorName;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal overheadCost;
    private BigDecimal totalCost;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private List<WorkOrderItemResponse> items;
}
