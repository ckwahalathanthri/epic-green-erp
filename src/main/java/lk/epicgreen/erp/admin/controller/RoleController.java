package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.request.RoleCreateRequest;
import lk.epicgreen.erp.admin.dto.request.RoleUpdateRequest;
import lk.epicgreen.erp.admin.dto.response.RoleResponse;
import lk.epicgreen.erp.admin.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * Role Controller
 * REST controller for role management
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
    
    // Create Role
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleCreateRequest request) {
        log.info("Creating role: {}", request.getRoleCode());
        RoleResponse response = roleService.createRole(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Role created successfully"));
    }
    
    // Update Role
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
        @PathVariable Long id,
        @Valid @RequestBody RoleUpdateRequest request
    ) {
        log.info("Updating role: {}", id);
        RoleResponse response = roleService.updateRole(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Role updated successfully"));
    }
    
    // Delete Role
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        log.info("Deleting role: {}", id);
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Role deleted successfully"));
    }
    
    // Assign Permission to Role
    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignPermission(
        @PathVariable Long roleId,
        @PathVariable Long permissionId
    ) {
        log.info("Assigning permission {} to role {}", permissionId, roleId);
        roleService.assignPermission(roleId, permissionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Permission assigned successfully"));
    }
    
    // Remove Permission from Role
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removePermission(
        @PathVariable Long roleId,
        @PathVariable Long permissionId
    ) {
        log.info("Removing permission {} from role {}", permissionId, roleId);
        roleService.removePermission(roleId, permissionId);
        return ResponseEntity.ok(ApiResponse.success(null, "Permission removed successfully"));
    }
    
    // Assign Permissions in Bulk
    @PostMapping("/{roleId}/permissions/bulk")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignPermissions(
        @PathVariable Long roleId,
        @RequestBody List<Long> permissionIds
    ) {
        log.info("Assigning {} permissions to role {}", permissionIds.size(), roleId);
        roleService.assignPermissions(roleId, permissionIds);
        return ResponseEntity.ok(ApiResponse.success(null, "Permissions assigned successfully"));
    }
    
    // Get Role by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable Long id) {
        RoleResponse response = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Role retrieved successfully"));
    }
    
    // Get Role by Code
    @GetMapping("/code/{roleCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleByCode(@PathVariable String roleCode) {
        RoleResponse response = roleService.getRoleByCode(roleCode);
        return ResponseEntity.ok(ApiResponse.success(response, "Role retrieved successfully"));
    }
    
    // Get All Roles
    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> response = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.success(response, "Roles retrieved successfully"));
    }
    
    // Get Non-System Roles
    @GetMapping("/non-system")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getNonSystemRoles() {
        List<RoleResponse> response = roleService.getNonSystemRoles();
        return ResponseEntity.ok(ApiResponse.success(response, "Non-system roles retrieved successfully"));
    }
    
    // Get System Roles
    @GetMapping("/system")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getSystemRoles() {
        List<RoleResponse> response = roleService.getSystemRoles();
        return ResponseEntity.ok(ApiResponse.success(response, "System roles retrieved successfully"));
    }
}
