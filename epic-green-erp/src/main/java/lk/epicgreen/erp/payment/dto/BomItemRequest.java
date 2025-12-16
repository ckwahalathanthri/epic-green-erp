package lk.epicgreen.erp.production.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BomItem Request DTO
 * DTO for Bill of Materials item operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BomItemRequest {
    
    @NotNull(message = "Material/Product ID is required")
    private Long materialProductId;
    
    private String materialProductCode;
    
    private String materialProductName;
    
    @NotNull(message = "Quantity is required")
    private Double quantity;
    
    private String unit;
    
    private Double unitCost;
    
    private Double totalCost;
    
    private String itemType; // RAW_MATERIAL, SEMI_FINISHED, CONSUMABLE
    
    private Boolean isOptional;
    
    private String alternativeMaterials; // JSON array of alternative product IDs
    
    private Double scrapPercentage;
    
    private Integer sequenceNumber;
    
    private String notes;
}
