package lk.epicgreen.erp.audit.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.audit.entity.ErrorLog;
import lk.epicgreen.erp.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Error Log Controller
 * REST controller for error log operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/audit/errors")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ErrorLogController {
    
    private final AuditService auditService;
    
    // Query Operations
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<ErrorLog>> getErrorLogById(@PathVariable Long id) {
        ErrorLog errorLog = auditService.getErrorLogById(id);
        return ResponseEntity.ok(ApiResponse.success(errorLog, "Error log retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<ErrorLog>>> getAllErrorLogs(Pageable pageable) {
        Page<ErrorLog> errorLogs = auditService.getAllErrorLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success(errorLogs, "Error logs retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<List<ErrorLog>>> getAllErrorLogsList() {
        List<ErrorLog> errorLogs = auditService.getAllErrorLogs();
        return ResponseEntity.ok(ApiResponse.success(errorLogs, "Error logs list retrieved successfully"));
    }
    
    @GetMapping("/severity/{severityLevel}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<ErrorLog>>> getErrorLogsBySeverity(@PathVariable String severityLevel, Pageable pageable) {
        Page<ErrorLog> errorLogs = auditService.getErrorLogsBySeverity(severityLevel, pageable);
        return ResponseEntity.ok(ApiResponse.success(errorLogs, "Error logs by severity retrieved successfully"));
    }
    
    @GetMapping("/critical")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<ErrorLog>>> getCriticalErrors(Pageable pageable) {
        Page<ErrorLog> criticalErrors = auditService.getCriticalErrors(pageable);
        return ResponseEntity.ok(ApiResponse.success(criticalErrors, "Critical errors retrieved successfully"));
    }
    
    @GetMapping("/unresolved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ApiResponse<Page<ErrorLog>>> getUnresolvedErrors(Pageable pageable) {
        Page<ErrorLog> unresolvedErrors = auditService.getUnresolvedErrors(pageable);
        return ResponseEntity.ok(ApiResponse.success(unresolvedErrors, "Unresolved errors retrieved successfully"));
    }
    
    // Error Management Operations
    @PutMapping("/{errorId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ErrorLog>> updateErrorStatus(@PathVariable Long errorId, @RequestParam String status) {
        log.info("Updating error status: {}", errorId);
        ErrorLog updated = auditService.updateErrorStatus(errorId, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Error status updated successfully"));
    }
    
    @PutMapping("/{errorId}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ErrorLog>> assignError(@PathVariable Long errorId, @RequestParam Long userId) {
        log.info("Assigning error {} to user {}", errorId, userId);
        ErrorLog assigned = auditService.assignError(errorId, userId);
        return ResponseEntity.ok(ApiResponse.success(assigned, "Error assigned successfully"));
    }
    
    @PutMapping("/{errorId}/resolve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ErrorLog>> resolveError(@PathVariable Long errorId, @RequestParam String resolutionNotes) {
        log.info("Resolving error: {}", errorId);
        ErrorLog resolved = auditService.resolveError(errorId, resolutionNotes);
        return ResponseEntity.ok(ApiResponse.success(resolved, "Error resolved successfully"));
    }
}
