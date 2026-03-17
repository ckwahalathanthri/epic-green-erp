package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProductionPlanDTO {
    private Long id;
    private String planNumber;
    private String planName;
    private String planStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private String approvedBy;
    private List<ProductionPlanItemDTO> items;
}