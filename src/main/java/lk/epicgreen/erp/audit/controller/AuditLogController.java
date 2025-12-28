package lk.epicgreen.erp.audit.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.audit.entity.AuditLog;
import lk.epicgreen.erp.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Audit Log Controller
 * REST controller for audit log operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/audit/logs")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuditLogController {
    
    private final AuditService auditService;
    
    // Query Operations
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<AuditLog>> getAuditLogById(@PathVariable Long id) {
        AuditLog auditLog = auditService.getAuditLogById(id);
        return ResponseEntity.ok(ApiResponse.success(auditLog, "Audit log retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAllAuditLogs(Pageable pageable) {
        Page<AuditLog> auditLogs = auditService.getAllAuditLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAllAuditLogsList() {
        List<AuditLog> auditLogs = auditService.getAllAuditLogs();
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs list retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogsByUserId(@PathVariable Long userId, Pageable pageable) {
        Page<AuditLog> auditLogs = auditService.getAuditLogsByUserId(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs by user retrieved successfully"));
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogsByUsername(@PathVariable String username, Pageable pageable) {
        Page<AuditLog> auditLogs = auditService.getAuditLogsByUsername(username, pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs by username retrieved successfully"));
    }
    
    @GetMapping("/module/{moduleName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogsByModule(@PathVariable String moduleName, Pageable pageable) {
        Page<AuditLog> auditLogs = auditService.getAuditLogsByModule(moduleName, pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs by module retrieved successfully"));
    }
    
    @GetMapping("/action/{actionType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogsByActionType(@PathVariable String actionType, Pageable pageable) {
        Page<AuditLog> auditLogs = auditService.getAuditLogsByActionType(actionType, pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs by action type retrieved successfully"));
    }
    
    @GetMapping("/entity/{entityType}/{entityId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogsByEntity(
        @PathVariable String entityType,
        @PathVariable Long entityId,
        Pageable pageable
    ) {
        Page<AuditLog> auditLogs = auditService.getAuditLogsByEntity(entityType, entityId, pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs by entity retrieved successfully"));
    }
    
    @GetMapping("/entity/{entityType}/{entityId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR', 'USER')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getEntityHistory(
        @PathVariable String entityType,
        @PathVariable Long entityId
    ) {
        List<AuditLog> history = auditService.getEntityHistory(entityType, entityId);
        return ResponseEntity.ok(ApiResponse.success(history, "Entity history retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        Pageable pageable
    ) {
        Page<AuditLog> auditLogs = auditService.getAuditLogsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs by date range retrieved successfully"));
    }
    
    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AuditLog> auditLogs = auditService.getAuditLogsByDate(date);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Audit logs by date retrieved successfully"));
    }
    
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getTodayAuditLogs() {
        List<AuditLog> auditLogs = auditService.getTodayAuditLogs();
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Today's audit logs retrieved successfully"));
    }
    
    @GetMapping("/failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getFailedAuditLogs(Pageable pageable) {
        Page<AuditLog> auditLogs = auditService.getFailedAuditLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Failed audit logs retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> searchAuditLogs(@RequestParam String keyword, Pageable pageable) {
        Page<AuditLog> auditLogs = auditService.searchAuditLogs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Search results retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getRecentAuditLogs(@RequestParam(defaultValue = "100") int limit) {
        List<AuditLog> auditLogs = auditService.getRecentAuditLogs(limit);
        return ResponseEntity.ok(ApiResponse.success(auditLogs, "Recent audit logs retrieved successfully"));
    }
    
    @GetMapping("/slow-queries")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getSlowQueries(
        @RequestParam(defaultValue = "1000") Long thresholdMillis,
        Pageable pageable
    ) {
        Page<AuditLog> slowQueries = auditService.getSlowQueries(thresholdMillis, pageable);
        return ResponseEntity.ok(ApiResponse.success(slowQueries, "Slow queries retrieved successfully"));
    }
    
    // Cleanup Operations
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteOldAuditLogs(@RequestParam(defaultValue = "90") int daysToKeep) {
        log.info("Deleting audit logs older than {} days", daysToKeep);
        auditService.deleteOldAuditLogs(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(null, "Old audit logs deleted successfully"));
    }
}
