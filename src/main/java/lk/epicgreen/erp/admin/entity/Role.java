package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Role entity
 * Represents user roles for authorization
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "roles", indexes = {
    @Index(name = "idx_role_code", columnList = "role_code")
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
     * Role name (unique, display name)
     */
    @NotBlank(message = "Role name is required")
    @Size(max = 50)
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;
    
    /**
     * Role code (unique, system identifier)
     */
    @NotBlank(message = "Role code is required")
    @Size(max = 30)
    @Column(name = "role_code", nullable = false, unique = true, length = 30)
    private String roleCode;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Is system role (cannot be deleted)
     */
    @Column(name = "is_system_role")
    private Boolean isSystemRole;

     @Transient
    public Set<Permission> getPermissions() {
        if (rolePermissions == null) {
            return new HashSet<>();
        }
        return rolePermissions.stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toSet());
    }

    public void setPermissions(Set<Permission> newPermissions) {
        if (newPermissions == null) {
            if (this.rolePermissions != null) {
                this.rolePermissions.clear();
            }
            return;
        }

        // Initialize set if null
        if (this.rolePermissions == null) {
            this.rolePermissions = new HashSet<>();
        }

        // 1. Remove permissions that are NOT in the new set
        // This triggers the orphanRemoval=true to delete from DB
        this.rolePermissions.removeIf(rp -> !newPermissions.contains(rp.getPermission()));

        // 2. Add permissions that are in the new set but NOT in current list
        Set<Long> currentPermissionIds = this.rolePermissions.stream()
                .map(rp -> rp.getPermission().getId())
                .collect(Collectors.toSet());

        for (Permission permission : newPermissions) {
            if (!currentPermissionIds.contains(permission.getId())) {
                RolePermission rolePermission = RolePermission.builder()
                        .role(this)
                        .permission(permission)
                        .build();
                this.rolePermissions.add(rolePermission);
            }
        }
    }


    
    /**
     * Role permissions (many-to-many)
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
     * Check if this is a system role
     */
    @Transient
    public boolean isSystemRole() {
        return Boolean.TRUE.equals(isSystemRole);
    }
    
    /**
     * Check if role can be deleted
     */
    @Transient
    public boolean canDelete() {
        return !isSystemRole() && (userRoles == null || userRoles.isEmpty());
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isSystemRole == null) {
            isSystemRole = false;
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
