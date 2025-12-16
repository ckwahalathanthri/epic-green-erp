package lk.epicgreen.erp.sales.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.sales.dto.InvoiceRequest;
import lk.epicgreen.erp.sales.entity.Invoice;
import lk.epicgreen.erp.sales.service.InvoiceService;
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
 * Invoice Controller
 * REST controller for invoice operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/sales/invoices")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class InvoiceController {
    
    private final InvoiceService invoiceService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Invoice>> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        Invoice created = invoiceService.createInvoice(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Invoice created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Invoice>> updateInvoice(
        @PathVariable Long id,
        @Valid @RequestBody InvoiceRequest request
    ) {
        Invoice updated = invoiceService.updateInvoice(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Invoice updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Invoice deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Invoice>> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ApiResponse.success(invoice, "Invoice retrieved successfully"));
    }
    
    @GetMapping("/number/{invoiceNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Invoice>> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        Invoice invoice = invoiceService.getInvoiceByNumber(invoiceNumber);
        return ResponseEntity.ok(ApiResponse.success(invoice, "Invoice retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Invoice>>> getAllInvoices(Pageable pageable) {
        Page<Invoice> invoices = invoiceService.getAllInvoices(pageable);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Invoices retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP', 'USER')")
    public ResponseEntity<ApiResponse<Page<Invoice>>> searchInvoices(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<Invoice> invoices = invoiceService.searchInvoices(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Invoice>> approveInvoice(
        @PathVariable Long id,
        @RequestParam Long approvedByUserId,
        @RequestParam(required = false) String approvalNotes
    ) {
        Invoice approved = invoiceService.approveInvoice(id, approvedByUserId, approvalNotes);
        return ResponseEntity.ok(ApiResponse.success(approved, "Invoice approved"));
    }
    
    @PostMapping("/{id}/send")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Invoice>> sendInvoice(@PathVariable Long id) {
        Invoice sent = invoiceService.sendInvoice(id);
        return ResponseEntity.ok(ApiResponse.success(sent, "Invoice sent"));
    }
    
    @PostMapping("/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Invoice>> markAsPaid(
        @PathVariable Long id,
        @RequestParam Double paidAmount
    ) {
        Invoice paid = invoiceService.markAsPaid(id, paidAmount);
        return ResponseEntity.ok(ApiResponse.success(paid, "Invoice marked as paid"));
    }
    
    @PostMapping("/{id}/record-payment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Invoice>> recordPartialPayment(
        @PathVariable Long id,
        @RequestParam Double paidAmount
    ) {
        Invoice updated = invoiceService.recordPartialPayment(id, paidAmount);
        return ResponseEntity.ok(ApiResponse.success(updated, "Payment recorded"));
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Invoice>> cancelInvoice(
        @PathVariable Long id,
        @RequestParam String cancellationReason
    ) {
        Invoice cancelled = invoiceService.cancelInvoice(id, cancellationReason);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Invoice cancelled"));
    }
    
    @GetMapping("/draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getDraftInvoices() {
        List<Invoice> invoices = invoiceService.getDraftInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Draft invoices retrieved"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getPendingInvoices() {
        List<Invoice> invoices = invoiceService.getPendingInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Pending invoices retrieved"));
    }
    
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getApprovedInvoices() {
        List<Invoice> invoices = invoiceService.getApprovedInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Approved invoices retrieved"));
    }
    
    @GetMapping("/sent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getSentInvoices() {
        List<Invoice> invoices = invoiceService.getSentInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Sent invoices retrieved"));
    }
    
    @GetMapping("/paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getPaidInvoices() {
        List<Invoice> invoices = invoiceService.getPaidInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Paid invoices retrieved"));
    }
    
    @GetMapping("/unpaid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getUnpaidInvoices() {
        List<Invoice> invoices = invoiceService.getUnpaidInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Unpaid invoices retrieved"));
    }
    
    @GetMapping("/partially-paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getPartiallyPaidInvoices() {
        List<Invoice> invoices = invoiceService.getPartiallyPaidInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Partially paid invoices retrieved"));
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getOverdueInvoices() {
        List<Invoice> invoices = invoiceService.getOverdueInvoices();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Overdue invoices retrieved"));
    }
    
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getInvoicesPendingApproval() {
        List<Invoice> invoices = invoiceService.getInvoicesPendingApproval();
        return ResponseEntity.ok(ApiResponse.success(invoices, "Invoices pending approval retrieved"));
    }
    
    @GetMapping("/due-soon")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getInvoicesDueSoon(
        @RequestParam(defaultValue = "7") int days
    ) {
        List<Invoice> invoices = invoiceService.getInvoicesDueSoon(days);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Invoices due soon retrieved"));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Page<Invoice>>> getInvoicesByCustomer(
        @PathVariable Long customerId,
        Pageable pageable
    ) {
        Page<Invoice> invoices = invoiceService.getInvoicesByCustomer(customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Customer invoices retrieved"));
    }
    
    @GetMapping("/customer/{customerId}/outstanding")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getCustomerOutstandingInvoices(@PathVariable Long customerId) {
        List<Invoice> invoices = invoiceService.getCustomerOutstandingInvoices(customerId);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Customer outstanding invoices retrieved"));
    }
    
    @GetMapping("/customer/{customerId}/outstanding-balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<Double>> getOutstandingBalance(@PathVariable Long customerId) {
        Double balance = invoiceService.getOutstandingBalance(customerId);
        return ResponseEntity.ok(ApiResponse.success(balance, "Outstanding balance retrieved"));
    }
    
    @GetMapping("/sales-rep/{salesRepId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getInvoicesBySalesRep(@PathVariable Long salesRepId) {
        List<Invoice> invoices = invoiceService.getInvoicesBySalesRep(salesRepId);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Sales rep invoices retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getInvoicesByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Invoice> invoices = invoiceService.getInvoicesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Date range invoices retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT', 'SALES_REP')")
    public ResponseEntity<ApiResponse<List<Invoice>>> getRecentInvoices(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<Invoice> invoices = invoiceService.getRecentInvoices(limit);
        return ResponseEntity.ok(ApiResponse.success(invoices, "Recent invoices retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = invoiceService.getInvoiceStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ACCOUNTANT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = invoiceService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
