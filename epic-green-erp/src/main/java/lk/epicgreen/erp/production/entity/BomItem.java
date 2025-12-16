package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BomItem entity
 * Represents individual ingredients/components in a BOM
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "bom_items", indexes = {
    @Index(name = "idx_bom_item_bom", columnList = "bill_of_materials_id"),
    @Index(name = "idx_bom_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * BOM reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_of_materials_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bom_item_bom"))
    private BillOfMaterials billOfMaterials;
    
    /**
     * Product reference (raw material/component)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bom_item_product"))
    private Product product;
    
    /**
     * Item description (optional override)
     */
    @Column(name = "item_description", length = 500)
    private String itemDescription;
    
    /**
     * Quantity required (per output quantity of BOM)
     */
    @Column(name = "quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;
    
    /**
     * Wastage/Scrap percentage
     */
    @Column(name = "wastage_percentage", precision = 5, scale = 2)
    private BigDecimal wastagePercentage;
    
    /**
     * Actual quantity needed (quantity + wastage)
     */
    @Column(name = "actual_quantity", precision = 15, scale = 3)
    private BigDecimal actualQuantity;
    
    /**
     * Unit cost (cost per unit of raw material)
     */
    @Column(name = "unit_cost", precision = 15, scale = 2)
    private BigDecimal unitCost;
    
    /**
     * Total cost (actual quantity * unit cost)
     */
    @Column(name = "total_cost", precision = 15, scale = 2)
    private BigDecimal totalCost;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Sequence number (for ordering steps in production)
     */
    @Column(name = "sequence_number")
    private Integer sequenceNumber;
    
    /**
     * Item type (RAW_MATERIAL, PACKAGING, CONSUMABLE)
     */
    @Column(name = "item_type", length = 20)
    private String itemType;
    
    /**
     * Is optional component
     */
    @Column(name = "is_optional")
    private Boolean isOptional;
    
    /**
     * Preparation instructions
     */
    @Column(name = "preparation_instructions", length = 500)
    private String preparationInstructions;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Calculates actual quantity including wastage
     */
    @Transient
    public void calculateActualQuantity() {
        if (quantity == null) {
            actualQuantity = BigDecimal.ZERO;
            return;
        }
        
        actualQuantity = quantity;
        
        if (wastagePercentage != null && wastagePercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal wastage = quantity.multiply(wastagePercentage)
                .divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
            actualQuantity = quantity.add(wastage);
        }
    }
    
    /**
     * Calculates total cost
     */
    @Transient
    public void calculateTotalCost() {
        if (actualQuantity == null || unitCost == null) {
            totalCost = BigDecimal.ZERO;
            return;
        }
        
        totalCost = actualQuantity.multiply(unitCost);
    }
    
    /**
     * Gets quantity for specific output quantity
     */
    @Transient
    public BigDecimal getQuantityForOutput(BigDecimal outputQuantity) {
        if (quantity == null || outputQuantity == null) {
            return BigDecimal.ZERO;
        }
        
        // If BOM produces 100 units and this item needs 10 kg,
        // then for 50 units output, we need (50/100) * 10 = 5 kg
        BigDecimal bomOutputQty = billOfMaterials.getOutputQuantity();
        if (bomOutputQty == null || bomOutputQty.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal ratio = outputQuantity.divide(bomOutputQty, 6, RoundingMode.HALF_UP);
        return actualQuantity.multiply(ratio);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isOptional == null) {
            isOptional = false;
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (itemType == null) {
            itemType = "RAW_MATERIAL";
        }
        
        // Calculate actual quantity and total cost
        calculateActualQuantity();
        calculateTotalCost();
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalculate actual quantity and total cost
        calculateActualQuantity();
        calculateTotalCost();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BomItem)) return false;
        BomItem bomItem = (BomItem) o;
        return id != null && id.equals(bomItem.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
