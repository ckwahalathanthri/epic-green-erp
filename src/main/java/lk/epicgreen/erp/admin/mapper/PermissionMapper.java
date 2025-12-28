package lk.epicgreen.erp.admin.mapper;

import lk.epicgreen.erp.admin.dto.response.PermissionResponse;
import lk.epicgreen.erp.admin.entity.Permission;
import org.springframework.stereotype.Component;

/**
 * Mapper for Permission entity and DTOs
 */
@Component
public class PermissionMapper {

    public PermissionResponse toResponse(Permission permission) {
        if (permission == null) {
            return null;
        }

        return PermissionResponse.builder()
            .id(permission.getId())
            .permissionName(permission.getPermissionName())
            .permissionCode(permission.getPermissionCode())
            .module(permission.getModule())
            .description(permission.getDescription())
            .build();
    }
}
