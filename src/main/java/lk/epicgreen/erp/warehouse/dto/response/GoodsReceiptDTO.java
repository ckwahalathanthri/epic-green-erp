package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoodsReceiptDTO {
    private Long id;
    private String receiptNumber;
    private LocalDate receiptDate;
    private Long warehouseId;
    private String receiptType;
    private Long sourceId;
    private String receiptStatus;
    private BigDecimal totalValue;
}
