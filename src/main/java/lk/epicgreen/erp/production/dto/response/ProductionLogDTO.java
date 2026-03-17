package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductionLogDTO {
    private Long id;
    private String logNumber;
    private Long productionOrderId;
    private Long workCenterId;
    private String activityType;
    private LocalDateTime activityDate;
    private Integer duration;
    private String operatorName;
    private String notes;
}