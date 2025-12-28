package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.request.StockMovementRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockMovementResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

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
}
