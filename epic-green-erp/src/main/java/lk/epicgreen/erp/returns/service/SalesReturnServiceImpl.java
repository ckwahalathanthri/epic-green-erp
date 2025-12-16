package lk.epicgreen.erp.returns.service;

import lk.epicgreen.erp.returns.dto.SalesReturnRequest;
import lk.epicgreen.erp.returns.entity.CreditNote;
import lk.epicgreen.erp.returns.entity.SalesReturn;
import lk.epicgreen.erp.returns.entity.SalesReturnLine;
import lk.epicgreen.erp.returns.repository.CreditNoteRepository;
import lk.epicgreen.erp.returns.repository.SalesReturnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @Override
    public SalesReturn createSalesReturn(SalesReturnRequest request) {
        log.info("Creating sales return for customer: {}", request.getCustomerId());
        
        SalesReturn salesReturn = new SalesReturn();
        salesReturn.setReturnNumber(generateReturnNumber());
        
        // Set customer info - use denormalized fields if they exist
        if (request.getCustomerId() != null) {
            salesReturn.setCustomerId(request.getCustomerId());
        }
        if (request.getCustomerName() != null) {
            salesReturn.setCustomerName(request.getCustomerName());
        }
        
        // Set sales order and invoice IDs
        if (request.getSalesOrderId() != null) {
            salesReturn.setSalesOrderId(request.getSalesOrderId());
        }
        if (request.getInvoiceId() != null) {
            salesReturn.setInvoiceId(request.getInvoiceId());
        }
        
        // Set basic info
        salesReturn.setReturnDate(request.getReturnDate() != null ? request.getReturnDate() : LocalDate.now());
        salesReturn.setReturnType(request.getReturnType());
        salesReturn.setReturnReason(request.getReturnReason());
        
        // Set warehouse - may need to be object reference instead of ID
        if (request.getWarehouseId() != null) {
            salesReturn.setWarehouseId(request.getWarehouseId());
        }
        
        // Set status flags - correct field names without "Is" prefix
        salesReturn.setStatus("PENDING");
        salesReturn.setApproved(false);
        salesReturn.setCreditNoteGenerated(false);
        salesReturn.setInspected(false);
        
        // Quality check - field may be named differently
        Boolean requiresQC = request.getQualityCheckRequired();
        if (requiresQC != null) {
            salesReturn.setQualityCheckRequired(requiresQC);
        } else {
            salesReturn.setQualityCheckRequired(false);
        }
        
        // Inspection status - may need to be handled differently
        salesReturn.setInspectionStatus("PENDING");
        
        salesReturn.setNotes(request.getNotes());
        
        // Calculate totals - handle BigDecimal properly
        BigDecimal subtotal = request.getSubtotal() != null ? 
            BigDecimal.valueOf(request.getSubtotal()) : BigDecimal.ZERO;
        BigDecimal tax = request.getTax() != null ? 
            BigDecimal.valueOf(request.getTax()) : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscount() != null ? 
            BigDecimal.valueOf(request.getDiscount()) : BigDecimal.ZERO;
        BigDecimal total = request.getTotal() != null ? 
            BigDecimal.valueOf(request.getTotal()) : BigDecimal.ZERO;
        
        salesReturn.setSubtotalAmount(subtotal);
        salesReturn.setTaxAmount(tax);
        salesReturn.setDiscountAmount(discount);
        salesReturn.setTotalAmount(total);
        
        return salesReturnRepository.save(salesReturn);
    }
    
    @Override
    public SalesReturn updateSalesReturn(Long id, SalesReturnRequest request) {
        log.info("Updating sales return: {}", id);
        
        SalesReturn existing = getSalesReturnById(id);
        
        // Only allow updates if not approved
        if (existing.getApproved()) {
            throw new RuntimeException("Cannot update approved sales return");
        }
        
        existing.setReturnDate(request.getReturnDate());
        existing.setReturnType(request.getReturnType());
        existing.setReturnReason(request.getReturnReason());
        
        if (request.getWarehouseId() != null) {
            existing.setWarehouseId(request.getWarehouseId());
        }
        
        // Update amounts - handle BigDecimal properly
        BigDecimal subtotal = request.getSubtotal() != null ? 
            BigDecimal.valueOf(request.getSubtotal()) : BigDecimal.ZERO;
        BigDecimal tax = request.getTax() != null ? 
            BigDecimal.valueOf(request.getTax()) : BigDecimal.ZERO;
        BigDecimal discount = request.getDiscount() != null ? 
            BigDecimal.valueOf(request.getDiscount()) : BigDecimal.ZERO;
        BigDecimal total = request.getTotal() != null ? 
            BigDecimal.valueOf(request.getTotal()) : BigDecimal.ZERO;
        
        existing.setSubtotalAmount(subtotal);
        existing.setTaxAmount(tax);
        existing.setDiscountAmount(discount);
        existing.setTotalAmount(total);
        existing.setNotes(request.getNotes());
        
        return salesReturnRepository.save(existing);
    }
    
    @Override
    public void deleteSalesReturn(Long id) {
        log.info("Deleting sales return: {}", id);
        
        SalesReturn salesReturn = getSalesReturnById(id);
        
        // Prevent deletion if approved or credit note generated
        if (salesReturn.getApproved()) {
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
    
    @Override
    public SalesReturn approveSalesReturn(Long id, Long approvedByUserId, String notes) {
        log.info("Approving sales return: {}", id);
        
        SalesReturn salesReturn = getSalesReturnById(id);
        
        if (salesReturn.getApproved()) {
            throw new RuntimeException("Sales return already approved");
        }
        
        salesReturn.setApproved(true);
        salesReturn.setStatus("APPROVED");
        salesReturn.setApprovalDate(LocalDate.now());
        salesReturn.setApprovedBy(approvedByUserId);
        
        if (notes != null) {
            salesReturn.setNotes(notes);
        }
        
        return salesReturnRepository.save(salesReturn);
    }
    
    @Override
    public SalesReturn rejectSalesReturn(Long id, String reason) {
        log.info("Rejecting sales return: {}", id);
        
        SalesReturn salesReturn = getSalesReturnById(id);
        
        salesReturn.setStatus("REJECTED");
        salesReturn.setRejectionReason(reason);
        salesReturn.setRejectedDate(LocalDate.now());
        
        return salesReturnRepository.save(salesReturn);
    }
    
    @Override
    public SalesReturn inspectSalesReturn(Long id, String inspectionNotes, String inspectionResult) {
        log.info("Inspecting sales return: {}", id);
        
        SalesReturn salesReturn = getSalesReturnById(id);
        
        salesReturn.setInspected(true);
        salesReturn.setInspectionStatus(inspectionResult);
        salesReturn.setInspectionDate(LocalDate.now());
        salesReturn.setInspectionNotes(inspectionNotes);
        
        return salesReturnRepository.save(salesReturn);
    }
    
    @Override
    public CreditNote generateCreditNote(Long salesReturnId) {
        log.info("Generating credit note for sales return: {}", salesReturnId);
        
        SalesReturn salesReturn = getSalesReturnById(salesReturnId);
        
        if (!salesReturn.getApproved()) {
            throw new RuntimeException("Cannot generate credit note for unapproved sales return");
        }
        
        if (salesReturn.getCreditNoteGenerated()) {
            throw new RuntimeException("Credit note already generated for this sales return");
        }
        
        // Create credit note
        CreditNote creditNote = new CreditNote();
        creditNote.setCreditNoteNumber(generateCreditNoteNumber());
        creditNote.setSalesReturnId(salesReturnId);
        creditNote.setCustomerId(salesReturn.getCustomerId());
        creditNote.setIssueDate(LocalDate.now());
        creditNote.setCreditAmount(salesReturn.getTotalAmount());
        creditNote.setRemainingCreditAmount(salesReturn.getTotalAmount());
        creditNote.setStatus("PENDING");
        creditNote.setApproved(false);
        creditNote.setApplied(false);
        creditNote.setRefunded(false);
        creditNote.setReason(salesReturn.getReturnReason());
        
        CreditNote savedCreditNote = creditNoteRepository.save(creditNote);
        
        // Update sales return
        salesReturn.setCreditNoteGenerated(true);
        salesReturn.setCreditNoteId(savedCreditNote.getId());
        salesReturnRepository.save(salesReturn);
        
        return savedCreditNote;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getSalesReturnsByCustomer(Long customerId) {
        return salesReturnRepository.findByCustomerId(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getSalesReturnsByStatus(String status) {
        return salesReturnRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getSalesReturnsByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesReturnRepository.findByReturnDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getPendingSalesReturns() {
        return salesReturnRepository.findPendingSalesReturns();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getApprovedSalesReturns() {
        return salesReturnRepository.findApprovedSalesReturns();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getRejectedSalesReturns() {
        return salesReturnRepository.findRejectedSalesReturns();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesReturn> getRecentSalesReturns(int limit) {
        return salesReturnRepository.findRecentSalesReturns(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countSalesReturnsByStatus(String status) {
        return salesReturnRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countSalesReturnsByCustomer(Long customerId) {
        return salesReturnRepository.countByCustomerId(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalReturnAmount(Long customerId, LocalDate startDate, LocalDate endDate) {
        List<SalesReturn> returns;
        if (customerId != null) {
            returns = salesReturnRepository.findByCustomerId(customerId);
        } else {
            returns = salesReturnRepository.findAll();
        }
        
        return returns.stream()
            .filter(sr -> {
                LocalDate returnDate = sr.getReturnDate();
                return returnDate != null && 
                       !returnDate.isBefore(startDate) && 
                       !returnDate.isAfter(endDate);
            })
            .map(SalesReturn::getTotalAmount)
            .filter(Objects::nonNull)
            .map(BigDecimal::doubleValue)
            .reduce(0.0, Double::sum);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSalesReturnStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", salesReturnRepository.count());
        stats.put("pending", countSalesReturnsByStatus("PENDING"));
        stats.put("approved", countSalesReturnsByStatus("APPROVED"));
        stats.put("rejected", countSalesReturnsByStatus("REJECTED"));
        stats.put("inspected", salesReturnRepository.findInspectedSalesReturns().size());
        stats.put("creditNoteGenerated", salesReturnRepository.findWithCreditNoteGenerated().size());
        return stats;
    }
    
    @Override
    public List<SalesReturn> approveBulkSalesReturns(List<Long> ids, Long approvedByUserId) {
        return ids.stream()
            .map(id -> approveSalesReturn(id, approvedByUserId, null))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CreditNote> generateBulkCreditNotes(List<Long> salesReturnIds) {
        return salesReturnIds.stream()
            .map(this::generateCreditNote)
            .collect(Collectors.toList());
    }
    
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
}
