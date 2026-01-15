package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.admin.dto.request.UserCreateRequest;
import lk.epicgreen.erp.admin.dto.request.UserUpdateRequest;
import lk.epicgreen.erp.admin.dto.request.ChangePasswordRequest;
import lk.epicgreen.erp.admin.dto.response.UserResponse;
import lk.epicgreen.erp.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * User Controller
 * REST controller for user management
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    private final UserService userService;
    
    // Create User
    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("Creating user: {}", request.getUsername());
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success(response, "User created successfully"));
    }
    
    // Update User
    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UserUpdateRequest request
    ) {
        log.info("Updating user: {}", id);
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "User updated successfully"));
    }
    
    // Change Password
    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Void>> changePassword(
        @PathVariable Long id,
        @Valid @RequestBody ChangePasswordRequest request
    ) {
        log.info("Changing password for user: {}", id);
        userService.changePassword(id, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }
    
    // Reset Password (Admin Function)
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
        @PathVariable Long id,
        @RequestParam String newPassword
    ) {
        log.info("Resetting password for user: {}", id);
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
    }
    
    // Activate User
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        log.info("Activating user: {}", id);
        userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User activated successfully"));
    }
    
    // Deactivate User
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        log.info("Deactivating user: {}", id);
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deactivated successfully"));
    }
    
    // Suspend User
    @PutMapping("/{id}/suspend")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> suspendUser(@PathVariable Long id) {
        log.info("Suspending user: {}", id);
        userService.suspendUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User suspended successfully"));
    }
    
    // Delete User (Soft Delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("Deleting user: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }
    
    // Record Login Attempt
    @PostMapping("/login-attempt")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> recordLoginAttempt(
        @RequestParam String username,
        @RequestParam boolean successful
    ) {
        log.info("Recording login attempt for user: {}", username);
        userService.recordLoginAttempt(username, successful);
        return ResponseEntity.ok(ApiResponse.success(null, "Login attempt recorded successfully"));
    }
    
    // Assign Role to User
    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignRole(
        @PathVariable Long userId,
        @PathVariable Long roleId
    ) {
        log.info("Assigning role {} to user {}", roleId, userId);
        userService.assignRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success(null, "Role assigned successfully"));
    }
    
    // Remove Role from User
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeRole(
        @PathVariable Long userId,
        @PathVariable Long roleId
    ) {
        log.info("Removing role {} from user {}", roleId, userId);
        userService.removeRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success(null, "Role removed successfully"));
    }
    
    // Get User by ID
    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "User retrieved successfully"));
    }
    
    // Get User by Username
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(response, "User retrieved successfully"));
    }
    
    // Get User by Email
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(response, "User retrieved successfully"));
    }
    
    // Get All Active Users
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllActiveUsers() {
        List<UserResponse> response = userService.getAllActiveUsers();
        return ResponseEntity.ok(ApiResponse.success(response, "Active users retrieved successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        List<UserResponse> response=userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(response,"All users retrieved successfully"));
    }
    
    // Get All Users with Pagination
    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(Pageable pageable) {
        PageResponse<UserResponse> response = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Users retrieved successfully"));
    }
    
    // Search Users
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> searchUsers(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        PageResponse<UserResponse> response = userService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Search results retrieved successfully"));
    }
    
    // Get Users by Status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsersByStatus(
        @PathVariable String status,
        Pageable pageable
    ) {
        PageResponse<UserResponse> response = userService.getUsersByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "Users retrieved successfully"));
    }
    
    // Get Users by Role
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable Long roleId) {
        List<UserResponse> response = userService.getUsersByRole(roleId);
        return ResponseEntity.ok(ApiResponse.success(response, "Users retrieved successfully"));
    }
}
