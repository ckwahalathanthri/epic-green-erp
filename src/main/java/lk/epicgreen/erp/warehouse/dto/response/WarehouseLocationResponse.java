package lk.epicgreen.erp.warehouse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for warehouse location response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseLocationResponse {

    private Long id;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private String locationCode;
    private String locationName;
    private String aisle;
    private String rack;
    private String shelf;
    private String bin;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
