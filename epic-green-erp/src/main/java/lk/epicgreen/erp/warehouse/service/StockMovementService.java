package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.StockMovementRequest;
import lk.epicgreen.erp.warehouse.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * StockMovement Service Interface
 * Service for stock movement operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface StockMovementService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    StockMovement createStockMovement(StockMovementRequest request);
    StockMovement updateStockMovement(Long id, StockMovementRequest request);
    void deleteStockMovement(Long id);
    StockMovement getStockMovementById(Long id);
    StockMovement getStockMovementByNumber(String movementNumber);
    List<StockMovement> getAllStockMovements();
    Page<StockMovement> getAllStockMovements(Pageable pageable);
    Page<StockMovement> searchStockMovements(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    StockMovement approveStockMovement(Long id, Long approvedByUserId, String approvalNotes);
    StockMovement completeStockMovement(Long id);
    StockMovement cancelStockMovement(Long id, String cancellationReason);
    
    // ===================================================================
    // MOVEMENT OPERATIONS
    // ===================================================================
    
    StockMovement recordStockIn(StockMovementRequest request);
    StockMovement recordStockOut(StockMovementRequest request);
    StockMovement recordTransfer(StockMovementRequest request);
    StockMovement recordAdjustment(StockMovementRequest request);
    StockMovement recordReturn(StockMovementRequest request);
    StockMovement recordDamage(StockMovementRequest request);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<StockMovement> getStockInMovements();
    List<StockMovement> getStockOutMovements();
    List<StockMovement> getTransferMovements();
    List<StockMovement> getAdjustmentMovements();
    List<StockMovement> getReturnMovements();
    List<StockMovement> getDamageMovements();
    List<StockMovement> getPendingMovements();
    List<StockMovement> getApprovedMovements();
    List<StockMovement> getCompletedMovements();
    List<StockMovement> getMovementsPendingApproval();
    List<StockMovement> getTodaysMovements();
    List<StockMovement> getMovementsByProduct(Long productId);
    List<StockMovement> getMovementsByWarehouse(Long warehouseId);
    List<StockMovement> getMovementsByDateRange(LocalDate startDate, LocalDate endDate);
    List<StockMovement> getTransfersBetweenWarehouses(Long fromWarehouseId, Long toWarehouseId);
    List<StockMovement> getProductMovementHistory(Long productId, LocalDate startDate, LocalDate endDate);
    List<StockMovement> getWarehouseMovementHistory(Long warehouseId, LocalDate startDate, LocalDate endDate);
    List<StockMovement> getRecentMovements(int limit);
    Double getTotalQuantityInByProduct(Long productId);
    Double getTotalQuantityOutByProduct(Long productId);
    Double getTotalValueInByWarehouse(Long warehouseId);
    Double getTotalValueOutByWarehouse(Long warehouseId);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateStockMovement(StockMovement stockMovement);
    boolean canApproveStockMovement(Long movementId);
    boolean canCancelStockMovement(Long movementId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<StockMovement> createBulkStockMovements(List<StockMovementRequest> requests);
    int approveBulkStockMovements(List<Long> movementIds, Long approvedByUserId);
    int deleteBulkStockMovements(List<Long> movementIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    Map<String, Object> getStockMovementStatistics();
    List<Map<String, Object>> getMovementTypeDistribution();
    List<Map<String, Object>> getStatusDistribution();
    List<Map<String, Object>> getTransactionTypeDistribution();
    List<Map<String, Object>> getMonthlyMovementCount(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> getMovementsByWarehouse();
    List<Map<String, Object>> getMovementsByProduct();
    Double getTotalQuantityMoved();
    Double getTotalValueMoved();
    Map<String, Object> getDashboardStatistics();
}
