package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StockTransferDTO {
    private Long id;
    private String transferNumber;
    private LocalDate transferDate;
    private Long fromWarehouseId;
    private Long toWarehouseId;
    private String transferStatus;
    private String priority;
    private LocalDate expectedDeliveryDate;
    private String vehicleNumber;
    private String driverName;
    private String notes;
    private List<StockTransferItemDTO> items;
}
