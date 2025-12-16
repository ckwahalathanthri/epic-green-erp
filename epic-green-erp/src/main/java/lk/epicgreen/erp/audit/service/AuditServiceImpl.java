package lk.epicgreen.erp.audit.service;

import lk.epicgreen.erp.audit.entity.ActivityLog;
import lk.epicgreen.erp.audit.entity.AuditLog;
import lk.epicgreen.erp.audit.entity.ErrorLog;
import lk.epicgreen.erp.audit.repository.ActivityLogRepository;
import lk.epicgreen.erp.audit.repository.AuditLogRepository;
import lk.epicgreen.erp.audit.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Audit Service Implementation
 * Implementation of audit service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditServiceImpl implements AuditService {
    
    private final AuditLogRepository auditLogRepository;
    private final ActivityLogRepository activityLogRepository;
    private final ErrorLogRepository errorLogRepository;
    
    // ===================================================================
    // AUDIT LOG OPERATIONS
    // ===================================================================
    
    @Override
    public AuditLog createAuditLog(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AuditLog getAuditLogById(Long id) {
        return auditLogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Audit log not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByUserId(Long userId) {
        return auditLogRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByUserId(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByUsername(String username, Pageable pageable) {
        return auditLogRepository.findByUsername(username, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByModule(String moduleName) {
        return auditLogRepository.findByModuleName(moduleName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByModule(String moduleName, Pageable pageable) {
        return auditLogRepository.findByModuleName(moduleName, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByActionType(String actionType) {
        return auditLogRepository.findByActionType(actionType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByActionType(String actionType, Pageable pageable) {
        return auditLogRepository.findByActionType(actionType, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByEntity(String entityType, Long entityId, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getEntityHistory(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByActionTimestampDesc(entityType, entityId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByActionTimestampBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByActionTimestampBetween(startDate, endDate, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return auditLogRepository.findByActionTimestampBetween(startOfDay, endOfDay);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTodayAuditLogs() {
        return getAuditLogsByDate(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getFailedAuditLogs() {
        return auditLogRepository.findByIsSuccessful(false);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getFailedAuditLogs(Pageable pageable) {
        return auditLogRepository.findByIsSuccessful(false, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> searchAuditLogs(String keyword, Pageable pageable) {
        return auditLogRepository.searchAuditLogs(keyword, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentAuditLogs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return auditLogRepository.findRecentAuditLogs(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getSlowQueries(Long thresholdMillis) {
        return auditLogRepository.findSlowQueries(thresholdMillis);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getSlowQueries(Long thresholdMillis, Pageable pageable) {
        return auditLogRepository.findSlowQueries(thresholdMillis, pageable);
    }
    
    @Override
    public void deleteOldAuditLogs(int daysToKeep) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysToKeep);
        auditLogRepository.deleteOldAuditLogs(beforeDate);
        log.info("Deleted audit logs older than {} days", daysToKeep);
    }
    
    // ===================================================================
    // ACTIVITY LOG OPERATIONS
    // ===================================================================
    
    @Override
    public ActivityLog createActivityLog(ActivityLog activityLog) {
        return activityLogRepository.save(activityLog);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ActivityLog getActivityLogById(Long id) {
        return activityLogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Activity log not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAll(Sort.by(Sort.Direction.DESC, "activityTimestamp"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getAllActivityLogs(Pageable pageable) {
        return activityLogRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivityLogsByUserId(Long userId) {
        return activityLogRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getActivityLogsByUserId(Long userId, Pageable pageable) {
        return activityLogRepository.findByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivityLogsByType(String activityType) {
        return activityLogRepository.findByActivityType(activityType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getActivityLogsByType(String activityType, Pageable pageable) {
        return activityLogRepository.findByActivityType(activityType, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivityLogsBySessionId(String sessionId) {
        return activityLogRepository.findBySessionIdOrderByActivityTimestampAsc(sessionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getLoginActivitiesByUserId(Long userId) {
        return activityLogRepository.findLoginActivitiesByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ActivityLog getLastLoginByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<ActivityLog> logins = activityLogRepository.findLastLoginByUserId(userId, pageable);
        return logins.isEmpty() ? null : logins.get(0);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getFailedLoginAttempts() {
        return activityLogRepository.findFailedLoginAttempts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getFailedLoginAttempts(Pageable pageable) {
        return activityLogRepository.findFailedLoginAttempts(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getFailedLoginAttemptsByUserId(Long userId) {
        return activityLogRepository.findFailedLoginAttemptsByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getPageViewsByUserId(Long userId) {
        return activityLogRepository.findPageViewsByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getPageViewsByUserId(Long userId, Pageable pageable) {
        return activityLogRepository.findPageViewsByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> searchActivityLogs(String keyword, Pageable pageable) {
        return activityLogRepository.searchActivityLogs(keyword, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getRecentActivityLogs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return activityLogRepository.findRecentActivityLogs(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ActivityLog> getCurrentUserSessions() {
        return activityLogRepository.findCurrentUserSessions();
    }
    
    @Override
    public void deleteOldActivityLogs(int daysToKeep) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysToKeep);
        activityLogRepository.deleteOldActivityLogs(beforeDate);
        log.info("Deleted activity logs older than {} days", daysToKeep);
    }
    
    // ===================================================================
    // STATISTICS AND REPORTS - Part 1
    // ===================================================================
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAuditStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        
        return getAuditStatistics(startOfMonth, now);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAuditStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        // Total audit logs
        List<AuditLog> logs = auditLogRepository.findByActionTimestampBetween(startDate, endDate);
        stats.put("totalAuditLogs", logs.size());
        
        // Successful vs Failed
        long successfulCount = logs.stream().filter(AuditLog::getIsSuccessful).count();
        long failedCount = logs.size() - successfulCount;
        stats.put("successfulActions", successfulCount);
        stats.put("failedActions", failedCount);
        
        // By module
        Map<String, Long> byModule = logs.stream()
            .collect(Collectors.groupingBy(AuditLog::getModuleName, Collectors.counting()));
        stats.put("byModule", byModule);
        
        // By action type
        Map<String, Long> byActionType = logs.stream()
            .collect(Collectors.groupingBy(AuditLog::getActionType, Collectors.counting()));
        stats.put("byActionType", byActionType);
        
        // Average execution time
        double avgExecutionTime = logs.stream()
            .filter(log -> log.getExecutionTimeMillis() != null)
            .mapToLong(AuditLog::getExecutionTimeMillis)
            .average()
            .orElse(0.0);
        stats.put("averageExecutionTimeMillis", avgExecutionTime);
        
        // Unique users
        long uniqueUsers = logs.stream()
            .map(AuditLog::getUserId)
            .filter(Objects::nonNull)
            .distinct()
            .count();
        stats.put("uniqueUsers", uniqueUsers);
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getActivityStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        
        return getActivityStatistics(startOfMonth, now);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getActivityStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        // Total activities
        List<ActivityLog> logs = activityLogRepository.findByActivityTimestampBetween(startDate, endDate);
        stats.put("totalActivities", logs.size());
        
        // By activity type
        Map<String, Long> byType = logs.stream()
            .collect(Collectors.groupingBy(ActivityLog::getActivityType, Collectors.counting()));
        stats.put("byActivityType", byType);
        
        // Login statistics
        long loginCount = logs.stream()
            .filter(log -> "LOGIN".equals(log.getActivityType()))
            .count();
        long failedLoginCount = logs.stream()
            .filter(log -> "LOGIN".equals(log.getActivityType()) && !log.getIsSuccessful())
            .count();
        stats.put("loginCount", loginCount);
        stats.put("failedLoginCount", failedLoginCount);
        
        // Unique users
        long uniqueUsers = activityLogRepository.getUniqueUsersCount(startDate, endDate);
        stats.put("uniqueUsers", uniqueUsers);
        
        // Unique sessions
        long uniqueSessions = activityLogRepository.getUniqueSessionsCount(startDate, endDate);
        stats.put("uniqueSessions", uniqueSessions);
        
        // By device type
        Map<String, Long> byDeviceType = logs.stream()
            .filter(log -> log.getDeviceType() != null)
            .collect(Collectors.groupingBy(ActivityLog::getDeviceType, Collectors.counting()));
        stats.put("byDeviceType", byDeviceType);
        
        // Average session duration
        Double avgDuration = activityLogRepository.getAverageSessionDuration();
        stats.put("averageSessionDurationMillis", avgDuration);
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserActivitySummary(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        
        return getUserActivitySummary(userId, startOfMonth, now);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserActivitySummary(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        // Total activities
        long totalActivities = activityLogRepository.countByUserId(userId);
        summary.put("totalActivities", totalActivities);
        
        // Activities in date range
        List<ActivityLog> logs = activityLogRepository.findByUserIdAndActivityTimestampBetween(
            userId, startDate, endDate);
        summary.put("activitiesInPeriod", logs.size());
        
        // Last login
        ActivityLog lastLogin = getLastLoginByUserId(userId);
        summary.put("lastLogin", lastLogin != null ? lastLogin.getActivityTimestamp() : null);
        
        // Failed login attempts
        List<ActivityLog> failedLogins = activityLogRepository.findFailedLoginAttemptsByUserId(userId);
        summary.put("failedLoginAttempts", failedLogins.size());
        
        // Total session duration
        Long totalDuration = activityLogRepository.getTotalSessionDurationByUser(userId);
        summary.put("totalSessionDurationMillis", totalDuration);
        
        // IP addresses
        List<String> ipAddresses = auditLogRepository.getIpAddressesByUser(userId);
        summary.put("ipAddresses", ipAddresses);
        
        return summary;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostActiveUsers() {
        List<Object[]> results = activityLogRepository.getMostActiveUsers();
        return convertToMapList(results, "username", "activityCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostActiveUsers(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = activityLogRepository.getMostActiveUsers(startDate, endDate);
        return convertToMapList(results, "username", "activityCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostUsedModules() {
        List<Object[]> results = auditLogRepository.getMostUsedModules();
        return convertToMapList(results, "moduleName", "actionCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getActionTypeDistribution() {
        List<Object[]> results = auditLogRepository.getActionTypeDistribution();
        return convertToMapList(results, "actionType", "actionCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getActivityTypeDistribution() {
        List<Object[]> results = activityLogRepository.getActivityTypeDistribution();
        return convertToMapList(results, "activityType", "activityCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDeviceTypeDistribution() {
        List<Object[]> results = activityLogRepository.getDeviceTypeDistribution();
        return convertToMapList(results, "deviceType", "deviceCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostViewedPages() {
        List<Object[]> results = activityLogRepository.findMostViewedPages();
        return convertToMapList(results, "pageUrl", "viewCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMostViewedPages(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = activityLogRepository.findMostViewedPages(startDate, endDate);
        return convertToMapList(results, "pageUrl", "viewCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getHourlyActivity(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        
        List<Object[]> results = activityLogRepository.getHourlyActivity(startDate, endDate);
        return convertToMapList(results, "hour", "activityCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDailyActivity(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        
        List<Object[]> results = activityLogRepository.getDailyActivity(start, end);
        return convertToMapList(results, "date", "activityCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getWeeklyActivity(LocalDate startDate, LocalDate endDate) {
        // Group daily activity by week
        List<Map<String, Object>> dailyActivity = getDailyActivity(startDate, endDate);
        
        Map<Integer, Long> weeklyMap = new HashMap<>();
        for (Map<String, Object> day : dailyActivity) {
            LocalDate date = (LocalDate) day.get("date");
            int weekOfYear = date.getDayOfYear() / 7;
            Long count = (Long) day.get("activityCount");
            weeklyMap.merge(weekOfYear, count, Long::sum);
        }
        
        return weeklyMap.entrySet().stream()
            .map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("week", entry.getKey());
                map.put("activityCount", entry.getValue());
                return map;
            })
            .sorted(Comparator.comparing(m -> (Integer) m.get("week")))
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyActivity(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        
        // Get daily activity for the year
        List<Map<String, Object>> dailyActivity = getDailyActivity(startDate, endDate);
        
        // Group by month
        Map<Integer, Long> monthlyMap = new HashMap<>();
        for (Map<String, Object> day : dailyActivity) {
            LocalDate date = (LocalDate) day.get("date");
            int month = date.getMonthValue();
            Long count = (Long) day.get("activityCount");
            monthlyMap.merge(month, count, Long::sum);
        }
        
        return monthlyMap.entrySet().stream()
            .map(entry -> {
                Map<String, Object> map = new HashMap<>();
                map.put("month", entry.getKey());
                map.put("activityCount", entry.getValue());
                return map;
            })
            .sorted(Comparator.comparing(m -> (Integer) m.get("month")))
            .collect(Collectors.toList());
    }
    
    // ===================================================================
    // ERROR LOG OPERATIONS
    // ===================================================================
    
    @Override
    public ErrorLog createErrorLog(ErrorLog errorLog) {
        return errorLogRepository.save(errorLog);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ErrorLog getErrorLogById(Long id) {
        return errorLogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Error log not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ErrorLog> getAllErrorLogs() {
        return errorLogRepository.findAll(Sort.by(Sort.Direction.DESC, "errorTimestamp"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ErrorLog> getAllErrorLogs(Pageable pageable) {
        return errorLogRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ErrorLog> getErrorLogsBySeverity(String severityLevel) {
        return errorLogRepository.findBySeverityLevel(severityLevel);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ErrorLog> getErrorLogsBySeverity(String severityLevel, Pageable pageable) {
        return errorLogRepository.findBySeverityLevel(severityLevel, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ErrorLog> getCriticalErrors() {
        return errorLogRepository.findBySeverityLevel("CRITICAL");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ErrorLog> getCriticalErrors(Pageable pageable) {
        return errorLogRepository.findBySeverityLevel("CRITICAL", pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ErrorLog> getUnresolvedErrors() {
        return errorLogRepository.findByStatus("NEW");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ErrorLog> getUnresolvedErrors(Pageable pageable) {
        return errorLogRepository.findByStatus("NEW", pageable);
    }
    
    @Override
    public ErrorLog updateErrorStatus(Long errorId, String status) {
        ErrorLog errorLog = getErrorLogById(errorId);
        errorLog.setStatus(status);
        
        if ("RESOLVED".equals(status) || "CLOSED".equals(status)) {
            errorLog.setResolvedAt(LocalDateTime.now());
        }
        
        return errorLogRepository.save(errorLog);
    }
    
    @Override
    public ErrorLog assignError(Long errorId, Long userId) {
        ErrorLog errorLog = getErrorLogById(errorId);
        errorLog.setAssignedTo(userId);
        errorLog.setStatus("ASSIGNED");
        
        return errorLogRepository.save(errorLog);
    }
    
    @Override
    public ErrorLog resolveError(Long errorId, String resolutionNotes) {
        ErrorLog errorLog = getErrorLogById(errorId);
        errorLog.setStatus("RESOLVED");
        errorLog.setResolvedAt(LocalDateTime.now());
        errorLog.setResolutionNotes(resolutionNotes);
        
        return errorLogRepository.save(errorLog);
    }
    
    // ===================================================================
    // HELPER METHODS
    // ===================================================================
    
    /**
     * Convert Object[] results to List of Maps
     */
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
