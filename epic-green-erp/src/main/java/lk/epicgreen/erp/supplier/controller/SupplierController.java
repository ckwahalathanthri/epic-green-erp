package lk.epicgreen.erp.supplier.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.supplier.dto.SupplierRequest;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Supplier Controller
 * REST controller for supplier operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SupplierController {
    
    private final SupplierService supplierService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> createSupplier(
        @Valid @RequestBody SupplierRequest request
    ) {
        Supplier created = supplierService.createSupplier(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Supplier created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> updateSupplier(
        @PathVariable Long id,
        @Valid @RequestBody SupplierRequest request
    ) {
        Supplier updated = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Supplier updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Supplier deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Supplier>> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully"));
    }
    
    @GetMapping("/code/{supplierCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Supplier>> getSupplierByCode(@PathVariable String supplierCode) {
        Supplier supplier = supplierService.getSupplierByCode(supplierCode);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully"));
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> getSupplierByEmail(@PathVariable String email) {
        Supplier supplier = supplierService.getSupplierByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<Supplier>>> getAllSuppliers(Pageable pageable) {
        Page<Supplier> suppliers = supplierService.getAllSuppliers(pageable);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<Supplier>>> searchSuppliers(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Supplier> suppliers = supplierService.searchSuppliers(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> activateSupplier(@PathVariable Long id) {
        Supplier activated = supplierService.activateSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Supplier activated"));
    }
    
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> deactivateSupplier(@PathVariable Long id) {
        Supplier deactivated = supplierService.deactivateSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Supplier deactivated"));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> approveSupplier(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        Supplier approved = supplierService.approveSupplier(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Supplier approved"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> rejectSupplier(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        Supplier rejected = supplierService.rejectSupplier(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Supplier rejected"));
    }
    
    @PostMapping("/{id}/block")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> blockSupplier(
        @PathVariable Long id,
        @RequestParam String blockReason
    ) {
        Supplier blocked = supplierService.blockSupplier(id, blockReason);
        return ResponseEntity.ok(ApiResponse.success(blocked, "Supplier blocked"));
    }
    
    @PostMapping("/{id}/unblock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> unblockSupplier(@PathVariable Long id) {
        Supplier unblocked = supplierService.unblockSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(unblocked, "Supplier unblocked"));
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<Supplier>>> getActiveSuppliers(Pageable pageable) {
        Page<Supplier> suppliers = supplierService.getActiveSuppliers(pageable);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Active suppliers retrieved"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getInactiveSuppliers() {
        List<Supplier> suppliers = supplierService.getInactiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Inactive suppliers retrieved"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getApprovedSuppliers() {
        List<Supplier> suppliers = supplierService.getApprovedSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Approved suppliers retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getPendingApprovalSuppliers() {
        List<Supplier> suppliers = supplierService.getPendingApprovalSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Pending approval suppliers retrieved"));
    }
    
    @GetMapping("/rejected")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getRejectedSuppliers() {
        List<Supplier> suppliers = supplierService.getRejectedSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Rejected suppliers retrieved"));
    }
    
    @GetMapping("/blocked")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getBlockedSuppliers() {
        List<Supplier> suppliers = supplierService.getBlockedSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Blocked suppliers retrieved"));
    }
    
    @GetMapping("/type/{supplierType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByType(@PathVariable String supplierType) {
        List<Supplier> suppliers = supplierService.getSuppliersByType(supplierType);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by type retrieved"));
    }
    
    @GetMapping("/city/{city}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByCity(@PathVariable String city) {
        List<Supplier> suppliers = supplierService.getSuppliersByCity(city);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by city retrieved"));
    }
    
    @GetMapping("/country/{country}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByCountry(@PathVariable String country) {
        List<Supplier> suppliers = supplierService.getSuppliersByCountry(country);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by country retrieved"));
    }
    
    @GetMapping("/local/{country}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getLocalSuppliers(@PathVariable String country) {
        List<Supplier> suppliers = supplierService.getLocalSuppliers(country);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Local suppliers retrieved"));
    }
    
    @GetMapping("/international/{country}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getInternationalSuppliers(@PathVariable String country) {
        List<Supplier> suppliers = supplierService.getInternationalSuppliers(country);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "International suppliers retrieved"));
    }
    
    @GetMapping("/with-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersWithCredit() {
        List<Supplier> suppliers = supplierService.getSuppliersWithCredit();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers with credit retrieved"));
    }
    
    @GetMapping("/exceeding-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersExceedingCreditLimit() {
        List<Supplier> suppliers = supplierService.getSuppliersExceedingCreditLimit();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers exceeding credit limit retrieved"));
    }
    
    @GetMapping("/with-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersWithOutstandingBalance() {
        List<Supplier> suppliers = supplierService.getSuppliersWithOutstandingBalance();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers with outstanding balance retrieved"));
    }
    
    @GetMapping("/high-rated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getHighRatedSuppliers(
        @RequestParam(defaultValue = "4.0") Double minRating
    ) {
        List<Supplier> suppliers = supplierService.getHighRatedSuppliers(minRating);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "High rated suppliers retrieved"));
    }
    
    @GetMapping("/low-rated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getLowRatedSuppliers(
        @RequestParam(defaultValue = "3.0") Double maxRating
    ) {
        List<Supplier> suppliers = supplierService.getLowRatedSuppliers(maxRating);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Low rated suppliers retrieved"));
    }
    
    @GetMapping("/requiring-attention")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersRequiringAttention(
        @RequestParam(defaultValue = "3.0") Double lowRatingThreshold
    ) {
        List<Supplier> suppliers = supplierService.getSuppliersRequiringAttention(lowRatingThreshold);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers requiring attention retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getRecentSuppliers(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Supplier> suppliers = supplierService.getRecentSuppliers(limit);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Recent suppliers retrieved"));
    }
    
    @GetMapping("/top-rated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getTopRatedSuppliers(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Supplier> suppliers = supplierService.getTopRatedSuppliers(limit);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Top rated suppliers retrieved"));
    }
    
    @PostMapping("/{id}/update-credit-limit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateCreditLimit(
        @PathVariable Long id,
        @RequestParam Double newCreditLimit
    ) {
        supplierService.updateCreditLimit(id, newCreditLimit);
        return ResponseEntity.ok(ApiResponse.success(null, "Credit limit updated"));
    }
    
    @PostMapping("/{id}/enable-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> enableCredit(
        @PathVariable Long id,
        @RequestParam Double creditLimit,
        @RequestParam Integer creditDays
    ) {
        supplierService.enableCredit(id, creditLimit, creditDays);
        return ResponseEntity.ok(ApiResponse.success(null, "Credit enabled"));
    }
    
    @PostMapping("/{id}/disable-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> disableCredit(@PathVariable Long id) {
        supplierService.disableCredit(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Credit disabled"));
    }
    
    @GetMapping("/{id}/available-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getAvailableCredit(@PathVariable Long id) {
        Double availableCredit = supplierService.getAvailableCredit(id);
        return ResponseEntity.ok(ApiResponse.success(availableCredit, "Available credit retrieved"));
    }
    
    @PostMapping("/{id}/update-rating")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateRating(
        @PathVariable Long id,
        @RequestParam Double rating
    ) {
        supplierService.updateRating(id, rating);
        return ResponseEntity.ok(ApiResponse.success(null, "Rating updated"));
    }
    
    @PostMapping("/{id}/update-rating-and-reviews")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateRatingAndReviews(
        @PathVariable Long id,
        @RequestParam Double rating,
        @RequestParam String reviewComments
    ) {
        supplierService.updateRatingAndReviews(id, rating, reviewComments);
        return ResponseEntity.ok(ApiResponse.success(null, "Rating and reviews updated"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = supplierService.getSupplierStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = supplierService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
