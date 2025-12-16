package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.User;
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
 * User Repository
 * Repository for user data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Check if user exists by username
     */
    boolean existsByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Find user by employee code
     */
    Optional<User> findByEmployeeCode(String employeeCode);
    
    /**
     * Check if user exists by employee code
     */
    boolean existsByEmployeeCode(String employeeCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find users by department
     */
    List<User> findByDepartment(String department);
    
    /**
     * Find users by position
     */
    List<User> findByPosition(String position);
    
    /**
     * Find users by is active
     */
    List<User> findByIsActive(Boolean isActive);
    
    /**
     * Find users by is locked
     */
    List<User> findByIsLocked(Boolean isLocked);
    
    /**
     * Find users by email verified
     */
    List<User> findByEmailVerified(Boolean emailVerified);
    
    /**
     * Find users by role ID
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = :roleId")
    List<User> findByRoleId(@Param("roleId") Long roleId);
    
    /**
     * Find users by role name
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find users by created at between dates
     */
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find users by last login between dates
     */
    List<User> findByLastLoginBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find users by department and is active
     */
    List<User> findByDepartmentAndIsActive(String department, Boolean isActive);
    
    /**
     * Find users by position and is active
     */
    List<User> findByPositionAndIsActive(String position, Boolean isActive);
    
    /**
     * Find users by is active and is locked
     */
    List<User> findByIsActiveAndIsLocked(Boolean isActive, Boolean isLocked);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search users
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.position) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active users
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.firstName ASC, u.lastName ASC")
    List<User> findActiveUsers();
    
    /**
     * Find active users with pagination
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.firstName ASC, u.lastName ASC")
    Page<User> findActiveUsers(Pageable pageable);
    
    /**
     * Find inactive users
     */
    @Query("SELECT u FROM User u WHERE u.isActive = false ORDER BY u.firstName ASC, u.lastName ASC")
    List<User> findInactiveUsers();
    
    /**
     * Find locked users
     */
    @Query("SELECT u FROM User u WHERE u.isLocked = true ORDER BY u.lockedAt DESC")
    List<User> findLockedUsers();
    
    /**
     * Find users with verified email
     */
    @Query("SELECT u FROM User u WHERE u.emailVerified = true AND u.isActive = true " +
           "ORDER BY u.firstName ASC, u.lastName ASC")
    List<User> findUsersWithVerifiedEmail();
    
    /**
     * Find users with unverified email
     */
    @Query("SELECT u FROM User u WHERE u.emailVerified = false AND u.isActive = true " +
           "ORDER BY u.createdAt DESC")
    List<User> findUsersWithUnverifiedEmail();
    
    /**
     * Find users who never logged in
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin IS NULL AND u.isActive = true " +
           "ORDER BY u.createdAt DESC")
    List<User> findUsersWhoNeverLoggedIn();
    
    /**
     * Find users by last login after date
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin > :afterDate AND u.isActive = true " +
           "ORDER BY u.lastLogin DESC")
    List<User> findActiveUsersSince(@Param("afterDate") LocalDateTime afterDate);
    
    /**
     * Find users by last login before date (inactive users)
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin < :beforeDate AND u.isActive = true " +
           "ORDER BY u.lastLogin ASC")
    List<User> findInactiveUsersSince(@Param("beforeDate") LocalDateTime beforeDate);
    
    /**
     * Find users with failed login attempts
     */
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts > 0 AND u.isActive = true " +
           "ORDER BY u.failedLoginAttempts DESC")
    List<User> findUsersWithFailedLoginAttempts();
    
    /**
     * Find users exceeding failed login threshold
     */
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= :threshold AND u.isActive = true " +
           "ORDER BY u.failedLoginAttempts DESC")
    List<User> findUsersExceedingFailedLoginThreshold(@Param("threshold") Integer threshold);
    
    /**
     * Find recent users
     */
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findRecentUsers(Pageable pageable);
    
    /**
     * Find recently updated users
     */
    @Query("SELECT u FROM User u ORDER BY u.updatedAt DESC")
    List<User> findRecentlyUpdatedUsers(Pageable pageable);
    
    /**
     * Find recently logged in users
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin IS NOT NULL ORDER BY u.lastLogin DESC")
    List<User> findRecentlyLoggedInUsers(Pageable pageable);
    
    /**
     * Find users by department with role
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE u.department = :department " +
           "AND r.roleName = :roleName AND u.isActive = true")
    List<User> findByDepartmentAndRole(@Param("department") String department, 
                                       @Param("roleName") String roleName);
    
    /**
     * Find users with multiple roles
     */
    @Query("SELECT u FROM User u WHERE SIZE(u.roles) > 1 AND u.isActive = true")
    List<User> findUsersWithMultipleRoles();
    
    /**
     * Find users without roles
     */
    @Query("SELECT u FROM User u WHERE SIZE(u.roles) = 0")
    List<User> findUsersWithoutRoles();
    
    /**
     * Find users by permission
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r JOIN r.permissions p " +
           "WHERE p.permissionName = :permissionName AND u.isActive = true")
    List<User> findUsersByPermission(@Param("permissionName") String permissionName);
    
    /**
     * Find users requiring attention (locked, unverified, or many failed attempts)
     */
    @Query("SELECT u FROM User u WHERE " +
           "(u.isLocked = true OR u.emailVerified = false OR u.failedLoginAttempts >= :threshold) " +
           "AND u.isActive = true ORDER BY u.failedLoginAttempts DESC")
    List<User> findUsersRequiringAttention(@Param("threshold") Integer threshold);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count users by department
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.department = :department")
    Long countByDepartment(@Param("department") String department);
    
    /**
     * Count users by position
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.position = :position")
    Long countByPosition(@Param("position") String position);
    
    /**
     * Count users by role
     */
    @Query("SELECT COUNT(DISTINCT u) FROM User u JOIN u.roles r WHERE r.roleName = :roleName")
    Long countByRoleName(@Param("roleName") String roleName);
    
    /**
     * Count active users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    Long countActiveUsers();
    
    /**
     * Count inactive users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = false")
    Long countInactiveUsers();
    
    /**
     * Count locked users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isLocked = true")
    Long countLockedUsers();
    
    /**
     * Count users with verified email
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = true")
    Long countUsersWithVerifiedEmail();
    
    /**
     * Count users with unverified email
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = false")
    Long countUsersWithUnverifiedEmail();
    
    /**
     * Count users who never logged in
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLogin IS NULL")
    Long countUsersWhoNeverLoggedIn();
    
    /**
     * Count users with failed login attempts
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.failedLoginAttempts > 0")
    Long countUsersWithFailedLoginAttempts();
    
    /**
     * Get department distribution
     */
    @Query("SELECT u.department, COUNT(u) as userCount FROM User u " +
           "WHERE u.isActive = true AND u.department IS NOT NULL " +
           "GROUP BY u.department ORDER BY userCount DESC")
    List<Object[]> getDepartmentDistribution();
    
    /**
     * Get position distribution
     */
    @Query("SELECT u.position, COUNT(u) as userCount FROM User u " +
           "WHERE u.isActive = true AND u.position IS NOT NULL " +
           "GROUP BY u.position ORDER BY userCount DESC")
    List<Object[]> getPositionDistribution();
    
    /**
     * Get role distribution
     */
    @Query("SELECT r.roleName, COUNT(DISTINCT u) as userCount FROM User u " +
           "JOIN u.roles r WHERE u.isActive = true " +
           "GROUP BY r.roleName ORDER BY userCount DESC")
    List<Object[]> getRoleDistribution();
    
    /**
     * Get users by registration month
     */
    @Query("SELECT YEAR(u.createdAt) as year, MONTH(u.createdAt) as month, COUNT(u) as userCount " +
           "FROM User u GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
           "ORDER BY year DESC, month DESC")
    List<Object[]> getUsersByRegistrationMonth();
    
    /**
     * Get login activity summary
     */
    @Query("SELECT DATE(u.lastLogin) as loginDate, COUNT(u) as userCount " +
           "FROM User u WHERE u.lastLogin IS NOT NULL " +
           "GROUP BY DATE(u.lastLogin) ORDER BY loginDate DESC")
    List<Object[]> getLoginActivitySummary(Pageable pageable);
    
    /**
     * Get most active users (by login count)
     */
    @Query("SELECT u FROM User u WHERE u.loginCount > 0 AND u.isActive = true " +
           "ORDER BY u.loginCount DESC")
    List<User> getMostActiveUsers(Pageable pageable);
}
