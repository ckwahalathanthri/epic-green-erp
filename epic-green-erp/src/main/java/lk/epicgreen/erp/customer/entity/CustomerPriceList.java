package lk.epicgreen.erp.customer.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CustomerPriceList entity
 * Represents customer-specific product pricing
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customer_price_lists", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_customer_product", columnNames = {"customer_id", "product_id"})
    },
    indexes = {
        @Index(name = "idx_customer_price_customer", columnList = "customer_id"),
        @Index(name = "idx_customer_price_product", columnList = "product_id"),
        @Index(name = "idx_customer_price_valid", columnList = "valid_from, valid_to")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPriceList extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_price_customer"))
    private Customer customer;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_price_product"))
    private Product product;
    
    /**
     * Unit price for this customer
     */
    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;
    
    /**
     * Minimum order quantity
     */
    @Column(name = "minimum_order_quantity", precision = 15, scale = 3)
    private BigDecimal minimumOrderQuantity;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Valid from date
     */
    @Column(name = "valid_from")
    private LocalDate validFrom;
    
    /**
     * Valid to date
     */
    @Column(name = "valid_to")
    private LocalDate validTo;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Checks if price is currently valid
     */
    @Transient
    public boolean isValidNow() {
        if (!Boolean.TRUE.equals(isActive)) {
            return false;
        }
        
        LocalDate today = LocalDate.now();
        
        if (validFrom != null && today.isBefore(validFrom)) {
            return false;
        }
        
        if (validTo != null && today.isAfter(validTo)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if price is valid on a specific date
     */
    @Transient
    public boolean isValidOn(LocalDate date) {
        if (!Boolean.TRUE.equals(isActive)) {
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
     * Gets price for quantity (can be extended for volume discounts)
     */
    @Transient
    public BigDecimal getPriceForQuantity(BigDecimal quantity) {
        if (minimumOrderQuantity != null && quantity.compareTo(minimumOrderQuantity) < 0) {
            throw new IllegalArgumentException(
                "Quantity " + quantity + " is less than minimum order quantity " + minimumOrderQuantity
            );
        }
        return unitPrice;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (validFrom == null) {
            validFrom = LocalDate.now();
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
