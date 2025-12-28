package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.PermissionRequest;
import lk.epicgreen.erp.admin.entity.Permission;
import lk.epicgreen.erp.admin.service.PermissionService;
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
 * Permission Controller
 * REST controller for permission operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/permissions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PermissionController {
    
    private final PermissionService permissionService;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Permission>> createPermission(@Valid @RequestBody PermissionRequest request) {
        log.info("Creating permission: {}", request.getPermissionName());
        Permission created = permissionService.createPermission(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Permission created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Permission>> updatePermission(
        @PathVariable Long id,
        @Valid @RequestBody PermissionRequest request
    ) {
        log.info("Updating permission: {}", id);
        Permission updated = permissionService.updatePermission(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Permission updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable Long id) {
        log.info("Deleting permission: {}", id);
        permissionService.deletePermission(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Permission deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Permission>> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(ApiResponse.success(permission, "Permission retrieved successfully"));
    }
    
    @GetMapping("/name/{permissionName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Permission>> getPermissionByName(@PathVariable String permissionName) {
        Permission permission = permissionService.getPermissionByName(permissionName);
        return ResponseEntity.ok(ApiResponse.success(permission, "Permission retrieved successfully"));
    }
    
    @GetMapping("/code/{permissionCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Permission>> getPermissionByCode(@PathVariable String permissionCode) {
        Permission permission = permissionService.getPermissionByCode(permissionCode);
        return ResponseEntity.ok(ApiResponse.success(permission, "Permission retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<Permission>>> getAllPermissions(Pageable pageable) {
        Page<Permission> permissions = permissionService.getAllPermissions(pageable);
        return ResponseEntity.ok(ApiResponse.success(permissions, "Permissions retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Permission>>> getAllPermissionsList() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(ApiResponse.success(permissions, "Permissions list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<Permission>>> searchPermissions(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Permission> permissions = permissionService.searchPermissions(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(permissions, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Permission>> activatePermission(@PathVariable Long id) {
        log.info("Activating permission: {}", id);
        Permission activated = permissionService.activatePermission(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Permission activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Permission>> deactivatePermission(@PathVariable Long id) {
        log.info("Deactivating permission: {}", id);
        Permission deactivated = permissionService.deactivatePermission(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Permission deactivated successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Permission>>> getActivePermissions() {
        List<Permission> permissions = permissionService.getActivePermissions();
        return ResponseEntity.ok(ApiResponse.success(permissions, "Active permissions retrieved successfully"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Permission>>> getInactivePermissions() {
        List<Permission> permissions = permissionService.getInactivePermissions();
        return ResponseEntity.ok(ApiResponse.success(permissions, "Inactive permissions retrieved successfully"));
    }
    
    @GetMapping("/module/{module}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Permission>>> getPermissionsByModule(@PathVariable String module) {
        List<Permission> permissions = permissionService.getPermissionsByModule(module);
        return ResponseEntity.ok(ApiResponse.success(permissions, "Permissions by module retrieved successfully"));
    }
    
    @GetMapping("/modules")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<String>>> getDistinctModules() {
        List<String> modules = permissionService.getDistinctModules();
        return ResponseEntity.ok(ApiResponse.success(modules, "Distinct modules retrieved successfully"));
    }
    
    @GetMapping("/unused")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Permission>>> getUnusedPermissions() {
        List<Permission> permissions = permissionService.getUnusedPermissions();
        return ResponseEntity.ok(ApiResponse.success(permissions, "Unused permissions retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Permission>>> getRecentPermissions(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Permission> permissions = permissionService.getRecentPermissions(limit);
        return ResponseEntity.ok(ApiResponse.success(permissions, "Recent permissions retrieved successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPermissionStatistics() {
        Map<String, Object> statistics = permissionService.getPermissionStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Permission statistics retrieved successfully"));
    }
    
    @GetMapping("/distribution/module")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getModuleDistribution() {
        List<Map<String, Object>> distribution = permissionService.getModuleDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Module distribution retrieved successfully"));
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @GetMapping("/validate/name/{permissionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> isPermissionNameAvailable(@PathVariable String permissionName) {
        boolean available = permissionService.isPermissionNameAvailable(permissionName);
        return ResponseEntity.ok(ApiResponse.success(available, "Permission name availability checked"));
    }
    
    @GetMapping("/validate/code/{permissionCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> isPermissionCodeAvailable(@PathVariable String permissionCode) {
        boolean available = permissionService.isPermissionCodeAvailable(permissionCode);
        return ResponseEntity.ok(ApiResponse.success(available, "Permission code availability checked"));
    }
}
