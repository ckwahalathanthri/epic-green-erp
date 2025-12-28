package lk.epicgreen.erp.accounting.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.accounting.entity.GeneralLedger;
import lk.epicgreen.erp.accounting.service.AccountingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * General Ledger Controller
 * REST controller for general ledger query operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/accounting/ledger")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class GeneralLedgerController {
    
    private final AccountingService accountingService;
    
    // General Ledger Query Operations
    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getAccountLedger(@PathVariable Long accountId) {
        List<GeneralLedger> ledger = accountingService.getAccountLedger(accountId);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Account ledger retrieved successfully"));
    }
    
    @GetMapping("/account/{accountId}/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getAccountLedgerForPeriod(
        @PathVariable Long accountId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<GeneralLedger> ledger = accountingService.getAccountLedgerForPeriod(accountId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(ledger, "Period ledger retrieved successfully"));
    }
    
    @GetMapping("/journal-entry/{journalEntryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getLedgerEntriesByJournalEntry(@PathVariable Long journalEntryId) {
        List<GeneralLedger> entries = accountingService.getLedgerEntriesByJournalEntry(journalEntryId);
        return ResponseEntity.ok(ApiResponse.success(entries, "Journal entry ledger retrieved successfully"));
    }
    
    @GetMapping("/posted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getPostedLedgerEntries() {
        List<GeneralLedger> entries = accountingService.getPostedLedgerEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Posted ledger entries retrieved successfully"));
    }
    
    @GetMapping("/unposted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<GeneralLedger>>> getUnpostedLedgerEntries() {
        List<GeneralLedger> entries = accountingService.getUnpostedLedgerEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Unposted ledger entries retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<GeneralLedger>>> searchLedgerEntries(@RequestParam String keyword, Pageable pageable) {
        Page<GeneralLedger> entries = accountingService.searchLedgerEntries(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Ledger search results retrieved successfully"));
    }
}
