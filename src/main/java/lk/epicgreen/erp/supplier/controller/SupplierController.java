package lk.epicgreen.erp.supplier.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.supplier.dto.request.SupplierRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierResponse;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
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
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(@Valid @RequestBody SupplierRequest request) {
        log.info("Creating supplier: {}", request.getSupplierName());
        SupplierResponse created = supplierService.createSupplier(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Supplier created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
        @PathVariable Long id,
        @Valid @RequestBody SupplierRequest request
    ) {
        log.info("Updating supplier: {}", id);
        SupplierResponse updated = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Supplier updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        log.info("Deleting supplier: {}", id);
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Supplier deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(@PathVariable Long id) {
        SupplierResponse supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully"));
    }
    
    @GetMapping("/code/{supplierCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierByCode(@PathVariable String supplierCode) {
        SupplierResponse supplier = supplierService.getSupplierByCode(supplierCode);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully"));
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> getSupplierByEmail(@PathVariable String email) {
        Supplier supplier = supplierService.getSupplierByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(supplier, "Supplier retrieved successfully"));
    }
    
    @GetMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> getAllSuppliers(Pageable pageable) {
        PageResponse<SupplierResponse> suppliers = supplierService.getAllSuppliers(pageable);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> getAllSuppliersList(Pageable pageable) {
        PageResponse<SupplierResponse> suppliers = supplierService.getAllSuppliers(pageable);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> searchSuppliers(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        PageResponse<SupplierResponse> suppliers = supplierService.searchSuppliers(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> activateSupplier(@PathVariable Long id) {
        log.info("Activating supplier: {}", id);
        Supplier activated = supplierService.activateSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Supplier activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> deactivateSupplier(@PathVariable Long id) {
        log.info("Deactivating supplier: {}", id);
        Supplier deactivated = supplierService.deactivateSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Supplier deactivated successfully"));
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> approveSupplier(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        log.info("Approving supplier: {}", id);
        Supplier approved = supplierService.approveSupplier(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Supplier approved successfully"));
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> rejectSupplier(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        log.info("Rejecting supplier: {}", id);
        Supplier rejected = supplierService.rejectSupplier(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Supplier rejected successfully"));
    }
    
    @PutMapping("/{id}/block")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> blockSupplier(
        @PathVariable Long id,
        @RequestParam String blockReason
    ) {
        log.info("Blocking supplier: {}", id);
        Supplier blocked = supplierService.blockSupplier(id, blockReason);
        return ResponseEntity.ok(ApiResponse.success(blocked, "Supplier blocked successfully"));
    }
    
    @PutMapping("/{id}/unblock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Supplier>> unblockSupplier(@PathVariable Long id) {
        log.info("Unblocking supplier: {}", id);
        Supplier unblocked = supplierService.unblockSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(unblocked, "Supplier unblocked successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/active")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Page<Supplier>>> getActiveSuppliers(Pageable pageable) {
        Page<Supplier> suppliers = supplierService.getActiveSuppliers(pageable);
        System.out.println("Total number of supplier are: "+suppliers.getTotalElements());
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Active suppliers retrieved successfully"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getInactiveSuppliers() {
        List<Supplier> suppliers = supplierService.getInactiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Inactive suppliers retrieved successfully"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getApprovedSuppliers() {
        List<Supplier> suppliers = supplierService.getApprovedSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Approved suppliers retrieved successfully"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getPendingApprovalSuppliers() {
        List<Supplier> suppliers = supplierService.getPendingApprovalSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Pending approval suppliers retrieved successfully"));
    }
    
    @GetMapping("/rejected")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getRejectedSuppliers() {
        List<Supplier> suppliers = supplierService.getRejectedSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Rejected suppliers retrieved successfully"));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByStatus(@PathVariable String status) {
        List<Supplier> suppliers = supplierService.getSuppliersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by status retrieved successfully"));
    }
    
    @GetMapping("/blocked")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getBlockedSuppliers() {
        List<Supplier> suppliers = supplierService.getBlockedSuppliers();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Blocked suppliers retrieved successfully"));
    }
    
    @GetMapping("/type/{supplierType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> getSuppliersByType(@PathVariable String supplierType,Pageable pageable) {
        PageResponse<SupplierResponse> suppliers = supplierService.getSuppliersByType(supplierType,pageable);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by type retrieved successfully"));
    }
    
    @GetMapping("/city/{city}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByCity(@PathVariable String city) {
        List<Supplier> suppliers = supplierService.getSuppliersByCity(city);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by city retrieved successfully"));
    }
    
    @GetMapping("/country/{country}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByCountry(@PathVariable String country) {
        List<Supplier> suppliers = supplierService.getSuppliersByCountry(country);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by country retrieved successfully"));
    }
    
    @GetMapping("/local/{country}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getLocalSuppliers(@PathVariable String country) {
        List<Supplier> suppliers = supplierService.getLocalSuppliers(country);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Local suppliers retrieved successfully"));
    }
    
    @GetMapping("/international/{country}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getInternationalSuppliers(@PathVariable String country) {
        List<Supplier> suppliers = supplierService.getInternationalSuppliers(country);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "International suppliers retrieved successfully"));
    }
    
    @GetMapping("/with-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersWithCredit() {
        List<Supplier> suppliers = supplierService.getSuppliersWithCredit();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers with credit retrieved successfully"));
    }
    
    @GetMapping("/credit-exceeded")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getSuppliersExceedingCreditLimit() {
        List<SupplierResponse> suppliers = supplierService.getSuppliersExceedingCreditLimit();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers exceeding credit limit retrieved successfully"));
    }
    
    @GetMapping("/with-outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getSuppliersWithOutstandingBalance() {
        List<SupplierResponse> suppliers = supplierService.getSuppliersWithOutstandingBalance();
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers with outstanding balance retrieved successfully"));
    }
    
    @GetMapping("/credit-limit-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByCreditLimitRange(
        @RequestParam Double minLimit,
        @RequestParam Double maxLimit
    ) {
        List<Supplier> suppliers = supplierService.getSuppliersByCreditLimitRange(minLimit, maxLimit);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by credit limit range retrieved successfully"));
    }
    
    @GetMapping("/balance-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByBalanceRange(
        @RequestParam Double minBalance,
        @RequestParam Double maxBalance
    ) {
        List<Supplier> suppliers = supplierService.getSuppliersByBalanceRange(minBalance, maxBalance);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by balance range retrieved successfully"));
    }
    
    @GetMapping("/high-rated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getHighRatedSuppliers(@RequestParam(defaultValue = "4.0") Double minRating) {
        List<Supplier> suppliers = supplierService.getHighRatedSuppliers(minRating);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "High rated suppliers retrieved successfully"));
    }
    
    @GetMapping("/low-rated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getLowRatedSuppliers(@RequestParam(defaultValue = "3.0") Double maxRating) {
        List<Supplier> suppliers = supplierService.getLowRatedSuppliers(maxRating);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Low rated suppliers retrieved successfully"));
    }
    
    @GetMapping("/region/{region}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByRegion(@PathVariable String region) {
        List<Supplier> suppliers = supplierService.getSuppliersByRegion(region);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers by region retrieved successfully"));
    }
    
    @GetMapping("/requiring-attention")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersRequiringAttention(
        @RequestParam(defaultValue = "3.0") Double lowRatingThreshold
    ) {
        List<Supplier> suppliers = supplierService.getSuppliersRequiringAttention(lowRatingThreshold);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers requiring attention retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getRecentSuppliers(Pageable limit) {
        List<Supplier> suppliers = supplierService.getRecentSuppliers(limit);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Recent suppliers retrieved successfully"));
    }
    
    @GetMapping("/recently-updated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getRecentlyUpdatedSuppliers(Pageable limit) {
        List<Supplier> suppliers = supplierService.getRecentlyUpdatedSuppliers(limit);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Recently updated suppliers retrieved successfully"));
    }
    
    @GetMapping("/top-rated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getTopRatedSuppliers(Pageable limit) {
        List<Supplier> suppliers = supplierService.getTopRatedSuppliers(limit);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Top rated suppliers retrieved successfully"));
    }
    
    @GetMapping("/highest-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersWithHighestBalance(Pageable limit) {
        List<Supplier> suppliers = supplierService.getSuppliersWithHighestBalance(limit);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers with highest balance retrieved successfully"));
    }
    
    @GetMapping("/highest-credit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersWithHighestCreditLimit(Pageable limit) {
        List<Supplier> suppliers = supplierService.getSuppliersWithHighestCreditLimit(limit);
        return ResponseEntity.ok(ApiResponse.success(suppliers, "Suppliers with highest credit limit retrieved successfully"));
    }
    
    // ===================================================================
    // CREDIT OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/credit/update-limit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateCreditLimit(
        @PathVariable Long id,
        @RequestParam Double newCreditLimit
    ) {
        log.info("Updating credit limit for supplier: {}", id);
        supplierService.updateCreditLimit(id, newCreditLimit);
        return ResponseEntity.ok(ApiResponse.success(null, "Credit limit updated successfully"));
    }
    
    @PutMapping("/{id}/credit/update-settings")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Supplier>> updateCreditSettings(
        @PathVariable Long id,
        @RequestParam Double creditLimit,
        @RequestParam Integer creditDays
    ) {
        log.info("Updating credit settings for supplier: {}", id);
        Supplier updated = supplierService.updateCreditSettings(id, creditLimit, creditDays);
        return ResponseEntity.ok(ApiResponse.success(updated, "Credit settings updated successfully"));
    }
    
    @PutMapping("/{id}/credit/enable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> enableCredit(
        @PathVariable Long id,
        @RequestParam Double creditLimit,
        @RequestParam Integer creditDays
    ) {
        log.info("Enabling credit for supplier: {}", id);
        supplierService.enableCredit(id, creditLimit, creditDays);
        return ResponseEntity.ok(ApiResponse.success(null, "Credit enabled successfully"));
    }
    
    @PutMapping("/{id}/credit/disable")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> disableCredit(@PathVariable Long id) {
        log.info("Disabling credit for supplier: {}", id);
        supplierService.disableCredit(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Credit disabled successfully"));
    }
    
    @PutMapping("/{id}/balance/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> updateCurrentBalance(
        @PathVariable Long id,
        @RequestParam Double newBalance
    ) {
        log.info("Updating balance for supplier: {}", id);
        supplierService.updateCurrentBalance(id, newBalance);
        return ResponseEntity.ok(ApiResponse.success(null, "Balance updated successfully"));
    }
    
    @PutMapping("/{id}/balance/increase")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> increaseBalance(
        @PathVariable Long id,
        @RequestParam Double amount
    ) {
        log.info("Increasing balance for supplier: {}", id);
        supplierService.increaseBalance(id, amount);
        return ResponseEntity.ok(ApiResponse.success(null, "Balance increased successfully"));
    }
    
    @PutMapping("/{id}/balance/decrease")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> decreaseBalance(
        @PathVariable Long id,
        @RequestParam Double amount
    ) {
        log.info("Decreasing balance for supplier: {}", id);
        supplierService.decreaseBalance(id, amount);
        return ResponseEntity.ok(ApiResponse.success(null, "Balance decreased successfully"));
    }
    
    @GetMapping("/{id}/credit/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getAvailableCredit(@PathVariable Long id) {
        Double availableCredit = supplierService.getAvailableCredit(id);
        return ResponseEntity.ok(ApiResponse.success(availableCredit, "Available credit retrieved successfully"));
    }
    
    @GetMapping("/{id}/credit/can-extend")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> canExtendCredit(
        @PathVariable Long id,
        @RequestParam Double amount
    ) {
        boolean canExtend = supplierService.canExtendCredit(id, amount);
        return ResponseEntity.ok(ApiResponse.success(canExtend, "Credit extension check completed"));
    }
    
    @GetMapping("/{id}/credit/is-available")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> isCreditAvailable(
        @PathVariable Long id,
        @RequestParam Double amount
    ) {
        boolean isAvailable = supplierService.isCreditAvailable(id, amount);
        return ResponseEntity.ok(ApiResponse.success(isAvailable, "Credit availability checked"));
    }
    
    // ===================================================================
    // RATING OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/rating/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateRating(
        @PathVariable Long id,
        @RequestParam Double rating
    ) {
        log.info("Updating rating for supplier: {}", id);
        supplierService.updateRating(id, rating);
        return ResponseEntity.ok(ApiResponse.success(null, "Rating updated successfully"));
    }
    
    @PutMapping("/{id}/rating/update-with-review")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> updateRatingAndReviews(
        @PathVariable Long id,
        @RequestParam Double rating,
        @RequestParam String reviewComments
    ) {
        log.info("Updating rating and reviews for supplier: {}", id);
        supplierService.updateRatingAndReviews(id, rating, reviewComments);
        return ResponseEntity.ok(ApiResponse.success(null, "Rating and reviews updated successfully"));
    }
    
    @PutMapping("/{id}/orders/increment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> incrementTotalOrders(@PathVariable Long id) {
        log.info("Incrementing total orders for supplier: {}", id);
        supplierService.incrementTotalOrders(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Total orders incremented successfully"));
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @GetMapping("/validate/code/{supplierCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isSupplierCodeAvailable(@PathVariable String supplierCode) {
        boolean available = supplierService.isSupplierCodeAvailable(supplierCode);
        return ResponseEntity.ok(ApiResponse.success(available, "Supplier code availability checked"));
    }
    
    @GetMapping("/validate/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isEmailAvailable(@PathVariable String email) {
        boolean available = supplierService.isEmailAvailable(email);
        return ResponseEntity.ok(ApiResponse.success(available, "Email availability checked"));
    }
    
    @GetMapping("/validate/tax-number/{taxNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isTaxNumberAvailable(@PathVariable String taxNumber) {
        boolean available = supplierService.isTaxNumberAvailable(taxNumber);
        return ResponseEntity.ok(ApiResponse.success(available, "Tax number availability checked"));
    }
    
    @GetMapping("/{id}/can-delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canDeleteSupplier(@PathVariable Long id) {
        boolean canDelete = supplierService.canDeleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(canDelete, "Delete check completed"));
    }
    
    @GetMapping("/{id}/can-approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canApproveSupplier(@PathVariable Long id) {
        boolean canApprove = supplierService.canApproveSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(canApprove, "Approval check completed"));
    }
    
    @GetMapping("/{id}/can-block")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> canBlockSupplier(@PathVariable Long id) {
        boolean canBlock = supplierService.canBlockSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(canBlock, "Block check completed"));
    }
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Supplier>>> createBulkSuppliers(@Valid @RequestBody List<SupplierRequest> requests) {
        log.info("Creating {} suppliers in bulk", requests.size());
        List<Supplier> suppliers = supplierService.createBulkSuppliers(requests);
        return ResponseEntity.ok(ApiResponse.success(suppliers, suppliers.size() + " suppliers created successfully"));
    }
    
    @PutMapping("/bulk/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> activateBulkSuppliers(@RequestBody List<Long> supplierIds) {
        log.info("Activating {} suppliers in bulk", supplierIds.size());
        int count = supplierService.activateBulkSuppliers(supplierIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " suppliers activated successfully"));
    }
    
    @PutMapping("/bulk/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deactivateBulkSuppliers(@RequestBody List<Long> supplierIds) {
        log.info("Deactivating {} suppliers in bulk", supplierIds.size());
        int count = supplierService.deactivateBulkSuppliers(supplierIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " suppliers deactivated successfully"));
    }
    
    @PutMapping("/bulk/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> approveBulkSuppliers(
        @RequestBody List<Long> supplierIds,
        @RequestParam Long approvedByUserId
    ) {
        log.info("Approving {} suppliers in bulk", supplierIds.size());
        int count = supplierService.approveBulkSuppliers(supplierIds, approvedByUserId);
        return ResponseEntity.ok(ApiResponse.success(count, count + " suppliers approved successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkSuppliers(@RequestBody List<Long> supplierIds) {
        log.info("Deleting {} suppliers in bulk", supplierIds.size());
        int count = supplierService.deleteBulkSuppliers(supplierIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " suppliers deleted successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSupplierStatistics() {
        Map<String, Object> statistics = supplierService.getSupplierStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Supplier statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSupplierTypeDistribution() {
        List<Map<String, Object>> distribution = supplierService.getSupplierTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Supplier type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = supplierService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
    }
    
//    @GetMapping("/statistics/by-country")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
//    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByCountry(String country) {
//        List<Supplier> stats = supplierService.getSuppliersByCountry(country);
//        return ResponseEntity.ok(ApiResponse.success(stats, "Suppliers by country retrieved successfully"));
//    }
    
//    @GetMapping("/statistics/by-city")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
//    public ResponseEntity<ApiResponse<List<Supplier>>> getSuppliersByCity(String city) {
//        List<Supplier> stats = supplierService.getSuppliersByCity(city);
//        return ResponseEntity.ok(ApiResponse.success(stats, "Suppliers by city retrieved successfully"));
//    }
    
//    @GetMapping("/statistics/payment-terms-distribution")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
//    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPaymentTermsDistribution() {
//        List<Map<String, Object>> distribution = supplierService.getPaymentTermsDistribution();
//        return ResponseEntity.ok(ApiResponse.success(distribution, "Payment terms distribution retrieved successfully"));
//    }
    
    @GetMapping("/statistics/total-outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalOutstandingBalance() {
        Double totalOutstanding = supplierService.getTotalOutstandingBalance();
        return ResponseEntity.ok(ApiResponse.success(totalOutstanding, "Total outstanding balance retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-credit-limit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalCreditLimit() {
        Double totalCredit = supplierService.getTotalCreditLimit();
        return ResponseEntity.ok(ApiResponse.success(totalCredit, "Total credit limit retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-credit-limit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getAverageCreditLimit() {
        Double avgCredit = supplierService.getAverageCreditLimit();
        return ResponseEntity.ok(ApiResponse.success(avgCredit, "Average credit limit retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-rating")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getAverageRating() {
        Double avgRating = supplierService.getAverageRating();
        return ResponseEntity.ok(ApiResponse.success(avgRating, "Average rating retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getAverageCurrentBalance() {
        Double avgBalance = supplierService.getAverageCurrentBalance();
        return ResponseEntity.ok(ApiResponse.success(avgBalance, "Average balance retrieved successfully"));
    }
    
    @GetMapping("/{id}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSupplierSummary(@PathVariable Long id) {
        Map<String, Object> summary = supplierService.getSupplierSummary(id);
        return ResponseEntity.ok(ApiResponse.success(summary, "Supplier summary retrieved successfully"));
    }
    
    @GetMapping("/statistics/top-by-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopSuppliersByOrders(Pageable  limit) {
        List<Map<String, Object>> topSuppliers = supplierService.getTopSuppliersByOrders(limit);
        return ResponseEntity.ok(ApiResponse.success(topSuppliers, "Top suppliers by orders retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = supplierService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
    
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Long>> countSuppliersByStatus(@PathVariable String status) {
        Long count = supplierService.countSuppliersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(count, "Supplier count by status retrieved successfully"));
    }
    
    @GetMapping("/count/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Long>> countActiveSuppliers() {
        Long count = supplierService.countActiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success(count, "Active supplier count retrieved successfully"));
    }
}
