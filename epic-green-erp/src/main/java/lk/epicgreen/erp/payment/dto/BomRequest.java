package lk.epicgreen.erp.production.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Bom Request DTO
 * DTO for Bill of Materials operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BomRequest {
    
    @NotBlank(message = "BOM code is required")
    private String bomCode;
    
    @NotBlank(message = "BOM name is required")
    private String bomName;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Output quantity is required")
    private Double outputQuantity;
    
    private String outputUnit;
    
    private String bomType; // STANDARD, VARIANT, PHANTOM
    
    private Integer version;
    
    private String description;
    
    private String notes;
    
    private Double laborCost;
    
    private Double overheadCost;
    
    private Double otherCost;
    
    private Double scrapPercentage;
    
    private Double yieldPercentage;
    
    private Integer leadTimeDays;
    
    private String status; // DRAFT, ACTIVE, INACTIVE, ARCHIVED
    
    private Long createdByUserId;
    
    private Long approvedByUserId;
    
    private String approvalNotes;
    
    @NotNull(message = "BOM items are required")
    private List<BomItemRequest> items;
}
