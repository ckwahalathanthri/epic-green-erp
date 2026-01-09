package lk.epicgreen.erp.accounting.entity;


import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * FinancialPeriod entity
 * Represents fiscal periods (months, quarters, years) for accounting
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "financial_periods", indexes = {
    @Index(name = "idx_period_code", columnList = "period_code"),
    @Index(name = "idx_fiscal_year", columnList = "fiscal_year"),
    @Index(name = "idx_period_dates", columnList = "start_date, end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialPeriod {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Period code (unique identifier, e.g., "2024-01", "2024-Q1", "FY2024")
     */
    @NotBlank(message = "Period code is required")
    @Size(max = 20)
    @Column(name = "period_code", nullable = false, unique = true, length = 20)
    private String periodCode;
    
    /**
     * Period name (e.g., "January 2024", "Q1 2024", "Fiscal Year 2024")
     */
    @NotBlank(message = "Period name is required")
    @Size(max = 100)
    @Column(name = "period_name", nullable = false, length = 100)
    private String periodName;
    
    /**
     * Period type (MONTH, QUARTER, YEAR)
     */
    @NotBlank(message = "Period type is required")
    @Column(name = "period_type", nullable = false, length = 10)
    private String periodType;
    
    /**
     * Start date
     */
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    /**
     * End date
     */
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    /**
     * Fiscal year
     */
    @NotNull(message = "Fiscal year is required")
    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;
    
    /**
     * Is closed (period locked for posting)
     */
    @Column(name = "is_closed")
    private Boolean isClosed;
    
    /**
     * Closed by (user who closed the period)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closed_by", foreignKey = @ForeignKey(name = "fk_financial_period_closed_by"))
    private User closedBy;
    
    /**
     * Closed timestamp
     */
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Created by (user ID)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * Period type checks
     */
    @Transient
    public boolean isMonthly() {
        return "MONTH".equals(periodType);
    }
    
    @Transient
    public boolean isQuarterly() {
        return "QUARTER".equals(periodType);
    }
    
    @Transient
    public boolean isYearly() {
        return "YEAR".equals(periodType);
    }
    
    /**
     * Check if closed
     */
    @Transient
    public boolean isClosed() {
        return Boolean.TRUE.equals(isClosed);
    }
    
    /**
     * Check if current period (today falls within period)
     */
    @Transient
    public boolean isCurrent() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
    
    /**
     * Check if future period
     */
    @Transient
    public boolean isFuture() {
        return LocalDate.now().isBefore(startDate);
    }
    
    /**
     * Check if past period
     */
    @Transient
    public boolean isPast() {
        return LocalDate.now().isAfter(endDate);
    }
    
    /**
     * Check if can post entries
     */
    @Transient
    public boolean canPost() {
        return !isClosed() && !isFuture();
    }
    
    /**
     * Get period duration in days
     */
    @Transient
    public long getDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    /**
     * Close period
     */
    public void close(User user) {
        if (isClosed()) {
            throw new IllegalStateException("Period is already closed");
        }
        this.isClosed = true;
        this.closedBy = user;
        this.closedAt = LocalDateTime.now();
    }
    
    /**
     * Reopen period
     */
    public void reopen() {
        if (!isClosed()) {
            throw new IllegalStateException("Period is not closed");
        }
        this.isClosed = false;
        this.closedBy = null;
        this.closedAt = null;
    }
    
    @PrePersist
    protected void onCreate() {
        if (isClosed == null) {
            isClosed = false;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
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
