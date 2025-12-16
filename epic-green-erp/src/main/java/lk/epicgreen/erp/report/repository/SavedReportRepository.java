package lk.epicgreen.erp.report.repository;

import lk.epicgreen.erp.report.entity.SavedReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SavedReport Repository
 * Repository for saved report data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SavedReportRepository extends JpaRepository<SavedReport, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find report by report code
     */
    Optional<SavedReport> findByReportCode(String reportCode);
    
    /**
     * Check if report exists by report code
     */
    boolean existsByReportCode(String reportCode);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find reports by report type
     */
    List<SavedReport> findByReportType(String reportType);
    
    /**
     * Find reports by report type with pagination
     */
    Page<SavedReport> findByReportType(String reportType, Pageable pageable);
    
    /**
     * Find reports by category
     */
    List<SavedReport> findByCategory(String category);
    
    /**
     * Find reports by category with pagination
     */
    Page<SavedReport> findByCategory(String category, Pageable pageable);
    
    /**
     * Find reports by status
     */
    List<SavedReport> findByStatus(String status);
    
    /**
     * Find reports by status with pagination
     */
    Page<SavedReport> findByStatus(String status, Pageable pageable);
    
    /**
     * Find reports by format
     */
    List<SavedReport> findByFormat(String format);
    
    /**
     * Find reports by format with pagination
     */
    Page<SavedReport> findByFormat(String format, Pageable pageable);
    
    /**
     * Find reports by created user
     */
    List<SavedReport> findByCreatedByUserId(Long userId);
    
    /**
     * Find reports by created user with pagination
     */
    Page<SavedReport> findByCreatedByUserId(Long userId, Pageable pageable);
    
    /**
     * Find reports by generated user
     */
    List<SavedReport> findByGeneratedByUserId(Long userId);
    
    /**
     * Find reports by generated user with pagination
     */
    Page<SavedReport> findByGeneratedByUserId(Long userId, Pageable pageable);
    
    /**
     * Find public reports
     */
    List<SavedReport> findByIsPublic(Boolean isPublic);
    
    /**
     * Find public reports with pagination
     */
    Page<SavedReport> findByIsPublic(Boolean isPublic, Pageable pageable);
    
    /**
     * Find scheduled reports
     */
    List<SavedReport> findByIsScheduled(Boolean isScheduled);
    
    /**
     * Find scheduled reports with pagination
     */
    Page<SavedReport> findByIsScheduled(Boolean isScheduled, Pageable pageable);
    
    /**
     * Find favorite reports
     */
    List<SavedReport> findByIsFavorite(Boolean isFavorite);
    
    /**
     * Find favorite reports with pagination
     */
    Page<SavedReport> findByIsFavorite(Boolean isFavorite, Pageable pageable);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find reports by generated time between dates
     */
    List<SavedReport> findByGeneratedTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find reports by generated time between dates with pagination
     */
    Page<SavedReport> findByGeneratedTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find reports by start date between dates
     */
    List<SavedReport> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find reports by end date between dates
     */
    List<SavedReport> findByEndDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find reports by report type and status
     */
    List<SavedReport> findByReportTypeAndStatus(String reportType, String status);
    
    /**
     * Find reports by report type and status with pagination
     */
    Page<SavedReport> findByReportTypeAndStatus(String reportType, String status, Pageable pageable);
    
    /**
     * Find reports by category and status
     */
    List<SavedReport> findByCategoryAndStatus(String category, String status);
    
    /**
     * Find reports by user and status
     */
    List<SavedReport> findByCreatedByUserIdAndStatus(Long userId, String status);
    
    /**
     * Find reports by user and report type
     */
    List<SavedReport> findByCreatedByUserIdAndReportType(Long userId, String reportType);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Find reports by report name pattern
     */
    @Query("SELECT r FROM SavedReport r WHERE LOWER(r.reportName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<SavedReport> findByReportNameContaining(@Param("name") String name);
    
    /**
     * Search reports
     */
    @Query("SELECT r FROM SavedReport r WHERE " +
           "LOWER(r.reportCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.reportName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SavedReport> searchReports(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find completed reports
     */
    @Query("SELECT r FROM SavedReport r WHERE r.status = 'COMPLETED' ORDER BY r.generatedTime DESC")
    List<SavedReport> findCompletedReports();
    
    /**
     * Find completed reports with pagination
     */
    @Query("SELECT r FROM SavedReport r WHERE r.status = 'COMPLETED' ORDER BY r.generatedTime DESC")
    Page<SavedReport> findCompletedReports(Pageable pageable);
    
    /**
     * Find recent reports
     */
    @Query("SELECT r FROM SavedReport r ORDER BY r.generatedTime DESC")
    List<SavedReport> findRecentReports(Pageable pageable);
    
    /**
     * Find user's recent reports
     */
    @Query("SELECT r FROM SavedReport r WHERE r.createdByUserId = :userId " +
           "ORDER BY r.generatedTime DESC")
    List<SavedReport> findUserRecentReports(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Find most generated reports
     */
    @Query("SELECT r.reportType, COUNT(r) as reportCount FROM SavedReport r " +
           "GROUP BY r.reportType ORDER BY reportCount DESC")
    List<Object[]> findMostGeneratedReports();
    
    /**
     * Find scheduled reports due for execution
     */
    @Query("SELECT r FROM SavedReport r WHERE r.isScheduled = true " +
           "AND r.scheduleFrequency IS NOT NULL " +
           "AND (r.lastScheduledRun IS NULL OR r.nextScheduledRun <= :now) " +
           "ORDER BY r.nextScheduledRun ASC")
    List<SavedReport> findScheduledReportsDue(@Param("now") LocalDateTime now);
    
    /**
     * Find scheduled reports due for execution with limit
     */
    @Query("SELECT r FROM SavedReport r WHERE r.isScheduled = true " +
           "AND r.scheduleFrequency IS NOT NULL " +
           "AND (r.lastScheduledRun IS NULL OR r.nextScheduledRun <= :now) " +
           "ORDER BY r.nextScheduledRun ASC")
    List<SavedReport> findScheduledReportsDue(@Param("now") LocalDateTime now, Pageable pageable);
    
    /**
     * Find user's favorite reports
     */
    @Query("SELECT r FROM SavedReport r WHERE r.createdByUserId = :userId " +
           "AND r.isFavorite = true ORDER BY r.reportName ASC")
    List<SavedReport> findUserFavoriteReports(@Param("userId") Long userId);
    
    /**
     * Find public reports
     */
    @Query("SELECT r FROM SavedReport r WHERE r.isPublic = true AND r.status = 'COMPLETED' " +
           "ORDER BY r.generatedTime DESC")
    List<SavedReport> findPublicReports();
    
    /**
     * Find public reports with pagination
     */
    @Query("SELECT r FROM SavedReport r WHERE r.isPublic = true AND r.status = 'COMPLETED' " +
           "ORDER BY r.generatedTime DESC")
    Page<SavedReport> findPublicReports(Pageable pageable);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count reports by report type
     */
    @Query("SELECT COUNT(r) FROM SavedReport r WHERE r.reportType = :reportType")
    Long countByReportType(@Param("reportType") String reportType);
    
    /**
     * Count reports by category
     */
    @Query("SELECT COUNT(r) FROM SavedReport r WHERE r.category = :category")
    Long countByCategory(@Param("category") String category);
    
    /**
     * Count reports by status
     */
    @Query("SELECT COUNT(r) FROM SavedReport r WHERE r.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count user's reports
     */
    @Query("SELECT COUNT(r) FROM SavedReport r WHERE r.createdByUserId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * Get report type distribution
     */
    @Query("SELECT r.reportType, COUNT(r) as reportCount FROM SavedReport r " +
           "GROUP BY r.reportType ORDER BY reportCount DESC")
    List<Object[]> getReportTypeDistribution();
    
    /**
     * Get category distribution
     */
    @Query("SELECT r.category, COUNT(r) as reportCount FROM SavedReport r " +
           "GROUP BY r.category ORDER BY reportCount DESC")
    List<Object[]> getCategoryDistribution();
    
    /**
     * Get format distribution
     */
    @Query("SELECT r.format, COUNT(r) as reportCount FROM SavedReport r " +
           "GROUP BY r.format ORDER BY reportCount DESC")
    List<Object[]> getFormatDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT r.status, COUNT(r) as reportCount FROM SavedReport r " +
           "GROUP BY r.status ORDER BY reportCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get daily report count
     */
    @Query("SELECT DATE(r.generatedTime) as date, COUNT(r) as reportCount FROM SavedReport r " +
           "WHERE r.generatedTime BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(r.generatedTime) ORDER BY date")
    List<Object[]> getDailyReportCount(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get most active users
     */
    @Query("SELECT r.createdByUsername, COUNT(r) as reportCount FROM SavedReport r " +
           "WHERE r.createdByUsername IS NOT NULL " +
           "GROUP BY r.createdByUsername ORDER BY reportCount DESC")
    List<Object[]> getMostActiveUsers();
    
    /**
     * Get average generation time
     */
    @Query("SELECT AVG(r.generationTimeMillis) FROM SavedReport r " +
           "WHERE r.generationTimeMillis IS NOT NULL")
    Double getAverageGenerationTime();
    
    /**
     * Get average file size
     */
    @Query("SELECT AVG(r.fileSizeBytes) FROM SavedReport r " +
           "WHERE r.fileSizeBytes IS NOT NULL")
    Double getAverageFileSize();
    
    /**
     * Find reports by tags
     */
    @Query("SELECT r FROM SavedReport r WHERE r.tags LIKE CONCAT('%', :tag, '%')")
    List<SavedReport> findByTag(@Param("tag") String tag);
    
    /**
     * Get total file size by user
     */
    @Query("SELECT SUM(r.fileSizeBytes) FROM SavedReport r WHERE r.createdByUserId = :userId")
    Long getTotalFileSizeByUser(@Param("userId") Long userId);
    
    /**
     * Get total file size
     */
    @Query("SELECT SUM(r.fileSizeBytes) FROM SavedReport r WHERE r.fileSizeBytes IS NOT NULL")
    Long getTotalFileSize();
    
    /**
     * Delete old reports
     */
    @Query("DELETE FROM SavedReport r WHERE r.status = 'COMPLETED' " +
           "AND r.generatedTime < :beforeDate")
    void deleteOldReports(@Param("beforeDate") LocalDateTime beforeDate);
}
