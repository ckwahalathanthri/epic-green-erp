package lk.epicgreen.erp.admin.mapper;

import lk.epicgreen.erp.admin.dto.request.PermissionCreateRequest;
import lk.epicgreen.erp.admin.dto.response.PermissionResponse;
import lk.epicgreen.erp.admin.entity.Permission;
import org.springframework.stereotype.Component;

/**
 * Mapper for Permission entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class PermissionMapper {

    public Permission toEntity(PermissionCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Permission.builder()
            .permissionName(request.getPermissionName())
            .permissionCode(request.getPermissionCode())
            .module(request.getModule())
            .description(request.getDescription())
            .build();
    }

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
