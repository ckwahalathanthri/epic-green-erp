package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.production.dto.BomRequest;
import lk.epicgreen.erp.production.entity.Bom;
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
    public ResponseEntity<ApiResponse<Bom>> createBom(@Valid @RequestBody BomRequest request) {
        Bom created = bomService.createBom(request);
        return ResponseEntity.ok(ApiResponse.success(created, "BOM created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Bom>> updateBom(
        @PathVariable Long id,
        @Valid @RequestBody BomRequest request
    ) {
        Bom updated = bomService.updateBom(id, request);
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
    public ResponseEntity<ApiResponse<Bom>> getBomById(@PathVariable Long id) {
        Bom bom = bomService.getBomById(id);
        return ResponseEntity.ok(ApiResponse.success(bom, "BOM retrieved successfully"));
    }
    
    @GetMapping("/code/{bomCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Bom>> getBomByCode(@PathVariable String bomCode) {
        Bom bom = bomService.getBomByCode(bomCode);
        return ResponseEntity.ok(ApiResponse.success(bom, "BOM retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<Bom>>> getAllBoms(Pageable pageable) {
        Page<Bom> boms = bomService.getAllBoms(pageable);
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR', 'USER')")
    public ResponseEntity<ApiResponse<Page<Bom>>> searchBoms(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Bom> boms = bomService.searchBoms(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(boms, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Bom>> approveBom(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        Bom approved = bomService.approveBom(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "BOM approved"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Bom>> activateBom(@PathVariable Long id) {
        Bom activated = bomService.activateBom(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "BOM activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Bom>> deactivateBom(@PathVariable Long id) {
        Bom deactivated = bomService.deactivateBom(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "BOM deactivated"));
    }
    
    @PostMapping("/{id}/mark-obsolete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Bom>> markAsObsolete(
        @PathVariable Long id,
        @RequestParam String obsoleteReason
    ) {
        Bom obsolete = bomService.markAsObsolete(id, obsoleteReason);
        return ResponseEntity.ok(ApiResponse.success(obsolete, "BOM marked as obsolete"));
    }
    
    @PostMapping("/product/{productId}/set-default/{bomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Bom>> setAsDefault(
        @PathVariable Long productId,
        @PathVariable Long bomId
    ) {
        Bom defaultBom = bomService.setAsDefault(productId, bomId);
        return ResponseEntity.ok(ApiResponse.success(defaultBom, "BOM set as default"));
    }
    
    @PostMapping("/{id}/new-version")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<Bom>> createNewVersion(
        @PathVariable Long id,
        @RequestParam String newVersion
    ) {
        Bom newVersionBom = bomService.createNewVersion(id, newVersion);
        return ResponseEntity.ok(ApiResponse.success(newVersionBom, "New BOM version created"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<Bom>>> getActiveBoms() {
        List<Bom> boms = bomService.getActiveBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Active BOMs retrieved"));
    }
    
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Bom>>> getDraftBoms() {
        List<Bom> boms = bomService.getDraftBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Draft BOMs retrieved"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Bom>>> getApprovedBoms() {
        List<Bom> boms = bomService.getApprovedBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Approved BOMs retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Bom>>> getBomsPendingApproval() {
        List<Bom> boms = bomService.getBomsPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs pending approval retrieved"));
    }
    
    @GetMapping("/default")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<Bom>>> getActiveDefaultBoms() {
        List<Bom> boms = bomService.getActiveDefaultBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Active default BOMs retrieved"));
    }
    
    @GetMapping("/effective")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<Bom>>> getEffectiveBoms() {
        List<Bom> boms = bomService.getEffectiveBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Effective BOMs retrieved"));
    }
    
    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Bom>>> getExpiredBoms() {
        List<Bom> boms = bomService.getExpiredBoms();
        return ResponseEntity.ok(ApiResponse.success(boms, "Expired BOMs retrieved"));
    }
    
    @GetMapping("/expiring-soon")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Bom>>> getExpiringSoonBoms(
        @RequestParam(defaultValue = "30") int days
    ) {
        List<Bom> boms = bomService.getExpiringSoonBoms(days);
        return ResponseEntity.ok(ApiResponse.success(boms, "Expiring soon BOMs retrieved"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Bom>>> getBomsRequiringAction() {
        List<Bom> boms = bomService.getBomsRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(boms, "BOMs requiring action retrieved"));
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<Bom>>> getProductBoms(@PathVariable Long productId) {
        List<Bom> boms = bomService.getProductBoms(productId);
        return ResponseEntity.ok(ApiResponse.success(boms, "Product BOMs retrieved"));
    }
    
    @GetMapping("/product/{productId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER', 'PRODUCTION_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Bom>> getProductActiveBom(@PathVariable Long productId) {
        Bom bom = bomService.getProductActiveBom(productId);
        return ResponseEntity.ok(ApiResponse.success(bom, "Product active BOM retrieved"));
    }
    
    @GetMapping("/product/{productId}/versions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Bom>>> getBomVersions(@PathVariable Long productId) {
        List<Bom> boms = bomService.getBomVersions(productId);
        return ResponseEntity.ok(ApiResponse.success(boms, "BOM versions retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PRODUCTION_MANAGER')")
    public ResponseEntity<ApiResponse<List<Bom>>> getRecentBoms(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Bom> boms = bomService.getRecentBoms(limit);
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
