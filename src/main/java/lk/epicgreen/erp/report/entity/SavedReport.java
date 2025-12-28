package lk.epicgreen.erp.report.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SavedReport entity
 * Represents saved report definitions and configurations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "saved_reports", indexes = {
    @Index(name = "idx_report_code", columnList = "report_code"),
    @Index(name = "idx_report_category", columnList = "report_category"),
    @Index(name = "idx_created_by", columnList = "created_by")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Report code (unique identifier)
     */
    @NotBlank(message = "Report code is required")
    @Size(max = 50)
    @Column(name = "report_code", nullable = false, unique = true, length = 50)
    private String reportCode;
    
    /**
     * Report name (display name)
     */
    @NotBlank(message = "Report name is required")
    @Size(max = 200)
    @Column(name = "report_name", nullable = false, length = 200)
    private String reportName;
    
    /**
     * Report category (e.g., SALES, INVENTORY, ACCOUNTING, PRODUCTION)
     */
    @NotBlank(message = "Report category is required")
    @Size(max = 50)
    @Column(name = "report_category", nullable = false, length = 50)
    private String reportCategory;
    
    /**
     * Report type (STANDARD, CUSTOM, SCHEDULED)
     */
    @NotBlank(message = "Report type is required")
    @Column(name = "report_type", nullable = false, length = 10)
    private String reportType;
    
    /**
     * Query template (SQL or query definition)
     */
    @Column(name = "query_template", columnDefinition = "TEXT")
    private String queryTemplate;
    
    /**
     * Parameters (JSON array of parameter definitions)
     */
    @Column(name = "parameters", columnDefinition = "JSON")
    private String parameters;
    
    /**
     * Output format (PDF, EXCEL, CSV, HTML)
     */
    @Column(name = "output_format", length = 10)
    private String outputFormat;
    
    /**
     * Is public (accessible by all users)
     */
    @Column(name = "is_public")
    private Boolean isPublic;
    
    /**
     * Created by (user)
     */
    @NotNull(message = "Created by is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, foreignKey = @ForeignKey(name = "fk_saved_report_created_by"))
    private User createdBy;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Updated timestamp
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Execution history
     */
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @OrderBy("executedAt DESC")
    @Builder.Default
    private List<ReportExecutionHistory> executionHistory = new ArrayList<>();
    
    /**
     * Report type checks
     */
    @Transient
    public boolean isStandard() {
        return "STANDARD".equals(reportType);
    }
    
    @Transient
    public boolean isCustom() {
        return "CUSTOM".equals(reportType);
    }
    
    @Transient
    public boolean isScheduled() {
        return "SCHEDULED".equals(reportType);
    }
    
    /**
     * Output format checks
     */
    @Transient
    public boolean isPdf() {
        return "PDF".equals(outputFormat);
    }
    
    @Transient
    public boolean isExcel() {
        return "EXCEL".equals(outputFormat);
    }
    
    @Transient
    public boolean isCsv() {
        return "CSV".equals(outputFormat);
    }
    
    @Transient
    public boolean isHtml() {
        return "HTML".equals(outputFormat);
    }
    
    /**
     * Category checks
     */
    @Transient
    public boolean isSalesReport() {
        return "SALES".equals(reportCategory);
    }
    
    @Transient
    public boolean isInventoryReport() {
        return "INVENTORY".equals(reportCategory);
    }
    
    @Transient
    public boolean isAccountingReport() {
        return "ACCOUNTING".equals(reportCategory);
    }
    
    @Transient
    public boolean isProductionReport() {
        return "PRODUCTION".equals(reportCategory);
    }
    
    @Transient
    public boolean isCustomerReport() {
        return "CUSTOMER".equals(reportCategory);
    }
    
    @Transient
    public boolean isFinancialReport() {
        return "FINANCIAL".equals(reportCategory);
    }
    
    /**
     * Check if public
     */
    @Transient
    public boolean isPublic() {
        return Boolean.TRUE.equals(isPublic);
    }
    
    /**
     * Check if private
     */
    @Transient
    public boolean isPrivate() {
        return !isPublic();
    }
    
    /**
     * Check if has parameters
     */
    @Transient
    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }
    
    /**
     * Check if has query template
     */
    @Transient
    public boolean hasQueryTemplate() {
        return queryTemplate != null && !queryTemplate.isEmpty();
    }
    
    /**
     * Check if user can access
     */
    @Transient
    public boolean canAccess(User user) {
        if (isPublic()) {
            return true;
        }
        return createdBy != null && createdBy.equals(user);
    }
    
    /**
     * Get last execution
     */
    @Transient
    public ReportExecutionHistory getLastExecution() {
        if (executionHistory == null || executionHistory.isEmpty()) {
            return null;
        }
        return executionHistory.get(0);
    }
    
    /**
     * Get execution count
     */
    @Transient
    public int getExecutionCount() {
        if (executionHistory == null) {
            return 0;
        }
        return executionHistory.size();
    }
    
    /**
     * Get successful execution count
     */
    @Transient
    public long getSuccessfulExecutionCount() {
        if (executionHistory == null) {
            return 0;
        }
        return executionHistory.stream()
                .filter(ReportExecutionHistory::isCompleted)
                .count();
    }
    
    /**
     * Get failed execution count
     */
    @Transient
    public long getFailedExecutionCount() {
        if (executionHistory == null) {
            return 0;
        }
        return executionHistory.stream()
                .filter(ReportExecutionHistory::isFailed)
                .count();
    }
    
    /**
     * Get average execution time (ms)
     */
    @Transient
    public double getAverageExecutionTime() {
        if (executionHistory == null || executionHistory.isEmpty()) {
            return 0;
        }
        
        return executionHistory.stream()
                .filter(ReportExecutionHistory::isCompleted)
                .mapToInt(h -> h.getExecutionTimeMs() != null ? h.getExecutionTimeMs() : 0)
                .average()
                .orElse(0);
    }
    
    /**
     * Make public
     */
    public void makePublic() {
        this.isPublic = true;
    }
    
    /**
     * Make private
     */
    public void makePrivate() {
        this.isPublic = false;
    }
    
    /**
     * Get report summary
     */
    @Transient
    public String getReportSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(reportName);
        summary.append(" (").append(reportCategory).append(")");
        summary.append(" - ").append(reportType);
        summary.append(" - ").append(outputFormat);
        
        if (isPublic()) {
            summary.append(" - PUBLIC");
        } else {
            summary.append(" - PRIVATE");
        }
        
        return summary.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (isPublic == null) {
            isPublic = false;
        }
        if (outputFormat == null) {
            outputFormat = "PDF";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavedReport)) return false;
        SavedReport that = (SavedReport) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
