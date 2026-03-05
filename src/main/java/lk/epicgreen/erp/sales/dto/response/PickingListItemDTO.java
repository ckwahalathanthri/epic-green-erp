package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PickingListItemDTO {
    private Long id;
    private Long pickingListId;
    private Integer lineNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private String warehouseLocation;
    private BigDecimal quantityToPick;
    private BigDecimal pickedQuantity;
    private String unitOfMeasure;
    private String batchNumber;
    private String lotNumber;
    private LocalDate expiryDate;
    private String pickingStrategy;
    private Boolean isPicked;
    private String notes;
}