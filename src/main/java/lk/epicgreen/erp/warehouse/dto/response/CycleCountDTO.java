package lk.epicgreen.erp.warehouse.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CycleCountDTO {
    private Long id;
    private String countNumber;
    private Long warehouseId;
    private String warehouseName;
    private LocalDate countDate;
    private String countType;
    private String countStatus;
    private String countMethod;
    private String counterName;
    private String approvedBy;
    private Integer totalItemsCounted;
    private Integer itemsWithVariance;
    private BigDecimal accuracyPercentage;
    private List<CycleCountItemDTO> items;
}