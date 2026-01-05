package lk.epicgreen.erp.audit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

/**
 * DTO for creating Activity Log
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Activity type is required")
    @Size(max = 50, message = "Activity type must not exceed 50 characters")
    private String activityType;

    @NotBlank(message = "Module is required")
    @Size(max = 50, message = "Module must not exceed 50 characters")
    private String module;

    @Size(max = 1000, message = "Activity description must not exceed 1000 characters")
    private String activityDescription;

    @Size(max = 50, message = "Reference type must not exceed 50 characters")
    private String referenceType;

    private Long referenceId;

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    private String ipAddress;

    @NotBlank(message = "Device type is required")
    @Pattern(regexp = "^(WEB|MOBILE_ANDROID|MOBILE_IOS|API)$", 
             message = "Device type must be one of: WEB, MOBILE_ANDROID, MOBILE_IOS, API")
    private String deviceType;
}
