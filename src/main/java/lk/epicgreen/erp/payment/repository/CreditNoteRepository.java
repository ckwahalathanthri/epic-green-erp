package lk.epicgreen.erp.payment.repository;

import lk.epicgreen.erp.payment.entity.CreditNote;
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
 * Repository interface for CreditNote entity
 * Based on ACTUAL database schema: credit_notes table
 * 
 * Fields: credit_note_number, credit_note_date, customer_id (BIGINT),
 *         return_id (BIGINT), invoice_id (BIGINT),
 *         reason (ENUM: SALES_RETURN, DISCOUNT_ADJUSTMENT, BILLING_ERROR, GOODWILL, OTHER),
 *         subtotal, tax_amount, total_amount, utilized_amount,
 *         balance_amount (GENERATED/COMPUTED),
 *         status (ENUM: DRAFT, ISSUED, UTILIZED, EXPIRED),
 *         expiry_date, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNote, Long>, JpaSpecificationExecutor<CreditNote> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find credit note by credit note number
     */
    Optional<CreditNote> findByCreditNoteNumber(String creditNoteNumber);
    
    /**
     * Find all credit notes for a customer
     */
    List<CreditNote> findByCustomerId(Long customerId);
    
    /**
     * Find all credit notes for a customer with pagination
     */
    Page<CreditNote> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find credit notes by return
     */
    List<CreditNote> findByReturnId(Long returnId);
    
    /**
     * Find credit notes by invoice
     */
    List<CreditNote> findByInvoiceId(Long invoiceId);
    
    /**
     * Find credit notes by status
     */
    List<CreditNote> findByStatus(String status);
    
    /**
     * Find credit notes by status with pagination
     */
    Page<CreditNote> findByStatus(String status, Pageable pageable);
    
    /**
     * Find credit notes by reason
     */
    List<CreditNote> findByReason(String reason);
    
    /**
     * Find credit notes by credit note date
     */
    List<CreditNote> findByCreditNoteDate(LocalDate creditNoteDate);
    
    /**
     * Find credit notes by credit note date range
     */
    List<CreditNote> findByCreditNoteDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find credit notes by credit note date range with pagination
     */
    Page<CreditNote> findByCreditNoteDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find credit notes by expiry date
     */
    List<CreditNote> findByExpiryDate(LocalDate expiryDate);
    
    /**
     * Find credit notes by expiry date range
     */
    List<CreditNote> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if credit note number exists
     */
    boolean existsByCreditNoteNumber(String creditNoteNumber);
    
    /**
     * Check if credit note number exists excluding specific credit note ID
     */
    boolean existsByCreditNoteNumberAndIdNot(String creditNoteNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search credit notes by credit note number containing (case-insensitive)
     */
    Page<CreditNote> findByCreditNoteNumberContainingIgnoreCase(String creditNoteNumber, Pageable pageable);
    
    /**
     * Search credit notes by multiple criteria
     */
    @Query("SELECT cn FROM CreditNote cn WHERE " +
           "(:creditNoteNumber IS NULL OR LOWER(cn.creditNoteNumber) LIKE LOWER(CONCAT('%', :creditNoteNumber, '%'))) AND " +
           "(:customerId IS NULL OR cn.customerId = :customerId) AND " +
           "(:status IS NULL OR cn.status = :status) AND " +
           "(:reason IS NULL OR cn.reason = :reason) AND " +
           "(:startDate IS NULL OR cn.creditNoteDate >= :startDate) AND " +
           "(:endDate IS NULL OR cn.creditNoteDate <= :endDate)")
    Page<CreditNote> searchCreditNotes(
            @Param("creditNoteNumber") String creditNoteNumber,
            @Param("customerId") Long customerId,
            @Param("status") String status,
            @Param("reason") String reason,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count credit notes by status
     */
    long countByStatus(String status);
    
    /**
     * Count credit notes by reason
     */
    long countByReason(String reason);
    
    /**
     * Count credit notes by customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count credit notes in date range
     */
    long countByCreditNoteDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find draft credit notes
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.status = 'DRAFT' ORDER BY cn.creditNoteDate DESC")
    List<CreditNote> findDraftCreditNotes();
    
    /**
     * Find issued credit notes
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.status = 'ISSUED' ORDER BY cn.creditNoteDate DESC")
    List<CreditNote> findIssuedCreditNotes();
    
    /**
     * Find utilized credit notes
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.status = 'UTILIZED' ORDER BY cn.creditNoteDate DESC")
    List<CreditNote> findUtilizedCreditNotes();
    
    /**
     * Find expired credit notes
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.status = 'EXPIRED' ORDER BY cn.creditNoteDate DESC")
    List<CreditNote> findExpiredCreditNotes();
    
    /**
     * Find credit notes with balance
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.totalAmount > cn.utilizedAmount " +
           "AND cn.status = 'ISSUED' ORDER BY cn.creditNoteDate")
    List<CreditNote> findCreditNotesWithBalance();
    
    /**
     * Find fully utilized credit notes
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.totalAmount = cn.utilizedAmount " +
           "ORDER BY cn.creditNoteDate DESC")
    List<CreditNote> findFullyUtilizedCreditNotes();
    
    /**
     * Find credit notes expiring soon
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.expiryDate BETWEEN CURRENT_DATE AND :futureDate " +
           "AND cn.status = 'ISSUED' ORDER BY cn.expiryDate")
    List<CreditNote> findCreditNotesExpiringSoon(@Param("futureDate") LocalDate futureDate);
    
    /**
     * Find expired but unused credit notes
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.expiryDate < CURRENT_DATE " +
           "AND cn.totalAmount > cn.utilizedAmount AND cn.status = 'ISSUED'")
    List<CreditNote> findExpiredUnusedCreditNotes();
    
    /**
     * Find credit notes by customer and status
     */
    List<CreditNote> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Get total credit note amount for a customer
     */
    @Query("SELECT SUM(cn.totalAmount) FROM CreditNote cn WHERE cn.customerId = :customerId " +
           "AND cn.status = 'ISSUED'")
    BigDecimal getTotalCreditNoteAmountByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get total balance amount for a customer
     */
    @Query("SELECT SUM(cn.totalAmount - cn.utilizedAmount) FROM CreditNote cn " +
           "WHERE cn.customerId = :customerId AND cn.status = 'ISSUED'")
    BigDecimal getTotalBalanceByCustomer(@Param("customerId") Long customerId);
    
    /**
     * Get credit note statistics
     */
    @Query("SELECT " +
           "COUNT(cn) as totalCreditNotes, " +
           "SUM(CASE WHEN cn.status = 'DRAFT' THEN 1 ELSE 0 END) as draftNotes, " +
           "SUM(CASE WHEN cn.status = 'ISSUED' THEN 1 ELSE 0 END) as issuedNotes, " +
           "SUM(CASE WHEN cn.status = 'UTILIZED' THEN 1 ELSE 0 END) as utilizedNotes, " +
           "SUM(CASE WHEN cn.status = 'EXPIRED' THEN 1 ELSE 0 END) as expiredNotes, " +
           "SUM(cn.totalAmount) as totalAmount, " +
           "SUM(cn.totalAmount - cn.utilizedAmount) as totalBalance " +
           "FROM CreditNote cn")
    Object getCreditNoteStatistics();
    
    /**
     * Get credit notes grouped by status
     */
    @Query("SELECT cn.status, COUNT(cn) as noteCount, " +
           "SUM(cn.totalAmount) as totalAmount, SUM(cn.totalAmount - cn.utilizedAmount) as totalBalance " +
           "FROM CreditNote cn GROUP BY cn.status ORDER BY noteCount DESC")
    List<Object[]> getCreditNotesByStatus();
    
    /**
     * Get credit notes grouped by reason
     */
    @Query("SELECT cn.reason, COUNT(cn) as noteCount, SUM(cn.totalAmount) as totalAmount " +
           "FROM CreditNote cn GROUP BY cn.reason ORDER BY noteCount DESC")
    List<Object[]> getCreditNotesByReason();
    
    /**
     * Get credit notes grouped by customer
     */
    @Query("SELECT cn.customerId, COUNT(cn) as noteCount, " +
           "SUM(cn.totalAmount) as totalAmount, SUM(cn.totalAmount - cn.utilizedAmount) as totalBalance " +
           "FROM CreditNote cn WHERE cn.status = 'ISSUED' " +
           "GROUP BY cn.customerId ORDER BY totalAmount DESC")
    List<Object[]> getCreditNotesByCustomer();
    
    /**
     * Get daily credit note summary
     */
    @Query("SELECT cn.creditNoteDate, COUNT(cn) as noteCount, SUM(cn.totalAmount) as totalAmount " +
           "FROM CreditNote cn WHERE cn.creditNoteDate BETWEEN :startDate AND :endDate " +
           "GROUP BY cn.creditNoteDate ORDER BY cn.creditNoteDate DESC")
    List<Object[]> getDailyCreditNoteSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find today's credit notes
     */
    @Query("SELECT cn FROM CreditNote cn WHERE cn.creditNoteDate = CURRENT_DATE ORDER BY cn.createdAt DESC")
    List<CreditNote> findTodayCreditNotes();
    
    /**
     * Find all credit notes ordered by date
     */
    List<CreditNote> findAllByOrderByCreditNoteDateDescCreatedAtDesc();
    
    /**
     * Get available credit for customer
     */
    @Query("SELECT SUM(cn.totalAmount - cn.utilizedAmount) FROM CreditNote cn " +
           "WHERE cn.customerId = :customerId AND cn.status = 'ISSUED' " +
           "AND (cn.expiryDate IS NULL OR cn.expiryDate >= CURRENT_DATE)")
    BigDecimal getAvailableCreditByCustomer(@Param("customerId") Long customerId);
}
