package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity
 * Represents system users with authentication and profile information
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_username", columnList = "username"),
    @Index(name = "idx_user_email", columnList = "email"),
    @Index(name = "idx_user_employee_code", columnList = "employee_code"),
    @Index(name = "idx_user_status", columnList = "status")
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
     * Username for login (unique)
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    /**
     * Email address (unique)
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    /**
     * Encrypted password
     */
    @Column(name = "password", nullable = false)
    private String password;
    
    /**
     * First name
     */
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    /**
     * Last name
     */
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    /**
     * Employee code (optional, for HR integration)
     */
    @Column(name = "employee_code", unique = true, length = 20)
    private String employeeCode;
    
    /**
     * Phone number
     */
    @Column(name = "phone", length = 20)
    private String phone;
    
    /**
     * Mobile number
     */
    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;
    
    /**
     * Mobile (alias for mobileNumber)
     */
    @Column(name = "mobile", length = 20)
    private String mobile;
    
    /**
     * Address
     */
    @Column(name = "address", length = 500)
    private String address;
    
    /**
     * Department
     */
    @Column(name = "department", length = 50)
    private String department;
    
    /**
     * Designation/Job title
     */
    @Column(name = "designation", length = 50)
    private String designation;
    
    /**
     * Position (alias for designation)
     */
    @Column(name = "position", length = 50)
    private String position;
    
    /**
     * Profile picture URL
     */
    @Column(name = "profile_picture", length = 255)
    private String profilePicture;
    
    /**
     * Profile picture URL (alias)
     */
    @Column(name = "profile_picture_url", length = 255)
    private String profilePictureUrl;
    
    /**
     * Account status (ACTIVE, INACTIVE, LOCKED, PENDING)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Account enabled flag
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
    
    /**
     * Is active (alias for enabled)
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Account locked flag
     */
    @Column(name = "locked", nullable = false)
    private Boolean locked;
    
    /**
     * Is locked (alias for locked)
     */
    @Column(name = "is_locked")
    private Boolean isLocked;
    
    /**
     * Locked at timestamp
     */
    @Column(name = "locked_at")
    private LocalDateTime lockedAt;
    
    /**
     * Lock reason
     */
    @Column(name = "lock_reason", length = 255)
    private String lockReason;
    
    /**
     * Failed login attempts
     */
    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts;
    
    /**
     * Login count
     */
    @Column(name = "login_count")
    private Integer loginCount;
    
    /**
     * Last login timestamp
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    /**
     * Last login (alias for lastLoginAt)
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    /**
     * Last login IP address
     */
    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp;
    
    /**
     * Password changed at
     */
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;
    
    /**
     * Password expiry date (optional)
     */
    @Column(name = "password_expires_at")
    private LocalDateTime passwordExpiresAt;
    
    /**
     * Must change password on next login
     */
    @Column(name = "must_change_password")
    private Boolean mustChangePassword;
    
    /**
     * Email verified flag
     */
    @Column(name = "email_verified")
    private Boolean emailVerified;
    
    /**
     * Email verified at timestamp
     */
    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;
    
    /**
     * Email verification sent at timestamp
     */
    @Column(name = "email_verification_sent_at")
    private LocalDateTime emailVerificationSentAt;
    
    /**
     * Email verification token
     */
    @Column(name = "email_verification_token", length = 255)
    private String emailVerificationToken;
    
    /**
     * Password reset token
     */
    @Column(name = "password_reset_token", length = 255)
    private String passwordResetToken;
    
    /**
     * Password reset token expiry
     */
    @Column(name = "password_reset_expires_at")
    private LocalDateTime passwordResetExpiresAt;
    
    /**
     * User preferences (JSON)
     */
    @Column(name = "preferences", columnDefinition = "TEXT")
    private String preferences;
    
    /**
     * Notes about the user
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * User roles - many to many relationship
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();
    
    /**
     * Gets full name
     */
    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Checks if account is active
     */
    @Transient
    public boolean isActive() {
        return "ACTIVE".equals(status) && enabled && !locked;
    }
    
    /**
     * Checks if password is expired
     */
    @Transient
    public boolean isPasswordExpired() {
        return passwordExpiresAt != null && LocalDateTime.now().isAfter(passwordExpiresAt);
    }
    
    /**
     * Checks if account is locked due to failed login attempts
     */
    @Transient
    public boolean isAccountLocked() {
        return locked || (failedLoginAttempts != null && failedLoginAttempts >= 5);
    }
    
    /**
     * Increments failed login attempts
     */
    public void incrementFailedLoginAttempts() {
        if (failedLoginAttempts == null) {
            failedLoginAttempts = 0;
        }
        failedLoginAttempts++;
        
        // Lock account after 5 failed attempts
        if (failedLoginAttempts >= 5) {
            locked = true;
        }
    }
    
    /**
     * Resets failed login attempts
     */
    public void resetFailedLoginAttempts() {
        failedLoginAttempts = 0;
        locked = false;
    }
    
    /**
     * Updates last login information
     */
    public void updateLastLogin(String ipAddress) {
        lastLoginAt = LocalDateTime.now();
        lastLoginIp = ipAddress;
        resetFailedLoginAttempts();
    }
    
    /**
     * Adds a role to the user
     */
    public void addRole(Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(this);
        userRole.setRole(role);
        userRoles.add(userRole);
    }
    
    /**
     * Removes a role from the user
     */
    public void removeRole(Role role) {
        userRoles.removeIf(ur -> ur.getRole().equals(role));
    }
    
    /**
     * Gets all roles for this user
     */
    @Transient
    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        for (UserRole ur : userRoles) {
            roles.add(ur.getRole());
        }
        return roles;
    }
    
    /**
     * Sets roles for this user (replaces existing)
     */
    public void setRoles(Set<Role> roles) {
        userRoles.clear();
        if (roles != null) {
            for (Role role : roles) {
                addRole(role);
            }
        }
    }
    
    /**
     * Gets isActive flag (delegates to enabled)
     */
    @Transient
    public Boolean getIsActive() {
        return enabled;
    }
    
    /**
     * Sets isActive flag (delegates to enabled)
     */
    public void setIsActive(Boolean isActive) {
        this.enabled = isActive;
        this.isActive = isActive;
    }
    
    /**
     * Gets isLocked flag (delegates to locked)
     */
    @Transient
    public Boolean getIsLocked() {
        return locked;
    }
    
    /**
     * Sets isLocked flag (delegates to locked)
     */
    public void setIsLocked(Boolean isLocked) {
        this.locked = isLocked;
        this.isLocked = isLocked;
    }
    
    /**
     * Gets login count
     */
    public Integer getLoginCount() {
        return loginCount != null ? loginCount : 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (enabled == null) {
            enabled = true;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (locked == null) {
            locked = false;
        }
        if (isLocked == null) {
            isLocked = false;
        }
        if (failedLoginAttempts == null) {
            failedLoginAttempts = 0;
        }
        if (loginCount == null) {
            loginCount = 0;
        }
        if (mustChangePassword == null) {
            mustChangePassword = false;
        }
        if (emailVerified == null) {
            emailVerified = false;
        }
        if (status == null) {
            status = "ACTIVE";
        }
    }
}
