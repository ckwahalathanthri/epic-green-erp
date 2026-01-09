package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.request.StockMovementRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockMovementResponse;
import lk.epicgreen.erp.warehouse.entity.StockMovement;
import lk.epicgreen.erp.common.dto.PageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for StockMovement entity business logic
 * 
 * IMPORTANT: StockMovement is IMMUTABLE
 * - CREATE: Allowed (add new movements)
 * - READ: Allowed (query movements)
 * - UPDATE: NOT ALLOWED (audit trail requirement)
 * - DELETE: NOT ALLOWED (audit trail requirement)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface StockMovementService {

    StockMovementResponse createStockMovement(StockMovementRequest request);
    StockMovementResponse getStockMovementById(Long id);
    PageResponse<StockMovementResponse> getAllStockMovements(Pageable pageable);
    List<StockMovementResponse> getMovementsByWarehouse(Long warehouseId);
    List<StockMovementResponse> getMovementsByProduct(Long productId);
    PageResponse<StockMovementResponse> getMovementsByType(String movementType, Pageable pageable);
    List<StockMovementResponse> getMovementsByDateRange(LocalDate startDate, LocalDate endDate);
    List<StockMovementResponse> getMovementsByReference(String referenceType, Long referenceId);
    List<StockMovementResponse> getReceiptMovements(Long warehouseId);
    List<StockMovementResponse> getIssueMovements(Long warehouseId);
    List<StockMovementResponse> getTransferMovements(Long warehouseId);
    List<StockMovementResponse> getSalesMovements(Long warehouseId);
    StockMovementResponse updateStockMovement(Long id, StockMovementRequest request);
	void deleteStockMovement(Long id);
    StockMovementResponse getStockMovementByNumber(String movementNumber);
    List<StockMovementResponse> getAllStockMovements();
	PageResponse<StockMovementResponse> searchStockMovements(String keyword, Pageable pageable);
	StockMovementResponse approveStockMovement(Long id, Long approvedByUserId, String approvalNotes);
	StockMovementResponse completeStockMovement(Long id);
	StockMovementResponse cancelStockMovement(Long id, String cancellationReason);
	StockMovementResponse recordStockIn(StockMovementRequest request);
	StockMovementResponse recordStockOut(StockMovementRequest request);
	StockMovementResponse recordTransfer(StockMovementRequest request);
	StockMovementResponse recordAdjustment(StockMovementRequest request);
	StockMovementResponse recordReturn(StockMovementRequest request);
	StockMovementResponse recordDamage(StockMovementRequest request);
	List<StockMovementResponse> getStockInMovements();
	List<StockMovementResponse> getStockOutMovements();
	List<StockMovementResponse> getTransferMovements();
    List<StockMovementResponse> getAdjustmentMovements();
	List<StockMovementResponse> getReturnMovements();
	List<StockMovementResponse> getDamageMovements();
    List<StockMovementResponse> getPendingMovements();
	List<StockMovementResponse> getApprovedMovements();
	List<StockMovementResponse> getCompletedMovements();
	List<StockMovementResponse> getMovementsPendingApproval();
	List<StockMovementResponse> getTodaysMovements();
	Double getTotalQuantityInByProduct(Long productId);
	Double getTotalQuantityOutByProduct(Long productId);
	Double getTotalValueInByWarehouse(Long warehouseId);
	Double getTotalValueOutByWarehouse(Long warehouseId);
	boolean canApproveStockMovement(Long id);
	boolean canCancelStockMovement(Long id);
	List<StockMovement> createBulkStockMovements(List<StockMovementRequest> requests);
	int approveBulkStockMovements(List<Long> movementIds, Long approvedByUserId);
	int deleteBulkStockMovements(List<Long> movementIds);
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
	List<StockMovementResponse> getTransfersBetweenWarehouses(Long fromWarehouseId, Long toWarehouseId);
	List<StockMovementResponse> getProductMovementHistory(Long productId, LocalDate startDate, LocalDate endDate);
	List<StockMovementResponse> getWarehouseMovementHistory(Long warehouseId, LocalDate startDate, LocalDate endDate);
	List<StockMovementResponse> getRecentMovements(int limit);
}
