package lk.epicgreen.erp.purchase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lk.epicgreen.erp.common.constants.ValidationMessages;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Create Purchase Order Request DTO
 * Request object for creating purchase orders
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePurchaseOrderRequest {
    
    /**
     * PO date
     */
    @NotNull(message = "PO date is required")
    private LocalDate poDate;
    
    /**
     * Supplier ID
     */
    @NotNull(message = "Supplier is required")
    private Long supplierId;
    
    /**
     * Expected delivery date
     */
    private LocalDate expectedDeliveryDate;
    
    /**
     * Delivery details
     */
    @Size(max = 5000, message = "Delivery address must not exceed 5000 characters")
    private String deliveryAddress;
    
    @Size(max = 100, message = "Contact person name must not exceed 100 characters")
    private String deliveryContactPerson;
    
    @Size(max = 20, message = "Contact number must not exceed 20 characters")
    private String deliveryContactNumber;
    
    /**
     * PO type
     */
    @Size(max = 30, message = "PO type must not exceed 30 characters")
    private String poType;
    
    /**
     * Currency
     */
    @NotBlank(message = "Currency is required")
    @Size(max = 10, message = "Currency code must not exceed 10 characters")
    private String currency;
    
    /**
     * Exchange rate
     */
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
    @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Invalid discount percentage format")
    private BigDecimal discountPercentage;
    
    @DecimalMin(value = "0.0", message = "Tax amount must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid tax amount format")
    private BigDecimal taxAmount;
    
    @DecimalMin(value = "0.0", message = "Tax percentage must be non-negative")
    @DecimalMax(value = "100.0", message = "Tax percentage cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Invalid tax percentage format")
    private BigDecimal taxPercentage;
    
    @DecimalMin(value = "0.0", message = "Shipping cost must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid shipping cost format")
    private BigDecimal shippingCost;
    
    @DecimalMin(value = "0.0", message = "Other charges must be non-negative")
    @Digits(integer = 13, fraction = 2, message = "Invalid other charges format")
    private BigDecimal otherCharges;
    
    /**
     * Payment terms
     */
    @Size(max = 255, message = "Payment terms must not exceed 255 characters")
    private String paymentTerms;
    
    @Min(value = 0, message = "Payment days must be non-negative")
    @Max(value = 365, message = "Payment days cannot exceed 365")
    private Integer paymentDays;
    
    /**
     * Terms and notes
     */
    @Size(max = 5000, message = "Terms and conditions must not exceed 5000 characters")
    private String termsAndConditions;
    
    @Size(max = 5000, message = ValidationMessages.NOTES_MAX_LENGTH)
    private String notes;
    
    @Size(max = 5000, message = "Internal notes must not exceed 5000 characters")
    private String internalNotes;
    
    /**
     * PO items
     */
    @NotEmpty(message = "At least one PO item is required")
    @Valid
    private List<CreatePurchaseOrderItemRequest> items;
    
    /**
     * Create Purchase Order Item Request
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatePurchaseOrderItemRequest {
        
        /**
         * Product ID
         */
        @NotNull(message = "Product is required")
        private Long productId;
        
        /**
         * Item description (optional override)
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
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be positive")
        @Digits(integer = 13, fraction = 2, message = "Invalid unit price format")
        private BigDecimal unitPrice;
        
        /**
         * Discounts
         */
        @DecimalMin(value = "0.0", message = "Discount percentage must be non-negative")
        @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100")
        @Digits(integer = 3, fraction = 2, message = "Invalid discount percentage format")
        private BigDecimal discountPercentage;
        
        @DecimalMin(value = "0.0", message = "Discount amount must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid discount amount format")
        private BigDecimal discountAmount;
        
        /**
         * Tax
         */
        @DecimalMin(value = "0.0", message = "Tax percentage must be non-negative")
        @DecimalMax(value = "100.0", message = "Tax percentage cannot exceed 100")
        @Digits(integer = 3, fraction = 2, message = "Invalid tax percentage format")
        private BigDecimal taxPercentage;
        
        @DecimalMin(value = "0.0", message = "Tax amount must be non-negative")
        @Digits(integer = 13, fraction = 2, message = "Invalid tax amount format")
        private BigDecimal taxAmount;
        
        /**
         * Expected delivery date for this item
         */
        private LocalDate expectedDeliveryDate;
        
        /**
         * Notes
         */
        @Size(max = 500, message = "Notes must not exceed 500 characters")
        private String notes;
    }
}
