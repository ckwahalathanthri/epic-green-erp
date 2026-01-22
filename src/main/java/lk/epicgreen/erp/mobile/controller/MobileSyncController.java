//package lk.epicgreen.erp.mobile.controller;
//
//import lk.epicgreen.erp.common.dto.ApiResponse;
//
//import lk.epicgreen.erp.mobile.dto.request.SyncLogRequest;
//import lk.epicgreen.erp.mobile.dto.response.SyncLogResponse;
//import lk.epicgreen.erp.mobile.entity.SyncLog;
//import lk.epicgreen.erp.mobile.entity.SyncQueue;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//
//import javax.validation.Valid;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
///**
// * Mobile Sync Controller
// * REST controller for mobile synchronization operations
// *
// * @author Epic Green Development Team
// * @version 1.0
// */
//@RestController
//@RequestMapping("/api/mobile/sync")
//@RequiredArgsConstructor
//@Slf4j
//@CrossOrigin(origins = "*", maxAge = 3600)
//public class MobileSyncController {
//
//    private final SyncService syncService;
//
//    // Sync Operations
//    @PostMapping("/synchronize")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncLogResponse>> synchronize(@Valid @RequestBody SyncLogRequest request) {
//        log.info("Synchronizing for user: {}, device: {}", request.getUserId(), request.getDeviceId());
//        SyncLogResponse response = syncService.synchronize(request);
//        return ResponseEntity.ok(ApiResponse.success(response, "Synchronization completed"));
//    }
//
//    @PostMapping("/push")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> pushData(@Valid @RequestBody SyncRequest request) {
//        log.info("Pushing data to server for user: {}", request.getUserId());
//        SyncResponse response = syncService.pushData(request);
//        return ResponseEntity.ok(ApiResponse.success(response, "Data pushed successfully"));
//    }
//
//    @PostMapping("/pull")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> pullData(@Valid @RequestBody SyncRequest request) {
//        log.info("Pulling data from server for user: {}", request.getUserId());
//        SyncResponse response = syncService.pullData(request);
//        return ResponseEntity.ok(ApiResponse.success(response, "Data pulled successfully"));
//    }
//
//    @PostMapping("/full-sync")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> fullSync(@Valid @RequestBody SyncRequest request) {
//        log.info("Full sync for user: {}", request.getUserId());
//        SyncResponse response = syncService.fullSync(request);
//        return ResponseEntity.ok(ApiResponse.success(response, "Full sync completed"));
//    }
//
//    @PostMapping("/incremental-sync")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> incrementalSync(@Valid @RequestBody SyncRequest request) {
//        log.info("Incremental sync for user: {}", request.getUserId());
//        SyncResponse response = syncService.incrementalSync(request);
//        return ResponseEntity.ok(ApiResponse.success(response, "Incremental sync completed"));
//    }
//
//    @PostMapping("/force-sync")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> forceSync(@Valid @RequestBody SyncRequest request) {
//        log.info("Force sync for user: {}", request.getUserId());
//        SyncResponse response = syncService.forceSync(request);
//        return ResponseEntity.ok(ApiResponse.success(response, "Force sync completed"));
//    }
//
//    // Entity-Specific Sync
//    @PostMapping("/sync/customers")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> syncCustomers(
//        @RequestParam Long userId,
//        @RequestParam String deviceId,
//        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
//    ) {
//        SyncResponse response = syncService.syncCustomers(userId, deviceId, lastSyncTime);
//        return ResponseEntity.ok(ApiResponse.success(response, "Customers synced successfully"));
//    }
//
//    @PostMapping("/sync/products")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> syncProducts(
//        @RequestParam Long userId,
//        @RequestParam String deviceId,
//        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
//    ) {
//        SyncResponse response = syncService.syncProducts(userId, deviceId, lastSyncTime);
//        return ResponseEntity.ok(ApiResponse.success(response, "Products synced successfully"));
//    }
//
//    @PostMapping("/sync/sales-orders")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> syncSalesOrders(
//        @RequestParam Long userId,
//        @RequestParam String deviceId,
//        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
//    ) {
//        SyncResponse response = syncService.syncSalesOrders(userId, deviceId, lastSyncTime);
//        return ResponseEntity.ok(ApiResponse.success(response, "Sales orders synced successfully"));
//    }
//
//    @PostMapping("/sync/payments")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> syncPayments(
//        @RequestParam Long userId,
//        @RequestParam String deviceId,
//        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
//    ) {
//        SyncResponse response = syncService.syncPayments(userId, deviceId, lastSyncTime);
//        return ResponseEntity.ok(ApiResponse.success(response, "Payments synced successfully"));
//    }
//
//    @PostMapping("/sync/inventory")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> syncInventory(
//        @RequestParam Long userId,
//        @RequestParam String deviceId,
//        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
//    ) {
//        SyncResponse response = syncService.syncInventory(userId, deviceId, lastSyncTime);
//        return ResponseEntity.ok(ApiResponse.success(response, "Inventory synced successfully"));
//    }
//
//    @PostMapping("/sync/price-lists")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> syncPriceLists(
//        @RequestParam Long userId,
//        @RequestParam String deviceId,
//        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
//    ) {
//        SyncResponse response = syncService.syncPriceLists(userId, deviceId, lastSyncTime);
//        return ResponseEntity.ok(ApiResponse.success(response, "Price lists synced successfully"));
//    }
//
//    @PostMapping("/sync/sales-reps")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> syncSalesReps(
//        @RequestParam Long userId,
//        @RequestParam String deviceId,
//        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSyncTime
//    ) {
//        SyncResponse response = syncService.syncSalesReps(userId, deviceId, lastSyncTime);
//        return ResponseEntity.ok(ApiResponse.success(response, "Sales reps synced successfully"));
//    }
//
//    // Bulk Operations
//    @PostMapping("/bulk/synchronize")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<SyncResponse>>> bulkSynchronize(@Valid @RequestBody List<SyncRequest> requests) {
//        log.info("Bulk synchronizing {} requests", requests.size());
//        List<SyncResponse> responses = syncService.bulkSynchronize(requests);
//        return ResponseEntity.ok(ApiResponse.success(responses, "Bulk sync completed"));
//    }
//
//    @PostMapping("/bulk/push")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<SyncResponse>>> bulkPushData(@Valid @RequestBody List<SyncRequest> requests) {
//        log.info("Bulk pushing {} requests", requests.size());
//        List<SyncResponse> responses = syncService.bulkPushData(requests);
//        return ResponseEntity.ok(ApiResponse.success(responses, "Bulk push completed"));
//    }
//
//    @PostMapping("/bulk/pull")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<List<SyncResponse>>> bulkPullData(@Valid @RequestBody List<SyncRequest> requests) {
//        log.info("Bulk pulling {} requests", requests.size());
//        List<SyncResponse> responses = syncService.bulkPullData(requests);
//        return ResponseEntity.ok(ApiResponse.success(responses, "Bulk pull completed"));
//    }
//
//    // Data Validation
//    @PostMapping("/validate")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<List<String>>> validateSyncData(@Valid @RequestBody SyncRequest request) {
//        List<String> errors = syncService.validateSyncData(request);
//        if (errors.isEmpty()) {
//            return ResponseEntity.ok(ApiResponse.success(errors, "Data is valid"));
//        } else {
//            return ResponseEntity.badRequest().body(ApiResponse.error("Validation failed", errors));
//        }
//    }
//
//    @PostMapping("/validate/entity")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<List<String>>> validateEntityData(
//        @RequestParam String entityType,
//        @RequestBody Map<String, Object> data
//    ) {
//        List<String> errors = syncService.validateEntityData(entityType, data);
//        if (errors.isEmpty()) {
//            return ResponseEntity.ok(ApiResponse.success(errors, "Entity data is valid"));
//        } else {
//            return ResponseEntity.badRequest().body(ApiResponse.error("Validation failed", errors));
//        }
//    }
//
//    @GetMapping("/check-integrity/{entityType}/{entityId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
//    public ResponseEntity<ApiResponse<Boolean>> checkDataIntegrity(
//        @PathVariable String entityType,
//        @PathVariable Long entityId
//    ) {
//        boolean isValid = syncService.checkDataIntegrity(entityType, entityId);
//        return ResponseEntity.ok(ApiResponse.success(isValid, "Data integrity checked"));
//    }
//
//    // Offline Support
//    @PostMapping("/offline/queue")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncQueue>> queueOfflineChange(
//        @RequestParam String entityType,
//        @RequestParam Long entityId,
//        @RequestParam String operation,
//        @RequestBody Map<String, Object> data,
//        @RequestParam Long userId,
//        @RequestParam String deviceId
//    ) {
//        SyncQueue queue = syncService.queueOfflineChange(entityType, entityId, operation, data, userId, deviceId);
//        return ResponseEntity.ok(ApiResponse.success(queue, "Offline change queued"));
//    }
//
//    @GetMapping("/offline/changes")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<List<SyncQueue>>> getOfflineChanges(
//        @RequestParam Long userId,
//        @RequestParam String deviceId
//    ) {
//        List<SyncQueue> changes = syncService.getOfflineChanges(userId, deviceId);
//        return ResponseEntity.ok(ApiResponse.success(changes, "Offline changes retrieved"));
//    }
//
//    @PostMapping("/offline/sync")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<SyncResponse>> syncOfflineChanges(
//        @RequestParam Long userId,
//        @RequestParam String deviceId
//    ) {
//        SyncResponse response = syncService.syncOfflineChanges(userId, deviceId);
//        return ResponseEntity.ok(ApiResponse.success(response, "Offline changes synced"));
//    }
//
//    @DeleteMapping("/offline/clear")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'MOBILE_USER')")
//    public ResponseEntity<ApiResponse<Integer>> clearOfflineChanges(
//        @RequestParam Long userId,
//        @RequestParam String deviceId
//    ) {
//        int cleared = syncService.clearOfflineChanges(userId, deviceId);
//        return ResponseEntity.ok(ApiResponse.success(cleared, "Offline changes cleared"));
//    }
//}
