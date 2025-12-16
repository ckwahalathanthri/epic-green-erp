package lk.epicgreen.erp.payment.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.payment.dto.ChequeRequest;
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

import jakarta.validation.Valid;
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
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Cheque>> createCheque(@Valid @RequestBody ChequeRequest request) {
        Cheque created = chequeService.createCheque(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Cheque created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> updateCheque(
        @PathVariable Long id,
        @Valid @RequestBody ChequeRequest request
    ) {
        Cheque updated = chequeService.updateCheque(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Cheque updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteCheque(@PathVariable Long id) {
        chequeService.deleteCheque(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Cheque deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<Cheque>> getChequeById(@PathVariable Long id) {
        Cheque cheque = chequeService.getChequeById(id);
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
    public ResponseEntity<ApiResponse<Page<Cheque>>> getAllCheques(Pageable pageable) {
        Page<Cheque> cheques = chequeService.getAllCheques(pageable);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER', 'USER')")
    public ResponseEntity<ApiResponse<Page<Cheque>>> searchCheques(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Cheque> cheques = chequeService.searchCheques(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/present")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<Cheque>> presentCheque(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate presentationDate
    ) {
        Cheque presented = chequeService.presentCheque(id, presentationDate);
        return ResponseEntity.ok(ApiResponse.success(presented, "Cheque presented"));
    }
    
    @PostMapping("/{id}/clear")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> clearCheque(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate clearanceDate
    ) {
        Cheque cleared = chequeService.clearCheque(id, clearanceDate);
        return ResponseEntity.ok(ApiResponse.success(cleared, "Cheque cleared"));
    }
    
    @PostMapping("/{id}/bounce")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> bounceCheque(
        @PathVariable Long id,
        @RequestParam String bounceReason
    ) {
        Cheque bounced = chequeService.bounceCheque(id, bounceReason);
        return ResponseEntity.ok(ApiResponse.success(bounced, "Cheque marked as bounced"));
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> cancelCheque(
        @PathVariable Long id,
        @RequestParam String cancellationReason
    ) {
        Cheque cancelled = chequeService.cancelCheque(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Cheque cancelled"));
    }
    
    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Cheque>> returnCheque(
        @PathVariable Long id,
        @RequestParam String returnReason
    ) {
        Cheque returned = chequeService.returnCheque(id, returnReason);
        return ResponseEntity.ok(ApiResponse.success(returned, "Cheque returned"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getPendingCheques() {
        List<Cheque> cheques = chequeService.getPendingCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Pending cheques retrieved"));
    }
    
    @GetMapping("/presented")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getPresentedCheques() {
        List<Cheque> cheques = chequeService.getPresentedCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Presented cheques retrieved"));
    }
    
    @GetMapping("/cleared")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getClearedCheques() {
        List<Cheque> cheques = chequeService.getClearedCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cleared cheques retrieved"));
    }
    
    @GetMapping("/bounced")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getBouncedCheques() {
        List<Cheque> cheques = chequeService.getBouncedCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Bounced cheques retrieved"));
    }
    
    @GetMapping("/cancelled")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getCancelledCheques() {
        List<Cheque> cheques = chequeService.getCancelledCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cancelled cheques retrieved"));
    }
    
    @GetMapping("/post-dated")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getPostDatedCheques() {
        List<Cheque> cheques = chequeService.getPostDatedCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Post-dated cheques retrieved"));
    }
    
    @GetMapping("/due-for-presentation")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getChequesDueForPresentation() {
        List<Cheque> cheques = chequeService.getChequesDueForPresentation();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques due for presentation retrieved"));
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getOverdueCheques() {
        List<Cheque> cheques = chequeService.getOverdueCheques();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Overdue cheques retrieved"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getChequesRequiringAction() {
        List<Cheque> cheques = chequeService.getChequesRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(cheques, "Cheques requiring action retrieved"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<Cheque>>> getChequesByCustomer(
        @PathVariable Long customerId,
        Pageable pageable
    ) {
        Page<Cheque> cheques = chequeService.getChequesByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Customer cheques retrieved"));
    }
    
    @GetMapping("/bank/{bankName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getChequesByBank(@PathVariable String bankName) {
        List<Cheque> cheques = chequeService.getChequesByBank(bankName);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Bank cheques retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getChequesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Cheque> cheques = chequeService.getChequesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Date range cheques retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<Cheque>>> getRecentCheques(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Cheque> cheques = chequeService.getRecentCheques(limit);
        return ResponseEntity.ok(ApiResponse.success(cheques, "Recent cheques retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = chequeService.getChequeStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = chequeService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
