package lk.epicgreen.erp.returns.service;

import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.returns.dto.SalesReturnRequest;
import lk.epicgreen.erp.returns.entity.CreditNote;
import lk.epicgreen.erp.returns.entity.SalesReturn;
import lk.epicgreen.erp.returns.entity.SalesReturnLine;
import lk.epicgreen.erp.returns.repository.CreditNoteRepository;
import lk.epicgreen.erp.returns.repository.SalesReturnRepository;
import lk.epicgreen.erp.sales.entity.Invoice;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.sales.repository.InvoiceRepository;
import lk.epicgreen.erp.sales.repository.SalesOrderRepository;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SalesReturn Service Implementation
 * Implementation of sales return service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalesReturnServiceImpl implements SalesReturnService {
    
    private final SalesReturnRepository salesReturnRepository;
    private final CreditNoteRepository creditNoteRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final WarehouseRepository warehouseRepository;
    private final SalesOrderRepository salesOrderRepository;

    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================

    @Override
    public SalesReturn createSalesReturn(SalesReturnRequest request) {
        log.info("Creating sales return for customer: {}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + request.getCustomerId()));


        SalesReturn salesReturn = new SalesReturn();
        salesReturn.setReturnNumber(generateReturnNumber());

        // Set customer - both relationship AND denormalized fields
        salesReturn.setCustomer(customer);
        salesReturn.setCustomerName(customer.getCustomerName()); // Denormalized

        // Set sales order if provided
        if (request.getSalesOrderId() != null) {
            SalesOrder salesOrder = salesOrderRepository.findById(request.getSalesOrderId())
                    .orElseThrow(() -> new RuntimeException("Sales order not found"));
            salesReturn.setSalesOrder(salesOrder);
            salesReturn.setSalesOrderNumber(salesOrder.getOrderNumber()); // Denormalized
        }

        // Set invoice if provided
        if (request.getInvoiceId() != null) {
            Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));
            salesReturn.setInvoice(invoice);
            salesReturn.setInvoiceNumber(invoice.getInvoiceNumber()); // Denormalized
        }

        // Set warehouse
        if (request.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            salesReturn.setWarehouse(warehouse);
            salesReturn.setWarehouseName(warehouse.getWarehouseName()); // Denormalized
        }

        // Set basic info
        salesReturn.setReturnDate(request.getReturnDate() != null ? request.getReturnDate() : LocalDate.now());
        salesReturn.setReturnType(request.getReturnType());
        salesReturn.setReturnReason(request.getReturnReason());
        salesReturn.setReturnReasonDescription(request.getReasonDescription());

        // Set status flags - correct field names
        salesReturn.setStatus("PENDING");
        salesReturn.setIsApproved(false);
        salesReturn.setCreditNoteGenerated(false);
        salesReturn.setIsInspected(false);

        // Quality inspection - use correct field name
        salesReturn.setQualityInspectionRequired(true); // Default to true
        salesReturn.setQualityInspectionStatus("PENDING");

        salesReturn.setNotes(request.getNotes());

        // Set financial fields - use BigDecimal and calculate from refundAmount or set defaults
        BigDecimal refundAmount = request.getRefundAmount() != null ?
                BigDecimal.valueOf(request.getRefundAmount()) : BigDecimal.ZERO;

        // For sales returns, typically the totalAmount equals refundAmount
        // You can calculate tax/discount separately if needed
        salesReturn.setSubtotal(refundAmount);
        salesReturn.setTaxAmount(BigDecimal.ZERO);
        salesReturn.setDiscountAmount(BigDecimal.ZERO);
        salesReturn.setTotalAmount(refundAmount);

        return salesReturnRepository.save(salesReturn);
    }

    @Override
    public SalesReturn updateSalesReturn(Long id, SalesReturnRequest request) {
        log.info("Updating sales return: {}", id);

        SalesReturn existing = getSalesReturnById(id);

        // Only allow updates if not approved
        if (existing.getIsApproved()) {
            throw new RuntimeException("Cannot update approved sales return");
        }

        existing.setReturnDate(request.getReturnDate());
        existing.setReturnType(request.getReturnType());
        existing.setReturnReason(request.getReturnReason());
        existing.setReturnReasonDescription(request.getReasonDescription());

        if (request.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            existing.setWarehouse(warehouse);
        }

        // Update amounts
        BigDecimal refundAmount = request.getRefundAmount() != null ?
                BigDecimal.valueOf(request.getRefundAmount()) : BigDecimal.ZERO;

        existing.setSubtotal(refundAmount);
        existing.setTaxAmount(BigDecimal.ZERO);
        existing.setDiscountAmount(BigDecimal.ZERO);
        existing.setTotalAmount(refundAmount);
        existing.setNotes(request.getNotes());

        return salesReturnRepository.save(existing);
    }

    @Override
    public void deleteSalesReturn(Long id) {
        log.info("Deleting sales return: {}", id);

        SalesReturn salesReturn = getSalesReturnById(id);

        // Prevent deletion if approved or credit note generated
        if (salesReturn.getIsApproved()) {
            throw new RuntimeException("Cannot delete approved sales return");
        }

        if (salesReturn.getCreditNoteGenerated()) {
            throw new RuntimeException("Cannot delete sales return with generated credit note");
        }

        salesReturnRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReturn getSalesReturnById(Long id) {
        return salesReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales return not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public SalesReturn getSalesReturnByNumber(String returnNumber) {
        return salesReturnRepository.findByReturnNumber(returnNumber)
                .orElseThrow(() -> new RuntimeException("Sales return not found with number: " + returnNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getAllSalesReturns() {
        return salesReturnRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalesReturn> getAllSalesReturns(Pageable pageable) {
        return salesReturnRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalesReturn> searchSalesReturns(String searchTerm, Pageable pageable) {
        return salesReturnRepository.searchSalesReturns(searchTerm, pageable);
    }

    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================

    @Override
    public SalesReturn approveSalesReturn(Long id, Long approvedByUserId, String notes) {
        log.info("Approving sales return: {}", id);

        SalesReturn salesReturn = getSalesReturnById(id);

        if (salesReturn.getIsApproved()) {
            throw new RuntimeException("Sales return already approved");
        }

        salesReturn.setIsApproved(true);
        salesReturn.setStatus("APPROVED");
        salesReturn.setApprovalDate(LocalDate.now());
        salesReturn.setApprovedBy(String.valueOf(approvedByUserId));

        if (notes != null) {
            String currentNotes = salesReturn.getNotes();
            salesReturn.setNotes(currentNotes != null ? currentNotes + "\n" + notes : notes);
        }

        return salesReturnRepository.save(salesReturn);
    }

    @Override
    public SalesReturn rejectSalesReturn(Long id, String reason) {
        log.info("Rejecting sales return: {}", id);

        SalesReturn salesReturn = getSalesReturnById(id);

        salesReturn.setStatus("REJECTED");
        // Store rejection reason in notes or add a rejectionReason field
        String currentNotes = salesReturn.getNotes();
        String rejectionNote = "REJECTED: " + reason;
        salesReturn.setNotes(currentNotes != null ? currentNotes + "\n" + rejectionNote : rejectionNote);

        return salesReturnRepository.save(salesReturn);
    }

    @Override
    public SalesReturn completeSalesReturn(Long id) {
        log.info("Completing sales return: {}", id);

        SalesReturn salesReturn = getSalesReturnById(id);

        if (!salesReturn.getIsApproved()) {
            throw new RuntimeException("Sales return must be approved before completion");
        }

        salesReturn.setStatus("COMPLETED");

        return salesReturnRepository.save(salesReturn);
    }

    @Override
    public SalesReturn cancelSalesReturn(Long id, String cancellationReason) {
        log.info("Cancelling sales return: {}", id);

        SalesReturn salesReturn = getSalesReturnById(id);

        salesReturn.setStatus("CANCELLED");
        String currentNotes = salesReturn.getNotes();
        String cancellationNote = "CANCELLED: " + cancellationReason;
        salesReturn.setNotes(currentNotes != null ? currentNotes + "\n" + cancellationNote : cancellationNote);

        return salesReturnRepository.save(salesReturn);
    }

    @Override
    public SalesReturn submitForApproval(Long id) {
        log.info("Submitting sales return for approval: {}", id);

        SalesReturn salesReturn = getSalesReturnById(id);

        if (salesReturn.getIsApproved()) {
            throw new RuntimeException("Sales return already approved");
        }

        salesReturn.setStatus("PENDING_APPROVAL");

        return salesReturnRepository.save(salesReturn);
    }

    // ===================================================================
    // INSPECTION OPERATIONS
    // ===================================================================

    @Override
    public SalesReturn performQualityInspection(Long id, Long inspectedBy, String inspectionResult, String inspectionNotes) {
        log.info("Inspecting sales return: {}", id);

        SalesReturn salesReturn = getSalesReturnById(id);

        salesReturn.setIsInspected(true);
        salesReturn.setQualityInspectionStatus(inspectionResult);
        salesReturn.setQualityInspectionDate(LocalDate.now());
        salesReturn.setQualityInspector(String.valueOf(inspectedBy));
        salesReturn.setQualityRemarks(inspectionNotes);

        return salesReturnRepository.save(salesReturn);
    }

    @Override
    public SalesReturn passInspection(Long id, Long inspectedBy) {
        return performQualityInspection(id, inspectedBy, "PASSED", "Quality inspection passed");
    }

    @Override
    public SalesReturn failInspection(Long id, Long inspectedBy, String failureReason) {
        return performQualityInspection(id, inspectedBy, "FAILED", failureReason);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsPendingInspection() {
        return salesReturnRepository.findReturnsPendingInspection();
    }

    // ===================================================================
    // CREDIT NOTE OPERATIONS
    // ===================================================================

    @Override
    public SalesReturn generateCreditNote(Long salesReturnId) {
        log.info("Generating credit note for sales return: {}", salesReturnId);

        SalesReturn salesReturn = getSalesReturnById(salesReturnId);

        if (!salesReturn.getIsApproved()) {
            throw new RuntimeException("Cannot generate credit note for unapproved sales return");
        }

        if (salesReturn.getCreditNoteGenerated()) {
            throw new RuntimeException("Credit note already generated for this sales return");
        }

        // Create credit note
        CreditNote creditNote = new CreditNote();
        creditNote.setCreditNoteNumber(generateCreditNoteNumber());
        creditNote.setSalesReturn(salesReturn);
        creditNote.setCustomer(salesReturn.getCustomer());
        creditNote.setCreditNoteDate(LocalDate.now());
        creditNote.setCreditAmount(salesReturn.getTotalAmount());
        creditNote.setRemainingCreditAmount(salesReturn.getTotalAmount());
        creditNote.setStatus("PENDING");
        creditNote.setIsApproved(false);
        creditNote.setIsApplied(false);
        creditNote.setIsRefunded(false);
        creditNote.setReason(salesReturn.getReturnReason());

        CreditNote savedCreditNote = creditNoteRepository.save(creditNote);

        // Update sales return
        salesReturn.setCreditNoteGenerated(true);
        salesReturn.setCreditNoteId(savedCreditNote.getId());

        return salesReturnRepository.save(salesReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsWithoutCreditNote() {
        return salesReturnRepository.findReturnsWithoutCreditNote();
    }

    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getPendingReturns() {
        return salesReturnRepository.findPendingReturns();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getApprovedReturns() {
        return salesReturnRepository.findApprovedReturns();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getRejectedReturns() {
        return salesReturnRepository.findRejectedReturns();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getCompletedReturns() {
        return salesReturnRepository.findCompletedReturns();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsPendingApproval() {
        return salesReturnRepository.findReturnsPendingApproval();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsRequiringQualityCheck() {
        return salesReturnRepository.findReturnsRequiringQualityCheck();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsByCustomer(Long customerId) {
        return salesReturnRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalesReturn> getReturnsByCustomer(Long customerId, Pageable pageable) {
        return salesReturnRepository.findByCustomerId(customerId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsBySalesOrder(Long salesOrderId) {
        return salesReturnRepository.findBySalesOrderId(salesOrderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsByInvoice(Long invoiceId) {
        return salesReturnRepository.findByInvoiceId(invoiceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesReturnRepository.findByReturnDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getReturnsByWarehouse(Long warehouseId) {
        return salesReturnRepository.findByWarehouseId(warehouseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getRecentReturns(int limit) {
        return salesReturnRepository.findRecentReturns(PageRequest.of(0, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getCustomerRecentReturns(Long customerId, int limit) {
        return salesReturnRepository.findCustomerRecentReturns(customerId, PageRequest.of(0, limit));
    }

    // ===================================================================
    // SALES RETURN LINE OPERATIONS
    // ===================================================================

    @Override
    public SalesReturnLine addSalesReturnLine(Long returnId, SalesReturnLine line) {
        // Implementation depends on your SalesReturnLine entity structure
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public SalesReturnLine updateSalesReturnLine(Long lineId, SalesReturnLine line) {
        // Implementation depends on your SalesReturnLine entity structure
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void deleteSalesReturnLine(Long lineId) {
        // Implementation depends on your SalesReturnLine entity structure
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesReturnLine> getSalesReturnLines(Long returnId) {
        // Implementation depends on your SalesReturnLine entity structure
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // ===================================================================
    // INVENTORY OPERATIONS
    // ===================================================================

    @Override
    public void processInventoryAdjustment(Long returnId) {
        // Implementation for inventory adjustment
        log.info("Processing inventory adjustment for return: {}", returnId);
        // Add your inventory adjustment logic here
    }

    @Override
    public void reverseInventoryAdjustment(Long returnId) {
        // Implementation for reversing inventory adjustment
        log.info("Reversing inventory adjustment for return: {}", returnId);
        // Add your reverse inventory adjustment logic here
    }

    // ===================================================================
    // VALIDATION
    // ===================================================================

    @Override
    @Transactional(readOnly = true)
    public boolean validateSalesReturn(SalesReturn salesReturn) {
        return salesReturn.getCustomer() != null &&
                salesReturn.getReturnDate() != null &&
                salesReturn.getTotalAmount() != null &&
                salesReturn.getTotalAmount().compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canApproveReturn(Long returnId) {
        SalesReturn salesReturn = getSalesReturnById(returnId);
        return !salesReturn.getIsApproved() &&
                "PENDING_APPROVAL".equals(salesReturn.getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canRejectReturn(Long returnId) {
        SalesReturn salesReturn = getSalesReturnById(returnId);
        return !salesReturn.getIsApproved() &&
                ("PENDING".equals(salesReturn.getStatus()) || "PENDING_APPROVAL".equals(salesReturn.getStatus()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canGenerateCreditNote(Long returnId) {
        SalesReturn salesReturn = getSalesReturnById(returnId);
        return salesReturn.getIsApproved() && !salesReturn.getCreditNoteGenerated();
    }

    // ===================================================================
    // CALCULATIONS
    // ===================================================================

    @Override
    public void calculateReturnTotals(Long returnId) {
        SalesReturn salesReturn = getSalesReturnById(returnId);
        salesReturn.calculateTotals();
        salesReturnRepository.save(salesReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public Double recalculateReturnAmount(Long returnId) {
        SalesReturn salesReturn = getSalesReturnById(returnId);
        return salesReturn.getTotalAmount() != null ? salesReturn.getTotalAmount().doubleValue() : 0.0;
    }

    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================

    @Override
    public List<SalesReturn> createBulkSalesReturns(List<SalesReturnRequest> requests) {
        log.info("Creating bulk sales returns: {} items", requests.size());

        List<SalesReturn> createdReturns = new ArrayList<>();
        for (SalesReturnRequest request : requests) {
            try {
                SalesReturn salesReturn = createSalesReturn(request);
                createdReturns.add(salesReturn);
            } catch (Exception e) {
                log.error("Error creating sales return for customer {}: {}",
                        request.getCustomerId(), e.getMessage());
            }
        }

        return createdReturns;
    }

    @Override
    public int approveBulkSalesReturns(List<Long> returnIds, Long approvedBy) {
        int count = 0;
        for (Long id : returnIds) {
            try {
                approveSalesReturn(id, approvedBy, null);
                count++;
            } catch (Exception e) {
                log.error("Error approving return: {}", id, e);
            }
        }
        return count;
    }

    @Override
    public int deleteBulkSalesReturns(List<Long> returnIds) {
        int count = 0;
        for (Long id : returnIds) {
            try {
                deleteSalesReturn(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting return: {}", id, e);
            }
        }
        return count;
    }

    // ===================================================================
    // STATISTICS
    // ===================================================================

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSalesReturnStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", salesReturnRepository.count());
        stats.put("pending", salesReturnRepository.countPendingReturns());
        stats.put("approved", salesReturnRepository.countApprovedReturns());
        stats.put("rejected", salesReturnRepository.findRejectedReturns().size());
        stats.put("completed", salesReturnRepository.findCompletedReturns().size());
        stats.put("pendingApproval", salesReturnRepository.countReturnsPendingApproval());
        stats.put("withoutCreditNote", salesReturnRepository.countReturnsWithoutCreditNote());
        stats.put("totalReturnValue", getTotalReturnValue());
        stats.put("averageReturnAmount", getAverageReturnAmount());
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getReturnTypeDistribution() {
        List<Object[]> results = salesReturnRepository.getReturnTypeDistribution();
        return convertToMapList(results, "returnType", "returnCount");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStatusDistribution() {
        List<Object[]> results = salesReturnRepository.getStatusDistribution();
        return convertToMapList(results, "status", "returnCount");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getReturnReasonDistribution() {
        List<Object[]> results = salesReturnRepository.getReturnReasonDistribution();
        return convertToMapList(results, "returnReason", "returnCount");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyReturnCount(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = salesReturnRepository.getMonthlyReturnCount(startDate, endDate);
        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("year", result[0]);
                    map.put("month", result[1]);
                    map.put("returnCount", result[2]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getReturnRateByCustomer(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = salesReturnRepository.getReturnRateByCustomer(startDate, endDate);
        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("customerId", result[0]);
                    map.put("customerName", result[1]);
                    map.put("returnCount", result[2]);
                    map.put("totalAmount", result[3]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalReturnValue() {
        Double total = salesReturnRepository.getTotalReturnValue();
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageReturnAmount() {
        Double average = salesReturnRepository.getAverageReturnAmount();
        return average != null ? average : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("statistics", getSalesReturnStatistics());
        dashboard.put("typeDistribution", getReturnTypeDistribution());
        dashboard.put("statusDistribution", getStatusDistribution());
        dashboard.put("reasonDistribution", getReturnReasonDistribution());

        return dashboard;
    }

    // ===================================================================
    // HELPER METHODS
    // ===================================================================

    private String generateReturnNumber() {
        String prefix = "SR";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "-" + timestamp.substring(timestamp.length() - 10);
    }

    private String generateCreditNoteNumber() {
        String prefix = "CN";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "-" + timestamp.substring(timestamp.length() - 10);
    }

    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put(key1, result[0]);
                    map.put(key2, result[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }
}
