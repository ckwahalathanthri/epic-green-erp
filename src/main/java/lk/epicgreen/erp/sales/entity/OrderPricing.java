package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Order Pricing Entity
 * Manages pricing rules, discounts, and special offers
 */
@Entity
@Table(name = "order_pricing_rules")
@Data
public class OrderPricing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rule_code", unique = true, length = 50)
    private String ruleCode;
    
    @Column(name = "rule_name", length = 200, nullable = false)
    private String ruleName;
    
    @Column(name = "rule_type", length = 50, nullable = false)
    private String ruleType; // VOLUME_DISCOUNT, SEASONAL_DISCOUNT, CUSTOMER_SPECIFIC, PRODUCT_SPECIFIC
    
    @Column(name = "discount_type", length = 20)
    private String discountType; // PERCENTAGE, FIXED_AMOUNT
    
    @Column(name = "discount_value", precision = 15, scale = 2)
    private BigDecimal discountValue;
    
    @Column(name = "min_order_amount", precision = 15, scale = 2)
    private BigDecimal minOrderAmount;
    
    @Column(name = "min_quantity", precision = 15, scale = 3)
    private BigDecimal minQuantity;
    
    @Column(name = "customer_id")
    private Long customerId; // Null if applicable to all customers
    
    @Column(name = "product_id")
    private Long productId; // Null if applicable to all products
    
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;
    
    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "priority")
    private Integer priority = 0; // Higher priority rules applied first
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void setTimestamps() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    public boolean isValidNow() {
        LocalDate today = LocalDate.now();
        return isActive && !today.isBefore(validFrom) && !today.isAfter(validTo);
    }
    
    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!isValidNow()) {
            return BigDecimal.ZERO;
        }
        
        if ("PERCENTAGE".equals(discountType)) {
            return orderAmount.multiply(discountValue).divide(BigDecimal.valueOf(100));
        } else if ("FIXED_AMOUNT".equals(discountType)) {
            return discountValue;
        }
        
        return BigDecimal.ZERO;
    }
}