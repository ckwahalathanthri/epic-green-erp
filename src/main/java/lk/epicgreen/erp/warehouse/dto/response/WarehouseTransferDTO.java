package lk.epicgreen.erp.warehouse.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WarehouseTransferDTO {
    private Long id;
    private String transferNumber;
    private Long fromWarehouseId;
    private String fromWarehouseName;
    private Long toWarehouseId;
    private String toWarehouseName;
    private LocalDate transferDate;
    private LocalDate expectedDeliveryDate;
    private String transferType;
    private String transferStatus;
    private String priority;
    private String approvedBy;
    private List<TransferItemDTO> items;
}