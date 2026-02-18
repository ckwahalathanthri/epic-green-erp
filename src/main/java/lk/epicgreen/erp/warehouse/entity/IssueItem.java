package lk.epicgreen.erp.warehouse.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_issue_id", nullable = false)
    private StockIssue stockIssue;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private Inventory inventoryItem;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "requested_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal requestedQuantity;
    
    @Column(name = "issued_quantity", precision = 15, scale = 3)
    private BigDecimal issuedQuantity = BigDecimal.ZERO;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id")
    private StorageBin bin;
    
    @Column(name = "batch_number", length = 100)
    private String batchNumber;
    
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "picked_by", length = 100)
    private String pickedBy;
    
    @Column(name = "picked_at")
    private LocalDateTime pickedAt;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    @PreUpdate
    public void calculateTotalCost() {
        if (issuedQuantity != null && unitCost != null) {
            totalCost = issuedQuantity.multiply(unitCost);
        }
    }
}
