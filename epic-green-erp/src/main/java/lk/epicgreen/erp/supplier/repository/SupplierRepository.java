package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Supplier Repository
 * Repository for supplier data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find supplier by code
     */
    Optional<Supplier> findBySupplierCode(String supplierCode);
    
    /**
     * Check if supplier exists by code
     */
    boolean existsBySupplierCode(String supplierCode);
    
    /**
     * Find supplier by email
     */
    Optional<Supplier> findByEmail(String email);
    
    /**
     * Check if supplier exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Find supplier by tax number
     */
    Optional<Supplier> findByTaxNumber(String taxNumber);
    
    /**
     * Check if supplier exists by tax number
     */
    boolean existsByTaxNumber(String taxNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find suppliers by supplier type
     */
    List<Supplier> findBySupplierType(String supplierType);
    
    /**
     * Find suppliers by status
     */
    List<Supplier> findByStatus(String status);
    
    /**
     * Find suppliers by status with pagination
     */
    Page<Supplier> findByStatus(String status, Pageable pageable);
    
    /**
     * Find suppliers by is active
     */
    List<Supplier> findByIsActive(Boolean isActive);
    
    /**
     * Find suppliers by is approved
     */
    List<Supplier> findByIsApproved(Boolean isApproved);
    
    /**
     * Find suppliers by is credit allowed
     */
    List<Supplier> findByIsCreditAllowed(Boolean isCreditAllowed);
    
    /**
     * Find suppliers by payment terms
     */
    List<Supplier> findByPaymentTerms(String paymentTerms);
    
    /**
     * Find suppliers by city
     */
    List<Supplier> findByCity(String city);
    
    /**
     * Find suppliers by state/province
     */
    List<Supplier> findByStateProvince(String stateProvince);
    
    /**
     * Find suppliers by country
     */
    List<Supplier> findByCountry(String country);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find suppliers by created at between dates
     */
    List<Supplier> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find suppliers by registration date between dates
     */
    List<Supplier> findByRegistrationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find suppliers by type and status
     */
    List<Supplier> findBySupplierTypeAndStatus(String supplierType, String status);
    
    /**
     * Find suppliers by type and is active
     */
    List<Supplier> findBySupplierTypeAndIsActive(String supplierType, Boolean isActive);
    
    /**
     * Find suppliers by status and is active
     */
    List<Supplier> findByStatusAndIsActive(String status, Boolean isActive);
    
    /**
     * Find suppliers by status and is approved
     */
    List<Supplier> findByStatusAndIsApproved(String status, Boolean isApproved);
    
    /**
     * Find suppliers by city and is active
     */
    List<Supplier> findByCityAndIsActive(String city, Boolean isActive);
    
    /**
     * Find suppliers by country and is active
     */
    List<Supplier> findByCountryAndIsActive(String country, Boolean isActive);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE " +
           "LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.supplierName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Supplier> searchSuppliers(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find active suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.isActive = true AND s.status = 'ACTIVE' " +
           "ORDER BY s.supplierName ASC")
    List<Supplier> findActiveSuppliers();
    
    /**
     * Find active suppliers with pagination
     */
    @Query("SELECT s FROM Supplier s WHERE s.isActive = true AND s.status = 'ACTIVE' " +
           "ORDER BY s.supplierName ASC")
    Page<Supplier> findActiveSuppliers(Pageable pageable);
    
    /**
     * Find inactive suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.isActive = false OR s.status = 'INACTIVE' " +
           "ORDER BY s.supplierName ASC")
    List<Supplier> findInactiveSuppliers();
    
    /**
     * Find approved suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.isApproved = true AND s.isActive = true " +
           "ORDER BY s.supplierName ASC")
    List<Supplier> findApprovedSuppliers();
    
    /**
     * Find pending approval suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.isApproved = false AND s.status != 'REJECTED' " +
           "ORDER BY s.registrationDate ASC")
    List<Supplier> findPendingApprovalSuppliers();
    
    /**
     * Find rejected suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.status = 'REJECTED' " +
           "ORDER BY s.updatedAt DESC")
    List<Supplier> findRejectedSuppliers();
    
    /**
     * Find blocked suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.status = 'BLOCKED' " +
           "ORDER BY s.updatedAt DESC")
    List<Supplier> findBlockedSuppliers();
    
    /**
     * Find suppliers with credit facility
     */
    @Query("SELECT s FROM Supplier s WHERE s.isCreditAllowed = true AND s.isActive = true " +
           "ORDER BY s.creditLimit DESC")
    List<Supplier> findSuppliersWithCredit();
    
    /**
     * Find suppliers exceeding credit limit
     */
    @Query("SELECT s FROM Supplier s WHERE s.isCreditAllowed = true " +
           "AND s.currentBalance > s.creditLimit AND s.isActive = true " +
           "ORDER BY (s.currentBalance - s.creditLimit) DESC")
    List<Supplier> findSuppliersExceedingCreditLimit();
    
    /**
     * Find suppliers with outstanding balance
     */
    @Query("SELECT s FROM Supplier s WHERE s.currentBalance > 0 AND s.isActive = true " +
           "ORDER BY s.currentBalance DESC")
    List<Supplier> findSuppliersWithOutstandingBalance();
    
    /**
     * Find suppliers by credit limit range
     */
    @Query("SELECT s FROM Supplier s WHERE s.creditLimit BETWEEN :minLimit AND :maxLimit " +
           "AND s.isCreditAllowed = true AND s.isActive = true ORDER BY s.creditLimit ASC")
    List<Supplier> findByCreditLimitRange(@Param("minLimit") Double minLimit,
                                          @Param("maxLimit") Double maxLimit);
    
    /**
     * Find suppliers by balance range
     */
    @Query("SELECT s FROM Supplier s WHERE s.currentBalance BETWEEN :minBalance AND :maxBalance " +
           "AND s.isActive = true ORDER BY s.currentBalance ASC")
    List<Supplier> findByBalanceRange(@Param("minBalance") Double minBalance,
                                      @Param("maxBalance") Double maxBalance);
    
    /**
     * Find suppliers with high rating
     */
    @Query("SELECT s FROM Supplier s WHERE s.rating >= :minRating AND s.isActive = true " +
           "ORDER BY s.rating DESC, s.supplierName ASC")
    List<Supplier> findHighRatedSuppliers(@Param("minRating") Double minRating);
    
    /**
     * Find suppliers with low rating
     */
    @Query("SELECT s FROM Supplier s WHERE s.rating < :maxRating AND s.isActive = true " +
           "ORDER BY s.rating ASC, s.supplierName ASC")
    List<Supplier> findLowRatedSuppliers(@Param("maxRating") Double maxRating);
    
    /**
     * Find suppliers by region
     */
    @Query("SELECT s FROM Supplier s WHERE " +
           "LOWER(s.stateProvince) LIKE LOWER(CONCAT('%', :region, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :region, '%')) " +
           "AND s.isActive = true ORDER BY s.supplierName ASC")
    List<Supplier> findByRegion(@Param("region") String region);
    
    /**
     * Find local suppliers (by country)
     */
    @Query("SELECT s FROM Supplier s WHERE s.country = :country AND s.isActive = true " +
           "ORDER BY s.supplierName ASC")
    List<Supplier> findLocalSuppliers(@Param("country") String country);
    
    /**
     * Find international suppliers (not in country)
     */
    @Query("SELECT s FROM Supplier s WHERE s.country != :country AND s.isActive = true " +
           "ORDER BY s.country ASC, s.supplierName ASC")
    List<Supplier> findInternationalSuppliers(@Param("country") String country);
    
    /**
     * Find recent suppliers
     */
    @Query("SELECT s FROM Supplier s ORDER BY s.createdAt DESC")
    List<Supplier> findRecentSuppliers(Pageable pageable);
    
    /**
     * Find recently updated suppliers
     */
    @Query("SELECT s FROM Supplier s ORDER BY s.updatedAt DESC")
    List<Supplier> findRecentlyUpdatedSuppliers(Pageable pageable);
    
    /**
     * Find suppliers requiring attention (low rating or exceeding credit)
     */
    @Query("SELECT s FROM Supplier s WHERE " +
           "(s.rating < :lowRatingThreshold OR " +
           "(s.isCreditAllowed = true AND s.currentBalance > s.creditLimit)) " +
           "AND s.isActive = true ORDER BY s.rating ASC, s.currentBalance DESC")
    List<Supplier> findSuppliersRequiringAttention(@Param("lowRatingThreshold") Double lowRatingThreshold);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count suppliers by type
     */
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.supplierType = :supplierType")
    Long countBySupplierType(@Param("supplierType") String supplierType);
    
    /**
     * Count suppliers by status
     */
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count active suppliers
     */
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isActive = true")
    Long countActiveSuppliers();
    
    /**
     * Count inactive suppliers
     */
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isActive = false")
    Long countInactiveSuppliers();
    
    /**
     * Count approved suppliers
     */
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isApproved = true")
    Long countApprovedSuppliers();
    
    /**
     * Count pending approval suppliers
     */
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isApproved = false AND s.status != 'REJECTED'")
    Long countPendingApprovalSuppliers();
    
    /**
     * Count suppliers with outstanding balance
     */
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.currentBalance > 0")
    Long countSuppliersWithOutstandingBalance();
    
    /**
     * Count suppliers exceeding credit limit
     */
    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isCreditAllowed = true " +
           "AND s.currentBalance > s.creditLimit")
    Long countSuppliersExceedingCreditLimit();
    
    /**
     * Get supplier type distribution
     */
    @Query("SELECT s.supplierType, COUNT(s) as supplierCount FROM Supplier s " +
           "WHERE s.isActive = true GROUP BY s.supplierType ORDER BY supplierCount DESC")
    List<Object[]> getSupplierTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT s.status, COUNT(s) as supplierCount FROM Supplier s " +
           "GROUP BY s.status ORDER BY supplierCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get suppliers by country
     */
    @Query("SELECT s.country, COUNT(s) as supplierCount FROM Supplier s " +
           "WHERE s.isActive = true GROUP BY s.country ORDER BY supplierCount DESC")
    List<Object[]> getSuppliersByCountry();
    
    /**
     * Get suppliers by city
     */
    @Query("SELECT s.city, COUNT(s) as supplierCount FROM Supplier s " +
           "WHERE s.isActive = true AND s.city IS NOT NULL " +
           "GROUP BY s.city ORDER BY supplierCount DESC")
    List<Object[]> getSuppliersByCity();
    
    /**
     * Get payment terms distribution
     */
    @Query("SELECT s.paymentTerms, COUNT(s) as supplierCount FROM Supplier s " +
           "WHERE s.isActive = true AND s.paymentTerms IS NOT NULL " +
           "GROUP BY s.paymentTerms ORDER BY supplierCount DESC")
    List<Object[]> getPaymentTermsDistribution();
    
    /**
     * Get total outstanding balance
     */
    @Query("SELECT SUM(s.currentBalance) FROM Supplier s WHERE s.currentBalance > 0")
    Double getTotalOutstandingBalance();
    
    /**
     * Get total credit limit
     */
    @Query("SELECT SUM(s.creditLimit) FROM Supplier s WHERE s.isCreditAllowed = true")
    Double getTotalCreditLimit();
    
    /**
     * Get average credit limit
     */
    @Query("SELECT AVG(s.creditLimit) FROM Supplier s WHERE s.isCreditAllowed = true")
    Double getAverageCreditLimit();
    
    /**
     * Get average rating
     */
    @Query("SELECT AVG(s.rating) FROM Supplier s WHERE s.rating IS NOT NULL AND s.isActive = true")
    Double getAverageRating();
    
    /**
     * Get average current balance
     */
    @Query("SELECT AVG(s.currentBalance) FROM Supplier s WHERE s.currentBalance > 0")
    Double getAverageCurrentBalance();
    
    /**
     * Get suppliers with highest balance
     */
    @Query("SELECT s FROM Supplier s WHERE s.currentBalance > 0 " +
           "ORDER BY s.currentBalance DESC")
    List<Supplier> getSuppliersWithHighestBalance(Pageable pageable);
    
    /**
     * Get suppliers with highest credit limit
     */
    @Query("SELECT s FROM Supplier s WHERE s.isCreditAllowed = true " +
           "ORDER BY s.creditLimit DESC")
    List<Supplier> getSuppliersWithHighestCreditLimit(Pageable pageable);
    
    /**
     * Get top rated suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.rating IS NOT NULL AND s.isActive = true " +
           "ORDER BY s.rating DESC, s.supplierName ASC")
    List<Supplier> getTopRatedSuppliers(Pageable pageable);
}
