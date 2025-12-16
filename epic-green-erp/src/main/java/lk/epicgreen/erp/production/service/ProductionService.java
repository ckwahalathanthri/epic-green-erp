package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.ProductionOutputRequest;
import lk.epicgreen.erp.production.entity.ProductionOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Production Service Interface
 * Service for production output operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ProductionService {
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    ProductionOutput createProductionOutput(ProductionOutputRequest request);
    ProductionOutput updateProductionOutput(Long id, ProductionOutputRequest request);
    void deleteProductionOutput(Long id);
    ProductionOutput getProductionOutputById(Long id);
    ProductionOutput getProductionOutputByNumber(String outputNumber);
    List<ProductionOutput> getAllProductionOutputs();
    Page<ProductionOutput> getAllProductionOutputs(Pageable pageable);
    Page<ProductionOutput> searchProductionOutputs(String keyword, Pageable pageable);
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    ProductionOutput verifyProductionOutput(Long id, Long verifiedByUserId, String verificationNotes);
    ProductionOutput postToInventory(Long id);
    ProductionOutput rejectProductionOutput(Long id, String rejectionReason);
    
    // ===================================================================
    // QUALITY OPERATIONS
    // ===================================================================
    
    void recordQualityCheck(Long outputId, Double goodQuantity, Double rejectedQuantity);
    void updateOutputQuantities(Long outputId, Double outputQuantity, Double goodQuantity, Double rejectedQuantity);
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    List<ProductionOutput> getPendingProductionOutputs();
    List<ProductionOutput> getVerifiedProductionOutputs();
    List<ProductionOutput> getPostedProductionOutputs();
    List<ProductionOutput> getRejectedProductionOutputs();
    List<ProductionOutput> getUnverifiedProductionOutputs();
    List<ProductionOutput> getUnpostedProductionOutputs();
    List<ProductionOutput> getTodaysProductionOutputs();
    List<ProductionOutput> getProductionOutputsByWorkOrder(Long workOrderId);
    List<ProductionOutput> getProductionOutputsByProduct(Long productId);
    List<ProductionOutput> getProductionOutputsByProductionLine(Long productionLineId);
    List<ProductionOutput> getProductionOutputsBySupervisor(Long supervisorId);
    List<ProductionOutput> getProductionOutputsByDateRange(LocalDate startDate, LocalDate endDate);
    List<ProductionOutput> getRecentProductionOutputs(int limit);
    Double getTotalOutputByWorkOrder(Long workOrderId);
    Double getTotalGoodQuantityByWorkOrder(Long workOrderId);
    Double getTotalRejectedQuantityByWorkOrder(Long workOrderId);
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    boolean validateProductionOutput(ProductionOutput output);
    boolean canVerifyProductionOutput(Long outputId);
    boolean canPostToInventory(Long outputId);
    boolean canRejectProductionOutput(Long outputId);
    
    // ===================================================================
    // CALCULATIONS
    // ===================================================================
    
    Double calculateQualityRate(Long outputId);
    Double calculateRejectionRate(Long outputId);
    Map<String, Object> calculateProductionMetrics(Long outputId);
    
    // ===================================================================
    // BATCH OPERATIONS
    // ===================================================================
    
    List<ProductionOutput> createBulkProductionOutputs(List<ProductionOutputRequest> requests);
    int verifyBulkProductionOutputs(List<Long> outputIds, Long verifiedByUserId);
    int postBulkToInventory(List<Long> outputIds);
    int deleteBulkProductionOutputs(List<Long> outputIds);
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
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
