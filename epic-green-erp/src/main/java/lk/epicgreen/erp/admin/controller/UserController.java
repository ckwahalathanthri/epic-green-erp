package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.UserRequest;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * User Controller
 * REST controller for user operations
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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody UserRequest request) {
        User created = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success(created, "User created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<User>> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UserRequest request
    ) {
        User updated = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "User updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
    
    @GetMapping("/employee-code/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<User>> getUserByEmployeeCode(@PathVariable String employeeCode) {
        User user = userService.getUserByEmployeeCode(employeeCode);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<User>>> searchUsers(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<User> users = userService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(users, "Search results retrieved"));
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<User>> authenticateUser(
        @RequestParam String username,
        @RequestParam String password
    ) {
        User user = userService.authenticateUser(username, password);
        return ResponseEntity.ok(ApiResponse.success(user, "Authentication successful"));
    }
    
    @PostMapping("/{id}/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Void>> changePassword(
        @PathVariable Long id,
        @RequestParam String oldPassword,
        @RequestParam String newPassword
    ) {
        userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }
    
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
        @PathVariable Long id,
        @RequestParam String newPassword
    ) {
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
    }
    
    @PostMapping("/{id}/lock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> lockUser(
        @PathVariable Long id,
        @RequestParam String lockReason
    ) {
        userService.lockUser(id, lockReason);
        return ResponseEntity.ok(ApiResponse.success(null, "User locked successfully"));
    }
    
    @PostMapping("/{id}/unlock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> unlockUser(@PathVariable Long id) {
        userService.unlockUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User unlocked successfully"));
    }
    
    @PostMapping("/{id}/send-verification-email")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> sendVerificationEmail(@PathVariable Long id) {
        userService.sendVerificationEmail(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Verification email sent"));
    }
    
    @PostMapping("/{id}/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
        @PathVariable Long id,
        @RequestParam String token
    ) {
        userService.verifyEmail(id, token);
        return ResponseEntity.ok(ApiResponse.success(null, "Email verified successfully"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<User>> activateUser(@PathVariable Long id) {
        User activated = userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "User activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<User>> deactivateUser(@PathVariable Long id) {
        User deactivated = userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "User deactivated"));
    }
    
    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> assignRole(
        @PathVariable Long userId,
        @PathVariable Long roleId
    ) {
        userService.assignRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success(null, "Role assigned successfully"));
    }
    
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> assignRoles(
        @PathVariable Long userId,
        @RequestBody List<Long> roleIds
    ) {
        userService.assignRoles(userId, roleIds);
        return ResponseEntity.ok(ApiResponse.success(null, "Roles assigned successfully"));
    }
    
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> removeRole(
        @PathVariable Long userId,
        @PathVariable Long roleId
    ) {
        userService.removeRole(userId, roleId);
        return ResponseEntity.ok(ApiResponse.success(null, "Role removed successfully"));
    }
    
    @DeleteMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeAllRoles(@PathVariable Long userId) {
        userService.removeAllRoles(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "All roles removed successfully"));
    }
    
    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateUserRoles(
        @PathVariable Long userId,
        @RequestBody List<Long> roleIds
    ) {
        userService.updateUserRoles(userId, roleIds);
        return ResponseEntity.ok(ApiResponse.success(null, "User roles updated successfully"));
    }
    
    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<String>>> getUserRoles(@PathVariable Long userId) {
        List<String> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(ApiResponse.success(roles, "User roles retrieved"));
    }
    
    @GetMapping("/{userId}/permissions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<String>>> getUserPermissions(@PathVariable Long userId) {
        List<String> permissions = userService.getUserPermissions(userId);
        return ResponseEntity.ok(ApiResponse.success(permissions, "User permissions retrieved"));
    }
    
    @GetMapping("/{userId}/has-role/{roleName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Boolean>> hasRole(
        @PathVariable Long userId,
        @PathVariable String roleName
    ) {
        boolean hasRole = userService.hasRole(userId, roleName);
        return ResponseEntity.ok(ApiResponse.success(hasRole, "Role check completed"));
    }
    
    @GetMapping("/{userId}/has-permission/{permissionName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Boolean>> hasPermission(
        @PathVariable Long userId,
        @PathVariable String permissionName
    ) {
        boolean hasPermission = userService.hasPermission(userId, permissionName);
        return ResponseEntity.ok(ApiResponse.success(hasPermission, "Permission check completed"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<User>>> getActiveUsers(Pageable pageable) {
        Page<User> users = userService.getActiveUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users, "Active users retrieved"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getInactiveUsers() {
        List<User> users = userService.getInactiveUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Inactive users retrieved"));
    }
    
    @GetMapping("/locked")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getLockedUsers() {
        List<User> users = userService.getLockedUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Locked users retrieved"));
    }
    
    @GetMapping("/verified-email")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersWithVerifiedEmail() {
        List<User> users = userService.getUsersWithVerifiedEmail();
        return ResponseEntity.ok(ApiResponse.success(users, "Users with verified email retrieved"));
    }
    
    @GetMapping("/unverified-email")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersWithUnverifiedEmail() {
        List<User> users = userService.getUsersWithUnverifiedEmail();
        return ResponseEntity.ok(ApiResponse.success(users, "Users with unverified email retrieved"));
    }
    
    @GetMapping("/never-logged-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersWhoNeverLoggedIn() {
        List<User> users = userService.getUsersWhoNeverLoggedIn();
        return ResponseEntity.ok(ApiResponse.success(users, "Users who never logged in retrieved"));
    }
    
    @GetMapping("/failed-login-attempts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersWithFailedLoginAttempts() {
        List<User> users = userService.getUsersWithFailedLoginAttempts();
        return ResponseEntity.ok(ApiResponse.success(users, "Users with failed login attempts retrieved"));
    }
    
    @GetMapping("/requiring-attention")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersRequiringAttention(
        @RequestParam(defaultValue = "3") Integer threshold
    ) {
        List<User> users = userService.getUsersRequiringAttention(threshold);
        return ResponseEntity.ok(ApiResponse.success(users, "Users requiring attention retrieved"));
    }
    
    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByDepartment(@PathVariable String department) {
        List<User> users = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success(users, "Users by department retrieved"));
    }
    
    @GetMapping("/position/{position}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByPosition(@PathVariable String position) {
        List<User> users = userService.getUsersByPosition(position);
        return ResponseEntity.ok(ApiResponse.success(users, "Users by position retrieved"));
    }
    
    @GetMapping("/role/{roleName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable String roleName) {
        List<User> users = userService.getUsersByRole(roleName);
        return ResponseEntity.ok(ApiResponse.success(users, "Users by role retrieved"));
    }
    
    @GetMapping("/permission/{permissionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByPermission(@PathVariable String permissionName) {
        List<User> users = userService.getUsersByPermission(permissionName);
        return ResponseEntity.ok(ApiResponse.success(users, "Users by permission retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getRecentUsers(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<User> users = userService.getRecentUsers(limit);
        return ResponseEntity.ok(ApiResponse.success(users, "Recent users retrieved"));
    }
    
    @GetMapping("/most-active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<User>>> getMostActiveUsers(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<User> users = userService.getMostActiveUsers(limit);
        return ResponseEntity.ok(ApiResponse.success(users, "Most active users retrieved"));
    }
    
    @PutMapping("/{userId}/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<User>> updateProfile(
        @PathVariable Long userId,
        @RequestBody Map<String, Object> profileData
    ) {
        User updated = userService.updateProfile(userId, profileData);
        return ResponseEntity.ok(ApiResponse.success(updated, "Profile updated successfully"));
    }
    
    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameAvailability(@RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(ApiResponse.success(available, "Username availability checked"));
    }
    
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailAvailability(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        return ResponseEntity.ok(ApiResponse.success(available, "Email availability checked"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = userService.getUserStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = userService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
