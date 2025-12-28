package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.request.StockAdjustmentRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockAdjustmentResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for StockAdjustment entity business logic
 * 
 * Stock Adjustment Workflow:
 * 1. DRAFT - Initial creation, can be edited
 * 2. PENDING_APPROVAL - Submitted for approval, cannot be edited
 * 3. APPROVED - Approved, stock movements created
 * 4. REJECTED - Rejected, no stock movements
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface StockAdjustmentService {

    StockAdjustmentResponse createStockAdjustment(StockAdjustmentRequest request);
    StockAdjustmentResponse updateStockAdjustment(Long id, StockAdjustmentRequest request);
    void submitForApproval(Long id);
    void approve(Long id, Long approvedBy);
    void reject(Long id, Long rejectedBy, String reason);
    void deleteStockAdjustment(Long id);
    StockAdjustmentResponse getStockAdjustmentById(Long id);
    PageResponse<StockAdjustmentResponse> getAllStockAdjustments(Pageable pageable);
    List<StockAdjustmentResponse> getAdjustmentsByWarehouse(Long warehouseId);
    PageResponse<StockAdjustmentResponse> getAdjustmentsByType(String adjustmentType, Pageable pageable);
    List<StockAdjustmentResponse> getAdjustmentsByStatus(String status);
    List<StockAdjustmentResponse> getPendingApprovals();
    List<StockAdjustmentResponse> getApprovedAdjustments(Long warehouseId);
}
