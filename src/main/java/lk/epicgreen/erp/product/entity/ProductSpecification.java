package lk.epicgreen.erp.product.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Product Specification Entity
 * Stores detailed specifications for products
 */
@Entity
@Table(name = "product_specifications")
@Data
@NoArgsConstructor
public class ProductSpecification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "spec_name", nullable = false)
    private String specName;
    
    @Column(name = "spec_value", nullable = false)
    private String specValue;
    
    @Column(name = "spec_unit")
    private String specUnit;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "is_required")
    private Boolean isRequired = false;
    
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
