package lk.epicgreen.erp.report.repository;

import lk.epicgreen.erp.report.entity.SavedReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SavedReport entity
 * Based on ACTUAL database schema: saved_reports table
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SavedReportRepository extends JpaRepository<SavedReport, Long>, JpaSpecificationExecutor<SavedReport> {
    
    // ==================== FINDER METHODS ====================
    
    Optional<SavedReport> findByReportCode(String reportCode);
    
    List<SavedReport> findByReportCategory(String category);
    
    List<SavedReport> findByReportType(String type);
    
    List<SavedReport> findByIsPublicTrue();

    List<SavedReport> findByIsPublicFalse();
    
    List<SavedReport> findByCreatedById(Long userId);
    
    List<SavedReport> findTop10ByOrderByCreatedAtDesc();
    
    List<SavedReport> findByOutputFormat(String outputFormat);
    
    // ==================== EXISTENCE CHECKS ====================
    
    boolean existsByReportCode(String reportCode);
    
    boolean existsByReportCodeAndIdNot(String reportCode, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    Page<SavedReport> findByReportNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT r FROM SavedReport r WHERE " +
           "(:name IS NULL OR LOWER(r.reportName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR r.reportCategory = :category) AND " +
           "(:type IS NULL OR r.reportType = :type) AND " +
           "(:isPublic IS NULL OR r.isPublic = :isPublic) AND " +
           "(:createdBy IS NULL OR r.createdBy.id = :createdBy)")
    Page<SavedReport> searchReports(
            @Param("name") String name,
            @Param("category") String category,
            @Param("type") String type,
            @Param("isPublic") Boolean isPublic,
            @Param("createdBy") Long createdBy,
            Pageable pageable);
            
    // ==================== CUSTOM QUERIES ====================
    
    @Query("SELECT r FROM SavedReport r WHERE r.isPublic = true OR r.createdBy.id = :userId")
    List<SavedReport> findAccessibleReports(@Param("userId") Long userId);

    @Query("SELECT r FROM SavedReport r WHERE r.isPublic = true OR r.createdBy.id = :userId")
    Page<SavedReport> findAccessibleReportsPage(@Param("userId") Long userId, Pageable pageable);
    
    // Alias to match service usage if needed, or update service to use findAccessibleReportsPage
    @Query("SELECT r FROM SavedReport r WHERE r.createdBy.id = :userId OR r.isPublic = true")
    List<SavedReport> findByCreatedByOrIsPublicTrue(@Param("userId") Long userId);
    
    @Query("SELECT r FROM SavedReport r WHERE r.updatedAt >= :date ORDER BY r.updatedAt DESC")
    List<SavedReport> findRecentlyUpdatedReports(@Param("date") LocalDateTime date);
    
    // ==================== STATISTICS ====================
    
    @Query("SELECT r.reportCategory, COUNT(r) FROM SavedReport r GROUP BY r.reportCategory")
    List<Object[]> getReportCountsByCategory();
    
    @Query("SELECT r.reportType, COUNT(r) FROM SavedReport r GROUP BY r.reportType")
    List<Object[]> getReportCountsByType();

    @Query("SELECT r.outputFormat, COUNT(r) FROM SavedReport r GROUP BY r.outputFormat")
    List<Object[]> getReportCountsByOutputFormat();

    // ==================== SPECIFIC TYPES ====================

    @Query("SELECT r FROM SavedReport r WHERE r.reportType = 'STANDARD' ORDER BY r.reportName")
    List<SavedReport> findStandardReports();
    
    @Query("SELECT r FROM SavedReport r WHERE r.reportType = 'CUSTOM' ORDER BY r.reportName")
    List<SavedReport> findCustomReports();
    
    @Query("SELECT r FROM SavedReport r WHERE r.reportType = 'SCHEDULED' ORDER BY r.reportName")
    List<SavedReport> findScheduledReports();
}
