package lk.epicgreen.erp.accounting.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.time.LocalDate;

/**
 * FinancialPeriod entity
 * Represents accounting periods (monthly, quarterly, yearly)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "financial_periods", indexes = {
    @Index(name = "idx_period_code", columnList = "period_code"),
    @Index(name = "idx_period_dates", columnList = "start_date, end_date"),
    @Index(name = "idx_period_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialPeriod extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Period code (unique identifier, e.g., "2024-01", "2024-Q1", "FY2024")
     */
    @Column(name = "period_code", nullable = false, unique = true, length = 50)
    private String periodCode;
    
    /**
     * Period name (e.g., "January 2024", "Q1 2024", "Fiscal Year 2024")
     */
    @Column(name = "period_name", nullable = false, length = 100)
    private String periodName;
    
    /**
     * Period type (MONTHLY, QUARTERLY, YEARLY)
     */
    @Column(name = "period_type", nullable = false, length = 20)
    private String periodType;
    
    /**
     * Fiscal year (e.g., 2024)
     */
    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;
    
    /**
     * Period number (1-12 for monthly, 1-4 for quarterly, 1 for yearly)
     */
    @Column(name = "period_number")
    private Integer periodNumber;
    
    /**
     * Start date
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    /**
     * End date
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    /**
     * Status (OPEN, CLOSED, LOCKED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Is current period
     */
    @Column(name = "is_current")
    private Boolean isCurrent;
    
    /**
     * Closed by
     */
    @Column(name = "closed_by", length = 50)
    private String closedBy;
    
    /**
     * Closed date
     */
    @Column(name = "closed_date")
    private LocalDate closedDate;
    
    /**
     * Locked by
     */
    @Column(name = "locked_by", length = 50)
    private String lockedBy;
    
    /**
     * Locked date
     */
    @Column(name = "locked_date")
    private LocalDate lockedDate;
    
    /**
     * Description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Checks if period is open
     */
    @Transient
    public boolean isOpen() {
        return "OPEN".equals(status);
    }
    
    /**
     * Checks if period is closed
     */
    @Transient
    public boolean isClosed() {
        return "CLOSED".equals(status) || "LOCKED".equals(status);
    }
    
    /**
     * Checks if period is locked
     */
    @Transient
    public boolean isLocked() {
        return "LOCKED".equals(status);
    }
    
    /**
     * Checks if can post entries
     */
    @Transient
    public boolean canPostEntries() {
        return "OPEN".equals(status);
    }
    
    /**
     * Checks if period contains date
     */
    @Transient
    public boolean containsDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    /**
     * Gets duration in days
     */
    @Transient
    public long getDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    /**
     * Checks if is current date within period
     */
    @Transient
    public boolean isCurrentPeriod() {
        return containsDate(LocalDate.now());
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "OPEN";
        }
        if (isCurrent == null) {
            isCurrent = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Validate dates
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalStateException("Start date cannot be after end date");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinancialPeriod)) return false;
        FinancialPeriod that = (FinancialPeriod) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
