package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository interface for Supplier entity
 * Based on ACTUAL database schema: suppliers table
 * 
 * Fields: supplier_code, supplier_name, supplier_type (ENUM: RAW_MATERIAL, PACKAGING, SERVICES, OTHER),
 *         contact_person, email, phone, mobile, tax_id, payment_terms,
 *         credit_limit, credit_days,
 *         address_line1, address_line2, city, state, country, postal_code,
 *         bank_name, bank_account_number, bank_branch,
 *         rating, is_active, deleted_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {

    // ==================== FINDER METHODS ====================

    /**
     * Find supplier by supplier code
     */
    Optional<Supplier> findBySupplierCode(String supplierCode);

    /**
     * Find supplier by supplier name
     */
    Optional<Supplier> findBySupplierName(String supplierName);

    /**
     * Find supplier by email
     */
    Optional<Supplier> findByEmail(String email);

    /**
     * Find supplier by mobile
     */
    Optional<Supplier> findByMobile(String mobile);

    /**
     * Find all active suppliers
     */
    List<Supplier> findByIsActiveTrue();

    /**
     * Find all active suppliers with pagination
     */
    Page<Supplier> findByIsActiveTrue(Pageable pageable);

    /**
     * Find all inactive suppliers
     */
    List<Supplier> findByIsActiveFalse();

    /**
     * Find suppliers by supplier type
     */
    List<Supplier> findBySupplierType(String supplierType);

    /**
     * Find suppliers by supplier type with pagination
     */
    Page<Supplier> findBySupplierType(String supplierType, Pageable pageable);

    /**
     * Find active suppliers by type
     */
    List<Supplier> findBySupplierTypeAndIsActiveTrue(String supplierType);

    /**
     * Find suppliers by city
     */
    List<Supplier> findByCity(String city);

    /**
     * Find suppliers by state
     */
    List<Supplier> findByState(String state);

    /**
     * Find suppliers by country
     */
    List<Supplier> findByCountry(String country);

    // ==================== EXISTENCE CHECKS ====================

    /**
     * Check if supplier code exists
     */
    boolean existsBySupplierCode(String supplierCode);

    /**
     * Check if supplier name exists
     */
    boolean existsBySupplierName(String supplierName);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if supplier code exists excluding specific supplier ID
     */
    boolean existsBySupplierCodeAndIdNot(String supplierCode, Long id);

    /**
     * Check if email exists excluding specific supplier ID
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    // ==================== SEARCH METHODS ====================

    /**
     * Search suppliers by supplier code containing (case-insensitive)
     */
    Page<Supplier> findBySupplierCodeContainingIgnoreCase(String supplierCode, Pageable pageable);

    /**
     * Search suppliers by supplier name containing (case-insensitive)
     */
    Page<Supplier> findBySupplierNameContainingIgnoreCase(String supplierName, Pageable pageable);

    /**
     * Search active suppliers by keyword
     */
    @Query("SELECT s FROM Supplier s WHERE s.isActive = true AND " +
            "(LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.supplierName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.mobile) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Supplier> searchActiveSuppliers(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Search suppliers by multiple criteria
     */
    @Query("SELECT s FROM Supplier s WHERE " +
            "(:supplierCode IS NULL OR LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :supplierCode, '%'))) AND " +
            "(:supplierName IS NULL OR LOWER(s.supplierName) LIKE LOWER(CONCAT('%', :supplierName, '%'))) AND " +
            "(:supplierType IS NULL OR s.supplierType = :supplierType) AND " +
            "(:city IS NULL OR s.city = :city) AND " +
            "(:isActive IS NULL OR s.isActive = :isActive)")
    Page<Supplier> searchSuppliers(
            @Param("supplierCode") String supplierCode,
            @Param("supplierName") String supplierName,
            @Param("supplierType") String supplierType,
            @Param("city") String city,
            @Param("isActive") Boolean isActive,
            Pageable pageable);

    // ==================== COUNT METHODS ====================

    /**
     * Count active suppliers
     */
    long countByIsActiveTrue();

    /**
     * Count inactive suppliers
     */
    long countByIsActiveFalse();

    /**
     * Count suppliers by type
     */
    long countBySupplierType(String supplierType);

    /**
     * Count suppliers by city
     */
    long countByCity(String city);

    // ==================== CUSTOM QUERIES ====================

    /**
     * Find raw material suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.supplierType = 'RAW_MATERIAL' AND s.isActive = true")
    List<Supplier> findRawMaterialSuppliers();

    /**
     * Find packaging suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.supplierType = 'PACKAGING' AND s.isActive = true")
    List<Supplier> findPackagingSuppliers();

    /**
     * Find service suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.supplierType = 'SERVICES' AND s.isActive = true")
    List<Supplier> findServiceSuppliers();

    /**
     * Find other type suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.supplierType = 'OTHER' AND s.isActive = true")
    List<Supplier> findOtherSuppliers();

    /**
     * Find suppliers by rating range
     */
    @Query("SELECT s FROM Supplier s WHERE s.rating BETWEEN :minRating AND :maxRating AND s.isActive = true")
    List<Supplier> findByRatingRange(
            @Param("minRating") BigDecimal minRating,
            @Param("maxRating") BigDecimal maxRating);

    /**
     * Find suppliers with rating greater than threshold
     */
    @Query("SELECT s FROM Supplier s WHERE s.rating >= :rating AND s.isActive = true ORDER BY s.rating DESC")
    List<Supplier> findByRatingGreaterThanEqual(@Param("rating") BigDecimal rating);

    /**
     * Find suppliers by credit limit range
     */
    @Query("SELECT s FROM Supplier s WHERE s.creditLimit BETWEEN :minLimit AND :maxLimit AND s.isActive = true")
    List<Supplier> findByCreditLimitRange(
            @Param("minLimit") BigDecimal minLimit,
            @Param("maxLimit") BigDecimal maxLimit);

    /**
     * Find all distinct cities
     */
    @Query("SELECT DISTINCT s.city FROM Supplier s WHERE s.city IS NOT NULL ORDER BY s.city")
    List<String> findAllDistinctCities();

    /**
     * Find all distinct states
     */
    @Query("SELECT DISTINCT s.state FROM Supplier s WHERE s.state IS NOT NULL ORDER BY s.state")
    List<String> findAllDistinctStates();

    /**
     * Find all distinct countries
     */
    @Query("SELECT DISTINCT s.country FROM Supplier s WHERE s.country IS NOT NULL ORDER BY s.country")
    List<String> findAllDistinctCountries();

    /**
     * Get supplier statistics
     */
    @Query("SELECT " +
            "COUNT(s) as totalSuppliers, " +
            "SUM(CASE WHEN s.isActive = true THEN 1 ELSE 0 END) as activeSuppliers, " +
            "SUM(CASE WHEN s.supplierType = 'RAW_MATERIAL' THEN 1 ELSE 0 END) as rawMaterialSuppliers, " +
            "SUM(CASE WHEN s.supplierType = 'PACKAGING' THEN 1 ELSE 0 END) as packagingSuppliers, " +
            "SUM(CASE WHEN s.supplierType = 'SERVICES' THEN 1 ELSE 0 END) as serviceSuppliers, " +
            "SUM(CASE WHEN s.supplierType = 'OTHER' THEN 1 ELSE 0 END) as otherSuppliers, " +
            "AVG(s.rating) as avgRating " +
            "FROM Supplier s")
    Object getSupplierStatistics();

    /**
     * Get suppliers grouped by type
     */
    @Query("SELECT s.supplierType, COUNT(s) as supplierCount, AVG(s.rating) as avgRating " +
            "FROM Supplier s WHERE s.isActive = true GROUP BY s.supplierType")
    List<Object[]> getSuppliersByType();

    /**
     * Get suppliers grouped by city
     */
    @Query("SELECT s.city, COUNT(s) as supplierCount " +
            "FROM Supplier s WHERE s.isActive = true AND s.city IS NOT NULL " +
            "GROUP BY s.city ORDER BY supplierCount DESC")
    List<Object[]> getSuppliersByCity();

    /**
     * Get suppliers grouped by country
     */
    @Query("SELECT s.country, COUNT(s) as supplierCount " +
            "FROM Supplier s WHERE s.isActive = true AND s.country IS NOT NULL " +
            "GROUP BY s.country ORDER BY supplierCount DESC")
    List<Object[]> getSuppliersByCountry();

    /**
     * Find suppliers with bank details
     */
    @Query("SELECT s FROM Supplier s WHERE s.bankName IS NOT NULL AND s.bankAccountNumber IS NOT NULL " +
            "AND s.isActive = true")
    List<Supplier> findSuppliersWithBankDetails();

    /**
     * Find suppliers without bank details
     */
    @Query("SELECT s FROM Supplier s WHERE (s.bankName IS NULL OR s.bankAccountNumber IS NULL) " +
            "AND s.isActive = true")
    List<Supplier> findSuppliersWithoutBankDetails();

    /**
     * Find top rated suppliers
     */
    @Query("SELECT s FROM Supplier s WHERE s.isActive = true AND s.rating > 0 ORDER BY s.rating DESC")
    List<Supplier> findTopRatedSuppliers(Pageable pageable);

    /**
     * Find suppliers by type and city
     */
    List<Supplier> findBySupplierTypeAndCityAndIsActiveTrue(String supplierType, String city);

    /**
     * Find suppliers by type and rating minimum
     */
    @Query("SELECT s FROM Supplier s WHERE s.supplierType = :supplierType AND s.rating >= :minRating " +
            "AND s.isActive = true ORDER BY s.rating DESC")
    List<Supplier> findByTypeAndMinRating(
            @Param("supplierType") String supplierType,
            @Param("minRating") BigDecimal minRating);

    /**
     * Find all suppliers ordered by code
     */
    List<Supplier> findAllByOrderBySupplierCodeAsc();

    /**
     * Find active suppliers ordered by name
     */
    List<Supplier> findByIsActiveTrueOrderBySupplierNameAsc();

    /**
     * Find active suppliers ordered by rating
     */
    List<Supplier> findByIsActiveTrueOrderByRatingDesc();

    @Query("SELECT s FROM Supplier s WHERE s.isActive = true AND s.deletedAt IS NULL")
    List<Supplier> findByIsActiveTrueAndDeletedAtIsNull();

    @Query("SELECT s FROM Supplier s WHERE s.isApproved = true AND s.deletedAt IS NULL")
    List<Supplier> findByIsApprovedTrueAndDeletedAtIsNull();

    @Query("SELECT s FROM Supplier s WHERE s.isApproved = false AND s.deletedAt IS NULL")
    List<Supplier> findByIsApprovedFalseAndDeletedAtIsNull();

    @Query("SELECT s FROM Supplier s WHERE s.isApproved = false AND s.rejectionReason IS NOT NULL AND s.deletedAt IS NULL")
    List<Supplier> findByIsApprovedFalseAndRejectionReasonIsNotNullAndDeletedAtIsNull();

    @Query("SELECT s FROM Supplier s WHERE s.status = :status AND s.deletedAt IS NULL")
    List<Supplier> findByStatus(String status);

    @Query("SELECT s FROM Supplier s WHERE s.isBlocked = true AND s.deletedAt IS NULL")
    List<Supplier> findByIsBlockedTrueAndDeletedAtIsNull();

    @Query("SELECT s FROM Supplier s WHERE s.country = :country AND s.isActive = true AND s.deletedAt IS NULL")
    List<Supplier> findByCountryAndIsActiveTrueAndDeletedAtIsNull(String country);

    @Query("SELECT s FROM Supplier s WHERE s.country <> :country AND s.isActive = true AND s.deletedAt IS NULL")
    List<Supplier> findByCountryNotAndIsActiveTrueAndDeletedAtIsNull(String country);

    @Query("SELECT s FROM Supplier s WHERE s.creditDays > :i AND s.isActive = true AND s.deletedAt IS NULL")
    List<Supplier> findByCreditDaysGreaterThanAndIsActiveTrueAndDeletedAtIsNull(int i);

    @Query
    List<Supplier> findByCurrentBalanceBetweenAndDeletedAtIsNull(BigDecimal bigDecimal, BigDecimal bigDecimal1);

    @Query("SELECT s FROM Supplier s WHERE s.rating>:rating")
    List<Supplier> findByRatingIsLargerThan(@Param("rating") double rating);

    @Query("SELECT s FROM Supplier s WHERE s.rating <= :rating AND s.deletedAt IS NULL")
    List<Supplier> findByRatingLessThanEqualAndDeletedAtIsNull(BigDecimal bigDecimal);

    @Query("SELECT s FROM Supplier s WHERE s.region = :region AND s.deletedAt IS NULL")
    List<Supplier> findByRegionAndDeletedAtIsNull(String region);

    @Query("SELECT s FROM Supplier s WHERE s.deletedAt IS NULL ORDER BY s.createdAt DESC")
    Page<Supplier> findByDeletedAtIsNullOrderByCreatedAtDesc(Pageable limit);

    @Query("SELECT s FROM Supplier s WHERE s.deletedAt IS NULL ORDER BY s.updatedAt DESC")
    Page<Supplier> findByDeletedAtIsNullOrderByUpdatedAtDesc(Pageable limit);

    @Query("SELECT s FROM Supplier s WHERE s.deletedAt IS NULL ORDER BY s.rating ASC")
    Page<Supplier> findByDeletedAtIsNullOrderByRatingDesc(Pageable limit);

    @Query("SELECT s FROM Supplier s WHERE s.deletedAt IS NULL ORDER BY s.balance DESC")
    Page<Supplier> findByDeletedAtIsNullOrderByCurrentBalanceDesc(Pageable limit);

    @Query("SELECT s FROM Supplier s WHERE s.deletedAt IS NULL ORDER BY s.creditLimit DESC")
    Page<Supplier> findByDeletedAtIsNullOrderByCreditLimitDesc(Pageable limit);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Supplier s WHERE s.taxNumber = :taxNumber")
    boolean existsByTaxNumber(String taxNumber);

    @Query("SELECT SUM(s.currentBalance) FROM Supplier s WHERE s.deletedAt IS NULL")
    Double getTotalOutstandingBalance();

    @Query("SELECT SUM(s.creditLimit) FROM Supplier s WHERE s.deletedAt IS NULL")
    Double getTotalCreditLimit();
@Query("SELECT AVG(s.creditLimit) FROM Supplier s WHERE s.deletedAt IS NULL")
    Double getAverageCreditLimit();

@Query("SELECT AVG(s.rating) FROM Supplier s WHERE s.deletedAt IS NULL")
    Double getAverageRating();

@Query("SELECT AVG(s.currentBalance) FROM Supplier s WHERE s.deletedAt IS NULL")
    Double getAverageCurrentBalance();

@Query("SELECT " +
            "s.supplierCode AS supplierCode, " +
            "s.supplierName AS supplierName, " +
            "s.supplierType AS supplierType, " +
            "s.contactPerson AS contactPerson, " +
            "s.email AS email, " +
            "s.phone AS phone, " +
            "s.mobile AS mobile, " +
            "s.taxNumber AS taxNumber, " +
            "s.paymentTerms AS paymentTerms, " +
            "s.creditLimit AS creditLimit, " +
            "s.creditDays AS creditDays, " +
            "s.addressLine1 AS addressLine1, " +
            "s.addressLine2 AS addressLine2, " +
            "s.city AS city, " +
            "s.state AS state, " +
            "s.country AS country, " +
            "s.postalCode AS postalCode, " +
            "s.bankName AS bankName, " +
            "s.bankAccountNumber AS bankAccountNumber, " +
            "s.bankBranch AS bankBranch, " +
            "s.rating AS rating, " +
            "s.isActive AS isActive " +
            "FROM Supplier s WHERE s.id = :id AND s.deletedAt IS NULL")
    Object getSupplierSummary(Long id);

@Query("SELECT " +
            "COUNT(s) AS totalSuppliers, " +
            "SUM(CASE WHEN s.isActive = true THEN 1 ELSE 0 END) AS activeSuppliers, " +
            "SUM(CASE WHEN s.isApproved = true THEN 1 ELSE 0 END) AS approvedSuppliers, " +
            "SUM(CASE WHEN s.isBlocked = true THEN 1 ELSE 0 END) AS blockedSuppliers, " +
            "AVG(s.rating) AS averageRating, " +
            "SUM(s.currentBalance) AS totalOutstandingBalance, " +
            "SUM(s.creditLimit) AS totalCreditLimit " +
            "FROM Supplier s WHERE s.deletedAt IS NULL")
    Object getDashboardStatistics();

@Query("SELECT COUNT(s) FROM Supplier s WHERE s.status = :status AND s.deletedAt IS NULL")
    Long countSuppliersByStatus(@Param("status") String status);

@Query("SELECT COUNT(s) FROM Supplier s WHERE s.isActive = true AND s.deletedAt IS NULL")
    Long countByIsActiveTrueAndDeletedAtIsNull();

@Query("SELECT COUNT(s) FROM Supplier s WHERE s.isActive = false AND s.deletedAt IS NULL")
    Long countByDeletedAtIsNull();
@Query("SELECT s FROM Supplier s WHERE s.id = :id AND s.deletedAt IS NULL")
    Supplier findByIdAndDeletedAtIsNull(Long id);

@Query("SELECT s FROM Supplier s WHERE s.supplierCode = :supplierCode AND s.deletedAt IS NULL")
    <T> Optional<T> findBySupplierCodeAndDeletedAtIsNull(@Param("supplierCode") String supplierCode);

@Query("SELECT s FROM Supplier s WHERE s.supplierType = :packaging AND s.isActive = true AND s.deletedAt IS NULL")
    List<Supplier> findBySupplierTypeAndIsActiveTrueAndDeletedAtIsNull(String packaging);

@Query("SELECT s FROM Supplier s WHERE s.currentBalance > 0 AND s.deletedAt IS NULL")
    List<Supplier> findSuppliersWithOutstandingBalance();

@Query("SELECT s FROM Supplier s WHERE s.currentBalance > s.creditLimit AND s.deletedAt IS NULL")
    List<Supplier> findSuppliersExceedingCreditLimit();

@Query("SELECT s FROM Supplier s WHERE s.supplierType = :supplierType AND s.deletedAt IS NULL")
    Page<Supplier> findBySupplierTypeAndDeletedAtIsNull(String supplierType, Pageable pageable);

@Query("SELECT s FROM Supplier s WHERE s.deletedAt IS NULL")
    Page<Supplier> findByDeletedAtIsNull(Pageable pageable);

//@Query
//    List<Map<String, Object>> getTopSuppliersByOrders(Pageable limit);
}


