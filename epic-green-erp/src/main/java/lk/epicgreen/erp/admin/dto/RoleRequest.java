package lk.epicgreen.erp.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Role Request DTO
 * DTO for role operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    
    @NotBlank(message = "Role name is required")
    private String roleName;
    
    @NotBlank(message = "Role code is required")
    private String roleCode;
    
    private String description;
    
    private String category;
    
    @NotNull(message = "Level is required")
    private Integer level;
    
    private Long parentRoleId;
    
    private List<Long> permissionIds;
}
