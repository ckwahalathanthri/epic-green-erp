package lk.epicgreen.erp.returns.service;

import lk.epicgreen.erp.returns.dto.SalesReturnRequest;
import lk.epicgreen.erp.returns.entity.SalesReturn;
import lk.epicgreen.erp.returns.entity.SalesReturnLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * SalesReturn Service Interface
 * Service for sales return operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SalesReturnService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    /**
     * Create sales return
     */
    SalesReturn createSalesReturn(SalesReturnRequest request);
    
    /**
     * Update sales return
     */
    SalesReturn updateSalesReturn(Long id, SalesReturnRequest request);
    
    /**
     * Delete sales return
     */
    void deleteSalesReturn(Long id);
    
    /**
     * Get sales return by ID
     */
    SalesReturn getSalesReturnById(Long id);
    
    /**
     * Get sales return by return number
     */
    SalesReturn getSalesReturnByNumber(String returnNumber);
    
    /**
     * Get all sales returns
     */
    List<SalesReturn> getAllSalesReturns();
    
    /**
     * Get all sales returns with pagination
     */
    Page<SalesReturn> getAllSalesReturns(Pageable pageable);
    
    /**
     * Search sales returns
     */
    Page<SalesReturn> searchSalesReturns(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    /**
     * Approve sales return
     */
    SalesReturn approveSalesReturn(Long id, Long approvedBy, String approvalNotes);
    
    /**
     * Reject sales return
     */
    SalesReturn rejectSalesReturn(Long id, String rejectionReason);
    
    /**
     * Complete sales return
     */
    SalesReturn completeSalesReturn(Long id);
    
    /**
     * Cancel sales return
     */
    SalesReturn cancelSalesReturn(Long id, String cancellationReason);
    
    /**
     * Submit for approval
     */
    SalesReturn submitForApproval(Long id);
    
    // ===================================================================
    // INSPECTION OPERATIONS
    // ===================================================================
    
    /**
     * Perform quality inspection
     */
    SalesReturn performQualityInspection(Long id, Long inspectedBy, String inspectionResult, String inspectionNotes);
    
    /**
     * Pass inspection
     */
    SalesReturn passInspection(Long id, Long inspectedBy);
    
    /**
     * Fail inspection
     */
    SalesReturn failInspection(Long id, Long inspectedBy, String failureReason);
    
    /**
     * Get returns pending inspection
     */
    List<SalesReturn> getReturnsPendingInspection();
    
    // ===================================================================
    // CREDIT NOTE OPERATIONS
    // ===================================================================
    
    /**
     * Generate credit note
     */
    SalesReturn generateCreditNote(Long id);
    
    /**
     * Get returns without credit note
     */
    List<SalesReturn> getReturnsWithoutCreditNote();
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    /**
     * Get pending returns
     */
    List<SalesReturn> getPendingReturns();
    
    /**
     * Get approved returns
     */
    List<SalesReturn> getApprovedReturns();
    
    /**
     * Get rejected returns
     */
    List<SalesReturn> getRejectedReturns();
    
    /**
     * Get completed returns
     */
    List<SalesReturn> getCompletedReturns();
    
    /**
     * Get returns pending approval
     */
    List<SalesReturn> getReturnsPendingApproval();
    
    /**
     * Get returns requiring quality check
     */
    List<SalesReturn> getReturnsRequiringQualityCheck();
    
    /**
     * Get returns by customer
     */
    List<SalesReturn> getReturnsByCustomer(Long customerId);
    
    /**
     * Get returns by customer with pagination
     */
    Page<SalesReturn> getReturnsByCustomer(Long customerId, Pageable pageable);
    
    /**
     * Get returns by sales order
     */
    List<SalesReturn> getReturnsBySalesOrder(Long salesOrderId);
    
    /**
     * Get returns by invoice
     */
    List<SalesReturn> getReturnsByInvoice(Long invoiceId);
    
    /**
     * Get returns by date range
     */
    List<SalesReturn> getReturnsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get returns by warehouse
     */
    List<SalesReturn> getReturnsByWarehouse(Long warehouseId);
    
    /**
     * Get recent returns
     */
    List<SalesReturn> getRecentReturns(int limit);
    
    /**
     * Get customer recent returns
     */
    List<SalesReturn> getCustomerRecentReturns(Long customerId, int limit);
    
    // ===================================================================
    // SALES RETURN LINE OPERATIONS
    // ===================================================================
    
    /**
     * Add sales return line
     */
    SalesReturnLine addSalesReturnLine(Long returnId, SalesReturnLine line);
    
    /**
     * Update sales return line
     */
    SalesReturnLine updateSalesReturnLine(Long lineId, SalesReturnLine line);
    
    /**
     * Delete sales return line
     */
    void deleteSalesReturnLine(Long lineId);
    
    /**
     * Get sales return lines
     */
    List<SalesReturnLine> getSalesReturnLines(Long returnId);
    
    // ===================================================================
    // INVENTORY OPERATIONS
    // ===================================================================
    
    /**
     * Process inventory adjustment
     */
    void processInventoryAdjustment(Long returnId);
    
    /**
     * Reverse inventory adjustment
     */
    void reverseInventoryAdjustment(Long returnId);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    /**
     * Validate sales return
     */
    boolean validateSalesReturn(SalesReturn salesReturn);
    
    /**
     * Can approve return
     */
    boolean canApproveReturn(Long returnId);
    
    /**
     * Can reject return
     */
    boolean canRejectReturn(Long returnId);
    
    /**
     * Can generate credit note
     */
    boolean canGenerateCreditNote(Long returnId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    /**
     * Calculate return totals
     */
    void calculateReturnTotals(Long returnId);
    
    /**
     * Recalculate return amount
     */
    Double recalculateReturnAmount(Long returnId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    /**
     * Create bulk sales returns
     */
    List<SalesReturn> createBulkSalesReturns(List<SalesReturnRequest> requests);
    
    /**
     * Approve bulk sales returns
     */
    int approveBulkSalesReturns(List<Long> returnIds, Long approvedBy);
    
    /**
     * Delete bulk sales returns
     */
    int deleteBulkSalesReturns(List<Long> returnIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Get sales return statistics
     */
    Map<String, Object> getSalesReturnStatistics();
    
    /**
     * Get return type distribution
     */
    List<Map<String, Object>> getReturnTypeDistribution();
    
    /**
     * Get status distribution
     */
    List<Map<String, Object>> getStatusDistribution();
    
    /**
     * Get return reason distribution
     */
    List<Map<String, Object>> getReturnReasonDistribution();
    
    /**
     * Get monthly return count
     */
    List<Map<String, Object>> getMonthlyReturnCount(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get return rate by customer
     */
    List<Map<String, Object>> getReturnRateByCustomer(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get total return value
     */
    Double getTotalReturnValue();
    
    /**
     * Get average return amount
     */
    Double getAverageReturnAmount();
    
    /**
     * Get dashboard statistics
     */
    Map<String, Object> getDashboardStatistics();
}
