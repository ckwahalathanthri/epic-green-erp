package lk.epicgreen.erp.admin.service.impl;

import lk.epicgreen.erp.admin.dto.request.RoleCreateRequest;
import lk.epicgreen.erp.admin.dto.request.RoleUpdateRequest;
import lk.epicgreen.erp.admin.dto.response.RoleResponse;
import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.entity.Permission;
import lk.epicgreen.erp.admin.mapper.RoleMapper;
import lk.epicgreen.erp.admin.repository.RoleRepository;
import lk.epicgreen.erp.admin.repository.PermissionRepository;
import lk.epicgreen.erp.admin.service.RoleService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of RoleService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleResponse createRole(RoleCreateRequest request) {
        log.info("Creating new role: {}", request.getRoleCode());

        // Validate unique constraints
        validateUniqueRoleName(request.getRoleName(), null);
        validateUniqueRoleCode(request.getRoleCode(), null);

        // Create role entity
        Role role = roleMapper.toEntity(request);

        // Assign permissions
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = request.getPermissionIds().stream()
                .map(permId -> permissionRepository.findById(permId)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permId)))
                .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        Role savedRole = roleRepository.save(role);
        log.info("Role created successfully: {}", savedRole.getRoleCode());

        return roleMapper.toResponse(savedRole);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long id, RoleUpdateRequest request) {
        log.info("Updating role: {}", id);

        Role role = findRoleById(id);

        // Prevent modification of system roles
        if (role.getIsSystemRole()) {
            throw new InvalidOperationException("System roles cannot be modified");
        }

        // Validate unique constraints
        if (request.getRoleName() != null) {
            validateUniqueRoleName(request.getRoleName(), id);
            role.setRoleName(request.getRoleName());
        }

        if (request.getRoleCode() != null) {
            validateUniqueRoleCode(request.getRoleCode(), id);
            role.setRoleCode(request.getRoleCode());
        }

        // Update fields
        roleMapper.updateEntityFromRequest(request, role);

        // Update permissions if provided
        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = request.getPermissionIds().stream()
                .map(permId -> permissionRepository.findById(permId)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permId)))
                .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        Role updatedRole = roleRepository.save(role);
        log.info("Role updated successfully: {}", updatedRole.getRoleCode());

        return roleMapper.toResponse(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        log.info("Deleting role: {}", id);

        Role role = findRoleById(id);

        // Prevent deletion of system roles
        if (role.getIsSystemRole()) {
            throw new InvalidOperationException("System roles cannot be deleted");
        }

        // Check if role is assigned to any users
        long userCount = roleRepository.countUsersByRoleId(id);
        if (userCount > 0) {
            throw new InvalidOperationException(
                "Cannot delete role. It is assigned to " + userCount + " user(s)");
        }

        roleRepository.delete(role);
        log.info("Role deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void assignPermission(Long roleId, Long permissionId) {
        log.info("Assigning permission {} to role {}", permissionId, roleId);

        Role role = findRoleById(roleId);
        
        if (role.getIsSystemRole()) {
            throw new InvalidOperationException("Cannot modify permissions of system roles");
        }

        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permissionId));

        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<>());
        }

        role.getPermissions().add(permission);
        roleRepository.save(role);

        log.info("Permission assigned successfully");
    }

    @Override
    @Transactional
    public void removePermission(Long roleId, Long permissionId) {
        log.info("Removing permission {} from role {}", permissionId, roleId);

        Role role = findRoleById(roleId);
        
        if (role.getIsSystemRole()) {
            throw new InvalidOperationException("Cannot modify permissions of system roles");
        }

        role.getPermissions().removeIf(perm -> perm.getId().equals(permissionId));
        roleRepository.save(role);

        log.info("Permission removed successfully");
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        log.info("Assigning {} permissions to role {}", permissionIds.size(), roleId);

        Role role = findRoleById(roleId);
        
        if (role.getIsSystemRole()) {
            throw new InvalidOperationException("Cannot modify permissions of system roles");
        }

        Set<Permission> permissions = permissionIds.stream()
            .map(permId -> permissionRepository.findById(permId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permId)))
            .collect(Collectors.toSet());

        role.setPermissions(permissions);
        roleRepository.save(role);

        log.info("Permissions assigned successfully");
    }

    @Override
    public RoleResponse getRoleById(Long id) {
        Role role = findRoleById(id);
        return roleMapper.toResponse(role);
    }

    @Override
    public RoleResponse getRoleByCode(String roleCode) {
        Role role = roleRepository.findByRoleCode(roleCode)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleCode));
        return roleMapper.toResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
            .map(roleMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<RoleResponse> getNonSystemRoles() {
        List<Role> roles = roleRepository.findByIsSystemRoleFalse();
        return roles.stream()
            .map(roleMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<RoleResponse> getSystemRoles() {
        List<Role> roles = roleRepository.findByIsSystemRoleTrue();
        return roles.stream()
            .map(roleMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Role findRoleById(Long id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));
    }

    private void validateUniqueRoleName(String roleName, Long excludeId) {
        if (excludeId == null) {
            if (roleRepository.existsByRoleName(roleName)) {
                throw new DuplicateResourceException("Role name already exists: " + roleName);
            }
        } else {
            if (roleRepository.existsByRoleNameAndIdNot(roleName, excludeId)) {
                throw new DuplicateResourceException("Role name already exists: " + roleName);
            }
        }
    }

    private void validateUniqueRoleCode(String roleCode, Long excludeId) {
        if (excludeId == null) {
            if (roleRepository.existsByRoleCode(roleCode)) {
                throw new DuplicateResourceException("Role code already exists: " + roleCode);
            }
        } else {
            if (roleRepository.existsByRoleCodeAndIdNot(roleCode, excludeId)) {
                throw new DuplicateResourceException("Role code already exists: " + roleCode);
            }
        }
    }
}
