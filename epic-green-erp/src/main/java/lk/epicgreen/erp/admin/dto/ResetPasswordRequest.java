package lk.epicgreen.erp.admin.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.AppConstants;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

/**
 * Reset Password Request DTO
 * Request object for resetting user password (admin or forgot password)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {
    
    /**
     * Password reset token (for forgot password flow)
     */
    private String token;
    
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
}
