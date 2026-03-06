package lk.epicgreen.erp.warehouse.dto.response;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class WarehouseDTO {
    private Long id;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseType;
    private String warehouseStatus;
    private String address;
    private String city;
    private String contactPerson;
    private BigDecimal totalCapacity;
}