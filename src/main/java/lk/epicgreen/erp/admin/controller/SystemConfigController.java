package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.SystemConfigRequest;
import lk.epicgreen.erp.admin.entity.SystemConfig;
import lk.epicgreen.erp.admin.service.SystemConfigService;
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
 * System Configuration Controller
 * REST controller for system configuration operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemConfigController {
    
    private final SystemConfigService systemConfigService;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SystemConfig>> createConfig(@Valid @RequestBody SystemConfigRequest request) {
        log.info("Creating system config: {}", request.getConfigKey());
        SystemConfig created = systemConfigService.createConfig(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Configuration created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SystemConfig>> updateConfig(
        @PathVariable Long id,
        @Valid @RequestBody SystemConfigRequest request
    ) {
        log.info("Updating system config: {}", id);
        SystemConfig updated = systemConfigService.updateConfig(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Configuration updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteConfig(@PathVariable Long id) {
        log.info("Deleting system config: {}", id);
        systemConfigService.deleteConfig(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Configuration deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SystemConfig>> getConfigById(@PathVariable Long id) {
        SystemConfig config = systemConfigService.getConfigById(id);
        return ResponseEntity.ok(ApiResponse.success(config, "Configuration retrieved successfully"));
    }
    
    @GetMapping("/key/{configKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<SystemConfig>> getConfigByKey(@PathVariable String configKey) {
        SystemConfig config = systemConfigService.getConfigByKey(configKey);
        return ResponseEntity.ok(ApiResponse.success(config, "Configuration retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<SystemConfig>>> getAllConfigs(Pageable pageable) {
        Page<SystemConfig> configs = systemConfigService.getAllConfigs(pageable);
        return ResponseEntity.ok(ApiResponse.success(configs, "Configurations retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> getAllConfigsList() {
        List<SystemConfig> configs = systemConfigService.getAllConfigs();
        return ResponseEntity.ok(ApiResponse.success(configs, "Configurations list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<SystemConfig>>> searchConfigs(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<SystemConfig> configs = systemConfigService.searchConfigs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(configs, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // GROUP OPERATIONS
    // ===================================================================
    
    @GetMapping("/group/{configGroup}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> getConfigsByGroup(@PathVariable String configGroup) {
        List<SystemConfig> configs = systemConfigService.getConfigsByGroup(configGroup);
        return ResponseEntity.ok(ApiResponse.success(configs, "Configurations by group retrieved successfully"));
    }
    
    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<String>>> getDistinctGroups() {
        List<String> groups = systemConfigService.getDistinctGroups();
        return ResponseEntity.ok(ApiResponse.success(groups, "Distinct groups retrieved successfully"));
    }
    
    @GetMapping("/groups/map")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, List<SystemConfig>>>> getConfigsByGroupMap() {
        Map<String, List<SystemConfig>> configMap = systemConfigService.getConfigsByGroupMap();
        return ResponseEntity.ok(ApiResponse.success(configMap, "Configurations by group map retrieved successfully"));
    }
    
    // ===================================================================
    // DATA TYPE OPERATIONS
    // ===================================================================
    
    @GetMapping("/type/{dataType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> getConfigsByDataType(@PathVariable String dataType) {
        List<SystemConfig> configs = systemConfigService.getConfigsByDataType(dataType);
        return ResponseEntity.ok(ApiResponse.success(configs, "Configurations by data type retrieved successfully"));
    }
    
    @GetMapping("/encrypted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> getEncryptedConfigs() {
        List<SystemConfig> configs = systemConfigService.getEncryptedConfigs();
        return ResponseEntity.ok(ApiResponse.success(configs, "Encrypted configurations retrieved successfully"));
    }
    
    @GetMapping("/unencrypted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> getUnencryptedConfigs() {
        List<SystemConfig> configs = systemConfigService.getUnencryptedConfigs();
        return ResponseEntity.ok(ApiResponse.success(configs, "Unencrypted configurations retrieved successfully"));
    }
    
    // ===================================================================
    // VALUE OPERATIONS
    // ===================================================================
    
    @GetMapping("/value/{configKey}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<String>> getConfigValue(@PathVariable String configKey) {
        String value = systemConfigService.getConfigValue(configKey);
        return ResponseEntity.ok(ApiResponse.success(value, "Configuration value retrieved successfully"));
    }
    
    @PutMapping("/value/{configKey}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> setConfigValue(
        @PathVariable String configKey,
        @RequestParam String configValue
    ) {
        log.info("Setting config value: {} = {}", configKey, configValue);
        systemConfigService.setConfigValue(configKey, configValue);
        return ResponseEntity.ok(ApiResponse.success(null, "Configuration value updated successfully"));
    }
    
    @GetMapping("/value/{configKey}/default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<String>> getConfigValueOrDefault(
        @PathVariable String configKey,
        @RequestParam String defaultValue
    ) {
        String value = systemConfigService.getConfigValueOrDefault(configKey, defaultValue);
        return ResponseEntity.ok(ApiResponse.success(value, "Configuration value retrieved successfully"));
    }
    
    // ===================================================================
    // BULK OPERATIONS
    // ===================================================================
    
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> bulkCreateConfigs(
        @Valid @RequestBody List<SystemConfigRequest> requests
    ) {
        log.info("Bulk creating {} configs", requests.size());
        List<SystemConfig> created = systemConfigService.bulkCreateConfigs(requests);
        return ResponseEntity.ok(ApiResponse.success(created, "Configurations created successfully"));
    }
    
    @PutMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SystemConfig>>> bulkUpdateConfigs(
        @Valid @RequestBody Map<Long, SystemConfigRequest> updates
    ) {
        log.info("Bulk updating {} configs", updates.size());
        List<SystemConfig> updated = systemConfigService.bulkUpdateConfigs(updates);
        return ResponseEntity.ok(ApiResponse.success(updated, "Configurations updated successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> bulkDeleteConfigs(@RequestBody List<Long> ids) {
        log.info("Bulk deleting {} configs", ids.size());
        systemConfigService.bulkDeleteConfigs(ids);
        return ResponseEntity.ok(ApiResponse.success(null, "Configurations deleted successfully"));
    }
    
    // ===================================================================
    // IMPORT/EXPORT
    // ===================================================================
    
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, String>>> exportConfigs() {
        Map<String, String> configs = systemConfigService.exportConfigs();
        return ResponseEntity.ok(ApiResponse.success(configs, "Configurations exported successfully"));
    }
    
    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> importConfigs(@RequestBody Map<String, String> configs) {
        log.info("Importing {} configs", configs.size());
        int imported = systemConfigService.importConfigs(configs);
        return ResponseEntity.ok(ApiResponse.success(imported, imported + " configurations imported successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getConfigStatistics() {
        Map<String, Object> statistics = systemConfigService.getConfigStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Configuration statistics retrieved successfully"));
    }
    
    @GetMapping("/distribution/group")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getGroupDistribution() {
        List<Map<String, Object>> distribution = systemConfigService.getGroupDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Group distribution retrieved successfully"));
    }
    
    @GetMapping("/distribution/type")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTypeDistribution() {
        List<Map<String, Object>> distribution = systemConfigService.getTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Type distribution retrieved successfully"));
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @GetMapping("/validate/key/{configKey}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> isConfigKeyAvailable(@PathVariable String configKey) {
        boolean available = systemConfigService.isConfigKeyAvailable(configKey);
        return ResponseEntity.ok(ApiResponse.success(available, "Config key availability checked"));
    }
}
