package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.Permission;
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
 * Repository interface for Permission entity (CORRECTED)
 * Matches actual database schema: permission_name, permission_code, module, description
 * (NO is_active, action, or resource_type fields)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find permission by permission code
     */
    Optional<Permission> findByPermissionCode(String permissionCode);
    
    /**
     * Find permission by permission name
     */
    Optional<Permission> findByPermissionName(String permissionName);
    
    /**
     * Find all permissions
     */
    List<Permission> findAll();
    
    /**
     * Find all permissions with pagination
     */
    Page<Permission> findAll(Pageable pageable);
    
    /**
     * Find permissions by module
     */
    List<Permission> findByModule(String module);
    
    /**
     * Find permissions by module with pagination
     */
    Page<Permission> findByModule(String module, Pageable pageable);
    
    /**
     * Find permissions by permission codes
     */
    List<Permission> findByPermissionCodeIn(List<String> permissionCodes);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if permission code exists
     */
    boolean existsByPermissionCode(String permissionCode);
    
    /**
     * Check if permission name exists
     */
    boolean existsByPermissionName(String permissionName);
    
    /**
     * Check if permission code exists excluding specific permission ID
     */
    boolean existsByPermissionCodeAndIdNot(String permissionCode, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search permissions by permission code containing (case-insensitive)
     */
    Page<Permission> findByPermissionCodeContainingIgnoreCase(String permissionCode, Pageable pageable);
    
    /**
     * Search permissions by permission name containing (case-insensitive)
     */
    Page<Permission> findByPermissionNameContainingIgnoreCase(String permissionName, Pageable pageable);
    
    /**
     * Search permissions by keyword
     */
    @Query("SELECT p FROM Permission p WHERE " +
           "LOWER(p.permissionCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.permissionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.module) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Permission> searchPermissions(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search permissions by multiple criteria
     */
    @Query("SELECT p FROM Permission p WHERE " +
           "(:permissionCode IS NULL OR LOWER(p.permissionCode) LIKE LOWER(CONCAT('%', :permissionCode, '%'))) AND " +
           "(:permissionName IS NULL OR LOWER(p.permissionName) LIKE LOWER(CONCAT('%', :permissionName, '%'))) AND " +
           "(:module IS NULL OR p.module = :module)")
    Page<Permission> searchPermissions(
            @Param("permissionCode") String permissionCode,
            @Param("permissionName") String permissionName,
            @Param("module") String module,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count all permissions
     */
    long count();
    
    /**
     * Count permissions by module
     */
    long countByModule(String module);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find permissions assigned to specific role
     */
    @Query("SELECT p FROM Permission p JOIN p.rolePermissions rp WHERE rp.role.id = :roleId")
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * Find permissions assigned to specific role code
     */
    @Query("SELECT p FROM Permission p JOIN p.rolePermissions rp " +
           "WHERE rp.role.roleCode = :roleCode")
    List<Permission> findPermissionsByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * Find permissions for specific user (through roles)
     */
    @Query("SELECT DISTINCT p FROM Permission p " +
           "JOIN p.rolePermissions rp " +
           "JOIN rp.role r " +
           "JOIN r.userRoles ur " +
           "WHERE ur.user.id = :userId")
    List<Permission> findPermissionsByUserId(@Param("userId") Long userId);
    
    /**
     * Find permissions for specific user by username
     */
    @Query("SELECT DISTINCT p FROM Permission p " +
           "JOIN p.rolePermissions rp " +
           "JOIN rp.role r " +
           "JOIN r.userRoles ur " +
           "WHERE ur.user.username = :username")
    List<Permission> findPermissionsByUsername(@Param("username") String username);
    
    /**
     * Find all distinct modules
     */
    @Query("SELECT DISTINCT p.module FROM Permission p WHERE p.module IS NOT NULL ORDER BY p.module")
    List<String> findAllDistinctModules();
    
    /**
     * Get permission statistics by module
     */
    @Query("SELECT p.module, COUNT(p) as permissionCount " +
           "FROM Permission p GROUP BY p.module")
    List<Object[]> getPermissionStatisticsByModule();
    
    /**
     * Find permissions without any roles
     */
    @Query("SELECT p FROM Permission p WHERE p.rolePermissions IS EMPTY")
    List<Permission> findPermissionsWithoutRoles();
    
    /**
     * Find permissions by module ordered by code
     */
    List<Permission> findByModuleOrderByPermissionCodeAsc(String module);
    
    /**
     * Check if user has specific permission
     */
    @Query("SELECT COUNT(p) > 0 FROM Permission p " +
           "JOIN p.rolePermissions rp " +
           "JOIN rp.role r " +
           "JOIN r.userRoles ur " +
           "WHERE ur.user.id = :userId AND p.permissionCode = :permissionCode")
    boolean userHasPermission(@Param("userId") Long userId, @Param("permissionCode") String permissionCode);
    
    /**
     * Find all permissions ordered by module and code
     */
    List<Permission> findAllByOrderByModuleAscPermissionCodeAsc();
}
