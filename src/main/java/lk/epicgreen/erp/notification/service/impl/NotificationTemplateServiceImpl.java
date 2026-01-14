package lk.epicgreen.erp.notification.service.impl;

import lk.epicgreen.erp.notification.dto.request.NotificationTemplateRequest;
import lk.epicgreen.erp.notification.dto.response.NotificationTemplateResponse;
import lk.epicgreen.erp.notification.entity.NotificationTemplate;
import lk.epicgreen.erp.notification.mapper.NotificationTemplateMapper;
import lk.epicgreen.erp.notification.repository.NotificationTemplateRepository;
import lk.epicgreen.erp.notification.service.NotificationTemplateService;

import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of NotificationTemplateService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateRepository templateRepository;
    private final NotificationTemplateMapper templateMapper;

    @Override
    @Transactional
    public NotificationTemplateResponse createTemplate(NotificationTemplateRequest request) {
        log.info("Creating Notification Template: {}", request.getTemplateCode());

        validateUniqueTemplateCode(request.getTemplateCode(), null);

        NotificationTemplate template = templateMapper.toEntity(request);
        NotificationTemplate savedTemplate = templateRepository.save(template);

        log.info("Notification Template created successfully: {}", savedTemplate.getTemplateCode());

        return templateMapper.toResponse(savedTemplate);
    }

    @Transactional
    public NotificationTemplate activateTemplate(Long id){
        log.info("Activating Notification Template: {}", id);

        NotificationTemplate template = findTemplateById(id);
        template.setIsActive(true);
        NotificationTemplate activatedTemplate = templateRepository.save(template);

        log.info("Notification Template activated successfully: {}", activatedTemplate.getTemplateCode());

        return activatedTemplate;
    }

    @Transactional
    public NotificationTemplate deactivateTemplate(Long id){
        log.info("Deactivating Notification Template: {}", id);

        NotificationTemplate template = findTemplateById(id);
        template.setIsActive(false);
        NotificationTemplate deactivatedTemplate = templateRepository.save(template);

        log.info("Notification Template deactivated successfully: {}", deactivatedTemplate.getTemplateCode());

        return deactivatedTemplate;
    }

    public     String renderSubject(NotificationTemplateResponse template, Map<String, Object> variables){
        String renderedSubject = template.getSubjectTemplate();

        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                renderedSubject = renderedSubject.replace(placeholder, value);
            }
        }

        log.debug("Subject rendered successfully");
        return renderedSubject;
    }

    public     String renderBody(NotificationTemplateResponse template, Map<String, Object> variables){
        String renderedBody = template.getBodyTemplate();

        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                renderedBody = renderedBody.replace(placeholder, value);
            }
        }

        log.debug("Body rendered successfully");
        return renderedBody;
    }

    public String renderHtmlBody(NotificationTemplateResponse template, Map<String, Object> variables){
        String renderedHtmlBody = template.getBodyTemplate();

        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                renderedHtmlBody = renderedHtmlBody.replace(placeholder, value);
            }
        }

        log.debug("HTML Body rendered successfully");
        return renderedHtmlBody;
    }
    @Override
    @Transactional
    public NotificationTemplateResponse updateTemplate(Long id, NotificationTemplateRequest request) {
        log.info("Updating Notification Template: {}", id);

        NotificationTemplate template = findTemplateById(id);

        if (!template.getTemplateCode().equals(request.getTemplateCode())) {
            validateUniqueTemplateCode(request.getTemplateCode(), id);
        }

        templateMapper.updateEntityFromRequest(request, template);
        NotificationTemplate updatedTemplate = templateRepository.save(template);

        log.info("Notification Template updated successfully: {}", updatedTemplate.getTemplateCode());

        return templateMapper.toResponse(updatedTemplate);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long id) {
        log.info("Deleting Notification Template: {}", id);

        if (!canDelete(id)) {
            throw new ResourceNotFoundException("Cannot delete template. It may be used in notifications.");
        }

        templateRepository.deleteById(id);
        log.info("Notification Template deleted successfully: {}", id);
    }
//    @Transactional
//    public List<NotificationTemplate> getTemplatesByCategory(String category){
//        return templateRepository.findByCategory(category);
//    }

    @Override
    public NotificationTemplateResponse getTemplateById(Long id) {
        NotificationTemplate template = findTemplateById(id);
        return templateMapper.toResponse(template);
    }

    @Override
    public NotificationTemplateResponse getTemplateByCode(String templateCode) {
        NotificationTemplate template = templateRepository.findByTemplateCode(templateCode)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found: " + templateCode));
        return templateMapper.toResponse(template);
    }

    @Override
    public PageResponse<NotificationTemplateResponse> getAllTemplates(Pageable pageable) {
        Page<NotificationTemplate> templatePage = templateRepository.findAll(pageable);
        return createPageResponse(templatePage);
    }

    @Override
    public List<NotificationTemplateResponse> getTemplatesByType(String notificationType) {
        List<NotificationTemplate> templates = templateRepository.findByNotificationType(notificationType);
        return templates.stream()
            .map(templateMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationTemplateResponse> getActiveTemplates() {
        List<NotificationTemplate> templates = templateRepository.findByIsActiveTrue();
        return templates.stream()
            .map(templateMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public String renderTemplate(String templateCode, Map<String, Object> variables) {
        log.debug("Rendering template: {} with variables", templateCode);

        NotificationTemplate template = templateRepository.findByTemplateCode(templateCode)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found: " + templateCode));

        String renderedBody = template.getBodyTemplate();

        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                renderedBody = renderedBody.replace(placeholder, value);
            }
        }

        log.debug("Template rendered successfully");
        return renderedBody;
    }

    @Override
    public PageResponse<NotificationTemplateResponse> searchTemplates(String keyword, Pageable pageable) {
        Page<NotificationTemplate> templatePage = templateRepository.searchTemplates(keyword, pageable);
        return createPageResponse(templatePage);
    }

    @Override
    @Transactional
    public void toggleTemplateStatus(Long id, Boolean isActive) {
        log.info("Toggling template status: {} to {}", id, isActive);

        NotificationTemplate template = findTemplateById(id);
        template.setIsActive(isActive);
        templateRepository.save(template);

        log.info("Template status toggled successfully");
    }

    @Override
    public boolean canDelete(Long id) {
        return true;
    }

    @Override
    public List<NotificationTemplate> getTemplatesByCategory(String category) {
        return null;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private void validateUniqueTemplateCode(String templateCode, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = templateRepository.existsByTemplateCodeAndIdNot(templateCode, excludeId);
        } else {
            exists = templateRepository.existsByTemplateCode(templateCode);
        }

        if (exists) {
            throw new DuplicateResourceException("Template with code '" + templateCode + "' already exists");
        }
    }

    private NotificationTemplate findTemplateById(Long id) {
        return templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification Template not found: " + id));
    }

    private PageResponse<NotificationTemplateResponse> createPageResponse(Page<NotificationTemplate> templatePage) {
        List<NotificationTemplateResponse> content = templatePage.getContent().stream()
            .map(templateMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<NotificationTemplateResponse>builder()
            .content(content)
            .pageNumber(templatePage.getNumber())
            .pageSize(templatePage.getSize())
            .totalElements(templatePage.getTotalElements())
            .totalPages(templatePage.getTotalPages())
            .last(templatePage.isLast())
            .first(templatePage.isFirst())
            .empty(templatePage.isEmpty())
            .build();
    }
}
