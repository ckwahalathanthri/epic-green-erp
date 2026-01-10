package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.request.PermissionCreateRequest;
import lk.epicgreen.erp.admin.dto.response.PermissionResponse;
import lk.epicgreen.erp.admin.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Permission Controller
 * REST controller for permission management
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
    
    // Create Permission
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
        @Valid @RequestBody PermissionCreateRequest request
    ) {
        log.info("Creating permission: {}", request.getPermissionCode());
        PermissionResponse response = permissionService.createPermission(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Permission created successfully"));
    }
    
    // Get Permission by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermissionById(@PathVariable Long id) {
        PermissionResponse response = permissionService.getPermissionById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Permission retrieved successfully"));
    }
    
    // Get Permission by Code
    @GetMapping("/code/{permissionCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermissionByCode(@PathVariable String permissionCode) {
        PermissionResponse response = permissionService.getPermissionByCode(permissionCode);
        return ResponseEntity.ok(ApiResponse.success(response, "Permission retrieved successfully"));
    }
    
    // Get All Permissions
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        List<PermissionResponse> response = permissionService.getAllPermissions();
        return ResponseEntity.ok(ApiResponse.success(response, "Permissions retrieved successfully"));
    }
    
    // Get Permissions by Module
    @GetMapping("/module/{module}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissionsByModule(@PathVariable String module) {
        List<PermissionResponse> response = permissionService.getPermissionsByModule(module);
        return ResponseEntity.ok(ApiResponse.success(response, "Permissions retrieved successfully"));
    }
    
    // Get Available Modules
    @GetMapping("/modules")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<String>>> getAvailableModules() {
        List<String> modules = permissionService.getAvailableModules();
        return ResponseEntity.ok(ApiResponse.success(modules, "Available modules retrieved successfully"));
    }
}
