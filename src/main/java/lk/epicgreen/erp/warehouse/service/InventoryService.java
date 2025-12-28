package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.dto.request.InventoryRequest;
import lk.epicgreen.erp.warehouse.dto.response.InventoryResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
}
