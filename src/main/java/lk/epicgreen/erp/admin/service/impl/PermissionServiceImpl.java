package lk.epicgreen.erp.admin.service.impl;

import lk.epicgreen.erp.admin.dto.request.PermissionCreateRequest;
import lk.epicgreen.erp.admin.dto.response.PermissionResponse;
import lk.epicgreen.erp.admin.entity.Permission;
import lk.epicgreen.erp.admin.mapper.PermissionMapper;
import lk.epicgreen.erp.admin.repository.PermissionRepository;
import lk.epicgreen.erp.admin.service.PermissionService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of PermissionService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    @Transactional
    public PermissionResponse createPermission(PermissionCreateRequest request) {
        log.info("Creating new permission: {}", request.getPermissionCode());

        // Validate unique constraints
        validateUniquePermissionCode(request.getPermissionCode());
        validateUniquePermissionName(request.getPermissionName());

        Permission permission = permissionMapper.toEntity(request);
        Permission savedPermission = permissionRepository.save(permission);

        log.info("Permission created successfully: {}", savedPermission.getPermissionCode());
        return permissionMapper.toResponse(savedPermission);
    }

    @Override
    public PermissionResponse getPermissionById(Long id) {
        Permission permission = findPermissionById(id);
        return permissionMapper.toResponse(permission);
    }

    @Override
    public PermissionResponse getPermissionByCode(String permissionCode) {
        Permission permission = permissionRepository.findByPermissionCode(permissionCode)
            .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permissionCode));
        return permissionMapper.toResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
            .map(permissionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<PermissionResponse> getPermissionsByModule(String module) {
        List<Permission> permissions = permissionRepository.findByModule(module);
        return permissions.stream()
            .map(permissionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableModules() {
        return permissionRepository.findDistinctModules();
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Permission findPermissionById(Long id) {
        return permissionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + id));
    }

    private void validateUniquePermissionCode(String permissionCode) {
        if (permissionRepository.existsByPermissionCode(permissionCode)) {
            throw new DuplicateResourceException("Permission code already exists: " + permissionCode);
        }
    }

    private void validateUniquePermissionName(String permissionName) {
        if (permissionRepository.existsByPermissionName(permissionName)) {
            throw new DuplicateResourceException("Permission name already exists: " + permissionName);
        }
    }
}
