package lk.epicgreen.erp.returns.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.returns.dto.SalesReturnRequest;
import lk.epicgreen.erp.returns.entity.SalesReturn;
import lk.epicgreen.erp.returns.service.SalesReturnService;
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
 * SalesReturn Controller
 * REST controller for sales return operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/returns/sales-returns")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class SalesReturnController {
    
    private final SalesReturnService salesReturnService;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesReturn>> createSalesReturn(
        @Valid @RequestBody SalesReturnRequest request
    ) {
        SalesReturn created = salesReturnService.createSalesReturn(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Sales return created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesReturn>> updateSalesReturn(
        @PathVariable Long id,
        @Valid @RequestBody SalesReturnRequest request
    ) {
        SalesReturn updated = salesReturnService.updateSalesReturn(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Sales return updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteSalesReturn(@PathVariable Long id) {
        salesReturnService.deleteSalesReturn(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Sales return deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<SalesReturn>> getSalesReturnById(@PathVariable Long id) {
        SalesReturn salesReturn = salesReturnService.getSalesReturnById(id);
        return ResponseEntity.ok(ApiResponse.success(salesReturn, "Sales return retrieved successfully"));
    }
    
    @GetMapping("/number/{returnNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<SalesReturn>> getSalesReturnByNumber(@PathVariable String returnNumber) {
        SalesReturn salesReturn = salesReturnService.getSalesReturnByNumber(returnNumber);
        return ResponseEntity.ok(ApiResponse.success(salesReturn, "Sales return retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<SalesReturn>>> getAllSalesReturns(Pageable pageable) {
        Page<SalesReturn> returns = salesReturnService.getAllSalesReturns(pageable);
        return ResponseEntity.ok(ApiResponse.success(returns, "Sales returns retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<SalesReturn>>> searchSalesReturns(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<SalesReturn> returns = salesReturnService.searchSalesReturns(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(returns, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesReturn>> approveSalesReturn(
        @PathVariable Long id,
        @RequestParam Long approvedBy,
        @RequestParam(required = false) String approvalNotes
    ) {
        SalesReturn approved = salesReturnService.approveSalesReturn(id, approvedBy, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Sales return approved successfully"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesReturn>> rejectSalesReturn(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        SalesReturn rejected = salesReturnService.rejectSalesReturn(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Sales return rejected successfully"));
    }
    
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesReturn>> completeSalesReturn(@PathVariable Long id) {
        SalesReturn completed = salesReturnService.completeSalesReturn(id);
        return ResponseEntity.ok(ApiResponse.success(completed, "Sales return completed successfully"));
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesReturn>> cancelSalesReturn(
        @PathVariable Long id,
        @RequestParam String cancellationReason
    ) {
        SalesReturn cancelled = salesReturnService.cancelSalesReturn(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Sales return cancelled successfully"));
    }
    
    @PostMapping("/{id}/submit-for-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<SalesReturn>> submitForApproval(@PathVariable Long id) {
        SalesReturn submitted = salesReturnService.submitForApproval(id);
        return ResponseEntity.ok(ApiResponse.success(submitted, "Sales return submitted for approval"));
    }
    
    // ===================================================================
    // INSPECTION OPERATIONS
    // ===================================================================
    
    @PostMapping("/{id}/inspect")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'QC_INSPECTOR')")
    public ResponseEntity<ApiResponse<SalesReturn>> performQualityInspection(
        @PathVariable Long id,
        @RequestParam Long inspectedBy,
        @RequestParam String inspectionResult,
        @RequestParam(required = false) String inspectionNotes
    ) {
        SalesReturn inspected = salesReturnService.performQualityInspection(id, inspectedBy, inspectionResult, inspectionNotes);
        return ResponseEntity.ok(ApiResponse.success(inspected, "Quality inspection completed"));
    }
    
    @PostMapping("/{id}/pass-inspection")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'QC_INSPECTOR')")
    public ResponseEntity<ApiResponse<SalesReturn>> passInspection(
        @PathVariable Long id,
        @RequestParam Long inspectedBy
    ) {
        SalesReturn passed = salesReturnService.passInspection(id, inspectedBy);
        return ResponseEntity.ok(ApiResponse.success(passed, "Return passed inspection"));
    }
    
    @PostMapping("/{id}/fail-inspection")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'QC_INSPECTOR')")
    public ResponseEntity<ApiResponse<SalesReturn>> failInspection(
        @PathVariable Long id,
        @RequestParam Long inspectedBy,
        @RequestParam String failureReason
    ) {
        SalesReturn failed = salesReturnService.failInspection(id, inspectedBy, failureReason);
        return ResponseEntity.ok(ApiResponse.success(failed, "Return failed inspection"));
    }
    
    @GetMapping("/pending-inspection")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'QC_INSPECTOR')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getReturnsPendingInspection() {
        List<SalesReturn> returns = salesReturnService.getReturnsPendingInspection();
        return ResponseEntity.ok(ApiResponse.success(returns, "Pending inspection returns retrieved"));
    }
    
    // ===================================================================
    // CREDIT NOTE OPERATIONS
    // ===================================================================
    
    @PostMapping("/{id}/generate-credit-note")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SalesReturn>> generateCreditNote(@PathVariable Long id) {
        SalesReturn salesReturn = salesReturnService.generateCreditNote(id);
        return ResponseEntity.ok(ApiResponse.success(salesReturn, "Credit note generated successfully"));
    }
    
    @GetMapping("/without-credit-note")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getReturnsWithoutCreditNote() {
        List<SalesReturn> returns = salesReturnService.getReturnsWithoutCreditNote();
        return ResponseEntity.ok(ApiResponse.success(returns, "Returns without credit note retrieved"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getPendingReturns() {
        List<SalesReturn> returns = salesReturnService.getPendingReturns();
        return ResponseEntity.ok(ApiResponse.success(returns, "Pending returns retrieved"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getApprovedReturns() {
        List<SalesReturn> returns = salesReturnService.getApprovedReturns();
        return ResponseEntity.ok(ApiResponse.success(returns, "Approved returns retrieved"));
    }
    
    @GetMapping("/rejected")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getRejectedReturns() {
        List<SalesReturn> returns = salesReturnService.getRejectedReturns();
        return ResponseEntity.ok(ApiResponse.success(returns, "Rejected returns retrieved"));
    }
    
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getCompletedReturns() {
        List<SalesReturn> returns = salesReturnService.getCompletedReturns();
        return ResponseEntity.ok(ApiResponse.success(returns, "Completed returns retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getReturnsPendingApproval() {
        List<SalesReturn> returns = salesReturnService.getReturnsPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(returns, "Returns pending approval retrieved"));
    }
    
    @GetMapping("/requiring-quality-check")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'QC_INSPECTOR')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getReturnsRequiringQualityCheck() {
        List<SalesReturn> returns = salesReturnService.getReturnsRequiringQualityCheck();
        return ResponseEntity.ok(ApiResponse.success(returns, "Returns requiring quality check retrieved"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<SalesReturn>>> getReturnsByCustomer(
        @PathVariable Long customerId,
        Pageable pageable
    ) {
        Page<SalesReturn> returns = salesReturnService.getReturnsByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(returns, "Customer returns retrieved"));
    }
    
    @GetMapping("/sales-order/{salesOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getReturnsBySalesOrder(@PathVariable Long salesOrderId) {
        List<SalesReturn> returns = salesReturnService.getReturnsBySalesOrder(salesOrderId);
        return ResponseEntity.ok(ApiResponse.success(returns, "Sales order returns retrieved"));
    }
    
    @GetMapping("/invoice/{invoiceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getReturnsByInvoice(@PathVariable Long invoiceId) {
        List<SalesReturn> returns = salesReturnService.getReturnsByInvoice(invoiceId);
        return ResponseEntity.ok(ApiResponse.success(returns, "Invoice returns retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getReturnsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<SalesReturn> returns = salesReturnService.getReturnsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(returns, "Date range returns retrieved"));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getReturnsByWarehouse(@PathVariable Long warehouseId) {
        List<SalesReturn> returns = salesReturnService.getReturnsByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(returns, "Warehouse returns retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<SalesReturn>>> getRecentReturns(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<SalesReturn> returns = salesReturnService.getRecentReturns(limit);
        return ResponseEntity.ok(ApiResponse.success(returns, "Recent returns retrieved"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = salesReturnService.getSalesReturnStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getReturnTypeDistribution() {
        List<Map<String, Object>> distribution = salesReturnService.getReturnTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Type distribution retrieved"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = salesReturnService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved"));
    }
    
    @GetMapping("/statistics/reason-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getReturnReasonDistribution() {
        List<Map<String, Object>> distribution = salesReturnService.getReturnReasonDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Reason distribution retrieved"));
    }
    
    @GetMapping("/statistics/monthly-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyReturnCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> count = salesReturnService.getMonthlyReturnCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(count, "Monthly return count retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = salesReturnService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
