package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Permission entity
 * Represents system permissions for fine-grained access control
 * 
 * Note: Does NOT extend AuditEntity (permissions are created at system init)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "permissions", indexes = {
    @Index(name = "idx_module", columnList = "module"),
    @Index(name = "idx_permission_code", columnList = "permission_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Permission name (unique, display name)
     */
    @NotBlank(message = "Permission name is required")
    @Size(max = 100)
    @Column(name = "permission_name", nullable = false, unique = true, length = 100)
    private String permissionName;
    
    /**
     * Permission code (unique, system identifier)
     */
    @NotBlank(message = "Permission code is required")
    @Size(max = 50)
    @Column(name = "permission_code", nullable = false, unique = true, length = 50)
    private String permissionCode;
    
    /**
     * Module name (grouping)
     */
    @NotBlank(message = "Module is required")
    @Size(max = 50)
    @Column(name = "module", nullable = false, length = 50)
    private String module;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Roles with this permission
     */
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<RolePermission> rolePermissions = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
