package lk.epicgreen.erp.accounting.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.accounting.dto.JournalEntryRequest;
import lk.epicgreen.erp.accounting.entity.JournalEntry;
import lk.epicgreen.erp.accounting.service.JournalEntryService;
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
 * JournalEntry Controller
 * REST controller for journal entry operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/accounting/journal-entries")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class JournalEntryController {
    
    private final JournalEntryService journalEntryService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> createJournalEntry(
        @Valid @RequestBody JournalEntryRequest request
    ) {
        JournalEntry created = journalEntryService.createJournalEntry(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Journal entry created"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> updateJournalEntry(
        @PathVariable Long id,
        @Valid @RequestBody JournalEntryRequest request
    ) {
        JournalEntry updated = journalEntryService.updateJournalEntry(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Journal entry updated"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> deleteJournalEntry(@PathVariable Long id) {
        journalEntryService.deleteJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Journal entry deleted"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<JournalEntry>> getJournalEntryById(@PathVariable Long id) {
        JournalEntry entry = journalEntryService.getJournalEntryById(id);
        return ResponseEntity.ok(ApiResponse.success(entry, "Journal entry retrieved"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<JournalEntry>>> getAllJournalEntries(Pageable pageable) {
        Page<JournalEntry> entries = journalEntryService.getAllJournalEntries(pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Journal entries retrieved"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Page<JournalEntry>>> searchJournalEntries(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<JournalEntry> entries = journalEntryService.searchJournalEntries(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Search results retrieved"));
    }
    
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getDraftEntries() {
        List<JournalEntry> entries = journalEntryService.getDraftEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Draft entries retrieved"));
    }
    
    @GetMapping("/posted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getPostedEntries() {
        List<JournalEntry> entries = journalEntryService.getPostedEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Posted entries retrieved"));
    }
    
    @GetMapping("/unposted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getUnpostedEntries() {
        List<JournalEntry> entries = journalEntryService.getUnpostedEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Unposted entries retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getEntriesPendingApproval() {
        List<JournalEntry> entries = journalEntryService.getEntriesPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(entries, "Pending entries retrieved"));
    }
    
    @PostMapping("/{id}/post")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> postJournalEntry(@PathVariable Long id) {
        JournalEntry posted = journalEntryService.postJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success(posted, "Journal entry posted"));
    }
    
    @PostMapping("/{id}/unpost")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> unpostJournalEntry(@PathVariable Long id) {
        JournalEntry unposted = journalEntryService.unpostJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success(unposted, "Journal entry unposted"));
    }
    
    @PostMapping("/{id}/reverse")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> reverseJournalEntry(
        @PathVariable Long id,
        @RequestParam String reason
    ) {
        JournalEntry reversed = journalEntryService.reverseJournalEntry(id, reason);
        return ResponseEntity.ok(ApiResponse.success(reversed, "Journal entry reversed"));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<JournalEntry>> approveJournalEntry(
        @PathVariable Long id,
        @RequestParam Long approvedBy
    ) {
        JournalEntry approved = journalEntryService.approveJournalEntry(id, approvedBy);
        return ResponseEntity.ok(ApiResponse.success(approved, "Journal entry approved"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<JournalEntry>> rejectJournalEntry(
        @PathVariable Long id,
        @RequestParam String reason
    ) {
        JournalEntry rejected = journalEntryService.rejectJournalEntry(id, reason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Journal entry rejected"));
    }
    
    @GetMapping("/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getEntriesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<JournalEntry> entries = journalEntryService.getEntriesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(entries, "Period entries retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = journalEntryService.getJournalEntryStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = journalEntryService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
