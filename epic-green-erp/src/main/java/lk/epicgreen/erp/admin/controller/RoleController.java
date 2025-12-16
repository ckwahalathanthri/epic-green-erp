package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.RoleRequest;
import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Role Controller
 * REST controller for role operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController {
    
    private final RoleService roleService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> createRole(@Valid @RequestBody RoleRequest request) {
        Role created = roleService.createRole(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Role created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> updateRole(
        @PathVariable Long id,
        @Valid @RequestBody RoleRequest request
    ) {
        Role updated = roleService.updateRole(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Role updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Role deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }
    
    @GetMapping("/name/{roleName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Role>> getRoleByName(@PathVariable String roleName) {
        Role role = roleService.getRoleByName(roleName);
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }
    
    @GetMapping("/code/{roleCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Role>> getRoleByCode(@PathVariable String roleCode) {
        Role role = roleService.getRoleByCode(roleCode);
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<Role>>> getAllRoles(Pageable pageable) {
        Page<Role> roles = roleService.getAllRoles(pageable);
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<Role>>> searchRoles(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Role> roles = roleService.searchRoles(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(roles, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> activateRole(@PathVariable Long id) {
        Role activated = roleService.activateRole(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Role activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> deactivateRole(@PathVariable Long id) {
        Role deactivated = roleService.deactivateRole(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Role deactivated"));
    }
    
    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignPermission(
        @PathVariable Long roleId,
        @PathVariable Long permissionId
    ) {
        roleService.assignPermission(roleId, permissionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Permission assigned successfully"));
    }
    
    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignPermissions(
        @PathVariable Long roleId,
        @RequestBody List<Long> permissionIds
    ) {
        roleService.assignPermissions(roleId, permissionIds);
        return ResponseEntity.ok(ApiResponse.success(null, "Permissions assigned successfully"));
    }
    
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removePermission(
        @PathVariable Long roleId,
        @PathVariable Long permissionId
    ) {
        roleService.removePermission(roleId, permissionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Permission removed successfully"));
    }
    
    @DeleteMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeAllPermissions(@PathVariable Long roleId) {
        roleService.removeAllPermissions(roleId);
        return ResponseEntity.ok(ApiResponse.success(null, "All permissions removed successfully"));
    }
    
    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateRolePermissions(
        @PathVariable Long roleId,
        @RequestBody List<Long> permissionIds
    ) {
        roleService.updateRolePermissions(roleId, permissionIds);
        return ResponseEntity.ok(ApiResponse.success(null, "Role permissions updated successfully"));
    }
    
    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<String>>> getRolePermissions(@PathVariable Long roleId) {
        List<String> permissions = roleService.getRolePermissions(roleId);
        return ResponseEntity.ok(ApiResponse.success(permissions, "Role permissions retrieved"));
    }
    
    @GetMapping("/{roleId}/has-permission/{permissionName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> hasPermission(
        @PathVariable Long roleId,
        @PathVariable String permissionName
    ) {
        boolean hasPermission = roleService.hasPermission(roleId, permissionName);
        return ResponseEntity.ok(ApiResponse.success(hasPermission, "Permission check completed"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<Role>>> getActiveRoles(Pageable pageable) {
        Page<Role> roles = roleService.getActiveRoles(pageable);
        return ResponseEntity.ok(ApiResponse.success(roles, "Active roles retrieved"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Role>>> getInactiveRoles() {
        List<Role> roles = roleService.getInactiveRoles();
        return ResponseEntity.ok(ApiResponse.success(roles, "Inactive roles retrieved"));
    }
    
    @GetMapping("/system")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getSystemRoles() {
        List<Role> roles = roleService.getSystemRoles();
        return ResponseEntity.ok(ApiResponse.success(roles, "System roles retrieved"));
    }
    
    @GetMapping("/custom")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getCustomRoles() {
        List<Role> roles = roleService.getCustomRoles();
        return ResponseEntity.ok(ApiResponse.success(roles, "Custom roles retrieved"));
    }
    
    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesByCategory(@PathVariable String category) {
        List<Role> roles = roleService.getRolesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles by category retrieved"));
    }
    
    @GetMapping("/level/{level}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesByLevel(@PathVariable Integer level) {
        List<Role> roles = roleService.getRolesByLevel(level);
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles by level retrieved"));
    }
    
    @GetMapping("/permission/{permissionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesByPermission(@PathVariable String permissionName) {
        List<Role> roles = roleService.getRolesByPermission(permissionName);
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles by permission retrieved"));
    }
    
    @GetMapping("/with-permissions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesWithPermissions() {
        List<Role> roles = roleService.getRolesWithPermissions();
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles with permissions retrieved"));
    }
    
    @GetMapping("/without-permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesWithoutPermissions() {
        List<Role> roles = roleService.getRolesWithoutPermissions();
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles without permissions retrieved"));
    }
    
    @GetMapping("/no-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesWithNoUsers() {
        List<Role> roles = roleService.getRolesWithNoUsers();
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles with no users retrieved"));
    }
    
    @GetMapping("/root")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getRootRoles() {
        List<Role> roles = roleService.getRootRoles();
        return ResponseEntity.ok(ApiResponse.success(roles, "Root roles retrieved"));
    }
    
    @GetMapping("/{parentId}/children")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getChildRoles(@PathVariable Long parentId) {
        List<Role> roles = roleService.getChildRoles(parentId);
        return ResponseEntity.ok(ApiResponse.success(roles, "Child roles retrieved"));
    }
    
    @GetMapping("/administrative")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getAdministrativeRoles() {
        List<Role> roles = roleService.getAdministrativeRoles();
        return ResponseEntity.ok(ApiResponse.success(roles, "Administrative roles retrieved"));
    }
    
    @GetMapping("/operational")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getOperationalRoles() {
        List<Role> roles = roleService.getOperationalRoles();
        return ResponseEntity.ok(ApiResponse.success(roles, "Operational roles retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getRecentRoles(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Role> roles = roleService.getRecentRoles(limit);
        return ResponseEntity.ok(ApiResponse.success(roles, "Recent roles retrieved"));
    }
    
    @GetMapping("/most-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesWithMostUsers(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Role> roles = roleService.getRolesWithMostUsers(limit);
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles with most users retrieved"));
    }
    
    @GetMapping("/most-permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Role>>> getRolesWithMostPermissions(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Role> roles = roleService.getRolesWithMostPermissions(limit);
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles with most permissions retrieved"));
    }
    
    @PostMapping("/{roleId}/parent/{parentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> setParentRole(
        @PathVariable Long roleId,
        @PathVariable Long parentId
    ) {
        roleService.setParentRole(roleId, parentId);
        return ResponseEntity.ok(ApiResponse.success(null, "Parent role set successfully"));
    }
    
    @DeleteMapping("/{roleId}/parent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeParentRole(@PathVariable Long roleId) {
        roleService.removeParentRole(roleId);
        return ResponseEntity.ok(ApiResponse.success(null, "Parent role removed successfully"));
    }
    
    @GetMapping("/{roleId}/parent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Role>> getParentRole(@PathVariable Long roleId) {
        Role parent = roleService.getParentRole(roleId);
        return ResponseEntity.ok(ApiResponse.success(parent, "Parent role retrieved"));
    }
    
    @GetMapping("/{roleId}/hierarchy")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Role>>> getRoleHierarchy(@PathVariable Long roleId) {
        List<Role> hierarchy = roleService.getRoleHierarchy(roleId);
        return ResponseEntity.ok(ApiResponse.success(hierarchy, "Role hierarchy retrieved"));
    }
    
    @GetMapping("/check-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> checkRoleNameAvailability(@RequestParam String roleName) {
        boolean available = roleService.isRoleNameAvailable(roleName);
        return ResponseEntity.ok(ApiResponse.success(available, "Role name availability checked"));
    }
    
    @GetMapping("/check-code")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> checkRoleCodeAvailability(@RequestParam String roleCode) {
        boolean available = roleService.isRoleCodeAvailable(roleCode);
        return ResponseEntity.ok(ApiResponse.success(available, "Role code availability checked"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = roleService.getRoleStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = roleService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
