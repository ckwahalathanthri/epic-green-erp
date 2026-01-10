//package lk.epicgreen.erp.admin.controller;
//
//import lk.epicgreen.erp.common.dto.ApiResponse;
//import lk.epicgreen.erp.admin.dto.request.LoginRequest;
//import lk.epicgreen.erp.admin.dto.request.ChangePasswordRequest;
//import lk.epicgreen.erp.admin.dto.request.ResetPasswordRequest;
//import lk.epicgreen.erp.admin.dto.response.AuthenticationResponse;
//import lk.epicgreen.erp.admin.service.AuthenticationService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import javax.validation.constraints.*;
//import java.util.Map;
//import javax.validation.constraints.*;
//
///**
// * Authentication Controller
// * REST controller for authentication and authorization operations
// *
// * @author Epic Green Development Team
// * @version 1.0
// */
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//@Slf4j
//@CrossOrigin(origins = "*", maxAge = 3600)
//public class AuthenticationController {
//
//    private final AuthenticationService authenticationService;
//
//    // ===================================================================
//    // AUTHENTICATION OPERATIONS
//    // ===================================================================
//
//    /**
//     * User login endpoint
//     * @param request Login credentials (username, password)
//     * @return JWT access token and refresh token
//     */
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginRequest request) {
//        log.info("Login attempt for username: {}", request.getUsername());
//        AuthenticationResponse response = authenticationService.login(request);
//        log.info("Login successful for username: {}", request.getUsername());
//        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
//    }
//
//    /**
//     * User logout endpoint
//     * @param token JWT access token
//     */
//    @PostMapping("/logout")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
//        log.info("Logout requested");
//        authenticationService.logout(token);
//        log.info("Logout successful");
//        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
//    }
//
//    /**
//     * Refresh access token endpoint
//     * @param refreshToken JWT refresh token
//     * @return New JWT access token
//     */
//    @PostMapping("/refresh-token")
//    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(@RequestParam String refreshToken) {
//        log.info("Token refresh requested");
//        AuthenticationResponse response = authenticationService.refreshToken(refreshToken);
//        log.info("Token refreshed successfully");
//        return ResponseEntity.ok(ApiResponse.success(response, "Token refreshed successfully"));
//    }
//
//    /**
//     * Validate token endpoint
//     * @param token JWT access token
//     * @return Token validation result
//     */
//    @GetMapping("/validate-token")
//    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestParam String token) {
//        boolean isValid = authenticationService.validateToken(token);
//        return ResponseEntity.ok(ApiResponse.success(isValid, "Token validation completed"));
//    }
//
//    /**
//     * Get current user info endpoint
//     * @return Current authenticated user information
//     */
//    @GetMapping("/me")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser() {
//        Map<String, Object> userInfo = authenticationService.getCurrentUser();
//        return ResponseEntity.ok(ApiResponse.success(userInfo, "Current user retrieved successfully"));
//    }
//
//    // ===================================================================
//    // PASSWORD MANAGEMENT
//    // ===================================================================
//
//    /**
//     * Change password endpoint
//     * @param request Change password request (old password, new password)
//     */
//    @PostMapping("/change-password")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
//        log.info("Password change requested for current user");
//        authenticationService.changePassword(request);
//        log.info("Password changed successfully");
//        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
//    }
//
//    /**
//     * Reset password endpoint (Admin only)
//     * @param request Reset password request (user ID, new password)
//     */
//    @PostMapping("/reset-password")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
//        log.info("Password reset requested for user: {}", request.getUserId());
//        authenticationService.resetPassword(request);
//        log.info("Password reset successfully for user: {}", request.getUserId());
//        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
//    }
//
//    /**
//     * Forgot password endpoint - Send reset link
//     * @param email User email address
//     */
//    @PostMapping("/forgot-password")
//    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email) {
//        log.info("Password reset requested for email: {}", email);
//        authenticationService.forgotPassword(email);
//        log.info("Password reset link sent to email: {}", email);
//        return ResponseEntity.ok(ApiResponse.success(null, "Password reset link sent to your email"));
//    }
//
//    /**
//     * Reset password with token endpoint
//     * @param token Reset token from email
//     * @param newPassword New password
//     */
//    @PostMapping("/reset-password-with-token")
//    public ResponseEntity<ApiResponse<Void>> resetPasswordWithToken(
//        @RequestParam String token,
//        @RequestParam String newPassword
//    ) {
//        log.info("Password reset with token requested");
//        authenticationService.resetPasswordWithToken(token, newPassword);
//        log.info("Password reset with token successful");
//        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
//    }
//
//    // ===================================================================
//    // PERMISSION CHECK
//    // ===================================================================
//
//    /**
//     * Check if current user has specific permission
//     * @param permissionName Permission name to check
//     * @return true if user has permission, false otherwise
//     */
//    @GetMapping("/has-permission")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<Boolean>> hasPermission(@RequestParam String permissionName) {
//        boolean hasPermission = authenticationService.hasPermission(permissionName);
//        return ResponseEntity.ok(ApiResponse.success(hasPermission, "Permission check completed"));
//    }
//
//    /**
//     * Check if current user has specific role
//     * @param roleName Role name to check
//     * @return true if user has role, false otherwise
//     */
//    @GetMapping("/has-role")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<Boolean>> hasRole(@RequestParam String roleName) {
//        boolean hasRole = authenticationService.hasRole(roleName);
//        return ResponseEntity.ok(ApiResponse.success(hasRole, "Role check completed"));
//    }
//
//    /**
//     * Get current user's roles
//     * @return List of role names
//     */
//    @GetMapping("/my-roles")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<java.util.List<String>>> getMyRoles() {
//        java.util.List<String> roles = authenticationService.getCurrentUserRoles();
//        return ResponseEntity.ok(ApiResponse.success(roles, "User roles retrieved successfully"));
//    }
//
//    /**
//     * Get current user's permissions
//     * @return List of permission names
//     */
//    @GetMapping("/my-permissions")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<java.util.List<String>>> getMyPermissions() {
//        java.util.List<String> permissions = authenticationService.getCurrentUserPermissions();
//        return ResponseEntity.ok(ApiResponse.success(permissions, "User permissions retrieved successfully"));
//    }
//
//    // ===================================================================
//    // SESSION MANAGEMENT
//    // ===================================================================
//
//    /**
//     * Get active sessions for current user
//     * @return List of active sessions
//     */
//    @GetMapping("/active-sessions")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getActiveSessions() {
//        java.util.List<Map<String, Object>> sessions = authenticationService.getActiveSessions();
//        return ResponseEntity.ok(ApiResponse.success(sessions, "Active sessions retrieved successfully"));
//    }
//
//    /**
//     * Invalidate specific session
//     * @param sessionId Session ID to invalidate
//     */
//    @DeleteMapping("/sessions/{sessionId}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<Void>> invalidateSession(@PathVariable String sessionId) {
//        log.info("Session invalidation requested: {}", sessionId);
//        authenticationService.invalidateSession(sessionId);
//        log.info("Session invalidated successfully: {}", sessionId);
//        return ResponseEntity.ok(ApiResponse.success(null, "Session invalidated successfully"));
//    }
//
//    /**
//     * Invalidate all sessions except current
//     */
//    @DeleteMapping("/sessions")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<ApiResponse<Void>> invalidateAllOtherSessions() {
//        log.info("Invalidate all other sessions requested");
//        authenticationService.invalidateAllOtherSessions();
//        log.info("All other sessions invalidated successfully");
//        return ResponseEntity.ok(ApiResponse.success(null, "All other sessions invalidated successfully"));
//    }
//}
