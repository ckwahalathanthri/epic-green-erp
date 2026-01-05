package lk.epicgreen.erp.accounting.controller;

import lk.epicgreen.erp.accounting.dto.response.JournalEntryResponse;
import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.accounting.dto.request.JournalEntryRequest;
import lk.epicgreen.erp.accounting.entity.JournalEntry;
import lk.epicgreen.erp.accounting.entity.JournalEntryLine;
import lk.epicgreen.erp.accounting.service.JournalEntryService;
import lk.epicgreen.erp.common.dto.PageResponse;
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
 * Journal Entry Controller
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
    
    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> createJournalEntry(@Valid @RequestBody JournalEntryRequest request) {
        log.info("Creating journal entry");
        JournalEntryResponse created = journalEntryService.createJournalEntry(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Journal entry created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> updateJournalEntry(@PathVariable Long id, @Valid @RequestBody JournalEntryRequest request) {
        log.info("Updating journal entry: {}", id);
        JournalEntryResponse updated = journalEntryService.updateJournalEntry(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Journal entry updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> deleteJournalEntry(@PathVariable Long id) {
        log.info("Deleting journal entry: {}", id);
        journalEntryService.deleteJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Journal entry deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> getJournalEntryById(@PathVariable Long id) {
        JournalEntryResponse entry = journalEntryService.getJournalEntryById(id);
        return ResponseEntity.ok(ApiResponse.success(entry, "Journal entry retrieved successfully"));
    }
    
    @GetMapping("/number/{entryNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> getJournalEntryByNumber(@PathVariable String entryNumber) {
        JournalEntryResponse entry = journalEntryService.getJournalEntryByNumber(entryNumber);
        return ResponseEntity.ok(ApiResponse.success(entry, "Journal entry retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<JournalEntryResponse>>> getAllJournalEntries(Pageable pageable) {
        PageResponse<JournalEntryResponse> entries = journalEntryService.getAllJournalEntries(pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Journal entries retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getAllJournalEntriesList() {
        List<JournalEntry> entries = journalEntryService.getAllJournalEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Journal entries list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<JournalEntry>>> searchJournalEntries(@RequestParam String keyword, Pageable pageable) {
        Page<JournalEntry> entries = journalEntryService.searchJournalEntries(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(entries, "Search results retrieved successfully"));
    }
    
    // Status Operations
    @PutMapping("/{id}/post")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> postJournalEntry(@PathVariable Long id) {
        log.info("Posting journal entry: {}", id);
        JournalEntry posted = journalEntryService.postJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success(posted, "Journal entry posted successfully"));
    }
    
    @PutMapping("/{id}/unpost")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> unpostJournalEntry(@PathVariable Long id) {
        log.info("Unposting journal entry: {}", id);
        JournalEntry unposted = journalEntryService.unpostJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success(unposted, "Journal entry unposted successfully"));
    }
    
    @PostMapping("/{id}/reverse")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> reverseJournalEntry(@PathVariable Long id, @RequestParam String reason) {
        log.info("Reversing journal entry: {}", id);
        JournalEntry reversed = journalEntryService.reverseJournalEntry(id, reason);
        return ResponseEntity.ok(ApiResponse.success(reversed, "Journal entry reversed successfully"));
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<JournalEntry>> approveJournalEntry(@PathVariable Long id, @RequestParam Long approvedBy) {
        log.info("Approving journal entry: {}", id);
        JournalEntry approved = journalEntryService.approveJournalEntry(id, approvedBy);
        return ResponseEntity.ok(ApiResponse.success(approved, "Journal entry approved successfully"));
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<JournalEntry>> rejectJournalEntry(@PathVariable Long id, @RequestParam String reason) {
        log.info("Rejecting journal entry: {}", id);
        JournalEntry rejected = journalEntryService.rejectJournalEntry(id, reason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Journal entry rejected"));
    }
    
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntry>> submitForApproval(@PathVariable Long id) {
        log.info("Submitting journal entry for approval: {}", id);
        JournalEntry submitted = journalEntryService.submitForApproval(id);
        return ResponseEntity.ok(ApiResponse.success(submitted, "Journal entry submitted for approval"));
    }
    
    // Query Operations
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getDraftEntries() {
        List<JournalEntry> entries = journalEntryService.getDraftEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Draft entries retrieved successfully"));
    }
    
    @GetMapping("/posted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getPostedEntries() {
        List<JournalEntry> entries = journalEntryService.getPostedEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Posted entries retrieved successfully"));
    }
    
    @GetMapping("/unposted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getUnpostedEntries() {
        List<JournalEntry> entries = journalEntryService.getUnpostedEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Unposted entries retrieved successfully"));
    }
    
    @GetMapping("/reversed")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getReversedEntries() {
        List<JournalEntry> entries = journalEntryService.getReversedEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Reversed entries retrieved successfully"));
    }
    
    @GetMapping("/unbalanced")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getUnbalancedEntries() {
        List<JournalEntry> entries = journalEntryService.getUnbalancedEntries();
        return ResponseEntity.ok(ApiResponse.success(entries, "Unbalanced entries retrieved successfully"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getEntriesPendingApproval() {
        List<JournalEntry> entries = journalEntryService.getEntriesPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(entries, "Entries pending approval retrieved successfully"));
    }
    
    @GetMapping("/requiring-review")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getEntriesRequiringReview() {
        List<JournalEntry> entries = journalEntryService.getEntriesRequiringReview();
        return ResponseEntity.ok(ApiResponse.success(entries, "Entries requiring review retrieved successfully"));
    }
    
    @GetMapping("/fiscal-period")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getEntriesByFiscalPeriod(@RequestParam Integer year, @RequestParam Integer period) {
        List<JournalEntry> entries = journalEntryService.getEntriesByFiscalPeriod(year, period);
        return ResponseEntity.ok(ApiResponse.success(entries, "Fiscal period entries retrieved successfully"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getEntriesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<JournalEntry> entries = journalEntryService.getEntriesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(entries, "Date range entries retrieved successfully"));
    }
    
    // Journal Entry Line Operations
    @PostMapping("/{entryId}/lines")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntryLine>> addJournalEntryLine(@PathVariable Long entryId, @Valid @RequestBody JournalEntryLine line) {
        log.info("Adding line to journal entry: {}", entryId);
        JournalEntryLine added = journalEntryService.addJournalEntryLine(entryId, line);
        return ResponseEntity.ok(ApiResponse.success(added, "Journal entry line added successfully"));
    }
    
    @PutMapping("/{entryId}/lines/{lineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<JournalEntryLine>> updateJournalEntryLine(
        @PathVariable Long entryId,
        @PathVariable Long lineId,
        @Valid @RequestBody JournalEntryLine line
    ) {
        log.info("Updating line {} in journal entry: {}", lineId, entryId);
        JournalEntryLine updated = journalEntryService.updateJournalEntryLine(entryId, lineId, line);
        return ResponseEntity.ok(ApiResponse.success(updated, "Journal entry line updated successfully"));
    }
    
    @DeleteMapping("/{entryId}/lines/{lineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> deleteJournalEntryLine(@PathVariable Long entryId, @PathVariable Long lineId) {
        log.info("Deleting line {} from journal entry: {}", lineId, entryId);
        journalEntryService.deleteJournalEntryLine(entryId, lineId);
        return ResponseEntity.ok(ApiResponse.success(null, "Journal entry line deleted successfully"));
    }
    
    @GetMapping("/{entryId}/lines")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<JournalEntryLine>>> getJournalEntryLines(@PathVariable Long entryId) {
        List<JournalEntryLine> lines = journalEntryService.getJournalEntryLines(entryId);
        return ResponseEntity.ok(ApiResponse.success(lines, "Journal entry lines retrieved successfully"));
    }
    
    // Validation Operations
    @GetMapping("/{id}/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> validateJournalEntry(@PathVariable Long id) {
        boolean valid = journalEntryService.validateJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success(valid, "Journal entry validation completed"));
    }
    
    @GetMapping("/{id}/is-balanced")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Boolean>> isJournalEntryBalanced(@PathVariable Long id) {
        boolean balanced = journalEntryService.isJournalEntryBalanced(id);
        return ResponseEntity.ok(ApiResponse.success(balanced, "Balance check completed"));
    }
    
    @GetMapping("/{id}/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getJournalEntryBalance(@PathVariable Long id) {
        Map<String, Object> balance = journalEntryService.getJournalEntryBalance(id);
        return ResponseEntity.ok(ApiResponse.success(balance, "Journal entry balance retrieved successfully"));
    }
    
    @PutMapping("/{id}/recalculate-totals")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Void>> recalculateJournalEntryTotals(@PathVariable Long id) {
        log.info("Recalculating totals for journal entry: {}", id);
        journalEntryService.recalculateJournalEntryTotals(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Journal entry totals recalculated successfully"));
    }
    
    // Batch Operations
    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> createBulkJournalEntries(@Valid @RequestBody List<JournalEntryRequest> requests) {
        log.info("Creating {} journal entries in bulk", requests.size());
        List<JournalEntry> entries = journalEntryService.createBulkJournalEntries(requests);
        return ResponseEntity.ok(ApiResponse.success(entries, entries.size() + " journal entries created successfully"));
    }
    
    @PutMapping("/bulk/post")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Integer>> postBulkJournalEntries(@RequestBody List<Long> entryIds) {
        log.info("Posting {} journal entries in bulk", entryIds.size());
        int count = journalEntryService.postBulkJournalEntries(entryIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " journal entries posted successfully"));
    }
    
    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Integer>> deleteBulkJournalEntries(@RequestBody List<Long> entryIds) {
        log.info("Deleting {} journal entries in bulk", entryIds.size());
        int count = journalEntryService.deleteBulkJournalEntries(entryIds);
        return ResponseEntity.ok(ApiResponse.success(count, count + " journal entries deleted successfully"));
    }
    
    // Statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getJournalEntryStatistics() {
        Map<String, Object> statistics = journalEntryService.getJournalEntryStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Journal entry statistics retrieved successfully"));
    }
    
    @GetMapping("/statistics/type-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getEntryTypeDistribution() {
        List<Map<String, Object>> distribution = journalEntryService.getEntryTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Entry type distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/status-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStatusDistribution() {
        List<Map<String, Object>> distribution = journalEntryService.getStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Status distribution retrieved successfully"));
    }
    
    @GetMapping("/statistics/most-active-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMostActiveUsers() {
        List<Map<String, Object>> users = journalEntryService.getMostActiveUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Most active users retrieved successfully"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = journalEntryService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved successfully"));
    }
}
