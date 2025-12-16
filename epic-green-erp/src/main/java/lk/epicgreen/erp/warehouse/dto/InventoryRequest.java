package lk.epicgreen.erp.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Inventory Request DTO
 * DTO for inventory operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Quantity is required")
    private Double quantity;
    
    private Double reservedQuantity;
    
    private Double availableQuantity;
    
    private String unit;
    
    private String batchNumber;
    
    private String lotNumber;
    
    private String serialNumber;
    
    private LocalDate manufactureDate;
    
    private LocalDate expiryDate;
    
    private String location; // Bin location within warehouse
    
    private String zone;
    
    private String aisle;
    
    private String rack;
    
    private String shelf;
    
    private String bin;
    
    private Double unitCost;
    
    private Double totalCost;
    
    private Integer reorderLevel;
    
    private Integer reorderQuantity;
    
    private Integer minStockLevel;
    
    private Integer maxStockLevel;
    
    private String inventoryStatus; // AVAILABLE, RESERVED, DAMAGED, EXPIRED, QUARANTINE
    
    private String qualityStatus; // APPROVED, REJECTED, PENDING
    
    private String notes;
}
