package lk.epicgreen.erp.admin.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * UserRole entity
 * Many-to-many relationship between User and Role
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
           @Index(name = "idx_user_id", columnList = "user_id"),
           @Index(name = "idx_role_id", columnList = "role_id")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    
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
     * Assignment timestamp
     */
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    /**
     * Assigned by (user ID)
     */
    @Column(name = "assigned_by")
    private Long assignedBy;
    
    @PrePersist
    protected void onCreate() {
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
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
