package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Permission Repository
 * Repository for permission data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find permission by name
     */
    Optional<Permission> findByPermissionName(String permissionName);
    
    /**
     * Check if permission exists by name
     */
    boolean existsByPermissionName(String permissionName);
    
    /**
     * Find permission by code
     */
    Optional<Permission> findByPermissionCode(String permissionCode);
    
    /**
     * Check if permission exists by code
     */
    boolean existsByPermissionCode(String permissionCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find permissions by module
     */
    List<Permission> findByModule(String module);
    
    /**
     * Find permissions by resource
     */
    List<Permission> findByResource(String resource);
    
    /**
     * Find permissions by action
     */
    List<Permission> findByAction(String action);
    
    /**
     * Find permissions by is active
     */
    List<Permission> findByIsActive(Boolean isActive);
    
    /**
     * Find permissions by is system permission
     */
    List<Permission> findByIsSystemPermission(Boolean isSystemPermission);
    
    /**
     * Find permissions by category
     */
    List<Permission> findByCategory(String category);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find permissions by created at between dates
     */
    List<Permission> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find permissions by module and is active
     */
    List<Permission> findByModuleAndIsActive(String module, Boolean isActive);
    
    /**
     * Find permissions by resource and action
     */
    List<Permission> findByResourceAndAction(String resource, String action);
    
    /**
     * Find permissions by module and resource
     */
    List<Permission> findByModuleAndResource(String module, String resource);
    
    /**
     * Find permissions by category and is active
     */
    List<Permission> findByCategoryAndIsActive(String category, Boolean isActive);
    
    /**
     * Find permissions by is active and is system permission
     */
    List<Permission> findByIsActiveAndIsSystemPermission(Boolean isActive, Boolean isSystemPermission);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search permissions
     */
    @Query("SELECT p FROM Permission p WHERE " +
           "LOWER(p.permissionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.permissionCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.module) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.resource) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.action) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Permission> searchPermissions(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active permissions
     */
    @Query("SELECT p FROM Permission p WHERE p.isActive = true " +
           "ORDER BY p.module ASC, p.resource ASC, p.action ASC")
    List<Permission> findActivePermissions();
    
    /**
     * Find active permissions with pagination
     */
    @Query("SELECT p FROM Permission p WHERE p.isActive = true " +
           "ORDER BY p.module ASC, p.resource ASC, p.action ASC")
    Page<Permission> findActivePermissions(Pageable pageable);
    
    /**
     * Find inactive permissions
     */
    @Query("SELECT p FROM Permission p WHERE p.isActive = false " +
           "ORDER BY p.permissionName ASC")
    List<Permission> findInactivePermissions();
    
    /**
     * Find system permissions
     */
    @Query("SELECT p FROM Permission p WHERE p.isSystemPermission = true " +
           "ORDER BY p.module ASC, p.resource ASC, p.action ASC")
    List<Permission> findSystemPermissions();
    
    /**
     * Find custom permissions (non-system)
     */
    @Query("SELECT p FROM Permission p WHERE p.isSystemPermission = false AND p.isActive = true " +
           "ORDER BY p.module ASC, p.resource ASC, p.action ASC")
    List<Permission> findCustomPermissions();
    
    /**
     * Find permissions by role ID
     */
    @Query("SELECT DISTINCT p FROM Permission p JOIN p.roles r WHERE r.id = :roleId")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);
    
    /**
     * Find permissions by role name
     */
    @Query("SELECT DISTINCT p FROM Permission p JOIN p.roles r WHERE r.roleName = :roleName")
    List<Permission> findByRoleName(@Param("roleName") String roleName);
    
    /**
     * Find permissions with roles
     */
    @Query("SELECT p FROM Permission p WHERE SIZE(p.roles) > 0 AND p.isActive = true " +
           "ORDER BY p.module ASC")
    List<Permission> findPermissionsWithRoles();
    
    /**
     * Find permissions without roles
     */
    @Query("SELECT p FROM Permission p WHERE SIZE(p.roles) = 0 " +
           "ORDER BY p.permissionName ASC")
    List<Permission> findPermissionsWithoutRoles();
    
    /**
     * Find permissions by action (CREATE, READ, UPDATE, DELETE)
     */
    @Query("SELECT p FROM Permission p WHERE UPPER(p.action) = UPPER(:action) " +
           "AND p.isActive = true ORDER BY p.module ASC, p.resource ASC")
    List<Permission> findByActionType(@Param("action") String action);
    
    /**
     * Find read permissions
     */
    @Query("SELECT p FROM Permission p WHERE UPPER(p.action) IN ('READ', 'VIEW', 'GET') " +
           "AND p.isActive = true ORDER BY p.module ASC, p.resource ASC")
    List<Permission> findReadPermissions();
    
    /**
     * Find write permissions
     */
    @Query("SELECT p FROM Permission p WHERE UPPER(p.action) IN ('CREATE', 'UPDATE', 'DELETE', 'WRITE') " +
           "AND p.isActive = true ORDER BY p.module ASC, p.resource ASC")
    List<Permission> findWritePermissions();
    
    /**
     * Find module permissions grouped
     */
    @Query("SELECT p FROM Permission p WHERE p.module = :module AND p.isActive = true " +
           "ORDER BY p.resource ASC, p.action ASC")
    List<Permission> findModulePermissions(@Param("module") String module);
    
    /**
     * Find resource permissions (all actions for a resource)
     */
    @Query("SELECT p FROM Permission p WHERE p.resource = :resource AND p.isActive = true " +
           "ORDER BY p.action ASC")
    List<Permission> findResourcePermissions(@Param("resource") String resource);
    
    /**
     * Find permissions by parent permission
     */
    @Query("SELECT p FROM Permission p WHERE p.parentPermissionId = :parentId " +
           "AND p.isActive = true ORDER BY p.permissionName ASC")
    List<Permission> findByParentPermissionId(@Param("parentId") Long parentId);
    
    /**
     * Find root permissions (no parent)
     */
    @Query("SELECT p FROM Permission p WHERE p.parentPermissionId IS NULL " +
           "AND p.isActive = true ORDER BY p.module ASC, p.permissionName ASC")
    List<Permission> findRootPermissions();
    
    /**
     * Find recent permissions
     */
    @Query("SELECT p FROM Permission p ORDER BY p.createdAt DESC")
    List<Permission> findRecentPermissions(Pageable pageable);
    
    /**
     * Find recently updated permissions
     */
    @Query("SELECT p FROM Permission p ORDER BY p.updatedAt DESC")
    List<Permission> findRecentlyUpdatedPermissions(Pageable pageable);
    
    /**
     * Find widely used permissions (by role count)
     */
    @Query("SELECT p FROM Permission p WHERE SIZE(p.roles) >= :minRoles " +
           "AND p.isActive = true ORDER BY SIZE(p.roles) DESC")
    List<Permission> findWidelyUsedPermissions(@Param("minRoles") Integer minRoles);
    
    /**
     * Find rarely used permissions (by role count)
     */
    @Query("SELECT p FROM Permission p WHERE SIZE(p.roles) < :maxRoles " +
           "AND SIZE(p.roles) > 0 AND p.isActive = true ORDER BY SIZE(p.roles) ASC")
    List<Permission> findRarelyUsedPermissions(@Param("maxRoles") Integer maxRoles);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count permissions by module
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE p.module = :module")
    Long countByModule(@Param("module") String module);
    
    /**
     * Count permissions by action
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE p.action = :action")
    Long countByAction(@Param("action") String action);
    
    /**
     * Count permissions by category
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE p.category = :category")
    Long countByCategory(@Param("category") String category);
    
    /**
     * Count active permissions
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE p.isActive = true")
    Long countActivePermissions();
    
    /**
     * Count inactive permissions
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE p.isActive = false")
    Long countInactivePermissions();
    
    /**
     * Count system permissions
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE p.isSystemPermission = true")
    Long countSystemPermissions();
    
    /**
     * Count custom permissions
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE p.isSystemPermission = false")
    Long countCustomPermissions();
    
    /**
     * Count permissions with roles
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE SIZE(p.roles) > 0")
    Long countPermissionsWithRoles();
    
    /**
     * Count permissions without roles
     */
    @Query("SELECT COUNT(p) FROM Permission p WHERE SIZE(p.roles) = 0")
    Long countPermissionsWithoutRoles();
    
    /**
     * Get module distribution
     */
    @Query("SELECT p.module, COUNT(p) as permissionCount FROM Permission p " +
           "WHERE p.isActive = true AND p.module IS NOT NULL " +
           "GROUP BY p.module ORDER BY permissionCount DESC")
    List<Object[]> getModuleDistribution();
    
    /**
     * Get action distribution
     */
    @Query("SELECT p.action, COUNT(p) as permissionCount FROM Permission p " +
           "WHERE p.isActive = true AND p.action IS NOT NULL " +
           "GROUP BY p.action ORDER BY permissionCount DESC")
    List<Object[]> getActionDistribution();
    
    /**
     * Get category distribution
     */
    @Query("SELECT p.category, COUNT(p) as permissionCount FROM Permission p " +
           "WHERE p.isActive = true AND p.category IS NOT NULL " +
           "GROUP BY p.category ORDER BY permissionCount DESC")
    List<Object[]> getCategoryDistribution();
    
    /**
     * Get permissions by role count
     */
    @Query("SELECT p.permissionName, SIZE(p.roles) as roleCount FROM Permission p " +
           "WHERE p.isActive = true GROUP BY p.permissionName ORDER BY roleCount DESC")
    List<Object[]> getPermissionsByRoleCount();
    
    /**
     * Get average roles per permission
     */
    @Query("SELECT AVG(SIZE(p.roles)) FROM Permission p WHERE p.isActive = true")
    Double getAverageRolesPerPermission();
    
    /**
     * Get permissions with most roles
     */
    @Query("SELECT p FROM Permission p WHERE p.isActive = true ORDER BY SIZE(p.roles) DESC")
    List<Permission> getPermissionsWithMostRoles(Pageable pageable);
    
    /**
     * Get distinct modules
     */
    @Query("SELECT DISTINCT p.module FROM Permission p WHERE p.module IS NOT NULL " +
           "AND p.isActive = true ORDER BY p.module ASC")
    List<String> getDistinctModules();
    
    /**
     * Get distinct resources
     */
    @Query("SELECT DISTINCT p.resource FROM Permission p WHERE p.resource IS NOT NULL " +
           "AND p.isActive = true ORDER BY p.resource ASC")
    List<String> getDistinctResources();
    
    /**
     * Get distinct actions
     */
    @Query("SELECT DISTINCT p.action FROM Permission p WHERE p.action IS NOT NULL " +
           "AND p.isActive = true ORDER BY p.action ASC")
    List<String> getDistinctActions();
}
