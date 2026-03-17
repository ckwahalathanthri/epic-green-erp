package lk.epicgreen.erp.product.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * Price History Entity
 * Tracks historical price changes
 */
@Entity
@Table(name = "price_history")
@Data
@NoArgsConstructor
public class PriceHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "old_price", precision = 15, scale = 2)
    private BigDecimal oldPrice;
    
    @Column(name = "new_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal newPrice;
    
    @Column(name = "price_type", length = 50)
    private String priceType;
    
    @Column(name = "change_reason", length = 500)
    private String changeReason;
    
    @Column(name = "changed_by")
    private Long changedBy;
    
    @Column(name = "changed_at")
    private LocalDateTime changedAt;
    
    @PrePersist
    protected void onCreate() {
        changedAt = LocalDateTime.now();
    }
}
