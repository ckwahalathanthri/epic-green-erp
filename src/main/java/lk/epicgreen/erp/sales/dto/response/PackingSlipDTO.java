package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PackingSlipDTO {
    private Long id;
    private String packingNumber;
    private LocalDate packingDate;
    private Long orderId;
    private String orderNumber;
    private Long pickingListId;
    private Long customerId;
    private String customerName;
    private String shippingAddress;
    private String packingStatus;
    private String packedBy;
    private LocalDateTime packingStartedAt;
    private LocalDateTime packingCompletedAt;
    private Integer totalItems;
    private Integer packedItems;
    private Integer totalPackages;
    private BigDecimal totalWeight;
    private String weightUnit;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PackingSlipItemDTO> items;
}