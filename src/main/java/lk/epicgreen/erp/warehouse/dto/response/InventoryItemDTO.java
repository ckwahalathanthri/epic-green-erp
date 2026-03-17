package lk.epicgreen.erp.warehouse.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    private Long id;
    private String code;
    private String name;
    private LocalDateTime createdAt;
    private Long productId;
    private Long warehouseId;
    private BigDecimal quantityOnHand = BigDecimal.ZERO;
    private BigDecimal quantityReserved = BigDecimal.ZERO;
    private BigDecimal quantityAvailable = BigDecimal.ZERO;

}
