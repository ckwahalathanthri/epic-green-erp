package lk.epicgreen.erp.report.repository;

import lk.epicgreen.erp.report.entity.SavedReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SavedReport entity
 * Based on ACTUAL database schema: saved_reports table
 * 
 * Fields: report_code (UNIQUE), report_name, report_category,
 *         report_type (ENUM: STANDARD, CUSTOM, SCHEDULED),
 *         query_template, parameters (JSON),
 *         output_format (ENUM: PDF, EXCEL, CSV, HTML),
 *         is_public, created_by (BIGINT), created_at, updated_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SavedReportRepository extends JpaRepository<SavedReport, Long>, JpaSpecificationExecutor<SavedReport> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find saved report by report code
     */
    Optional<SavedReport> findByReportCode(String reportCode);
    
    /**
     * Find saved reports by report category
     */
    List<SavedReport> findByReportCategory(String reportCategory);
    
    /**
     * Find saved reports by report category with pagination
     */
    Page<SavedReport> findByReportCategory(String reportCategory, Pageable pageable);
    
    /**
     * Find saved reports by report type
     */
    List<SavedReport> findByReportType(String reportType);
    
    /**
     * Find saved reports by report type with pagination
     */
    Page<SavedReport> findByReportType(String reportType, Pageable pageable);
    
    /**
     * Find saved reports by output format
     */
    List<SavedReport> findByOutputFormat(String outputFormat);
    
    /**
     * Find all public saved reports
     */
    List<SavedReport> findByIsPublicTrue();
    
    /**
     * Find all public saved reports with pagination
     */
    Page<SavedReport> findByIsPublicTrue(Pageable pageable);
    
    /**
     * Find all private saved reports
     */
    List<SavedReport> findByIsPublicFalse();
    
    /**
     * Find saved reports by created by user
     */
    List<SavedReport> findByCreatedBy(Long createdBy);
    
    /**
     * Find saved reports by created by user with pagination
     */
    Page<SavedReport> findByCreatedBy(Long createdBy, Pageable pageable);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if report code exists
     */
    boolean existsByReportCode(String reportCode);
    
    /**
     * Check if report code exists excluding specific report ID
     */
    boolean existsByReportCodeAndIdNot(String reportCode, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search saved reports by report code containing (case-insensitive)
     */
    Page<SavedReport> findByReportCodeContainingIgnoreCase(String reportCode, Pageable pageable);
    
    /**
     * Search saved reports by report name containing (case-insensitive)
     */
    Page<SavedReport> findByReportNameContainingIgnoreCase(String reportName, Pageable pageable);
    
    /**
     * Search public reports by keyword
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.isPublic = true AND " +
           "(LOWER(sr.reportCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sr.reportName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(sr.reportCategory) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<SavedReport> searchPublicReports(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search saved reports by multiple criteria
     */
    @Query("SELECT sr FROM SavedReport sr WHERE " +
           "(:reportCode IS NULL OR LOWER(sr.reportCode) LIKE LOWER(CONCAT('%', :reportCode, '%'))) AND " +
           "(:reportName IS NULL OR LOWER(sr.reportName) LIKE LOWER(CONCAT('%', :reportName, '%'))) AND " +
           "(:reportCategory IS NULL OR sr.reportCategory = :reportCategory) AND " +
           "(:reportType IS NULL OR sr.reportType = :reportType) AND " +
           "(:isPublic IS NULL OR sr.isPublic = :isPublic) AND " +
           "(:createdBy IS NULL OR sr.createdBy = :createdBy)")
    Page<SavedReport> searchSavedReports(
            @Param("reportCode") String reportCode,
            @Param("reportName") String reportName,
            @Param("reportCategory") String reportCategory,
            @Param("reportType") String reportType,
            @Param("isPublic") Boolean isPublic,
            @Param("createdBy") Long createdBy,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count public saved reports
     */
    long countByIsPublicTrue();
    
    /**
     * Count saved reports by report type
     */
    long countByReportType(String reportType);
    
    /**
     * Count saved reports by report category
     */
    long countByReportCategory(String reportCategory);
    
    /**
     * Count saved reports by created by user
     */
    long countByCreatedBy(Long createdBy);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find STANDARD reports
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.reportType = 'STANDARD' " +
           "ORDER BY sr.reportName")
    List<SavedReport> findStandardReports();
    
    /**
     * Find CUSTOM reports
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.reportType = 'CUSTOM' " +
           "ORDER BY sr.reportName")
    List<SavedReport> findCustomReports();
    
    /**
     * Find SCHEDULED reports
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.reportType = 'SCHEDULED' " +
           "ORDER BY sr.reportName")
    List<SavedReport> findScheduledReports();
    
    /**
     * Find reports by category and type
     */
    List<SavedReport> findByReportCategoryAndReportType(String reportCategory, String reportType);
    
    /**
     * Find reports by category and public status
     */
    List<SavedReport> findByReportCategoryAndIsPublic(String reportCategory, Boolean isPublic);
    
    /**
     * Find public reports by category
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.reportCategory = :reportCategory " +
           "AND sr.isPublic = true ORDER BY sr.reportName")
    List<SavedReport> findPublicReportsByCategory(@Param("reportCategory") String reportCategory);
    
    /**
     * Find public reports by type
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.reportType = :reportType " +
           "AND sr.isPublic = true ORDER BY sr.reportName")
    List<SavedReport> findPublicReportsByType(@Param("reportType") String reportType);
    
    /**
     * Find user's reports (created by user or public)
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.createdBy = :userId OR sr.isPublic = true " +
           "ORDER BY sr.reportName")
    List<SavedReport> findUserAccessibleReports(@Param("userId") Long userId);
    
    /**
     * Find user's reports with pagination
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.createdBy = :userId OR sr.isPublic = true " +
           "ORDER BY sr.reportName")
    Page<SavedReport> findUserAccessibleReports(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Find PDF reports
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.outputFormat = 'PDF' " +
           "ORDER BY sr.reportName")
    List<SavedReport> findPdfReports();
    
    /**
     * Find EXCEL reports
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.outputFormat = 'EXCEL' " +
           "ORDER BY sr.reportName")
    List<SavedReport> findExcelReports();
    
    /**
     * Find CSV reports
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.outputFormat = 'CSV' " +
           "ORDER BY sr.reportName")
    List<SavedReport> findCsvReports();
    
    /**
     * Find HTML reports
     */
    @Query("SELECT sr FROM SavedReport sr WHERE sr.outputFormat = 'HTML' " +
           "ORDER BY sr.reportName")
    List<SavedReport> findHtmlReports();
    
    /**
     * Get saved report statistics
     */
    @Query("SELECT " +
           "COUNT(sr) as totalReports, " +
           "SUM(CASE WHEN sr.reportType = 'STANDARD' THEN 1 ELSE 0 END) as standardReports, " +
           "SUM(CASE WHEN sr.reportType = 'CUSTOM' THEN 1 ELSE 0 END) as customReports, " +
           "SUM(CASE WHEN sr.reportType = 'SCHEDULED' THEN 1 ELSE 0 END) as scheduledReports, " +
           "SUM(CASE WHEN sr.isPublic = true THEN 1 ELSE 0 END) as publicReports, " +
           "SUM(CASE WHEN sr.isPublic = false THEN 1 ELSE 0 END) as privateReports " +
           "FROM SavedReport sr")
    Object getSavedReportStatistics();
    
    /**
     * Get saved reports grouped by report category
     */
    @Query("SELECT sr.reportCategory, COUNT(sr) as reportCount " +
           "FROM SavedReport sr GROUP BY sr.reportCategory ORDER BY reportCount DESC")
    List<Object[]> getSavedReportsByCategory();
    
    /**
     * Get saved reports grouped by report type
     */
    @Query("SELECT sr.reportType, COUNT(sr) as reportCount " +
           "FROM SavedReport sr GROUP BY sr.reportType ORDER BY reportCount DESC")
    List<Object[]> getSavedReportsByType();
    
    /**
     * Get saved reports grouped by output format
     */
    @Query("SELECT sr.outputFormat, COUNT(sr) as reportCount " +
           "FROM SavedReport sr GROUP BY sr.outputFormat ORDER BY reportCount DESC")
    List<Object[]> getSavedReportsByOutputFormat();
    
    /**
     * Get saved reports grouped by creator
     */
    @Query("SELECT sr.createdBy, COUNT(sr) as reportCount " +
           "FROM SavedReport sr GROUP BY sr.createdBy ORDER BY reportCount DESC")
    List<Object[]> getSavedReportsByCreator();
    
    /**
     * Find all saved reports ordered by report name
     */
    List<SavedReport> findAllByOrderByReportNameAsc();
    
    /**
     * Find all saved reports ordered by created at
     */
    List<SavedReport> findAllByOrderByCreatedAtDesc();
    
    /**
     * Find public reports ordered by report name
     */
    List<SavedReport> findByIsPublicTrueOrderByReportNameAsc();
    
    /**
     * Get most popular report categories
     */
    @Query("SELECT sr.reportCategory, COUNT(sr) as reportCount " +
           "FROM SavedReport sr WHERE sr.isPublic = true " +
           "GROUP BY sr.reportCategory ORDER BY reportCount DESC")
    List<Object[]> getMostPopularReportCategories();
}
