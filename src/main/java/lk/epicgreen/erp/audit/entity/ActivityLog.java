package lk.epicgreen.erp.audit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ActivityLog entity
 * Tracks user activities and interactions within the system
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "activity_logs", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_module", columnList = "module"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User reference
     */
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_activity_log_user"))
    private User user;
    
    /**
     * Activity type (e.g., SEARCH, EXPORT, REPORT, NAVIGATION)
     */
    @NotBlank(message = "Activity type is required")
    @Size(max = 50)
    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;
    
    /**
     * Module (e.g., SALES, INVENTORY, CUSTOMER)
     */
    @NotBlank(message = "Module is required")
    @Size(max = 50)
    @Column(name = "module", nullable = false, length = 50)
    private String module;
    
    /**
     * Activity description
     */
    @Column(name = "activity_description", columnDefinition = "TEXT")
    private String activityDescription;
    
    /**
     * Reference type (e.g., SALES_ORDER, INVOICE)
     */
    @Size(max = 50)
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    
    /**
     * Reference ID (record ID)
     */
    @Column(name = "reference_id")
    private Long referenceId;
    
    /**
     * IP address
     */
    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    /**
     * Device type (WEB, MOBILE_ANDROID, MOBILE_IOS, API)
     */
    @NotBlank(message = "Device type is required")
    @Column(name = "device_type", nullable = false, length = 20)
    private String deviceType;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Activity type checks
     */
    @Transient
    public boolean isSearch() {
        return "SEARCH".equals(activityType);
    }
    
    @Transient
    public boolean isExport() {
        return "EXPORT".equals(activityType);
    }
    
    @Transient
    public boolean isReport() {
        return "REPORT".equals(activityType);
    }
    
    @Transient
    public boolean isNavigation() {
        return "NAVIGATION".equals(activityType);
    }
    
    @Transient
    public boolean isPrint() {
        return "PRINT".equals(activityType);
    }
    
    @Transient
    public boolean isDownload() {
        return "DOWNLOAD".equals(activityType);
    }
    
    /**
     * Device type checks
     */
    @Transient
    public boolean isWeb() {
        return "WEB".equals(deviceType);
    }
    
    @Transient
    public boolean isMobileAndroid() {
        return "MOBILE_ANDROID".equals(deviceType);
    }
    
    @Transient
    public boolean isMobileIOS() {
        return "MOBILE_IOS".equals(deviceType);
    }
    
    @Transient
    public boolean isAPI() {
        return "API".equals(deviceType);
    }
    
    @Transient
    public boolean isMobile() {
        return isMobileAndroid() || isMobileIOS();
    }
    
    /**
     * Module checks
     */
    @Transient
    public boolean isSalesModule() {
        return "SALES".equals(module);
    }
    
    @Transient
    public boolean isInventoryModule() {
        return "INVENTORY".equals(module);
    }
    
    @Transient
    public boolean isCustomerModule() {
        return "CUSTOMER".equals(module);
    }
    
    @Transient
    public boolean isProductionModule() {
        return "PRODUCTION".equals(module);
    }
    
    @Transient
    public boolean isAccountingModule() {
        return "ACCOUNTING".equals(module);
    }
    
    /**
     * Check if has reference
     */
    @Transient
    public boolean hasReference() {
        return referenceType != null && referenceId != null;
    }
    
    /**
     * Get activity summary
     */
    @Transient
    public String getActivitySummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(module).append(" - ").append(activityType);
        if (activityDescription != null) {
            summary.append(": ").append(activityDescription);
        }
        if (hasReference()) {
            summary.append(" (").append(referenceType).append(" #").append(referenceId).append(")");
        }
        summary.append(" - ").append(deviceType);
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Activity logs should be immutable after creation
        throw new IllegalStateException("Activity logs cannot be modified after creation");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityLog)) return false;
        ActivityLog that = (ActivityLog) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
