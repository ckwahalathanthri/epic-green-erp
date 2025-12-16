package lk.epicgreen.erp.sales.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.sales.dto.DispatchNoteRequest;
import lk.epicgreen.erp.sales.entity.DispatchNote;
import lk.epicgreen.erp.sales.service.DispatchService;
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
 * Dispatch Controller
 * REST controller for dispatch note operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/sales/dispatch")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class DispatchController {
    
    private final DispatchService dispatchService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<DispatchNote>> createDispatchNote(@Valid @RequestBody DispatchNoteRequest request) {
        DispatchNote created = dispatchService.createDispatchNote(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Dispatch note created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<DispatchNote>> updateDispatchNote(
        @PathVariable Long id,
        @Valid @RequestBody DispatchNoteRequest request
    ) {
        DispatchNote updated = dispatchService.updateDispatchNote(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Dispatch note updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteDispatchNote(@PathVariable Long id) {
        dispatchService.deleteDispatchNote(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Dispatch note deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER', 'USER')")
    public ResponseEntity<ApiResponse<DispatchNote>> getDispatchNoteById(@PathVariable Long id) {
        DispatchNote dispatchNote = dispatchService.getDispatchNoteById(id);
        return ResponseEntity.ok(ApiResponse.success(dispatchNote, "Dispatch note retrieved successfully"));
    }
    
    @GetMapping("/number/{dispatchNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER', 'USER')")
    public ResponseEntity<ApiResponse<DispatchNote>> getDispatchNoteByNumber(@PathVariable String dispatchNumber) {
        DispatchNote dispatchNote = dispatchService.getDispatchNoteByNumber(dispatchNumber);
        return ResponseEntity.ok(ApiResponse.success(dispatchNote, "Dispatch note retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER', 'USER')")
    public ResponseEntity<ApiResponse<Page<DispatchNote>>> getAllDispatchNotes(Pageable pageable) {
        Page<DispatchNote> dispatchNotes = dispatchService.getAllDispatchNotes(pageable);
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Dispatch notes retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER', 'USER')")
    public ResponseEntity<ApiResponse<Page<DispatchNote>>> searchDispatchNotes(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<DispatchNote> dispatchNotes = dispatchService.searchDispatchNotes(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/prepare")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<DispatchNote>> prepareDispatchNote(@PathVariable Long id) {
        DispatchNote prepared = dispatchService.prepareDispatchNote(id);
        return ResponseEntity.ok(ApiResponse.success(prepared, "Dispatch note preparation started"));
    }
    
    @PostMapping("/{id}/mark-ready")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<DispatchNote>> markAsReady(@PathVariable Long id) {
        DispatchNote ready = dispatchService.markAsReady(id);
        return ResponseEntity.ok(ApiResponse.success(ready, "Dispatch note marked as ready"));
    }
    
    @PostMapping("/{id}/dispatch")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<DispatchNote>> dispatchNote(
        @PathVariable Long id,
        @RequestParam Long driverId,
        @RequestParam Long vehicleId
    ) {
        DispatchNote dispatched = dispatchService.dispatchNote(id, driverId, vehicleId);
        return ResponseEntity.ok(ApiResponse.success(dispatched, "Dispatch note dispatched"));
    }
    
    @PostMapping("/{id}/mark-delivered")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER')")
    public ResponseEntity<ApiResponse<DispatchNote>> markAsDelivered(
        @PathVariable Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveredDate,
        @RequestParam Long deliveredByUserId
    ) {
        DispatchNote delivered = dispatchService.markAsDelivered(id, deliveredDate, deliveredByUserId);
        return ResponseEntity.ok(ApiResponse.success(delivered, "Dispatch note marked as delivered"));
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<DispatchNote>> cancelDispatchNote(
        @PathVariable Long id,
        @RequestParam String cancellationReason
    ) {
        DispatchNote cancelled = dispatchService.cancelDispatchNote(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Dispatch note cancelled"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getPendingDispatchNotes() {
        List<DispatchNote> dispatchNotes = dispatchService.getPendingDispatchNotes();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Pending dispatch notes retrieved"));
    }
    
    @GetMapping("/preparing")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getPreparingDispatchNotes() {
        List<DispatchNote> dispatchNotes = dispatchService.getPreparingDispatchNotes();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Preparing dispatch notes retrieved"));
    }
    
    @GetMapping("/ready")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getReadyDispatchNotes() {
        List<DispatchNote> dispatchNotes = dispatchService.getReadyDispatchNotes();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Ready dispatch notes retrieved"));
    }
    
    @GetMapping("/dispatched")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getDispatchedNotes() {
        List<DispatchNote> dispatchNotes = dispatchService.getDispatchedNotes();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Dispatched notes retrieved"));
    }
    
    @GetMapping("/delivered")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getDeliveredDispatchNotes() {
        List<DispatchNote> dispatchNotes = dispatchService.getDeliveredDispatchNotes();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Delivered dispatch notes retrieved"));
    }
    
    @GetMapping("/undelivered")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getUndeliveredDispatchNotes() {
        List<DispatchNote> dispatchNotes = dispatchService.getUndeliveredDispatchNotes();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Undelivered dispatch notes retrieved"));
    }
    
    @GetMapping("/partial-deliveries")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getPartialDeliveries() {
        List<DispatchNote> dispatchNotes = dispatchService.getPartialDeliveries();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Partial deliveries retrieved"));
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getOverdueDeliveries() {
        List<DispatchNote> dispatchNotes = dispatchService.getOverdueDeliveries();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Overdue deliveries retrieved"));
    }
    
    @GetMapping("/todays-deliveries")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getTodaysDeliveries() {
        List<DispatchNote> dispatchNotes = dispatchService.getTodaysDeliveries();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Today's deliveries retrieved"));
    }
    
    @GetMapping("/requiring-action")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getDispatchNotesRequiringAction() {
        List<DispatchNote> dispatchNotes = dispatchService.getDispatchNotesRequiringAction();
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Dispatch notes requiring action retrieved"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<DispatchNote>>> getDispatchNotesByCustomer(
        @PathVariable Long customerId,
        Pageable pageable
    ) {
        Page<DispatchNote> dispatchNotes = dispatchService.getDispatchNotesByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Customer dispatch notes retrieved"));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getDispatchNotesByWarehouse(@PathVariable Long warehouseId) {
        List<DispatchNote> dispatchNotes = dispatchService.getDispatchNotesByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Warehouse dispatch notes retrieved"));
    }
    
    @GetMapping("/driver/{driverId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getDispatchNotesByDriver(@PathVariable Long driverId) {
        List<DispatchNote> dispatchNotes = dispatchService.getDispatchNotesByDriver(driverId);
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Driver dispatch notes retrieved"));
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getDispatchNotesByVehicle(@PathVariable Long vehicleId) {
        List<DispatchNote> dispatchNotes = dispatchService.getDispatchNotesByVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Vehicle dispatch notes retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getDispatchNotesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<DispatchNote> dispatchNotes = dispatchService.getDispatchNotesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Date range dispatch notes retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER', 'DRIVER')")
    public ResponseEntity<ApiResponse<List<DispatchNote>>> getRecentDispatchNotes(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<DispatchNote> dispatchNotes = dispatchService.getRecentDispatchNotes(limit);
        return ResponseEntity.ok(ApiResponse.success(dispatchNotes, "Recent dispatch notes retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = dispatchService.getDispatchStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = dispatchService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
