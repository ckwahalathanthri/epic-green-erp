package lk.epicgreen.erp.returns.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.returns.entity.CreditNote;
import lk.epicgreen.erp.returns.repository.CreditNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CreditNote Controller
 * REST controller for credit note operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/returns/credit-notes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CreditNoteController {
    
    private final CreditNoteRepository creditNoteRepository;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<CreditNote>> getCreditNoteById(@PathVariable Long id) {
        CreditNote creditNote = creditNoteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credit note not found with id: " + id));
        return ResponseEntity.ok(ApiResponse.success(creditNote, "Credit note retrieved successfully"));
    }
    
    @GetMapping("/number/{creditNoteNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<CreditNote>> getCreditNoteByNumber(@PathVariable String creditNoteNumber) {
        CreditNote creditNote = creditNoteRepository.findByCreditNoteNumber(creditNoteNumber)
            .orElseThrow(() -> new RuntimeException("Credit note not found with number: " + creditNoteNumber));
        return ResponseEntity.ok(ApiResponse.success(creditNote, "Credit note retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<CreditNote>>> getAllCreditNotes(Pageable pageable) {
        Page<CreditNote> creditNotes = creditNoteRepository.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Credit notes retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<CreditNote>>> searchCreditNotes(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<CreditNote> creditNotes = creditNoteRepository.searchCreditNotes(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Search results retrieved successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCreditNote(@PathVariable Long id) {
        creditNoteRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Credit note deleted successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<CreditNote>> approveCreditNote(
        @PathVariable Long id,
        @RequestParam Long approvedBy
    ) {
        CreditNote creditNote = creditNoteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credit note not found"));
        
        // Use correct field names
        creditNote.setIsApproved(true);
        creditNote.setStatus("APPROVED");
        creditNote.setApprovalDate(LocalDate.now());
        creditNote.setApprovedBy(String.valueOf(approvedBy));
        
        CreditNote updated = creditNoteRepository.save(creditNote);
        return ResponseEntity.ok(ApiResponse.success(updated, "Credit note approved successfully"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<CreditNote>> rejectCreditNote(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        CreditNote creditNote = creditNoteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credit note not found"));
        
        creditNote.setRejectionReason(rejectionReason);
        creditNote.setRejectedDate(LocalDate.now());
        creditNote.setStatus("REJECTED");
        
        CreditNote updated = creditNoteRepository.save(creditNote);
        return ResponseEntity.ok(ApiResponse.success(updated, "Credit note rejected successfully"));
    }
    
    @PostMapping("/{id}/apply")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<CreditNote>> applyCreditNote(
        @PathVariable Long id,
        @RequestParam Double appliedAmount
    ) {
        CreditNote creditNote = creditNoteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credit note not found"));
        
        if (!creditNote.getIsApproved()) {
            throw new RuntimeException("Cannot apply unapproved credit note");
        }
        
        BigDecimal appliedBD = BigDecimal.valueOf(appliedAmount != null ? appliedAmount : 0.0);
        BigDecimal total = creditNote.getCreditAmount();
        BigDecimal remaining = total.subtract(appliedBD);
        
        creditNote.setRemainingCreditAmount(remaining);
        creditNote.setIsApplied(true);
        creditNote.setAppliedDate(LocalDate.now());
        
        CreditNote updated = creditNoteRepository.save(creditNote);
        return ResponseEntity.ok(ApiResponse.success(updated, "Credit note applied successfully"));
    }
    
    @PostMapping("/{id}/refund")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<CreditNote>> refundCreditNote(
        @PathVariable Long id,
        @RequestParam String refundMethod
    ) {
        CreditNote creditNote = creditNoteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credit note not found"));
        
        creditNote.setIsRefunded(true);
        creditNote.setRefundedDate(LocalDate.now());
        creditNote.setRefundType(refundMethod);
        creditNote.setStatus("REFUNDED");
        
        CreditNote updated = creditNoteRepository.save(creditNote);
        return ResponseEntity.ok(ApiResponse.success(updated, "Credit note refunded successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<CreditNote>>> getCreditNotesByStatus(@PathVariable String status) {
        List<CreditNote> creditNotes = creditNoteRepository.findByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Credit notes retrieved successfully"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<List<CreditNote>>> getCreditNotesByCustomer(@PathVariable Long customerId) {
        List<CreditNote> creditNotes = creditNoteRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Credit notes retrieved successfully"));
    }
    
    @GetMapping("/sales-return/{salesReturnId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<CreditNote>> getCreditNoteBySalesReturn(@PathVariable Long salesReturnId) {
        CreditNote creditNote = creditNoteRepository.findBySalesReturnId(salesReturnId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credit note not found for sales return: " + salesReturnId));
        return ResponseEntity.ok(ApiResponse.success(creditNote, "Credit note retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<CreditNote>>> getCreditNotesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<CreditNote> creditNotes = creditNoteRepository.findByCreditNoteDateBetween(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Credit notes retrieved successfully"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<CreditNote>>> getPendingCreditNotes() {
        List<CreditNote> creditNotes = creditNoteRepository.findPendingCreditNotes();
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Pending credit notes retrieved successfully"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<CreditNote>>> getApprovedCreditNotes() {
        List<CreditNote> creditNotes = creditNoteRepository.findApprovedCreditNotes();
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Approved credit notes retrieved successfully"));
    }
    
    @GetMapping("/unapplied")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<CreditNote>>> getUnappliedCreditNotes() {
        List<CreditNote> creditNotes = creditNoteRepository.findUnappliedCreditNotes();
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Unapplied credit notes retrieved successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCreditNoteStatistics() {
        Map<String, Object> stats = Map.of(
            "total", creditNoteRepository.count(),
            "pending", creditNoteRepository.findPendingCreditNotes().size(),
            "approved", creditNoteRepository.findApprovedCreditNotes().size(),
            "applied", creditNoteRepository.findAppliedCreditNotes().size(),
            "unapplied", creditNoteRepository.findUnappliedCreditNotes().size()
        );
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved successfully"));
    }
    
    @GetMapping("/total-credit/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getTotalCreditByCustomer(@PathVariable Long customerId) {
        Double totalCredit = creditNoteRepository.findByCustomerId(customerId).stream()
            .map(CreditNote::getCreditAmount)
            .filter(amount -> amount != null)
            .map(BigDecimal::doubleValue)
            .reduce(0.0, Double::sum);
        return ResponseEntity.ok(ApiResponse.success(totalCredit, "Total credit calculated successfully"));
    }
    
    @GetMapping("/remaining-credit/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<Double>> getRemainingCreditByCustomer(@PathVariable Long customerId) {
        Double remainingCredit = creditNoteRepository.findByCustomerId(customerId).stream()
            .filter(cn -> cn.getIsApproved() && !cn.getIsApplied())
            .map(CreditNote::getRemainingCreditAmount)
            .filter(amount -> amount != null)
            .map(BigDecimal::doubleValue)
            .reduce(0.0, Double::sum);
        return ResponseEntity.ok(ApiResponse.success(remainingCredit, "Remaining credit calculated successfully"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'USER')")
    public ResponseEntity<ApiResponse<List<CreditNote>>> getRecentCreditNotes(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<CreditNote> creditNotes = creditNoteRepository.findRecentCreditNotes(PageRequest.of(0, limit));
        return ResponseEntity.ok(ApiResponse.success(creditNotes, "Recent credit notes retrieved successfully"));
    }
}
