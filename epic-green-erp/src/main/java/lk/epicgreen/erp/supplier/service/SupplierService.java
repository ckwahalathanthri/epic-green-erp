package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.SupplierRequest;
import lk.epicgreen.erp.supplier.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Supplier Service Interface
 * Service for supplier operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SupplierService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    Supplier createSupplier(SupplierRequest request);
    Supplier updateSupplier(Long id, SupplierRequest request);
    void deleteSupplier(Long id);
    Supplier getSupplierById(Long id);
    Supplier getSupplierByCode(String supplierCode);
    Supplier getSupplierByEmail(String email);
    List<Supplier> getAllSuppliers();
    Page<Supplier> getAllSuppliers(Pageable pageable);
    Page<Supplier> searchSuppliers(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    Supplier activateSupplier(Long id);
    Supplier deactivateSupplier(Long id);
    Supplier approveSupplier(Long id, Long approvedByUserId, String approvalNotes);
    Supplier rejectSupplier(Long id, String rejectionReason);
    Supplier blockSupplier(Long id, String blockReason);
    Supplier unblockSupplier(Long id);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<Supplier> getActiveSuppliers();
    Page<Supplier> getActiveSuppliers(Pageable pageable);
    List<Supplier> getInactiveSuppliers();
    List<Supplier> getApprovedSuppliers();
    List<Supplier> getPendingApprovalSuppliers();
    List<Supplier> getRejectedSuppliers();

    @Transactional(readOnly = true)
    List<Supplier> getSuppliersByStatus(String status);

    List<Supplier> getBlockedSuppliers();
    List<Supplier> getSuppliersByType(String supplierType);
    List<Supplier> getSuppliersByCity(String city);
    List<Supplier> getSuppliersByCountry(String country);
    List<Supplier> getLocalSuppliers(String country);
    List<Supplier> getInternationalSuppliers(String country);
    List<Supplier> getSuppliersWithCredit();
    List<Supplier> getSuppliersExceedingCreditLimit();
    List<Supplier> getSuppliersWithOutstandingBalance();
    List<Supplier> getSuppliersByCreditLimitRange(Double minLimit, Double maxLimit);
    List<Supplier> getSuppliersByBalanceRange(Double minBalance, Double maxBalance);
    List<Supplier> getHighRatedSuppliers(Double minRating);
    List<Supplier> getLowRatedSuppliers(Double maxRating);
    List<Supplier> getSuppliersByRegion(String region);
    List<Supplier> getSuppliersRequiringAttention(Double lowRatingThreshold);
    List<Supplier> getRecentSuppliers(int limit);
    List<Supplier> getRecentlyUpdatedSuppliers(int limit);
    List<Supplier> getTopRatedSuppliers(int limit);
    List<Supplier> getSuppliersWithHighestBalance(int limit);
    List<Supplier> getSuppliersWithHighestCreditLimit(int limit);
    
    // ===================================================================
    // CREDIT OPERATIONS
    // ===================================================================
    
    void updateCreditLimit(Long supplierId, Double newCreditLimit);

    @Transactional(readOnly = true)
    Long countSuppliersByStatus(String status);

    @Transactional(readOnly = true)
    Long countActiveSuppliers();

    Supplier updateCreditSettings(Long supplierId, Double creditLimit, Integer creditDays);

    void enableCredit(Long supplierId, Double creditLimit, Integer creditDays);
    void disableCredit(Long supplierId);
    void updateCurrentBalance(Long supplierId, Double newBalance);
    void increaseBalance(Long supplierId, Double amount);
    void decreaseBalance(Long supplierId, Double amount);
    Double getAvailableCredit(Long supplierId);
    boolean isCreditAvailable(Long supplierId, Double amount);
    
    // ===================================================================
    // RATING OPERATIONS
    // ===================================================================
    
    void updateRating(Long supplierId, Double rating);
    void updateRatingAndReviews(Long supplierId, Double rating, String reviewComments);
    void incrementTotalOrders(Long supplierId);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateSupplier(Supplier supplier);
    boolean isSupplierCodeAvailable(String supplierCode);
    boolean isEmailAvailable(String email);
    boolean isTaxNumberAvailable(String taxNumber);
    boolean canDeleteSupplier(Long supplierId);
    boolean canApproveSupplier(Long supplierId);
    boolean canBlockSupplier(Long supplierId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<Supplier> createBulkSuppliers(List<SupplierRequest> requests);
    int activateBulkSuppliers(List<Long> supplierIds);
    int deactivateBulkSuppliers(List<Long> supplierIds);
    int approveBulkSuppliers(List<Long> supplierIds, Long approvedByUserId);
    int deleteBulkSuppliers(List<Long> supplierIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================

    void updateSupplierBalance(Long supplierId, Double amount);

    void addPurchaseToSupplier(Long supplierId, Double amount);

    void recordPaymentToSupplier(Long supplierId, Double amount);

    @Transactional(readOnly = true)
    boolean canExtendCredit(Long supplierId, Double amount);

    Supplier updateSupplierRating(Long supplierId, Integer rating);

    Supplier addSupplierReview(Long supplierId, Integer rating, String comments);

    void incrementSupplierOrderCount(Long supplierId);

    Map<String, Object> getSupplierStatistics();
    List<Map<String, Object>> getSupplierTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getSuppliersByCountry();
    List<Map<String, Object>> getSuppliersByCity();
    List<Map<String, Object>> getPaymentTermsDistribution();
    Double getTotalOutstandingBalance();
    Double getTotalCreditLimit();
    Double getAverageCreditLimit();
    Double getAverageRating();
    Double getAverageCurrentBalance();

    @Transactional(readOnly = true)
    Map<String, Object> getSupplierSummary(Long supplierId);

    @Transactional(readOnly = true)
    List<Map<String, Object>> getTopSuppliersByOrders(int limit);

    Map<String, Object> getDashboardStatistics();
}
