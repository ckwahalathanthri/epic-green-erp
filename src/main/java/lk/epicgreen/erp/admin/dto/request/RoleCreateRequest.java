package lk.epicgreen.erp.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {

    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    private String roleName;

    @NotBlank(message = "Role code is required")
    @Size(max = 30, message = "Role code must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Role code must be uppercase letters, numbers and underscores only")
    private String roleCode;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Boolean isSystemRole;

    private Set<Long> permissionIds;
}
