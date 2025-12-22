package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.SupplierRequest;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Supplier Service Implementation
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public abstract class SupplierServiceImpl implements SupplierService {
    
    private final SupplierRepository supplierRepository;
    
    @Override
    public Supplier createSupplier(SupplierRequest request) {
        log.info("Creating supplier: {}", request.getSupplierName());
        
        // Generate supplier code if not provided
        String supplierCode = request.getSupplierCode() != null ? 
            request.getSupplierCode() : generateSupplierCode();
        
        Supplier supplier = new Supplier();
        supplier.setSupplierCode(supplierCode);
        supplier.setSupplierName(request.getSupplierName());
        supplier.setName(request.getSupplierName());
        supplier.setCompanyName(request.getCompanyName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setMobile(request.getMobile());
        supplier.setFax(request.getFax());
        supplier.setWebsite(request.getWebsite());
        
        // Address
        supplier.setAddress(request.getAddress());
        supplier.setCity(request.getCity());
        supplier.setStateProvince(request.getStateProvince());
        supplier.setState(request.getStateProvince());
        supplier.setCountry(request.getCountry());
        supplier.setPostalCode(request.getPostalCode());
        
        // Business info
        supplier.setTaxNumber(request.getTaxNumber());
        supplier.setTaxId(request.getTaxNumber());
        supplier.setRegistrationNumber(request.getRegistrationNumber());
        supplier.setBusinessRegistrationNo(request.getRegistrationNumber());
        supplier.setSupplierType(request.getSupplierType());
        supplier.setRating(request.getRating());
        
        // Initial status
        supplier.setIsApproved(false);
        supplier.setIsCreditAllowed(false);
        supplier.setCreditLimit(request.getCreditLimit() != null ? 
            request.getCreditLimit() : BigDecimal.ZERO);
        supplier.setCurrentBalance(BigDecimal.ZERO);
        supplier.setOutstandingBalance(BigDecimal.ZERO);
        supplier.setTotalOrders(0);
        supplier.setCreditDays(request.getCreditDays() != null ? request.getCreditDays() : 0);
        supplier.setRegistrationDate(LocalDateTime.now());
        
        // Payment and delivery terms
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setDeliveryTerms(request.getDeliveryTerms());
        
        // Banking info
        supplier.setBankName(request.getBankName());
        supplier.setBankAccountNo(request.getBankAccountNo());
        supplier.setBankBranch(request.getBankBranch());
        supplier.setBankSwiftCode(request.getBankSwiftCode());
        
        supplier.setNotes(request.getNotes());
        supplier.setStatus("PENDING_APPROVAL");
        supplier.setIsActive(true);
        supplier.setIsBlocked(false);
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier: {}", id);
        Supplier existing = getSupplierById(id);
        
        // Only update if supplier is not yet approved or if certain fields allow updates
        if (Boolean.TRUE.equals(existing.getIsApproved()) && 
            request.getTaxNumber() != null &&
            !request.getTaxNumber().equals(existing.getTaxNumber())) {
            throw new RuntimeException("Cannot change tax number for approved supplier");
        }
        
        existing.setSupplierName(request.getSupplierName());
        existing.setName(request.getSupplierName());
        existing.setCompanyName(request.getCompanyName());
        existing.setContactPerson(request.getContactPerson());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());
        existing.setMobile(request.getMobile());
        existing.setFax(request.getFax());
        existing.setWebsite(request.getWebsite());
        
        // Address
        existing.setAddress(request.getAddress());
        existing.setCity(request.getCity());
        existing.setStateProvince(request.getStateProvince());
        existing.setState(request.getStateProvince());
        existing.setCountry(request.getCountry());
        existing.setPostalCode(request.getPostalCode());
        
        // Business info
        existing.setTaxNumber(request.getTaxNumber());
        existing.setTaxId(request.getTaxNumber());
        existing.setRegistrationNumber(request.getRegistrationNumber());
        existing.setBusinessRegistrationNo(request.getRegistrationNumber());
        existing.setSupplierType(request.getSupplierType());
        existing.setRating(request.getRating());
        
        // Credit settings
        if (request.getCreditLimit() != null) {
            existing.setCreditLimit(request.getCreditLimit());
        }
        if (request.getCreditDays() != null) {
            existing.setCreditDays(request.getCreditDays());
        }
        
        // Payment and delivery terms
        existing.setPaymentTerms(request.getPaymentTerms());
        existing.setDeliveryTerms(request.getDeliveryTerms());
        
        // Banking info
        existing.setBankName(request.getBankName());
        existing.setBankAccountNo(request.getBankAccountNo());
        existing.setBankBranch(request.getBankBranch());
        existing.setBankSwiftCode(request.getBankSwiftCode());
        
        existing.setNotes(request.getNotes());
        
        return supplierRepository.save(existing);
    }
    
    @Override
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        // Check if supplier has outstanding balance
        BigDecimal balance = supplier.getCurrentBalance();
        if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Cannot delete supplier with outstanding balance");
        }
        
        // Soft delete - just set inactive
        supplier.setIsActive(false);
        supplier.setStatus("DELETED");
        supplierRepository.save(supplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Supplier getSupplierByCode(String supplierCode) {
        return supplierRepository.findBySupplierCode(supplierCode)
            .orElseThrow(() -> new RuntimeException("Supplier not found with code: " + supplierCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Supplier> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Supplier> searchSuppliers(String searchTerm, Pageable pageable) {
        return supplierRepository.searchSuppliers(searchTerm, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getActiveSuppliers() {
        return supplierRepository.findByIsActive(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getApprovedSuppliers() {
        return supplierRepository.findByIsApproved(true);
    }
    
    @Override
    public Supplier approveSupplier(Long id, Long approvedByUserId, String notes) {
        log.info("Approving supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        supplier.setIsApproved(true);
        supplier.setApprovedDate(LocalDateTime.now());
        supplier.setApprovedByUserId(approvedByUserId);
        supplier.setApprovalNotes(notes);
        supplier.setStatus("APPROVED");
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier rejectSupplier(Long id, String reason) {
        log.info("Rejecting supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        supplier.setIsApproved(false);
        supplier.setStatus("REJECTED");
        supplier.setRejectionReason(reason);
        supplier.setRejectedDate(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier blockSupplier(Long id, String reason) {
        log.info("Blocking supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        supplier.setIsBlocked(true);
        supplier.setBlockReason(reason);
        supplier.setBlockedReason(reason);
        supplier.setBlockedDate(LocalDate.now());
        supplier.setStatus("BLOCKED");
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier unblockSupplier(Long id) {
        log.info("Unblocking supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        supplier.setIsBlocked(false);
        supplier.setBlockReason(null);
        supplier.setBlockedReason(null);
        supplier.setBlockedDate(null);
        supplier.setStatus(Boolean.TRUE.equals(supplier.getIsApproved()) ? "APPROVED" : "PENDING_APPROVAL");
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersByType(String supplierType) {
        return supplierRepository.findBySupplierType(supplierType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersByStatus(String status) {
        return supplierRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getBlockedSuppliers() {
        // Manual filtering if repository method doesn't exist
        return supplierRepository.findAll().stream()
            .filter(s -> Boolean.TRUE.equals(s.getIsBlocked()))
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersWithOutstandingBalance() {
        return supplierRepository.findAll().stream()
            .filter(s -> {
                BigDecimal balance = s.getCurrentBalance();
                return balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersExceedingCreditLimit() {
        return supplierRepository.findAll().stream()
            .filter(s -> {
                BigDecimal balance = s.getCurrentBalance();
                BigDecimal limit = s.getCreditLimit();
                return balance != null && limit != null && 
                       balance.compareTo(limit) > 0;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countSuppliersByStatus(String status) {
        return supplierRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countActiveSuppliers() {
        // Manual counting if repository method doesn't exist
        return supplierRepository.findAll().stream()
            .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
            .count();
    }
    
    @Override
    public Supplier updateCreditSettings(Long supplierId, Double creditLimit, Integer creditDays) {
        Supplier supplier = getSupplierById(supplierId);
        
        if (creditLimit != null) {
            supplier.setCreditLimit(BigDecimal.valueOf(creditLimit));
        }
        if (creditDays != null) {
            supplier.setCreditDays(creditDays);
        }
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public void enableCredit(Long supplierId, Double creditLimit, Integer creditDays) {
        Supplier supplier = getSupplierById(supplierId);
        
        supplier.setIsCreditAllowed(true);
        supplier.setCreditLimit(creditLimit != null ? 
            BigDecimal.valueOf(creditLimit) : BigDecimal.ZERO);
        supplier.setCreditDays(creditDays != null ? creditDays : 0);
        
        supplierRepository.save(supplier);
    }
    
    @Override
    public void disableCredit(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        
        supplier.setIsCreditAllowed(false);
        supplier.setCreditLimit(BigDecimal.ZERO);
        supplier.setCreditDays(0);
        
        supplierRepository.save(supplier);
    }
    
    @Override
    public void updateSupplierBalance(Long supplierId, Double amount) {
        Supplier supplier = getSupplierById(supplierId);
        BigDecimal newBalance = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
        supplier.setCurrentBalance(newBalance);
        supplier.setOutstandingBalance(newBalance);
        supplier.setLastTransactionDate(LocalDateTime.now());
        supplier.setLastTransactionAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void addPurchaseToSupplier(Long supplierId, Double amount) {
        Supplier supplier = getSupplierById(supplierId);
        BigDecimal currentBalance = supplier.getCurrentBalance() != null ? 
            supplier.getCurrentBalance() : BigDecimal.ZERO;
        BigDecimal purchaseAmount = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
        BigDecimal newBalance = currentBalance.add(purchaseAmount);
        
        supplier.setCurrentBalance(newBalance);
        supplier.setOutstandingBalance(newBalance);
        supplier.setLastTransactionDate(LocalDateTime.now());
        supplier.setLastTransactionAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void recordPaymentToSupplier(Long supplierId, Double amount) {
        Supplier supplier = getSupplierById(supplierId);
        BigDecimal currentBalance = supplier.getCurrentBalance() != null ? 
            supplier.getCurrentBalance() : BigDecimal.ZERO;
        BigDecimal paymentAmount = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
        BigDecimal newBalance = currentBalance.subtract(paymentAmount);
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
        }
        
        supplier.setCurrentBalance(newBalance);
        supplier.setOutstandingBalance(newBalance);
        supplier.setLastTransactionDate(LocalDateTime.now());
        supplier.setLastTransactionAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canExtendCredit(Long supplierId, Double amount) {
        Supplier supplier = getSupplierById(supplierId);
        
        if (!Boolean.TRUE.equals(supplier.getIsCreditAllowed())) {
            return false;
        }
        
        BigDecimal currentBalance = supplier.getCurrentBalance() != null ? 
            supplier.getCurrentBalance() : BigDecimal.ZERO;
        BigDecimal requestedAmount = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO;
        BigDecimal newBalance = currentBalance.add(requestedAmount);
        BigDecimal creditLimit = supplier.getCreditLimit() != null ? 
            supplier.getCreditLimit() : BigDecimal.ZERO;
        
        return newBalance.compareTo(creditLimit) <= 0;
    }
    
    @Override
    public Supplier updateSupplierRating(Long supplierId, Integer rating) {
        Supplier supplier = getSupplierById(supplierId);
        
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        
        supplier.setRating(rating);
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier addSupplierReview(Long supplierId, Integer rating, String comments) {
        Supplier supplier = getSupplierById(supplierId);
        
        if (rating != null) {
            if (rating < 1 || rating > 5) {
                throw new RuntimeException("Rating must be between 1 and 5");
            }
            supplier.setRating(rating);
        }
        
        supplier.setReviewComments(comments);
        return supplierRepository.save(supplier);
    }
    
    @Override
    public void incrementSupplierOrderCount(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        Integer currentCount = supplier.getTotalOrders() != null ? 
            supplier.getTotalOrders() : 0;
        supplier.setTotalOrders(currentCount + 1);
        supplier.setLastPurchaseDate(LocalDate.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSupplierStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Supplier> allSuppliers = supplierRepository.findAll();
        
        stats.put("total", allSuppliers.size());
        stats.put("active", allSuppliers.stream().filter(s -> Boolean.TRUE.equals(s.getIsActive())).count());
        stats.put("approved", allSuppliers.stream().filter(s -> Boolean.TRUE.equals(s.getIsApproved())).count());
        stats.put("blocked", allSuppliers.stream().filter(s -> Boolean.TRUE.equals(s.getIsBlocked())).count());
        stats.put("withOutstanding", allSuppliers.stream()
            .filter(s -> {
                BigDecimal balance = s.getCurrentBalance();
                return balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
            })
            .count());
        
        // Calculate total outstanding
        BigDecimal totalOutstanding = allSuppliers.stream()
            .map(Supplier::getCurrentBalance)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.put("totalOutstanding", totalOutstanding.doubleValue());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSupplierSummary(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("id", supplier.getId());
        summary.put("supplierCode", supplier.getSupplierCode());
        summary.put("supplierName", supplier.getSupplierName());
        summary.put("companyName", supplier.getCompanyName());
        summary.put("email", supplier.getEmail());
        summary.put("phone", supplier.getPhone());
        summary.put("status", supplier.getStatus());
        summary.put("isActive", supplier.getIsActive());
        summary.put("isApproved", supplier.getIsApproved());
        summary.put("isBlocked", supplier.getIsBlocked());
        summary.put("isCreditAllowed", supplier.getIsCreditAllowed());
        summary.put("rating", supplier.getRating());
        
        BigDecimal currentBalance = supplier.getCurrentBalance();
        BigDecimal creditLimit = supplier.getCreditLimit();
        
        summary.put("currentBalance", currentBalance != null ? currentBalance.doubleValue() : 0.0);
        summary.put("creditLimit", creditLimit != null ? creditLimit.doubleValue() : 0.0);
        summary.put("creditDays", supplier.getCreditDays());
        summary.put("totalOrders", supplier.getTotalOrders());
        
        return summary;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTopSuppliersByOrders(int limit) {
        return supplierRepository.findAll().stream()
            .filter(s -> s.getTotalOrders() != null && s.getTotalOrders() > 0)
            .sorted((s1, s2) -> {
                Integer orders1 = s1.getTotalOrders() != null ? s1.getTotalOrders() : 0;
                Integer orders2 = s2.getTotalOrders() != null ? s2.getTotalOrders() : 0;
                return Integer.compare(orders2, orders1);
            })
            .limit(limit)
            .map(supplier -> {
                Map<String, Object> summary = new HashMap<>();
                summary.put("supplierId", supplier.getId());
                summary.put("supplierName", supplier.getSupplierName());
                summary.put("totalOrders", supplier.getTotalOrders());
                summary.put("rating", supplier.getRating());
                return summary;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Supplier> allSuppliers = supplierRepository.findAll();
        
        stats.put("totalSuppliers", allSuppliers.size());
        stats.put("activeSuppliers", allSuppliers.stream()
            .filter(s -> Boolean.TRUE.equals(s.getIsActive())).count());
        stats.put("approvedSuppliers", allSuppliers.stream()
            .filter(s -> Boolean.TRUE.equals(s.getIsApproved())).count());
        stats.put("blockedSuppliers", allSuppliers.stream()
            .filter(s -> Boolean.TRUE.equals(s.getIsBlocked())).count());
        
        // Outstanding balances
        BigDecimal totalOutstanding = allSuppliers.stream()
            .map(Supplier::getCurrentBalance)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.put("totalOutstanding", totalOutstanding.doubleValue());
        
        return stats;
    }
    
    private String generateSupplierCode() {
        String prefix = "SUP";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "-" + timestamp.substring(timestamp.length() - 8);
    }
}
