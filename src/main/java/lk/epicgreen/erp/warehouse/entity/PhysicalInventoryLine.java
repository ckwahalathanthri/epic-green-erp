package lk.epicgreen.erp.warehouse.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "physical_inventory_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalInventoryLine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "physical_inventory_id", nullable = false)
    private Long physicalInventoryId;
    
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "location_code", length = 50)
    private String locationCode;
    
    @Column(name = "system_quantity", precision = 15, scale = 3)
    private BigDecimal systemQuantity = BigDecimal.ZERO;
    
    @Column(name = "counted_quantity", precision = 15, scale = 3)
    private BigDecimal countedQuantity;
    
    @Column(name = "variance_quantity", precision = 15, scale = 3)
    private BigDecimal varianceQuantity;
    
    @Column(name = "unit_cost", precision = 15, scale = 4)
    private BigDecimal unitCost;
    
    @Column(name = "variance_value", precision = 15, scale = 2)
    private BigDecimal varianceValue;
    
    @Column(name = "count_status", length = 20)
    private String countStatus; // PENDING, COUNTED, VERIFIED, DISCREPANCY
    
    @Column(name = "recount_required")
    private Boolean recountRequired = false;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @PrePersist
    @PreUpdate
    public void calculateVariance() {
        if (countedQuantity != null && systemQuantity != null) {
            varianceQuantity = countedQuantity.subtract(systemQuantity);
            if (unitCost != null) {
                varianceValue = varianceQuantity.multiply(unitCost);
            }
            // Set discrepancy status if variance is not zero
            if (varianceQuantity.compareTo(BigDecimal.ZERO) != 0) {
                countStatus = "DISCREPANCY";
            }
        }
    }
}
