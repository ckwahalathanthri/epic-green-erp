package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.production.dto.request.BillOfMaterialsRequest;
import lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse;
import lk.epicgreen.erp.production.service.BillOfMaterialsService;
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
 * BOM Controller
 * REST controller for bill of materials operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/production/boms")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class BomController {
    
    private final BillOfMaterialsService bomService;
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> createBom(@Valid @RequestBody BillOfMaterialsRequest request) {
        log.info("Creating BOM for product: {}", request.getFinishedProductId());
        BillOfMaterialsResponse created = bomService.createBom(request);
        return ResponseEntity.ok(ApiResponse.success(created, "BOM created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> updateBom(@PathVariable Long id, @Valid @RequestBody BillOfMaterialsRequest request) {
        log.info("Updating BOM: {}", id);
        BillOfMaterialsResponse updated = bomService.updateBom(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "BOM updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteBom(@PathVariable Long id) {
        log.info("Deleting BOM: {}", id);
        bomService.deleteBom(id);
        return ResponseEntity.ok(ApiResponse.success(null, "BOM deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> getBomById(@PathVariable Long id) {
        BillOfMaterialsResponse bom = bomService.getBomById(id);
        return ResponseEntity.ok(ApiResponse.success(bom, "BOM retrieved successfully"));
    }
    
    @GetMapping("/code/{bomCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> getBomByCode(@PathVariable String bomCode) {
        BillOfMaterialsResponse bom = bomService.getBomByCode(bomCode);
        return ResponseEntity.ok(ApiResponse.success(bom, "BOM retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<BillOfMaterialsResponse>>> getAllBoms(Pageable pageable) {
        Page<BillOfMaterialsResponse> boms = bomService.getAllBoms(pageable);
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getAllBomsList() {
        List<BillOfMaterialsResponse> boms = bomService.getAllBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<BillOfMaterialsResponse>>> searchBoms(@RequestParam String keyword, Pageable pageable) {
        Page<BillOfMaterialsResponse> boms = bomService.searchBoms(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(boms, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> approveBom(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        log.info("Approving BOM: {}", id);
        BillOfMaterialsResponse approved = bomService.approveBom(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "BOM approved successfully"));
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> activateBom(@PathVariable Long id) {
        log.info("Activating BOM: {}", id);
        BillOfMaterialsResponse activated = bomService.activateBom(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "BOM activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> deactivateBom(@PathVariable Long id) {
        log.info("Deactivating BOM: {}", id);
        BillOfMaterialsResponse deactivated = bomService.deactivateBom(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "BOM deactivated successfully"));
    }
    
    @PutMapping("/{id}/obsolete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> markAsObsolete(@PathVariable Long id, @RequestParam String obsoleteReason) {
        log.info("Marking BOM as obsolete: {}", id);
        BillOfMaterialsResponse obsolete = bomService.markAsObsolete(id, obsoleteReason);
        return ResponseEntity.ok(ApiResponse.success(obsolete, "BOM marked as obsolete"));
    }
    
    @PutMapping("/product/{productId}/set-default/{bomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> setAsDefault(@PathVariable Long productId, @PathVariable Long bomId) {
        log.info("Setting BOM {} as default for product {}", bomId, productId);
        BillOfMaterialsResponse defaultBom = bomService.setAsDefault(productId, bomId);
        return ResponseEntity.ok(ApiResponse.success(defaultBom, "BOM set as default successfully"));
    }
    
    // Version Operations
    @PostMapping("/{bomId}/create-version")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> createNewVersion(@PathVariable Long bomId, @RequestParam String newVersion) {
        log.info("Creating new version {} for BOM {}", newVersion, bomId);
        BillOfMaterialsResponse newBom = bomService.createNewVersion(bomId, newVersion);
        return ResponseEntity.ok(ApiResponse.success(newBom, "New BOM version created successfully"));
    }
    
    @GetMapping("/product/{productId}/versions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getBomVersions(@PathVariable Long productId) {
        List<BillOfMaterialsResponse> versions = bomService.getBomVersions(productId);
        return ResponseEntity.ok(ApiResponse.success(versions, "BOM versions retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}/latest-version")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> getLatestBomVersion(@PathVariable Long productId) {
        BillOfMaterialsResponse latest = bomService.getLatestBomVersion(productId);
        return ResponseEntity.ok(ApiResponse.success(latest, "Latest BOM version retrieved successfully"));
    }
    
    // Query Operations
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getActiveBoms() {
        List<BillOfMaterialsResponse> boms = bomService.getActiveBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Active BOMs retrieved successfully"));
    }
    
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getDraftBoms() {
        List<BillOfMaterialsResponse> boms = bomService.getDraftBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Draft BOMs retrieved successfully"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getApprovedBoms() {
        List<BillOfMaterialsResponse> boms = bomService.getApprovedBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Approved BOMs retrieved successfully"));
    }
    
    @GetMapping("/obsolete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getObsoleteBoms() {
        List<BillOfMaterialsResponse> boms = bomService.getObsoleteBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Obsolete BOMs retrieved successfully"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getBomsPendingApproval() {
        List<BillOfMaterialsResponse> boms = bomService.getBomsPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs pending approval retrieved successfully"));
    }
    
    @GetMapping("/default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getActiveDefaultBoms() {
        List<BillOfMaterialsResponse> boms = bomService.getActiveDefaultBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Active default BOMs retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BillOfMaterialsResponse>> getProductActiveBom(@PathVariable Long productId) {
        BillOfMaterialsResponse bom = bomService.getProductActiveBom(productId);
        return ResponseEntity.ok(ApiResponse.success(bom, "Product active BOM retrieved successfully"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getProductBoms(@PathVariable Long productId) {
        List<BillOfMaterialsResponse> boms = bomService.getProductBoms(productId);
        return ResponseEntity.ok(ApiResponse.success(boms, "Product BOMs retrieved successfully"));
    }
    
    @GetMapping("/effective")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getEffectiveBoms() {
        List<BillOfMaterialsResponse> boms = bomService.getEffectiveBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Effective BOMs retrieved successfully"));
    }
    
    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getExpiredBoms() {
        List<BillOfMaterialsResponse> boms = bomService.getExpiredBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Expired BOMs retrieved successfully"));
    }
    
    @GetMapping("/expiring-soon")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getExpiringSoonBoms(@RequestParam(defaultValue = "30") int days) {
        List<BillOfMaterialsResponse> boms = bomService.getExpiringSoonBoms(days);
        return ResponseEntity.ok(ApiResponse.success(boms, "Expiring soon BOMs retrieved successfully"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getBomsRequiringAction() {
        List<BillOfMaterialsResponse> boms = bomService.getBomsRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs requiring action retrieved successfully"));
    }
    
    @GetMapping("/type/{bomType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getBomsByType(@PathVariable String bomType) {
        List<BillOfMaterialsResponse> boms = bomService.getBomsByType(bomType);
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs by type retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> getRecentBoms(@RequestParam(defaultValue = "10") int limit) {
        List<BillOfMaterialsResponse> boms = bomService.getRecentBoms(limit);
        return ResponseEntity.ok(ApiResponse.success(boms, "Recent BOMs retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/{id}/can-approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canApproveBom(@PathVariable Long id) {
        boolean canApprove = bomService.canApproveBom(id);
        return ResponseEntity.ok(ApiResponse.success(canApprove, "Approval check completed"));
    }
    
    @GetMapping("/{id}/can-activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canActivateBom(@PathVariable Long id) {
        boolean canActivate = bomService.canActivateBom(id);
        return ResponseEntity.ok(ApiResponse.success(canActivate, "Activation check completed"));
    }
    
    @GetMapping("/{id}/can-obsolete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canMarkAsObsolete(@PathVariable Long id) {
        boolean canObsolete = bomService.canMarkAsObsolete(id);
        return ResponseEntity.ok(ApiResponse.success(canObsolete, "Obsolete check completed"));
    }
    
    @GetMapping("/code-available/{bomCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isBomCodeAvailable(@PathVariable String bomCode) {
        boolean isAvailable = bomService.isBomCodeAvailable(bomCode);
        return ResponseEntity.ok(ApiResponse.success(isAvailable, "Code availability check completed"));
    }
    
    // Batch Operations
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterialsResponse>>> createBulkBoms(@Valid @RequestBody List<BillOfMaterialsRequest> requests) {
        log.info("Creating {} BOMs in bulk", requests.size());
        List<BillOfMaterialsResponse> boms = bomService.createBulkBoms(requests);
        return ResponseEntity.ok(ApiResponse.success(boms, boms.size() + " BOMs created successfully"));
    }
    
    @PutMapping("/bulk/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> approveBulkBoms(@RequestBody List<Long> bomIds, @RequestParam Long approvedByUserId) {
        log.info("Approving {} BOMs in bulk", bomIds.size());
        int count = bomService.approveBulkBoms(bomIds, approvedByUserId);
        return ResponseEntity.ok(ApiResponse.success(count, count + " BOMs approved successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkBoms(@RequestBody List<Long> bomIds) {
        log.info("Deleting {} BOMs in bulk", bomIds.size());
        int count = bomService.deleteBulkBoms(bomIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " BOMs deleted successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBomStatistics() {
        Map<String, Object> statistics = bomService.getBomStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "BOM statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getBomTypeDistribution() {
        List<Map<String, Object>> distribution = bomService.getBomTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "BOM type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = bomService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyBomCreationCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<Map<String, Object>> count = bomService.getMonthlyBomCreationCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(count, "Monthly BOM creation count retrieved successfully"));
    }
    
    @GetMapping("/statistics/products-with-boms")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductsWithBoms() {
        List<Map<String, Object>> products = bomService.getProductsWithBoms();
        return ResponseEntity.ok(ApiResponse.success(products, "Products with BOMs retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = bomService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
