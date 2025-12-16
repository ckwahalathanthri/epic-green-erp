package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDate;

/**
 * UserRole entity
 * Junction table for many-to-many relationship between User and Role
 * Allows a user to have multiple roles
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "user_roles", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_role", columnNames = {"user_id", "role_id"})
    },
    indexes = {
        @Index(name = "idx_user_role_user", columnList = "user_id"),
        @Index(name = "idx_user_role_role", columnList = "role_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_role_user"))
    private User user;
    
    /**
     * Role reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_role_role"))
    private Role role;
    
    /**
     * Is this the primary role for the user
     */
    @Column(name = "is_primary_role")
    private Boolean isPrimaryRole;
    
    /**
     * Role assignment start date (optional)
     */
    @Column(name = "valid_from")
    private LocalDate validFrom;
    
    /**
     * Role assignment end date (optional)
     */
    @Column(name = "valid_to")
    private LocalDate validTo;
    
    /**
     * Notes about this role assignment
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Checks if role assignment is currently valid
     */
    @Transient
    public boolean isValid() {
        LocalDate today = LocalDate.now();
        
        if (validFrom != null && today.isBefore(validFrom)) {
            return false;
        }
        
        if (validTo != null && today.isAfter(validTo)) {
            return false;
        }
        
        return true;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isPrimaryRole == null) {
            isPrimaryRole = false;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return id != null && id.equals(userRole.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
