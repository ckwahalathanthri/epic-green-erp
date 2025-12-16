package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Role entity
 * Represents user roles in the system (e.g., ADMIN, MANAGER, USER)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "roles", indexes = {
    @Index(name = "idx_role_code", columnList = "role_code"),
    @Index(name = "idx_role_name", columnList = "role_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Role code (unique, e.g., ROLE_ADMIN)
     */
    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    private String roleCode;
    
    /**
     * Role name (display name)
     */
    @Column(name = "role_name", nullable = false, length = 100)
    private String roleName;
    
    /**
     * Role description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Role type (SYSTEM, DEPARTMENT, CUSTOM)
     */
    @Column(name = "role_type", length = 20)
    private String roleType;
    
    /**
     * Role category (e.g., ADMINISTRATIVE, OPERATIONAL, SALES, PRODUCTION)
     */
    @Column(name = "category", length = 50)
    private String category;
    
    /**
     * Role level (1=highest, 5=lowest)
     */
    @Column(name = "level")
    private Integer level;
    
    /**
     * Parent role ID for hierarchy
     */
    @Column(name = "parent_role_id")
    private Long parentRoleId;
    
    /**
     * System role flag (cannot be deleted)
     */
    @Column(name = "is_system_role", nullable = false)
    private Boolean isSystemRole;
    
    /**
     * Active status
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    /**
     * Display order
     */
    @Column(name = "display_order")
    private Integer displayOrder;
    
    /**
     * Role permissions - many to many relationship
     */
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<RolePermission> rolePermissions = new HashSet<>();
    
    /**
     * Users with this role
     */
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();
    
    /**
     * Adds a permission to the role
     */
    public void addPermission(Permission permission) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(this);
        rolePermission.setPermission(permission);
        rolePermissions.add(rolePermission);
    }
    
    /**
     * Removes a permission from the role
     */
    public void removePermission(Permission permission) {
        rolePermissions.removeIf(rp -> rp.getPermission().equals(permission));
    }
    
    /**
     * Checks if role has a specific permission
     */
    @Transient
    public boolean hasPermission(String permissionCode) {
        return rolePermissions.stream()
            .anyMatch(rp -> rp.getPermission().getPermissionCode().equals(permissionCode));
    }
    
    /**
     * Gets all permission codes for this role
     */
    @Transient
    public Set<String> getPermissionCodes() {
        Set<String> codes = new HashSet<>();
        for (RolePermission rp : rolePermissions) {
            codes.add(rp.getPermission().getPermissionCode());
        }
        return codes;
    }
    
    /**
     * Gets all permissions for this role
     */
    @Transient
    public Set<Permission> getPermissions() {
        Set<Permission> permissions = new HashSet<>();
        for (RolePermission rp : rolePermissions) {
            permissions.add(rp.getPermission());
        }
        return permissions;
    }
    
    /**
     * Sets permissions for this role (replaces existing)
     */
    public void setPermissions(Set<Permission> permissions) {
        rolePermissions.clear();
        if (permissions != null) {
            for (Permission permission : permissions) {
                addPermission(permission);
            }
        }
    }
    
    /**
     * Gets all users with this role
     */
    @Transient
    public Set<User> getUsers() {
        Set<User> users = new HashSet<>();
        for (UserRole ur : userRoles) {
            users.add(ur.getUser());
        }
        return users;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isSystemRole == null) {
            isSystemRole = false;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (roleType == null) {
            roleType = "CUSTOM";
        }
        if (level == null) {
            level = 5; // Default to lowest level
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return id != null && id.equals(role.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
