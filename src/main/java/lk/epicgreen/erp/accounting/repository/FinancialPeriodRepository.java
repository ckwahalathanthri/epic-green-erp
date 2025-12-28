package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.FinancialPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for FinancialPeriod entity
 * Based on ACTUAL database schema: financial_periods table
 * 
 * Fields: period_code, period_name,
 *         period_type (ENUM: MONTH, QUARTER, YEAR),
 *         start_date, end_date, fiscal_year,
 *         is_closed, closed_by (BIGINT), closed_at
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface FinancialPeriodRepository extends JpaRepository<FinancialPeriod, Long>, JpaSpecificationExecutor<FinancialPeriod> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find period by period code
     */
    Optional<FinancialPeriod> findByPeriodCode(String periodCode);
    
    /**
     * Find periods by period type
     */
    List<FinancialPeriod> findByPeriodType(String periodType);
    
    /**
     * Find periods by period type with pagination
     */
    Page<FinancialPeriod> findByPeriodType(String periodType, Pageable pageable);
    
    /**
     * Find periods by fiscal year
     */
    List<FinancialPeriod> findByFiscalYear(Integer fiscalYear);
    
    /**
     * Find periods by fiscal year with pagination
     */
    Page<FinancialPeriod> findByFiscalYear(Integer fiscalYear, Pageable pageable);
    
    /**
     * Find all open periods
     */
    List<FinancialPeriod> findByIsClosedFalse();
    
    /**
     * Find all open periods with pagination
     */
    Page<FinancialPeriod> findByIsClosedFalse(Pageable pageable);
    
    /**
     * Find all closed periods
     */
    List<FinancialPeriod> findByIsClosedTrue();
    
    /**
     * Find periods by closed by user
     */
    List<FinancialPeriod> findByClosedBy(Long closedBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if period code exists
     */
    boolean existsByPeriodCode(String periodCode);
    
    /**
     * Check if period code exists excluding specific period ID
     */
    boolean existsByPeriodCodeAndIdNot(String periodCode, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search periods by period code containing (case-insensitive)
     */
    Page<FinancialPeriod> findByPeriodCodeContainingIgnoreCase(String periodCode, Pageable pageable);
    
    /**
     * Search periods by period name containing (case-insensitive)
     */
    Page<FinancialPeriod> findByPeriodNameContainingIgnoreCase(String periodName, Pageable pageable);
    
    /**
     * Search periods by multiple criteria
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE " +
           "(:periodCode IS NULL OR LOWER(fp.periodCode) LIKE LOWER(CONCAT('%', :periodCode, '%'))) AND " +
           "(:periodType IS NULL OR fp.periodType = :periodType) AND " +
           "(:fiscalYear IS NULL OR fp.fiscalYear = :fiscalYear) AND " +
           "(:isClosed IS NULL OR fp.isClosed = :isClosed)")
    Page<FinancialPeriod> searchPeriods(
            @Param("periodCode") String periodCode,
            @Param("periodType") String periodType,
            @Param("fiscalYear") Integer fiscalYear,
            @Param("isClosed") Boolean isClosed,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count open periods
     */
    long countByIsClosedFalse();
    
    /**
     * Count closed periods
     */
    long countByIsClosedTrue();
    
    /**
     * Count periods by fiscal year
     */
    long countByFiscalYear(Integer fiscalYear);
    
    /**
     * Count periods by period type
     */
    long countByPeriodType(String periodType);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find monthly periods
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE fp.periodType = 'MONTH' " +
           "ORDER BY fp.startDate DESC")
    List<FinancialPeriod> findMonthlyPeriods();
    
    /**
     * Find quarterly periods
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE fp.periodType = 'QUARTER' " +
           "ORDER BY fp.startDate DESC")
    List<FinancialPeriod> findQuarterlyPeriods();
    
    /**
     * Find yearly periods
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE fp.periodType = 'YEAR' " +
           "ORDER BY fp.startDate DESC")
    List<FinancialPeriod> findYearlyPeriods();
    
    /**
     * Find current period (containing today's date)
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE fp.startDate <= CURRENT_DATE " +
           "AND fp.endDate >= CURRENT_DATE AND fp.isClosed = false")
    Optional<FinancialPeriod> findCurrentPeriod();
    
    /**
     * Find period by date
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE fp.startDate <= :date " +
           "AND fp.endDate >= :date")
    Optional<FinancialPeriod> findPeriodByDate(@Param("date") LocalDate date);
    
    /**
     * Find periods by date range
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE " +
           "(fp.startDate BETWEEN :startDate AND :endDate) OR " +
           "(fp.endDate BETWEEN :startDate AND :endDate) OR " +
           "(fp.startDate <= :startDate AND fp.endDate >= :endDate)")
    List<FinancialPeriod> findPeriodsInDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find open periods by fiscal year
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE fp.fiscalYear = :fiscalYear " +
           "AND fp.isClosed = false ORDER BY fp.startDate")
    List<FinancialPeriod> findOpenPeriodsByFiscalYear(@Param("fiscalYear") Integer fiscalYear);
    
    /**
     * Find closed periods by fiscal year
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE fp.fiscalYear = :fiscalYear " +
           "AND fp.isClosed = true ORDER BY fp.startDate")
    List<FinancialPeriod> findClosedPeriodsByFiscalYear(@Param("fiscalYear") Integer fiscalYear);
    
    /**
     * Find periods closed in time range
     */
    @Query("SELECT fp FROM FinancialPeriod fp WHERE fp.closedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY fp.closedAt DESC")
    List<FinancialPeriod> findPeriodsClosedBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Get period statistics
     */
    @Query("SELECT " +
           "COUNT(fp) as totalPeriods, " +
           "SUM(CASE WHEN fp.isClosed = false THEN 1 ELSE 0 END) as openPeriods, " +
           "SUM(CASE WHEN fp.isClosed = true THEN 1 ELSE 0 END) as closedPeriods, " +
           "COUNT(DISTINCT fp.fiscalYear) as fiscalYears " +
           "FROM FinancialPeriod fp")
    Object getPeriodStatistics();
    
    /**
     * Get periods grouped by fiscal year
     */
    @Query("SELECT fp.fiscalYear, COUNT(fp) as periodCount, " +
           "SUM(CASE WHEN fp.isClosed = false THEN 1 ELSE 0 END) as openCount, " +
           "SUM(CASE WHEN fp.isClosed = true THEN 1 ELSE 0 END) as closedCount " +
           "FROM FinancialPeriod fp GROUP BY fp.fiscalYear ORDER BY fp.fiscalYear DESC")
    List<Object[]> getPeriodsByFiscalYear();
    
    /**
     * Get periods grouped by period type
     */
    @Query("SELECT fp.periodType, COUNT(fp) as periodCount " +
           "FROM FinancialPeriod fp GROUP BY fp.periodType ORDER BY periodCount DESC")
    List<Object[]> getPeriodsByType();
    
    /**
     * Find all fiscal years
     */
    @Query("SELECT DISTINCT fp.fiscalYear FROM FinancialPeriod fp ORDER BY fp.fiscalYear DESC")
    List<Integer> findAllFiscalYears();
    
    /**
     * Find all periods ordered by start date
     */
    List<FinancialPeriod> findAllByOrderByStartDateDesc();
    
    /**
     * Find open periods ordered by start date
     */
    List<FinancialPeriod> findByIsClosedFalseOrderByStartDateAsc();
    
    /**
     * Find periods by fiscal year and type
     */
    List<FinancialPeriod> findByFiscalYearAndPeriodType(Integer fiscalYear, String periodType);
}
