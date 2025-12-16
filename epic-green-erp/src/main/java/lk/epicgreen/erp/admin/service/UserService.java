package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.UserRequest;
import lk.epicgreen.erp.admin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * User Service Interface
 * Service for user operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface UserService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    User createUser(UserRequest request);
    User updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User getUserByEmployeeCode(String employeeCode);
    List<User> getAllUsers();
    Page<User> getAllUsers(Pageable pageable);
    Page<User> searchUsers(String keyword, Pageable pageable);
    
    // ===================================================================
    // AUTHENTICATION & SECURITY
    // ===================================================================
    
    User authenticateUser(String username, String password);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void resetPassword(Long userId, String newPassword);
    void updateLastLogin(Long userId);
    void incrementLoginCount(Long userId);
    void incrementFailedLoginAttempts(Long userId);
    void resetFailedLoginAttempts(Long userId);
    void lockUser(Long userId, String lockReason);
    void unlockUser(Long userId);
    boolean verifyPassword(Long userId, String password);
    
    // ===================================================================
    // EMAIL VERIFICATION
    // ===================================================================
    
    void sendVerificationEmail(Long userId);
    void verifyEmail(Long userId, String verificationToken);
    void resendVerificationEmail(Long userId);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    User activateUser(Long userId);
    User deactivateUser(Long userId);
    
    // ===================================================================
    // ROLE MANAGEMENT
    // ===================================================================
    
    void assignRole(Long userId, Long roleId);
    void assignRoles(Long userId, List<Long> roleIds);
    void removeRole(Long userId, Long roleId);
    void removeAllRoles(Long userId);
    void updateUserRoles(Long userId, List<Long> roleIds);
    List<String> getUserRoles(Long userId);
    List<String> getUserPermissions(Long userId);
    boolean hasRole(Long userId, String roleName);
    boolean hasPermission(Long userId, String permissionName);
    boolean hasAnyRole(Long userId, List<String> roleNames);
    boolean hasAllRoles(Long userId, List<String> roleNames);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<User> getActiveUsers();
    Page<User> getActiveUsers(Pageable pageable);
    List<User> getInactiveUsers();
    List<User> getLockedUsers();
    List<User> getUsersWithVerifiedEmail();
    List<User> getUsersWithUnverifiedEmail();
    List<User> getUsersWhoNeverLoggedIn();
    List<User> getActiveUsersSince(LocalDateTime afterDate);
    List<User> getInactiveUsersSince(LocalDateTime beforeDate);
    List<User> getUsersWithFailedLoginAttempts();
    List<User> getUsersExceedingFailedLoginThreshold(Integer threshold);
    List<User> getUsersByDepartment(String department);
    List<User> getUsersByPosition(String position);
    List<User> getUsersByRole(String roleName);
    List<User> getUsersByPermission(String permissionName);
    List<User> getUsersByDepartmentAndRole(String department, String roleName);
    List<User> getUsersWithMultipleRoles();
    List<User> getUsersWithoutRoles();
    List<User> getUsersRequiringAttention(Integer failedLoginThreshold);
    List<User> getRecentUsers(int limit);
    List<User> getRecentlyUpdatedUsers(int limit);
    List<User> getRecentlyLoggedInUsers(int limit);
    List<User> getMostActiveUsers(int limit);
    
    // ===================================================================
    // PROFILE OPERATIONS
    // ===================================================================
    
    User updateProfile(Long userId, Map<String, Object> profileData);
    User updateContactInfo(Long userId, String phone, String mobile, String address);
    User updateProfilePicture(Long userId, String profilePictureUrl);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateUser(User user);
    boolean isUsernameAvailable(String username);
    boolean isEmailAvailable(String email);
    boolean isEmployeeCodeAvailable(String employeeCode);
    boolean canDeleteUser(Long userId);
    boolean canLockUser(Long userId);
    boolean isPasswordStrong(String password);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<User> createBulkUsers(List<UserRequest> requests);
    int activateBulkUsers(List<Long> userIds);
    int deactivateBulkUsers(List<Long> userIds);
    int lockBulkUsers(List<Long> userIds, String lockReason);
    int unlockBulkUsers(List<Long> userIds);
    int deleteBulkUsers(List<Long> userIds);
    int assignRoleToBulkUsers(List<Long> userIds, Long roleId);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getUserStatistics();
    List<Map<String, Object>> getDepartmentDistribution();
    List<Map<String, Object>> getPositionDistribution();
    List<Map<String, Object>> getRoleDistribution();
    List<Map<String, Object>> getUsersByRegistrationMonth();
    List<Map<String, Object>> getLoginActivitySummary(int limit);
    Long getTotalUsers();
    Long getActiveUsersCount();
    Long getInactiveUsersCount();
    Long getLockedUsersCount();
    Long getVerifiedUsersCount();
    Long getUnverifiedUsersCount();
    Map<String, Object> getDashboardStatistics();
}
