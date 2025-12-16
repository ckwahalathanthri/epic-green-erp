package lk.epicgreen.erp.production.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Create Work Order Request DTO
 * Request object for creating work orders
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkOrderRequest {
    
    /**
     * Work order date
     */
    @NotNull(message = "Work order date is required")
    private LocalDate woDate;
    
    /**
     * BOM ID
     */
    @NotNull(message = "BOM is required")
    private Long bomId;
    
    /**
     * Output warehouse ID
     */
    private Long outputWarehouseId;
    
    /**
     * Material warehouse ID
     */
    private Long materialWarehouseId;
    
    /**
     * Planned quantity to produce
     */
    @NotNull(message = "Planned quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Planned quantity must be positive")
    @Digits(integer = 12, fraction = 3, message = "Invalid quantity format")
    private BigDecimal plannedQuantity;
    
    /**
     * Unit
     */
    @NotBlank(message = "Unit is required")
    @Size(max = 10, message = "Unit must not exceed 10 characters")
    private String unit;
    
    /**
     * Schedule
     */
    private LocalDate plannedStartDate;
    
    private LocalDate plannedEndDate;
    
    /**
     * Priority
     */
    @Size(max = 20, message = "Priority must not exceed 20 characters")
    private String priority;
    
    /**
     * Supervisor
     */
    @Size(max = 50, message = "Supervisor name must not exceed 50 characters")
    private String supervisor;
    
    /**
     * Shift
     */
    @Size(max = 20, message = "Shift must not exceed 20 characters")
    private String shift;
    
    /**
     * Batch number
     */
    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;
    
    /**
     * Costs (optional overrides)
     */
    @DecimalMin(value = "0.0", message = "Labor cost must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid labor cost format")
    private BigDecimal plannedLaborCost;
    
    @DecimalMin(value = "0.0", message = "Overhead cost must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid overhead cost format")
    private BigDecimal plannedOverheadCost;
    
    /**
     * Quality check required
     */
    private Boolean qualityCheckRequired;
    
    /**
     * Production instructions
     */
    @Size(max = 5000, message = "Production instructions must not exceed 5000 characters")
    private String productionInstructions;
    
    /**
     * Notes
     */
    @Size(max = 5000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    /**
     * Work order items (optional - if not provided, will be created from BOM)
     */
    @Valid
    private List<CreateWorkOrderItemRequest> items;
    
    /**
     * Create Work Order Item Request
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateWorkOrderItemRequest {
        
        /**
         * Product ID
         */
        @NotNull(message = "Product is required")
        private Long productId;
        
        /**
         * Item description (optional)
         */
        @Size(max = 500, message = "Item description must not exceed 500 characters")
        private String itemDescription;
        
        /**
         * Planned quantity
         */
        @NotNull(message = "Planned quantity is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Planned quantity must be positive")
        @Digits(integer = 12, fraction = 3, message = "Invalid quantity format")
        private BigDecimal plannedQuantity;
        
        /**
         * Unit
         */
        @NotBlank(message = "Unit is required")
        @Size(max = 10, message = "Unit must not exceed 10 characters")
        private String unit;
        
        /**
         * Notes
         */
        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;
    }
}
