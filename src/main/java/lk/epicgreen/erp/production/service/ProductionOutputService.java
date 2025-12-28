package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.request.ProductionOutputRequest;
import lk.epicgreen.erp.production.dto.response.ProductionOutputResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for ProductionOutput entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ProductionOutputService {

    /**
     * Record production output
     */
    ProductionOutputResponse recordOutput(ProductionOutputRequest request);

    /**
     * Update production output
     */
    ProductionOutputResponse updateOutput(Long id, ProductionOutputRequest request);

    /**
     * Pass quality check (PENDING → PASSED)
     */
    void passQualityCheck(Long id, Long qualityCheckedBy);

    /**
     * Fail quality check (PENDING → FAILED)
     */
    void failQualityCheck(Long id, Long qualityCheckedBy, String remarks);

    /**
     * Delete production output
     */
    void deleteOutput(Long id);

    /**
     * Get production output by ID
     */
    ProductionOutputResponse getProductionOutputById(Long id);

    /**
     * Get all production output records (paginated)
     */
    PageResponse<ProductionOutputResponse> getAllProductionOutput(Pageable pageable);

    /**
     * Get production output by work order
     */
    List<ProductionOutputResponse> getOutputByWorkOrder(Long woId);

    /**
     * Get production output by finished product
     */
    List<ProductionOutputResponse> getOutputByFinishedProduct(Long finishedProductId);

    /**
     * Get production output by warehouse
     */
    List<ProductionOutputResponse> getOutputByWarehouse(Long warehouseId);

    /**
     * Get production output by batch number
     */
    List<ProductionOutputResponse> getOutputByBatchNumber(String batchNumber);

    /**
     * Get production output by quality status
     */
    PageResponse<ProductionOutputResponse> getOutputByQualityStatus(String qualityStatus, Pageable pageable);

    /**
     * Get production output by date range
     */
    List<ProductionOutputResponse> getOutputByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get expired production output
     */
    List<ProductionOutputResponse> getExpiredOutput();

    /**
     * Get production output expiring soon
     */
    List<ProductionOutputResponse> getOutputExpiringSoon(Integer daysBeforeExpiry);

    /**
     * Get pending quality check items
     */
    List<ProductionOutputResponse> getPendingQualityCheck();

    /**
     * Get total output quantity for a work order
     */
    BigDecimal getTotalOutputByWorkOrder(Long woId);

    /**
     * Get accepted output quantity for a work order
     */
    BigDecimal getAcceptedOutputByWorkOrder(Long woId);

    /**
     * Calculate quality pass rate for a work order (%)
     */
    Double calculateQualityPassRate(Long woId);

    /**
     * Search production output records
     */
    PageResponse<ProductionOutputResponse> searchProductionOutput(String keyword, Pageable pageable);
}
