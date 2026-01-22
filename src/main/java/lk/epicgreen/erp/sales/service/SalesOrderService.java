package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.request.SalesOrderRequest;
import lk.epicgreen.erp.sales.dto.response.SalesOrderResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    SalesOrder confirmSalesOrder(Long id);

    /**
     * Submit for Approval (CONFIRMED → PENDING_APPROVAL)
     */
    void submitForApproval(Long id);

    /**
     * Approve Sales Order (PENDING_APPROVAL → APPROVED)
     */
    SalesOrder approveSalesOrder(Long id, Long approvedBy,String approvalNotes);

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
    SalesOrder markAsDispatched(Long id);

    /**
     * Mark as Delivered (DISPATCHED → DELIVERED)
     */
    void markAsDelivered(Long id);

    /**
     * Cancel Sales Order
     */
    SalesOrder cancelSalesOrder(Long id, String reason);

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

    SalesOrder processSalesOrder(Long id);

    SalesOrder completeSalesOrder(Long id);

    SalesOrder updateDeliveryStatus(Long id, String deliveryStatus);
    SalesOrder markAsInvoiced(Long id,Long invoiceId);

    SalesOrder updatePaymentStatus(Long id,String paymentStatus);

    SalesOrder markAsPaid(Long id);

    List<SalesOrder> getDraftOrders();

    List<SalesOrder>getPendingOrders();

    List<SalesOrder> getConfirmedOrders();
    List<SalesOrder> getProcessingOrders();

    List<SalesOrder> getCompletedOrders();

    List<SalesOrder> getCancelledOrders();

    List<SalesOrder> getOrdersPendingApproval();

    List<SalesOrder>  getOrdersPendingDispatch();

    List<SalesOrder> getOrdersPendingInvoicing();

    List<SalesOrder> getUnpaidOrders();

    List<SalesOrder> getHighPriorityOrders();

    List<SalesOrder> getOrdersRequiringAction();

    Page<SalesOrder> getOrdersByCustomer(Long customerId,Pageable pageable);

    List<SalesOrder> getOrdersByCustomer(Long customerId);
    List<SalesOrder> getOrdersBySalesRep(Long salesRepId);

    List<SalesOrder> getOrdersByDateRange(LocalDate startDate,LocalDate endDate);
    List<SalesOrder> getCustomerRecentOrders(Long customerId,Pageable limit);

    boolean canConfirmOrder(Long id);

    boolean canCancelOrder(Long id);

    boolean canApproveOrder(Long id);

    double calculateSubtotal(Long id);

    double calculateTotalTax(Long id);

    Map<String,Object> getSalesOrderStatistics();
    Map<String,Object> getOrderTypeDistribution();
    List<Map<String,Object>> getDeliveryStatusDistribution();

    double getTotalOrderValue();

    double getAverageOrderValue();

    Map<String,Object> getDashboardStatistics();

    double calculateTotalDiscount(Long id);

    List<SalesOrder> getOverdueDeliveries();

    SalesOrder rejectSalesOrder(Long id, String rejectionReason);
}
