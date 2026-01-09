package lk.epicgreen.erp.accounting.repository;

import lk.epicgreen.erp.accounting.entity.BankAccount;
import lk.epicgreen.erp.accounting.entity.BankReconciliation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for BankReconciliation entity
 * Based on ACTUAL database schema: bank_reconciliations table
 * 
 * Fields: reconciliation_number, bank_account_id (BIGINT), statement_date,
 *         statement_balance, book_balance, reconciled_balance,
 *         status (ENUM: DRAFT, IN_PROGRESS, COMPLETED),
 *         reconciled_by (BIGINT), reconciled_at, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface BankReconciliationRepository extends JpaRepository<BankReconciliation, Long>, JpaSpecificationExecutor<BankReconciliation> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find bank reconciliation by reconciliation number
     */
    Optional<BankReconciliation> findByReconciliationNumber(String reconciliationNumber);
    
    /**
     * Find all reconciliations for a bank account
     */
    List<BankReconciliation> findByBankAccountId(Long bankAccountId);
    
    /**
     * Find all reconciliations for a bank account with pagination
     */
    Page<BankReconciliation> findByBankAccountId(Long bankAccountId, Pageable pageable);
    
    /**
     * Find reconciliations by status
     */
    List<BankReconciliation> findByStatus(String status);

    List<BankReconciliation> findByIsReconciled(Boolean value);
    
    /**
     * Find reconciliations by status with pagination
     */
    Page<BankReconciliation> findByStatus(String status, Pageable pageable);
    
    /**
     * Find reconciliations by statement date
     */
    List<BankReconciliation> findByStatementDate(LocalDate statementDate);
    
    /**
     * Find reconciliations by statement date range
     */
    List<BankReconciliation> findByStatementDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find reconciliations by statement date range with pagination
     */
    Page<BankReconciliation> findByStatementDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find reconciliations by reconciled by user
     */
    List<BankReconciliation> findByReconciledBy(Long reconciledBy);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if reconciliation number exists
     */
    boolean existsByReconciliationNumber(String reconciliationNumber);
    
    /**
     * Check if reconciliation number exists excluding specific reconciliation ID
     */
    boolean existsByReconciliationNumberAndIdNot(String reconciliationNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search reconciliations by reconciliation number containing (case-insensitive)
     */
    Page<BankReconciliation> findByReconciliationNumberContainingIgnoreCase(String reconciliationNumber, Pageable pageable);
    
    /**
     * Search reconciliations by multiple criteria
     */
    @Query("SELECT br FROM BankReconciliation br WHERE " +
           "(:reconciliationNumber IS NULL OR LOWER(br.reconciliationNumber) LIKE LOWER(CONCAT('%', :reconciliationNumber, '%'))) AND " +
           "(:bankAccountId IS NULL OR br.bankAccount.id = :bankAccountId) AND " +
           "(:status IS NULL OR br.status = :status) AND " +
           "(:startDate IS NULL OR br.statementDate >= :startDate) AND " +
           "(:endDate IS NULL OR br.statementDate <= :endDate)")
    Page<BankReconciliation> searchReconciliations(
            @Param("reconciliationNumber") String reconciliationNumber,
            @Param("bankAccountId") Long bankAccountId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count reconciliations by status
     */
    long countByStatus(String status);
    
    /**
     * Count reconciliations by bank account
     */
    long countByBankAccountId(Long bankAccountId);
    
    /**
     * Count reconciliations in date range
     */
    long countByStatementDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find draft reconciliations
     */
    @Query("SELECT br FROM BankReconciliation br WHERE br.status = 'DRAFT' " +
           "ORDER BY br.statementDate DESC")
    List<BankReconciliation> findDraftReconciliations();
    
    /**
     * Find in-progress reconciliations
     */
    @Query("SELECT br FROM BankReconciliation br WHERE br.status = 'IN_PROGRESS' " +
           "ORDER BY br.statementDate")
    List<BankReconciliation> findInProgressReconciliations();
    
    /**
     * Find completed reconciliations
     */
    @Query("SELECT br FROM BankReconciliation br WHERE br.status = 'COMPLETED' " +
           "ORDER BY br.statementDate DESC")
    List<BankReconciliation> findCompletedReconciliations();
    
    /**
     * Find latest reconciliation for a bank account
     */
    @Query("SELECT br FROM BankReconciliation br WHERE br.bankAccount.id = :bankAccountId " +
           "ORDER BY br.statementDate DESC, br.reconciledAt DESC LIMIT 1")
    Optional<BankReconciliation> findLatestReconciliationByBankAccount(@Param("bankAccountId") Long bankAccountId);
    
    /**
     * Find reconciliations by bank account and status
     */
    List<BankReconciliation> findByBankAccountIdAndStatus(Long bankAccountId, String status);
    
    /**
     * Find reconciliations with differences
     */
    @Query("SELECT br FROM BankReconciliation br WHERE br.statementBalance <> br.bookBalance " +
           "ORDER BY br.statementDate DESC")
    List<BankReconciliation> findReconciliationsWithDifferences();
    
    /**
     * Find reconciliations reconciled in time range
     */
    @Query("SELECT br FROM BankReconciliation br WHERE br.reconciledAt BETWEEN :startTime AND :endTime " +
           "ORDER BY br.reconciledAt DESC")
    List<BankReconciliation> findReconciliationsReconciledBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * Get total statement balance for bank account
     */
    @Query("SELECT br.statementBalance FROM BankReconciliation br " +
           "WHERE br.bankAccount.id = :bankAccountId AND br.status = 'COMPLETED' " +
           "ORDER BY br.statementDate DESC LIMIT 1")
    BigDecimal getLatestStatementBalance(@Param("bankAccountId") Long bankAccountId);
    
    /**
     * Get reconciliation statistics
     */
    @Query("SELECT " +
           "COUNT(br) as totalReconciliations, " +
           "SUM(CASE WHEN br.status = 'DRAFT' THEN 1 ELSE 0 END) as draftReconciliations, " +
           "SUM(CASE WHEN br.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) as inProgressReconciliations, " +
           "SUM(CASE WHEN br.status = 'COMPLETED' THEN 1 ELSE 0 END) as completedReconciliations " +
           "FROM BankReconciliation br")
    Object getReconciliationStatistics();
    
    /**
     * Get reconciliations grouped by status
     */
    @Query("SELECT br.status, COUNT(br) as reconciliationCount " +
           "FROM BankReconciliation br GROUP BY br.status ORDER BY reconciliationCount DESC")
    List<Object[]> getReconciliationsByStatus();
    
    /**
     * Get reconciliations grouped by bank account
     */
    @Query("SELECT br.bankAccount.id, COUNT(br) as reconciliationCount, " +
           "SUM(CASE WHEN br.status = 'COMPLETED' THEN 1 ELSE 0 END) as completedCount " +
           "FROM BankReconciliation br GROUP BY br.bankAccount.id ORDER BY reconciliationCount DESC")
    List<Object[]> getReconciliationsByBankAccount();
    
    /**
     * Find all reconciliations ordered by statement date
     */
    List<BankReconciliation> findAllByOrderByStatementDateDesc();
    
    /**
     * Find completed reconciliations ordered by statement date
     */
    List<BankReconciliation> findByStatusOrderByStatementDateDesc(String status);
}
