package lk.epicgreen.erp.sales.service;

import lk.epicgreen.erp.sales.dto.SalesOrderRequest;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * SalesOrder Service Interface
 * Service for sales order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface SalesOrderService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    SalesOrder createSalesOrder(SalesOrderRequest request);
    SalesOrder updateSalesOrder(Long id, SalesOrderRequest request);
    void deleteSalesOrder(Long id);
    SalesOrder getSalesOrderById(Long id);
    SalesOrder getSalesOrderByNumber(String orderNumber);
    List<SalesOrder> getAllSalesOrders();
    Page<SalesOrder> getAllSalesOrders(Pageable pageable);
    Page<SalesOrder> searchSalesOrders(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    SalesOrder confirmSalesOrder(Long id);
    SalesOrder processSalesOrder(Long id);
    SalesOrder completeSalesOrder(Long id);

    void recordPayment(Long salesOrderId, Double paidAmount);

    @Transactional(readOnly = true)
    Double calculateSubtotal(SalesOrder order);

    @Transactional(readOnly = true)
    Double calculateTotalTax(SalesOrder order);

    @Transactional(readOnly = true)
    Double calculateTotalDiscount(SalesOrder order);

    SalesOrder cancelSalesOrder(Long id, String cancellationReason);
    SalesOrder approveSalesOrder(Long id, Long approvedByUserId, String approvalNotes);
    SalesOrder rejectSalesOrder(Long id, String rejectionReason);
    
    // ===================================================================
    // DELIVERY OPERATIONS
    // ===================================================================
    
    SalesOrder markAsDispatched(Long id, Long dispatchNoteId);
    SalesOrder updateDeliveryStatus(Long id, String deliveryStatus);
    
    // ===================================================================
    // PAYMENT OPERATIONS
    // ===================================================================
    
    SalesOrder markAsInvoiced(Long id, Long invoiceId);

    SalesOrder dispatchSalesOrder(Long id, Long dispatchNoteId);

    SalesOrder markAsDelivered(Long id);

    SalesOrder generateInvoice(Long id, Long invoiceId);

    SalesOrder updatePaymentStatus(Long id, String paymentStatus);
    SalesOrder markAsPaid(Long id);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<SalesOrder> getDraftOrders();
    List<SalesOrder> getPendingOrders();
    List<SalesOrder> getConfirmedOrders();
    List<SalesOrder> getProcessingOrders();
    List<SalesOrder> getCompletedOrders();
    List<SalesOrder> getCancelledOrders();
    List<SalesOrder> getOrdersPendingApproval();
    List<SalesOrder> getOrdersPendingDispatch();
    List<SalesOrder> getOrdersPendingInvoicing();
    List<SalesOrder> getUnpaidOrders();
    List<SalesOrder> getOverdueDeliveries();
    List<SalesOrder> getHighPriorityOrders();
    List<SalesOrder> getOrdersRequiringAction();
    List<SalesOrder> getOrdersByCustomer(Long customerId);
    Page<SalesOrder> getOrdersByCustomer(Long customerId, Pageable pageable);
    List<SalesOrder> getOrdersBySalesRep(Long salesRepId);
    List<SalesOrder> getOrdersByDateRange(LocalDate startDate, LocalDate endDate);
    List<SalesOrder> getRecentOrders(int limit);
    List<SalesOrder> getCustomerRecentOrders(Long customerId, int limit);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateSalesOrder(SalesOrder order);
    boolean canConfirmOrder(Long orderId);
    boolean canCancelOrder(Long orderId);
    boolean canApproveOrder(Long orderId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    void calculateOrderTotals(Long orderId);
    Double calculateSubtotal(Long orderId);
    Double calculateTotalTax(Long orderId);
    Double calculateTotalDiscount(Long orderId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<SalesOrder> createBulkSalesOrders(List<SalesOrderRequest> requests);
    int approveBulkSalesOrders(List<Long> orderIds, Long approvedByUserId);
    int deleteBulkSalesOrders(List<Long> orderIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getSalesOrderStatistics();
    List<Map<String, Object>> getOrderTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getPriorityDistribution();
    List<Map<String, Object>> getDeliveryStatusDistribution();
    List<Map<String, Object>> getPaymentStatusDistribution();
    List<Map<String, Object>> getMonthlyOrderCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getTotalOrderValueByCustomer();
    List<Map<String, Object>> getTotalOrderValueBySalesRep();
    List<Map<String, Object>> getTopCustomers(int limit);
    Double getTotalOrderValue();
    Double getAverageOrderValue();
    Map<String, Object> getDashboardStatistics();
}
