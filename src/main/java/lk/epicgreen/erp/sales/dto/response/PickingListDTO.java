package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PickingListDTO {
    private Long id;
    private String pickingNumber;
    private LocalDate pickingDate;
    private Long orderId;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private Long warehouseId;
    private String warehouseName;
    private String pickingStatus;
    private String priority;
    private String assignedTo;
    private LocalDateTime pickingStartedAt;
    private LocalDateTime pickingCompletedAt;
    private Integer totalItems;
    private Integer pickedItems;
    private String notes;
    private String pickingNotes;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PickingListItemDTO> items;
}