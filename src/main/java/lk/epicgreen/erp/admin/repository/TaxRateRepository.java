package lk.epicgreen.erp.admin.repository;

import lk.epicgreen.erp.admin.entity.TaxRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for TaxRate entity (CORRECTED)
 * Matches actual database schema: tax_percentage, applicable_from, applicable_to, is_active
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long>, JpaSpecificationExecutor<TaxRate> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find tax rate by tax code
     */
    Optional<TaxRate> findByTaxCode(String taxCode);
    
    /**
     * Find tax rate by tax name
     */
    Optional<TaxRate> findByTaxName(String taxName);
    
    /**
     * Find all active tax rates
     */
    List<TaxRate> findByIsActiveTrue();
    
    /**
     * Find all active tax rates with pagination
     */
    Page<TaxRate> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all inactive tax rates
     */
    List<TaxRate> findByIsActiveFalse();
    
    /**
     * Find tax rates by tax type
     */
    List<TaxRate> findByTaxType(String taxType);
    
    /**
     * Find tax rates by tax type with pagination
     */
    Page<TaxRate> findByTaxType(String taxType, Pageable pageable);
    
    /**
     * Find active tax rates by type
     */
    List<TaxRate> findByTaxTypeAndIsActiveTrue(String taxType);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if tax code exists
     */
    boolean existsByTaxCode(String taxCode);
    
    /**
     * Check if tax name exists
     */
    boolean existsByTaxName(String taxName);
    
    /**
     * Check if tax code exists excluding specific tax rate ID
     */
    boolean existsByTaxCodeAndIdNot(String taxCode, Long id);
    
    /**
     * Check if tax name exists excluding specific tax rate ID
     */
    boolean existsByTaxNameAndIdNot(String taxName, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search tax rates by tax code containing (case-insensitive)
     */
    Page<TaxRate> findByTaxCodeContainingIgnoreCase(String taxCode, Pageable pageable);
    
    /**
     * Search tax rates by tax name containing (case-insensitive)
     */
    Page<TaxRate> findByTaxNameContainingIgnoreCase(String taxName, Pageable pageable);
    
    /**
     * Search active tax rates by keyword
     */
    @Query("SELECT t FROM TaxRate t WHERE t.isActive = true AND " +
           "(LOWER(t.taxCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.taxName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<TaxRate> searchActiveTaxRates(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Search tax rates by multiple criteria
     */
    @Query("SELECT t FROM TaxRate t WHERE " +
           "(:taxCode IS NULL OR LOWER(t.taxCode) LIKE LOWER(CONCAT('%', :taxCode, '%'))) AND " +
           "(:taxName IS NULL OR LOWER(t.taxName) LIKE LOWER(CONCAT('%', :taxName, '%'))) AND " +
           "(:taxType IS NULL OR t.taxType = :taxType) AND " +
           "(:isActive IS NULL OR t.isActive = :isActive)")
    Page<TaxRate> searchTaxRates(
            @Param("taxCode") String taxCode,
            @Param("taxName") String taxName,
            @Param("taxType") String taxType,
            @Param("isActive") Boolean isActive,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count active tax rates
     */
    long countByIsActiveTrue();
    
    /**
     * Count inactive tax rates
     */
    long countByIsActiveFalse();
    
    /**
     * Count tax rates by type
     */
    long countByTaxType(String taxType);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find tax rates applicable on a specific date
     */
    @Query("SELECT t FROM TaxRate t WHERE t.applicableFrom <= :date AND " +
           "(t.applicableTo IS NULL OR t.applicableTo >= :date) AND t.isActive = true")
    List<TaxRate> findApplicableTaxRatesOnDate(@Param("date") LocalDate date);
    
    /**
     * Find current applicable tax rates
     */
    @Query("SELECT t FROM TaxRate t WHERE t.applicableFrom <= CURRENT_DATE AND " +
           "(t.applicableTo IS NULL OR t.applicableTo >= CURRENT_DATE) AND t.isActive = true")
    List<TaxRate> findCurrentApplicableTaxRates();
    
    /**
     * Find tax rate applicable on date by tax code
     */
    @Query("SELECT t FROM TaxRate t WHERE t.taxCode = :taxCode AND " +
           "t.applicableFrom <= :date AND " +
           "(t.applicableTo IS NULL OR t.applicableTo >= :date) AND t.isActive = true")
    Optional<TaxRate> findApplicableTaxRateByCodeOnDate(
            @Param("taxCode") String taxCode, 
            @Param("date") LocalDate date);
    
    /**
     * Find current applicable tax rate by tax code
     */
    @Query("SELECT t FROM TaxRate t WHERE t.taxCode = :taxCode AND " +
           "t.applicableFrom <= CURRENT_DATE AND " +
           "(t.applicableTo IS NULL OR t.applicableTo >= CURRENT_DATE) AND t.isActive = true")
    Optional<TaxRate> findCurrentApplicableTaxRateByCode(@Param("taxCode") String taxCode);
    
    /**
     * Find tax rates by percentage
     */
    List<TaxRate> findByTaxPercentage(BigDecimal taxPercentage);
    
    /**
     * Find tax rates by percentage range
     */
    @Query("SELECT t FROM TaxRate t WHERE t.taxPercentage BETWEEN :minRate AND :maxRate AND t.isActive = true")
    List<TaxRate> findByTaxPercentageRange(
            @Param("minRate") BigDecimal minRate, 
            @Param("maxRate") BigDecimal maxRate);
    
    /**
     * Find expired tax rates (applicableTo < current date)
     */
    @Query("SELECT t FROM TaxRate t WHERE t.applicableTo < CURRENT_DATE")
    List<TaxRate> findExpiredTaxRates();
    
    /**
     * Find future tax rates (applicableFrom > current date)
     */
    @Query("SELECT t FROM TaxRate t WHERE t.applicableFrom > CURRENT_DATE")
    List<TaxRate> findFutureTaxRates();
    
    /**
     * Get tax rate statistics
     */
    @Query("SELECT " +
           "COUNT(t) as totalTaxRates, " +
           "SUM(CASE WHEN t.isActive = true THEN 1 ELSE 0 END) as activeTaxRates, " +
           "AVG(t.taxPercentage) as averageRate, " +
           "MAX(t.taxPercentage) as maxRate, " +
           "MIN(t.taxPercentage) as minRate " +
           "FROM TaxRate t")
    Object getTaxRateStatistics();
    
    /**
     * Get tax rates grouped by type
     */
    @Query("SELECT t.taxType, COUNT(t) as taxCount, AVG(t.taxPercentage) as avgRate " +
           "FROM TaxRate t WHERE t.isActive = true GROUP BY t.taxType")
    List<Object[]> getTaxRatesByType();
    
    /**
     * Find overlapping tax rates for same tax code
     */
    @Query("SELECT t FROM TaxRate t WHERE t.taxCode = :taxCode AND t.id != :excludeId AND " +
           "((t.applicableFrom <= :applicableTo AND (t.applicableTo IS NULL OR t.applicableTo >= :applicableFrom)))")
    List<TaxRate> findOverlappingTaxRates(
            @Param("taxCode") String taxCode,
            @Param("applicableFrom") LocalDate applicableFrom,
            @Param("applicableTo") LocalDate applicableTo,
            @Param("excludeId") Long excludeId);
    
    /**
     * Find all tax rates ordered by tax code
     */
    List<TaxRate> findAllByOrderByTaxCodeAsc();
    
    /**
     * Find active tax rates ordered by percentage
     */
    List<TaxRate> findByIsActiveTrueOrderByTaxPercentageDesc();
    
    /**
     * Find tax rates with no end date (applicableTo IS NULL)
     */
    @Query("SELECT t FROM TaxRate t WHERE t.applicableTo IS NULL AND t.isActive = true")
    List<TaxRate> findTaxRatesWithNoEndDate();
}
