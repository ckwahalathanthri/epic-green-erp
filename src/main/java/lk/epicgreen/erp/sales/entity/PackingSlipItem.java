package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "packing_slip_items")
@Data
public class PackingSlipItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packing_slip_id", nullable = false)
    private PackingSlip packingSlip;
    
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200, nullable = false)
    private String productName;
    
    @Column(name = "quantity_to_pack", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantityToPack;
    
    @Column(name = "packed_quantity", precision = 15, scale = 3)
    private BigDecimal packedQuantity = BigDecimal.ZERO;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "package_number", length = 50)
    private String packageNumber; // Box/carton number
    
    @Column(name = "package_type", length = 50)
    private String packageType; // BOX, CARTON, PALLET, BAG
    
    @Column(name = "weight", precision = 15, scale = 3)
    private BigDecimal weight;
    
    @Column(name = "is_packed")
    private Boolean isPacked = false;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @PrePersist
    @PreUpdate
    public void updatePackingStatus() {
        if (packedQuantity != null && quantityToPack != null) {
            isPacked = packedQuantity.compareTo(quantityToPack) >= 0;
        }
    }
}