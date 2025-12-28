package lk.epicgreen.erp.warehouse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * DTO for creating/updating warehouse location
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseLocationRequest {

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotBlank(message = "Location code is required")
    @Size(max = 30, message = "Location code must not exceed 30 characters")
    private String locationCode;

    @Size(max = 100, message = "Location name must not exceed 100 characters")
    private String locationName;

    @Size(max = 10, message = "Aisle must not exceed 10 characters")
    private String aisle;

    @Size(max = 10, message = "Rack must not exceed 10 characters")
    private String rack;

    @Size(max = 10, message = "Shelf must not exceed 10 characters")
    private String shelf;

    @Size(max = 10, message = "Bin must not exceed 10 characters")
    private String bin;

    private Boolean isActive;
}
