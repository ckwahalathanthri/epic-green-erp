package lk.epicgreen.erp.warehouse.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "grn_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GRNItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grn_id", nullable = false)
    private GoodsReceiptNote grn;


    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private Inventory inventoryItem;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "ordered_quantity", precision = 15, scale = 3)
    private BigDecimal orderedQuantity;
    
    @Column(name = "received_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal receivedQuantity;
    
    @Column(name = "rejected_quantity", precision = 15, scale = 3)
    private BigDecimal rejectedQuantity = BigDecimal.ZERO;
    
    @Column(name = "accepted_quantity", precision = 15, scale = 3)
    private BigDecimal acceptedQuantity;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "batch_number", length = 100)
    private String batchNumber;
    
    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id")
    private StorageBin bin;
    
    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue;
    
    @Column(name = "quality_status", length = 50)
    private String qualityStatus;
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    @PreUpdate
    public void calculate() {
        if (receivedQuantity != null && rejectedQuantity != null) {
            acceptedQuantity = receivedQuantity.subtract(rejectedQuantity);
        }
        if (acceptedQuantity != null && unitPrice != null) {
            totalValue = acceptedQuantity.multiply(unitPrice);
        }
    }
}
