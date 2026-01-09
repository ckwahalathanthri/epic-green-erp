package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity (CORRECTED)
 * Matches actual database schema: status (ENUM), first_name, last_name, mobile_number, employee_code
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by employee code
     */
    Optional<User> findByEmployeeCode(String employeeCode);
    
    /**
     * Find user by mobile number
     */
    Optional<User> findByMobileNumber(String mobileNumber);
    
    /**
     * Find all active users (status = 'ACTIVE')
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();
    
    /**
     * Find all active users with pagination
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    Page<User> findActiveUsers(Pageable pageable);
    
    /**
     * Find users by status
     */
    List<User> findByStatus(String status);
    
    /**
     * Find users by status with pagination
     */
    Page<User> findByStatus(String status, Pageable pageable);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if employee code exists
     */
    boolean existsByEmployeeCode(String employeeCode);
    
    /**
     * Check if mobile number exists
     */
    boolean existsByMobileNumber(String mobileNumber);
    
    /**
     * Check if username exists excluding specific user ID
     */
    boolean existsByUsernameAndIdNot(String username, Long id);
    
    /**
     * Check if email exists excluding specific user ID
     */
    boolean existsByEmailAndIdNot(String email, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search users by username containing (case-insensitive)
     */
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    
    /**
     * Search users by first name or last name containing (case-insensitive)
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> findByNameContaining(@Param("name") String name, Pageable pageable);
    
    /**
     * Search users by email containing (case-insensitive)
     */
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    
    /**
     * Search active users by username or name
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND " +
           "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchActiveUsers(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search users by multiple criteria
     */
    @Query("SELECT u FROM User u WHERE " +
           "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
           "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:status IS NULL OR u.status = :status)")
    Page<User> searchUsers(
            @Param("username") String username,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("status") String status,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    long countActiveUsers();
    
    /**
     * Count users by status
     */
    long countByStatus(String status);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find users created between dates
     */
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find users who logged in after specific date
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :date")
    List<User> findUsersLoggedInAfter(@Param("date") LocalDateTime date);
    
    /**
     * Find users who never logged in
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt IS NULL")
    List<User> findUsersNeverLoggedIn();
    
    /**
     * Find users with specific role
     */
    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.role.id = :roleId")
    List<User> findUsersByRoleId(@Param("roleId") Long roleId);
    
    /**
     * Find users with specific role code
     */
    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.role.roleCode = :roleCode")
    List<User> findUsersByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * Find users with multiple roles
     */
    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.role.id IN :roleIds " +
           "GROUP BY u.id HAVING COUNT(DISTINCT ur.role.id) = :roleCount")
    List<User> findUsersWithAllRoles(@Param("roleIds") List<Long> roleIds, @Param("roleCount") long roleCount);
    
    /**
     * Get user statistics by status
     */
    @Query("SELECT u.status, COUNT(u) as userCount FROM User u GROUP BY u.status")
    List<Object[]> getUserStatisticsByStatus();
    
    /**
     * Find users by role and status
     */
    @Query("SELECT u FROM User u JOIN u.userRoles ur " +
           "WHERE ur.role.roleCode = :roleCode AND u.status = :status")
    List<User> findByRoleCodeAndStatus(@Param("roleCode") String roleCode, @Param("status") String status);
    
    /**
     * Find users without any roles
     */
    @Query("SELECT u FROM User u WHERE u.userRoles IS EMPTY")
    List<User> findUsersWithoutRoles();
    
    /**
     * Find top N recently created users
     */
    List<User> findTop10ByOrderByCreatedAtDesc();
    
    /**
     * Find top N recently logged in users
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt IS NOT NULL ORDER BY u.lastLoginAt DESC")
    List<User> findRecentlyLoggedInUsers(Pageable pageable);
    
    /**
     * Find users with failed login attempts above threshold
     */
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= :threshold")
    List<User> findUsersWithFailedLoginAttempts(@Param("threshold") int threshold);
    
    /**
     * Find users who need password change (password older than X days)
     */
    @Query("SELECT u FROM User u WHERE u.passwordChangedAt < :date OR u.passwordChangedAt IS NULL")
    List<User> findUsersNeedingPasswordChange(@Param("date") LocalDateTime date);

    /**
     * Find users by status who are not soft-deleted
     */
    List<User> findByStatusAndDeletedAtIsNull(String status);

    /**
     * Find users by status who are not soft-deleted (paginated)
     */
    Page<User> findByStatusAndDeletedAtIsNull(String status, Pageable pageable);

    /**
     * Find all users who are not soft-deleted (paginated)
     */
    Page<User> findByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.role.id = :roleId AND u.deletedAt IS NULL")
    List<User> findByRolesId(@Param("roleId") Long roleId);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);
}
