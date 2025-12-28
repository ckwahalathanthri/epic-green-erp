package lk.epicgreen.erp.notification.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.notification.dto.NotificationRequest;
import lk.epicgreen.erp.notification.entity.NotificationQueue;
import lk.epicgreen.erp.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Notification Queue Controller
 * REST controller for notification queue and sending operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationQueueController {
    
    private final NotificationService notificationService;
    
    // Notification Sending Operations
    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<NotificationQueue>> sendNotification(@Valid @RequestBody NotificationRequest request) {
        log.info("Sending notification using template: {}", request.getTemplateCode());
        NotificationQueue sent = notificationService.sendNotification(request);
        return ResponseEntity.ok(ApiResponse.success(sent, "Notification queued successfully"));
    }
    
    @PostMapping("/send/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> sendBulkNotifications(@Valid @RequestBody NotificationRequest request) {
        log.info("Sending bulk notifications: {} recipients", request.getRecipients().size());
        List<NotificationQueue> sent = notificationService.sendBulkNotifications(request);
        return ResponseEntity.ok(ApiResponse.success(sent, sent.size() + " notifications queued successfully"));
    }
    
    @PostMapping("/send/email")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<NotificationQueue>> sendEmail(
        @RequestParam String to,
        @RequestParam String subject,
        @RequestParam String body,
        @RequestParam(required = false) String htmlBody
    ) {
        log.info("Sending email to: {}", to);
        NotificationQueue sent = htmlBody != null 
            ? notificationService.sendEmail(to, subject, body, htmlBody)
            : notificationService.sendEmail(to, subject, body);
        return ResponseEntity.ok(ApiResponse.success(sent, "Email queued successfully"));
    }
    
    @PostMapping("/send/sms")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<NotificationQueue>> sendSms(
        @RequestParam String to,
        @RequestParam String message
    ) {
        log.info("Sending SMS to: {}", to);
        NotificationQueue sent = notificationService.sendSms(to, message);
        return ResponseEntity.ok(ApiResponse.success(sent, "SMS queued successfully"));
    }
    
    @PostMapping("/send/push")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<NotificationQueue>> sendPush(
        @RequestParam String deviceToken,
        @RequestParam String title,
        @RequestParam String message
    ) {
        log.info("Sending push notification to: {}", deviceToken);
        NotificationQueue sent = notificationService.sendPush(deviceToken, title, message);
        return ResponseEntity.ok(ApiResponse.success(sent, "Push notification queued successfully"));
    }
    
    @PostMapping("/send/in-app")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<NotificationQueue>> sendInAppNotification(
        @RequestParam Long userId,
        @RequestParam String title,
        @RequestParam String message
    ) {
        log.info("Sending in-app notification to user: {}", userId);
        NotificationQueue sent = notificationService.sendInAppNotification(userId, title, message);
        return ResponseEntity.ok(ApiResponse.success(sent, "In-app notification queued successfully"));
    }
    
    // Schedule Notifications
    @PostMapping("/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<NotificationQueue>> scheduleNotification(
        @Valid @RequestBody NotificationRequest request,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime
    ) {
        log.info("Scheduling notification for: {}", scheduledTime);
        NotificationQueue scheduled = notificationService.scheduleNotification(request, scheduledTime);
        return ResponseEntity.ok(ApiResponse.success(scheduled, "Notification scheduled successfully"));
    }
    
    @PostMapping("/schedule/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> scheduleBulkNotifications(
        @Valid @RequestBody NotificationRequest request,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime
    ) {
        log.info("Scheduling bulk notifications for: {}", scheduledTime);
        List<NotificationQueue> scheduled = notificationService.scheduleBulkNotifications(request, scheduledTime);
        return ResponseEntity.ok(ApiResponse.success(scheduled, scheduled.size() + " notifications scheduled successfully"));
    }
    
    // Queue Query Operations
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<NotificationQueue>> getNotificationById(@PathVariable Long id) {
        NotificationQueue notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.success(notification, "Notification retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<NotificationQueue>>> getAllNotifications(Pageable pageable) {
        Page<NotificationQueue> notifications = notificationService.getAllNotifications(pageable);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> getAllNotificationsList() {
        List<NotificationQueue> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications list retrieved successfully"));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<NotificationQueue>>> getNotificationsByStatus(@PathVariable String status, Pageable pageable) {
        Page<NotificationQueue> notifications = notificationService.getNotificationsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications by status retrieved successfully"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> getPendingNotifications() {
        List<NotificationQueue> notifications = notificationService.getPendingNotifications();
        return ResponseEntity.ok(ApiResponse.success(notifications, "Pending notifications retrieved successfully"));
    }
    
    @GetMapping("/ready-to-send")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> getNotificationsReadyToSend(@RequestParam(defaultValue = "100") int limit) {
        List<NotificationQueue> notifications = notificationService.getNotificationsReadyToSend(limit);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications ready to send retrieved successfully"));
    }
    
    @GetMapping("/failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> getFailedNotifications() {
        List<NotificationQueue> notifications = notificationService.getFailedNotifications();
        return ResponseEntity.ok(ApiResponse.success(notifications, "Failed notifications retrieved successfully"));
    }
    
    @GetMapping("/failed/retry")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> getFailedNotificationsForRetry() {
        List<NotificationQueue> notifications = notificationService.getFailedNotificationsForRetry();
        return ResponseEntity.ok(ApiResponse.success(notifications, "Failed notifications for retry retrieved successfully"));
    }
    
    @GetMapping("/recipient/{recipient}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> getNotificationsByRecipient(@PathVariable String recipient) {
        List<NotificationQueue> notifications = notificationService.getNotificationsByRecipient(recipient);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications by recipient retrieved successfully"));
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationQueue> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications by user retrieved successfully"));
    }
    
    @GetMapping("/batch/{batchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> getNotificationsByBatchId(@PathVariable String batchId) {
        List<NotificationQueue> notifications = notificationService.getNotificationsByBatchId(batchId);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Batch notifications retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<NotificationQueue>>> searchNotifications(@RequestParam String keyword, Pageable pageable) {
        Page<NotificationQueue> notifications = notificationService.searchNotifications(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Search results retrieved successfully"));
    }
    
    // Processing Operations
    @PostMapping("/process/queue")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> processNotificationQueue() {
        log.info("Processing notification queue");
        notificationService.processNotificationQueue();
        return ResponseEntity.ok(ApiResponse.success(null, "Notification queue processing started"));
    }
    
    @PostMapping("/process/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> processPendingNotifications(@RequestParam(defaultValue = "100") int limit) {
        log.info("Processing pending notifications with limit: {}", limit);
        int processed = notificationService.processPendingNotifications(limit);
        return ResponseEntity.ok(ApiResponse.success(processed, processed + " notifications processed successfully"));
    }
    
    @PostMapping("/process/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> processNotification(@PathVariable Long id) {
        log.info("Processing notification: {}", id);
        boolean processed = notificationService.processNotification(id);
        return ResponseEntity.ok(ApiResponse.success(processed, "Notification processed " + (processed ? "successfully" : "with errors")));
    }
    
    @PostMapping("/retry/failed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> retryFailedNotifications(@RequestParam(defaultValue = "50") int limit) {
        log.info("Retrying failed notifications with limit: {}", limit);
        int retried = notificationService.retryFailedNotifications(limit);
        return ResponseEntity.ok(ApiResponse.success(retried, retried + " notifications retried successfully"));
    }
    
    @PostMapping("/retry/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> retryNotification(@PathVariable Long id) {
        log.info("Retrying notification: {}", id);
        boolean retried = notificationService.retryNotification(id);
        return ResponseEntity.ok(ApiResponse.success(retried, "Notification retried " + (retried ? "successfully" : "with errors")));
    }
    
    @GetMapping("/stuck")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<NotificationQueue>>> checkForStuckNotifications() {
        List<NotificationQueue> stuck = notificationService.checkForStuckNotifications();
        return ResponseEntity.ok(ApiResponse.success(stuck, stuck.size() + " stuck notifications found"));
    }
    
    @PostMapping("/stuck/reset")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> resetStuckNotifications() {
        log.info("Resetting stuck notifications");
        int reset = notificationService.resetStuckNotifications();
        return ResponseEntity.ok(ApiResponse.success(reset, reset + " stuck notifications reset successfully"));
    }
    
    // Cancel Operations
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> cancelNotification(@PathVariable Long id) {
        log.info("Cancelling notification: {}", id);
        boolean cancelled = notificationService.cancelNotification(id);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Notification " + (cancelled ? "cancelled" : "could not be cancelled")));
    }
    
    @PutMapping("/batch/{batchId}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> cancelBatchNotifications(@PathVariable String batchId) {
        log.info("Cancelling batch notifications: {}", batchId);
        int cancelled = notificationService.cancelBatchNotifications(batchId);
        return ResponseEntity.ok(ApiResponse.success(cancelled, cancelled + " notifications cancelled successfully"));
    }
    
    // Batch Operations
    @GetMapping("/batch/{batchId}/progress")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBatchProgress(@PathVariable String batchId) {
        Map<String, Object> progress = notificationService.getBatchProgress(batchId);
        return ResponseEntity.ok(ApiResponse.success(progress, "Batch progress retrieved successfully"));
    }
    
    @GetMapping("/batch/{batchId}/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getBatchStatistics(@PathVariable String batchId) {
        Map<String, Long> statistics = notificationService.getBatchStatistics(batchId);
        return ResponseEntity.ok(ApiResponse.success(statistics, "Batch statistics retrieved successfully"));
    }
    
    @GetMapping("/batch/{batchId}/is-complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Boolean>> isBatchComplete(@PathVariable String batchId) {
        boolean complete = notificationService.isBatchComplete(batchId);
        return ResponseEntity.ok(ApiResponse.success(complete, "Batch completion status retrieved"));
    }
    
    // Cleanup Operations
    @DeleteMapping("/cleanup/sent")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> deleteOldSentNotifications(@RequestParam(defaultValue = "30") int daysToKeep) {
        log.info("Deleting sent notifications older than {} days", daysToKeep);
        int deleted = notificationService.deleteOldSentNotifications(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(deleted, deleted + " sent notifications deleted successfully"));
    }
    
    @DeleteMapping("/cleanup/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Integer>> deleteOldCancelledNotifications(@RequestParam(defaultValue = "30") int daysToKeep) {
        log.info("Deleting cancelled notifications older than {} days", daysToKeep);
        int deleted = notificationService.deleteOldCancelledNotifications(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(deleted, deleted + " cancelled notifications deleted successfully"));
    }
    
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> cleanupOldNotifications(@RequestParam(defaultValue = "30") int daysToKeep) {
        log.info("Cleaning up old notifications older than {} days", daysToKeep);
        Map<String, Integer> results = notificationService.cleanupOldNotifications(daysToKeep);
        return ResponseEntity.ok(ApiResponse.success(results, "Cleanup completed successfully"));
    }
}
