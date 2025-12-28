package lk.epicgreen.erp.report.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.admin.entity.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ReportExecutionHistory entity
 * Tracks report execution history and performance
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "report_execution_history", indexes = {
    @Index(name = "idx_report_id", columnList = "report_id"),
    @Index(name = "idx_executed_by", columnList = "executed_by"),
    @Index(name = "idx_executed_at", columnList = "executed_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportExecutionHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Report reference
     */
    @NotNull(message = "Report is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false, foreignKey = @ForeignKey(name = "fk_report_execution_report"))
    private SavedReport report;
    
    /**
     * Executed by (user)
     */
    @NotNull(message = "Executed by is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executed_by", nullable = false, foreignKey = @ForeignKey(name = "fk_report_execution_executed_by"))
    private User executedBy;
    
    /**
     * Parameters used (JSON)
     */
    @Column(name = "parameters_used", columnDefinition = "JSON")
    private String parametersUsed;
    
    /**
     * Execution time in milliseconds
     */
    @PositiveOrZero(message = "Execution time must be positive or zero")
    @Column(name = "execution_time_ms")
    private Integer executionTimeMs;
    
    /**
     * Output format (PDF, EXCEL, CSV, HTML)
     */
    @Size(max = 20)
    @Column(name = "output_format", length = 20)
    private String outputFormat;
    
    /**
     * Output file path (where file is stored)
     */
    @Size(max = 500)
    @Column(name = "output_file_path", length = 500)
    private String outputFilePath;
    
    /**
     * Status (RUNNING, COMPLETED, FAILED)
     */
    @NotNull(message = "Status is required")
    @Column(name = "status", nullable = false, length = 10)
    private String status;
    
    /**
     * Error message (if failed)
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * Executed timestamp
     */
    @Column(name = "executed_at")
    private LocalDateTime executedAt;
    
    /**
     * Status checks
     */
    @Transient
    public boolean isRunning() {
        return "RUNNING".equals(status);
    }
    
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    @Transient
    public boolean isFailed() {
        return "FAILED".equals(status);
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
     * Check if has parameters
     */
    @Transient
    public boolean hasParameters() {
        return parametersUsed != null && !parametersUsed.isEmpty();
    }
    
    /**
     * Check if has output file
     */
    @Transient
    public boolean hasOutputFile() {
        return outputFilePath != null && !outputFilePath.isEmpty();
    }
    
    /**
     * Get execution time in seconds
     */
    @Transient
    public double getExecutionTimeSeconds() {
        if (executionTimeMs == null) {
            return 0;
        }
        return executionTimeMs / 1000.0;
    }
    
    /**
     * Check if slow (> 5 seconds)
     */
    @Transient
    public boolean isSlow() {
        return executionTimeMs != null && executionTimeMs > 5000;
    }
    
    /**
     * Check if very slow (> 30 seconds)
     */
    @Transient
    public boolean isVerySlow() {
        return executionTimeMs != null && executionTimeMs > 30000;
    }
    
    /**
     * Get age in hours
     */
    @Transient
    public long getAgeHours() {
        if (executedAt == null) {
            return 0;
        }
        return java.time.Duration.between(executedAt, LocalDateTime.now()).toHours();
    }
    
    /**
     * Get age in days
     */
    @Transient
    public long getAgeDays() {
        if (executedAt == null) {
            return 0;
        }
        return java.time.Duration.between(executedAt, LocalDateTime.now()).toDays();
    }
    
    /**
     * Check if recent (less than 24 hours)
     */
    @Transient
    public boolean isRecent() {
        return getAgeHours() < 24;
    }
    
    /**
     * Start execution
     */
    public void start() {
        this.status = "RUNNING";
        this.executedAt = LocalDateTime.now();
    }
    
    /**
     * Complete execution
     */
    public void complete(String filePath, int executionTime) {
        this.status = "COMPLETED";
        this.outputFilePath = filePath;
        this.executionTimeMs = executionTime;
    }
    
    /**
     * Mark as failed
     */
    public void fail(String error, int executionTime) {
        this.status = "FAILED";
        this.errorMessage = error;
        this.executionTimeMs = executionTime;
    }
    
    /**
     * Get execution summary
     */
    @Transient
    public String getExecutionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(report.getReportName());
        summary.append(" - ").append(status);
        
        if (isCompleted()) {
            summary.append(" (").append(getExecutionTimeSeconds()).append("s)");
            if (outputFormat != null) {
                summary.append(" - ").append(outputFormat);
            }
        } else if (isFailed()) {
            summary.append(" - ERROR");
        }
        
        return summary.toString();
    }
    
    /**
     * Get performance rating
     */
    @Transient
    public String getPerformanceRating() {
        if (executionTimeMs == null || !isCompleted()) {
            return "N/A";
        }
        
        if (executionTimeMs < 1000) {
            return "EXCELLENT";
        } else if (executionTimeMs < 5000) {
            return "GOOD";
        } else if (executionTimeMs < 30000) {
            return "FAIR";
        } else {
            return "POOR";
        }
    }
    
    @PrePersist
    protected void onCreate() {
        if (executedAt == null) {
            executedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "RUNNING";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportExecutionHistory)) return false;
        ReportExecutionHistory that = (ReportExecutionHistory) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
