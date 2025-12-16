package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

/**
 * RolePermission entity
 * Junction table for many-to-many relationship between Role and Permission
 * Defines which permissions are granted to each role
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "role_permissions",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_role_permission", columnNames = {"role_id", "permission_id"})
    },
    indexes = {
        @Index(name = "idx_role_permission_role", columnList = "role_id"),
        @Index(name = "idx_role_permission_permission", columnList = "permission_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Role reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_role_permission_role"))
    private Role role;
    
    /**
     * Permission reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false, foreignKey = @ForeignKey(name = "fk_role_permission_permission"))
    private Permission permission;
    
    /**
     * Is this permission granted or denied
     * true = granted, false = denied (for explicit denial)
     */
    @Column(name = "is_granted", nullable = false)
    private Boolean isGranted;
    
    /**
     * Notes about this permission assignment
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isGranted == null) {
            isGranted = true;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolePermission)) return false;
        RolePermission that = (RolePermission) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
