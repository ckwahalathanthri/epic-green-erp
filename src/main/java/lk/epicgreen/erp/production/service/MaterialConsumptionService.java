package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.request.MaterialConsumptionRequest;
import lk.epicgreen.erp.production.dto.response.MaterialConsumptionResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for MaterialConsumption entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface MaterialConsumptionService {

    /**
     * Record material consumption
     */
    MaterialConsumptionResponse recordConsumption(MaterialConsumptionRequest request);

    /**
     * Get material consumption by ID
     */
    MaterialConsumptionResponse getMaterialConsumptionById(Long id);

    /**
     * Get all material consumption records (paginated)
     */
    PageResponse<MaterialConsumptionResponse> getAllMaterialConsumption(Pageable pageable);

    /**
     * Get material consumption by work order
     */
    List<MaterialConsumptionResponse> getConsumptionByWorkOrder(Long woId);

    /**
     * Get material consumption by work order item
     */
    List<MaterialConsumptionResponse> getConsumptionByWorkOrderItem(Long woItemId);

    /**
     * Get material consumption by raw material
     */
    List<MaterialConsumptionResponse> getConsumptionByRawMaterial(Long rawMaterialId);

    /**
     * Get material consumption by warehouse
     */
    List<MaterialConsumptionResponse> getConsumptionByWarehouse(Long warehouseId);

    /**
     * Get material consumption by date range
     */
    List<MaterialConsumptionResponse> getConsumptionByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get total consumption quantity for a raw material in a work order
     */
    BigDecimal getTotalConsumptionByWoAndMaterial(Long woId, Long rawMaterialId);

    /**
     * Get total consumption cost for a work order
     */
    BigDecimal getTotalConsumptionCostByWorkOrder(Long woId);

    /**
     * Search material consumption records
     */
    PageResponse<MaterialConsumptionResponse> searchMaterialConsumption(String keyword, Pageable pageable);
}
