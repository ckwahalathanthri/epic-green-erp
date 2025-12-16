package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Permission entity
 * Represents system permissions (e.g., PERM_VIEW_PRODUCTS, PERM_CREATE_ORDERS)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "permissions", indexes = {
    @Index(name = "idx_permission_code", columnList = "permission_code"),
    @Index(name = "idx_permission_module", columnList = "module"),
    @Index(name = "idx_permission_category", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Permission code (unique, e.g., PERM_VIEW_PRODUCTS)
     */
    @Column(name = "permission_code", nullable = false, unique = true, length = 100)
    private String permissionCode;
    
    /**
     * Permission name (display name)
     */
    @Column(name = "permission_name", nullable = false, length = 100)
    private String permissionName;
    
    /**
     * Permission description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Module (e.g., PRODUCT, SALES, INVENTORY)
     */
    @Column(name = "module", nullable = false, length = 50)
    private String module;
    
    /**
     * Category (e.g., VIEW, CREATE, EDIT, DELETE, APPROVE)
     */
    @Column(name = "category", length = 20)
    private String category;
    
    /**
     * System permission flag (cannot be deleted)
     */
    @Column(name = "is_system_permission", nullable = false)
    private Boolean isSystemPermission;
    
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
     * Roles that have this permission
     */
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<RolePermission> rolePermissions = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isSystemPermission == null) {
            isSystemPermission = false;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (category == null) {
            // Extract category from permission code
            // Example: PERM_VIEW_PRODUCTS -> VIEW
            if (permissionCode != null && permissionCode.startsWith("PERM_")) {
                String[] parts = permissionCode.substring(5).split("_");
                if (parts.length > 0) {
                    category = parts[0];
                }
            }
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
