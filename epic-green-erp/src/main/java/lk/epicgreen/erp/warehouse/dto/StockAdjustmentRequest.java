package lk.epicgreen.erp.warehouse.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Stock Adjustment Request DTO
 * Request object for creating stock adjustments
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAdjustmentRequest {
    
    /**
     * Adjustment date
     */
    @NotNull(message = "Adjustment date is required")
    private LocalDate adjustmentDate;
    
    /**
     * Adjustment type
     */
    @NotBlank(message = "Adjustment type is required")
    private String adjustmentType;
    
    /**
     * Warehouse ID
     */
    @NotNull(message = "Warehouse is required")
    private Long warehouseId;
    
    /**
     * Reason for adjustment
     */
    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
    
    /**
     * Description
     */
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
    
    /**
     * Notes
     */
    @Size(max = 5000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    /**
     * Adjustment items
     */
    @NotEmpty(message = "At least one adjustment item is required")
    @Valid
    private List<StockAdjustmentItemRequest> items;
    
    /**
     * Stock Adjustment Item Request
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StockAdjustmentItemRequest {
        
        /**
         * Product ID
         */
        @NotNull(message = "Product is required")
        private Long productId;
        
        /**
         * Location ID
         */
        private Long locationId;
        
        /**
         * Batch number
         */
        @Size(max = 50, message = "Batch number must not exceed 50 characters")
        private String batchNumber;
        
        /**
         * Serial number
         */
        @Size(max = 50, message = "Serial number must not exceed 50 characters")
        private String serialNumber;
        
        /**
         * System quantity (current quantity in system)
         */
        @NotNull(message = "System quantity is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "System quantity must be non-negative")
        @Digits(integer = 12, fraction = 3, message = "Invalid system quantity format")
        private BigDecimal systemQuantity;
        
        /**
         * Physical quantity (actual counted quantity)
         */
        @NotNull(message = "Physical quantity is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Physical quantity must be non-negative")
        @Digits(integer = 12, fraction = 3, message = "Invalid physical quantity format")
        private BigDecimal physicalQuantity;
        
        /**
         * Unit
         */
        @Size(max = 10, message = "Unit must not exceed 10 characters")
        private String unit;
        
        /**
         * Cost per unit
         */
        @DecimalMin(value = "0.0", inclusive = true, message = "Cost per unit must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid cost format")
        private BigDecimal costPerUnit;
        
        /**
         * Reason for discrepancy
         */
        @Size(max = 500, message = "Reason must not exceed 500 characters")
        private String reason;
        
        /**
         * Notes
         */
        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;
    }
}
