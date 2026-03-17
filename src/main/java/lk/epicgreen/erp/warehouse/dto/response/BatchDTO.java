package lk.epicgreen.erp.warehouse.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BatchDTO {
    private Long id;
    private String batchNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private Long warehouseId;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Integer initialQuantity;
    private Integer currentQuantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private String unitOfMeasure;
    private BigDecimal unitCost;
    private BigDecimal totalValue;
    private String batchStatus;
    private String grnNumber;
}