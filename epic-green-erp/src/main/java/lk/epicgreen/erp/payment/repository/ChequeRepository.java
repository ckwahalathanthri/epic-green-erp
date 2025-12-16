package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.Cheque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Cheque Repository
 * Repository for cheque data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ChequeRepository extends JpaRepository<Cheque, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find cheque by cheque number
     */
    Optional<Cheque> findByChequeNumber(String chequeNumber);
    
    /**
     * Check if cheque exists by cheque number
     */
    boolean existsByChequeNumber(String chequeNumber);
    
    /**
     * Find cheque by cheque number and bank
     */
    Optional<Cheque> findByChequeNumberAndBankName(String chequeNumber, String bankName);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find cheques by payment ID
     */
    List<Cheque> findByPaymentId(Long paymentId);
    
    /**
     * Find cheques by customer ID
     */
    List<Cheque> findByCustomerId(Long customerId);
    
    /**
     * Find cheques by customer ID with pagination
     */
    Page<Cheque> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find cheques by bank name
     */
    List<Cheque> findByBankName(String bankName);
    
    /**
     * Find cheques by bank name with pagination
     */
    Page<Cheque> findByBankName(String bankName, Pageable pageable);
    
    /**
     * Find cheques by branch name
     */
    List<Cheque> findByBranchName(String branchName);
    
    /**
     * Find cheques by cheque status
     */
    List<Cheque> findByChequeStatus(String chequeStatus);
    
    /**
     * Find cheques by cheque status with pagination
     */
    Page<Cheque> findByChequeStatus(String chequeStatus, Pageable pageable);
    
    /**
     * Find cheques by cheque type
     */
    List<Cheque> findByChequeType(String chequeType);
    
    /**
     * Find cheques by is post dated
     */
    List<Cheque> findByIsPostDated(Boolean isPostDated);
    
    /**
     * Find cheques by is cleared
     */
    List<Cheque> findByIsCleared(Boolean isCleared);
    
    /**
     * Find cheques by is bounced
     */
    List<Cheque> findByIsBounced(Boolean isBounced);
    
    /**
     * Find cheques by is cancelled
     */
    List<Cheque> findByIsCancelled(Boolean isCancelled);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find cheques by cheque date between dates
     */
    List<Cheque> findByChequeDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find cheques by cheque date between dates with pagination
     */
    Page<Cheque> findByChequeDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find cheques by presented date between dates
     */
    List<Cheque> findByPresentedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find cheques by cleared date between dates
     */
    List<Cheque> findByClearedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find cheques by bounced date between dates
     */
    List<Cheque> findByBouncedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find cheques by created at between dates
     */
    List<Cheque> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find cheques by customer ID and cheque status
     */
    List<Cheque> findByCustomerIdAndChequeStatus(Long customerId, String chequeStatus);
    
    /**
     * Find cheques by customer ID and cheque status with pagination
     */
    Page<Cheque> findByCustomerIdAndChequeStatus(Long customerId, String chequeStatus, Pageable pageable);
    
    /**
     * Find cheques by bank name and cheque status
     */
    List<Cheque> findByBankNameAndChequeStatus(String bankName, String chequeStatus);
    
    /**
     * Find cheques by is post dated and cheque status
     */
    List<Cheque> findByIsPostDatedAndChequeStatus(Boolean isPostDated, String chequeStatus);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search cheques
     */
    @Query("SELECT c FROM Cheque c WHERE " +
           "LOWER(c.chequeNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.bankName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Cheque> searchCheques(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find pending cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeStatus = 'PENDING' " +
           "ORDER BY c.chequeDate DESC")
    List<Cheque> findPendingCheques();
    
    /**
     * Find pending cheques with pagination
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeStatus = 'PENDING' " +
           "ORDER BY c.chequeDate DESC")
    Page<Cheque> findPendingCheques(Pageable pageable);
    
    /**
     * Find presented cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeStatus = 'PRESENTED' " +
           "ORDER BY c.presentedDate DESC")
    List<Cheque> findPresentedCheques();
    
    /**
     * Find cleared cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeStatus = 'CLEARED' AND c.isCleared = true " +
           "ORDER BY c.clearedDate DESC")
    List<Cheque> findClearedCheques();
    
    /**
     * Find bounced cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeStatus = 'BOUNCED' AND c.isBounced = true " +
           "ORDER BY c.bouncedDate DESC")
    List<Cheque> findBouncedCheques();
    
    /**
     * Find cancelled cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.isCancelled = true " +
           "ORDER BY c.cancelledDate DESC")
    List<Cheque> findCancelledCheques();
    
    /**
     * Find post dated cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.isPostDated = true " +
           "AND c.chequeDate > :currentDate " +
           "ORDER BY c.chequeDate ASC")
    List<Cheque> findPostDatedCheques(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find cheques due for presentation
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeStatus = 'PENDING' " +
           "AND c.chequeDate <= :currentDate " +
           "AND c.isPostDated = false " +
           "ORDER BY c.chequeDate ASC")
    List<Cheque> findChequesDueForPresentation(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find overdue cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeStatus = 'PRESENTED' " +
           "AND c.presentedDate < :thresholdDate " +
           "AND c.isCleared = false " +
           "ORDER BY c.presentedDate ASC")
    List<Cheque> findOverdueCheques(@Param("thresholdDate") LocalDate thresholdDate);
    
    /**
     * Find recent cheques
     */
    @Query("SELECT c FROM Cheque c ORDER BY c.chequeDate DESC, c.createdAt DESC")
    List<Cheque> findRecentCheques(Pageable pageable);
    
    /**
     * Find customer recent cheques
     */
    @Query("SELECT c FROM Cheque c WHERE c.customerId = :customerId " +
           "ORDER BY c.chequeDate DESC, c.createdAt DESC")
    List<Cheque> findCustomerRecentCheques(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Find cheques by date range and status
     */
    @Query("SELECT c FROM Cheque c WHERE c.chequeDate BETWEEN :startDate AND :endDate " +
           "AND c.chequeStatus = :status ORDER BY c.chequeDate DESC")
    List<Cheque> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate,
                                          @Param("status") String status);
    
    /**
     * Find cheques requiring action
     */
    @Query("SELECT c FROM Cheque c WHERE " +
           "(c.chequeStatus = 'PENDING' AND c.chequeDate <= :currentDate) OR " +
           "(c.chequeStatus = 'PRESENTED' AND c.presentedDate < :overdueDate AND c.isCleared = false) " +
           "ORDER BY c.chequeDate ASC")
    List<Cheque> findChequesRequiringAction(@Param("currentDate") LocalDate currentDate,
                                           @Param("overdueDate") LocalDate overdueDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count cheques by customer
     */
    @Query("SELECT COUNT(c) FROM Cheque c WHERE c.customerId = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Count cheques by bank
     */
    @Query("SELECT COUNT(c) FROM Cheque c WHERE c.bankName = :bankName")
    Long countByBankName(@Param("bankName") String bankName);
    
    /**
     * Count cheques by status
     */
    @Query("SELECT COUNT(c) FROM Cheque c WHERE c.chequeStatus = :status")
    Long countByChequeStatus(@Param("status") String status);
    
    /**
     * Count pending cheques
     */
    @Query("SELECT COUNT(c) FROM Cheque c WHERE c.chequeStatus = 'PENDING'")
    Long countPendingCheques();
    
    /**
     * Count cleared cheques
     */
    @Query("SELECT COUNT(c) FROM Cheque c WHERE c.isCleared = true")
    Long countClearedCheques();
    
    /**
     * Count bounced cheques
     */
    @Query("SELECT COUNT(c) FROM Cheque c WHERE c.isBounced = true")
    Long countBouncedCheques();
    
    /**
     * Count post dated cheques
     */
    @Query("SELECT COUNT(c) FROM Cheque c WHERE c.isPostDated = true " +
           "AND c.chequeDate > :currentDate")
    Long countPostDatedCheques(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Get cheque status distribution
     */
    @Query("SELECT c.chequeStatus, COUNT(c) as chequeCount FROM Cheque c " +
           "GROUP BY c.chequeStatus ORDER BY chequeCount DESC")
    List<Object[]> getChequeStatusDistribution();
    
    /**
     * Get bank distribution
     */
    @Query("SELECT c.bankName, COUNT(c) as chequeCount FROM Cheque c " +
           "WHERE c.bankName IS NOT NULL " +
           "GROUP BY c.bankName ORDER BY chequeCount DESC")
    List<Object[]> getBankDistribution();
    
    /**
     * Get monthly cheque count
     */
    @Query("SELECT YEAR(c.chequeDate) as year, MONTH(c.chequeDate) as month, " +
           "COUNT(c) as chequeCount FROM Cheque c " +
           "WHERE c.chequeDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(c.chequeDate), MONTH(c.chequeDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyChequeCount(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);
    
    /**
     * Get total cheque amount
     */
    @Query("SELECT SUM(c.chequeAmount) FROM Cheque c WHERE c.isCleared = true")
    Double getTotalChequeAmount();
    
    /**
     * Get total cheque amount by bank
     */
    @Query("SELECT c.bankName, SUM(c.chequeAmount) as totalAmount FROM Cheque c " +
           "WHERE c.isCleared = true AND c.bankName IS NOT NULL " +
           "GROUP BY c.bankName ORDER BY totalAmount DESC")
    List<Object[]> getTotalChequeAmountByBank();
    
    /**
     * Get average cheque amount
     */
    @Query("SELECT AVG(c.chequeAmount) FROM Cheque c WHERE c.isCleared = true")
    Double getAverageChequeAmount();
    
    /**
     * Get cheque bounce rate
     */
    @Query("SELECT " +
           "(SELECT COUNT(c) FROM Cheque c WHERE c.isBounced = true) * 100.0 / COUNT(c) " +
           "FROM Cheque c WHERE c.chequeStatus IN ('CLEARED', 'BOUNCED')")
    Double getChequeBounceRate();
    
    /**
     * Find cheques by tags
     */
    @Query("SELECT c FROM Cheque c WHERE c.tags LIKE CONCAT('%', :tag, '%')")
    List<Cheque> findByTag(@Param("tag") String tag);
}
