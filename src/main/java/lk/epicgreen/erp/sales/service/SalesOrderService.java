package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.request.SalesOrderRequest;
import lk.epicgreen.erp.sales.dto.response.SalesOrderResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for SalesOrder entity business logic
 * 
 * Sales Order Status Workflow:
 * DRAFT → CONFIRMED → PENDING_APPROVAL → APPROVED → PROCESSING → PACKED → DISPATCHED → DELIVERED
 * Can be CANCELLED from any status
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SalesOrderService {

    /**
     * Create a new Sales Order
     */
    SalesOrderResponse createSalesOrder(SalesOrderRequest request);

    /**
     * Update an existing Sales Order (only in DRAFT status)
     */
    SalesOrderResponse updateSalesOrder(Long id, SalesOrderRequest request);

    /**
     * Confirm Sales Order (DRAFT → CONFIRMED)
     */
    void confirmSalesOrder(Long id);

    /**
     * Submit for Approval (CONFIRMED → PENDING_APPROVAL)
     */
    void submitForApproval(Long id);

    /**
     * Approve Sales Order (PENDING_APPROVAL → APPROVED)
     */
    void approveSalesOrder(Long id, Long approvedBy);

    /**
     * Start Processing (APPROVED → PROCESSING)
     */
    void startProcessing(Long id);

    /**
     * Mark as Packed (PROCESSING → PACKED)
     */
    void markAsPacked(Long id);

    /**
     * Mark as Dispatched (PACKED → DISPATCHED)
     */
    void markAsDispatched(Long id);

    /**
     * Mark as Delivered (DISPATCHED → DELIVERED)
     */
    void markAsDelivered(Long id);

    /**
     * Cancel Sales Order
     */
    void cancelSalesOrder(Long id, String reason);

    /**
     * Delete Sales Order (only in DRAFT status)
     */
    void deleteSalesOrder(Long id);

    /**
     * Assign Sales Representative
     */
    void assignSalesRep(Long orderId, Long salesRepId);

    /**
     * Get Sales Order by ID
     */
    SalesOrderResponse getSalesOrderById(Long id);

    /**
     * Get Sales Order by number
     */
    SalesOrderResponse getSalesOrderByNumber(String orderNumber);

    /**
     * Get all Sales Orders (paginated)
     */
    PageResponse<SalesOrderResponse> getAllSalesOrders(Pageable pageable);

    /**
     * Get Sales Orders by status
     */
    PageResponse<SalesOrderResponse> getSalesOrdersByStatus(String status, Pageable pageable);

    /**
     * Get Sales Orders by customer
     */
    List<SalesOrderResponse> getSalesOrdersByCustomer(Long customerId);

    /**
     * Get Sales Orders by sales representative
     */
    List<SalesOrderResponse> getSalesOrdersBySalesRep(Long salesRepId);

    /**
     * Get Sales Orders by warehouse
     */
    List<SalesOrderResponse> getSalesOrdersByWarehouse(Long warehouseId);

    /**
     * Get Sales Orders by date range
     */
    List<SalesOrderResponse> getSalesOrdersByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get Sales Orders by order type
     */
    PageResponse<SalesOrderResponse> getSalesOrdersByOrderType(String orderType, Pageable pageable);

    /**
     * Get pending approval orders
     */
    List<SalesOrderResponse> getPendingApprovalOrders();

    /**
     * Get overdue orders (expected_delivery_date < today and status not DELIVERED/CANCELLED)
     */
    List<SalesOrderResponse> getOverdueOrders();

    /**
     * Search Sales Orders
     */
    PageResponse<SalesOrderResponse> searchSalesOrders(String keyword, Pageable pageable);

    /**
     * Get total sales amount for a customer
     */
    BigDecimal getTotalSalesByCustomer(Long customerId);

    /**
     * Get total sales amount for a date range
     */
    BigDecimal getTotalSalesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Check if can delete
     */
    boolean canDelete(Long id);

    /**
     * Check if can update
     */
    boolean canUpdate(Long id);
}
