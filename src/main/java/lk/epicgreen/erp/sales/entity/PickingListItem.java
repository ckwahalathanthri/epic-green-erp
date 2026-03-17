package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "picking_list_items")
@Data
public class PickingListItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picking_list_id", nullable = false)
    private PickingList pickingList;
    
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200, nullable = false)
    private String productName;
    
    @Column(name = "warehouse_location", length = 100)
    private String warehouseLocation; // Bin location (e.g., A-01-05)
    
    @Column(name = "quantity_to_pick", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantityToPick;
    
    @Column(name = "picked_quantity", precision = 15, scale = 3)
    private BigDecimal pickedQuantity = BigDecimal.ZERO;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "lot_number", length = 50)
    private String lotNumber;
    
    @Column(name = "expiry_date")
    private java.time.LocalDate expiryDate;
    
    @Column(name = "picking_strategy", length = 20)
    private String pickingStrategy; // FIFO, FEFO, LIFO
    
    @Column(name = "is_picked")
    private Boolean isPicked = false;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @PrePersist
    @PreUpdate
    public void updatePickingStatus() {
        if (pickedQuantity != null && quantityToPick != null) {
            isPicked = pickedQuantity.compareTo(quantityToPick) >= 0;
        }
    }
}