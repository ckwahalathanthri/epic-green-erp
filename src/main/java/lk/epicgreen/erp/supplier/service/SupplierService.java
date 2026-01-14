package lk.epicgreen.erp.supplier.service;

import lk.epicgreen.erp.supplier.dto.request.SupplierRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.supplier.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Supplier entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SupplierService {

    /**
     * Create new supplier
     */
    SupplierResponse createSupplier(SupplierRequest request);

    /**
     * Update existing supplier
     */
    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    /**
     * Activate supplier
     */
    Supplier activateSupplier(Long id);

    /**
     * Deactivate supplier
     */
    Supplier deactivateSupplier(Long id);

    /**
     * Delete supplier (soft delete)
     */
    void deleteSupplier(Long id);

    /**
     * Get supplier by ID
     */
    SupplierResponse getSupplierById(Long id);

    /**
     * Get supplier by code
     */
    SupplierResponse getSupplierByCode(String supplierCode);

    /**
     * Get all suppliers with pagination
     */
    PageResponse<SupplierResponse> getAllSuppliers(Pageable pageable);

    /**
     * Get all active suppliers
     */
    List<SupplierResponse> getAllActiveSuppliers();

    /**
     * Get suppliers by type
     */
    PageResponse<SupplierResponse> getSuppliersByType(String supplierType, Pageable pageable);

    /**
     * Get raw material suppliers
     */
    List<SupplierResponse> getRawMaterialSuppliers();

    List<Supplier> getSuppliersByCity(String city);

    List<Supplier> getSuppliersByCountry(String country);

    List<Supplier> getLocalSuppliers(String country);

    List<Supplier> getInternationalSuppliers(String country);
    
    List<Supplier> getSuppliersWithCredit();

    /**
     * Get packaging suppliers
     */
    List<SupplierResponse> getPackagingSuppliers();

    /**
     * Get service suppliers
     */
    List<SupplierResponse> getServiceSuppliers();

    /**
     * Search suppliers
     */
    PageResponse<SupplierResponse> searchSuppliers(String keyword, Pageable pageable);

    /**
     * Get suppliers with outstanding balance
     */
    List<SupplierResponse> getSuppliersWithOutstandingBalance();

    /**
     * Get suppliers exceeding credit limit
     */
    List<SupplierResponse> getSuppliersExceedingCreditLimit();

    /**
     * Update supplier rating
     */
    void updateSupplierRating(Long id, BigDecimal rating);

    /**
     * Get supplier current balance
     */
    BigDecimal getSupplierBalance(Long id);

    Supplier getSupplierByEmail(String email);

    Supplier approveSupplier(Long id, Long approvedByUserId, String approvalNotes);

    Supplier rejectSupplier(Long id, String rejectionReason);

    Supplier blockSupplier(Long id, String blockReason);

    Supplier unblockSupplier(Long id);
    Page<Supplier> getActiveSuppliers(Pageable pageable);
    List<Supplier> getInactiveSuppliers();
    List<Supplier> getApprovedSuppliers();

    List<Supplier> getPendingApprovalSuppliers();

    List<Supplier> getRejectedSuppliers();

    List<Supplier>getSuppliersByStatus(String status);
    List<Supplier> getBlockedSuppliers();

    List<Supplier> getHighRatedSuppliers(Double minRating);

    List<Supplier> getLowRatedSuppliers(Double maxRating);
    List<Supplier> getSuppliersByRegion(String region);

    List<Supplier> getSuppliersRequiringAttention(double lowRatingThreshold);

    List<Supplier> getRecentSuppliers(Pageable limit);

    List<Supplier> getRecentlyUpdatedSuppliers(Pageable limit);

    List<Supplier> getTopRatedSuppliers(Pageable limit);

    List<Supplier> getSuppliersWithHighestBalance(Pageable limit);

    List<Supplier> getSuppliersWithHighestCreditLimit(Pageable limit);

    void updateCreditLimit(Long id, Double newCreditLimit);

    Supplier updateCreditSettings(Long id, Double creditLimit, Integer creditDays);

    void enableCredit(Long id, Double creditLimit, Integer creditDays);

    void disableCredit(Long id);

    void updateCurrentBalance(Long id, Double newBalance);

    void increaseBalance(Long id, Double amount);

    void decreaseBalance(Long id, Double amount);

    Double getAvailableCredit(Long id);

    boolean canExtendCredit(Long id, Double amount);

    boolean isCreditAvailable(Long id, Double amount);

    void updateRating(Long id, Double rating);

    void updateRatingAndReviews(Long id, Double rating, String reviewComments);

    void incrementTotalOrders(Long id);

    boolean isSupplierCodeAvailable(String supplierCode);

    boolean isEmailAvailable(String email);

    boolean isTaxNumberAvailable(String taxNumber);

    boolean canDeleteSupplier(Long id);

    boolean canApproveSupplier(Long id);

    boolean canBlockSupplier(Long id);

    Map<String, Object> getSupplierStatistics();

    List<Map<String, Object>> getSupplierTypeDistribution();

    Double getTotalOutstandingBalance();

    Double getTotalCreditLimit();

    Double getAverageCreditLimit();

    Double getAverageRating();

    Double getAverageCurrentBalance();

    Map<String, Object> getSupplierSummary(Long id);

    List<Map<String, Object>> getTopSuppliersByOrders(Pageable limit);

    Map<String, Object> getDashboardStatistics();

    Long countSuppliersByStatus(String status);

    Long countActiveSuppliers();

    List<Supplier> getSuppliersByCreditLimitRange(Double minLimit, Double maxLimit);

    List<Supplier> getSuppliersByBalanceRange(Double minBalance, Double maxBalance);

    List<Supplier> createBulkSuppliers(List<SupplierRequest> requests);

    int activateBulkSuppliers(List<Long> supplierIds);

    int deactivateBulkSuppliers(List<Long> supplierIds);

    int approveBulkSuppliers(List<Long> supplierIds, Long approvedByUserId);

    int deleteBulkSuppliers(List<Long> supplierIds);

    List<Map<String, Object>> getStatusDistribution();

//    List<Map<String, Object>> getStatusDistribution();

//    List<Map<String, Object>> getPaymentTermsDistribution();

//    List<Supplier> getSuppliersByBalanceRange(Double minBalance, Double maxBalance);
}
