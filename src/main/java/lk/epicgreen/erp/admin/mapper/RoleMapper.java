package lk.epicgreen.erp.admin.mapper;

import lk.epicgreen.erp.admin.dto.request.RoleCreateRequest;
import lk.epicgreen.erp.admin.dto.response.RoleResponse;
import lk.epicgreen.erp.admin.entity.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper for Role entity and DTOs
 */
@Component
public class RoleMapper {

    private final PermissionMapper permissionMapper;

    public RoleMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public Role toEntity(RoleCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Role.builder()
            .roleName(request.getRoleName())
            .roleCode(request.getRoleCode())
            .description(request.getDescription())
            .isSystemRole(request.getIsSystemRole() != null ? request.getIsSystemRole() : false)
            .build();
    }

    public RoleResponse toResponse(Role role) {
        if (role == null) {
            return null;
        }

        return RoleResponse.builder()
            .id(role.getId())
            .roleName(role.getRoleName())
            .roleCode(role.getRoleCode())
            .description(role.getDescription())
            .isSystemRole(role.getIsSystemRole())
            .permissions(role.getPermissions() != null ? 
                role.getPermissions().stream()
                    .map(permissionMapper::toResponse)
                    .collect(Collectors.toSet()) : 
                null)
            .createdAt(role.getCreatedAt())
            .updatedAt(role.getUpdatedAt())
            .build();
    }
}
