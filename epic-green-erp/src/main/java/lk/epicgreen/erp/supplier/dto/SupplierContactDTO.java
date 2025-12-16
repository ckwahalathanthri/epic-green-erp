package lk.epicgreen.erp.supplier.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;

/**
 * SupplierContact DTO
 * Data transfer object for SupplierContact entity
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
public class SupplierContactDTO {
    
    private Long id;
    
    private Long supplierId;
    
    private String contactName;
    
    private String designation;
    
    private String department;
    
    private String email;
    
    private String phoneNumber;
    
    private String mobileNumber;
    
    private String extension;
    
    private Boolean isPrimary;
    
    private Boolean isActive;
    
    private String notes;
    
    private String fullContactInfo;
    
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
