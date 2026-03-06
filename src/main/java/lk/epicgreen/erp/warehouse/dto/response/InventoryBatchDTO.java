package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InventoryBatchDTO {
    private Long id;
    private Long productId;
    private Long warehouseId;
    private String batchNumber;
    private LocalDate expiryDate;
    private BigDecimal quantity;
    private String batchStatus;
}