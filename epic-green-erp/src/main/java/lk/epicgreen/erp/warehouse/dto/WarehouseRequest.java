package lk.epicgreen.erp.warehouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Warehouse Request DTO
 * DTO for warehouse operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequest {
    
    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;
    
    @NotBlank(message = "Warehouse name is required")
    private String warehouseName;
    
    private String warehouseType; // MAIN, BRANCH, TRANSIT, VIRTUAL, RETAIL
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private String city;
    
    private String stateProvince;
    
    private String postalCode;
    
    private String country;
    
    private String contactPerson;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    
    private String mobile;
    
    private String fax;
    
    private String managerUserId;
    
    private String managerName;
    
    private Double storageCapacity; // in cubic meters or square meters
    
    private String capacityUnit;
    
    private Integer numberOfZones;
    
    private Integer numberOfAisles;
    
    private Integer numberOfRacks;
    
    private String operatingHours;
    
    private String workingDays;
    
    private String description;
    
    private String notes;
    
    private Boolean isColdStorage;
    
    private Boolean hasLoadingDock;
    
    private Boolean hasSecuritySystem;
    
    private Boolean hasForklifts;
    
    private String facilityFeatures; // JSON array
}
