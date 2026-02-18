package lk.epicgreen.erp.warehouse.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReservationDTO {
    private Long id;
    private String reservationNumber;
    private Long warehouseId;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal reservedQuantity;
    private BigDecimal fulfilledQuantity;
    private BigDecimal remainingQuantity;
    private String reservationType;
    private String referenceNumber;
    private LocalDate reservationDate;
    private LocalDate expiryDate;
    private String reservationStatus;
    private String priority;
    private String notes;
}
