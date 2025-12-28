package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.TaxRateRequest;
import lk.epicgreen.erp.admin.entity.TaxRate;
import lk.epicgreen.erp.admin.service.TaxRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Tax Rate Controller
 * REST controller for tax rate operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/tax-rates")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaxRateController {
    
    private final TaxRateService taxRateService;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<TaxRate>> createTaxRate(@Valid @RequestBody TaxRateRequest request) {
        log.info("Creating tax rate: {}", request.getTaxCode());
        TaxRate created = taxRateService.createTaxRate(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Tax rate created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<TaxRate>> updateTaxRate(
        @PathVariable Long id,
        @Valid @RequestBody TaxRateRequest request
    ) {
        log.info("Updating tax rate: {}", id);
        TaxRate updated = taxRateService.updateTaxRate(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Tax rate updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTaxRate(@PathVariable Long id) {
        log.info("Deleting tax rate: {}", id);
        taxRateService.deleteTaxRate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tax rate deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<TaxRate>> getTaxRateById(@PathVariable Long id) {
        TaxRate taxRate = taxRateService.getTaxRateById(id);
        return ResponseEntity.ok(ApiResponse.success(taxRate, "Tax rate retrieved successfully"));
    }
    
    @GetMapping("/code/{taxCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<TaxRate>> getTaxRateByCode(@PathVariable String taxCode) {
        TaxRate taxRate = taxRateService.getTaxRateByCode(taxCode);
        return ResponseEntity.ok(ApiResponse.success(taxRate, "Tax rate retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<TaxRate>>> getAllTaxRates(Pageable pageable) {
        Page<TaxRate> taxRates = taxRateService.getAllTaxRates(pageable);
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Tax rates retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRate>>> getAllTaxRatesList() {
        List<TaxRate> taxRates = taxRateService.getAllTaxRates();
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Tax rates list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<TaxRate>>> searchTaxRates(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<TaxRate> taxRates = taxRateService.searchTaxRates(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<TaxRate>> activateTaxRate(@PathVariable Long id) {
        log.info("Activating tax rate: {}", id);
        TaxRate activated = taxRateService.activateTaxRate(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Tax rate activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<TaxRate>> deactivateTaxRate(@PathVariable Long id) {
        log.info("Deactivating tax rate: {}", id);
        TaxRate deactivated = taxRateService.deactivateTaxRate(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Tax rate deactivated successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRate>>> getActiveTaxRates() {
        List<TaxRate> taxRates = taxRateService.getActiveTaxRates();
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Active tax rates retrieved successfully"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<TaxRate>>> getInactiveTaxRates() {
        List<TaxRate> taxRates = taxRateService.getInactiveTaxRates();
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Inactive tax rates retrieved successfully"));
    }
    
    @GetMapping("/type/{taxType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRate>>> getTaxRatesByType(@PathVariable String taxType) {
        List<TaxRate> taxRates = taxRateService.getTaxRatesByType(taxType);
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Tax rates by type retrieved successfully"));
    }
    
    @GetMapping("/applicable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRate>>> getApplicableTaxRates(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate
    ) {
        List<TaxRate> taxRates = taxRateService.getApplicableTaxRates(asOfDate);
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Applicable tax rates retrieved successfully"));
    }
    
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRate>>> getCurrentTaxRates() {
        List<TaxRate> taxRates = taxRateService.getCurrentTaxRates();
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Current tax rates retrieved successfully"));
    }
    
    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<TaxRate>>> getExpiredTaxRates() {
        List<TaxRate> taxRates = taxRateService.getExpiredTaxRates();
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Expired tax rates retrieved successfully"));
    }
    
    @GetMapping("/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRate>>> getTaxRatesByPercentageRange(
        @RequestParam BigDecimal minPercentage,
        @RequestParam BigDecimal maxPercentage
    ) {
        List<TaxRate> taxRates = taxRateService.getTaxRatesByPercentageRange(minPercentage, maxPercentage);
        return ResponseEntity.ok(ApiResponse.success(taxRates, "Tax rates by percentage range retrieved successfully"));
    }
    
    // ===================================================================
    // TAX CALCULATION
    // ===================================================================
    
    @GetMapping("/{taxCode}/calculate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateTax(
        @PathVariable String taxCode,
        @RequestParam BigDecimal amount
    ) {
        BigDecimal taxAmount = taxRateService.calculateTax(taxCode, amount);
        return ResponseEntity.ok(ApiResponse.success(taxAmount, "Tax calculated successfully"));
    }
    
    @GetMapping("/{taxCode}/calculate-inclusive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateTaxInclusive(
        @PathVariable String taxCode,
        @RequestParam BigDecimal totalAmount
    ) {
        BigDecimal taxAmount = taxRateService.calculateTaxInclusive(taxCode, totalAmount);
        return ResponseEntity.ok(ApiResponse.success(taxAmount, "Tax (inclusive) calculated successfully"));
    }
    
    @GetMapping("/{taxCode}/calculate-exclusive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateTaxExclusive(
        @PathVariable String taxCode,
        @RequestParam BigDecimal baseAmount
    ) {
        BigDecimal taxAmount = taxRateService.calculateTaxExclusive(taxCode, baseAmount);
        return ResponseEntity.ok(ApiResponse.success(taxAmount, "Tax (exclusive) calculated successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTaxRateStatistics() {
        Map<String, Object> statistics = taxRateService.getTaxRateStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Tax rate statistics retrieved successfully"));
    }
    
    @GetMapping("/distribution/type")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTypeDistribution() {
        List<Map<String, Object>> distribution = taxRateService.getTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Type distribution retrieved successfully"));
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @GetMapping("/validate/code/{taxCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isTaxCodeAvailable(@PathVariable String taxCode) {
        boolean available = taxRateService.isTaxCodeAvailable(taxCode);
        return ResponseEntity.ok(ApiResponse.success(available, "Tax code availability checked"));
    }
    
    @GetMapping("/{id}/can-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> canDelete(@PathVariable Long id) {
        boolean canDelete = taxRateService.canDelete(id);
        return ResponseEntity.ok(ApiResponse.success(canDelete, "Delete check completed"));
    }
}
