//package lk.epicgreen.erp.notification.controller;
//
//import lk.epicgreen.erp.common.dto.ApiResponse;
//import lk.epicgreen.erp.notification.service.NotificationService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
///**
// * Notification Statistics Controller
// * REST controller for notification statistics and reporting
// *
// * @author Epic Green Development Team
// * @version 1.0
// */
//@RestController
//@RequestMapping("/api/notifications/statistics")
//@RequiredArgsConstructor
//@Slf4j
//@CrossOrigin(origins = "*", maxAge = 3600)
//public class NotificationStatisticsController {
//
//    private final lk.epicgreen.erp.notifications.service.impl.InAppNotificationServiceImpl notificationService;
//
//    // Statistics Operations
//    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotificationStatistics() {
//        Map<String, Object> statistics = notificationService.getNotificationStatistics();
//        return ResponseEntity.ok(ApiResponse.success(statistics, "Notification statistics retrieved successfully"));
//    }
//
//    @GetMapping("/date-range")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotificationStatisticsByDateRange(
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
//    ) {
//        Map<String, Object> statistics = notificationService.getNotificationStatistics(startDate, endDate);
//        return ResponseEntity.ok(ApiResponse.success(statistics, "Notification statistics by date range retrieved successfully"));
//    }
//
//    @GetMapping("/status-distribution")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNotificationStatusDistribution() {
//        List<Map<String, Object>> distribution = notificationService.getNotificationStatusDistribution();
//        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
//    }
//
//    @GetMapping("/type-distribution")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNotificationTypeDistribution() {
//        List<Map<String, Object>> distribution = notificationService.getNotificationTypeDistribution();
//        return ResponseEntity.ok(ApiResponse.success(distribution, "Type distribution retrieved successfully"));
//    }
//
//    @GetMapping("/priority-distribution")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNotificationPriorityDistribution() {
//        List<Map<String, Object>> distribution = notificationService.getNotificationPriorityDistribution();
//        return ResponseEntity.ok(ApiResponse.success(distribution, "Priority distribution retrieved successfully"));
//    }
//
//    @GetMapping("/template-usage")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTemplateUsageStatistics() {
//        List<Map<String, Object>> usage = notificationService.getTemplateUsageStatistics();
//        return ResponseEntity.ok(ApiResponse.success(usage, "Template usage statistics retrieved successfully"));
//    }
//
//    @GetMapping("/success-rate")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<Double>> getSuccessRate() {
//        Double rate = notificationService.getSuccessRate();
//        return ResponseEntity.ok(ApiResponse.success(rate, "Success rate retrieved successfully"));
//    }
//
//    @GetMapping("/success-rate-by-type")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSuccessRateByType() {
//        List<Map<String, Object>> rates = notificationService.getSuccessRateByType();
//        return ResponseEntity.ok(ApiResponse.success(rates, "Success rate by type retrieved successfully"));
//    }
//
//    @GetMapping("/provider-performance")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProviderPerformance() {
//        List<Map<String, Object>> performance = notificationService.getProviderPerformance();
//        return ResponseEntity.ok(ApiResponse.success(performance, "Provider performance retrieved successfully"));
//    }
//
//    @GetMapping("/hourly")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getHourlyNotificationCount(
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
//    ) {
//        List<Map<String, Object>> count = notificationService.getHourlyNotificationCount(startDate, endDate);
//        return ResponseEntity.ok(ApiResponse.success(count, "Hourly notification count retrieved successfully"));
//    }
//
//    @GetMapping("/daily")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDailyNotificationCount(
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
//    ) {
//        List<Map<String, Object>> count = notificationService.getDailyNotificationCount(startDate, endDate);
//        return ResponseEntity.ok(ApiResponse.success(count, "Daily notification count retrieved successfully"));
//    }
//
//    @GetMapping("/dashboard")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
//        Map<String, Object> dashboard = notificationService.getDashboardStatistics();
//        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
//    }
//}
