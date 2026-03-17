package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PhysicalInventoryDTO {
    private Long id;
    private String countNumber;
    private LocalDate countDate;
    private Long warehouseId;
    private String countType;
    private String countStatus;
    private Integer totalItemsCounted;
    private Integer totalDiscrepancies;
    private BigDecimal totalVarianceValue;
    private String countedBy;
    private String notes;
    private List<PhysicalInventoryLineDTO> lines;
}
