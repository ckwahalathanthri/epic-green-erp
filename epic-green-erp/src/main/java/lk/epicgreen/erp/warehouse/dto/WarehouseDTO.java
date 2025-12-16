package lk.epicgreen.erp.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Warehouse DTO
 * Data transfer object for Warehouse entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarehouseDTO {
    
    private Long id;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private String warehouseType;
    
    private String description;
    
    /**
     * Management
     */
    private String managerName;
    
    private String contactPerson;
    
    /**
     * Contact details
     */
    private String email;
    
    private String phoneNumber;
    
    private String mobileNumber;
    
    /**
     * Address
     */
    private String addressLine1;
    
    private String addressLine2;
    
    private String city;
    
    private String state;
    
    private String postalCode;
    
    private String country;
    
    private String fullAddress;
    
    /**
     * Flags
     */
    private Boolean isMainWarehouse;
    
    private Boolean isActive;
    
    /**
     * Details
     */
    private String operatingHours;
    
    private Integer capacity;
    
    private String capacityUnit;
    
    private String facilities;
    
    private String notes;
    
    /**
     * Statistics
     */
    private Long totalLocations;
    
    private Long activeLocations;
    
    private Long totalProducts;
    
    private Long lowStockProducts;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
}
