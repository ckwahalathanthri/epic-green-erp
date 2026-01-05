package lk.epicgreen.erp.admin.mapper;

import lk.epicgreen.erp.admin.dto.request.SystemConfigRequest;
import lk.epicgreen.erp.admin.dto.response.SystemConfigResponse;
import lk.epicgreen.erp.admin.entity.SystemConfig;
import org.springframework.stereotype.Component;

/**
 * Mapper for SystemConfig entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class SystemConfigMapper {

    public SystemConfig toEntity(SystemConfigRequest request) {
        if (request == null) {
            return null;
        }

        return SystemConfig.builder()
            .configKey(request.getConfigKey())
            .configValue(request.getConfigValue())
            .configGroup(request.getConfigGroup())
            .description(request.getDescription())
            .isEncrypted(request.getIsEncrypted() != null ? request.getIsEncrypted() : false)
            .build();
    }

    public void updateEntityFromRequest(SystemConfigRequest request, SystemConfig systemConfig) {
        if (request == null || systemConfig == null) {
            return;
        }

        systemConfig.setConfigKey(request.getConfigKey());
        systemConfig.setConfigValue(request.getConfigValue());
        systemConfig.setConfigGroup(request.getConfigGroup());
        systemConfig.setDescription(request.getDescription());
        
        if (request.getIsEncrypted() != null) {
            systemConfig.setIsEncrypted(request.getIsEncrypted());
        }
    }

    public SystemConfigResponse toResponse(SystemConfig systemConfig) {
        if (systemConfig == null) {
            return null;
        }

        return SystemConfigResponse.builder()
            .id(systemConfig.getId())
            .configKey(systemConfig.getConfigKey())
            .configValue(systemConfig.getConfigValue())
            .configGroup(systemConfig.getConfigGroup())
            .description(systemConfig.getDescription())
            .isEncrypted(systemConfig.getIsEncrypted())
            .createdAt(systemConfig.getCreatedAt())
            .updatedAt(systemConfig.getUpdatedAt())
            .build();
    }
}
