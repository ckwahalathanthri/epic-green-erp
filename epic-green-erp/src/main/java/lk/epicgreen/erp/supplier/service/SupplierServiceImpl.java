package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.SupplierRequest;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Supplier Service Implementation
 * Implementation of supplier service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierServiceImpl implements SupplierService {
    
    private final SupplierRepository supplierRepository;
    
    @Override
    public Supplier createSupplier(SupplierRequest request) {
        log.info("Creating supplier: {}", request.getSupplierName());
        
        // Validate unique fields
        if (supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
            throw new RuntimeException("Supplier code already exists: " + request.getSupplierCode());
        }
        if (request.getEmail() != null && supplierRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        if (request.getTaxNumber() != null && supplierRepository.existsByTaxNumber(request.getTaxNumber())) {
            throw new RuntimeException("Tax number already exists: " + request.getTaxNumber());
        }
        
        Supplier supplier = new Supplier();
        supplier.setSupplierCode(request.getSupplierCode());
        supplier.setSupplierName(request.getSupplierName());
        supplier.setSupplierType(request.getSupplierType() != null ? request.getSupplierType() : "GENERAL");
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setMobile(request.getMobile());
        supplier.setFax(request.getFax());
        supplier.setWebsite(request.getWebsite());
        supplier.setAddress(request.getAddress());
        supplier.setCity(request.getCity());
        supplier.setStateProvince(request.getStateProvince());
        supplier.setPostalCode(request.getPostalCode());
        supplier.setCountry(request.getCountry());
        supplier.setTaxNumber(request.getTaxNumber());
        supplier.setRegistrationNumber(request.getRegistrationNumber());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setCreditDays(request.getCreditDays());
        supplier.setIsActive(true);
        supplier.setStatus("PENDING");
        supplier.setIsApproved(false);
        supplier.setIsCreditAllowed(false);
        supplier.setCreditLimit(0.0);
        supplier.setCurrentBalance(0.0);
        supplier.setTotalOrders(0);
        supplier.setRating(0.0);
        supplier.setRegistrationDate(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier: {}", id);
        Supplier existing = getSupplierById(id);
        
        // Validate unique fields if changed
        if (!existing.getSupplierCode().equals(request.getSupplierCode()) &&
            supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
            throw new RuntimeException("Supplier code already exists: " + request.getSupplierCode());
        }
        if (request.getEmail() != null && !request.getEmail().equals(existing.getEmail()) &&
            supplierRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        if (request.getTaxNumber() != null && !request.getTaxNumber().equals(existing.getTaxNumber()) &&
            supplierRepository.existsByTaxNumber(request.getTaxNumber())) {
            throw new RuntimeException("Tax number already exists: " + request.getTaxNumber());
        }
        
        existing.setSupplierCode(request.getSupplierCode());
        existing.setSupplierName(request.getSupplierName());
        existing.setSupplierType(request.getSupplierType());
        existing.setContactPerson(request.getContactPerson());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());
        existing.setMobile(request.getMobile());
        existing.setFax(request.getFax());
        existing.setWebsite(request.getWebsite());
        existing.setAddress(request.getAddress());
        existing.setCity(request.getCity());
        existing.setStateProvince(request.getStateProvince());
        existing.setPostalCode(request.getPostalCode());
        existing.setCountry(request.getCountry());
        existing.setTaxNumber(request.getTaxNumber());
        existing.setRegistrationNumber(request.getRegistrationNumber());
        existing.setPaymentTerms(request.getPaymentTerms());
        existing.setCreditDays(request.getCreditDays());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(existing);
    }
    
    @Override
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        if (!canDeleteSupplier(id)) {
            throw new RuntimeException("Cannot delete supplier with existing transactions");
        }
        
        supplierRepository.deleteById(id);
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
    public Supplier getSupplierByEmail(String email) {
        return supplierRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Supplier not found with email: " + email));
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
    public Page<Supplier> searchSuppliers(String keyword, Pageable pageable) {
        return supplierRepository.searchSuppliers(keyword, pageable);
    }
    
    @Override
    public Supplier activateSupplier(Long id) {
        log.info("Activating supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        supplier.setIsActive(true);
        supplier.setStatus("ACTIVE");
        supplier.setUpdatedAt(LocalDateTime.now());
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier deactivateSupplier(Long id) {
        log.info("Deactivating supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        supplier.setIsActive(false);
        supplier.setStatus("INACTIVE");
        supplier.setUpdatedAt(LocalDateTime.now());
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier approveSupplier(Long id, Long approvedByUserId, String approvalNotes) {
        log.info("Approving supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        if (!canApproveSupplier(id)) {
            throw new RuntimeException("Supplier cannot be approved");
        }
        
        supplier.setIsApproved(true);
        supplier.setApprovedDate(LocalDateTime.now());
        supplier.setApprovedByUserId(approvedByUserId);
        supplier.setApprovalNotes(approvalNotes);
        supplier.setStatus("ACTIVE");
        supplier.setIsActive(true);
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier rejectSupplier(Long id, String rejectionReason) {
        log.info("Rejecting supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        supplier.setStatus("REJECTED");
        supplier.setIsApproved(false);
        supplier.setIsActive(false);
        supplier.setRejectionReason(rejectionReason);
        supplier.setRejectedDate(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier blockSupplier(Long id, String blockReason) {
        log.info("Blocking supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        supplier.setStatus("BLOCKED");
        supplier.setIsActive(false);
        supplier.setBlockReason(blockReason);
        supplier.setBlockedDate(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    public Supplier unblockSupplier(Long id) {
        log.info("Unblocking supplier: {}", id);
        Supplier supplier = getSupplierById(id);
        
        supplier.setStatus("ACTIVE");
        supplier.setIsActive(true);
        supplier.setBlockReason(null);
        supplier.setBlockedDate(null);
        supplier.setUpdatedAt(LocalDateTime.now());
        
        return supplierRepository.save(supplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getActiveSuppliers() {
        return supplierRepository.findActiveSuppliers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Supplier> getActiveSuppliers(Pageable pageable) {
        return supplierRepository.findActiveSuppliers(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getInactiveSuppliers() {
        return supplierRepository.findInactiveSuppliers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getApprovedSuppliers() {
        return supplierRepository.findApprovedSuppliers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getPendingApprovalSuppliers() {
        return supplierRepository.findPendingApprovalSuppliers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getRejectedSuppliers() {
        return supplierRepository.findRejectedSuppliers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getBlockedSuppliers() {
        return supplierRepository.findBlockedSuppliers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersByType(String supplierType) {
        return supplierRepository.findBySupplierType(supplierType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersByCity(String city) {
        return supplierRepository.findByCity(city);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersByCountry(String country) {
        return supplierRepository.findByCountry(country);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getLocalSuppliers(String country) {
        return supplierRepository.findLocalSuppliers(country);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getInternationalSuppliers(String country) {
        return supplierRepository.findInternationalSuppliers(country);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersWithCredit() {
        return supplierRepository.findSuppliersWithCredit();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersExceedingCreditLimit() {
        return supplierRepository.findSuppliersExceedingCreditLimit();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersWithOutstandingBalance() {
        return supplierRepository.findSuppliersWithOutstandingBalance();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersByCreditLimitRange(Double minLimit, Double maxLimit) {
        return supplierRepository.findByCreditLimitRange(minLimit, maxLimit);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersByBalanceRange(Double minBalance, Double maxBalance) {
        return supplierRepository.findByBalanceRange(minBalance, maxBalance);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getHighRatedSuppliers(Double minRating) {
        return supplierRepository.findHighRatedSuppliers(minRating);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getLowRatedSuppliers(Double maxRating) {
        return supplierRepository.findLowRatedSuppliers(maxRating);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersByRegion(String region) {
        return supplierRepository.findByRegion(region);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersRequiringAttention(Double lowRatingThreshold) {
        return supplierRepository.findSuppliersRequiringAttention(lowRatingThreshold);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getRecentSuppliers(int limit) {
        return supplierRepository.findRecentSuppliers(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getRecentlyUpdatedSuppliers(int limit) {
        return supplierRepository.findRecentlyUpdatedSuppliers(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopRatedSuppliers(int limit) {
        return supplierRepository.getTopRatedSuppliers(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersWithHighestBalance(int limit) {
        return supplierRepository.getSuppliersWithHighestBalance(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getSuppliersWithHighestCreditLimit(int limit) {
        return supplierRepository.getSuppliersWithHighestCreditLimit(PageRequest.of(0, limit));
    }
    
    @Override
    public void updateCreditLimit(Long supplierId, Double newCreditLimit) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setCreditLimit(newCreditLimit);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void enableCredit(Long supplierId, Double creditLimit, Integer creditDays) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setIsCreditAllowed(true);
        supplier.setCreditLimit(creditLimit);
        supplier.setCreditDays(creditDays);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void disableCredit(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setIsCreditAllowed(false);
        supplier.setCreditLimit(0.0);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void updateCurrentBalance(Long supplierId, Double newBalance) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setCurrentBalance(newBalance);
        supplier.setLastTransactionDate(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void increaseBalance(Long supplierId, Double amount) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setCurrentBalance(supplier.getCurrentBalance() + amount);
        supplier.setLastTransactionDate(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void decreaseBalance(Long supplierId, Double amount) {
        Supplier supplier = getSupplierById(supplierId);
        Double newBalance = supplier.getCurrentBalance() - amount;
        if (newBalance < 0) {
            newBalance = 0.0;
        }
        supplier.setCurrentBalance(newBalance);
        supplier.setLastTransactionDate(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAvailableCredit(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        if (!supplier.getIsCreditAllowed()) {
            return 0.0;
        }
        Double available = supplier.getCreditLimit() - supplier.getCurrentBalance();
        return available > 0 ? available : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isCreditAvailable(Long supplierId, Double amount) {
        return getAvailableCredit(supplierId) >= amount;
    }
    
    @Override
    public void updateRating(Long supplierId, Double rating) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setRating(rating);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void updateRatingAndReviews(Long supplierId, Double rating, String reviewComments) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setRating(rating);
        supplier.setReviewComments(reviewComments);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    public void incrementTotalOrders(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setTotalOrders(supplier.getTotalOrders() + 1);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateSupplier(Supplier supplier) {
        return supplier.getSupplierCode() != null &&
               supplier.getSupplierName() != null &&
               supplier.getContactPerson() != null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isSupplierCodeAvailable(String supplierCode) {
        return !supplierRepository.existsBySupplierCode(supplierCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !supplierRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isTaxNumberAvailable(String taxNumber) {
        return !supplierRepository.existsByTaxNumber(taxNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteSupplier(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        // Cannot delete if has transactions or outstanding balance
        return supplier.getCurrentBalance() == 0 && supplier.getTotalOrders() == 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canApproveSupplier(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        return !supplier.getIsApproved() && !supplier.getStatus().equals("REJECTED");
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canBlockSupplier(Long supplierId) {
        Supplier supplier = getSupplierById(supplierId);
        return !supplier.getStatus().equals("BLOCKED");
    }
    
    @Override
    public List<Supplier> createBulkSuppliers(List<SupplierRequest> requests) {
        return requests.stream()
            .map(this::createSupplier)
            .collect(Collectors.toList());
    }
    
    @Override
    public int activateBulkSuppliers(List<Long> supplierIds) {
        int count = 0;
        for (Long id : supplierIds) {
            try {
                activateSupplier(id);
                count++;
            } catch (Exception e) {
                log.error("Error activating supplier: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deactivateBulkSuppliers(List<Long> supplierIds) {
        int count = 0;
        for (Long id : supplierIds) {
            try {
                deactivateSupplier(id);
                count++;
            } catch (Exception e) {
                log.error("Error deactivating supplier: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int approveBulkSuppliers(List<Long> supplierIds, Long approvedByUserId) {
        int count = 0;
        for (Long id : supplierIds) {
            try {
                approveSupplier(id, approvedByUserId, "Bulk approval");
                count++;
            } catch (Exception e) {
                log.error("Error approving supplier: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkSuppliers(List<Long> supplierIds) {
        int count = 0;
        for (Long id : supplierIds) {
            try {
                deleteSupplier(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting supplier: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSupplierStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalSuppliers", supplierRepository.count());
        stats.put("activeSuppliers", supplierRepository.countActiveSuppliers());
        stats.put("inactiveSuppliers", supplierRepository.countInactiveSuppliers());
        stats.put("approvedSuppliers", supplierRepository.countApprovedSuppliers());
        stats.put("pendingApprovalSuppliers", supplierRepository.countPendingApprovalSuppliers());
        stats.put("suppliersWithOutstandingBalance", supplierRepository.countSuppliersWithOutstandingBalance());
        stats.put("suppliersExceedingCreditLimit", supplierRepository.countSuppliersExceedingCreditLimit());
        stats.put("totalOutstandingBalance", getTotalOutstandingBalance());
        stats.put("totalCreditLimit", getTotalCreditLimit());
        stats.put("averageCreditLimit", getAverageCreditLimit());
        stats.put("averageRating", getAverageRating());
        stats.put("averageCurrentBalance", getAverageCurrentBalance());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSupplierTypeDistribution() {
        List<Object[]> results = supplierRepository.getSupplierTypeDistribution();
        return convertToMapList(results, "supplierType", "supplierCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getStatusDistribution() {
        List<Object[]> results = supplierRepository.getStatusDistribution();
        return convertToMapList(results, "status", "supplierCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSuppliersByCountry() {
        List<Object[]> results = supplierRepository.getSuppliersByCountry();
        return convertToMapList(results, "country", "supplierCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSuppliersByCity() {
        List<Object[]> results = supplierRepository.getSuppliersByCity();
        return convertToMapList(results, "city", "supplierCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPaymentTermsDistribution() {
        List<Object[]> results = supplierRepository.getPaymentTermsDistribution();
        return convertToMapList(results, "paymentTerms", "supplierCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalOutstandingBalance() {
        Double total = supplierRepository.getTotalOutstandingBalance();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalCreditLimit() {
        Double total = supplierRepository.getTotalCreditLimit();
        return total != null ? total : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageCreditLimit() {
        Double average = supplierRepository.getAverageCreditLimit();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating() {
        Double average = supplierRepository.getAverageRating();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageCurrentBalance() {
        Double average = supplierRepository.getAverageCurrentBalance();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getSupplierStatistics());
        dashboard.put("supplierTypeDistribution", getSupplierTypeDistribution());
        dashboard.put("statusDistribution", getStatusDistribution());
        dashboard.put("suppliersByCountry", getSuppliersByCountry());
        dashboard.put("suppliersByCity", getSuppliersByCity());
        dashboard.put("paymentTermsDistribution", getPaymentTermsDistribution());
        dashboard.put("topRatedSuppliers", getTopRatedSuppliers(10));
        dashboard.put("suppliersWithHighestBalance", getSuppliersWithHighestBalance(10));
        dashboard.put("suppliersRequiringAttention", getSuppliersRequiringAttention(3.0));
        
        return dashboard;
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
