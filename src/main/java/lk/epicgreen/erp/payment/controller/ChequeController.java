package lk.epicgreen.erp.payment.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.payment.dto.request.ChequeRequest;
import lk.epicgreen.erp.payment.dto.response.ChequeResponse;
import lk.epicgreen.erp.payment.entity.Cheque;
import lk.epicgreen.erp.payment.service.ChequeService;
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
 * Cheque Controller
 * REST controller for cheque operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/payments/cheques")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChequeController {
    
    private final ChequeService chequeService;
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<ChequeResponse>> createCheque(@Valid @RequestBody ChequeRequest request) {
        log.info("Creating cheque for customer: {}", request.getCustomerId());
        ChequeResponse created = chequeService.createCheque(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Cheque created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<ChequeResponse>> updateCheque(@PathVariable Long id, @Valid @RequestBody ChequeRequest request) {
        log.info("Updating cheque: {}", id);
        ChequeResponse updated = chequeService.updateCheque(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Cheque updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteCheque(@PathVariable Long id) {
        log.info("Deleting cheque: {}", id);
        chequeService.deleteCheque(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Cheque deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<ChequeResponse>> getChequeById(@PathVariable Long id) {
        ChequeResponse cheque = chequeService.getChequeById(id);
        return ResponseEntity.ok(ApiResponse.success(cheque, "Cheque retrieved successfully"));
    }
    
    @GetMapping("/number/{chequeNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<Cheque>> getChequeByNumber(@PathVariable String chequeNumber) {
        Cheque cheque = chequeService.getChequeByNumber(chequeNumber);
        return ResponseEntity.ok(ApiResponse.success(cheque, "Cheque retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<ChequeResponse>>> getAllCheques(Pageable pageable) {
        PageResponse<ChequeResponse> cheques = chequeService.getAllCheques(pageable);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<ChequeResponse>>> getAllChequesList(Pageable pageable) {
        PageResponse<ChequeResponse> cheques = chequeService.getAllCheques(pageable);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<ChequeResponse>>> searchCheques(@RequestParam String keyword, Pageable pageable) {
        PageResponse<ChequeResponse> cheques = chequeService.searchCheques(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/present")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Cheque>> presentCheque(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate presentationDate
    ) {
        log.info("Presenting cheque: {}", id);
        Cheque presented = chequeService.presentCheque(id, presentationDate);
        return ResponseEntity.ok(ApiResponse.success(presented, "Cheque presented successfully"));
    }
    
    @PutMapping("/{id}/clear")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> clearCheque(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate clearanceDate
    ) {
        log.info("Clearing cheque: {}", id);
        chequeService.clearCheque(id, clearanceDate);
        return ResponseEntity.ok(ApiResponse.success( "Cheque cleared successfully"));
    }
    
    @PutMapping("/{id}/bounce")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> bounceCheque(@PathVariable Long id, @RequestParam String bounceReason, @RequestParam BigDecimal bounceCharges) {
        log.info("Bouncing cheque: {}", id);
        chequeService.bounceCheque(id, bounceReason, bounceCharges);
        return ResponseEntity.ok(ApiResponse.success("Cheque bounced"));
    }
    
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> cancelCheque(@PathVariable Long id, @RequestParam String cancellationReason) {
        log.info("Cancelling cheque: {}", id);
        Cheque cancelled = chequeService.cancelCheque(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Cheque cancelled successfully"));
    }
    
    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> returnCheque(@PathVariable Long id, @RequestParam String returnReason) {
        log.info("Returning cheque: {}", id);
        Cheque returned = chequeService.returnCheque(id, returnReason);
        return ResponseEntity.ok(ApiResponse.success(returned, "Cheque returned successfully"));
    }
    
    // Query Operations
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getPendingCheques() {
        List<Cheque> cheques = chequeService.getPendingCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Pending cheques retrieved successfully"));
    }
    
    @GetMapping("/presented")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getPresentedCheques() {
        List<Cheque> cheques = chequeService.getPresentedCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Presented cheques retrieved successfully"));
    }
    
    @GetMapping("/cleared")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getClearedCheques() {
        List<Cheque> cheques = chequeService.getClearedCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cleared cheques retrieved successfully"));
    }
    
    @GetMapping("/bounced")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<ChequeResponse>>> getBouncedCheques() {
        List<ChequeResponse> cheques = chequeService.getBouncedCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Bounced cheques retrieved successfully"));
    }
    
    @GetMapping("/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getCancelledCheques() {
        List<Cheque> cheques = chequeService.getCancelledCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cancelled cheques retrieved successfully"));
    }
    
    @GetMapping("/post-dated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getPostDatedCheques() {
        List<Cheque> cheques = chequeService.getPostDatedCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Post-dated cheques retrieved successfully"));
    }
    
    @GetMapping("/due-for-presentation")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getChequesDueForPresentation() {
        List<Cheque> cheques = chequeService.getChequesDueForPresentation();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques due for presentation retrieved successfully"));
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getOverdueCheques() {
        List<Cheque> cheques = chequeService.getOverdueCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Overdue cheques retrieved successfully"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getChequesRequiringAction() {
        List<Cheque> cheques = chequeService.getChequesRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques requiring action retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ChequeResponse>>> getChequesByCustomer(@PathVariable Long customerId, Pageable pageable) {
        List<ChequeResponse> cheques = chequeService.getChequesByCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Customer cheques retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<ChequeResponse>>> getChequesByCustomerList(@PathVariable Long customerId) {
        List<ChequeResponse> cheques = chequeService.getChequesByCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Customer cheques list retrieved successfully"));
    }
    
    @GetMapping("/bank/{bankName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<ChequeResponse>>> getChequesByBank(@PathVariable String bankName) {
        List<ChequeResponse> cheques = chequeService.getChequesByBank(bankName);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques by bank retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<ChequeResponse>>> getChequesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<ChequeResponse> cheques = chequeService.getChequesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques by date range retrieved successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getRecentCheques(Pageable pageable) {
        List<Cheque> cheques = chequeService.getRecentCheques(pageable);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Recent cheques retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getCustomerRecentCheques(@PathVariable Long customerId, Pageable page) {
        List<Cheque> cheques = chequeService.getCustomerRecentCheques(customerId, page);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Customer recent cheques retrieved successfully"));
    }
    
    // Validation
    @GetMapping("/{id}/can-present")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Boolean>> canPresentCheque(@PathVariable Long id) {
        boolean canPresent = chequeService.canPresentCheque(id);
        return ResponseEntity.ok(ApiResponse.success(canPresent, "Present check completed"));
    }
    
    @GetMapping("/{id}/can-clear")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> canClearCheque(@PathVariable Long id) {
        boolean canClear = chequeService.canClearCheque(id);
        return ResponseEntity.ok(ApiResponse.success(canClear, "Clear check completed"));
    }
    
    @GetMapping("/{id}/can-bounce")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> canBounceCheque(@PathVariable Long id) {
        boolean canBounce = chequeService.canBounceCheque(id);
        return ResponseEntity.ok(ApiResponse.success(canBounce, "Bounce check completed"));
    }
    
    // Batch Operations
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> createBulkCheques(@Valid @RequestBody List<ChequeRequest> requests) {
        log.info("Creating {} cheques in bulk", requests.size());
        List<Cheque> cheques = chequeService.createBulkCheques(requests);
        return ResponseEntity.ok(ApiResponse.success(cheques, cheques.size() + " cheques created successfully"));
    }
    
    @PutMapping("/bulk/present")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Integer>> presentBulkCheques(
        @RequestBody List<Long> chequeIds,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate presentationDate
    ) {
        log.info("Presenting {} cheques in bulk", chequeIds.size());
        int count = chequeService.presentBulkCheques(chequeIds, presentationDate);
        return ResponseEntity.ok(ApiResponse.success(count, count + " cheques presented successfully"));
    }
    
    @PutMapping("/bulk/clear")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Integer>> clearBulkCheques(
        @RequestBody List<Long> chequeIds,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate clearanceDate
    ) {
        log.info("Clearing {} cheques in bulk", chequeIds.size());
        int count = chequeService.clearBulkCheques(chequeIds, clearanceDate);
        return ResponseEntity.ok(ApiResponse.success(count, count + " cheques cleared successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkCheques(@RequestBody List<Long> chequeIds) {
        log.info("Deleting {} cheques in bulk", chequeIds.size());
        int count = chequeService.deleteBulkCheques(chequeIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " cheques deleted successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getChequeStatistics() {
        Map<String, Object> statistics = chequeService.getChequeStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Cheque statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getChequeStatusDistribution() {
        List<Map<String, Object>> distribution = chequeService.getChequeStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Cheque status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/bank-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getBankDistribution() {
        List<Map<String, Object>> distribution = chequeService.getBankDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Bank distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMonthlyChequeCount(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Map<String, Object>> count = chequeService.getMonthlyChequeCount(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(count, "Monthly cheque count retrieved successfully"));
    }
    
    @GetMapping("/statistics/total-amount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getTotalChequeAmount() {
        Double total = chequeService.getTotalChequeAmount();
        return ResponseEntity.ok(ApiResponse.success(total, "Total cheque amount retrieved successfully"));
    }
    
    @GetMapping("/statistics/average-amount")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getAverageChequeAmount() {
        Double average = chequeService.getAverageChequeAmount();
        return ResponseEntity.ok(ApiResponse.success(average, "Average cheque amount retrieved successfully"));
    }
    
    @GetMapping("/statistics/bounce-rate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Double>> getChequeBounceRate() {
        Double bounceRate = chequeService.getChequeBounceRate();
        return ResponseEntity.ok(ApiResponse.success(bounceRate, "Cheque bounce rate retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = chequeService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
