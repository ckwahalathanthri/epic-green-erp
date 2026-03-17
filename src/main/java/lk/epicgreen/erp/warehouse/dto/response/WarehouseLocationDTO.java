package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;

@Data
public class WarehouseLocationDTO {
    private Long id;
    private Long warehouseId;
    private String locationCode;
    private String locationType;
    private String locationStatus;
    private String zone;
    private String aisle;
    private String rack;
}