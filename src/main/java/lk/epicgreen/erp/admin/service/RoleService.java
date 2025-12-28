package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.request.RoleCreateRequest;
import lk.epicgreen.erp.admin.dto.request.RoleUpdateRequest;
import lk.epicgreen.erp.admin.dto.response.RoleResponse;

import java.util.List;

/**
 * Service interface for Role entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface RoleService {

    /**
     * Create new role
     */
    RoleResponse createRole(RoleCreateRequest request);

    /**
     * Update existing role
     */
    RoleResponse updateRole(Long id, RoleUpdateRequest request);

    /**
     * Delete role
     */
    void deleteRole(Long id);

    /**
     * Assign permission to role
     */
    void assignPermission(Long roleId, Long permissionId);

    /**
     * Remove permission from role
     */
    void removePermission(Long roleId, Long permissionId);

    /**
     * Assign permissions in bulk
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * Get role by ID
     */
    RoleResponse getRoleById(Long id);

    /**
     * Get role by code
     */
    RoleResponse getRoleByCode(String roleCode);

    /**
     * Get all roles
     */
    List<RoleResponse> getAllRoles();

    /**
     * Get non-system roles only
     */
    List<RoleResponse> getNonSystemRoles();

    /**
     * Get system roles only
     */
    List<RoleResponse> getSystemRoles();
}
