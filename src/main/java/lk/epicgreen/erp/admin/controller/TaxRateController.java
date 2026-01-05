package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.request.TaxRateRequest;
import lk.epicgreen.erp.admin.dto.response.TaxRateResponse;
import lk.epicgreen.erp.admin.service.TaxRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * Tax Rate Controller
 * REST controller for tax rate management
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
    
    // Create Tax Rate
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<TaxRateResponse>> createTaxRate(@Valid @RequestBody TaxRateRequest request) {
        log.info("Creating tax rate: {}", request.getTaxCode());
        TaxRateResponse response = taxRateService.createTaxRate(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Tax rate created successfully"));
    }
    
    // Update Tax Rate
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<TaxRateResponse>> updateTaxRate(
        @PathVariable Long id,
        @Valid @RequestBody TaxRateRequest request
    ) {
        log.info("Updating tax rate: {}", id);
        TaxRateResponse response = taxRateService.updateTaxRate(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Tax rate updated successfully"));
    }
    
    // Activate Tax Rate
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> activateTaxRate(@PathVariable Long id) {
        log.info("Activating tax rate: {}", id);
        taxRateService.activateTaxRate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tax rate activated successfully"));
    }
    
    // Deactivate Tax Rate
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deactivateTaxRate(@PathVariable Long id) {
        log.info("Deactivating tax rate: {}", id);
        taxRateService.deactivateTaxRate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tax rate deactivated successfully"));
    }
    
    // Delete Tax Rate
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTaxRate(@PathVariable Long id) {
        log.info("Deleting tax rate: {}", id);
        taxRateService.deleteTaxRate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tax rate deleted successfully"));
    }
    
    // Get Tax Rate by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<TaxRateResponse>> getTaxRateById(@PathVariable Long id) {
        TaxRateResponse response = taxRateService.getTaxRateById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Tax rate retrieved successfully"));
    }
    
    // Get Tax Rate by Code
    @GetMapping("/code/{taxCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<TaxRateResponse>> getTaxRateByCode(@PathVariable String taxCode) {
        TaxRateResponse response = taxRateService.getTaxRateByCode(taxCode);
        return ResponseEntity.ok(ApiResponse.success(response, "Tax rate retrieved successfully"));
    }
    
    // Get All Tax Rates
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRateResponse>>> getAllTaxRates() {
        List<TaxRateResponse> response = taxRateService.getAllTaxRates();
        return ResponseEntity.ok(ApiResponse.success(response, "Tax rates retrieved successfully"));
    }
    
    // Get All Active Tax Rates
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRateResponse>>> getAllActiveTaxRates() {
        List<TaxRateResponse> response = taxRateService.getAllActiveTaxRates();
        return ResponseEntity.ok(ApiResponse.success(response, "Active tax rates retrieved successfully"));
    }
    
    // Get Tax Rates by Type
    @GetMapping("/type/{taxType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRateResponse>>> getTaxRatesByType(@PathVariable String taxType) {
        List<TaxRateResponse> response = taxRateService.getTaxRatesByType(taxType);
        return ResponseEntity.ok(ApiResponse.success(response, "Tax rates retrieved successfully"));
    }
    
    // Get Active Tax Rates by Date
    @GetMapping("/active/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<TaxRateResponse>>> getActiveTaxRatesByDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<TaxRateResponse> response = taxRateService.getActiveTaxRatesByDate(date);
        return ResponseEntity.ok(ApiResponse.success(response, "Active tax rates retrieved successfully"));
    }
}
