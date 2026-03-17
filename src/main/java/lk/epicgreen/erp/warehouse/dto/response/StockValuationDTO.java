package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StockValuationDTO {
    private Long id;
    private LocalDate valuationDate;
    private Long warehouseId;
    private BigDecimal totalValue;
}