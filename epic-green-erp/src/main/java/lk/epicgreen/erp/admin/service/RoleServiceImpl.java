package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.RoleRequest;
import lk.epicgreen.erp.admin.entity.Permission;
import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.repository.PermissionRepository;
import lk.epicgreen.erp.admin.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Role Service Implementation
 * Implementation of role service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    
    @Override
    public Role createRole(RoleRequest request) {
        log.info("Creating role: {}", request.getRoleName());
        
        // Validate unique fields
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new RuntimeException("Role name already exists: " + request.getRoleName());
        }
        if (request.getRoleCode() != null && roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new RuntimeException("Role code already exists: " + request.getRoleCode());
        }
        
        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setRoleCode(request.getRoleCode());
        role.setDescription(request.getDescription());
        role.setCategory(request.getCategory());
        role.setLevel(request.getLevel() != null ? request.getLevel() : 5);
        role.setIsActive(true);
        role.setIsSystemRole(false);
        role.setParentRoleId(request.getParentRoleId());
        
        Role saved = roleRepository.save(role);
        
        // Assign permissions if provided
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            assignPermissions(saved.getId(), request.getPermissionIds());
        }
        
        return saved;
    }
    
    @Override
    public Role updateRole(Long id, RoleRequest request) {
        log.info("Updating role: {}", id);
        Role existing = getRoleById(id);
        
        // Cannot update system roles
        if (existing.getIsSystemRole()) {
            throw new RuntimeException("Cannot update system role");
        }
        
        // Validate unique fields if changed
        if (!existing.getRoleName().equals(request.getRoleName()) &&
            roleRepository.existsByRoleName(request.getRoleName())) {
            throw new RuntimeException("Role name already exists: " + request.getRoleName());
        }
        if (request.getRoleCode() != null && !request.getRoleCode().equals(existing.getRoleCode()) &&
            roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new RuntimeException("Role code already exists: " + request.getRoleCode());
        }
        
        existing.setRoleName(request.getRoleName());
        existing.setRoleCode(request.getRoleCode());
        existing.setDescription(request.getDescription());
        existing.setCategory(request.getCategory());
        existing.setLevel(request.getLevel());
        existing.setParentRoleId(request.getParentRoleId());
        existing.setUpdatedAt(LocalDateTime.now());
        
        Role updated = roleRepository.save(existing);
        
        // Update permissions if provided
        if (request.getPermissionIds() != null) {
            updateRolePermissions(id, request.getPermissionIds());
        }
        
        return updated;
    }
    
    @Override
    public void deleteRole(Long id) {
        log.info("Deleting role: {}", id);
        Role role = getRoleById(id);
        
        if (!canDeleteRole(id)) {
            throw new RuntimeException("Cannot delete role with existing users or system role");
        }
        
        roleRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Role getRoleByCode(String roleCode) {
        return roleRepository.findByRoleCode(roleCode)
            .orElseThrow(() -> new RuntimeException("Role not found with code: " + roleCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Role> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Role> searchRoles(String keyword, Pageable pageable) {
        return roleRepository.searchRoles(keyword, pageable);
    }
    
    @Override
    public Role activateRole(Long id) {
        log.info("Activating role: {}", id);
        Role role = getRoleById(id);
        role.setIsActive(true);
        role.setUpdatedAt(LocalDateTime.now());
        return roleRepository.save(role);
    }
    
    @Override
    public Role deactivateRole(Long id) {
        log.info("Deactivating role: {}", id);
        Role role = getRoleById(id);
        
        if (role.getIsSystemRole()) {
            throw new RuntimeException("Cannot deactivate system role");
        }
        
        role.setIsActive(false);
        role.setUpdatedAt(LocalDateTime.now());
        return roleRepository.save(role);
    }
    
    @Override
    public void assignPermission(Long roleId, Long permissionId) {
        Role role = getRoleById(roleId);
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
        
        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<>());
        }
        
        role.getPermissions().add(permission);
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }
    
    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = getRoleById(roleId);
        Set<Permission> permissions = new HashSet<>();
        
        for (Long permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
            permissions.add(permission);
        }
        
        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<>());
        }
        
        role.getPermissions().addAll(permissions);
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }
    
    @Override
    public void removePermission(Long roleId, Long permissionId) {
        Role role = getRoleById(roleId);
        
        if (role.getPermissions() != null) {
            role.getPermissions().removeIf(permission -> permission.getId().equals(permissionId));
            role.setUpdatedAt(LocalDateTime.now());
            roleRepository.save(role);
        }
    }
    
    @Override
    public void removeAllPermissions(Long roleId) {
        Role role = getRoleById(roleId);
        
        if (role.getPermissions() != null) {
            role.getPermissions().clear();
            role.setUpdatedAt(LocalDateTime.now());
            roleRepository.save(role);
        }
    }
    
    @Override
    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {
        Role role = getRoleById(roleId);
        Set<Permission> permissions = new HashSet<>();
        
        for (Long permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
            permissions.add(permission);
        }
        
        role.setPermissions(permissions);
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<String> getRolePermissions(Long roleId) {
        Role role = getRoleById(roleId);
        if (role.getPermissions() == null) {
            return new ArrayList<>();
        }
        return role.getPermissions().stream()
            .map(Permission::getPermissionName)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasPermission(Long roleId, String permissionName) {
        return getRolePermissions(roleId).contains(permissionName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getActiveRoles() {
        return roleRepository.findActiveRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Role> getActiveRoles(Pageable pageable) {
        return roleRepository.findActiveRoles(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getInactiveRoles() {
        return roleRepository.findInactiveRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getSystemRoles() {
        return roleRepository.findSystemRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getCustomRoles() {
        return roleRepository.findCustomRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesByCategory(String category) {
        return roleRepository.findByCategory(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesByLevel(Integer level) {
        return roleRepository.findByLevel(level);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesByLevelRange(Integer minLevel, Integer maxLevel) {
        return roleRepository.findByLevelRange(minLevel, maxLevel);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesByPermission(String permissionName) {
        return roleRepository.findByPermissionName(permissionName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesWithPermissions() {
        return roleRepository.findRolesWithPermissions();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesWithoutPermissions() {
        return roleRepository.findRolesWithoutPermissions();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesByUserCount(Integer minUsers) {
        return roleRepository.findRolesByUserCount(minUsers);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesWithNoUsers() {
        return roleRepository.findRolesWithNoUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRootRoles() {
        return roleRepository.findRootRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getChildRoles(Long parentId) {
        return roleRepository.findByParentRoleId(parentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getAdministrativeRoles() {
        return roleRepository.findAdministrativeRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getOperationalRoles() {
        return roleRepository.findOperationalRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRecentRoles(int limit) {
        return roleRepository.findRecentRoles(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRecentlyUpdatedRoles(int limit) {
        return roleRepository.findRecentlyUpdatedRoles(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesWithMostUsers(int limit) {
        return roleRepository.getRolesWithMostUsers(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesWithMostPermissions(int limit) {
        return roleRepository.getRolesWithMostPermissions(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateRole(Role role) {
        return role.getRoleName() != null &&
               role.getRoleCode() != null &&
               role.getLevel() != null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isRoleNameAvailable(String roleName) {
        return !roleRepository.existsByRoleName(roleName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isRoleCodeAvailable(String roleCode) {
        return !roleRepository.existsByRoleCode(roleCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteRole(Long roleId) {
        Role role = getRoleById(roleId);
        // Cannot delete system roles or roles with users
        return !role.getIsSystemRole() && (role.getUsers() == null || role.getUsers().isEmpty());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isSystemRole(Long roleId) {
        Role role = getRoleById(roleId);
        return role.getIsSystemRole();
    }
    
    @Override
    public void setParentRole(Long roleId, Long parentId) {
        Role role = getRoleById(roleId);
        Role parent = getRoleById(parentId);
        
        role.setParentRoleId(parentId);
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }
    
    @Override
    public void removeParentRole(Long roleId) {
        Role role = getRoleById(roleId);
        role.setParentRoleId(null);
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Role getParentRole(Long roleId) {
        Role role = getRoleById(roleId);
        if (role.getParentRoleId() == null) {
            return null;
        }
        return getRoleById(role.getParentRoleId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoleHierarchy(Long roleId) {
        List<Role> hierarchy = new ArrayList<>();
        Role current = getRoleById(roleId);
        
        while (current != null) {
            hierarchy.add(0, current);
            if (current.getParentRoleId() != null) {
                current = getRoleById(current.getParentRoleId());
            } else {
                current = null;
            }
        }
        
        return hierarchy;
    }
    
    @Override
    public List<Role> createBulkRoles(List<RoleRequest> requests) {
        return requests.stream()
            .map(this::createRole)
            .collect(Collectors.toList());
    }
    
    @Override
    public int activateBulkRoles(List<Long> roleIds) {
        int count = 0;
        for (Long id : roleIds) {
            try {
                activateRole(id);
                count++;
            } catch (Exception e) {
                log.error("Error activating role: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deactivateBulkRoles(List<Long> roleIds) {
        int count = 0;
        for (Long id : roleIds) {
            try {
                deactivateRole(id);
                count++;
            } catch (Exception e) {
                log.error("Error deactivating role: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkRoles(List<Long> roleIds) {
        int count = 0;
        for (Long id : roleIds) {
            try {
                deleteRole(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting role: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int assignPermissionToBulkRoles(List<Long> roleIds, Long permissionId) {
        int count = 0;
        for (Long id : roleIds) {
            try {
                assignPermission(id, permissionId);
                count++;
            } catch (Exception e) {
                log.error("Error assigning permission to role: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getRoleStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalRoles", getTotalRoles());
        stats.put("activeRoles", getActiveRolesCount());
        stats.put("inactiveRoles", getInactiveRolesCount());
        stats.put("systemRoles", getSystemRolesCount());
        stats.put("customRoles", getCustomRolesCount());
        stats.put("rolesWithPermissions", roleRepository.countRolesWithPermissions());
        stats.put("rolesWithoutPermissions", roleRepository.countRolesWithoutPermissions());
        stats.put("averagePermissionsPerRole", getAveragePermissionsPerRole());
        stats.put("averageUsersPerRole", getAverageUsersPerRole());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoryDistribution() {
        List<Object[]> results = roleRepository.getCategoryDistribution();
        return convertToMapList(results, "category", "roleCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLevelDistribution() {
        List<Object[]> results = roleRepository.getLevelDistribution();
        return convertToMapList(results, "level", "roleCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRolesByUserCount() {
        List<Object[]> results = roleRepository.getRolesByUserCount();
        return convertToMapList(results, "roleName", "userCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRolesByPermissionCount() {
        List<Object[]> results = roleRepository.getRolesByPermissionCount();
        return convertToMapList(results, "roleName", "permissionCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAveragePermissionsPerRole() {
        Double average = roleRepository.getAveragePermissionsPerRole();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageUsersPerRole() {
        Double average = roleRepository.getAverageUsersPerRole();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getTotalRoles() {
        return roleRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getActiveRolesCount() {
        return roleRepository.countActiveRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getInactiveRolesCount() {
        return roleRepository.countInactiveRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getSystemRolesCount() {
        return roleRepository.countSystemRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getCustomRolesCount() {
        return roleRepository.countCustomRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getRoleStatistics());
        dashboard.put("categoryDistribution", getCategoryDistribution());
        dashboard.put("levelDistribution", getLevelDistribution());
        dashboard.put("rolesByUserCount", getRolesByUserCount());
        dashboard.put("rolesByPermissionCount", getRolesByPermissionCount());
        dashboard.put("rolesWithMostUsers", getRolesWithMostUsers(10));
        dashboard.put("rolesWithMostPermissions", getRolesWithMostPermissions(10));
        dashboard.put("recentRoles", getRecentRoles(10));
        
        return dashboard;
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
