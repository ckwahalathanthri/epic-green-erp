package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.production.dto.BomRequest;
import lk.epicgreen.erp.production.entity.BillOfMaterials;
import lk.epicgreen.erp.production.service.BomService;
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
    
    private final BomService bomService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> createBom(@Valid @RequestBody BomRequest request) {
        BillOfMaterials created = bomService.createBom(request);
        return ResponseEntity.ok(ApiResponse.success(created, "BOM created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> updateBom(
        @PathVariable Long id,
        @Valid @RequestBody BomRequest request
    ) {
        BillOfMaterials updated = bomService.updateBom(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "BOM updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteBom(@PathVariable Long id) {
        bomService.deleteBom(id);
        return ResponseEntity.ok(ApiResponse.success(null, "BOM deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> getBomById(@PathVariable Long id) {
        BillOfMaterials bom = bomService.getBomById(id);
        return ResponseEntity.ok(ApiResponse.success(bom, "BOM retrieved successfully"));
    }
    
    @GetMapping("/code/{bomCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> getBomByCode(@PathVariable String bomCode) {
        BillOfMaterials bom = bomService.getBomByCode(bomCode);
        return ResponseEntity.ok(ApiResponse.success(bom, "BOM retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<BillOfMaterials>>> getAllBoms(Pageable pageable) {
        Page<BillOfMaterials> boms = bomService.getAllBoms(pageable);
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<BillOfMaterials>>> searchBoms(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<BillOfMaterials> boms = bomService.searchBoms(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(boms, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> approveBom(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        BillOfMaterials approved = bomService.approveBom(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "BOM approved"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> activateBom(@PathVariable Long id) {
        BillOfMaterials activated = bomService.activateBom(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "BOM activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> deactivateBom(@PathVariable Long id) {
        BillOfMaterials deactivated = bomService.deactivateBom(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "BOM deactivated"));
    }
    
    @PostMapping("/{id}/mark-obsolete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> markAsObsolete(
        @PathVariable Long id,
        @RequestParam String obsoleteReason
    ) {
        BillOfMaterials obsolete = bomService.markAsObsolete(id, obsoleteReason);
        return ResponseEntity.ok(ApiResponse.success(obsolete, "BOM marked as obsolete"));
    }
    
    @PostMapping("/product/{productId}/set-default/{bomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> setAsDefault(
        @PathVariable Long productId,
        @PathVariable Long bomId
    ) {
        BillOfMaterials defaultBom = bomService.setAsDefault(productId, bomId);
        return ResponseEntity.ok(ApiResponse.success(defaultBom, "BOM set as default"));
    }
    
    @PostMapping("/{id}/new-version")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> createNewVersion(
        @PathVariable Long id,
        @RequestParam String newVersion
    ) {
        BillOfMaterials newVersionBom = bomService.createNewVersion(id, newVersion);
        return ResponseEntity.ok(ApiResponse.success(newVersionBom, "New BOM version created"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getActiveBoms() {
        List<BillOfMaterials> boms = bomService.getActiveBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Active BOMs retrieved"));
    }
    
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getDraftBoms() {
        List<BillOfMaterials> boms = bomService.getDraftBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Draft BOMs retrieved"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getApprovedBoms() {
        List<BillOfMaterials> boms = bomService.getApprovedBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Approved BOMs retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getBomsPendingApproval() {
        List<BillOfMaterials> boms = bomService.getBomsPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs pending approval retrieved"));
    }
    
    @GetMapping("/default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getActiveDefaultBoms() {
        List<BillOfMaterials> boms = bomService.getActiveDefaultBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Active default BOMs retrieved"));
    }
    
    @GetMapping("/effective")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getEffectiveBoms() {
        List<BillOfMaterials> boms = bomService.getEffectiveBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Effective BOMs retrieved"));
    }
    
    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getExpiredBoms() {
        List<BillOfMaterials> boms = bomService.getExpiredBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Expired BOMs retrieved"));
    }
    
    @GetMapping("/expiring-soon")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getExpiringSoonBoms(
        @RequestParam(defaultValue = "30") int days
    ) {
        List<BillOfMaterials> boms = bomService.getExpiringSoonBoms(days);
        return ResponseEntity.ok(ApiResponse.success(boms, "Expiring soon BOMs retrieved"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getBomsRequiringAction() {
        List<BillOfMaterials> boms = bomService.getBomsRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs requiring action retrieved"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getProductBoms(@PathVariable Long productId) {
        List<BillOfMaterials> boms = bomService.getProductBoms(productId);
        return ResponseEntity.ok(ApiResponse.success(boms, "Product BOMs retrieved"));
    }
    
    @GetMapping("/product/{productId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<BillOfMaterials>> getProductActiveBom(@PathVariable Long productId) {
        BillOfMaterials bom = bomService.getProductActiveBom(productId);
        return ResponseEntity.ok(ApiResponse.success(bom, "Product active BOM retrieved"));
    }
    
    @GetMapping("/product/{productId}/versions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getBomVersions(@PathVariable Long productId) {
        List<BillOfMaterials> boms = bomService.getBomVersions(productId);
        return ResponseEntity.ok(ApiResponse.success(boms, "BOM versions retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<BillOfMaterials>>> getRecentBoms(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<BillOfMaterials> boms = bomService.getRecentBoms(limit);
        return ResponseEntity.ok(ApiResponse.success(boms, "Recent BOMs retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = bomService.getBomStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = bomService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
