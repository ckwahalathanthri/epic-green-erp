package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.request.ProductionOutputRequest;
import lk.epicgreen.erp.production.dto.response.ProductionOutputResponse;
import lk.epicgreen.erp.common.dto.PageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    ProductionOutputResponse createProductionOutput(ProductionOutputRequest request);

	lk.epicgreen.erp.production.dto.response.ProductionOutputResponse updateProductionOutput(Long id,
			ProductionOutputRequest request);

	void deleteProductionOutput(Long id);

	lk.epicgreen.erp.production.dto.response.ProductionOutputResponse getProductionOutputByNumber(String outputNumber);

	Page<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getAllProductionOutputs(Pageable pageable);

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getAllProductionOutputs();

	Page<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> searchProductionOutputs(String keyword,
			Pageable pageable);

	lk.epicgreen.erp.production.dto.response.ProductionOutputResponse verifyProductionOutput(Long id,
			Long verifiedByUserId, String verificationNotes);

	lk.epicgreen.erp.production.dto.response.ProductionOutputResponse postToInventory(Long id);

	lk.epicgreen.erp.production.dto.response.ProductionOutputResponse rejectProductionOutput(Long id,
			String rejectionReason);

	void recordQualityCheck(Long id, String qualityStatus);

	void updateOutputQuantities(Long id, Double goodQuantity, Double rejectedQuantity);

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getPendingProductionOutputs();

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getVerifiedProductionOutputs();

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getPostedProductionOutputs();

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getRejectedProductionOutputs();

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getUnverifiedProductionOutputs();

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getUnpostedProductionOutputs();

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getTodaysProductionOutputs();

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getProductionOutputsByWorkOrder(
			Long workOrderId);

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getProductionOutputsByProduct(
			Long productId);

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getProductionOutputsByProductionLine(
			Long productionLineId);

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getProductionOutputsBySupervisor(
			Long supervisorId);

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getProductionOutputsByDateRange(
			LocalDate startDate, LocalDate endDate);

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> getRecentProductionOutputs(int limit);

	Double getTotalGoodQuantityByWorkOrder(Long workOrderId);

	Double getTotalRejectedQuantityByWorkOrder(Long workOrderId);

	boolean canVerifyProductionOutput(Long id);

	boolean canPostToInventory(Long id);

	boolean canRejectProductionOutput(Long id);

	Double calculateQualityRate(Long id);

	Double calculateRejectionRate(Long id);

	Map<String, Object> calculateProductionMetrics(Long id);

	List<lk.epicgreen.erp.production.dto.response.ProductionOutputResponse> createBulkProductionOutputs(
			List<ProductionOutputRequest> requests);

	int verifyBulkProductionOutputs(List<Long> outputIds, Long verifiedByUserId);

	int postBulkToInventory(List<Long> outputIds);

	int deleteBulkProductionOutputs(List<Long> outputIds);

	Map<String, Object> getProductionOutputStatistics();

	List<Map<String, Object>> getOutputTypeDistribution();

	List<Map<String, Object>> getStatusDistribution();

	List<Map<String, Object>> getMonthlyProductionOutput(LocalDate startDate, LocalDate endDate);

	List<Map<String, Object>> getProductionByProduct();

	List<Map<String, Object>> getProductionByProductionLine();

	Double getQualityRate();

	Double getRejectionRate();

	Map<String, Object> getDashboardStatistics();
}
