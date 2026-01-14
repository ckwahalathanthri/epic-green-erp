package lk.epicgreen.erp.customer.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * CustomerPriceList entity
 * Represents customer-specific pricing and discounts
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customer_price_lists", indexes = {
    @Index(name = "idx_customer_product", columnList = "customer_id, product_id"),
    @Index(name = "idx_valid_dates", columnList = "valid_from, valid_to")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPriceList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer reference
     */
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_price_list_customer"))
    private Customer customer;
    
    /**
     * Product reference
     */
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_price_list_product"))
    private Product product;
    
    /**
     * Special price for this customer
     */
    @NotNull(message = "Special price is required")
    @Positive(message = "Special price must be positive")
    @Column(name = "special_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal specialPrice;
    
    /**
     * Discount percentage (0-100)
     */
    @DecimalMin(value = "0.00", message = "Discount percentage must be positive or zero")
    @DecimalMax(value = "100.00", message = "Discount percentage cannot exceed 100")
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;
    
    /**
     * Minimum quantity for this price
     */
    @PositiveOrZero(message = "Minimum quantity must be positive or zero")
    @Column(name = "min_quantity", precision = 15, scale = 3)
    private BigDecimal minQuantity;
    
    /**
     * Valid from date
     */
    @NotNull(message = "Valid from date is required")
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;
    
    /**
     * Valid to date (null = no end date)
     */
    @Column(name = "valid_to")
    private LocalDate validTo;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Created by (user ID)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Check if currently valid
     */
    @Transient
    public boolean isCurrentlyValid() {
        LocalDate today = LocalDate.now();
        if (!isActive()) {
            return false;
        }
        if (validFrom != null && today.isBefore(validFrom)) {
            return false;
        }
        if (validTo != null && today.isAfter(validTo)) {
            return false;
        }
        return true;
    }
    
    /**
     * Check if valid on a specific date
     */
    @Transient
    public boolean isValidOn(LocalDate date) {
        if (!isActive()) {
            return false;
        }
        if (validFrom != null && date.isBefore(validFrom)) {
            return false;
        }
        if (validTo != null && date.isAfter(validTo)) {
            return false;
        }
        return true;
    }
    
    /**
     * Check if quantity meets minimum requirement
     */
    @Transient
    public boolean meetsMinimumQuantity(BigDecimal quantity) {
        if (minQuantity == null) {
            return true;
        }
        return quantity != null && quantity.compareTo(minQuantity) >= 0;
    }
    
    /**
     * Calculate final price with discount
     */
    @Transient
    public BigDecimal getFinalPrice() {
        if (discountPercentage == null || discountPercentage.compareTo(BigDecimal.ZERO) == 0) {
            return specialPrice;
        }
        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
            discountPercentage.divide(new BigDecimal("100"), 4, java.math.RoundingMode.HALF_UP)
        );
        return specialPrice.multiply(discountMultiplier).setScale(2, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate discount amount
     */
    @Transient
    public BigDecimal getDiscountAmount() {
        if (discountPercentage == null || discountPercentage.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return specialPrice.subtract(getFinalPrice());
    }
    
    /**
     * Calculate total price for quantity
     */
    @Transient
    public BigDecimal getTotalPriceForQuantity(BigDecimal quantity) {
        if (quantity == null) {
            return BigDecimal.ZERO;
        }
        return getFinalPrice().multiply(quantity).setScale(2, java.math.RoundingMode.HALF_UP);
    }
    
    @PrePersist
    protected void onCreate() {
        if (isActive == null) {
            isActive = true;
        }
        if (discountPercentage == null) {
            discountPercentage = BigDecimal.ZERO;
        }
        if (minQuantity == null) {
            minQuantity = new BigDecimal("1.000");
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerPriceList)) return false;
        CustomerPriceList that = (CustomerPriceList) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
