package lk.epicgreen.erp.production.controller;


import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.production.dto.request.ProductionOutputRequest;
import lk.epicgreen.erp.production.dto.response.ProductionOutputResponse;
import lk.epicgreen.erp.production.service.ProductionOutputService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Production Output Controller
 * REST controller for production output operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/production/outputs")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductionOutputController {
    
    private final ProductionOutputService productionService;
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<ProductionOutputResponse>> createProductionOutput(@Valid @RequestBody ProductionOutputRequest request) {
        log.info("Creating production output for work order: {}", request.getWorkOrderId());
        ProductionOutputResponse created = productionService.createProductionOutput(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Production output created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<ProductionOutputResponse>> updateProductionOutput(@PathVariable Long id, @Valid @RequestBody ProductionOutputRequest request) {
        log.info("Updating production output: {}", id);
        ProductionOutputResponse updated = productionService.updateProductionOutput(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Production output updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteProductionOutput(@PathVariable Long id) {
        log.info("Deleting production output: {}", id);
        productionService.deleteProductionOutput(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Production output deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<ProductionOutputResponse>> getProductionOutputById(@PathVariable Long id) {
        ProductionOutputResponse output = productionService.getProductionOutputById(id);
        return ResponseEntity.ok(ApiResponse.success(output, "Production output retrieved successfully"));
    }
    
    @GetMapping("/number/{outputNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<ProductionOutputResponse>> getProductionOutputByNumber(@PathVariable String outputNumber) {
        ProductionOutputResponse output = productionService.getProductionOutputByNumber(outputNumber);
        return ResponseEntity.ok(ApiResponse.success(output, "Production output retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<ProductionOutputResponse>>> getAllProductionOutputs(Pageable pageable) {
        Page<ProductionOutputResponse> outputs = productionService.getAllProductionOutputs(pageable);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getAllProductionOutputsList() {
        List<ProductionOutputResponse> outputs = productionService.getAllProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<ProductionOutputResponse>>> searchProductionOutputs(@RequestParam String keyword, Pageable pageable) {
        Page<ProductionOutputResponse> outputs = productionService.searchProductionOutputs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductionOutputResponse>> verifyProductionOutput(
        @PathVariable Long id,
        @RequestParam Long verifiedByUserId,
        @RequestParam(required = false) String verificationNotes
    ) {
        log.info("Verifying production output: {}", id);
        ProductionOutputResponse verified = productionService.verifyProductionOutput(id, verifiedByUserId, verificationNotes);
        return ResponseEntity.ok(ApiResponse.success(verified, "Production output verified successfully"));
    }
    
    @PutMapping("/{id}/post-to-inventory")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductionOutputResponse>> postToInventory(@PathVariable Long id) {
        log.info("Posting production output to inventory: {}", id);
        ProductionOutputResponse posted = productionService.postToInventory(id);
        return ResponseEntity.ok(ApiResponse.success(posted, "Production output posted to inventory successfully"));
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductionOutputResponse>> rejectProductionOutput(@PathVariable Long id, @RequestParam String rejectionReason) {
        log.info("Rejecting production output: {}", id);
        ProductionOutputResponse rejected = productionService.rejectProductionOutput(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Production output rejected"));
    }
    
    // Quality Operations
    @PutMapping("/{id}/quality-check")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> recordQualityCheck(@PathVariable Long id, @RequestParam String qualityStatus) {
        log.info("Recording quality check for production output: {}", id);
        productionService.recordQualityCheck(id, qualityStatus);
        return ResponseEntity.ok(ApiResponse.success(null, "Quality check recorded successfully"));
    }
    
    @PutMapping("/{id}/update-quantities")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> updateOutputQuantities(
        @PathVariable Long id,
        @RequestParam Double goodQuantity,
        @RequestParam Double rejectedQuantity
    ) {
        log.info("Updating quantities for production output: {}", id);
        productionService.updateOutputQuantities(id, goodQuantity, rejectedQuantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Output quantities updated successfully"));
    }
    
    // Query Operations
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getPendingProductionOutputs() {
        List<ProductionOutputResponse> outputs = productionService.getPendingProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Pending production outputs retrieved successfully"));
    }
    
    @GetMapping("/verified")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getVerifiedProductionOutputs() {
        List<ProductionOutputResponse> outputs = productionService.getVerifiedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Verified production outputs retrieved successfully"));
    }
    
    @GetMapping("/posted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getPostedProductionOutputs() {
        List<ProductionOutputResponse> outputs = productionService.getPostedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Posted production outputs retrieved successfully"));
    }
    
    @GetMapping("/rejected")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getRejectedProductionOutputs() {
        List<ProductionOutputResponse> outputs = productionService.getRejectedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Rejected production outputs retrieved successfully"));
    }
    
    @GetMapping("/unverified")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getUnverifiedProductionOutputs() {
        List<ProductionOutputResponse> outputs = productionService.getUnverifiedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Unverified production outputs retrieved successfully"));
    }
    
    @GetMapping("/unposted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getUnpostedProductionOutputs() {
        List<ProductionOutputResponse> outputs = productionService.getUnpostedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Unposted production outputs retrieved successfully"));
    }
    
    @GetMapping("/todays")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getTodaysProductionOutputs() {
        List<ProductionOutputResponse> outputs = productionService.getTodaysProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Today's production outputs retrieved successfully"));
    }
    
    @GetMapping("/work-order/{workOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getProductionOutputsByWorkOrder(@PathVariable Long workOrderId) {
        List<ProductionOutputResponse> outputs = productionService.getProductionOutputsByWorkOrder(workOrderId);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs by work order retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getProductionOutputsByProduct(@PathVariable Long productId) {
        List<ProductionOutputResponse> outputs = productionService.getProductionOutputsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs by product retrieved successfully"));
    }
    
    @GetMapping("/production-line/{productionLineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getProductionOutputsByProductionLine(@PathVariable Long productionLineId) {
        List<ProductionOutputResponse> outputs = productionService.getProductionOutputsByProductionLine(productionLineId);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs by production line retrieved successfully"));
    }
    
    @GetMapping("/supervisor/{supervisorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getProductionOutputsBySupervisor(@PathVariable Long supervisorId) {
        List<ProductionOutputResponse> outputs = productionService.getProductionOutputsBySupervisor(supervisorId);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs by supervisor retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getProductionOutputsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<ProductionOutputResponse> outputs = productionService.getProductionOutputsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs by date range retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> getRecentProductionOutputs(@RequestParam(defaultValue = "10") int limit) {
        List<ProductionOutputResponse> outputs = productionService.getRecentProductionOutputs(limit);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Recent production outputs retrieved successfully"));
    }
    
    @GetMapping("/work-order/{workOrderId}/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalOutputByWorkOrder(@PathVariable Long workOrderId) {
        BigDecimal total = productionService.getTotalOutputByWorkOrder(workOrderId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total output by work order retrieved successfully"));
    }
    
    @GetMapping("/work-order/{workOrderId}/good-quantity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getTotalGoodQuantityByWorkOrder(@PathVariable Long workOrderId) {
        Double total = productionService.getTotalGoodQuantityByWorkOrder(workOrderId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total good quantity by work order retrieved successfully"));
    }
    
    @GetMapping("/work-order/{workOrderId}/rejected-quantity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Double>> getTotalRejectedQuantityByWorkOrder(@PathVariable Long workOrderId) {
        Double total = productionService.getTotalRejectedQuantityByWorkOrder(workOrderId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total rejected quantity by work order retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/{id}/can-verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canVerifyProductionOutput(@PathVariable Long id) {
        boolean canVerify = productionService.canVerifyProductionOutput(id);
        return ResponseEntity.ok(ApiResponse.success(canVerify, "Verification check completed"));
    }
    
    @GetMapping("/{id}/can-post")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canPostToInventory(@PathVariable Long id) {
        boolean canPost = productionService.canPostToInventory(id);
        return ResponseEntity.ok(ApiResponse.success(canPost, "Post check completed"));
    }
    
    @GetMapping("/{id}/can-reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canRejectProductionOutput(@PathVariable Long id) {
        boolean canReject = productionService.canRejectProductionOutput(id);
        return ResponseEntity.ok(ApiResponse.success(canReject, "Rejection check completed"));
    }
    
    // Calculations
    @GetMapping("/{id}/quality-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Double>> calculateQualityRate(@PathVariable Long id) {
        Double rate = productionService.calculateQualityRate(id);
        return ResponseEntity.ok(ApiResponse.success(rate, "Quality rate calculated successfully"));
    }
    
    @GetMapping("/{id}/rejection-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Double>> calculateRejectionRate(@PathVariable Long id) {
        Double rate = productionService.calculateRejectionRate(id);
        return ResponseEntity.ok(ApiResponse.success(rate, "Rejection rate calculated successfully"));
    }
    
    @GetMapping("/{id}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> calculateProductionMetrics(@PathVariable Long id) {
        Map<String, Object> metrics = productionService.calculateProductionMetrics(id);
        return ResponseEntity.ok(ApiResponse.success(metrics, "Production metrics calculated successfully"));
    }
    
    // Batch Operations
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutputResponse>>> createBulkProductionOutputs(@Valid @RequestBody List<ProductionOutputRequest> requests) {
        log.info("Creating {} production outputs in bulk", requests.size());
        List<ProductionOutputResponse> outputs = productionService.createBulkProductionOutputs(requests);
        return ResponseEntity.ok(ApiResponse.success(outputs, outputs.size() + " production outputs created successfully"));
    }
    
    @PutMapping("/bulk/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> verifyBulkProductionOutputs(@RequestBody List<Long> outputIds, @RequestParam Long verifiedByUserId) {
        log.info("Verifying {} production outputs in bulk", outputIds.size());
        int count = productionService.verifyBulkProductionOutputs(outputIds, verifiedByUserId);
        return ResponseEntity.ok(ApiResponse.success(count, count + " production outputs verified successfully"));
    }
    
    @PutMapping("/bulk/post")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> postBulkToInventory(@RequestBody List<Long> outputIds) {
        log.info("Posting {} production outputs to inventory in bulk", outputIds.size());
        int count = productionService.postBulkToInventory(outputIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " production outputs posted to inventory successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkProductionOutputs(@RequestBody List<Long> outputIds) {
        log.info("Deleting {} production outputs in bulk", outputIds.size());
        int count = productionService.deleteBulkProductionOutputs(outputIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " production outputs deleted successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductionOutputStatistics() {
        Map<String, Object> statistics = productionService.getProductionOutputStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Production output statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOutputTypeDistribution() {
        List<Map<String, Object>> distribution = productionService.getOutputTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Output type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = productionService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyProductionOutput(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> output = productionService.getMonthlyProductionOutput(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(output, "Monthly production output retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-product")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductionByProduct() {
        List<Map<String, Object>> stats = productionService.getProductionByProduct();
        return ResponseEntity.ok(ApiResponse.success(stats, "Production by product retrieved successfully"));
    }
    
    @GetMapping("/statistics/by-production-line")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductionByProductionLine() {
        List<Map<String, Object>> stats = productionService.getProductionByProductionLine();
        return ResponseEntity.ok(ApiResponse.success(stats, "Production by production line retrieved successfully"));
    }
    
    @GetMapping("/statistics/quality-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getQualityRate() {
        Double rate = productionService.getQualityRate();
        return ResponseEntity.ok(ApiResponse.success(rate, "Quality rate retrieved successfully"));
    }
    
    @GetMapping("/statistics/rejection-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getRejectionRate() {
        Double rate = productionService.getRejectionRate();
        return ResponseEntity.ok(ApiResponse.success(rate, "Rejection rate retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = productionService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
