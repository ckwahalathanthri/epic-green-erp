package lk.epicgreen.erp.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Create Sales Order Request DTO
 * Request object for creating sales orders
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSalesOrderRequest {
    
    /**
     * Order date
     */
    @NotNull(message = "Order date is required")
    private LocalDate orderDate;
    
    /**
     * Customer ID
     */
    @NotNull(message = "Customer is required")
    private Long customerId;
    
    /**
     * Warehouse ID
     */
    private Long warehouseId;
    
    /**
     * Order details
     */
    @Size(max = 20, message = "Order type must not exceed 20 characters")
    private String orderType;
    
    @Size(max = 50, message = "Customer reference must not exceed 50 characters")
    private String customerReference;
    
    private LocalDate expectedDeliveryDate;
    
    private Long deliveryAddressId;
    
    @Size(max = 5000, message = "Delivery instructions must not exceed 5000 characters")
    private String deliveryInstructions;
    
    /**
     * Currency
     */
    @Size(max = 10, message = "Currency must not exceed 10 characters")
    private String currency;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Exchange rate must be positive")
    @Digits(integer = 9, fraction = 6, message = "Invalid exchange rate format")
    private BigDecimal exchangeRate;
    
    /**
     * Discounts and charges
     */
    @DecimalMin(value = "0.0", message = "Discount amount must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid discount amount format")
    private BigDecimal discountAmount;
    
    @DecimalMin(value = "0.0", message = "Discount percentage must be non-negative")
    @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Invalid discount percentage format")
    private BigDecimal discountPercentage;
    
    @DecimalMin(value = "0.0", message = "Tax amount must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid tax amount format")
    private BigDecimal taxAmount;
    
    @DecimalMin(value = "0.0", message = "Tax percentage must be non-negative")
    @DecimalMax(value = "100.0", message = "Tax percentage cannot exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Invalid tax percentage format")
    private BigDecimal taxPercentage;
    
    @DecimalMin(value = "0.0", message = "Shipping charges must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid shipping charges format")
    private BigDecimal shippingCharges;
    
    @DecimalMin(value = "0.0", message = "Other charges must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid other charges format")
    private BigDecimal otherCharges;
    
    /**
     * Payment
     */
    @Size(max = 255, message = "Payment terms must not exceed 255 characters")
    private String paymentTerms;
    
    @Size(max = 30, message = "Payment method must not exceed 30 characters")
    private String paymentMethod;
    
    /**
     * Sales information
     */
    @Size(max = 50, message = "Sales representative must not exceed 50 characters")
    private String salesRepresentative;
    
    @Size(max = 20, message = "Priority must not exceed 20 characters")
    private String priority;
    
    /**
     * Notes
     */
    @Size(max = 5000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    @Size(max = 5000, message = "Internal notes must not exceed 5000 characters")
    private String internalNotes;
    
    /**
     * Order items
     */
    @NotNull(message = "Order items are required")
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<CreateSalesOrderItemRequest> items;
    
    /**
     * Create Sales Order Item Request
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateSalesOrderItemRequest {
        
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
         * Ordered quantity
         */
        @NotNull(message = "Ordered quantity is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Ordered quantity must be positive")
        @Digits(integer = 12, fraction = 3, message = "Invalid quantity format")
        private BigDecimal orderedQuantity;
        
        /**
         * Unit
         */
        @NotBlank(message = "Unit is required")
        @Size(max = 10, message = "Unit must not exceed 10 characters")
        private String unit;
        
        /**
         * Unit price
         */
        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.0", message = "Unit price must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid unit price format")
        private BigDecimal unitPrice;
        
        /**
         * Discount
         */
        @DecimalMin(value = "0.0", message = "Discount percentage must be non-negative")
        @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100%")
        @Digits(integer = 3, fraction = 2, message = "Invalid discount percentage format")
        private BigDecimal discountPercentage;
        
        @DecimalMin(value = "0.0", message = "Discount amount must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid discount amount format")
        private BigDecimal discountAmount;
        
        /**
         * Tax
         */
        @DecimalMin(value = "0.0", message = "Tax percentage must be non-negative")
        @DecimalMax(value = "100.0", message = "Tax percentage cannot exceed 100%")
        @Digits(integer = 3, fraction = 2, message = "Invalid tax percentage format")
        private BigDecimal taxPercentage;
        
        @DecimalMin(value = "0.0", message = "Tax amount must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid tax amount format")
        private BigDecimal taxAmount;
        
        /**
         * Notes
         */
        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;
    }
}
