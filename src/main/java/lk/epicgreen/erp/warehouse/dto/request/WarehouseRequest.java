package lk.epicgreen.erp.warehouse.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO for creating/updating warehouse
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequest {

    @NotBlank(message = "Warehouse code is required")
    @Size(max = 20, message = "Warehouse code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Warehouse code must contain only uppercase letters, numbers, hyphens and underscores")
    private String warehouseCode;

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 100, message = "Warehouse name must not exceed 100 characters")
    private String warehouseName;

    @NotBlank(message = "Warehouse type is required")
    @Pattern(regexp = "^(RAW_MATERIAL|FINISHED_GOODS|MIXED)$", 
             message = "Warehouse type must be one of: RAW_MATERIAL, FINISHED_GOODS, MIXED")
    private String warehouseType;

    @Size(max = 200, message = "Address line 1 must not exceed 200 characters")
    private String addressLine1;

    @Size(max = 200, message = "Address line 2 must not exceed 200 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    private Long managerId;

    @Size(max = 20, message = "Contact number must not exceed 20 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Invalid contact number format")
    private String contactNumber;

    private Boolean isActive;
}
