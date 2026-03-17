package lk.epicgreen.erp.warehouse.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StockAdjustmentDTO {
    private Long id;
    private String adjustmentNumber;
    private Long warehouseId;
    private String warehouseName;
    private LocalDate adjustmentDate;
    private String adjustmentType;
    private String adjustmentReason;
    private String adjustmentStatus;
    private String adjustedBy;
    private String approvedBy;
    private List<AdjustmentItemDTO> items;
}