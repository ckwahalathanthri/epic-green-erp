package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ReorderPointDTO {
    private Long id;
    private Long productId;
    private String productCode;
    private Long warehouseId;
    private BigDecimal minStockLevel;
    private BigDecimal maxStockLevel;
    private BigDecimal reorderPoint;
    private BigDecimal reorderQuantity;
    private String status;
    private Boolean autoReorderEnabled;
}
