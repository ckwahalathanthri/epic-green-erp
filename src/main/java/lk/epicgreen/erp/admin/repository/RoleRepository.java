package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Role entity (CORRECTED)
 * Matches actual database schema: role_name, role_code, is_system_role (NO is_active field)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find role by role code
     */
    Optional<Role> findByRoleCode(String roleCode);
    
    /**
     * Find role by role name
     */
    Optional<Role> findByRoleName(String roleName);
    
    /**
     * Find all roles (no is_active field in schema)
     */
    List<Role> findAll();
    
    /**
     * Find all roles with pagination
     */
    Page<Role> findAll(Pageable pageable);
    
    /**
     * Find system roles
     */
    List<Role> findByIsSystemRoleTrue();
    
    /**
     * Find non-system roles (custom roles)
     */
//    List<Role> findByIsSystemRoleFalse();
    
    /**
     * Find roles by role code in list
     */
    List<Role> findByRoleCodeIn(List<String> roleCodes);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if role code exists
     */
    boolean existsByRoleCode(String roleCode);
    
    /**
     * Check if role name exists
     */
    boolean existsByRoleName(String roleName);
    
    /**
     * Check if role code exists excluding specific role ID
     */
    boolean existsByRoleCodeAndIdNot(String roleCode, Long id);
    
    /**
     * Check if role name exists excluding specific role ID
     */
    boolean existsByRoleNameAndIdNot(String roleName, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search roles by role code containing (case-insensitive)
     */
    Page<Role> findByRoleCodeContainingIgnoreCase(String roleCode, Pageable pageable);
    
    /**
     * Search roles by role name containing (case-insensitive)
     */
    Page<Role> findByRoleNameContainingIgnoreCase(String roleName, Pageable pageable);
    
    /**
     * Search roles by role code or role name
     */
    @Query("SELECT r FROM Role r WHERE " +
           "LOWER(r.roleCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.roleName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Role> searchRoles(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search roles by multiple criteria
     */
    @Query("SELECT r FROM Role r WHERE " +
           "(:roleCode IS NULL OR LOWER(r.roleCode) LIKE LOWER(CONCAT('%', :roleCode, '%'))) AND " +
           "(:roleName IS NULL OR LOWER(r.roleName) LIKE LOWER(CONCAT('%', :roleName, '%'))) AND " +
           "(:isSystemRole IS NULL OR r.isSystemRole = :isSystemRole)")
    Page<Role> searchRoles(
            @Param("roleCode") String roleCode,
            @Param("roleName") String roleName,
            @Param("isSystemRole") Boolean isSystemRole,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count all roles
     */
    long count();
    
    /**
     * Count system roles
     */
    long countByIsSystemRoleTrue();
    
    /**
     * Count non-system roles
     */
    long countByIsSystemRoleFalse();
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find roles with specific permission
     */
    @Query("SELECT r FROM Role r JOIN r.rolePermissions rp WHERE rp.permission.id = :permissionId")
    List<Role> findRolesByPermissionId(@Param("permissionId") Long permissionId);
    
    /**
     * Find roles with specific permission code
     */
    @Query("SELECT r FROM Role r JOIN r.rolePermissions rp " +
           "WHERE rp.permission.permissionCode = :permissionCode")
    List<Role> findRolesByPermissionCode(@Param("permissionCode") String permissionCode);
    
    /**
     * Find roles assigned to specific user
     */
    @Query("SELECT r FROM Role r JOIN r.userRoles ur WHERE ur.user.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
    
    /**
     * Find roles with permission count
     */
    @Query("SELECT r, COUNT(rp) as permissionCount FROM Role r " +
           "LEFT JOIN r.rolePermissions rp GROUP BY r.id")
    List<Object[]> findRolesWithPermissionCount();
    
    /**
     * Find roles without any permissions
     */
    @Query("SELECT r FROM Role r WHERE r.rolePermissions IS EMPTY")
    List<Role> findRolesWithoutPermissions();
    
    /**
     * Find roles without any users
     */
    @Query("SELECT r FROM Role r WHERE r.userRoles IS EMPTY")
    List<Role> findRolesWithoutUsers();
    
    /**
     * Get role statistics
     */
    @Query("SELECT " +
           "COUNT(r) as totalRoles, " +
           "SUM(CASE WHEN r.isSystemRole = true THEN 1 ELSE 0 END) as systemRoles, " +
           "SUM(CASE WHEN r.isSystemRole = false THEN 1 ELSE 0 END) as customRoles " +
           "FROM Role r")
    Object getRoleStatistics();
    
    /**
     * Find non-system roles (custom roles)
     */
    List<Role> findByIsSystemRoleFalse();
    
    /**
     * Find roles by system role flag with pagination
     */
    Page<Role> findByIsSystemRole(boolean isSystemRole, Pageable pageable);
    
    /**
     * Find all roles ordered by role name
     */
    List<Role> findAllByOrderByRoleNameAsc();
}
