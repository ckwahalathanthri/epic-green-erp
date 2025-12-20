package lk.epicgreen.erp.warehouse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Inventory Request DTO
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;
    
    private String warehouseName;
    
    @DecimalMin(value = "0.0", message = "Quantity on hand must be positive")
    private Double quantityOnHand;
    
    @DecimalMin(value = "0.0", message = "Quantity reserved must be positive")
    private Double quantityReserved;
    
    @DecimalMin(value = "0.0", message = "Quantity allocated must be positive")
    private Double quantityAllocated;
    
    @DecimalMin(value = "0.0", message = "Quantity damaged must be positive")
    private Double quantityDamaged;
    
    @DecimalMin(value = "0.0", message = "Quantity expired must be positive")
    private Double quantityExpired;
    
    @Min(value = 0, message = "Reorder level must be positive")
    private Integer reorderLevel;
    
    @Min(value = 0, message = "Reorder quantity must be positive")
    private Integer reorderQuantity;
    
    @Min(value = 0, message = "Max stock level must be positive")
    private Integer maxStockLevel;
    
    @Min(value = 0, message = "Min stock level must be positive")
    private Integer minStockLevel;
    
    @DecimalMin(value = "0.0", message = "Unit cost must be positive")
    private Double unitCost;
    
    @DecimalMin(value = "0.0", message = "Average cost must be positive")
    private Double averageCost;
    
    @DecimalMin(value = "0.0", message = "Last cost must be positive")
    private Double lastCost;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
}
