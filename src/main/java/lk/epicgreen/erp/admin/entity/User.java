package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User entity
 * Represents system users with authentication and authorization
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_username", columnList = "username"),
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_deleted_at", columnList = "deleted_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Username (unique, login identifier)
     */
    @NotBlank(message = "Username is required")
    @Size(max = 50)
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    /**
     * Email (unique)
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    /**
     * Password hash (BCrypt encrypted)
     */
    @NotBlank(message = "Password is required")
    @Size(max = 255)
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    
    /**
     * First name
     */
    @NotBlank(message = "First name is required")
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    /**
     * Last name
     */
    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    /**
     * Mobile number
     */
    @Size(max = 20)
    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;
    
    /**
     * Employee code (unique, optional)
     */
    @Size(max = 20)
    @Column(name = "employee_code", unique = true, length = 20)
    private String employeeCode;

    /*
    role of the user
     */
    
    
    /**
     * User status (ACTIVE, INACTIVE, SUSPENDED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Last login timestamp
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    /**
     * Password last changed timestamp
     */
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;
    
    /**
     * Failed login attempts counter
     */
    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts;
    
    /**
     * Soft delete timestamp
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    /**
     * User roles (many-to-many)
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();

    /**
     * Helper to get flat roles
     */
    @Transient
    public Set<Role> getRoles() {
        if (userRoles == null) {
            return new HashSet<>();
        }
        return userRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());
    }

    public void setRoles(Set<Role> newRoles){
        if(newRoles == null){
            if (this.userRoles != null) {
                this.userRoles.clear();
            }
            return;  
        }

        if (this.userRoles == null) {
            this.userRoles = new HashSet<>();
        }

        this.userRoles.removeIf(ur -> !newRoles.contains(ur.getRole()));

        Set<Long> currentRoleIds = this.userRoles.stream()
                .map(ur -> ur.getRole().getId())
                .collect(Collectors.toSet());

        for (Role role : newRoles) {
            if (!currentRoleIds.contains(role.getId())) {
                UserRole newUserRole = UserRole.builder()
                        .user(this) 
                        .role(role)
                        .assignedAt(LocalDateTime.now()) 
                        .build();
                this.userRoles.add(newUserRole);
            }
        }
    }
    
    /**
     * Get full name
     */
    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Check if user is active
     */
    @Transient
    public boolean isActive() {
        return "ACTIVE".equals(status) && deletedAt == null;
    }
    
    /**
     * Check if user is locked due to failed login attempts
     */
    @Transient
    public boolean isLocked() {
        return failedLoginAttempts != null && failedLoginAttempts >= 5;
    }
    
    /**
     * Check if user is deleted (soft delete)
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
    
    /**
     * Reset failed login attempts
     */
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }
    
    /**
     * Increment failed login attempts
     */
    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts = (this.failedLoginAttempts == null ? 0 : this.failedLoginAttempts) + 1;
    }
    
    /**
     * Soft delete user
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.status = "INACTIVE";
    }
    
    /**
     * Restore soft deleted user
     */
    public void restore() {
        this.deletedAt = null;
        this.status = "ACTIVE";
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "ACTIVE";
        }
        if (failedLoginAttempts == null) {
            failedLoginAttempts = 0;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
