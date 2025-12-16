package lk.epicgreen.erp.admin.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.AppConstants;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.util.Set;

/**
 * Update User Request DTO
 * Request object for updating an existing user
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    
    /**
     * Email address (unique)
     */
    @Email(message = ValidationMessages.EMAIL_INVALID)
    @Size(max = 100, message = ValidationMessages.EMAIL_MAX_LENGTH)
    private String email;
    
    /**
     * First name
     */
    @Size(min = 2, max = 50, message = ValidationMessages.NAME_SIZE)
    private String firstName;
    
    /**
     * Last name
     */
    @Size(min = 2, max = 50, message = ValidationMessages.NAME_SIZE)
    private String lastName;
    
    /**
     * Employee code (optional, unique if provided)
     */
    @Size(max = 20, message = ValidationMessages.EMPLOYEE_CODE_MAX_LENGTH)
    private String employeeCode;
    
    /**
     * Mobile number
     */
    @Pattern(regexp = AppConstants.PHONE_PATTERN, message = ValidationMessages.PHONE_INVALID)
    private String mobileNumber;
    
    /**
     * Department
     */
    @Size(max = 50, message = ValidationMessages.DEPARTMENT_MAX_LENGTH)
    private String department;
    
    /**
     * Designation/Job title
     */
    @Size(max = 50, message = ValidationMessages.DESIGNATION_MAX_LENGTH)
    private String designation;
    
    /**
     * Profile picture URL
     */
    @Size(max = 255, message = ValidationMessages.URL_MAX_LENGTH)
    private String profilePicture;
    
    /**
     * Role IDs to assign to the user
     * If provided, replaces existing roles
     */
    private Set<Long> roleIds;
    
    /**
     * Account status (ACTIVE, INACTIVE, LOCKED, PENDING)
     */
    private String status;
    
    /**
     * Account enabled flag
     */
    private Boolean enabled;
    
    /**
     * Account locked flag
     */
    private Boolean locked;
    
    /**
     * Notes about the user
     */
    @Size(max = 1000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    /**
     * User preferences (JSON string)
     */
    private String preferences;
}
