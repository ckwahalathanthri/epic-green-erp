package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.entity.SystemConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
// Import for Management
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.*;

/**
 * SystemConfig Controller
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
    
    // Note: In a real implementation, you would inject a SystemConfigService
    // For now, using in-memory map for demonstration
    private final Map<String, Object> configStore = new HashMap<>();
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> setConfig(
        @RequestParam String key,
        @RequestParam String value,
        @RequestParam(required = false) String description
    ) {
        Map<String, Object> config = new HashMap<>();
        config.put("key", key);
        config.put("value", value);
        config.put("description", description);
        config.put("updatedAt", LocalDateTime.now());
        
        configStore.put(key, config);
        
        log.info("System config set: {} = {}", key, value);
        return ResponseEntity.ok(ApiResponse.success(config, "Configuration set successfully"));
    }
    
    @GetMapping("/{key}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Object>> getConfig(@PathVariable String key) {
        Object config = configStore.get(key);
        if (config == null) {
            return ResponseEntity.ok(ApiResponse.error("Configuration not found", null));
        }
        return ResponseEntity.ok(ApiResponse.success(config, "Configuration retrieved"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllConfigs() {
        return ResponseEntity.ok(ApiResponse.success(configStore, "All configurations retrieved"));
    }
    
    @DeleteMapping("/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteConfig(@PathVariable String key) {
        configStore.remove(key);
        log.info("System config deleted: {}", key);
        return ResponseEntity.ok(ApiResponse.success(null, "Configuration deleted successfully"));
    }
    
    // System Information Endpoints
    
    @GetMapping("/system-info")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        // Application Info
        systemInfo.put("applicationName", "Epic Green ERP");
        systemInfo.put("version", "1.0.0");
        systemInfo.put("environment", System.getProperty("spring.profiles.active", "development"));
        
        // Runtime Info
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> runtimeInfo = new HashMap<>();
        runtimeInfo.put("availableProcessors", runtime.availableProcessors());
        runtimeInfo.put("freeMemory", runtime.freeMemory());
        runtimeInfo.put("totalMemory", runtime.totalMemory());
        runtimeInfo.put("maxMemory", runtime.maxMemory());
        systemInfo.put("runtime", runtimeInfo);
        
        // System Properties
        Map<String, Object> systemProps = new HashMap<>();
        systemProps.put("javaVersion", System.getProperty("java.version"));
        systemProps.put("javaVendor", System.getProperty("java.vendor"));
        systemProps.put("osName", System.getProperty("os.name"));
        systemProps.put("osVersion", System.getProperty("os.version"));
        systemProps.put("osArch", System.getProperty("os.arch"));
        systemInfo.put("system", systemProps);
        
        return ResponseEntity.ok(ApiResponse.success(systemInfo, "System information retrieved"));
    }
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
        
        return ResponseEntity.ok(ApiResponse.success(health, "System is healthy"));
    }
    
    // Application Settings
    
    @GetMapping("/settings/general")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getGeneralSettings() {
        Map<String, Object> settings = new HashMap<>();
        
        settings.put("companyName", configStore.getOrDefault("company.name", "Epic Green"));
        settings.put("companyEmail", configStore.getOrDefault("company.email", "info@epicgreen.lk"));
        settings.put("companyPhone", configStore.getOrDefault("company.phone", "+94 11 234 5678"));
        settings.put("companyAddress", configStore.getOrDefault("company.address", "Colombo, Sri Lanka"));
        settings.put("timezone", configStore.getOrDefault("app.timezone", "Asia/Colombo"));
        settings.put("dateFormat", configStore.getOrDefault("app.dateFormat", "yyyy-MM-dd"));
        settings.put("currency", configStore.getOrDefault("app.currency", "LKR"));
        settings.put("language", configStore.getOrDefault("app.language", "en"));
        
        return ResponseEntity.ok(ApiResponse.success(settings, "General settings retrieved"));
    }
    
    @PostMapping("/settings/general")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateGeneralSettings(
        @RequestBody Map<String, String> settings
    ) {
        settings.forEach((key, value) -> {
            String configKey = "app." + key;
            Map<String, Object> config = new HashMap<>();
            config.put("key", configKey);
            config.put("value", value);
            config.put("updatedAt", LocalDateTime.now());
            configStore.put(configKey, config);
        });
        
        return ResponseEntity.ok(ApiResponse.success(null, "General settings updated"));
    }
    
    @GetMapping("/settings/security")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSecuritySettings() {
        Map<String, Object> settings = new HashMap<>();
        
        settings.put("passwordMinLength", configStore.getOrDefault("security.password.minLength", 8));
        settings.put("passwordRequireUppercase", configStore.getOrDefault("security.password.requireUppercase", true));
        settings.put("passwordRequireLowercase", configStore.getOrDefault("security.password.requireLowercase", true));
        settings.put("passwordRequireDigit", configStore.getOrDefault("security.password.requireDigit", true));
        settings.put("passwordRequireSpecialChar", configStore.getOrDefault("security.password.requireSpecialChar", true));
        settings.put("maxLoginAttempts", configStore.getOrDefault("security.maxLoginAttempts", 5));
        settings.put("sessionTimeout", configStore.getOrDefault("security.sessionTimeout", 3600));
        settings.put("enableTwoFactor", configStore.getOrDefault("security.enableTwoFactor", false));
        
        return ResponseEntity.ok(ApiResponse.success(settings, "Security settings retrieved"));
    }
    
    @PostMapping("/settings/security")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateSecuritySettings(
        @RequestBody Map<String, Object> settings
    ) {
        settings.forEach((key, value) -> {
            String configKey = "security." + key;
            Map<String, Object> config = new HashMap<>();
            config.put("key", configKey);
            config.put("value", value);
            config.put("updatedAt", LocalDateTime.now());
            configStore.put(configKey, config);
        });
        
        return ResponseEntity.ok(ApiResponse.success(null, "Security settings updated"));
    }
    
    @GetMapping("/settings/email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEmailSettings() {
        Map<String, Object> settings = new HashMap<>();
        
        settings.put("smtpHost", configStore.getOrDefault("email.smtp.host", "smtp.gmail.com"));
        settings.put("smtpPort", configStore.getOrDefault("email.smtp.port", 587));
        settings.put("smtpUsername", configStore.getOrDefault("email.smtp.username", ""));
        settings.put("smtpUseTLS", configStore.getOrDefault("email.smtp.useTLS", true));
        settings.put("fromEmail", configStore.getOrDefault("email.from", "noreply@epicgreen.lk"));
        settings.put("fromName", configStore.getOrDefault("email.fromName", "Epic Green ERP"));
        
        return ResponseEntity.ok(ApiResponse.success(settings, "Email settings retrieved"));
    }
    
    @PostMapping("/settings/email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateEmailSettings(
        @RequestBody Map<String, Object> settings
    ) {
        settings.forEach((key, value) -> {
            String configKey = "email." + key;
            Map<String, Object> config = new HashMap<>();
            config.put("key", configKey);
            config.put("value", value);
            config.put("updatedAt", LocalDateTime.now());
            configStore.put(configKey, config);
        });
        
        return ResponseEntity.ok(ApiResponse.success(null, "Email settings updated"));
    }
    
    @GetMapping("/settings/notification")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotificationSettings() {
        Map<String, Object> settings = new HashMap<>();
        
        settings.put("enableEmailNotifications", configStore.getOrDefault("notification.email.enabled", true));
        settings.put("enableSmsNotifications", configStore.getOrDefault("notification.sms.enabled", false));
        settings.put("enablePushNotifications", configStore.getOrDefault("notification.push.enabled", true));
        settings.put("notifyOnNewUser", configStore.getOrDefault("notification.onNewUser", true));
        settings.put("notifyOnNewOrder", configStore.getOrDefault("notification.onNewOrder", true));
        settings.put("notifyOnLowStock", configStore.getOrDefault("notification.onLowStock", true));
        
        return ResponseEntity.ok(ApiResponse.success(settings, "Notification settings retrieved"));
    }
    
    @PostMapping("/settings/notification")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateNotificationSettings(
        @RequestBody Map<String, Object> settings
    ) {
        settings.forEach((key, value) -> {
            String configKey = "notification." + key;
            Map<String, Object> config = new HashMap<>();
            config.put("key", configKey);
            config.put("value", value);
            config.put("updatedAt", LocalDateTime.now());
            configStore.put(configKey, config);
        });
        
        return ResponseEntity.ok(ApiResponse.success(null, "Notification settings updated"));
    }
    
    @GetMapping("/settings/business")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBusinessSettings() {
        Map<String, Object> settings = new HashMap<>();
        
        settings.put("fiscalYearStart", configStore.getOrDefault("business.fiscalYearStart", "01-01"));
        settings.put("taxRate", configStore.getOrDefault("business.taxRate", 15.0));
        settings.put("defaultPaymentTerms", configStore.getOrDefault("business.defaultPaymentTerms", "NET30"));
        settings.put("defaultCreditDays", configStore.getOrDefault("business.defaultCreditDays", 30));
        settings.put("enableMultiCurrency", configStore.getOrDefault("business.enableMultiCurrency", false));
        settings.put("lowStockThreshold", configStore.getOrDefault("business.lowStockThreshold", 10));
        
        return ResponseEntity.ok(ApiResponse.success(settings, "Business settings retrieved"));
    }
    
    @PostMapping("/settings/business")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateBusinessSettings(
        @RequestBody Map<String, Object> settings
    ) {
        settings.forEach((key, value) -> {
            String configKey = "business." + key;
            Map<String, Object> config = new HashMap<>();
            config.put("key", configKey);
            config.put("value", value);
            config.put("updatedAt", LocalDateTime.now());
            configStore.put(configKey, config);
        });
        
        return ResponseEntity.ok(ApiResponse.success(null, "Business settings updated"));
    }
    
    // Cache Management
    
    @PostMapping("/cache/clear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> clearCache() {
        // In a real implementation, this would clear application caches
        log.info("Cache cleared by admin");
        return ResponseEntity.ok(ApiResponse.success(null, "Cache cleared successfully"));
    }
    
    @PostMapping("/cache/clear/{cacheName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> clearSpecificCache(@PathVariable String cacheName) {
        // In a real implementation, this would clear a specific cache
        log.info("Cache '{}' cleared by admin", cacheName);
        return ResponseEntity.ok(ApiResponse.success(null, "Cache '" + cacheName + "' cleared successfully"));
    }
    
    // Backup & Maintenance
    
    @PostMapping("/backup/database")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> backupDatabase() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("timestamp", LocalDateTime.now());
        result.put("filename", "epicgreen_backup_" + System.currentTimeMillis() + ".sql");
        
        log.info("Database backup initiated by admin");
        return ResponseEntity.ok(ApiResponse.success(result, "Database backup initiated"));
    }
    
    @PostMapping("/maintenance/mode")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> toggleMaintenanceMode(@RequestParam boolean enabled) {
        configStore.put("system.maintenanceMode", enabled);
        String message = enabled ? "Maintenance mode enabled" : "Maintenance mode disabled";
        log.info(message);
        return ResponseEntity.ok(ApiResponse.success(null, message));
    }
    
    @GetMapping("/maintenance/status")
    public ResponseEntity<ApiResponse<Boolean>> getMaintenanceStatus() {
        boolean maintenanceMode = (boolean) configStore.getOrDefault("system.maintenanceMode", false);
        return ResponseEntity.ok(ApiResponse.success(maintenanceMode, "Maintenance status retrieved"));
    }
    
    // Audit Log
    
    @GetMapping("/audit-log")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAuditLog(
        @RequestParam(defaultValue = "50") int limit
    ) {
        // In a real implementation, this would fetch from audit log table
        List<Map<String, Object>> auditLog = new ArrayList<>();
        
        return ResponseEntity.ok(ApiResponse.success(auditLog, "Audit log retrieved"));
    }
}


