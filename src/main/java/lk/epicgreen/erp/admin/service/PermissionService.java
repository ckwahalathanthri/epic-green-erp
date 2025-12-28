package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.request.PermissionCreateRequest;
import lk.epicgreen.erp.admin.dto.response.PermissionResponse;

import java.util.List;

/**
 * Service interface for Permission entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface PermissionService {

    /**
     * Create new permission
     */
    PermissionResponse createPermission(PermissionCreateRequest request);

    /**
     * Get permission by ID
     */
    PermissionResponse getPermissionById(Long id);

    /**
     * Get permission by code
     */
    PermissionResponse getPermissionByCode(String permissionCode);

    /**
     * Get all permissions
     */
    List<PermissionResponse> getAllPermissions();

    /**
     * Get permissions by module
     */
    List<PermissionResponse> getPermissionsByModule(String module);

    /**
     * Get available modules
     */
    List<String> getAvailableModules();
}
