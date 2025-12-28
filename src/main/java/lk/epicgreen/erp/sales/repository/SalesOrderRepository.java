package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.SalesOrder;
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
 * Repository interface for SalesOrder entity
 * Based on ACTUAL database schema: sales_orders table
 * 
 * Fields: order_number, order_date, customer_id (BIGINT), customer_po_number, customer_po_date,
 *         billing_address_id (BIGINT), shipping_address_id (BIGINT), warehouse_id (BIGINT),
 *         sales_rep_id (BIGINT), order_type (ENUM: REGULAR, URGENT, ADVANCE_ORDER),
 *         status (ENUM: DRAFT, CONFIRMED, PENDING_APPROVAL, APPROVED, PROCESSING, PACKED, DISPATCHED, DELIVERED, CANCELLED),
 *         payment_mode (ENUM: CASH, CHEQUE, CREDIT, BANK_TRANSFER),
 *         delivery_mode (ENUM: SELF_PICKUP, COMPANY_DELIVERY, COURIER),
 *         expected_delivery_date, subtotal, tax_amount, discount_percentage, discount_amount,
 *         freight_charges, total_amount, approved_by (BIGINT), approved_at, remarks
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find sales order by order number
     */
    Optional<SalesOrder> findByOrderNumber(String orderNumber);
    
    /**
     * Find all orders for a customer
     */
    List<SalesOrder> findByCustomerId(Long customerId);
    
    /**
     * Find all orders for a customer with pagination
     */
    Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Find orders by status
     */
    List<SalesOrder> findByStatus(String status);
    
    /**
     * Find orders by status with pagination
     */
    Page<SalesOrder> findByStatus(String status, Pageable pageable);
    
    /**
     * Find orders by order type
     */
    List<SalesOrder> findByOrderType(String orderType);
    
    /**
     * Find orders by payment mode
     */
    List<SalesOrder> findByPaymentMode(String paymentMode);
    
    /**
     * Find orders by delivery mode
     */
    List<SalesOrder> findByDeliveryMode(String deliveryMode);
    
    /**
     * Find orders by sales rep
     */
    List<SalesOrder> findBySalesRepId(Long salesRepId);
    
    /**
     * Find orders by sales rep with pagination
     */
    Page<SalesOrder> findBySalesRepId(Long salesRepId, Pageable pageable);
    
    /**
     * Find orders by warehouse
     */
    List<SalesOrder> findByWarehouseId(Long warehouseId);
    
    /**
     * Find orders by order date
     */
    List<SalesOrder> findByOrderDate(LocalDate orderDate);
    
    /**
     * Find orders by order date range
     */
    List<SalesOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find orders by order date range with pagination
     */
    Page<SalesOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // ==================== EXISTENCE CHECKS ====================
    
    /**
     * Check if order number exists
     */
    boolean existsByOrderNumber(String orderNumber);
    
    /**
     * Check if order number exists excluding specific order ID
     */
    boolean existsByOrderNumberAndIdNot(String orderNumber, Long id);
    
    // ==================== SEARCH METHODS ====================
    
    /**
     * Search orders by order number containing (case-insensitive)
     */
    Page<SalesOrder> findByOrderNumberContainingIgnoreCase(String orderNumber, Pageable pageable);
    
    /**
     * Search orders by customer PO number
     */
    Page<SalesOrder> findByCustomerPoNumberContainingIgnoreCase(String customerPoNumber, Pageable pageable);
    
    /**
     * Search orders by multiple criteria
     */
    @Query("SELECT so FROM SalesOrder so WHERE " +
           "(:orderNumber IS NULL OR LOWER(so.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%'))) AND " +
           "(:customerId IS NULL OR so.customerId = :customerId) AND " +
           "(:status IS NULL OR so.status = :status) AND " +
           "(:orderType IS NULL OR so.orderType = :orderType) AND " +
           "(:salesRepId IS NULL OR so.salesRepId = :salesRepId) AND " +
           "(:startDate IS NULL OR so.orderDate >= :startDate) AND " +
           "(:endDate IS NULL OR so.orderDate <= :endDate)")
    Page<SalesOrder> searchOrders(
            @Param("orderNumber") String orderNumber,
            @Param("customerId") Long customerId,
            @Param("status") String status,
            @Param("orderType") String orderType,
            @Param("salesRepId") Long salesRepId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count orders by status
     */
    long countByStatus(String status);
    
    /**
     * Count orders by customer
     */
    long countByCustomerId(Long customerId);
    
    /**
     * Count orders by sales rep
     */
    long countBySalesRepId(Long salesRepId);
    
    /**
     * Count orders in date range
     */
    long countByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find draft orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'DRAFT' ORDER BY so.orderDate DESC")
    List<SalesOrder> findDraftOrders();
    
    /**
     * Find confirmed orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'CONFIRMED' ORDER BY so.orderDate DESC")
    List<SalesOrder> findConfirmedOrders();
    
    /**
     * Find pending approval orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'PENDING_APPROVAL' ORDER BY so.orderDate DESC")
    List<SalesOrder> findPendingApprovalOrders();
    
    /**
     * Find approved orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'APPROVED' ORDER BY so.approvedAt DESC")
    List<SalesOrder> findApprovedOrders();
    
    /**
     * Find processing orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'PROCESSING' ORDER BY so.orderDate DESC")
    List<SalesOrder> findProcessingOrders();
    
    /**
     * Find dispatched orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'DISPATCHED' ORDER BY so.orderDate DESC")
    List<SalesOrder> findDispatchedOrders();
    
    /**
     * Find delivered orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'DELIVERED' ORDER BY so.orderDate DESC")
    List<SalesOrder> findDeliveredOrders();
    
    /**
     * Find cancelled orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.status = 'CANCELLED' ORDER BY so.orderDate DESC")
    List<SalesOrder> findCancelledOrders();
    
    /**
     * Find urgent orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.orderType = 'URGENT' AND so.status NOT IN ('DELIVERED', 'CANCELLED') " +
           "ORDER BY so.orderDate")
    List<SalesOrder> findUrgentOrders();
    
    /**
     * Find advance orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.orderType = 'ADVANCE_ORDER' ORDER BY so.orderDate DESC")
    List<SalesOrder> findAdvanceOrders();
    
    /**
     * Find orders by expected delivery date
     */
    List<SalesOrder> findByExpectedDeliveryDate(LocalDate expectedDeliveryDate);
    
    /**
     * Find orders due for delivery
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.expectedDeliveryDate <= :date " +
           "AND so.status NOT IN ('DELIVERED', 'CANCELLED') ORDER BY so.expectedDeliveryDate")
    List<SalesOrder> findOrdersDueForDelivery(@Param("date") LocalDate date);
    
    /**
     * Find overdue orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.expectedDeliveryDate < CURRENT_DATE " +
           "AND so.status NOT IN ('DELIVERED', 'CANCELLED') ORDER BY so.expectedDeliveryDate")
    List<SalesOrder> findOverdueOrders();
    
    /**
     * Get orders by customer and status
     */
    List<SalesOrder> findByCustomerIdAndStatus(Long customerId, String status);
    
    /**
     * Get orders by sales rep and status
     */
    List<SalesOrder> findBySalesRepIdAndStatus(Long salesRepId, String status);
    
    /**
     * Get order statistics
     */
    @Query("SELECT " +
           "COUNT(so) as totalOrders, " +
           "SUM(CASE WHEN so.status = 'DRAFT' THEN 1 ELSE 0 END) as draftOrders, " +
           "SUM(CASE WHEN so.status = 'CONFIRMED' THEN 1 ELSE 0 END) as confirmedOrders, " +
           "SUM(CASE WHEN so.status = 'APPROVED' THEN 1 ELSE 0 END) as approvedOrders, " +
           "SUM(CASE WHEN so.status = 'DELIVERED' THEN 1 ELSE 0 END) as deliveredOrders, " +
           "SUM(so.totalAmount) as totalOrderValue " +
           "FROM SalesOrder so")
    Object getOrderStatistics();
    
    /**
     * Get orders grouped by status
     */
    @Query("SELECT so.status, COUNT(so) as orderCount, SUM(so.totalAmount) as totalValue " +
           "FROM SalesOrder so GROUP BY so.status ORDER BY orderCount DESC")
    List<Object[]> getOrdersByStatus();
    
    /**
     * Get orders grouped by customer
     */
    @Query("SELECT so.customerId, COUNT(so) as orderCount, SUM(so.totalAmount) as totalValue " +
           "FROM SalesOrder so GROUP BY so.customerId ORDER BY totalValue DESC")
    List<Object[]> getOrdersByCustomer();
    
    /**
     * Get orders grouped by sales rep
     */
    @Query("SELECT so.salesRepId, COUNT(so) as orderCount, SUM(so.totalAmount) as totalValue " +
           "FROM SalesOrder so WHERE so.salesRepId IS NOT NULL " +
           "GROUP BY so.salesRepId ORDER BY totalValue DESC")
    List<Object[]> getOrdersBySalesRep();
    
    /**
     * Get daily order summary
     */
    @Query("SELECT so.orderDate, COUNT(so) as orderCount, SUM(so.totalAmount) as totalValue " +
           "FROM SalesOrder so WHERE so.orderDate BETWEEN :startDate AND :endDate " +
           "GROUP BY so.orderDate ORDER BY so.orderDate DESC")
    List<Object[]> getDailyOrderSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find orders approved in date range
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.approvedAt BETWEEN :startDate AND :endDate " +
           "ORDER BY so.approvedAt DESC")
    List<SalesOrder> findOrdersApprovedBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find today's orders
     */
    @Query("SELECT so FROM SalesOrder so WHERE so.orderDate = CURRENT_DATE ORDER BY so.createdAt DESC")
    List<SalesOrder> findTodayOrders();
    
    /**
     * Find top customers by order value
     */
    @Query("SELECT so.customerId, SUM(so.totalAmount) as totalOrderValue " +
           "FROM SalesOrder so WHERE so.orderDate BETWEEN :startDate AND :endDate " +
           "GROUP BY so.customerId ORDER BY totalOrderValue DESC")
    List<Object[]> findTopCustomersByOrderValue(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    
    /**
     * Find all orders ordered by date
     */
    List<SalesOrder> findAllByOrderByOrderDateDescCreatedAtDesc();
    
    /**
     * Find orders by customer and date range
     */
    List<SalesOrder> findByCustomerIdAndOrderDateBetween(
            Long customerId, LocalDate startDate, LocalDate endDate);
}
