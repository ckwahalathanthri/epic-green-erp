package lk.epicgreen.erp.admin.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.AppConstants;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

/**
 * Change Password Request DTO
 * Request object for changing user password
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    
    /**
     * Current password
     */
    @NotBlank(message = ValidationMessages.CURRENT_PASSWORD_REQUIRED)
    private String currentPassword;
    
    /**
     * New password
     */
    @NotBlank(message = ValidationMessages.NEW_PASSWORD_REQUIRED)
    @Size(min = AppConstants.MIN_PASSWORD_LENGTH, message = ValidationMessages.PASSWORD_MIN_LENGTH)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]",
        message = ValidationMessages.PASSWORD_PATTERN
    )
    private String newPassword;
    
    /**
     * Confirm new password
     */
    @NotBlank(message = ValidationMessages.CONFIRM_PASSWORD_REQUIRED)
    private String confirmNewPassword;
    
    /**
     * Custom validation: newPassword and confirmNewPassword must match
     */
    @AssertTrue(message = ValidationMessages.PASSWORD_MISMATCH)
    public boolean isPasswordMatching() {
        if (newPassword == null || confirmNewPassword == null) {
            return true;
        }
        return newPassword.equals(confirmNewPassword);
    }
    
    /**
     * Custom validation: newPassword must be different from currentPassword
     */
    @AssertTrue(message = "New password must be different from current password")
    public boolean isPasswordDifferent() {
        if (currentPassword == null || newPassword == null) {
            return true;
        }
        return !currentPassword.equals(newPassword);
    }
}
