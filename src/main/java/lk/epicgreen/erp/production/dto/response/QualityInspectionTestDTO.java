package lk.epicgreen.erp.production.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class QualityInspectionTestDTO {
    private Long id;
    private String testParameter;
    private BigDecimal expectedValue;
    private BigDecimal actualValue;
    private BigDecimal tolerance;
    private String unit;
    private String testResult;
    private String testMethod;
}