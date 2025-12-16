package lk.epicgreen.erp.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * User DTO
 * Data transfer object for User entity
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
public class UserDTO {
    
    private Long id;
    
    private String username;
    
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    private String fullName;
    
    private String employeeCode;
    
    private String mobileNumber;
    
    private String department;
    
    private String designation;
    
    private String profilePicture;
    
    private String status;
    
    private Boolean enabled;
    
    private Boolean locked;
    
    private Integer failedLoginAttempts;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime lastLoginAt;
    
    private String lastLoginIp;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime passwordChangedAt;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime passwordExpiresAt;
    
    private Boolean mustChangePassword;
    
    private Boolean emailVerified;
    
    private String preferences;
    
    private String notes;
    
    /**
     * User's roles
     */
    private Set<RoleDTO> roles;
    
    /**
     * Flattened list of role codes for easy access
     */
    private Set<String> roleCodes;
    
    /**
     * Flattened list of permission codes for easy access
     */
    private Set<String> permissions;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
    
    /**
     * Computed properties
     */
    private Boolean isActive;
    
    private Boolean isPasswordExpired;
    
    private Boolean isAccountLocked;
}
