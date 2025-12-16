package lk.epicgreen.erp.returns.repository;

import lk.epicgreen.erp.returns.entity.SalesReturn;
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
 * SalesReturn Repository
 * Repository for sales return data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SalesReturnRepository extends JpaRepository<SalesReturn, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find sales return by return number
     */
    Optional<SalesReturn> findByReturnNumber(String returnNumber);
    
    /**
     * Check if sales return exists by return number
     */
    boolean existsByReturnNumber(String returnNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find sales returns by customer ID
     */
    List<SalesReturn> findByCustomerId(Long customerId);
    
    /**
     * Find sales returns by customer ID with pagination
     */
    Page<SalesReturn> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find sales returns by sales order ID
     */
    List<SalesReturn> findBySalesOrderId(Long salesOrderId);
    
    /**
     * Find sales returns by invoice ID
     */
    List<SalesReturn> findByInvoiceId(Long invoiceId);
    
    /**
     * Find sales returns by return type
     */
    List<SalesReturn> findByReturnType(String returnType);
    
    /**
     * Find sales returns by return type with pagination
     */
    Page<SalesReturn> findByReturnType(String returnType, Pageable pageable);
    
    /**
     * Find sales returns by return reason
     */
    List<SalesReturn> findByReturnReason(String returnReason);
    
    /**
     * Find sales returns by status
     */
    List<SalesReturn> findByStatus(String status);
    
    /**
     * Find sales returns by status with pagination
     */
    Page<SalesReturn> findByStatus(String status, Pageable pageable);
    
    /**
     * Find sales returns by warehouse ID
     */
    List<SalesReturn> findByWarehouseId(Long warehouseId);
    
    /**
     * Find sales returns by created by user
     */
    List<SalesReturn> findByCreatedByUserId(Long userId);
    
    /**
     * Find sales returns by approved by user
     */
    List<SalesReturn> findByApprovedByUserId(Long userId);
    
    /**
     * Find sales returns by inspected by user
     */
    List<SalesReturn> findByInspectedByUserId(Long userId);
    
    /**
     * Find sales returns by is approved
     */
    List<SalesReturn> findByIsApproved(Boolean isApproved);
    
    /**
     * Find sales returns by is credit note generated
     */
    List<SalesReturn> findByIsCreditNoteGenerated(Boolean isCreditNoteGenerated);
    
    /**
     * Find sales returns by credit note ID
     */
    List<SalesReturn> findByCreditNoteId(Long creditNoteId);
    
    /**
     * Find sales returns by is inspected
     */
    List<SalesReturn> findByIsInspected(Boolean isInspected);
    
    /**
     * Find sales returns by inspection status
     */
    List<SalesReturn> findByInspectionStatus(String inspectionStatus);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find sales returns by return date between dates
     */
    List<SalesReturn> findByReturnDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find sales returns by return date between dates with pagination
     */
    Page<SalesReturn> findByReturnDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find sales returns by received date between dates
     */
    List<SalesReturn> findByReceivedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find sales returns by approved date between dates
     */
    List<SalesReturn> findByApprovedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find sales returns by inspection date between dates
     */
    List<SalesReturn> findByInspectionDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find sales returns by created at between dates
     */
    List<SalesReturn> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find sales returns by customer ID and status
     */
    List<SalesReturn> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find sales returns by customer ID and status with pagination
     */
    Page<SalesReturn> findByCustomerIdAndStatus(Long customerId, String status, Pageable pageable);
    
    /**
     * Find sales returns by return type and status
     */
    List<SalesReturn> findByReturnTypeAndStatus(String returnType, String status);
    
    /**
     * Find sales returns by warehouse ID and status
     */
    List<SalesReturn> findByWarehouseIdAndStatus(Long warehouseId, String status);
    
    /**
     * Find sales returns by is approved and status
     */
    List<SalesReturn> findByIsApprovedAndStatus(Boolean isApproved, String status);
    
    /**
     * Find sales returns by is inspected and status
     */
    List<SalesReturn> findByIsInspectedAndStatus(Boolean isInspected, String status);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search sales returns
     */
    @Query("SELECT s FROM SalesReturn s WHERE " +
           "LOWER(s.returnNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SalesReturn> searchSalesReturns(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find pending sales returns
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.status = 'PENDING' " +
           "ORDER BY s.returnDate DESC")
    List<SalesReturn> findPendingReturns();
    
    /**
     * Find pending sales returns with pagination
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.status = 'PENDING' " +
           "ORDER BY s.returnDate DESC")
    Page<SalesReturn> findPendingReturns(Pageable pageable);
    
    /**
     * Find approved sales returns
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.status = 'APPROVED' AND s.isApproved = true " +
           "ORDER BY s.approvedDate DESC")
    List<SalesReturn> findApprovedReturns();
    
    /**
     * Find rejected sales returns
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.status = 'REJECTED' " +
           "ORDER BY s.returnDate DESC")
    List<SalesReturn> findRejectedReturns();
    
    /**
     * Find completed sales returns
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.status = 'COMPLETED' " +
           "ORDER BY s.returnDate DESC")
    List<SalesReturn> findCompletedReturns();
    
    /**
     * Find returns pending approval
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.isApproved = false " +
           "AND s.status NOT IN ('REJECTED', 'CANCELLED') " +
           "ORDER BY s.returnDate ASC")
    List<SalesReturn> findReturnsPendingApproval();
    
    /**
     * Find returns pending inspection
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.isInspected = false " +
           "AND s.isApproved = true AND s.status = 'APPROVED' " +
           "ORDER BY s.approvedDate ASC")
    List<SalesReturn> findReturnsPendingInspection();
    
    /**
     * Find returns without credit note
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.isCreditNoteGenerated = false " +
           "AND s.isApproved = true AND s.status = 'APPROVED' " +
           "ORDER BY s.approvedDate ASC")
    List<SalesReturn> findReturnsWithoutCreditNote();
    
    /**
     * Find recent sales returns
     */
    @Query("SELECT s FROM SalesReturn s ORDER BY s.returnDate DESC, s.createdAt DESC")
    List<SalesReturn> findRecentReturns(Pageable pageable);
    
    /**
     * Find customer recent sales returns
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.customerId = :customerId " +
           "ORDER BY s.returnDate DESC, s.createdAt DESC")
    List<SalesReturn> findCustomerRecentReturns(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Find returns by date range and status
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.returnDate BETWEEN :startDate AND :endDate " +
           "AND s.status = :status ORDER BY s.returnDate DESC")
    List<SalesReturn> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate,
                                               @Param("status") String status);
    
    /**
     * Find returns requiring quality check
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.requiresQualityCheck = true " +
           "AND s.inspectionStatus = 'PENDING' " +
           "ORDER BY s.returnDate ASC")
    List<SalesReturn> findReturnsRequiringQualityCheck();
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count sales returns by customer
     */
    @Query("SELECT COUNT(s) FROM SalesReturn s WHERE s.customerId = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Count sales returns by return type
     */
    @Query("SELECT COUNT(s) FROM SalesReturn s WHERE s.returnType = :returnType")
    Long countByReturnType(@Param("returnType") String returnType);
    
    /**
     * Count sales returns by status
     */
    @Query("SELECT COUNT(s) FROM SalesReturn s WHERE s.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending returns
     */
    @Query("SELECT COUNT(s) FROM SalesReturn s WHERE s.status = 'PENDING'")
    Long countPendingReturns();
    
    /**
     * Count approved returns
     */
    @Query("SELECT COUNT(s) FROM SalesReturn s WHERE s.isApproved = true")
    Long countApprovedReturns();
    
    /**
     * Count returns pending approval
     */
    @Query("SELECT COUNT(s) FROM SalesReturn s WHERE s.isApproved = false " +
           "AND s.status NOT IN ('REJECTED', 'CANCELLED')")
    Long countReturnsPendingApproval();
    
    /**
     * Count returns without credit note
     */
    @Query("SELECT COUNT(s) FROM SalesReturn s WHERE s.isCreditNoteGenerated = false " +
           "AND s.isApproved = true")
    Long countReturnsWithoutCreditNote();
    
    /**
     * Get return type distribution
     */
    @Query("SELECT s.returnType, COUNT(s) as returnCount FROM SalesReturn s " +
           "GROUP BY s.returnType ORDER BY returnCount DESC")
    List<Object[]> getReturnTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT s.status, COUNT(s) as returnCount FROM SalesReturn s " +
           "GROUP BY s.status ORDER BY returnCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get return reason distribution
     */
    @Query("SELECT s.returnReason, COUNT(s) as returnCount FROM SalesReturn s " +
           "WHERE s.returnReason IS NOT NULL " +
           "GROUP BY s.returnReason ORDER BY returnCount DESC")
    List<Object[]> getReturnReasonDistribution();
    
    /**
     * Get monthly return count
     */
    @Query("SELECT YEAR(s.returnDate) as year, MONTH(s.returnDate) as month, COUNT(s) as returnCount " +
           "FROM SalesReturn s WHERE s.returnDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(s.returnDate), MONTH(s.returnDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyReturnCount(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);
    
    /**
     * Get total return amount by customer
     */
    @Query("SELECT s.customerId, s.customerName, SUM(s.totalAmount) as totalAmount " +
           "FROM SalesReturn s WHERE s.isApproved = true " +
           "GROUP BY s.customerId, s.customerName ORDER BY totalAmount DESC")
    List<Object[]> getTotalReturnAmountByCustomer();
    
    /**
     * Get average return amount
     */
    @Query("SELECT AVG(s.totalAmount) FROM SalesReturn s WHERE s.isApproved = true")
    Double getAverageReturnAmount();
    
    /**
     * Get total return value
     */
    @Query("SELECT SUM(s.totalAmount) FROM SalesReturn s WHERE s.isApproved = true")
    Double getTotalReturnValue();
    
    /**
     * Get return rate by customer
     */
    @Query("SELECT s.customerId, s.customerName, " +
           "COUNT(s) as returnCount, SUM(s.totalAmount) as totalAmount " +
           "FROM SalesReturn s WHERE s.returnDate BETWEEN :startDate AND :endDate " +
           "GROUP BY s.customerId, s.customerName ORDER BY returnCount DESC")
    List<Object[]> getReturnRateByCustomer(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
    
    /**
     * Find returns by tags
     */
    @Query("SELECT s FROM SalesReturn s WHERE s.tags LIKE CONCAT('%', :tag, '%')")
    List<SalesReturn> findByTag(@Param("tag") String tag);
}
