package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryAlertDTO {
    private Long id;
    private String alertType;
    private String alertSeverity;
    private Long productId;
    private String productCode;
    private Long warehouseId;
    private BigDecimal currentQuantity;
    private BigDecimal thresholdQuantity;
    private String alertMessage;
    private String alertStatus;
    private LocalDateTime triggeredAt;
}
