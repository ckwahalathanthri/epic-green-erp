package lk.epicgreen.erp.audit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.epicgreen.erp.audit.entity.AuditLog;
import lk.epicgreen.erp.audit.entity.ErrorLog;
import lk.epicgreen.erp.audit.repository.AuditLogRepository;
import lk.epicgreen.erp.audit.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Audit Aspect
 * AOP aspect for automatic audit logging of method calls, data changes, and errors
 * 
 * Usage:
 * - Add @Audited annotation to methods that need audit logging
 * - Add @AuditedEntity annotation to entity classes
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {
    
    private final AuditLogRepository auditLogRepository;
    private final ErrorLogRepository errorLogRepository;
    private final ObjectMapper objectMapper;
    
    /**
     * Audited annotation
     * Mark methods that should be audited
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Audited {
        String module() default "";
        String action() default "";
        String description() default "";
        boolean logParameters() default false;
        boolean logResult() default false;
    }
    
    /**
     * AuditedEntity annotation
     * Mark entity classes that should be audited
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface AuditedEntity {
        String entityType();
        String moduleName();
    }
    
    /**
     * Around advice for @Audited methods
     */
    @Around("@annotation(audited)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Audited audited) throws Throwable {
        long startTime = System.currentTimeMillis();
        String moduleName = audited.module();
        String actionType = audited.action();
        String description = audited.description();
        
        // Get method details
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = signature.getDeclaringTypeName();
        
        // Get user details
        UserDetails userDetails = getCurrentUserDetails();
        
        // Get request details
        RequestDetails requestDetails = getRequestDetails();
        
        // Build audit log
        AuditLog auditLog = AuditLog.builder()
            .moduleName(moduleName.isEmpty() ? extractModuleName(className) : moduleName)
            .entityType(extractEntityType(className))
            .actionType(actionType.isEmpty() ? extractActionType(methodName) : actionType)
            .actionDescription(description.isEmpty() ? methodName : description)
            .actionTimestamp(LocalDateTime.now())
            .userId(userDetails.getUserId())
            .username(userDetails.getUsername())
            .userRole(userDetails.getUserRole())
            .ipAddress(requestDetails.getIpAddress())
            .userAgent(requestDetails.getUserAgent())
            .requestMethod(requestDetails.getRequestMethod())
            .requestUrl(requestDetails.getRequestUrl())
            .sessionId(requestDetails.getSessionId())
            .build();
        
        // Log parameters if requested
        if (audited.logParameters()) {
            try {
                Map<String, Object> params = new HashMap<>();
                Object[] args = joinPoint.getArgs();
                String[] paramNames = signature.getParameterNames();
                
                for (int i = 0; i < args.length; i++) {
                    params.put(paramNames[i], args[i]);
                }
                
                auditLog.setRequestParameters(objectMapper.writeValueAsString(params));
            } catch (Exception e) {
                log.error("Error logging parameters", e);
            }
        }
        
        Object result = null;
        try {
            // Execute method
            result = joinPoint.proceed();
            
            // Calculate execution time
            long executionTime = System.currentTimeMillis() - startTime;
            auditLog.setExecutionTimeMillis(executionTime);
            auditLog.setIsSuccessful(true);
            auditLog.setResponseStatus(200);
            
            // Log result if requested
            if (audited.logResult() && result != null) {
                try {
                    auditLog.setNewValues(objectMapper.writeValueAsString(result));
                } catch (Exception e) {
                    log.error("Error logging result", e);
                }
            }
            
            // Save audit log asynchronously
            saveAuditLogAsync(auditLog);
            
            return result;
            
        } catch (Exception e) {
            // Log error
            auditLog.setIsSuccessful(false);
            auditLog.setErrorMessage(e.getMessage());
            auditLog.setResponseStatus(500);
            
            long executionTime = System.currentTimeMillis() - startTime;
            auditLog.setExecutionTimeMillis(executionTime);
            
            // Save audit log
            saveAuditLogAsync(auditLog);
            
            // Log error separately
            logError(e, className, methodName, userDetails, requestDetails);
            
            throw e;
        }
    }
    
    /**
     * After throwing advice for all service methods
     * Automatically log errors
     */
    @AfterThrowing(
        pointcut = "execution(* lk.epicgreen.erp..service..*(..))",
        throwing = "exception"
    )
    public void logServiceError(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = signature.getDeclaringTypeName();
        
        UserDetails userDetails = getCurrentUserDetails();
        RequestDetails requestDetails = getRequestDetails();
        
        logError(exception, className, methodName, userDetails, requestDetails);
    }
    
    /**
     * Log error to error log table
     */
    private void logError(
        Throwable exception,
        String className,
        String methodName,
        UserDetails userDetails,
        RequestDetails requestDetails
    ) {
        try {
            ErrorLog errorLog = ErrorLog.builder()
                .errorTimestamp(LocalDateTime.now())
                .severityLevel(determineSeverityLevel(exception))
                .errorType(determineErrorType(exception))
                .errorMessage(exception.getMessage())
                .exceptionClass(exception.getClass().getName())
                .stackTrace(getStackTraceAsString(exception))
                .rootCause(getRootCause(exception))
                .moduleName(extractModuleName(className))
                .className(className)
                .methodName(methodName)
                .userId(userDetails.getUserId())
                .username(userDetails.getUsername())
                .requestUrl(requestDetails.getRequestUrl())
                .requestMethod(requestDetails.getRequestMethod())
                .ipAddress(requestDetails.getIpAddress())
                .userAgent(requestDetails.getUserAgent())
                .sessionId(requestDetails.getSessionId())
                .status("NEW")
                .build();
            
            saveErrorLogAsync(errorLog);
            
        } catch (Exception e) {
            log.error("Error logging error", e);
        }
    }
    
    /**
     * Get current user details
     */
    private UserDetails getCurrentUserDetails() {
        UserDetails userDetails = new UserDetails();
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                userDetails.setUsername(authentication.getName());
                
                // Extract user ID and role from authentication
                // This depends on your security implementation
                // Adjust according to your UserDetails implementation
                if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                    org.springframework.security.core.userdetails.UserDetails principal = 
                        (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
                    
                    userDetails.setUsername(principal.getUsername());
                    
                    if (!principal.getAuthorities().isEmpty()) {
                        userDetails.setUserRole(principal.getAuthorities().iterator().next().getAuthority());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting user details", e);
        }
        
        return userDetails;
    }
    
    /**
     * Get request details
     */
    private RequestDetails getRequestDetails() {
        RequestDetails requestDetails = new RequestDetails();
        
        try {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                requestDetails.setIpAddress(getClientIpAddress(request));
                requestDetails.setUserAgent(request.getHeader("User-Agent"));
                requestDetails.setRequestMethod(request.getMethod());
                requestDetails.setRequestUrl(request.getRequestURI());
                requestDetails.setSessionId(request.getSession(false) != null ? 
                    request.getSession(false).getId() : null);
            }
        } catch (Exception e) {
            log.error("Error getting request details", e);
        }
        
        return requestDetails;
    }
    
    /**
     * Get client IP address
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
     * Extract module name from class name
     */
    private String extractModuleName(String className) {
        // Extract module from package name
        // e.g., lk.epicgreen.erp.sales.service.SalesOrderService -> SALES
        String[] parts = className.split("\\.");
        if (parts.length >= 4) {
            return parts[3].toUpperCase();
        }
        return "UNKNOWN";
    }
    
    /**
     * Extract entity type from class name
     */
    private String extractEntityType(String className) {
        String[] parts = className.split("\\.");
        String simpleClassName = parts[parts.length - 1];
        
        // Remove "Service", "Controller", "Repository" suffixes
        return simpleClassName
            .replace("Service", "")
            .replace("Controller", "")
            .replace("Repository", "")
            .toUpperCase();
    }
    
    /**
     * Extract action type from method name
     */
    private String extractActionType(String methodName) {
        if (methodName.startsWith("create") || methodName.startsWith("save") || methodName.startsWith("add")) {
            return "CREATE";
        } else if (methodName.startsWith("update") || methodName.startsWith("edit") || methodName.startsWith("modify")) {
            return "UPDATE";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "DELETE";
        } else if (methodName.startsWith("get") || methodName.startsWith("find") || methodName.startsWith("list")) {
            return "VIEW";
        } else if (methodName.startsWith("approve")) {
            return "APPROVE";
        } else if (methodName.startsWith("reject")) {
            return "REJECT";
        } else if (methodName.startsWith("post")) {
            return "POST";
        } else {
            return "EXECUTE";
        }
    }
    
    /**
     * Determine severity level from exception
     */
    private String determineSeverityLevel(Throwable exception) {
        String exceptionName = exception.getClass().getSimpleName();
        
        if (exceptionName.contains("NullPointer") || 
            exceptionName.contains("OutOfMemory") ||
            exceptionName.contains("StackOverflow")) {
            return "CRITICAL";
        } else if (exceptionName.contains("SQL") || 
                   exceptionName.contains("Database") ||
                   exceptionName.contains("Connection")) {
            return "HIGH";
        } else if (exceptionName.contains("Validation") || 
                   exceptionName.contains("Business")) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    /**
     * Determine error type from exception
     */
    private String determineErrorType(Throwable exception) {
        String exceptionName = exception.getClass().getSimpleName();
        
        if (exceptionName.contains("SQL") || exceptionName.contains("Database")) {
            return "DATABASE";
        } else if (exceptionName.contains("Validation")) {
            return "VALIDATION";
        } else if (exceptionName.contains("Business")) {
            return "BUSINESS";
        } else if (exceptionName.contains("Security") || exceptionName.contains("Access")) {
            return "SECURITY";
        } else if (exceptionName.contains("Network") || exceptionName.contains("Connection")) {
            return "NETWORK";
        } else {
            return "EXCEPTION";
        }
    }
    
    /**
     * Get stack trace as string
     */
    private String getStackTraceAsString(Throwable exception) {
        return Arrays.toString(exception.getStackTrace());
    }
    
    /**
     * Get root cause
     */
    private String getRootCause(Throwable exception) {
        Throwable cause = exception;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
    
    /**
     * Save audit log asynchronously
     */
    private void saveAuditLogAsync(AuditLog auditLog) {
        try {
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error saving audit log", e);
        }
    }
    
    /**
     * Save error log asynchronously
     */
    private void saveErrorLogAsync(ErrorLog errorLog) {
        try {
            errorLogRepository.save(errorLog);
        } catch (Exception e) {
            log.error("Error saving error log", e);
        }
    }
    
    /**
     * User details holder
     */
    @lombok.Data
    private static class UserDetails {
        private Long userId;
        private String username = "ANONYMOUS";
        private String userRole = "GUEST";
    }
    
    /**
     * Request details holder
     */
    @lombok.Data
    private static class RequestDetails {
        private String ipAddress;
        private String userAgent;
        private String requestMethod;
        private String requestUrl;
        private String sessionId;
    }
}
