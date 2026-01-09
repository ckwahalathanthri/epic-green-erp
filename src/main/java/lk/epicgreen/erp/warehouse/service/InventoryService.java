package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.request.InventoryRequest;
import lk.epicgreen.erp.warehouse.dto.response.InventoryResponse;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Inventory entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest request);
    InventoryResponse updateInventory(Long id, InventoryRequest request);
    void reserveQuantity(Long inventoryId, BigDecimal quantity);
    void releaseReservation(Long inventoryId, BigDecimal quantity);
    void updateQuantityAvailable(Long inventoryId, BigDecimal quantity);
    InventoryResponse getInventoryById(Long id);
    InventoryResponse getInventoryByWarehouseAndProduct(Long warehouseId, Long productId, String batchNumber, Long locationId);
    List<InventoryResponse> getInventoryByBatch(String batchNumber);
    PageResponse<InventoryResponse> getAllInventory(Pageable pageable);
    List<InventoryResponse> getInventoryByWarehouse(Long warehouseId);
    List<InventoryResponse> getInventoryByProduct(Long productId);
    List<InventoryResponse> getLowStockItems();
    List<InventoryResponse> getExpiredStock();
    List<InventoryResponse> getExpiringStock(Integer daysBeforeExpiry);
    List<InventoryResponse> getInventoryByLocation(Long locationId);
    BigDecimal getTotalStockByProduct(Long productId);
    BigDecimal getTotalStockValue(Long warehouseId);
    List<InventoryValuation> getInventoryValuation(Long warehouseId);

    /**
     * DTO for inventory valuation
     */
    class InventoryValuation {
        private Long productId;
        private String productCode;
        private String productName;
        private BigDecimal totalQuantity;
        private BigDecimal averageCost;
        private BigDecimal totalValue;

        public InventoryValuation(Long productId, String productCode, String productName, 
                                 BigDecimal totalQuantity, BigDecimal averageCost, BigDecimal totalValue) {
            this.productId = productId;
            this.productCode = productCode;
            this.productName = productName;
            this.totalQuantity = totalQuantity;
            this.averageCost = averageCost;
            this.totalValue = totalValue;
        }

        public Long getProductId() { return productId; }
        public String getProductCode() { return productCode; }
        public String getProductName() { return productName; }
        public BigDecimal getTotalQuantity() { return totalQuantity; }
        public BigDecimal getAverageCost() { return averageCost; }
        public BigDecimal getTotalValue() { return totalValue; }
    }

    void deleteInventory(Long id);
    InventoryResponse getInventoryByProductAndWarehouse(Long productId, Long warehouseId);
    List<Inventory> getAllInventory();
    PageResponse<InventoryResponse> searchInventory(String keyword, Pageable pageable);
	void addStock(Long id, Double quantity, Double cost);
	void removeStock(Long id, Double quantity);
	void adjustStock(Long id, Double newQuantity, String reason);
	void increaseStock(Long id, Double quantity);
	void decreaseStock(Long id, Double quantity);
	void transferStock(Long fromInventoryId, Long toInventoryId, Double quantity);
	void reserveStock(Long id, Double quantity);
	void releaseReservation(Long id, Double quantity);
	void releaseReservedStock(Long id, Double quantity);
	void allocateStock(Long id, Double quantity);
	void releaseAllocation(Long id, Double quantity);
	void deallocateStock(Long id, Double quantity);
	void recordDamagedStock(Long id, Double quantity);
	void recordExpiredStock(Long id, Double quantity);
	void updateStockLevels(Long id, Integer reorderLevel, Integer maxLevel, Integer minLevel);
	Double getAvailableQuantity(Long productId, Long warehouseId);
	boolean isStockAvailable(Long productId, Long warehouseId, Double requiredQuantity);
    List<Inventory> getOutOfStockItems();
	Map<String, Object> getInventorySummary(Long id);
	Map<String, Object> calculateInventoryMetrics(Long id);
	Map<String, Object> getInventoryStatistics();
	Map<String, Object> getDashboardStatistics();
}
