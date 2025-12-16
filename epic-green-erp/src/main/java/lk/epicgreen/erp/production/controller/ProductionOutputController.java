package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.production.dto.ProductionOutputRequest;
import lk.epicgreen.erp.production.entity.ProductionOutput;
import lk.epicgreen.erp.production.service.ProductionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * ProductionOutput Controller
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
    
    private final ProductionService productionService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<ProductionOutput>> createProductionOutput(
        @Valid @RequestBody ProductionOutputRequest request
    ) {
        ProductionOutput created = productionService.createProductionOutput(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Production output created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<ProductionOutput>> updateProductionOutput(
        @PathVariable Long id,
        @Valid @RequestBody ProductionOutputRequest request
    ) {
        ProductionOutput updated = productionService.updateProductionOutput(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Production output updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteProductionOutput(@PathVariable Long id) {
        productionService.deleteProductionOutput(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Production output deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<ProductionOutput>> getProductionOutputById(@PathVariable Long id) {
        ProductionOutput output = productionService.getProductionOutputById(id);
        return ResponseEntity.ok(ApiResponse.success(output, "Production output retrieved successfully"));
    }
    
    @GetMapping("/number/{outputNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<ProductionOutput>> getProductionOutputByNumber(@PathVariable String outputNumber) {
        ProductionOutput output = productionService.getProductionOutputByNumber(outputNumber);
        return ResponseEntity.ok(ApiResponse.success(output, "Production output retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<ProductionOutput>>> getAllProductionOutputs(Pageable pageable) {
        Page<ProductionOutput> outputs = productionService.getAllProductionOutputs(pageable);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<ProductionOutput>>> searchProductionOutputs(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<ProductionOutput> outputs = productionService.searchProductionOutputs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductionOutput>> verifyProductionOutput(
        @PathVariable Long id,
        @RequestParam Long verifiedByUserId,
        @RequestParam(required = false) String verificationNotes
    ) {
        ProductionOutput verified = productionService.verifyProductionOutput(id, verifiedByUserId, verificationNotes);
        return ResponseEntity.ok(ApiResponse.success(verified, "Production output verified"));
    }
    
    @PostMapping("/{id}/post-to-inventory")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductionOutput>> postToInventory(@PathVariable Long id) {
        ProductionOutput posted = productionService.postToInventory(id);
        return ResponseEntity.ok(ApiResponse.success(posted, "Production output posted to inventory"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductionOutput>> rejectProductionOutput(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        ProductionOutput rejected = productionService.rejectProductionOutput(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Production output rejected"));
    }
    
    @PostMapping("/{id}/record-quality-check")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> recordQualityCheck(
        @PathVariable Long id,
        @RequestParam Double goodQuantity,
        @RequestParam Double rejectedQuantity
    ) {
        productionService.recordQualityCheck(id, goodQuantity, rejectedQuantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Quality check recorded"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getPendingProductionOutputs() {
        List<ProductionOutput> outputs = productionService.getPendingProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Pending production outputs retrieved"));
    }
    
    @GetMapping("/verified")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getVerifiedProductionOutputs() {
        List<ProductionOutput> outputs = productionService.getVerifiedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Verified production outputs retrieved"));
    }
    
    @GetMapping("/posted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getPostedProductionOutputs() {
        List<ProductionOutput> outputs = productionService.getPostedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Posted production outputs retrieved"));
    }
    
    @GetMapping("/rejected")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getRejectedProductionOutputs() {
        List<ProductionOutput> outputs = productionService.getRejectedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Rejected production outputs retrieved"));
    }
    
    @GetMapping("/unverified")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getUnverifiedProductionOutputs() {
        List<ProductionOutput> outputs = productionService.getUnverifiedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Unverified production outputs retrieved"));
    }
    
    @GetMapping("/unposted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getUnpostedProductionOutputs() {
        List<ProductionOutput> outputs = productionService.getUnpostedProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Unposted production outputs retrieved"));
    }
    
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getTodaysProductionOutputs() {
        List<ProductionOutput> outputs = productionService.getTodaysProductionOutputs();
        return ResponseEntity.ok(ApiResponse.success(outputs, "Today's production outputs retrieved"));
    }
    
    @GetMapping("/work-order/{workOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getProductionOutputsByWorkOrder(
        @PathVariable Long workOrderId
    ) {
        List<ProductionOutput> outputs = productionService.getProductionOutputsByWorkOrder(workOrderId);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Work order production outputs retrieved"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getProductionOutputsByProduct(
        @PathVariable Long productId
    ) {
        List<ProductionOutput> outputs = productionService.getProductionOutputsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Product production outputs retrieved"));
    }
    
    @GetMapping("/production-line/{productionLineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getProductionOutputsByProductionLine(
        @PathVariable Long productionLineId
    ) {
        List<ProductionOutput> outputs = productionService.getProductionOutputsByProductionLine(productionLineId);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production line outputs retrieved"));
    }
    
    @GetMapping("/supervisor/{supervisorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getProductionOutputsBySupervisor(
        @PathVariable Long supervisorId
    ) {
        List<ProductionOutput> outputs = productionService.getProductionOutputsBySupervisor(supervisorId);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Supervisor production outputs retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getProductionOutputsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<ProductionOutput> outputs = productionService.getProductionOutputsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Production outputs in date range retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductionOutput>>> getRecentProductionOutputs(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<ProductionOutput> outputs = productionService.getRecentProductionOutputs(limit);
        return ResponseEntity.ok(ApiResponse.success(outputs, "Recent production outputs retrieved"));
    }
    
    @GetMapping("/work-order/{workOrderId}/total-output")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Double>> getTotalOutputByWorkOrder(@PathVariable Long workOrderId) {
        Double totalOutput = productionService.getTotalOutputByWorkOrder(workOrderId);
        return ResponseEntity.ok(ApiResponse.success(totalOutput, "Total output retrieved"));
    }
    
    @GetMapping("/work-order/{workOrderId}/good-quantity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Double>> getTotalGoodQuantityByWorkOrder(@PathVariable Long workOrderId) {
        Double totalGood = productionService.getTotalGoodQuantityByWorkOrder(workOrderId);
        return ResponseEntity.ok(ApiResponse.success(totalGood, "Total good quantity retrieved"));
    }
    
    @GetMapping("/work-order/{workOrderId}/rejected-quantity")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Double>> getTotalRejectedQuantityByWorkOrder(@PathVariable Long workOrderId) {
        Double totalRejected = productionService.getTotalRejectedQuantityByWorkOrder(workOrderId);
        return ResponseEntity.ok(ApiResponse.success(totalRejected, "Total rejected quantity retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = productionService.getProductionOutputStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = productionService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
