package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * RolePermission entity
 * Many-to-many relationship between Role and Permission
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
           @Index(name = "idx_role_id", columnList = "role_id"),
           @Index(name = "idx_permission_id", columnList = "permission_id")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {
    
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
     * Grant timestamp
     */
    @Column(name = "granted_at")
    private LocalDateTime grantedAt;
    
    /**
     * Granted by (user ID)
     */
    @Column(name = "granted_by")
    private Long grantedBy;
    
    @PrePersist
    protected void onCreate() {
        if (grantedAt == null) {
            grantedAt = LocalDateTime.now();
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
