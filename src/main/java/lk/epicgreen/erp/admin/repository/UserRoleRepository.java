package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.entity.UserRole;
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
 * Repository interface for UserRole entity (CORRECTED)
 * Matches actual database schema: assigned_by is BIGINT (not User entity)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all roles for a specific user
     */
    List<UserRole> findByUser(User user);
    
    /**
     * Find all roles for a specific user by user ID
     */
    List<UserRole> findByUserId(Long userId);
    
    /**
     * Find all users for a specific role
     */
    List<UserRole> findByRole(Role role);
    
    /**
     * Find all users for a specific role by role ID
     */
    List<UserRole> findByRoleId(Long roleId);
    
    /**
     * Find specific user-role assignment
     */
    Optional<UserRole> findByUserAndRole(User user, Role role);
    
    /**
     * Find user-role by user ID and role ID
     */
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * Find assignments by user and role code
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.roleCode = :roleCode")
    Optional<UserRole> findByUserIdAndRoleCode(@Param("userId") Long userId, @Param("roleCode") String roleCode);
    
    /**
     * Find assignments assigned between dates
     */
    List<UserRole> findByAssignedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find assignments by assigned by user ID (assigned_by is BIGINT)
     */
    List<UserRole> findByAssignedBy(Long assignedBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if user-role assignment exists
     */
    boolean existsByUserAndRole(User user, Role role);
    
    /**
     * Check if user-role assignment exists by IDs
     */
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * Check if user has specific role code
     */
    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.roleCode = :roleCode")
    boolean userHasRole(@Param("userId") Long userId, @Param("roleCode") String roleCode);
    
    /**
     * Check if user has any of the specified role codes
     */
    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur " +
           "WHERE ur.user.id = :userId AND ur.role.roleCode IN :roleCodes")
    boolean userHasAnyRole(@Param("userId") Long userId, @Param("roleCodes") List<String> roleCodes);
    
    /**
     * Check if user has all of the specified role codes
     */
    @Query("SELECT COUNT(DISTINCT ur.role.roleCode) = :roleCount FROM UserRole ur " +
           "WHERE ur.user.id = :userId AND ur.role.roleCode IN :roleCodes")
    boolean userHasAllRoles(
            @Param("userId") Long userId, 
            @Param("roleCodes") List<String> roleCodes,
            @Param("roleCount") long roleCount);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count roles for a specific user
     */
    long countByUserId(Long userId);
    
    /**
     * Count users for a specific role
     */
    long countByRoleId(Long roleId);
    
    /**
     * Count total user-role assignments
     */
    long count();
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete user-role assignment
     */
    void deleteByUserAndRole(User user, Role role);
    
    /**
     * Delete user-role assignment by IDs
     */
    void deleteByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * Delete all roles for a specific user
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
    
    /**
     * Delete all users for a specific role
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.role.id = :roleId")
    void deleteAllByRoleId(@Param("roleId") Long roleId);
    
    /**
     * Delete specific role codes for a user
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.roleCode IN :roleCodes")
    void deleteByUserIdAndRoleCodes(@Param("userId") Long userId, @Param("roleCodes") List<String> roleCodes);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find users with specific role and active status
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.roleCode = :roleCode AND ur.user.status = 'ACTIVE'")
    List<UserRole> findByRoleCodeAndUserActive(@Param("roleCode") String roleCode);
    
    /**
     * Find active users with specific role
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId AND ur.user.status = 'ACTIVE'")
    List<UserRole> findActiveUsersByRoleId(@Param("roleId") Long roleId);
    
    /**
     * Find user-role assignments for active users
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.status = 'ACTIVE'")
    List<UserRole> findAllActiveAssignments();
    
    /**
     * Get role assignment statistics
     */
    @Query("SELECT " +
           "COUNT(DISTINCT ur.user.id) as totalUsers, " +
           "COUNT(DISTINCT ur.role.id) as totalRoles, " +
           "COUNT(ur) as totalAssignments " +
           "FROM UserRole ur")
    Object getAssignmentStatistics();
    
    /**
     * Get users count by role
     */
    @Query("SELECT ur.role.roleName, COUNT(ur.user) as userCount " +
           "FROM UserRole ur GROUP BY ur.role.id, ur.role.roleName " +
           "ORDER BY userCount DESC")
    List<Object[]> getUserCountByRole();
    
    /**
     * Get roles count by user
     */
    @Query("SELECT ur.user.username, COUNT(ur.role) as roleCount " +
           "FROM UserRole ur GROUP BY ur.user.id, ur.user.username " +
           "ORDER BY roleCount DESC")
    List<Object[]> getRoleCountByUser();
    
    /**
     * Find users with multiple roles
     */
    @Query("SELECT ur.user FROM UserRole ur " +
           "GROUP BY ur.user.id HAVING COUNT(ur.role) > 1")
    List<User> findUsersWithMultipleRoles();
    
    /**
     * Find inactive user assignments
     */
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.status != 'ACTIVE'")
    List<UserRole> findInactiveUserAssignments();
}
