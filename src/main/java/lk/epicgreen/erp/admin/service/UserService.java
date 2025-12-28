package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.request.UserCreateRequest;
import lk.epicgreen.erp.admin.dto.request.UserUpdateRequest;
import lk.epicgreen.erp.admin.dto.request.ChangePasswordRequest;
import lk.epicgreen.erp.admin.dto.response.UserResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for User entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface UserService {

    /**
     * Create new user
     */
    UserResponse createUser(UserCreateRequest request);

    /**
     * Update existing user
     */
    UserResponse updateUser(Long id, UserUpdateRequest request);

    /**
     * Change user password
     */
    void changePassword(Long id, ChangePasswordRequest request);

    /**
     * Reset user password (admin function)
     */
    void resetPassword(Long id, String newPassword);

    /**
     * Activate user
     */
    void activateUser(Long id);

    /**
     * Deactivate user
     */
    void deactivateUser(Long id);

    /**
     * Suspend user
     */
    void suspendUser(Long id);

    /**
     * Soft delete user
     */
    void deleteUser(Long id);

    /**
     * Record login attempt
     */
    void recordLoginAttempt(String username, boolean successful);

    /**
     * Assign role to user
     */
    void assignRole(Long userId, Long roleId);

    /**
     * Remove role from user
     */
    void removeRole(Long userId, Long roleId);

    /**
     * Get user by ID
     */
    UserResponse getUserById(Long id);

    /**
     * Get user by username
     */
    UserResponse getUserByUsername(String username);

    /**
     * Get user by email
     */
    UserResponse getUserByEmail(String email);

    /**
     * Get all active users
     */
    List<UserResponse> getAllActiveUsers();

    /**
     * Get all users with pagination
     */
    PageResponse<UserResponse> getAllUsers(Pageable pageable);

    /**
     * Search users
     */
    PageResponse<UserResponse> searchUsers(String keyword, Pageable pageable);

    /**
     * Get users by status
     */
    PageResponse<UserResponse> getUsersByStatus(String status, Pageable pageable);

    /**
     * Get users by role
     */
    List<UserResponse> getUsersByRole(Long roleId);
}
