package lk.epicgreen.erp.product.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product Price Entity
 * Tracks pricing information for products
 */
@Entity
@Table(name = "product_prices")
@Data
@NoArgsConstructor
public class ProductPrice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "price_type", length = 50)
    private String priceType; // STANDARD, WHOLESALE, RETAIL, SPECIAL
    
    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;
    
    @Column(name = "min_quantity", precision = 15, scale = 2)
    private BigDecimal minQuantity;
    
    @Column(name = "max_quantity", precision = 15, scale = 2)
    private BigDecimal maxQuantity;
    
    @Column(name = "currency", length = 10)
    private String currency = "USD";
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;
    
    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
