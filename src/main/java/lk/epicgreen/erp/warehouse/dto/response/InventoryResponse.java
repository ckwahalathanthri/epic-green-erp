package lk.epicgreen.erp.warehouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for inventory response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private Long id;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private Long productId;
    private String productCode;
    private String productName;
    private Long locationId;
    private String locationCode;
    private String batchNumber;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private BigDecimal quantityAvailable;
    private BigDecimal quantityReserved;
    private BigDecimal quantityOrdered;
    private BigDecimal unitCost;
    private BigDecimal totalValue;
    private LocalDate lastStockDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
