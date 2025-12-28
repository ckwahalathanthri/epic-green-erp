package lk.epicgreen.erp.report.repository;

import lk.epicgreen.erp.report.entity.ReportExecutionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ReportExecutionHistory entity
 * Based on ACTUAL database schema: report_execution_history table
 * 
 * Fields: report_id (BIGINT), executed_by (BIGINT), parameters_used (JSON),
 *         execution_time_ms, output_format, output_file_path,
 *         status (ENUM: RUNNING, COMPLETED, FAILED),
 *         error_message, executed_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ReportExecutionHistoryRepository extends JpaRepository<ReportExecutionHistory, Long>, JpaSpecificationExecutor<ReportExecutionHistory> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find report execution history by report
     */
    List<ReportExecutionHistory> findByReportId(Long reportId);
    
    /**
     * Find report execution history by report with pagination
     */
    Page<ReportExecutionHistory> findByReportId(Long reportId, Pageable pageable);
    
    /**
     * Find report execution history by executed by user
     */
    List<ReportExecutionHistory> findByExecutedBy(Long executedBy);
    
    /**
     * Find report execution history by executed by user with pagination
     */
    Page<ReportExecutionHistory> findByExecutedBy(Long executedBy, Pageable pageable);
    
    /**
     * Find report execution history by status
     */
    List<ReportExecutionHistory> findByStatus(String status);
    
    /**
     * Find report execution history by status with pagination
     */
    Page<ReportExecutionHistory> findByStatus(String status, Pageable pageable);
    
    /**
     * Find report execution history by output format
     */
    List<ReportExecutionHistory> findByOutputFormat(String outputFormat);
    
    /**
     * Find report execution history by executed at time range
     */
    List<ReportExecutionHistory> findByExecutedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find report execution history by executed at time range with pagination
     */
    Page<ReportExecutionHistory> findByExecutedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search report execution history by output file path containing (case-insensitive)
     */
    Page<ReportExecutionHistory> findByOutputFilePathContainingIgnoreCase(String outputFilePath, Pageable pageable);
    
    /**
     * Search report execution history by multiple criteria
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE " +
           "(:reportId IS NULL OR reh.reportId = :reportId) AND " +
           "(:executedBy IS NULL OR reh.executedBy = :executedBy) AND " +
           "(:status IS NULL OR reh.status = :status) AND " +
           "(:outputFormat IS NULL OR reh.outputFormat = :outputFormat) AND " +
           "(:startTime IS NULL OR reh.executedAt >= :startTime) AND " +
           "(:endTime IS NULL OR reh.executedAt <= :endTime)")
    Page<ReportExecutionHistory> searchReportExecutionHistory(
            @Param("reportId") Long reportId,
            @Param("executedBy") Long executedBy,
            @Param("status") String status,
            @Param("outputFormat") String outputFormat,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count report execution history by report
     */
    long countByReportId(Long reportId);
    
    /**
     * Count report execution history by executed by user
     */
    long countByExecutedBy(Long executedBy);
    
    /**
     * Count report execution history by status
     */
    long countByStatus(String status);
    
    /**
     * Count report execution history in time range
     */
    long countByExecutedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // ==================== DELETE METHODS ====================
    
    /**
     * Delete all completed execution history
     */
    @Modifying
    @Query("DELETE FROM ReportExecutionHistory reh WHERE reh.status = 'COMPLETED'")
    void deleteAllCompleted();
    
    /**
     * Delete completed execution history older than specified date
     */
    @Modifying
    @Query("DELETE FROM ReportExecutionHistory reh WHERE reh.status = 'COMPLETED' " +
           "AND reh.executedAt < :cutoffDate")
    void deleteCompletedBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    /**
     * Delete failed execution history older than specified date
     */
    @Modifying
    @Query("DELETE FROM ReportExecutionHistory reh WHERE reh.status = 'FAILED' " +
           "AND reh.executedAt < :cutoffDate")
    void deleteFailedBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find running report executions
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.status = 'RUNNING' " +
           "ORDER BY reh.executedAt DESC")
    List<ReportExecutionHistory> findRunningReportExecutions();
    
    /**
     * Find completed report executions
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.status = 'COMPLETED' " +
           "ORDER BY reh.executedAt DESC")
    List<ReportExecutionHistory> findCompletedReportExecutions();
    
    /**
     * Find failed report executions
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.status = 'FAILED' " +
           "ORDER BY reh.executedAt DESC")
    List<ReportExecutionHistory> findFailedReportExecutions();
    
    /**
     * Find report execution history by report and status
     */
    List<ReportExecutionHistory> findByReportIdAndStatus(Long reportId, String status);
    
    /**
     * Find report execution history by user and status
     */
    List<ReportExecutionHistory> findByExecutedByAndStatus(Long executedBy, String status);
    
    /**
     * Find recent execution history by report
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.reportId = :reportId " +
           "ORDER BY reh.executedAt DESC")
    List<ReportExecutionHistory> findRecentExecutionsByReport(@Param("reportId") Long reportId);
    
    /**
     * Find recent execution history by report with pagination
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.reportId = :reportId " +
           "ORDER BY reh.executedAt DESC")
    Page<ReportExecutionHistory> findRecentExecutionsByReport(@Param("reportId") Long reportId, Pageable pageable);
    
    /**
     * Find latest execution by report
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.reportId = :reportId " +
           "ORDER BY reh.executedAt DESC LIMIT 1")
    ReportExecutionHistory findLatestExecutionByReport(@Param("reportId") Long reportId);
    
    /**
     * Find latest successful execution by report
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.reportId = :reportId " +
           "AND reh.status = 'COMPLETED' ORDER BY reh.executedAt DESC LIMIT 1")
    ReportExecutionHistory findLatestSuccessfulExecutionByReport(@Param("reportId") Long reportId);
    
    /**
     * Find slow executions (execution time > threshold)
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.executionTimeMs > :thresholdMs " +
           "ORDER BY reh.executionTimeMs DESC")
    List<ReportExecutionHistory> findSlowExecutions(@Param("thresholdMs") Integer thresholdMs);
    
    /**
     * Get report execution history statistics
     */
    @Query("SELECT " +
           "COUNT(reh) as totalExecutions, " +
           "SUM(CASE WHEN reh.status = 'RUNNING' THEN 1 ELSE 0 END) as runningExecutions, " +
           "SUM(CASE WHEN reh.status = 'COMPLETED' THEN 1 ELSE 0 END) as completedExecutions, " +
           "SUM(CASE WHEN reh.status = 'FAILED' THEN 1 ELSE 0 END) as failedExecutions, " +
           "AVG(reh.executionTimeMs) as avgExecutionTime, " +
           "MAX(reh.executionTimeMs) as maxExecutionTime, " +
           "MIN(reh.executionTimeMs) as minExecutionTime " +
           "FROM ReportExecutionHistory reh")
    Object getReportExecutionHistoryStatistics();
    
    /**
     * Get report execution history statistics by report
     */
    @Query("SELECT " +
           "COUNT(reh) as totalExecutions, " +
           "SUM(CASE WHEN reh.status = 'COMPLETED' THEN 1 ELSE 0 END) as completedExecutions, " +
           "SUM(CASE WHEN reh.status = 'FAILED' THEN 1 ELSE 0 END) as failedExecutions, " +
           "AVG(reh.executionTimeMs) as avgExecutionTime " +
           "FROM ReportExecutionHistory reh WHERE reh.reportId = :reportId")
    Object getReportExecutionStatisticsByReport(@Param("reportId") Long reportId);
    
    /**
     * Get report execution history grouped by status
     */
    @Query("SELECT reh.status, COUNT(reh) as executionCount " +
           "FROM ReportExecutionHistory reh GROUP BY reh.status ORDER BY executionCount DESC")
    List<Object[]> getReportExecutionHistoryByStatus();
    
    /**
     * Get report execution history grouped by output format
     */
    @Query("SELECT reh.outputFormat, COUNT(reh) as executionCount " +
           "FROM ReportExecutionHistory reh GROUP BY reh.outputFormat ORDER BY executionCount DESC")
    List<Object[]> getReportExecutionHistoryByOutputFormat();
    
    /**
     * Get daily report execution summary
     */
    @Query("SELECT DATE(reh.executedAt) as executionDate, COUNT(reh) as executionCount, " +
           "SUM(CASE WHEN reh.status = 'COMPLETED' THEN 1 ELSE 0 END) as completedCount, " +
           "SUM(CASE WHEN reh.status = 'FAILED' THEN 1 ELSE 0 END) as failedCount, " +
           "AVG(reh.executionTimeMs) as avgExecutionTime " +
           "FROM ReportExecutionHistory reh WHERE reh.executedAt BETWEEN :startTime AND :endTime " +
           "GROUP BY DATE(reh.executedAt) ORDER BY executionDate DESC")
    List<Object[]> getDailyReportExecutionSummary(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Get hourly report execution summary
     */
    @Query("SELECT HOUR(reh.executedAt) as executionHour, COUNT(reh) as executionCount " +
           "FROM ReportExecutionHistory reh WHERE DATE(reh.executedAt) = CURRENT_DATE " +
           "GROUP BY HOUR(reh.executedAt) ORDER BY executionHour")
    List<Object[]> getHourlyReportExecutionSummary();
    
    /**
     * Get most executed reports
     */
    @Query("SELECT reh.reportId, COUNT(reh) as executionCount " +
           "FROM ReportExecutionHistory reh WHERE reh.executedAt >= :sinceTime " +
           "GROUP BY reh.reportId ORDER BY executionCount DESC")
    List<Object[]> getMostExecutedReports(@Param("sinceTime") LocalDateTime sinceTime);
    
    /**
     * Get most active users (by report execution)
     */
    @Query("SELECT reh.executedBy, COUNT(reh) as executionCount " +
           "FROM ReportExecutionHistory reh WHERE reh.executedAt >= :sinceTime " +
           "GROUP BY reh.executedBy ORDER BY executionCount DESC")
    List<Object[]> getMostActiveUsers(@Param("sinceTime") LocalDateTime sinceTime);
    
    /**
     * Get average execution time by report
     */
    @Query("SELECT reh.reportId, AVG(reh.executionTimeMs) as avgExecutionTime, " +
           "COUNT(reh) as executionCount " +
           "FROM ReportExecutionHistory reh WHERE reh.status = 'COMPLETED' " +
           "GROUP BY reh.reportId ORDER BY avgExecutionTime DESC")
    List<Object[]> getAverageExecutionTimeByReport();
    
    /**
     * Find today's report executions
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE DATE(reh.executedAt) = CURRENT_DATE " +
           "ORDER BY reh.executedAt DESC")
    List<ReportExecutionHistory> findTodayReportExecutions();
    
    /**
     * Find all report execution history ordered by executed at
     */
    List<ReportExecutionHistory> findAllByOrderByExecutedAtDesc();
    
    /**
     * Get execution success rate
     */
    @Query("SELECT " +
           "COUNT(reh) as totalExecutions, " +
           "SUM(CASE WHEN reh.status = 'COMPLETED' THEN 1 ELSE 0 END) as completedExecutions, " +
           "(SUM(CASE WHEN reh.status = 'COMPLETED' THEN 1 ELSE 0 END) * 100.0 / COUNT(reh)) as successRate " +
           "FROM ReportExecutionHistory reh WHERE reh.executedAt >= :sinceTime")
    Object getExecutionSuccessRate(@Param("sinceTime") LocalDateTime sinceTime);
    
    /**
     * Find long running executions (still running and started more than threshold ago)
     */
    @Query("SELECT reh FROM ReportExecutionHistory reh WHERE reh.status = 'RUNNING' " +
           "AND reh.executedAt < :thresholdTime ORDER BY reh.executedAt")
    List<ReportExecutionHistory> findLongRunningExecutions(@Param("thresholdTime") LocalDateTime thresholdTime);
}
