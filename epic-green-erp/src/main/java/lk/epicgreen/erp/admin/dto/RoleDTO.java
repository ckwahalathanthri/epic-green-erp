package lk.epicgreen.erp.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Role DTO
 * Data transfer object for Role entity
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
public class RoleDTO {
    
    private Long id;
    
    private String roleCode;
    
    private String roleName;
    
    private String description;
    
    private String roleType;
    
    private Boolean isSystemRole;
    
    private Boolean isActive;
    
    private Integer displayOrder;
    
    /**
     * Permissions assigned to this role
     */
    private Set<PermissionDTO> permissions;
    
    /**
     * Flattened list of permission codes for easy access
     */
    private Set<String> permissionCodes;
    
    /**
     * Number of users with this role
     */
    private Long userCount;
    
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
