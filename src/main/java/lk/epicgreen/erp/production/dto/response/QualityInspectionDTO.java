package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class QualityInspectionDTO {
    private Long id;
    private String inspectionNumber;
    private Long productionOrderId;
    private String batchNumber;
    private Long productId;
    private String productName;
    private String inspectionType;
    private LocalDate inspectionDate;
    private String inspectorName;
    private String inspectionStatus;
    private String overallResult;
    private String remarks;
    private List<QualityInspectionTestDTO> tests;
}