package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.Role;
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
 * Role Repository
 * Repository for role data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find role by name
     */
    Optional<Role> findByRoleName(String roleName);
    
    /**
     * Check if role exists by name
     */
    boolean existsByRoleName(String roleName);
    
    /**
     * Find role by code
     */
    Optional<Role> findByRoleCode(String roleCode);
    
    /**
     * Check if role exists by code
     */
    boolean existsByRoleCode(String roleCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find roles by is active
     */
    List<Role> findByIsActive(Boolean isActive);
    
    /**
     * Find roles by is system role
     */
    List<Role> findByIsSystemRole(Boolean isSystemRole);
    
    /**
     * Find roles by category
     */
    List<Role> findByCategory(String category);
    
    /**
     * Find roles by level
     */
    List<Role> findByLevel(Integer level);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find roles by created at between dates
     */
    List<Role> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find roles by category and is active
     */
    List<Role> findByCategoryAndIsActive(String category, Boolean isActive);
    
    /**
     * Find roles by level and is active
     */
    List<Role> findByLevelAndIsActive(Integer level, Boolean isActive);
    
    /**
     * Find roles by is active and is system role
     */
    List<Role> findByIsActiveAndIsSystemRole(Boolean isActive, Boolean isSystemRole);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search roles
     */
    @Query("SELECT r FROM Role r WHERE " +
           "LOWER(r.roleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.roleCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Role> searchRoles(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active roles
     */
    @Query("SELECT r FROM Role r WHERE r.isActive = true ORDER BY r.level ASC, r.roleName ASC")
    List<Role> findActiveRoles();
    
    /**
     * Find active roles with pagination
     */
    @Query("SELECT r FROM Role r WHERE r.isActive = true ORDER BY r.level ASC, r.roleName ASC")
    Page<Role> findActiveRoles(Pageable pageable);
    
    /**
     * Find inactive roles
     */
    @Query("SELECT r FROM Role r WHERE r.isActive = false ORDER BY r.roleName ASC")
    List<Role> findInactiveRoles();
    
    /**
     * Find system roles
     */
    @Query("SELECT r FROM Role r WHERE r.isSystemRole = true ORDER BY r.level ASC, r.roleName ASC")
    List<Role> findSystemRoles();
    
    /**
     * Find custom roles (non-system)
     */
    @Query("SELECT r FROM Role r WHERE r.isSystemRole = false AND r.isActive = true " +
           "ORDER BY r.level ASC, r.roleName ASC")
    List<Role> findCustomRoles();
    
    /**
     * Find roles by permission ID
     */
    @Query("SELECT DISTINCT r FROM Role r JOIN r.permissions p WHERE p.id = :permissionId")
    List<Role> findByPermissionId(@Param("permissionId") Long permissionId);
    
    /**
     * Find roles by permission name
     */
    @Query("SELECT DISTINCT r FROM Role r JOIN r.permissions p WHERE p.permissionName = :permissionName")
    List<Role> findByPermissionName(@Param("permissionName") String permissionName);
    
    /**
     * Find roles with permission
     */
    @Query("SELECT r FROM Role r WHERE SIZE(r.permissions) > 0 AND r.isActive = true " +
           "ORDER BY r.level ASC")
    List<Role> findRolesWithPermissions();
    
    /**
     * Find roles without permissions
     */
    @Query("SELECT r FROM Role r WHERE SIZE(r.permissions) = 0 ORDER BY r.roleName ASC")
    List<Role> findRolesWithoutPermissions();
    
    /**
     * Find roles by user count
     */
    @Query("SELECT r FROM Role r WHERE SIZE(r.users) >= :minUsers AND r.isActive = true " +
           "ORDER BY SIZE(r.users) DESC")
    List<Role> findRolesByUserCount(@Param("minUsers") Integer minUsers);
    
    /**
     * Find roles with no users
     */
    @Query("SELECT r FROM Role r WHERE SIZE(r.users) = 0 ORDER BY r.createdAt DESC")
    List<Role> findRolesWithNoUsers();
    
    /**
     * Find roles by level range
     */
    @Query("SELECT r FROM Role r WHERE r.level BETWEEN :minLevel AND :maxLevel " +
           "AND r.isActive = true ORDER BY r.level ASC, r.roleName ASC")
    List<Role> findByLevelRange(@Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel);
    
    /**
     * Find hierarchical roles (by parent)
     */
    @Query("SELECT r FROM Role r WHERE r.parentRoleId = :parentId AND r.isActive = true " +
           "ORDER BY r.level ASC, r.roleName ASC")
    List<Role> findByParentRoleId(@Param("parentId") Long parentId);
    
    /**
     * Find root roles (no parent)
     */
    @Query("SELECT r FROM Role r WHERE r.parentRoleId IS NULL AND r.isActive = true " +
           "ORDER BY r.level ASC, r.roleName ASC")
    List<Role> findRootRoles();
    
    /**
     * Find recent roles
     */
    @Query("SELECT r FROM Role r ORDER BY r.createdAt DESC")
    List<Role> findRecentRoles(Pageable pageable);
    
    /**
     * Find recently updated roles
     */
    @Query("SELECT r FROM Role r ORDER BY r.updatedAt DESC")
    List<Role> findRecentlyUpdatedRoles(Pageable pageable);
    
    /**
     * Find administrative roles (level <= 2)
     */
    @Query("SELECT r FROM Role r WHERE r.level <= 2 AND r.isActive = true " +
           "ORDER BY r.level ASC, r.roleName ASC")
    List<Role> findAdministrativeRoles();
    
    /**
     * Find operational roles (level > 2)
     */
    @Query("SELECT r FROM Role r WHERE r.level > 2 AND r.isActive = true " +
           "ORDER BY r.level ASC, r.roleName ASC")
    List<Role> findOperationalRoles();
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count roles by category
     */
    @Query("SELECT COUNT(r) FROM Role r WHERE r.category = :category")
    Long countByCategory(@Param("category") String category);
    
    /**
     * Count roles by level
     */
    @Query("SELECT COUNT(r) FROM Role r WHERE r.level = :level")
    Long countByLevel(@Param("level") Integer level);
    
    /**
     * Count active roles
     */
    @Query("SELECT COUNT(r) FROM Role r WHERE r.isActive = true")
    Long countActiveRoles();
    
    /**
     * Count inactive roles
     */
    @Query("SELECT COUNT(r) FROM Role r WHERE r.isActive = false")
    Long countInactiveRoles();
    
    /**
     * Count system roles
     */
    @Query("SELECT COUNT(r) FROM Role r WHERE r.isSystemRole = true")
    Long countSystemRoles();
    
    /**
     * Count custom roles
     */
    @Query("SELECT COUNT(r) FROM Role r WHERE r.isSystemRole = false")
    Long countCustomRoles();
    
    /**
     * Count roles with permissions
     */
    @Query("SELECT COUNT(r) FROM Role r WHERE SIZE(r.permissions) > 0")
    Long countRolesWithPermissions();
    
    /**
     * Count roles without permissions
     */
    @Query("SELECT COUNT(r) FROM Role r WHERE SIZE(r.permissions) = 0")
    Long countRolesWithoutPermissions();
    
    /**
     * Get category distribution
     */
    @Query("SELECT r.category, COUNT(r) as roleCount FROM Role r " +
           "WHERE r.isActive = true AND r.category IS NOT NULL " +
           "GROUP BY r.category ORDER BY roleCount DESC")
    List<Object[]> getCategoryDistribution();
    
    /**
     * Get level distribution
     */
    @Query("SELECT r.level, COUNT(r) as roleCount FROM Role r " +
           "WHERE r.isActive = true GROUP BY r.level ORDER BY r.level ASC")
    List<Object[]> getLevelDistribution();
    
    /**
     * Get roles by user count
     */
    @Query("SELECT r.roleName, SIZE(r.users) as userCount FROM Role r " +
           "WHERE r.isActive = true GROUP BY r.roleName ORDER BY userCount DESC")
    List<Object[]> getRolesByUserCount();
    
    /**
     * Get roles by permission count
     */
    @Query("SELECT r.roleName, SIZE(r.permissions) as permissionCount FROM Role r " +
           "WHERE r.isActive = true GROUP BY r.roleName ORDER BY permissionCount DESC")
    List<Object[]> getRolesByPermissionCount();
    
    /**
     * Get average permissions per role
     */
    @Query("SELECT AVG(SIZE(r.permissions)) FROM Role r WHERE r.isActive = true")
    Double getAveragePermissionsPerRole();
    
    /**
     * Get average users per role
     */
    @Query("SELECT AVG(SIZE(r.users)) FROM Role r WHERE r.isActive = true")
    Double getAverageUsersPerRole();
    
    /**
     * Get roles with most users
     */
    @Query("SELECT r FROM Role r WHERE r.isActive = true ORDER BY SIZE(r.users) DESC")
    List<Role> getRolesWithMostUsers(Pageable pageable);
    
    /**
     * Get roles with most permissions
     */
    @Query("SELECT r FROM Role r WHERE r.isActive = true ORDER BY SIZE(r.permissions) DESC")
    List<Role> getRolesWithMostPermissions(Pageable pageable);
}
