package lk.epicgreen.erp.production.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * ProductionOutput Request DTO
 * DTO for production output operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOutputRequest {
    
    @NotBlank(message = "Output number is required")
    private String outputNumber;
    
    @NotNull(message = "Output date is required")
    private LocalDate outputDate;
    
    @NotNull(message = "Work order ID is required")
    private Long workOrderId;
    
    private String workOrderNumber;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    @NotNull(message = "Quantity produced is required")
    private Double quantityProduced;
    
    private Double quantityGood;
    
    private Double quantityRejected;
    
    private Double quantityScrap;
    
    private String unit;
    
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;
    
    private String warehouseName;
    
    private String batchNumber;
    
    private String lotNumber;
    
    private LocalDate manufactureDate;
    
    private LocalDate expiryDate;
    
    private String shift;
    
    private String productionLineId;
    
    private String supervisorId;
    
    private String qualityCheckedBy;
    
    private String qualityStatus; // PASSED, FAILED, HOLD
    
    private String description;
    
    private String notes;
    
    private String status; // DRAFT, COMPLETED, VERIFIED, STORED
    
    private Long createdByUserId;
    
    private Long verifiedByUserId;
    
    private String verificationNotes;
}
