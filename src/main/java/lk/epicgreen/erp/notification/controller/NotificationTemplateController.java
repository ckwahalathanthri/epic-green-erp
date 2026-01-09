package lk.epicgreen.erp.notification.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.notification.dto.request.NotificationTemplateRequest;
import lk.epicgreen.erp.notification.dto.response.NotificationTemplateResponse;
import lk.epicgreen.erp.notification.entity.NotificationTemplate;
import lk.epicgreen.erp.notification.service.impl.NotificationTemplateServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notification Template Controller
 * REST controller for notification template operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/notifications/templates")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationTemplateController {
    
    private final NotificationTemplateServiceImpl notificationService;
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> createTemplate(@Valid @RequestBody NotificationTemplateRequest request) {
        log.info("Creating notification template: {}", request.getTemplateCode());
        NotificationTemplateResponse created = notificationService.createTemplate(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Template created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> updateTemplate(@PathVariable Long id, @Valid @RequestBody NotificationTemplateRequest request) {
        log.info("Updating notification template: {}", id);
        NotificationTemplateResponse updated = notificationService.updateTemplate(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Template updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long id) {
        log.info("Deleting notification template: {}", id);
        notificationService.deleteTemplate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Template deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> getTemplateById(@PathVariable Long id) {
        NotificationTemplateResponse template = notificationService.getTemplateById(id);
        return ResponseEntity.ok(ApiResponse.success(template, "Template retrieved successfully"));
    }
    
    @GetMapping("/code/{templateCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> getTemplateByCode(@PathVariable String templateCode) {
        NotificationTemplateResponse template = notificationService.getTemplateByCode(templateCode);
        return ResponseEntity.ok(ApiResponse.success(template, "Template retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationTemplateResponse>>> getAllTemplates(Pageable pageable) {
        PageResponse<NotificationTemplateResponse> templates = notificationService.getAllTemplates(pageable);
        return ResponseEntity.ok(ApiResponse.success(templates, "Templates retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationTemplateResponse>>> getAllTemplatesList(Pageable pageable) {
        PageResponse<NotificationTemplateResponse> templates = notificationService.getAllTemplates(pageable);
        return ResponseEntity.ok(ApiResponse.success(templates, "Templates list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationTemplateResponse>>> searchTemplates(@RequestParam String keyword, Pageable pageable) {
        PageResponse<NotificationTemplateResponse> templates = notificationService.searchTemplates(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(templates, "Search results retrieved successfully"));
    }
    
    // Template Type Queries
    @GetMapping("/type/{notificationType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<NotificationTemplateResponse>>> getTemplatesByType(@PathVariable String notificationType) {
        List<NotificationTemplateResponse> templates = notificationService.getTemplatesByType(notificationType);
        return ResponseEntity.ok(ApiResponse.success(templates, "Templates by type retrieved successfully"));
    }
    
    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<NotificationTemplate>>> getTemplatesByCategory(@PathVariable String category) {
        List<NotificationTemplate> templates = notificationService.getTemplatesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(templates, "Templates by category retrieved successfully"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<NotificationTemplateResponse>>> getActiveTemplates() {
        List<NotificationTemplateResponse> templates = notificationService.getActiveTemplates();
        return ResponseEntity.ok(ApiResponse.success(templates, "Active templates retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<NotificationTemplate>> activateTemplate(@PathVariable Long id) {
        log.info("Activating template: {}", id);
        NotificationTemplate activated = notificationService.activateTemplate(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Template activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<NotificationTemplate>> deactivateTemplate(@PathVariable Long id) {
        log.info("Deactivating template: {}", id);
        NotificationTemplate deactivated = notificationService.deactivateTemplate(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Template deactivated successfully"));
    }
    
    // Template Rendering
    @PostMapping("/{templateCode}/render")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Map<String,Object>>> renderTemplate(
        @PathVariable String templateCode,
        @RequestBody Map<String, Object> variables
    ) {
        NotificationTemplateResponse template = notificationService.getTemplateByCode(templateCode);
        
        String subject = notificationService.renderSubject(template, variables);
        String body = notificationService.renderBody(template, variables);
        String htmlBody = notificationService.renderHtmlBody(template, variables);
        


        Map<String,Object> map=new HashMap<>();
        map.put("subject",subject);
        map.put("body",body);
        map.put("htmlBody",htmlBody != null ? htmlBody : "");
        
        return ResponseEntity.ok(ApiResponse.success(map, "Template rendered successfully"));
    }
}
