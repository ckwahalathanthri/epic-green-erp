package lk.epicgreen.erp.admin.dto;

import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.util.Set;

/**
 * Update Role Request DTO
 * Request object for updating an existing role
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {
    
    /**
     * Role name (display name)
     */
    @Size(min = 3, max = 100, message = ValidationMessages.NAME_SIZE)
    private String roleName;
    
    /**
     * Role description
     */
    @Size(max = 500, message = ValidationMessages.DESCRIPTION_MAX_LENGTH)
    private String description;
    
    /**
     * Permission IDs to assign to this role
     * If provided, replaces existing permissions
     */
    private Set<Long> permissionIds;
    
    /**
     * Active status
     */
    private Boolean isActive;
    
    /**
     * Display order
     */
    @Min(value = 0, message = "Display order must be non-negative")
    private Integer displayOrder;
}
