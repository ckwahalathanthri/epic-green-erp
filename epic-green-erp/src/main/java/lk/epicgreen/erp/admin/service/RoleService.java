package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.RoleRequest;
import lk.epicgreen.erp.admin.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Role Service Interface
 * Service for role operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface RoleService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Role createRole(RoleRequest request);
    Role updateRole(Long id, RoleRequest request);
    void deleteRole(Long id);
    Role getRoleById(Long id);
    Role getRoleByName(String roleName);
    Role getRoleByCode(String roleCode);
    List<Role> getAllRoles();
    Page<Role> getAllRoles(Pageable pageable);
    Page<Role> searchRoles(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Role activateRole(Long id);
    Role deactivateRole(Long id);
    
    // ===================================================================
    // PERMISSION MANAGEMENT
    // ===================================================================
    
    void assignPermission(Long roleId, Long permissionId);
    void assignPermissions(Long roleId, List<Long> permissionIds);
    void removePermission(Long roleId, Long permissionId);
    void removeAllPermissions(Long roleId);
    void updateRolePermissions(Long roleId, List<Long> permissionIds);
    List<String> getRolePermissions(Long roleId);
    boolean hasPermission(Long roleId, String permissionName);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Role> getActiveRoles();
    Page<Role> getActiveRoles(Pageable pageable);
    List<Role> getInactiveRoles();
    List<Role> getSystemRoles();
    List<Role> getCustomRoles();
    List<Role> getRolesByCategory(String category);
    List<Role> getRolesByLevel(Integer level);
    List<Role> getRolesByLevelRange(Integer minLevel, Integer maxLevel);
    List<Role> getRolesByPermission(String permissionName);
    List<Role> getRolesWithPermissions();
    List<Role> getRolesWithoutPermissions();
    List<Role> getRolesByUserCount(Integer minUsers);
    List<Role> getRolesWithNoUsers();
    List<Role> getRootRoles();
    List<Role> getChildRoles(Long parentId);
    List<Role> getAdministrativeRoles();
    List<Role> getOperationalRoles();
    List<Role> getRecentRoles(int limit);
    List<Role> getRecentlyUpdatedRoles(int limit);
    List<Role> getRolesWithMostUsers(int limit);
    List<Role> getRolesWithMostPermissions(int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateRole(Role role);
    boolean isRoleNameAvailable(String roleName);
    boolean isRoleCodeAvailable(String roleCode);
    boolean canDeleteRole(Long roleId);
    boolean isSystemRole(Long roleId);
    
    // ===================================================================
    // HIERARCHY OPERATIONS
    // ===================================================================
    
    void setParentRole(Long roleId, Long parentId);
    void removeParentRole(Long roleId);
    Role getParentRole(Long roleId);
    List<Role> getRoleHierarchy(Long roleId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Role> createBulkRoles(List<RoleRequest> requests);
    int activateBulkRoles(List<Long> roleIds);
    int deactivateBulkRoles(List<Long> roleIds);
    int deleteBulkRoles(List<Long> roleIds);
    int assignPermissionToBulkRoles(List<Long> roleIds, Long permissionId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getRoleStatistics();
    List<Map<String, Object>> getCategoryDistribution();
    List<Map<String, Object>> getLevelDistribution();
    List<Map<String, Object>> getRolesByUserCount();
    List<Map<String, Object>> getRolesByPermissionCount();
    Double getAveragePermissionsPerRole();
    Double getAverageUsersPerRole();
    Long getTotalRoles();
    Long getActiveRolesCount();
    Long getInactiveRolesCount();
    Long getSystemRolesCount();
    Long getCustomRolesCount();
    Map<String, Object> getDashboardStatistics();
}
