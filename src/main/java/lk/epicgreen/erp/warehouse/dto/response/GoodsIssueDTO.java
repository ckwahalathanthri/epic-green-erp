package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoodsIssueDTO {
    private Long id;
    private String issueNumber;
    private LocalDate issueDate;
    private Long warehouseId;
    private String issueType;
    private Long destinationId;
    private String issueStatus;
    private BigDecimal totalValue;
}
