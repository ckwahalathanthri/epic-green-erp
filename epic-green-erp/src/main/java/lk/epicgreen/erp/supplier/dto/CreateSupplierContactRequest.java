package lk.epicgreen.erp.supplier.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.AppConstants;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

/**
 * Create Supplier Contact Request DTO
 * Request object for creating a supplier contact
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSupplierContactRequest {
    
    /**
     * Contact person name
     */
    @NotBlank(message = "Contact name is required")
    @Size(min = 2, max = 100, message = ValidationMessages.NAME_SIZE)
    private String contactName;
    
    /**
     * Designation/Job title
     */
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;
    
    /**
     * Department
     */
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;
    
    /**
     * Email address
     */
    @Email(message = ValidationMessages.EMAIL_INVALID)
    @Size(max = 100, message = ValidationMessages.EMAIL_MAX_LENGTH)
    private String email;
    
    /**
     * Phone number
     */
    @Pattern(regexp = AppConstants.PHONE_PATTERN, message = ValidationMessages.PHONE_INVALID)
    private String phoneNumber;
    
    /**
     * Mobile number
     */
    @Pattern(regexp = AppConstants.PHONE_PATTERN, message = ValidationMessages.PHONE_INVALID)
    private String mobileNumber;
    
    /**
     * Extension number
     */
    @Size(max = 10, message = "Extension must not exceed 10 characters")
    private String extension;
    
    /**
     * Is primary contact
     * Default: false
     */
    private Boolean isPrimary;
    
    /**
     * Is active contact
     * Default: true
     */
    private Boolean isActive;
    
    /**
     * Notes about this contact
     */
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
