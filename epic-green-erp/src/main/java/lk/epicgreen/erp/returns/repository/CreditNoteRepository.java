package lk.epicgreen.erp.returns.repository;

import lk.epicgreen.erp.returns.entity.CreditNote;
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
 * CreditNote Repository
 * Repository for credit note data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNote, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find credit note by credit note number
     */
    Optional<CreditNote> findByCreditNoteNumber(String creditNoteNumber);
    
    /**
     * Check if credit note exists by credit note number
     */
    boolean existsByCreditNoteNumber(String creditNoteNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find credit notes by customer ID
     */
    List<CreditNote> findByCustomerId(Long customerId);
    
    /**
     * Find credit notes by customer ID with pagination
     */
    Page<CreditNote> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find credit notes by sales return ID
     */
    List<CreditNote> findBySalesReturnId(Long salesReturnId);
    
    /**
     * Find credit notes by original invoice ID
     */
    List<CreditNote> findByOriginalInvoiceId(Long invoiceId);
    
    /**
     * Find credit notes by credit note type
     */
    List<CreditNote> findByCreditNoteType(String creditNoteType);
    
    /**
     * Find credit notes by credit note type with pagination
     */
    Page<CreditNote> findByCreditNoteType(String creditNoteType, Pageable pageable);
    
    /**
     * Find credit notes by status
     */
    List<CreditNote> findByStatus(String status);
    
    /**
     * Find credit notes by status with pagination
     */
    Page<CreditNote> findByStatus(String status, Pageable pageable);
    
    /**
     * Find credit notes by created by user
     */
    List<CreditNote> findByCreatedByUserId(Long userId);
    
    /**
     * Find credit notes by approved by user
     */
    List<CreditNote> findByApprovedByUserId(Long userId);
    
    /**
     * Find credit notes by is approved
     */
    List<CreditNote> findByIsApproved(Boolean isApproved);
    
    /**
     * Find credit notes by is applied
     */
    List<CreditNote> findByIsApplied(Boolean isApplied);
    
    /**
     * Find credit notes by is refunded
     */
    List<CreditNote> findByIsRefunded(Boolean isRefunded);
    
    /**
     * Find credit notes by payment status
     */
    List<CreditNote> findByPaymentStatus(String paymentStatus);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find credit notes by credit note date between dates
     */
    List<CreditNote> findByCreditNoteDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find credit notes by credit note date between dates with pagination
     */
    Page<CreditNote> findByCreditNoteDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find credit notes by approved date between dates
     */
    List<CreditNote> findByApprovedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find credit notes by applied date between dates
     */
    List<CreditNote> findByAppliedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find credit notes by expiry date between dates
     */
    List<CreditNote> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find credit notes by created at between dates
     */
    List<CreditNote> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find credit notes by customer ID and status
     */
    List<CreditNote> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find credit notes by customer ID and status with pagination
     */
    Page<CreditNote> findByCustomerIdAndStatus(Long customerId, String status, Pageable pageable);
    
    /**
     * Find credit notes by credit note type and status
     */
    List<CreditNote> findByCreditNoteTypeAndStatus(String creditNoteType, String status);
    
    /**
     * Find credit notes by is approved and status
     */
    List<CreditNote> findByIsApprovedAndStatus(Boolean isApproved, String status);
    
    /**
     * Find credit notes by is applied and status
     */
    List<CreditNote> findByIsAppliedAndStatus(Boolean isApplied, String status);
    
    /**
     * Find credit notes by payment status and status
     */
    List<CreditNote> findByPaymentStatusAndStatus(String paymentStatus, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE " +
           "LOWER(c.creditNoteNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<CreditNote> searchCreditNotes(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find pending credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.status = 'PENDING' " +
           "ORDER BY c.creditNoteDate DESC")
    List<CreditNote> findPendingCreditNotes();
    
    /**
     * Find pending credit notes with pagination
     */
    @Query("SELECT c FROM CreditNote c WHERE c.status = 'PENDING' " +
           "ORDER BY c.creditNoteDate DESC")
    Page<CreditNote> findPendingCreditNotes(Pageable pageable);
    
    /**
     * Find approved credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.status = 'APPROVED' AND c.isApproved = true " +
           "ORDER BY c.approvedDate DESC")
    List<CreditNote> findApprovedCreditNotes();
    
    /**
     * Find rejected credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.status = 'REJECTED' " +
           "ORDER BY c.creditNoteDate DESC")
    List<CreditNote> findRejectedCreditNotes();
    
    /**
     * Find applied credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.isApplied = true " +
           "ORDER BY c.appliedDate DESC")
    List<CreditNote> findAppliedCreditNotes();
    
    /**
     * Find unapplied credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.isApplied = false " +
           "AND c.isApproved = true AND c.status = 'APPROVED' " +
           "ORDER BY c.approvedDate ASC")
    List<CreditNote> findUnappliedCreditNotes();
    
    /**
     * Find credit notes pending approval
     */
    @Query("SELECT c FROM CreditNote c WHERE c.isApproved = false " +
           "AND c.status NOT IN ('REJECTED', 'CANCELLED') " +
           "ORDER BY c.creditNoteDate ASC")
    List<CreditNote> findCreditNotesPendingApproval();
    
    /**
     * Find refunded credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.isRefunded = true " +
           "ORDER BY c.refundDate DESC")
    List<CreditNote> findRefundedCreditNotes();
    
    /**
     * Find credit notes pending refund
     */
    @Query("SELECT c FROM CreditNote c WHERE c.isRefunded = false " +
           "AND c.paymentStatus = 'PENDING_REFUND' " +
           "ORDER BY c.creditNoteDate ASC")
    List<CreditNote> findCreditNotesPendingRefund();
    
    /**
     * Find recent credit notes
     */
    @Query("SELECT c FROM CreditNote c ORDER BY c.creditNoteDate DESC, c.createdAt DESC")
    List<CreditNote> findRecentCreditNotes(Pageable pageable);
    
    /**
     * Find customer recent credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.customerId = :customerId " +
           "ORDER BY c.creditNoteDate DESC, c.createdAt DESC")
    List<CreditNote> findCustomerRecentCreditNotes(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Find credit notes by date range and status
     */
    @Query("SELECT c FROM CreditNote c WHERE c.creditNoteDate BETWEEN :startDate AND :endDate " +
           "AND c.status = :status ORDER BY c.creditNoteDate DESC")
    List<CreditNote> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate,
                                              @Param("status") String status);
    
    /**
     * Find expiring credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.expiryDate BETWEEN :startDate AND :endDate " +
           "AND c.isApplied = false AND c.isApproved = true " +
           "ORDER BY c.expiryDate ASC")
    List<CreditNote> findExpiringCreditNotes(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);
    
    /**
     * Find expired credit notes
     */
    @Query("SELECT c FROM CreditNote c WHERE c.expiryDate < :currentDate " +
           "AND c.isApplied = false " +
           "ORDER BY c.expiryDate DESC")
    List<CreditNote> findExpiredCreditNotes(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find available credit notes for customer
     */
    @Query("SELECT c FROM CreditNote c WHERE c.customerId = :customerId " +
           "AND c.isApproved = true AND c.isApplied = false " +
           "AND c.remainingAmount > 0 " +
           "AND (c.expiryDate IS NULL OR c.expiryDate >= :currentDate) " +
           "ORDER BY c.creditNoteDate ASC")
    List<CreditNote> findAvailableCreditNotesForCustomer(@Param("customerId") Long customerId,
                                                         @Param("currentDate") LocalDate currentDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count credit notes by customer
     */
    @Query("SELECT COUNT(c) FROM CreditNote c WHERE c.customerId = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Count credit notes by credit note type
     */
    @Query("SELECT COUNT(c) FROM CreditNote c WHERE c.creditNoteType = :creditNoteType")
    Long countByCreditNoteType(@Param("creditNoteType") String creditNoteType);
    
    /**
     * Count credit notes by status
     */
    @Query("SELECT COUNT(c) FROM CreditNote c WHERE c.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending credit notes
     */
    @Query("SELECT COUNT(c) FROM CreditNote c WHERE c.status = 'PENDING'")
    Long countPendingCreditNotes();
    
    /**
     * Count approved credit notes
     */
    @Query("SELECT COUNT(c) FROM CreditNote c WHERE c.isApproved = true")
    Long countApprovedCreditNotes();
    
    /**
     * Count unapplied credit notes
     */
    @Query("SELECT COUNT(c) FROM CreditNote c WHERE c.isApplied = false " +
           "AND c.isApproved = true")
    Long countUnappliedCreditNotes();
    
    /**
     * Count credit notes pending approval
     */
    @Query("SELECT COUNT(c) FROM CreditNote c WHERE c.isApproved = false " +
           "AND c.status NOT IN ('REJECTED', 'CANCELLED')")
    Long countCreditNotesPendingApproval();
    
    /**
     * Get credit note type distribution
     */
    @Query("SELECT c.creditNoteType, COUNT(c) as creditNoteCount FROM CreditNote c " +
           "GROUP BY c.creditNoteType ORDER BY creditNoteCount DESC")
    List<Object[]> getCreditNoteTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT c.status, COUNT(c) as creditNoteCount FROM CreditNote c " +
           "GROUP BY c.status ORDER BY creditNoteCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get payment status distribution
     */
    @Query("SELECT c.paymentStatus, COUNT(c) as creditNoteCount FROM CreditNote c " +
           "WHERE c.paymentStatus IS NOT NULL " +
           "GROUP BY c.paymentStatus ORDER BY creditNoteCount DESC")
    List<Object[]> getPaymentStatusDistribution();
    
    /**
     * Get monthly credit note count
     */
    @Query("SELECT YEAR(c.creditNoteDate) as year, MONTH(c.creditNoteDate) as month, " +
           "COUNT(c) as creditNoteCount FROM CreditNote c " +
           "WHERE c.creditNoteDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(c.creditNoteDate), MONTH(c.creditNoteDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyCreditNoteCount(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);
    
    /**
     * Get total credit note amount by customer
     */
    @Query("SELECT c.customerId, c.customerName, SUM(c.totalAmount) as totalAmount " +
           "FROM CreditNote c WHERE c.isApproved = true " +
           "GROUP BY c.customerId, c.customerName ORDER BY totalAmount DESC")
    List<Object[]> getTotalCreditNoteAmountByCustomer();
    
    /**
     * Get total credit note value
     */
    @Query("SELECT SUM(c.totalAmount) FROM CreditNote c WHERE c.isApproved = true")
    Double getTotalCreditNoteValue();
    
    /**
     * Get total remaining credit
     */
    @Query("SELECT SUM(c.remainingAmount) FROM CreditNote c WHERE c.isApproved = true " +
           "AND c.isApplied = false")
    Double getTotalRemainingCredit();
    
    /**
     * Get total applied credit
     */
    @Query("SELECT SUM(c.appliedAmount) FROM CreditNote c WHERE c.isApplied = true")
    Double getTotalAppliedCredit();
    
    /**
     * Get average credit note amount
     */
    @Query("SELECT AVG(c.totalAmount) FROM CreditNote c WHERE c.isApproved = true")
    Double getAverageCreditNoteAmount();
    
    /**
     * Get customer credit summary
     */
    @Query("SELECT c.customerId, c.customerName, " +
           "COUNT(c) as creditNoteCount, SUM(c.totalAmount) as totalAmount, " +
           "SUM(c.remainingAmount) as remainingAmount " +
           "FROM CreditNote c WHERE c.isApproved = true " +
           "GROUP BY c.customerId, c.customerName ORDER BY totalAmount DESC")
    List<Object[]> getCustomerCreditSummary();
    
    /**
     * Find credit notes by tags
     */
    @Query("SELECT c FROM CreditNote c WHERE c.tags LIKE CONCAT('%', :tag, '%')")
    List<CreditNote> findByTag(@Param("tag") String tag);
}
