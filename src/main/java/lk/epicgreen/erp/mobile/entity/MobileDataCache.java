package lk.epicgreen.erp.mobile.entity;


import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * MobileDataCache entity
 * Represents cached data for offline mobile usage
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "mobile_data_cache",
    uniqueConstraints = @UniqueConstraint(name = "uk_user_cache_key", columnNames = {"user_id", "cache_key"}),
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_cache_type", columnList = "cache_type"),
        @Index(name = "idx_expires_at", columnList = "expires_at")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobileDataCache {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User reference
     */
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_mobile_data_cache_user"))
    private User user;
    
    /**
     * Cache key (unique per user)
     */
    @NotBlank(message = "Cache key is required")
    @Size(max = 100)
    @Column(name = "cache_key", nullable = false, length = 100)
    private String cacheKey;
    
    /**
     * Cache type (CUSTOMER, PRODUCT, PRICELIST, STOCK, ORDER, PAYMENT, OTHER)
     */
    @NotBlank(message = "Cache type is required")
    @Column(name = "cache_type", nullable = false, length = 20)
    private String cacheType;
    
    /**
     * Data snapshot (JSON)
     */
    @NotBlank(message = "Data snapshot is required")
    @Column(name = "data_snapshot", nullable = false, columnDefinition = "JSON")
    private String dataSnapshot;
    
    /**
     * Last synced timestamp
     */
    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    private Long UserId;
    
    /**
     * Expiry timestamp
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    /**
     * Cache type checks
     */
    @Transient
    public boolean isCustomer() {
        return "CUSTOMER".equals(cacheType);
    }
    
    @Transient
    public boolean isProduct() {
        return "PRODUCT".equals(cacheType);
    }
    
    @Transient
    public boolean isPriceList() {
        return "PRICELIST".equals(cacheType);
    }
    
    @Transient
    public boolean isStock() {
        return "STOCK".equals(cacheType);
    }
    
    @Transient
    public boolean isOrder() {
        return "ORDER".equals(cacheType);
    }
    
    @Transient
    public boolean isPayment() {
        return "PAYMENT".equals(cacheType);
    }
    
    /**
     * Check if expired
     */
    @Transient
    public boolean isExpired() {
        if (expiresAt == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Check if valid (not expired)
     */
    @Transient
    public boolean isValid() {
        return !isExpired();
    }
    
    /**
     * Get age in minutes
     */
    @Transient
    public long getAgeMinutes() {
        if (lastSyncedAt == null) {
            return 0;
        }
        return java.time.Duration.between(lastSyncedAt, LocalDateTime.now()).toMinutes();
    }
    
    /**
     * Get age in hours
     */
    @Transient
    public long getAgeHours() {
        if (lastSyncedAt == null) {
            return 0;
        }
        return java.time.Duration.between(lastSyncedAt, LocalDateTime.now()).toHours();
    }
    
    /**
     * Check if stale (older than threshold)
     */
    @Transient
    public boolean isStale(int thresholdMinutes) {
        return getAgeMinutes() > thresholdMinutes;
    }
    
    /**
     * Refresh cache data
     */
    public void refresh(String newData) {
        this.dataSnapshot = newData;
        this.lastSyncedAt = LocalDateTime.now();
    }
    
    /**
     * Refresh with new expiry
     */
    public void refresh(String newData, LocalDateTime newExpiry) {
        this.dataSnapshot = newData;
        this.lastSyncedAt = LocalDateTime.now();
        this.expiresAt = newExpiry;
    }
    
    /**
     * Set expiry (hours from now)
     */
    public void setExpiryHours(int hours) {
        this.expiresAt = LocalDateTime.now().plusHours(hours);
    }
    
    /**
     * Set expiry (days from now)
     */
    public void setExpiryDays(int days) {
        this.expiresAt = LocalDateTime.now().plusDays(days);
    }
    
    /**
     * Clear expiry
     */
    public void clearExpiry() {
        this.expiresAt = null;
    }
    
    /**
     * Get cache summary
     */
    @Transient
    public String getCacheSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(cacheType).append(" - ").append(cacheKey);
        if (lastSyncedAt != null) {
            summary.append(" (Age: ").append(getAgeMinutes()).append(" min)");
        }
        if (isExpired()) {
            summary.append(" - EXPIRED");
        }
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (lastSyncedAt == null) {
            lastSyncedAt = LocalDateTime.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MobileDataCache)) return false;
        MobileDataCache that = (MobileDataCache) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
