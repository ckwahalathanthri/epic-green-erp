package lk.epicgreen.erp.notification.service;

import lk.epicgreen.erp.notification.dto.request.NotificationTemplateRequest;
import lk.epicgreen.erp.notification.dto.response.NotificationTemplateResponse;
import lk.epicgreen.erp.notification.entity.NotificationTemplate;

import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Notification Template entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface NotificationTemplateService {

    /**
     * Create notification template
     */
    NotificationTemplateResponse createTemplate(NotificationTemplateRequest request);

    /**
     * Update notification template
     */
    NotificationTemplateResponse updateTemplate(Long id, NotificationTemplateRequest request);

    /**
     * Delete notification template
     */
    void deleteTemplate(Long id);

    /**
     * Get Notification Template by ID
     */
    NotificationTemplateResponse getTemplateById(Long id);

    /**
     * Get Notification Template by code
     */
    NotificationTemplateResponse getTemplateByCode(String templateCode);

    /**
     * Get all Notification Templates (paginated)
     */
    PageResponse<NotificationTemplateResponse> getAllTemplates(Pageable pageable);

    /**
     * Get Templates by notification type
     */
    List<NotificationTemplateResponse> getTemplatesByType(String notificationType);

    /**
     * Get active templates
     */
    List<NotificationTemplateResponse> getActiveTemplates();

    /**
     * Render template with variables
     */
    String renderTemplate(String templateCode, Map<String, Object> variables);

    /**
     * Search templates
     */
    PageResponse<NotificationTemplateResponse> searchTemplates(String keyword, Pageable pageable);

    /**
     * Activate/Deactivate template
     */
    void toggleTemplateStatus(Long id, Boolean isActive);

    /**
     * Check if can delete
     */
    boolean canDelete(Long id);

    List<NotificationTemplate> getTemplatesByCategory(String category);
    NotificationTemplate activateTemplate(Long id);

    NotificationTemplate deactivateTemplate(Long id);

    String renderSubject(NotificationTemplateResponse template, Map<String, Object> variables);

    String renderBody(NotificationTemplateResponse template, Map<String, Object> variables);

    String renderHtmlBody(NotificationTemplateResponse template, Map<String, Object> variables);
}
