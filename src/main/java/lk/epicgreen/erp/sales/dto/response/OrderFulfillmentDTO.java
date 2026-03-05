package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderFulfillmentDTO {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private String fulfillmentStatus;
    private BigDecimal totalQuantityOrdered;
    private BigDecimal totalQuantityPicked;
    private BigDecimal totalQuantityPacked;
    private BigDecimal totalQuantityDispatched;
    private BigDecimal totalQuantityDelivered;
    private Integer pickingListCount;
    private Integer packingSlipCount;
    private Integer dispatchCount;
    private BigDecimal pickingProgress;
    private BigDecimal packingProgress;
    private BigDecimal dispatchProgress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}