package lk.epicgreen.erp.purchase.repository;

import lk.epicgreen.erp.purchase.entity.PurchaseOrder;
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
 * PurchaseOrder Repository
 * Repository for purchase order data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find purchase order by order number
     */
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    
    /**
     * Check if purchase order exists by order number
     */
    boolean existsByOrderNumber(String orderNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find purchase orders by supplier ID
     */
    List<PurchaseOrder> findBySupplierId(Long supplierId);
    
    /**
     * Find purchase orders by supplier ID with pagination
     */
    Page<PurchaseOrder> findBySupplierId(Long supplierId, Pageable pageable);
    
    /**
     * Find purchase orders by warehouse ID
     */
    List<PurchaseOrder> findByWarehouseId(Long warehouseId);
    
    /**
     * Find purchase orders by status
     */
    List<PurchaseOrder> findByStatus(String status);
    
    /**
     * Find purchase orders by status with pagination
     */
    Page<PurchaseOrder> findByStatus(String status, Pageable pageable);
    
    /**
     * Find purchase orders by payment status
     */
    List<PurchaseOrder> findByPaymentStatus(String paymentStatus);
    
    /**
     * Find purchase orders by order type
     */
    List<PurchaseOrder> findByOrderType(String orderType);
    
    /**
     * Find purchase orders by priority
     */
    List<PurchaseOrder> findByPriority(String priority);
    
    /**
     * Find purchase orders by created by user
     */
    List<PurchaseOrder> findByCreatedByUserId(Long userId);
    
    /**
     * Find purchase orders by approved by user
     */
    List<PurchaseOrder> findByApprovedByUserId(Long userId);
    
    /**
     * Find purchase orders by is approved
     */
    List<PurchaseOrder> findByIsApproved(Boolean isApproved);
    
    /**
     * Find purchase orders by is received
     */
    List<PurchaseOrder> findByIsReceived(Boolean isReceived);
    
    /**
     * Find purchase orders by is paid
     */
    List<PurchaseOrder> findByIsPaid(Boolean isPaid);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find purchase orders by order date between dates
     */
    List<PurchaseOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find purchase orders by expected delivery date between dates
     */
    List<PurchaseOrder> findByExpectedDeliveryDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find purchase orders by approved date between dates
     */
    List<PurchaseOrder> findByApprovedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find purchase orders by created at between dates
     */
    List<PurchaseOrder> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find purchase orders by supplier ID and status
     */
    List<PurchaseOrder> findBySupplierIdAndStatus(Long supplierId, String status);
    
    /**
     * Find purchase orders by warehouse ID and status
     */
    List<PurchaseOrder> findByWarehouseIdAndStatus(Long warehouseId, String status);
    
    /**
     * Find purchase orders by order type and status
     */
    List<PurchaseOrder> findByOrderTypeAndStatus(String orderType, String status);
    
    /**
     * Find purchase orders by is approved and status
     */
    List<PurchaseOrder> findByIsApprovedAndStatus(Boolean isApproved, String status);
    
    /**
     * Find purchase orders by payment status and is paid
     */
    List<PurchaseOrder> findByPaymentStatusAndIsPaid(String paymentStatus, Boolean isPaid);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE " +
           "LOWER(po.orderNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(po.supplierName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(po.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PurchaseOrder> searchPurchaseOrders(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find draft purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'DRAFT' " +
           "ORDER BY po.createdAt DESC")
    List<PurchaseOrder> findDraftPurchaseOrders();
    
    /**
     * Find pending purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'PENDING' " +
           "ORDER BY po.orderDate DESC")
    List<PurchaseOrder> findPendingPurchaseOrders();
    
    /**
     * Find approved purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'APPROVED' " +
           "AND po.isApproved = true " +
           "ORDER BY po.approvedDate DESC")
    List<PurchaseOrder> findApprovedPurchaseOrders();
    
    /**
     * Find ordered purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'ORDERED' " +
           "ORDER BY po.expectedDeliveryDate ASC")
    List<PurchaseOrder> findOrderedPurchaseOrders();
    
    /**
     * Find partial received purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'PARTIAL_RECEIVED' " +
           "ORDER BY po.expectedDeliveryDate ASC")
    List<PurchaseOrder> findPartialReceivedPurchaseOrders();
    
    /**
     * Find received purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'RECEIVED' " +
           "AND po.isReceived = true " +
           "ORDER BY po.receivedDate DESC")
    List<PurchaseOrder> findReceivedPurchaseOrders();
    
    /**
     * Find cancelled purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'CANCELLED' " +
           "ORDER BY po.cancelledDate DESC")
    List<PurchaseOrder> findCancelledPurchaseOrders();
    
    /**
     * Find purchase orders pending approval
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.isApproved = false " +
           "AND po.status NOT IN ('DRAFT', 'CANCELLED') " +
           "ORDER BY po.createdAt ASC")
    List<PurchaseOrder> findPurchaseOrdersPendingApproval();
    
    /**
     * Find overdue deliveries
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.expectedDeliveryDate < :currentDate " +
           "AND po.status IN ('ORDERED', 'PARTIAL_RECEIVED') " +
           "AND po.isReceived = false " +
           "ORDER BY po.expectedDeliveryDate ASC")
    List<PurchaseOrder> findOverdueDeliveries(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find high priority purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.priority = 'HIGH' " +
           "AND po.status NOT IN ('RECEIVED', 'CANCELLED') " +
           "ORDER BY po.expectedDeliveryDate ASC")
    List<PurchaseOrder> findHighPriorityPurchaseOrders();
    
    /**
     * Find unpaid purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.isPaid = false " +
           "AND po.paymentStatus != 'PAID' " +
           "AND po.status NOT IN ('DRAFT', 'CANCELLED') " +
           "ORDER BY po.orderDate ASC")
    List<PurchaseOrder> findUnpaidPurchaseOrders();
    
    /**
     * Find partially paid purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.paymentStatus = 'PARTIAL' " +
           "AND po.paidAmount > 0 AND po.balanceAmount > 0 " +
           "ORDER BY po.orderDate ASC")
    List<PurchaseOrder> findPartiallyPaidPurchaseOrders();
    
    /**
     * Find unreceived purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.isReceived = false " +
           "AND po.status IN ('APPROVED', 'ORDERED', 'PARTIAL_RECEIVED') " +
           "ORDER BY po.expectedDeliveryDate ASC")
    List<PurchaseOrder> findUnreceivedPurchaseOrders();
    
    /**
     * Find today's expected deliveries
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.expectedDeliveryDate = :today " +
           "AND po.status NOT IN ('RECEIVED', 'CANCELLED') " +
           "ORDER BY po.priority DESC")
    List<PurchaseOrder> findTodaysExpectedDeliveries(@Param("today") LocalDate today);
    
    /**
     * Find recent purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po ORDER BY po.createdAt DESC")
    List<PurchaseOrder> findRecentPurchaseOrders(Pageable pageable);
    
    /**
     * Find supplier recent purchase orders
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.supplierId = :supplierId " +
           "ORDER BY po.createdAt DESC")
    List<PurchaseOrder> findSupplierRecentPurchaseOrders(@Param("supplierId") Long supplierId, 
                                                          Pageable pageable);
    
    /**
     * Find purchase orders requiring action
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE " +
           "(po.isApproved = false AND po.status = 'PENDING') OR " +
           "(po.expectedDeliveryDate <= :thresholdDate AND po.status IN ('ORDERED', 'PARTIAL_RECEIVED')) OR " +
           "(po.isPaid = false AND po.status = 'RECEIVED') " +
           "ORDER BY po.expectedDeliveryDate ASC")
    List<PurchaseOrder> findPurchaseOrdersRequiringAction(@Param("thresholdDate") LocalDate thresholdDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count purchase orders by supplier
     */
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.supplierId = :supplierId")
    Long countBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * Count purchase orders by warehouse
     */
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.warehouseId = :warehouseId")
    Long countByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * Count purchase orders by status
     */
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending purchase orders
     */
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.status = 'PENDING'")
    Long countPendingPurchaseOrders();
    
    /**
     * Count purchase orders pending approval
     */
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.isApproved = false " +
           "AND po.status NOT IN ('DRAFT', 'CANCELLED')")
    Long countPurchaseOrdersPendingApproval();
    
    /**
     * Count overdue deliveries
     */
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.expectedDeliveryDate < :currentDate " +
           "AND po.status IN ('ORDERED', 'PARTIAL_RECEIVED')")
    Long countOverdueDeliveries(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Count unpaid purchase orders
     */
    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.isPaid = false " +
           "AND po.status NOT IN ('DRAFT', 'CANCELLED')")
    Long countUnpaidPurchaseOrders();
    
    /**
     * Get order type distribution
     */
    @Query("SELECT po.orderType, COUNT(po) as orderCount FROM PurchaseOrder po " +
           "GROUP BY po.orderType ORDER BY orderCount DESC")
    List<Object[]> getOrderTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT po.status, COUNT(po) as orderCount FROM PurchaseOrder po " +
           "GROUP BY po.status ORDER BY orderCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get priority distribution
     */
    @Query("SELECT po.priority, COUNT(po) as orderCount FROM PurchaseOrder po " +
           "GROUP BY po.priority ORDER BY orderCount DESC")
    List<Object[]> getPriorityDistribution();
    
    /**
     * Get payment status distribution
     */
    @Query("SELECT po.paymentStatus, COUNT(po) as orderCount FROM PurchaseOrder po " +
           "GROUP BY po.paymentStatus ORDER BY orderCount DESC")
    List<Object[]> getPaymentStatusDistribution();
    
    /**
     * Get monthly purchase order count
     */
    @Query("SELECT YEAR(po.orderDate) as year, MONTH(po.orderDate) as month, " +
           "COUNT(po) as orderCount FROM PurchaseOrder po " +
           "WHERE po.orderDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(po.orderDate), MONTH(po.orderDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyPurchaseOrderCount(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);
    
    /**
     * Get total order value
     */
    @Query("SELECT SUM(po.totalAmount) FROM PurchaseOrder po " +
           "WHERE po.status NOT IN ('DRAFT', 'CANCELLED')")
    Double getTotalOrderValue();
    
    /**
     * Get total paid amount
     */
    @Query("SELECT SUM(po.paidAmount) FROM PurchaseOrder po WHERE po.isPaid = true")
    Double getTotalPaidAmount();
    
    /**
     * Get total outstanding amount
     */
    @Query("SELECT SUM(po.balanceAmount) FROM PurchaseOrder po " +
           "WHERE po.isPaid = false AND po.balanceAmount > 0")
    Double getTotalOutstandingAmount();
    
    /**
     * Get average order value
     */
    @Query("SELECT AVG(po.totalAmount) FROM PurchaseOrder po " +
           "WHERE po.status NOT IN ('DRAFT', 'CANCELLED')")
    Double getAverageOrderValue();
    
    /**
     * Get total order value by supplier
     */
    @Query("SELECT po.supplierId, po.supplierName, SUM(po.totalAmount) as totalValue " +
           "FROM PurchaseOrder po WHERE po.status NOT IN ('DRAFT', 'CANCELLED') " +
           "GROUP BY po.supplierId, po.supplierName ORDER BY totalValue DESC")
    List<Object[]> getTotalOrderValueBySupplier();
    
    /**
     * Get top suppliers
     */
    @Query("SELECT po.supplierId, po.supplierName, COUNT(po) as orderCount, " +
           "SUM(po.totalAmount) as totalValue FROM PurchaseOrder po " +
           "WHERE po.status NOT IN ('DRAFT', 'CANCELLED') " +
           "GROUP BY po.supplierId, po.supplierName " +
           "ORDER BY totalValue DESC")
    List<Object[]> getTopSuppliers(Pageable pageable);
    
    /**
     * Get on-time delivery rate
     */
    @Query("SELECT " +
           "(SELECT COUNT(po) FROM PurchaseOrder po WHERE po.isReceived = true " +
           "AND po.receivedDate <= po.expectedDeliveryDate) * 100.0 / " +
           "(SELECT COUNT(po) FROM PurchaseOrder po WHERE po.isReceived = true) " +
           "FROM PurchaseOrder po WHERE po.isReceived = true")
    Double getOnTimeDeliveryRate();
    
    /**
     * Find purchase orders by tags
     */
    @Query("SELECT po FROM PurchaseOrder po WHERE po.tags LIKE CONCAT('%', :tag, '%')")
    List<PurchaseOrder> findByTag(@Param("tag") String tag);
}
