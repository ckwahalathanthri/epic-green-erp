package lk.epicgreen.erp.warehouse.dto.response;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockIssueDTO {
    private Long id;
    private String issueNumber;
    private Long warehouseId;
    private LocalDate issueDate;
    private String issueType;
    private String referenceNumber;
    private String issuedTo;
    private String issueStatus;
    private String pickingMethod;
    private String priority;
    private String notes;
    private List<IssueItemDTO> items;
}
