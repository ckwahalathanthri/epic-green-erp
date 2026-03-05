package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DispatchDTO {
    private Long id;
    private String dispatchNumber;
    private LocalDate dispatchDate;
    private Long orderId;
    private String orderNumber;
    private Long packingSlipId;
    private Long customerId;
    private String customerName;
    private String shippingAddress;
    private String dispatchStatus;
    private String dispatchType;
    private Long vehicleId;
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private Integer totalPackages;
    private BigDecimal totalWeight;
    private BigDecimal freightCharge;
    private String trackingNumber;
    private String routeDetails;
    private String gpsCoordinates;
    private Boolean stockDeducted;
    private LocalDateTime stockDeductedAt;
    private String dispatchedBy;
    private LocalDateTime dispatchedAt;
    private String deliveredBy;
    private LocalDateTime deliveredAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DispatchItemDTO> items;
}