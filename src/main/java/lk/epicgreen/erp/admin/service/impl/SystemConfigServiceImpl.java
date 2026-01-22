package lk.epicgreen.erp.admin.service.impl;

import lk.epicgreen.erp.admin.dto.request.SystemConfigRequest;
import lk.epicgreen.erp.admin.dto.response.SystemConfigResponse;
import lk.epicgreen.erp.admin.entity.SystemConfig;
import lk.epicgreen.erp.admin.mapper.SystemConfigMapper;
import lk.epicgreen.erp.admin.repository.SystemConfigRepository;
import lk.epicgreen.erp.admin.service.SystemConfigService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of SystemConfigService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;
    private final SystemConfigMapper systemConfigMapper;

    @Override
    @Transactional
    public SystemConfigResponse createSystemConfig(SystemConfigRequest request) {
        log.info("Creating new system configuration: {}", request.getConfigKey());

        validateUniqueConfigKey(request.getConfigKey());

        SystemConfig systemConfig = systemConfigMapper.toEntity(request);
        SystemConfig savedConfig = systemConfigRepository.save(systemConfig);

        log.info("System configuration created successfully: {}", savedConfig.getConfigKey());
        return systemConfigMapper.toResponse(savedConfig);
    }

    @Override
    @Transactional
    public SystemConfigResponse updateSystemConfig(Long id, SystemConfigRequest request) {
        log.info("Updating system configuration: {}", id);

        SystemConfig systemConfig = findSystemConfigById(id);

        if (!systemConfig.getConfigKey().equals(request.getConfigKey())) {
            validateUniqueConfigKey(request.getConfigKey());
        }

        systemConfigMapper.updateEntityFromRequest(request, systemConfig);

        SystemConfig updatedConfig = systemConfigRepository.save(systemConfig);
        log.info("System configuration updated successfully: {}", updatedConfig.getConfigKey());

        return systemConfigMapper.toResponse(updatedConfig);
    }

    @Override
    @Transactional
    public void updateConfigValue(String configKey, String configValue) {
        log.info("Updating configuration value for key: {}", configKey);

        SystemConfig systemConfig = systemConfigRepository.findByConfigKey(configKey)
            .orElseThrow(() -> new ResourceNotFoundException("Configuration not found: " + configKey));

        systemConfig.setConfigValue(configValue);
        systemConfigRepository.save(systemConfig);

        log.info("Configuration value updated successfully for key: {}", configKey);
    }

    @Override
    @Transactional
    public void deleteSystemConfig(Long id) {
        log.info("Deleting system configuration: {}", id);

        SystemConfig systemConfig = findSystemConfigById(id);
        systemConfigRepository.delete(systemConfig);

        log.info("System configuration deleted successfully: {}", id);
    }

    @Override
    public SystemConfigResponse getSystemConfigById(Long id) {
        SystemConfig systemConfig = findSystemConfigById(id);
        return systemConfigMapper.toResponse(systemConfig);
    }

    @Override
    public SystemConfigResponse getSystemConfigByKey(String configKey) {
        SystemConfig systemConfig = systemConfigRepository.findByConfigKey(configKey)
            .orElseThrow(() -> new ResourceNotFoundException("Configuration not found: " + configKey));
        return systemConfigMapper.toResponse(systemConfig);
    }

    @Override
    public String getConfigValue(String configKey) {
        return systemConfigRepository.findByConfigKey(configKey)
            .map(SystemConfig::getConfigValue)
            .orElseThrow(() -> new ResourceNotFoundException("Configuration not found: " + configKey));
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        return systemConfigRepository.findByConfigKey(configKey)
            .map(SystemConfig::getConfigValue)
            .orElse(defaultValue);
    }

    @Override
    public List<SystemConfigResponse> getAllSystemConfigs() {
        List<SystemConfig> systemConfigs = systemConfigRepository.findAll();
        return systemConfigs.stream()
            .map(systemConfigMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<SystemConfigResponse> getConfigsByGroup(String configGroup) {
        List<SystemConfig> systemConfigs = systemConfigRepository.findByConfigGroup(configGroup);
        return systemConfigs.stream()
            .map(systemConfigMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableConfigGroups() {
        //return systemConfigRepository.findDistinctConfigGroups();
        return systemConfigRepository.findAllDistinctGroups();
    }

    @Override
    public boolean configKeyExists(String configKey) {
        return systemConfigRepository.existsByConfigKey(configKey);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private SystemConfig findSystemConfigById(Long id) {
        return systemConfigRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("System configuration not found: " + id));
    }

    private void validateUniqueConfigKey(String configKey) {
        if (systemConfigRepository.existsByConfigKey(configKey)) {
            throw new DuplicateResourceException("Configuration key already exists: " + configKey);
        }
    }
}
