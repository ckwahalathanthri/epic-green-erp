package lk.epicgreen.erp.common.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service for audit log operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {
    
    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    
    // ==================== Create Audit Logs ====================
    
    /**
     * Logs an audit event asynchronously
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @param action Action performed
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityName, String entityId, String action) {
        log(entityName, entityId, action, null, null, null);
    }
    
    /**
     * Logs an audit event with old and new values
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @param action Action performed
     * @param oldValues Old values
     * @param newValues New values
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityName, String entityId, String action, 
                   Object oldValues, Object newValues) {
        log(entityName, entityId, action, oldValues, newValues, null);
    }
    
    /**
     * Logs an audit event with full details
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @param action Action performed
     * @param oldValues Old values
     * @param newValues New values
     * @param details Additional details
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityName, String entityId, String action,
                   Object oldValues, Object newValues, Map<String, Object> details) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .entityName(entityName)
                    .entityId(String.valueOf(entityId))
                    .action(action)
                    .performedBy(getCurrentUsername())
                    .timestamp(LocalDateTime.now())
                    .status("SUCCESS")
                    .build();
            
            // Serialize values to JSON
            if (oldValues != null) {
                auditLog.setOldValues(objectMapper.writeValueAsString(oldValues));
            }
            if (newValues != null) {
                auditLog.setNewValues(objectMapper.writeValueAsString(newValues));
            }
            if (details != null) {
                auditLog.setDetails(objectMapper.writeValueAsString(details));
            }
            
            // Get HTTP request info
            enrichWithRequestInfo(auditLog);
            
            // Extract module from entity name
            auditLog.setModule(extractModule(entityName));
            
            // Calculate changed fields
            if (oldValues != null && newValues != null) {
                String changedFields = calculateChangedFields(oldValues, newValues);
                auditLog.setChangedFields(changedFields);
            }
            
            auditLogRepository.save(auditLog);
            
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Logs a failed operation
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @param action Action attempted
     * @param errorMessage Error message
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailure(String entityName, String entityId, String action, String errorMessage) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .entityName(entityName)
                    .entityId(String.valueOf(entityId))
                    .action(action)
                    .performedBy(getCurrentUsername())
                    .timestamp(LocalDateTime.now())
                    .status("FAILURE")
                    .errorMessage(errorMessage)
                    .build();
            
            enrichWithRequestInfo(auditLog);
            auditLog.setModule(extractModule(entityName));
            
            auditLogRepository.save(auditLog);
            
        } catch (Exception e) {
            log.error("Failed to log failure: {}", e.getMessage(), e);
        }
    }
    
    // ==================== Query Audit Logs ====================
    
    /**
     * Gets audit logs for an entity
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogs(String entityName, String entityId, Pageable pageable) {
        return auditLogRepository.findByEntityNameAndEntityIdOrderByTimestampDesc(
                entityName, entityId, pageable
        );
    }
    
    /**
     * Gets recent audit logs for an entity
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @param limit Limit
     * @return List of audit logs
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentAuditLogs(String entityName, String entityId, int limit) {
        return auditLogRepository.findRecentByEntity(entityName, entityId, limit);
    }
    
    /**
     * Gets audit logs by entity type
     * 
     * @param entityName Entity name
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByEntity(String entityName, Pageable pageable) {
        return auditLogRepository.findByEntityNameOrderByTimestampDesc(entityName, pageable);
    }
    
    /**
     * Gets audit logs by user
     * 
     * @param username Username
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByUser(String username, Pageable pageable) {
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(username, pageable);
    }
    
    /**
     * Gets audit logs by action
     * 
     * @param action Action type
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByAction(String action, Pageable pageable) {
        return auditLogRepository.findByActionOrderByTimestampDesc(action, pageable);
    }
    
    /**
     * Gets audit logs by date range
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByDateRange(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Pageable pageable) {
        return auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(
                startDate, endDate, pageable
        );
    }
    
    /**
     * Searches audit logs with criteria
     * 
     * @param entityName Entity name (optional)
     * @param action Action (optional)
     * @param performedBy User (optional)
     * @param startDate Start date (optional)
     * @param endDate End date (optional)
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> searchAuditLogs(
            String entityName,
            String action,
            String performedBy,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        return auditLogRepository.search(
                entityName, action, performedBy, startDate, endDate, pageable
        );
    }
    
    /**
     * Gets failed audit logs
     * 
     * @param pageable Pagination
     * @return Page of audit logs
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getFailedAuditLogs(Pageable pageable) {
        return auditLogRepository.findByStatusOrderByTimestampDesc("FAILURE", pageable);
    }
    
    // ==================== Statistics ====================
    
    /**
     * Counts audit logs for entity
     * 
     * @param entityName Entity name
     * @param entityId Entity ID
     * @return Count
     */
    @Transactional(readOnly = true)
    public long countAuditLogs(String entityName, String entityId) {
        return auditLogRepository.countByEntityNameAndEntityId(entityName, entityId);
    }
    
    /**
     * Counts user activity in date range
     * 
     * @param username Username
     * @param startDate Start date
     * @param endDate End date
     * @return Count
     */
    @Transactional(readOnly = true)
    public long countUserActivity(String username, LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.countByPerformedByAndTimestampBetween(
                username, startDate, endDate
        );
    }
    
    // ==================== Cleanup ====================
    
    /**
     * Deletes old audit logs
     * 
     * @param daysToKeep Number of days to keep
     * @return Number of deleted records
     */
    @Transactional
    public long deleteOldAuditLogs(int daysToKeep) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysToKeep);
        return auditLogRepository.deleteByTimestampBefore(beforeDate);
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Gets current username from security context
     * 
     * @return Username
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return AppConstants.SYSTEM_USER;
        }
        
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            return AppConstants.ANONYMOUS_USER;
        }
        
        return authentication.getName();
    }
    
    /**
     * Enriches audit log with HTTP request information
     * 
     * @param auditLog Audit log
     */
    private void enrichWithRequestInfo(AuditLog auditLog) {
        try {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                auditLog.setIpAddress(getClientIpAddress(request));
                auditLog.setUserAgent(request.getHeader("User-Agent"));
                auditLog.setRequestUrl(request.getRequestURI());
                auditLog.setHttpMethod(request.getMethod());
            }
        } catch (Exception e) {
            // Ignore - may not be in request context
        }
    }
    
    /**
     * Gets client IP address from request
     * 
     * @param request HTTP request
     * @return IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * Extracts module name from entity name
     * 
     * @param entityName Entity name
     * @return Module name
     */
    private String extractModule(String entityName) {
        if (entityName == null) return null;
        
        // Convert PascalCase to UPPER_SNAKE_CASE
        return entityName.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }
    
    /**
     * Calculates changed fields between old and new values
     * 
     * @param oldValues Old values
     * @param newValues New values
     * @return Comma-separated list of changed fields
     */
    private String calculateChangedFields(Object oldValues, Object newValues) {
        try {
            // Convert to maps for comparison
            @SuppressWarnings("unchecked")
            Map<String, Object> oldMap = objectMapper.convertValue(oldValues, Map.class);
            @SuppressWarnings("unchecked")
            Map<String, Object> newMap = objectMapper.convertValue(newValues, Map.class);
            
            StringBuilder changedFields = new StringBuilder();
            
            for (String key : newMap.keySet()) {
                Object oldValue = oldMap.get(key);
                Object newValue = newMap.get(key);
                
                if (oldValue == null && newValue != null) {
                    if (changedFields.length() > 0) changedFields.append(", ");
                    changedFields.append(key);
                } else if (oldValue != null && !oldValue.equals(newValue)) {
                    if (changedFields.length() > 0) changedFields.append(", ");
                    changedFields.append(key);
                }
            }
            
            return changedFields.toString();
            
        } catch (Exception e) {
            log.error("Failed to calculate changed fields: {}", e.getMessage());
            return null;
        }
    }
}
