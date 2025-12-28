package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.Permission;
import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RolePermission entity (CORRECTED)
 * Matches actual database schema: granted_by is BIGINT (not User entity)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all permissions for a specific role
     */
    List<RolePermission> findByRole(Role role);
    
    /**
     * Find all permissions for a specific role by role ID
     */
    List<RolePermission> findByRoleId(Long roleId);
    
    /**
     * Find all roles for a specific permission
     */
    List<RolePermission> findByPermission(Permission permission);
    
    /**
     * Find all roles for a specific permission by permission ID
     */
    List<RolePermission> findByPermissionId(Long permissionId);
    
    /**
     * Find specific role-permission assignment
     */
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
    
    /**
     * Find role-permission by role ID and permission ID
     */
    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    /**
     * Find permissions by role code
     */
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.roleCode = :roleCode")
    List<RolePermission> findByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * Find roles by permission code
     */
    @Query("SELECT rp FROM RolePermission rp WHERE rp.permission.permissionCode = :permissionCode")
    List<RolePermission> findByPermissionCode(@Param("permissionCode") String permissionCode);
    
    /**
     * Find role-permission by role code and permission code
     */
    @Query("SELECT rp FROM RolePermission rp " +
           "WHERE rp.role.roleCode = :roleCode AND rp.permission.permissionCode = :permissionCode")
    Optional<RolePermission> findByRoleCodeAndPermissionCode(
            @Param("roleCode") String roleCode, 
            @Param("permissionCode") String permissionCode);
    
    /**
     * Find assignments granted between dates
     */
    List<RolePermission> findByGrantedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find assignments by granted by user ID (granted_by is BIGINT)
     */
    List<RolePermission> findByGrantedBy(Long grantedBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if role-permission assignment exists
     */
    boolean existsByRoleAndPermission(Role role, Permission permission);
    
    /**
     * Check if role-permission assignment exists by IDs
     */
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    /**
     * Check if role has specific permission code
     */
    @Query("SELECT COUNT(rp) > 0 FROM RolePermission rp " +
           "WHERE rp.role.id = :roleId AND rp.permission.permissionCode = :permissionCode")
    boolean roleHasPermission(@Param("roleId") Long roleId, @Param("permissionCode") String permissionCode);
    
    /**
     * Check if role has any of the specified permission codes
     */
    @Query("SELECT COUNT(rp) > 0 FROM RolePermission rp " +
           "WHERE rp.role.id = :roleId AND rp.permission.permissionCode IN :permissionCodes")
    boolean roleHasAnyPermission(@Param("roleId") Long roleId, @Param("permissionCodes") List<String> permissionCodes);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count permissions for a specific role
     */
    long countByRoleId(Long roleId);
    
    /**
     * Count roles for a specific permission
     */
    long countByPermissionId(Long permissionId);
    
    /**
     * Count total role-permission assignments
     */
    long count();
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete role-permission assignment
     */
    void deleteByRoleAndPermission(Role role, Permission permission);
    
    /**
     * Delete role-permission assignment by IDs
     */
    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    /**
     * Delete all permissions for a specific role
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = :roleId")
    void deleteAllByRoleId(@Param("roleId") Long roleId);
    
    /**
     * Delete all roles for a specific permission
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permission.id = :permissionId")
    void deleteAllByPermissionId(@Param("permissionId") Long permissionId);
    
    /**
     * Delete specific permission codes for a role
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp " +
           "WHERE rp.role.id = :roleId AND rp.permission.permissionCode IN :permissionCodes")
    void deleteByRoleIdAndPermissionCodes(
            @Param("roleId") Long roleId, 
            @Param("permissionCodes") List<String> permissionCodes);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find permissions by role and module
     */
    @Query("SELECT rp FROM RolePermission rp " +
           "WHERE rp.role.id = :roleId AND rp.permission.module = :module")
    List<RolePermission> findByRoleIdAndModule(@Param("roleId") Long roleId, @Param("module") String module);
    
    /**
     * Get permission assignment statistics
     */
    @Query("SELECT " +
           "COUNT(DISTINCT rp.role.id) as totalRoles, " +
           "COUNT(DISTINCT rp.permission.id) as totalPermissions, " +
           "COUNT(rp) as totalAssignments " +
           "FROM RolePermission rp")
    Object getAssignmentStatistics();
    
    /**
     * Get permissions count by role
     */
    @Query("SELECT rp.role.roleName, COUNT(rp.permission) as permissionCount " +
           "FROM RolePermission rp GROUP BY rp.role.id, rp.role.roleName " +
           "ORDER BY permissionCount DESC")
    List<Object[]> getPermissionCountByRole();
    
    /**
     * Get roles count by permission
     */
    @Query("SELECT rp.permission.permissionName, COUNT(rp.role) as roleCount " +
           "FROM RolePermission rp GROUP BY rp.permission.id, rp.permission.permissionName " +
           "ORDER BY roleCount DESC")
    List<Object[]> getRoleCountByPermission();
    
    /**
     * Find permissions by module for a role
     */
    @Query("SELECT rp.permission.module, COUNT(rp) as permissionCount " +
           "FROM RolePermission rp WHERE rp.role.id = :roleId " +
           "GROUP BY rp.permission.module")
    List<Object[]> getPermissionCountByModuleForRole(@Param("roleId") Long roleId);
    
    /**
     * Find duplicate permission assignments for a role
     */
    @Query("SELECT rp.role.id, rp.permission.id, COUNT(rp) " +
           "FROM RolePermission rp " +
           "GROUP BY rp.role.id, rp.permission.id " +
           "HAVING COUNT(rp) > 1")
    List<Object[]> findDuplicateAssignments();
    
    /**
     * Find all role-permissions ordered by role and permission
     */
    @Query("SELECT rp FROM RolePermission rp ORDER BY rp.role.roleName, rp.permission.permissionName")
    List<RolePermission> findAllOrdered();
}
