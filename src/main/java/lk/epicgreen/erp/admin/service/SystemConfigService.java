package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.request.SystemConfigRequest;
import lk.epicgreen.erp.admin.dto.response.SystemConfigResponse;

import java.util.List;

/**
 * Service interface for SystemConfig entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SystemConfigService {

    /**
     * Create new system configuration
     */
    SystemConfigResponse createSystemConfig(SystemConfigRequest request);

    /**
     * Update existing system configuration
     */
    SystemConfigResponse updateSystemConfig(Long id, SystemConfigRequest request);

    /**
     * Update configuration value
     */
    void updateConfigValue(String configKey, String configValue);

    /**
     * Delete system configuration
     */
    void deleteSystemConfig(Long id);

    /**
     * Get system configuration by ID
     */
    SystemConfigResponse getSystemConfigById(Long id);

    /**
     * Get system configuration by key
     */
    SystemConfigResponse getSystemConfigByKey(String configKey);

    /**
     * Get configuration value
     */
    String getConfigValue(String configKey);

    /**
     * Get configuration value with default
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * Get all system configurations
     */
    List<SystemConfigResponse> getAllSystemConfigs();

    /**
     * Get configurations by group
     */
    List<SystemConfigResponse> getConfigsByGroup(String configGroup);

    /**
     * Get available configuration groups
     */
    List<String> getAvailableConfigGroups();

    /**
     * Check if configuration key exists
     */
    boolean configKeyExists(String configKey);
}
