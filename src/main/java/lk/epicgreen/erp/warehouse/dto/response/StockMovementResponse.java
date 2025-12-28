package lk.epicgreen.erp.warehouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for stock movement response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementResponse {

    private Long id;
    private LocalDate movementDate;
    private String movementType;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private Long productId;
    private String productCode;
    private String productName;
    private Long fromLocationId;
    private String fromLocationCode;
    private Long toLocationId;
    private String toLocationCode;
    private String batchNumber;
    private BigDecimal quantity;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private String referenceType;
    private Long referenceId;
    private String referenceNumber;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
}
