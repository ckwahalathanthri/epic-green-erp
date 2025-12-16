package lk.epicgreen.erp.audit.repository;

import lk.epicgreen.erp.audit.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ActivityLog Repository
 * Repository for activity log data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find activity logs by user ID
     */
    List<ActivityLog> findByUserId(Long userId);
    
    /**
     * Find activity logs by user ID with pagination
     */
    Page<ActivityLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find activity logs by username
     */
    List<ActivityLog> findByUsername(String username);
    
    /**
     * Find activity logs by username with pagination
     */
    Page<ActivityLog> findByUsername(String username, Pageable pageable);
    
    /**
     * Find activity logs by activity type
     */
    List<ActivityLog> findByActivityType(String activityType);
    
    /**
     * Find activity logs by activity type with pagination
     */
    Page<ActivityLog> findByActivityType(String activityType, Pageable pageable);
    
    /**
     * Find activity logs by module name
     */
    List<ActivityLog> findByModuleName(String moduleName);
    
    /**
     * Find activity logs by module name with pagination
     */
    Page<ActivityLog> findByModuleName(String moduleName, Pageable pageable);
    
    /**
     * Find activity logs by session ID
     */
    List<ActivityLog> findBySessionId(String sessionId);
    
    /**
     * Find activity logs by session ID ordered by activity timestamp
     */
    List<ActivityLog> findBySessionIdOrderByActivityTimestampAsc(String sessionId);
    
    /**
     * Find activity logs by IP address
     */
    List<ActivityLog> findByIpAddress(String ipAddress);
    
    /**
     * Find activity logs by IP address with pagination
     */
    Page<ActivityLog> findByIpAddress(String ipAddress, Pageable pageable);
    
    /**
     * Find activity logs by device type
     */
    List<ActivityLog> findByDeviceType(String deviceType);
    
    /**
     * Find activity logs by device type with pagination
     */
    Page<ActivityLog> findByDeviceType(String deviceType, Pageable pageable);
    
    /**
     * Find activity logs by success status
     */
    List<ActivityLog> findByIsSuccessful(Boolean isSuccessful);
    
    /**
     * Find activity logs by success status with pagination
     */
    Page<ActivityLog> findByIsSuccessful(Boolean isSuccessful, Pageable pageable);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find activity logs by activity timestamp between dates
     */
    List<ActivityLog> findByActivityTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find activity logs by activity timestamp between dates with pagination
     */
    Page<ActivityLog> findByActivityTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find activity logs by user ID and activity timestamp between dates
     */
    List<ActivityLog> findByUserIdAndActivityTimestampBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find activity logs by user ID and activity timestamp between dates with pagination
     */
    Page<ActivityLog> findByUserIdAndActivityTimestampBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find activity logs by activity type and activity timestamp between dates
     */
    List<ActivityLog> findByActivityTypeAndActivityTimestampBetween(String activityType, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find activity logs by module name and activity timestamp between dates
     */
    List<ActivityLog> findByModuleNameAndActivityTimestampBetween(String moduleName, LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find activity logs by user ID and activity type
     */
    List<ActivityLog> findByUserIdAndActivityType(Long userId, String activityType);
    
    /**
     * Find activity logs by user ID and module name
     */
    List<ActivityLog> findByUserIdAndModuleName(Long userId, String moduleName);
    
    /**
     * Find activity logs by user ID and session ID
     */
    List<ActivityLog> findByUserIdAndSessionId(Long userId, String sessionId);
    
    /**
     * Find activity logs by user ID, activity type and date range
     */
    List<ActivityLog> findByUserIdAndActivityTypeAndActivityTimestampBetween(
        Long userId, String activityType, LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // LOGIN/LOGOUT SPECIFIC
    // ===================================================================
    
    /**
     * Find login activities by user ID
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.userId = :userId AND a.activityType = 'LOGIN' " +
           "ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findLoginActivitiesByUserId(@Param("userId") Long userId);
    
    /**
     * Find login activities by user ID with pagination
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.userId = :userId AND a.activityType = 'LOGIN' " +
           "ORDER BY a.activityTimestamp DESC")
    Page<ActivityLog> findLoginActivitiesByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Find last login activity by user ID
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.userId = :userId AND a.activityType = 'LOGIN' " +
           "ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findLastLoginByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Find logout activities by user ID
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.userId = :userId AND a.activityType = 'LOGOUT' " +
           "ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findLogoutActivitiesByUserId(@Param("userId") Long userId);
    
    /**
     * Find failed login attempts
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.activityType = 'LOGIN' AND a.isSuccessful = false " +
           "ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findFailedLoginAttempts();
    
    /**
     * Find failed login attempts with pagination
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.activityType = 'LOGIN' AND a.isSuccessful = false " +
           "ORDER BY a.activityTimestamp DESC")
    Page<ActivityLog> findFailedLoginAttempts(Pageable pageable);
    
    /**
     * Find failed login attempts by user ID
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.userId = :userId AND a.activityType = 'LOGIN' " +
           "AND a.isSuccessful = false ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findFailedLoginAttemptsByUserId(@Param("userId") Long userId);
    
    /**
     * Find failed login attempts by IP address
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.ipAddress = :ipAddress AND a.activityType = 'LOGIN' " +
           "AND a.isSuccessful = false ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findFailedLoginAttemptsByIp(@Param("ipAddress") String ipAddress);
    
    /**
     * Count failed login attempts by user ID in time range
     */
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.userId = :userId AND a.activityType = 'LOGIN' " +
           "AND a.isSuccessful = false AND a.activityTimestamp BETWEEN :startDate AND :endDate")
    Long countFailedLoginAttempts(@Param("userId") Long userId,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);
    
    // ===================================================================
    // PAGE VIEW SPECIFIC
    // ===================================================================
    
    /**
     * Find page views by user ID
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.userId = :userId AND a.activityType = 'PAGE_VIEW' " +
           "ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findPageViewsByUserId(@Param("userId") Long userId);
    
    /**
     * Find page views by user ID with pagination
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.userId = :userId AND a.activityType = 'PAGE_VIEW' " +
           "ORDER BY a.activityTimestamp DESC")
    Page<ActivityLog> findPageViewsByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * Find most viewed pages
     */
    @Query("SELECT a.pageUrl, COUNT(a) as viewCount FROM ActivityLog a " +
           "WHERE a.activityType = 'PAGE_VIEW' " +
           "GROUP BY a.pageUrl ORDER BY viewCount DESC")
    List<Object[]> findMostViewedPages();
    
    /**
     * Find most viewed pages with date range
     */
    @Query("SELECT a.pageUrl, COUNT(a) as viewCount FROM ActivityLog a " +
           "WHERE a.activityType = 'PAGE_VIEW' " +
           "AND a.activityTimestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY a.pageUrl ORDER BY viewCount DESC")
    List<Object[]> findMostViewedPages(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count activities by user ID
     */
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * Count activities by activity type
     */
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.activityType = :activityType")
    Long countByActivityType(@Param("activityType") String activityType);
    
    /**
     * Count activities by module name
     */
    @Query("SELECT COUNT(a) FROM ActivityLog a WHERE a.moduleName = :moduleName")
    Long countByModuleName(@Param("moduleName") String moduleName);
    
    /**
     * Get total session duration by user
     */
    @Query("SELECT SUM(a.durationMillis) FROM ActivityLog a WHERE a.userId = :userId")
    Long getTotalSessionDurationByUser(@Param("userId") Long userId);
    
    /**
     * Get average session duration
     */
    @Query("SELECT AVG(a.durationMillis) FROM ActivityLog a WHERE a.durationMillis IS NOT NULL")
    Double getAverageSessionDuration();
    
    /**
     * Get most active users
     */
    @Query("SELECT a.username, COUNT(a) as activityCount FROM ActivityLog a " +
           "GROUP BY a.username ORDER BY activityCount DESC")
    List<Object[]> getMostActiveUsers();
    
    /**
     * Get most active users in date range
     */
    @Query("SELECT a.username, COUNT(a) as activityCount FROM ActivityLog a " +
           "WHERE a.activityTimestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY a.username ORDER BY activityCount DESC")
    List<Object[]> getMostActiveUsers(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get activity type distribution
     */
    @Query("SELECT a.activityType, COUNT(a) as activityCount FROM ActivityLog a " +
           "GROUP BY a.activityType ORDER BY activityCount DESC")
    List<Object[]> getActivityTypeDistribution();
    
    /**
     * Get device type distribution
     */
    @Query("SELECT a.deviceType, COUNT(a) as deviceCount FROM ActivityLog a " +
           "GROUP BY a.deviceType ORDER BY deviceCount DESC")
    List<Object[]> getDeviceTypeDistribution();
    
    /**
     * Get hourly activity
     */
    @Query("SELECT HOUR(a.activityTimestamp) as hour, COUNT(a) as activityCount FROM ActivityLog a " +
           "WHERE a.activityTimestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY HOUR(a.activityTimestamp) ORDER BY hour")
    List<Object[]> getHourlyActivity(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get daily activity
     */
    @Query("SELECT DATE(a.activityTimestamp) as date, COUNT(a) as activityCount FROM ActivityLog a " +
           "WHERE a.activityTimestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(a.activityTimestamp) ORDER BY date")
    List<Object[]> getDailyActivity(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get unique users count
     */
    @Query("SELECT COUNT(DISTINCT a.userId) FROM ActivityLog a " +
           "WHERE a.activityTimestamp BETWEEN :startDate AND :endDate")
    Long getUniqueUsersCount(@Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get unique sessions count
     */
    @Query("SELECT COUNT(DISTINCT a.sessionId) FROM ActivityLog a " +
           "WHERE a.activityTimestamp BETWEEN :startDate AND :endDate")
    Long getUniqueSessionsCount(@Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate);
    
    // ===================================================================
    // SEARCH AND FILTER
    // ===================================================================
    
    /**
     * Search activity logs
     */
    @Query("SELECT a FROM ActivityLog a WHERE " +
           "LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.activityType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.activityDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.moduleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.pageUrl) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ActivityLog> searchActivityLogs(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find recent activity logs
     */
    @Query("SELECT a FROM ActivityLog a ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findRecentActivityLogs(Pageable pageable);
    
    /**
     * Find current user sessions
     */
    @Query("SELECT a FROM ActivityLog a WHERE a.activityType = 'LOGIN' " +
           "AND NOT EXISTS (SELECT l FROM ActivityLog l WHERE l.sessionId = a.sessionId " +
           "AND l.activityType = 'LOGOUT') ORDER BY a.activityTimestamp DESC")
    List<ActivityLog> findCurrentUserSessions();
    
    /**
     * Get user sessions by date range
     */
    @Query("SELECT DISTINCT a.sessionId FROM ActivityLog a WHERE a.userId = :userId " +
           "AND a.activityTimestamp BETWEEN :startDate AND :endDate")
    List<String> getUserSessions(@Param("userId") Long userId,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);
    
    /**
     * Delete old activity logs
     */
    @Query("DELETE FROM ActivityLog a WHERE a.activityTimestamp < :beforeDate")
    void deleteOldActivityLogs(@Param("beforeDate") LocalDateTime beforeDate);
}
