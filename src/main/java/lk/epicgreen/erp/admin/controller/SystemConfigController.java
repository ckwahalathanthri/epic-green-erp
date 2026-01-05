package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.request.SystemConfigRequest;
import lk.epicgreen.erp.admin.dto.response.SystemConfigResponse;
import lk.epicgreen.erp.admin.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * System Config Controller
 * REST controller for system configuration management
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/system-config")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemConfigController {
    
    private final SystemConfigService systemConfigService;
    
    // Create System Configuration
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<SystemConfigResponse>> createSystemConfig(
        @Valid @RequestBody SystemConfigRequest request
    ) {
        log.info("Creating system configuration: {}", request.getConfigKey());
        SystemConfigResponse response = systemConfigService.createSystemConfig(request);
        return ResponseEntity.ok(ApiResponse.success(response, "System configuration created successfully"));
    }
    
    // Update System Configuration
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<SystemConfigResponse>> updateSystemConfig(
        @PathVariable Long id,
        @Valid @RequestBody SystemConfigRequest request
    ) {
        log.info("Updating system configuration: {}", id);
        SystemConfigResponse response = systemConfigService.updateSystemConfig(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "System configuration updated successfully"));
    }
    
    // Update Configuration Value
    @PatchMapping("/value")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateConfigValue(
        @RequestParam String configKey,
        @RequestParam String configValue
    ) {
        log.info("Updating configuration value for key: {}", configKey);
        systemConfigService.updateConfigValue(configKey, configValue);
        return ResponseEntity.ok(ApiResponse.success(null, "Configuration value updated successfully"));
    }
    
    // Delete System Configuration
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSystemConfig(@PathVariable Long id) {
        log.info("Deleting system configuration: {}", id);
        systemConfigService.deleteSystemConfig(id);
        return ResponseEntity.ok(ApiResponse.success(null, "System configuration deleted successfully"));
    }
    
    // Get System Configuration by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SystemConfigResponse>> getSystemConfigById(@PathVariable Long id) {
        SystemConfigResponse response = systemConfigService.getSystemConfigById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "System configuration retrieved successfully"));
    }
    
    // Get System Configuration by Key
    @GetMapping("/key/{configKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<SystemConfigResponse>> getSystemConfigByKey(@PathVariable String configKey) {
        SystemConfigResponse response = systemConfigService.getSystemConfigByKey(configKey);
        return ResponseEntity.ok(ApiResponse.success(response, "System configuration retrieved successfully"));
    }
    
    // Get Configuration Value
    @GetMapping("/value/{configKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<String>> getConfigValue(@PathVariable String configKey) {
        String value = systemConfigService.getConfigValue(configKey);
        return ResponseEntity.ok(ApiResponse.success(value, "Configuration value retrieved successfully"));
    }
    
    // Get Configuration Value with Default
    @GetMapping("/value/{configKey}/default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<String>> getConfigValueWithDefault(
        @PathVariable String configKey,
        @RequestParam String defaultValue
    ) {
        String value = systemConfigService.getConfigValue(configKey, defaultValue);
        return ResponseEntity.ok(ApiResponse.success(value, "Configuration value retrieved successfully"));
    }
    
    // Get All System Configurations
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SystemConfigResponse>>> getAllSystemConfigs() {
        List<SystemConfigResponse> response = systemConfigService.getAllSystemConfigs();
        return ResponseEntity.ok(ApiResponse.success(response, "System configurations retrieved successfully"));
    }
    
    // Get Configurations by Group
    @GetMapping("/group/{configGroup}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SystemConfigResponse>>> getConfigsByGroup(@PathVariable String configGroup) {
        List<SystemConfigResponse> response = systemConfigService.getConfigsByGroup(configGroup);
        return ResponseEntity.ok(ApiResponse.success(response, "Configurations retrieved successfully"));
    }
    
    // Get Available Configuration Groups
    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<String>>> getAvailableConfigGroups() {
        List<String> groups = systemConfigService.getAvailableConfigGroups();
        return ResponseEntity.ok(ApiResponse.success(groups, "Configuration groups retrieved successfully"));
    }
    
    // Check if Configuration Key Exists
    @GetMapping("/exists/{configKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> configKeyExists(@PathVariable String configKey) {
        boolean exists = systemConfigService.configKeyExists(configKey);
        return ResponseEntity.ok(ApiResponse.success(exists, "Configuration key existence checked"));
    }
}
