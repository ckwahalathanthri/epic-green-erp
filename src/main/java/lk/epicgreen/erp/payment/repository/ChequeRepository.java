package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.Cheque;
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
 * Repository interface for Cheque entity
 * Based on ACTUAL database schema: cheques table
 * 
 * Fields: payment_id (BIGINT), cheque_number, cheque_date, cheque_amount,
 *         bank_name, bank_branch, account_number, customer_id (BIGINT),
 *         status (ENUM: RECEIVED, DEPOSITED, CLEARED, BOUNCED, RETURNED, CANCELLED),
 *         deposit_date, clearance_date, bounce_reason, bounce_charges, remarks
 * 
 * NOTE: This tracks Post-Dated Cheques (PDC) and cheque lifecycle management
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ChequeRepository extends JpaRepository<Cheque, Long>, JpaSpecificationExecutor<Cheque> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find cheque by cheque number
     */
    Optional<Cheque> findByChequeNumber(String chequeNumber);
    
    /**
     * Find all cheques for a payment
     */
    List<Cheque> findByPaymentId(Long paymentId);
    
    /**
     * Find all cheques for a customer
     */
    List<Cheque> findByCustomerId(Long customerId);
    
    /**
     * Find all cheques for a customer with pagination
     */
    Page<Cheque> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find cheques by status
     */
    List<Cheque> findByStatus(String status);
    
    /**
     * Find cheques by status with pagination
     */
    Page<Cheque> findByStatus(String status, Pageable pageable);
    
    /**
     * Find cheques by bank name
     */
    List<Cheque> findByBankName(String bankName);
    
    /**
     * Find cheques by cheque date
     */
    List<Cheque> findByChequeDate(LocalDate chequeDate);
    
    /**
     * Find cheques by cheque date range
     */
    List<Cheque> findByChequeDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find cheques by cheque date range with pagination
     */
    Page<Cheque> findByChequeDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find cheques by clearance date
     */
    List<Cheque> findByClearanceDate(LocalDate clearanceDate);
    
    /**
     * Find cheques by clearance date range
     */
    List<Cheque> findByClearanceDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if cheque number exists
     */
    boolean existsByChequeNumber(String chequeNumber);
    
    /**
     * Check if cheque number exists excluding specific cheque ID
     */
    boolean existsByChequeNumberAndIdNot(String chequeNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search cheques by cheque number containing (case-insensitive)
     */
    Page<Cheque> findByChequeNumberContainingIgnoreCase(String chequeNumber, Pageable pageable);
    
    /**
     * Search cheques by bank name containing (case-insensitive)
     */
    Page<Cheque> findByBankNameContainingIgnoreCase(String bankName, Pageable pageable);
    
    /**
     * Search cheques by multiple criteria
     */
    @Query("SELECT c FROM Cheque c WHERE " +
           "(:chequeNumber IS NULL OR LOWER(c.chequeNumber) LIKE LOWER(CONCAT('%', :chequeNumber, '%'))) AND " +
           "(:customerId IS NULL OR c.customerId = :customerId) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:bankName IS NULL OR LOWER(c.bankName) LIKE LOWER(CONCAT('%', :bankName, '%'))) AND " +
           "(:startDate IS NULL OR c.chequeDate >= :startDate) AND " +
           "(:endDate IS NULL OR c.chequeDate <= :endDate)")
    Page<Cheque> searchCheques(
            @Param("chequeNumber") String chequeNumber,
            @Param("customerId") Long customerId,
            @Param("status") String status,
            @Param("bankName") String bankName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count cheques by status
     */
    long countByStatus(String status);
    
    /**
     * Count cheques by customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count cheques in date range
     */
    long countByChequeDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find received cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.status = 'RECEIVED' ORDER BY c.chequeDate")
    List<Cheque> findReceivedCheques();
    
    /**
     * Find deposited cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.status = 'DEPOSITED' ORDER BY c.depositDate")
    List<Cheque> findDepositedCheques();
    
    /**
     * Find cleared cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.status = 'CLEARED' ORDER BY c.clearanceDate DESC")
    List<Cheque> findClearedCheques();
    
    /**
     * Find bounced cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.status = 'BOUNCED' ORDER BY c.chequeDate DESC")
    List<Cheque> findBouncedCheques();
    
    /**
     * Find returned cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.status = 'RETURNED' ORDER BY c.chequeDate DESC")
    List<Cheque> findReturnedCheques();
    
    /**
     * Find cancelled cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.status = 'CANCELLED' ORDER BY c.chequeDate DESC")
    List<Cheque> findCancelledCheques();
    
    /**
     * Find post-dated cheques (PDC)
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeDate > CURRENT_DATE AND c.status IN ('RECEIVED', 'DEPOSITED') " +
           "ORDER BY c.chequeDate")
    List<Cheque> findPostDatedCheques();
    
    /**
     * Find cheques due for deposit
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeDate <= :date AND c.status = 'RECEIVED' " +
           "ORDER BY c.chequeDate")
    List<Cheque> findChequesDueForDeposit(@Param("date") LocalDate date);
    
    /**
     * Find cheques due for clearance
     */
    @Query("SELECT c FROM Cheque c WHERE c.depositDate <= :date AND c.status = 'DEPOSITED' " +
           "ORDER BY c.depositDate")
    List<Cheque> findChequesDueForClearance(@Param("date") LocalDate date);
    
    /**
     * Find cheques by customer and status
     */
    List<Cheque> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Get total cheque amount by customer
     */
    @Query("SELECT SUM(c.chequeAmount) FROM Cheque c WHERE c.customerId = :customerId " +
           "AND c.status = 'CLEARED'")
    BigDecimal getTotalChequeAmountByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total bounced cheque amount by customer
     */
    @Query("SELECT SUM(c.chequeAmount) FROM Cheque c WHERE c.customerId = :customerId " +
           "AND c.status = 'BOUNCED'")
    BigDecimal getTotalBouncedAmountByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get cheque statistics
     */
    @Query("SELECT " +
           "COUNT(c) as totalCheques, " +
           "SUM(CASE WHEN c.status = 'RECEIVED' THEN 1 ELSE 0 END) as receivedCheques, " +
           "SUM(CASE WHEN c.status = 'DEPOSITED' THEN 1 ELSE 0 END) as depositedCheques, " +
           "SUM(CASE WHEN c.status = 'CLEARED' THEN 1 ELSE 0 END) as clearedCheques, " +
           "SUM(CASE WHEN c.status = 'BOUNCED' THEN 1 ELSE 0 END) as bouncedCheques, " +
           "SUM(c.chequeAmount) as totalAmount " +
           "FROM Cheque c")
    Object getChequeStatistics();
    
    /**
     * Get cheques grouped by status
     */
    @Query("SELECT c.status, COUNT(c) as chequeCount, SUM(c.chequeAmount) as totalAmount " +
           "FROM Cheque c GROUP BY c.status ORDER BY chequeCount DESC")
    List<Object[]> getChequesByStatus();
    
    /**
     * Get cheques grouped by bank
     */
    @Query("SELECT c.bankName, COUNT(c) as chequeCount, SUM(c.chequeAmount) as totalAmount " +
           "FROM Cheque c GROUP BY c.bankName ORDER BY chequeCount DESC")
    List<Object[]> getChequesByBank();
    
    /**
     * Get cheques grouped by customer
     */
    @Query("SELECT c.customerId, COUNT(c) as chequeCount, " +
           "SUM(c.chequeAmount) as totalAmount, " +
           "SUM(CASE WHEN c.status = 'BOUNCED' THEN 1 ELSE 0 END) as bouncedCount " +
           "FROM Cheque c GROUP BY c.customerId ORDER BY totalAmount DESC")
    List<Object[]> getChequesByCustomer();
    
    /**
     * Get daily cheque summary
     */
    @Query("SELECT c.chequeDate, COUNT(c) as chequeCount, SUM(c.chequeAmount) as totalAmount " +
           "FROM Cheque c WHERE c.chequeDate BETWEEN :startDate AND :endDate " +
           "GROUP BY c.chequeDate ORDER BY c.chequeDate DESC")
    List<Object[]> getDailyChequeSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find today's cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeDate = CURRENT_DATE ORDER BY c.createdAt DESC")
    List<Cheque> findTodayCheques();
    
    /**
     * Find all cheques ordered by date
     */
    List<Cheque> findAllByOrderByChequeDateDescCreatedAtDesc();
    
    /**
     * Get PDC (Post-Dated Cheque) report
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeDate > CURRENT_DATE " +
           "AND c.status IN ('RECEIVED', 'DEPOSITED') ORDER BY c.chequeDate")
    List<Cheque> getPDCReport();
    
    /**
     * Get cheques expiring soon (due within specified days)
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeDate BETWEEN CURRENT_DATE AND :futureDate " +
           "AND c.status IN ('RECEIVED', 'DEPOSITED') ORDER BY c.chequeDate")
    List<Cheque> getChequesExpiringSoon(@Param("futureDate") LocalDate futureDate);
}
