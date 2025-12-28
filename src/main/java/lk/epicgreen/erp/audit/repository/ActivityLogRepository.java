package lk.epicgreen.erp.audit.repository;

import lk.epicgreen.erp.audit.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for ActivityLog entity
 * Based on ACTUAL database schema: activity_logs table
 * 
 * Fields: user_id (BIGINT), activity_type, module, activity_description,
 *         reference_type, reference_id (BIGINT), ip_address,
 *         device_type (ENUM: WEB, MOBILE_ANDROID, MOBILE_IOS, API),
 *         created_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long>, JpaSpecificationExecutor<ActivityLog> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find activity logs by user
     */
    List<ActivityLog> findByUserId(Long userId);
    
    /**
     * Find activity logs by user with pagination
     */
    Page<ActivityLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find activity logs by activity type
     */
    List<ActivityLog> findByActivityType(String activityType);
    
    /**
     * Find activity logs by activity type with pagination
     */
    Page<ActivityLog> findByActivityType(String activityType, Pageable pageable);
    
    /**
     * Find activity logs by module
     */
    List<ActivityLog> findByModule(String module);
    
    /**
     * Find activity logs by module with pagination
     */
    Page<ActivityLog> findByModule(String module, Pageable pageable);
    
    /**
     * Find activity logs by device type
     */
    List<ActivityLog> findByDeviceType(String deviceType);
    
    /**
     * Find activity logs by device type with pagination
     */
    Page<ActivityLog> findByDeviceType(String deviceType, Pageable pageable);
    
    /**
     * Find activity logs by reference type and reference ID
     */
    List<ActivityLog> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
    
    /**
     * Find activity logs by created at time range
     */
    List<ActivityLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find activity logs by created at time range with pagination
     */
    Page<ActivityLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search activity logs by activity type containing (case-insensitive)
     */
    Page<ActivityLog> findByActivityTypeContainingIgnoreCase(String activityType, Pageable pageable);
    
    /**
     * Search activity logs by activity description containing (case-insensitive)
     */
    Page<ActivityLog> findByActivityDescriptionContainingIgnoreCase(String activityDescription, Pageable pageable);
    
    /**
     * Search activity logs by multiple criteria
     */
    @Query("SELECT al FROM ActivityLog al WHERE " +
           "(:userId IS NULL OR al.userId = :userId) AND " +
           "(:activityType IS NULL OR LOWER(al.activityType) LIKE LOWER(CONCAT('%', :activityType, '%'))) AND " +
           "(:module IS NULL OR al.module = :module) AND " +
           "(:deviceType IS NULL OR al.deviceType = :deviceType) AND " +
           "(:startTime IS NULL OR al.createdAt >= :startTime) AND " +
           "(:endTime IS NULL OR al.createdAt <= :endTime)")
    Page<ActivityLog> searchActivityLogs(
            @Param("userId") Long userId,
            @Param("activityType") String activityType,
            @Param("module") String module,
            @Param("deviceType") String deviceType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count activity logs by user
     */
    long countByUserId(Long userId);
    
    /**
     * Count activity logs by module
     */
    long countByModule(String module);
    
    /**
     * Count activity logs by device type
     */
    long countByDeviceType(String deviceType);
    
    /**
     * Count activity logs in time range
     */
    long countByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find web activity logs
     */
    @Query("SELECT al FROM ActivityLog al WHERE al.deviceType = 'WEB' ORDER BY al.createdAt DESC")
    List<ActivityLog> findWebActivityLogs();
    
    /**
     * Find mobile Android activity logs
     */
    @Query("SELECT al FROM ActivityLog al WHERE al.deviceType = 'MOBILE_ANDROID' ORDER BY al.createdAt DESC")
    List<ActivityLog> findMobileAndroidActivityLogs();
    
    /**
     * Find mobile iOS activity logs
     */
    @Query("SELECT al FROM ActivityLog al WHERE al.deviceType = 'MOBILE_IOS' ORDER BY al.createdAt DESC")
    List<ActivityLog> findMobileIosActivityLogs();
    
    /**
     * Find API activity logs
     */
    @Query("SELECT al FROM ActivityLog al WHERE al.deviceType = 'API' ORDER BY al.createdAt DESC")
    List<ActivityLog> findApiActivityLogs();
    
    /**
     * Find activity logs by user and module
     */
    List<ActivityLog> findByUserIdAndModule(Long userId, String module);
    
    /**
     * Find activity logs by user and device type
     */
    List<ActivityLog> findByUserIdAndDeviceType(Long userId, String deviceType);
    
    /**
     * Find activity logs by IP address
     */
    List<ActivityLog> findByIpAddress(String ipAddress);
    
    /**
     * Get activity log statistics
     */
    @Query("SELECT " +
           "COUNT(al) as totalActivities, " +
           "COUNT(DISTINCT al.userId) as uniqueUsers, " +
           "COUNT(DISTINCT al.module) as uniqueModules, " +
           "SUM(CASE WHEN al.deviceType = 'WEB' THEN 1 ELSE 0 END) as webActivities, " +
           "SUM(CASE WHEN al.deviceType = 'MOBILE_ANDROID' THEN 1 ELSE 0 END) as androidActivities, " +
           "SUM(CASE WHEN al.deviceType = 'MOBILE_IOS' THEN 1 ELSE 0 END) as iosActivities, " +
           "SUM(CASE WHEN al.deviceType = 'API' THEN 1 ELSE 0 END) as apiActivities " +
           "FROM ActivityLog al")
    Object getActivityLogStatistics();
    
    /**
     * Get activity logs grouped by module
     */
    @Query("SELECT al.module, COUNT(al) as activityCount " +
           "FROM ActivityLog al GROUP BY al.module ORDER BY activityCount DESC")
    List<Object[]> getActivityLogsByModule();
    
    /**
     * Get activity logs grouped by device type
     */
    @Query("SELECT al.deviceType, COUNT(al) as activityCount " +
           "FROM ActivityLog al GROUP BY al.deviceType ORDER BY activityCount DESC")
    List<Object[]> getActivityLogsByDeviceType();
    
    /**
     * Get activity logs grouped by activity type
     */
    @Query("SELECT al.activityType, COUNT(al) as activityCount " +
           "FROM ActivityLog al GROUP BY al.activityType ORDER BY activityCount DESC")
    List<Object[]> getActivityLogsByType();
    
    /**
     * Get activity logs grouped by user
     */
    @Query("SELECT al.userId, COUNT(al) as activityCount " +
           "FROM ActivityLog al GROUP BY al.userId ORDER BY activityCount DESC")
    List<Object[]> getActivityLogsByUser();
    
    /**
     * Get daily activity log summary
     */
    @Query("SELECT DATE(al.createdAt) as activityDate, COUNT(al) as activityCount, " +
           "COUNT(DISTINCT al.userId) as uniqueUsers " +
           "FROM ActivityLog al WHERE al.createdAt BETWEEN :startTime AND :endTime " +
           "GROUP BY DATE(al.createdAt) ORDER BY activityDate DESC")
    List<Object[]> getDailyActivityLogSummary(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Get hourly activity log summary
     */
    @Query("SELECT HOUR(al.createdAt) as activityHour, COUNT(al) as activityCount " +
           "FROM ActivityLog al WHERE DATE(al.createdAt) = CURRENT_DATE " +
           "GROUP BY HOUR(al.createdAt) ORDER BY activityHour")
    List<Object[]> getHourlyActivityLogSummary();
    
    /**
     * Find today's activity logs
     */
    @Query("SELECT al FROM ActivityLog al WHERE DATE(al.createdAt) = CURRENT_DATE ORDER BY al.createdAt DESC")
    List<ActivityLog> findTodayActivityLogs();
    
    /**
     * Find all activity logs ordered by created at
     */
    List<ActivityLog> findAllByOrderByCreatedAtDesc();
    
    /**
     * Get user activity summary
     */
    @Query("SELECT al.activityType, COUNT(al) as activityCount " +
           "FROM ActivityLog al WHERE al.userId = :userId " +
           "AND al.createdAt BETWEEN :startTime AND :endTime " +
           "GROUP BY al.activityType ORDER BY activityCount DESC")
    List<Object[]> getUserActivitySummary(
            @Param("userId") Long userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Get module usage statistics
     */
    @Query("SELECT al.module, COUNT(al) as usageCount, COUNT(DISTINCT al.userId) as uniqueUsers " +
           "FROM ActivityLog al WHERE al.createdAt >= :sinceTime " +
           "GROUP BY al.module ORDER BY usageCount DESC")
    List<Object[]> getModuleUsageStatistics(@Param("sinceTime") LocalDateTime sinceTime);
    
    /**
     * Find most active users
     */
    @Query("SELECT al.userId, COUNT(al) as activityCount " +
           "FROM ActivityLog al WHERE al.createdAt >= :sinceTime " +
           "GROUP BY al.userId ORDER BY activityCount DESC")
    List<Object[]> findMostActiveUsers(@Param("sinceTime") LocalDateTime sinceTime);
    
    /**
     * Find peak activity hours
     */
    @Query("SELECT HOUR(al.createdAt) as activityHour, COUNT(al) as activityCount " +
           "FROM ActivityLog al WHERE al.createdAt >= :sinceTime " +
           "GROUP BY HOUR(al.createdAt) ORDER BY activityCount DESC")
    List<Object[]> findPeakActivityHours(@Param("sinceTime") LocalDateTime sinceTime);
}
