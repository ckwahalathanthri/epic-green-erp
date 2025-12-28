package lk.epicgreen.erp.notifications.service.impl;

import lk.epicgreen.erp.notifications.dto.request.NotificationQueueRequest;
import lk.epicgreen.erp.notifications.dto.response.NotificationQueueResponse;
import lk.epicgreen.erp.notifications.entity.NotificationQueue;
import lk.epicgreen.erp.notifications.entity.NotificationTemplate;
import lk.epicgreen.erp.notifications.mapper.NotificationQueueMapper;
import lk.epicgreen.erp.notifications.repository.NotificationQueueRepository;
import lk.epicgreen.erp.notifications.repository.NotificationTemplateRepository;
import lk.epicgreen.erp.notifications.service.NotificationQueueService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of NotificationQueueService interface
 * 
 * Notification Queue Status Workflow:
 * PENDING → SENT (sent successfully)
 * PENDING → FAILED (send failed, can retry)
 * Any status → CANCELLED (manually cancelled)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationQueueServiceImpl implements NotificationQueueService {

    private final NotificationQueueRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationQueueMapper notificationMapper;

    @Override
    @Transactional
    public NotificationQueueResponse createNotification(NotificationQueueRequest request) {
        log.info("Creating Notification for type: {}", request.getNotificationType());

        NotificationQueue notification = notificationMapper.toEntity(request);

        if (request.getTemplateId() != null) {
            NotificationTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found: " + request.getTemplateId()));
            notification.setTemplate(template);
        }

        NotificationQueue savedNotification = notificationRepository.save(notification);
        log.info("Notification created successfully with ID: {}", savedNotification.getId());

        return notificationMapper.toResponse(savedNotification);
    }

    @Override
    @Transactional
    public NotificationQueueResponse createFromTemplate(String templateCode, Long recipientUserId, 
                                                        Map<String, Object> variables) {
        log.info("Creating notification from template: {} for user: {}", templateCode, recipientUserId);

        NotificationTemplate template = templateRepository.findByTemplateCode(templateCode)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found: " + templateCode));

        String renderedMessage = renderTemplateBody(template.getBodyTemplate(), variables);
        String renderedSubject = template.getSubject() != null ? 
            renderTemplateBody(template.getSubject(), variables) : null;

        NotificationQueue notification = NotificationQueue.builder()
            .recipientUserId(recipientUserId)
            .notificationType(template.getNotificationType())
            .template(template)
            .subject(renderedSubject)
            .message(renderedMessage)
            .priority(5)
            .status("PENDING")
            .retryCount(0)
            .maxRetries(3)
            .build();

        NotificationQueue savedNotification = notificationRepository.save(notification);
        log.info("Notification created from template successfully");

        return notificationMapper.toResponse(savedNotification);
    }

    @Override
    @Transactional
    public NotificationQueueResponse updateNotification(Long id, NotificationQueueRequest request) {
        log.info("Updating Notification: {}", id);

        NotificationQueue notification = findNotificationById(id);

        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update notification. Current status: " + notification.getStatus() + 
                ". Only PENDING notifications can be updated.");
        }

        notificationMapper.updateEntityFromRequest(request, notification);

        if (request.getTemplateId() != null && 
            (notification.getTemplate() == null || !notification.getTemplate().getId().equals(request.getTemplateId()))) {
            NotificationTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found: " + request.getTemplateId()));
            notification.setTemplate(template);
        }

        NotificationQueue updatedNotification = notificationRepository.save(notification);
        log.info("Notification updated successfully");

        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    @Transactional
    public void sendNotification(Long id) {
        log.info("Sending notification: {}", id);

        NotificationQueue notification = findNotificationById(id);

        if (!"PENDING".equals(notification.getStatus())) {
            throw new InvalidOperationException("Only PENDING notifications can be sent");
        }

        // TODO: Implement actual sending logic here (email, SMS, push, etc.)
        // This is a placeholder for the actual sending implementation

        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);

        log.info("Notification sent successfully");
    }

    @Override
    @Transactional
    public void markAsSent(Long id) {
        log.info("Marking notification as sent: {}", id);

        NotificationQueue notification = findNotificationById(id);
        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);

        log.info("Notification marked as sent");
    }

    @Override
    @Transactional
    public void markAsFailed(Long id, String errorMessage) {
        log.error("Marking notification as failed: {} - Error: {}", id, errorMessage);

        NotificationQueue notification = findNotificationById(id);
        notification.setStatus("FAILED");
        notification.setErrorMessage(errorMessage);
        notification.setRetryCount(notification.getRetryCount() + 1);
        notificationRepository.save(notification);

        log.error("Notification marked as failed");
    }

    @Override
    @Transactional
    public void cancelNotification(Long id) {
        log.info("Cancelling notification: {}", id);

        NotificationQueue notification = findNotificationById(id);

        if ("SENT".equals(notification.getStatus())) {
            throw new InvalidOperationException("Cannot cancel already sent notification");
        }

        notification.setStatus("CANCELLED");
        notificationRepository.save(notification);

        log.info("Notification cancelled successfully");
    }

    @Override
    @Transactional
    public void retryNotification(Long id) {
        log.info("Retrying notification: {}", id);

        NotificationQueue notification = findNotificationById(id);

        if (!"FAILED".equals(notification.getStatus())) {
            throw new InvalidOperationException("Only FAILED notifications can be retried");
        }

        if (notification.getRetryCount() >= notification.getMaxRetries()) {
            throw new InvalidOperationException(
                "Maximum retry attempts reached (" + notification.getMaxRetries() + ")");
        }

        notification.setStatus("PENDING");
        notification.setErrorMessage(null);
        notificationRepository.save(notification);

        log.info("Notification queued for retry");
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        log.info("Deleting notification: {}", id);

        if (!canDelete(id)) {
            throw new InvalidOperationException("Cannot delete sent notification");
        }

        notificationRepository.deleteById(id);
        log.info("Notification deleted successfully");
    }

    @Override
    public NotificationQueueResponse getNotificationById(Long id) {
        NotificationQueue notification = findNotificationById(id);
        return notificationMapper.toResponse(notification);
    }

    @Override
    public PageResponse<NotificationQueueResponse> getAllNotifications(Pageable pageable) {
        Page<NotificationQueue> notificationPage = notificationRepository.findAll(pageable);
        return createPageResponse(notificationPage);
    }

    @Override
    public PageResponse<NotificationQueueResponse> getNotificationsByStatus(String status, Pageable pageable) {
        Page<NotificationQueue> notificationPage = notificationRepository.findByStatus(status, pageable);
        return createPageResponse(notificationPage);
    }

    @Override
    public List<NotificationQueueResponse> getNotificationsByType(String notificationType) {
        List<NotificationQueue> notifications = notificationRepository.findByNotificationType(notificationType);
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationQueueResponse> getNotificationsByRecipient(Long recipientUserId) {
        List<NotificationQueue> notifications = notificationRepository.findByRecipientUserId(recipientUserId);
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationQueueResponse> getPendingNotifications() {
        List<NotificationQueue> notifications = notificationRepository.findByStatus("PENDING");
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationQueueResponse> getFailedNotifications() {
        List<NotificationQueue> notifications = notificationRepository.findByStatus("FAILED");
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationQueueResponse> getScheduledNotifications() {
        List<NotificationQueue> notifications = notificationRepository.findByStatusAndScheduledAtNotNull("PENDING");
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationQueueResponse> getDueNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<NotificationQueue> notifications = 
            notificationRepository.findByStatusAndScheduledAtLessThanEqual("PENDING", now);
        return notifications.stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void processQueue() {
        log.info("Processing notification queue");

        List<NotificationQueue> dueNotifications = 
            notificationRepository.findByStatusAndScheduledAtLessThanEqual("PENDING", LocalDateTime.now());

        for (NotificationQueue notification : dueNotifications) {
            try {
                sendNotification(notification.getId());
            } catch (Exception e) {
                log.error("Failed to send notification {}: {}", notification.getId(), e.getMessage());
                markAsFailed(notification.getId(), e.getMessage());
            }
        }

        log.info("Notification queue processing completed. Processed: {}", dueNotifications.size());
    }

    @Override
    public PageResponse<NotificationQueueResponse> searchNotifications(String keyword, Pageable pageable) {
        Page<NotificationQueue> notificationPage = notificationRepository.searchNotifications(keyword, pageable);
        return createPageResponse(notificationPage);
    }

    @Override
    public boolean canUpdate(Long id) {
        NotificationQueue notification = findNotificationById(id);
        return "PENDING".equals(notification.getStatus());
    }

    @Override
    public boolean canDelete(Long id) {
        NotificationQueue notification = findNotificationById(id);
        return !"SENT".equals(notification.getStatus());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private String renderTemplateBody(String template, Map<String, Object> variables) {
        if (template == null || variables == null || variables.isEmpty()) {
            return template;
        }

        String rendered = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            rendered = rendered.replace(placeholder, value);
        }
        return rendered;
    }

    private NotificationQueue findNotificationById(Long id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
    }

    private PageResponse<NotificationQueueResponse> createPageResponse(Page<NotificationQueue> notificationPage) {
        List<NotificationQueueResponse> content = notificationPage.getContent().stream()
            .map(notificationMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<NotificationQueueResponse>builder()
            .content(content)
            .pageNumber(notificationPage.getNumber())
            .pageSize(notificationPage.getSize())
            .totalElements(notificationPage.getTotalElements())
            .totalPages(notificationPage.getTotalPages())
            .last(notificationPage.isLast())
            .first(notificationPage.isFirst())
            .empty(notificationPage.isEmpty())
            .build();
    }
}
