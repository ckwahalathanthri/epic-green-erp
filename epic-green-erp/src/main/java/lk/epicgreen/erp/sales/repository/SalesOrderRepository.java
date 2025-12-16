package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.SalesOrder;
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
 * SalesOrder Repository
 * Repository for sales order data access
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    
    // ===================================================================
    // FIND BY UNIQUE FIELDS
    // ===================================================================
    
    /**
     * Find sales order by order number
     */
    Optional<SalesOrder> findByOrderNumber(String orderNumber);
    
    /**
     * Check if sales order exists by order number
     */
    boolean existsByOrderNumber(String orderNumber);
    
    // ===================================================================
    // FIND BY SINGLE FIELD
    // ===================================================================
    
    /**
     * Find sales orders by customer ID
     */
    List<SalesOrder> findByCustomerId(Long customerId);
    
    /**
     * Find sales orders by customer ID with pagination
     */
    Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find sales orders by order type
     */
    List<SalesOrder> findByOrderType(String orderType);
    
    /**
     * Find sales orders by order type with pagination
     */
    Page<SalesOrder> findByOrderType(String orderType, Pageable pageable);
    
    /**
     * Find sales orders by status
     */
    List<SalesOrder> findByStatus(String status);
    
    /**
     * Find sales orders by status with pagination
     */
    Page<SalesOrder> findByStatus(String status, Pageable pageable);
    
    /**
     * Find sales orders by priority
     */
    List<SalesOrder> findByPriority(String priority);
    
    /**
     * Find sales orders by delivery status
     */
    List<SalesOrder> findByDeliveryStatus(String deliveryStatus);
    
    /**
     * Find sales orders by payment status
     */
    List<SalesOrder> findByPaymentStatus(String paymentStatus);
    
    /**
     * Find sales orders by sales representative
     */
    List<SalesOrder> findBySalesRepId(Long salesRepId);
    
    /**
     * Find sales orders by created by user
     */
    List<SalesOrder> findByCreatedByUserId(Long userId);
    
    /**
     * Find sales orders by approved by user
     */
    List<SalesOrder> findByApprovedByUserId(Long userId);
    
    /**
     * Find sales orders by is approved
     */
    List<SalesOrder> findByIsApproved(Boolean isApproved);
    
    /**
     * Find sales orders by is dispatched
     */
    List<SalesOrder> findByIsDispatched(Boolean isDispatched);
    
    /**
     * Find sales orders by is invoiced
     */
    List<SalesOrder> findByIsInvoiced(Boolean isInvoiced);
    
    /**
     * Find sales orders by is paid
     */
    List<SalesOrder> findByIsPaid(Boolean isPaid);
    
    // ===================================================================
    // FIND BY DATE RANGE
    // ===================================================================
    
    /**
     * Find sales orders by order date between dates
     */
    List<SalesOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find sales orders by order date between dates with pagination
     */
    Page<SalesOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find sales orders by delivery date between dates
     */
    List<SalesOrder> findByDeliveryDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find sales orders by approved date between dates
     */
    List<SalesOrder> findByApprovedDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find sales orders by created at between dates
     */
    List<SalesOrder> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ===================================================================
    // FIND BY MULTIPLE FIELDS
    // ===================================================================
    
    /**
     * Find sales orders by customer ID and status
     */
    List<SalesOrder> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Find sales orders by customer ID and status with pagination
     */
    Page<SalesOrder> findByCustomerIdAndStatus(Long customerId, String status, Pageable pageable);
    
    /**
     * Find sales orders by order type and status
     */
    List<SalesOrder> findByOrderTypeAndStatus(String orderType, String status);
    
    /**
     * Find sales orders by sales rep and status
     */
    List<SalesOrder> findBySalesRepIdAndStatus(Long salesRepId, String status);
    
    /**
     * Find sales orders by is approved and status
     */
    List<SalesOrder> findByIsApprovedAndStatus(Boolean isApproved, String status);
    
    /**
     * Find sales orders by delivery status and payment status
     */
    List<SalesOrder> findByDeliveryStatusAndPaymentStatus(String deliveryStatus, String paymentStatus);
    
    // ===================================================================
    // CUSTOM QUERIES
    // ===================================================================
    
    /**
     * Search sales orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE " +
           "LOWER(so.orderNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(so.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(so.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SalesOrder> searchSalesOrders(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * Find draft orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'DRAFT' " +
           "ORDER BY so.orderDate DESC")
    List<SalesOrder> findDraftOrders();
    
    /**
     * Find pending orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'PENDING' " +
           "ORDER BY so.orderDate DESC")
    List<SalesOrder> findPendingOrders();
    
    /**
     * Find pending orders with pagination
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'PENDING' " +
           "ORDER BY so.orderDate DESC")
    Page<SalesOrder> findPendingOrders(Pageable pageable);
    
    /**
     * Find confirmed orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'CONFIRMED' " +
           "ORDER BY so.orderDate DESC")
    List<SalesOrder> findConfirmedOrders();
    
    /**
     * Find processing orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'PROCESSING' " +
           "ORDER BY so.orderDate DESC")
    List<SalesOrder> findProcessingOrders();
    
    /**
     * Find completed orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'COMPLETED' " +
           "ORDER BY so.orderDate DESC")
    List<SalesOrder> findCompletedOrders();
    
    /**
     * Find cancelled orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'CANCELLED' " +
           "ORDER BY so.orderDate DESC")
    List<SalesOrder> findCancelledOrders();
    
    /**
     * Find orders pending approval
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.isApproved = false " +
           "AND so.status NOT IN ('DRAFT', 'CANCELLED') " +
           "ORDER BY so.orderDate ASC")
    List<SalesOrder> findOrdersPendingApproval();
    
    /**
     * Find orders pending dispatch
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.isApproved = true " +
           "AND so.isDispatched = false " +
           "AND so.status IN ('CONFIRMED', 'PROCESSING') " +
           "ORDER BY so.deliveryDate ASC")
    List<SalesOrder> findOrdersPendingDispatch();
    
    /**
     * Find orders pending invoicing
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.isDispatched = true " +
           "AND so.isInvoiced = false " +
           "ORDER BY so.orderDate ASC")
    List<SalesOrder> findOrdersPendingInvoicing();
    
    /**
     * Find unpaid orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.isInvoiced = true " +
           "AND so.isPaid = false " +
           "AND so.paymentStatus != 'PAID' " +
           "ORDER BY so.orderDate ASC")
    List<SalesOrder> findUnpaidOrders();
    
    /**
     * Find overdue deliveries
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.deliveryDate < :currentDate " +
           "AND so.deliveryStatus IN ('PENDING', 'PARTIAL') " +
           "AND so.status NOT IN ('CANCELLED', 'COMPLETED') " +
           "ORDER BY so.deliveryDate ASC")
    List<SalesOrder> findOverdueDeliveries(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find high priority orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.priority = 'HIGH' " +
           "AND so.status NOT IN ('COMPLETED', 'CANCELLED') " +
           "ORDER BY so.deliveryDate ASC")
    List<SalesOrder> findHighPriorityOrders();
    
    /**
     * Find recent orders
     */
    @Query("SELECT so FROM SalesOrder so ORDER BY so.orderDate DESC, so.createdAt DESC")
    List<SalesOrder> findRecentOrders(Pageable pageable);
    
    /**
     * Find customer recent orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.customerId = :customerId " +
           "ORDER BY so.orderDate DESC, so.createdAt DESC")
    List<SalesOrder> findCustomerRecentOrders(@Param("customerId") Long customerId, Pageable pageable);
    
    /**
     * Find sales rep recent orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.salesRepId = :salesRepId " +
           "ORDER BY so.orderDate DESC, so.createdAt DESC")
    List<SalesOrder> findSalesRepRecentOrders(@Param("salesRepId") Long salesRepId, Pageable pageable);
    
    /**
     * Find orders by date range and status
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.orderDate BETWEEN :startDate AND :endDate " +
           "AND so.status = :status ORDER BY so.orderDate DESC")
    List<SalesOrder> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate,
                                              @Param("status") String status);
    
    /**
     * Find orders requiring action
     */
    @Query("SELECT so FROM SalesOrder so WHERE " +
           "(so.isApproved = false AND so.status = 'PENDING') OR " +
           "(so.isApproved = true AND so.isDispatched = false AND so.deliveryDate <= :currentDate) OR " +
           "(so.isDispatched = true AND so.isInvoiced = false) " +
           "ORDER BY so.orderDate ASC")
    List<SalesOrder> findOrdersRequiringAction(@Param("currentDate") LocalDate currentDate);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    /**
     * Count orders by customer
     */
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.customerId = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Count orders by sales rep
     */
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.salesRepId = :salesRepId")
    Long countBySalesRepId(@Param("salesRepId") Long salesRepId);
    
    /**
     * Count orders by order type
     */
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.orderType = :orderType")
    Long countByOrderType(@Param("orderType") String orderType);
    
    /**
     * Count orders by status
     */
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.status = :status")
    Long countByStatus(@Param("status") String status);
    
    /**
     * Count pending orders
     */
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.status = 'PENDING'")
    Long countPendingOrders();
    
    /**
     * Count orders pending approval
     */
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.isApproved = false " +
           "AND so.status NOT IN ('DRAFT', 'CANCELLED')")
    Long countOrdersPendingApproval();
    
    /**
     * Count orders pending dispatch
     */
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.isApproved = true " +
           "AND so.isDispatched = false " +
           "AND so.status IN ('CONFIRMED', 'PROCESSING')")
    Long countOrdersPendingDispatch();
    
    /**
     * Count unpaid orders
     */
    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.isInvoiced = true " +
           "AND so.isPaid = false")
    Long countUnpaidOrders();
    
    /**
     * Get order type distribution
     */
    @Query("SELECT so.orderType, COUNT(so) as orderCount FROM SalesOrder so " +
           "GROUP BY so.orderType ORDER BY orderCount DESC")
    List<Object[]> getOrderTypeDistribution();
    
    /**
     * Get status distribution
     */
    @Query("SELECT so.status, COUNT(so) as orderCount FROM SalesOrder so " +
           "GROUP BY so.status ORDER BY orderCount DESC")
    List<Object[]> getStatusDistribution();
    
    /**
     * Get priority distribution
     */
    @Query("SELECT so.priority, COUNT(so) as orderCount FROM SalesOrder so " +
           "GROUP BY so.priority ORDER BY orderCount DESC")
    List<Object[]> getPriorityDistribution();
    
    /**
     * Get delivery status distribution
     */
    @Query("SELECT so.deliveryStatus, COUNT(so) as orderCount FROM SalesOrder so " +
           "GROUP BY so.deliveryStatus ORDER BY orderCount DESC")
    List<Object[]> getDeliveryStatusDistribution();
    
    /**
     * Get payment status distribution
     */
    @Query("SELECT so.paymentStatus, COUNT(so) as orderCount FROM SalesOrder so " +
           "GROUP BY so.paymentStatus ORDER BY orderCount DESC")
    List<Object[]> getPaymentStatusDistribution();
    
    /**
     * Get monthly order count
     */
    @Query("SELECT YEAR(so.orderDate) as year, MONTH(so.orderDate) as month, " +
           "COUNT(so) as orderCount FROM SalesOrder so " +
           "WHERE so.orderDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(so.orderDate), MONTH(so.orderDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyOrderCount(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
    
    /**
     * Get total order value
     */
    @Query("SELECT SUM(so.totalAmount) FROM SalesOrder so WHERE so.status NOT IN ('DRAFT', 'CANCELLED')")
    Double getTotalOrderValue();
    
    /**
     * Get total order value by customer
     */
    @Query("SELECT so.customerId, so.customerName, SUM(so.totalAmount) as totalValue " +
           "FROM SalesOrder so WHERE so.status NOT IN ('DRAFT', 'CANCELLED') " +
           "GROUP BY so.customerId, so.customerName ORDER BY totalValue DESC")
    List<Object[]> getTotalOrderValueByCustomer();
    
    /**
     * Get total order value by sales rep
     */
    @Query("SELECT so.salesRepId, so.salesRepName, SUM(so.totalAmount) as totalValue " +
           "FROM SalesOrder so WHERE so.status NOT IN ('DRAFT', 'CANCELLED') " +
           "GROUP BY so.salesRepId, so.salesRepName ORDER BY totalValue DESC")
    List<Object[]> getTotalOrderValueBySalesRep();
    
    /**
     * Get average order value
     */
    @Query("SELECT AVG(so.totalAmount) FROM SalesOrder so WHERE so.status NOT IN ('DRAFT', 'CANCELLED')")
    Double getAverageOrderValue();
    
    /**
     * Get top customers
     */
    @Query("SELECT so.customerId, so.customerName, COUNT(so) as orderCount, " +
           "SUM(so.totalAmount) as totalValue FROM SalesOrder so " +
           "WHERE so.status NOT IN ('DRAFT', 'CANCELLED') " +
           "GROUP BY so.customerId, so.customerName ORDER BY totalValue DESC")
    List<Object[]> getTopCustomers(Pageable pageable);
    
    /**
     * Find orders by tags
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.tags LIKE CONCAT('%', :tag, '%')")
    List<SalesOrder> findByTag(@Param("tag") String tag);
}
