package lk.epicgreen.erp.admin.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.AppConstants;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.util.Set;

/**
 * Create User Request DTO
 * Request object for creating a new user
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    
    /**
     * Username (unique)
     */
    @NotBlank(message = ValidationMessages.USERNAME_REQUIRED)
    @Size(min = 3, max = 50, message = ValidationMessages.USERNAME_SIZE)
    @Pattern(regexp = AppConstants.USERNAME_PATTERN, message = ValidationMessages.USERNAME_INVALID)
    private String username;
    
    /**
     * Email address (unique)
     */
    @NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
    @Email(message = ValidationMessages.EMAIL_INVALID)
    @Size(max = 100, message = ValidationMessages.EMAIL_MAX_LENGTH)
    private String email;
    
    /**
     * Password
     */
    @NotBlank(message = ValidationMessages.PASSWORD_REQUIRED)
    @Size(min = AppConstants.MIN_PASSWORD_LENGTH, message = ValidationMessages.PASSWORD_MIN_LENGTH)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]",
        message = ValidationMessages.PASSWORD_PATTERN
    )
    private String password;
    
    /**
     * Confirm password (must match password)
     */
    @NotBlank(message = ValidationMessages.CONFIRM_PASSWORD_REQUIRED)
    private String confirmPassword;
    
    /**
     * First name
     */
    @NotBlank(message = ValidationMessages.FIRST_NAME_REQUIRED)
    @Size(min = 2, max = 50, message = ValidationMessages.NAME_SIZE)
    private String firstName;
    
    /**
     * Last name
     */
    @NotBlank(message = ValidationMessages.LAST_NAME_REQUIRED)
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
     * Role IDs to assign to the user
     */
    @NotEmpty(message = ValidationMessages.ROLES_REQUIRED)
    private Set<Long> roleIds;
    
    /**
     * Account status (ACTIVE, INACTIVE, PENDING)
     * Default: ACTIVE
     */
    private String status;
    
    /**
     * Account enabled flag
     * Default: true
     */
    private Boolean enabled;
    
    /**
     * Must change password on first login
     * Default: true
     */
    private Boolean mustChangePassword;
    
    /**
     * Send email verification
     * Default: true
     */
    private Boolean sendEmailVerification;
    
    /**
     * Send welcome email with credentials
     * Default: true
     */
    private Boolean sendWelcomeEmail;
    
    /**
     * Notes about the user
     */
    @Size(max = 1000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    /**
     * Custom validation: password and confirmPassword must match
     */
    @AssertTrue(message = ValidationMessages.PASSWORD_MISMATCH)
    public boolean isPasswordMatching() {
        if (password == null || confirmPassword == null) {
            return true; // Let @NotBlank handle null validation
        }
        return password.equals(confirmPassword);
    }
}
