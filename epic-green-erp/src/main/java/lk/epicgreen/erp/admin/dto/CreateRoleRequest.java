package lk.epicgreen.erp.admin.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.util.Set;

/**
 * Create Role Request DTO
 * Request object for creating a new role
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleRequest {
    
    /**
     * Role code (unique, e.g., ROLE_MANAGER)
     */
    @NotBlank(message = ValidationMessages.ROLE_CODE_REQUIRED)
    @Size(min = 3, max = 50, message = ValidationMessages.CODE_SIZE)
    @Pattern(regexp = "^ROLE_[A-Z_]+$", message = "Role code must start with ROLE_ and contain only uppercase letters and underscores")
    private String roleCode;
    
    /**
     * Role name (display name)
     */
    @NotBlank(message = ValidationMessages.ROLE_NAME_REQUIRED)
    @Size(min = 3, max = 100, message = ValidationMessages.NAME_SIZE)
    private String roleName;
    
    /**
     * Role description
     */
    @Size(max = 500, message = ValidationMessages.DESCRIPTION_MAX_LENGTH)
    private String description;
    
    /**
     * Role type (SYSTEM, DEPARTMENT, CUSTOM)
     * Default: CUSTOM
     */
    private String roleType;
    
    /**
     * Permission IDs to assign to this role
     */
    @NotEmpty(message = ValidationMessages.PERMISSIONS_REQUIRED)
    private Set<Long> permissionIds;
    
    /**
     * Active status
     * Default: true
     */
    private Boolean isActive;
    
    /**
     * Display order
     */
    @Min(value = 0, message = "Display order must be non-negative")
    private Integer displayOrder;
}
