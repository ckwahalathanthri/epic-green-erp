package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;

/**
 * UnitOfMeasure entity
 * Represents units of measurement for products (kg, liter, piece, etc.)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "units_of_measure", indexes = {
    @Index(name = "idx_uom_code", columnList = "uom_code"),
    @Index(name = "idx_uom_type", columnList = "uom_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitOfMeasure extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * UOM code (unique, e.g., KG, L, PCS)
     */
    @NotBlank(message = "UOM code is required")
    @Size(max = 10)
    @Column(name = "uom_code", nullable = false, unique = true, length = 10)
    private String uomCode;
    
    /**
     * UOM name (display name, e.g., Kilogram, Liter, Pieces)
     */
    @NotBlank(message = "UOM name is required")
    @Size(max = 50)
    @Column(name = "uom_name", nullable = false, length = 50)
    private String uomName;
    
    /**
     * UOM type (WEIGHT, VOLUME, LENGTH, QUANTITY, AREA)
     */
    @NotBlank(message = "UOM type is required")
    @Column(name = "uom_type", nullable = false, length = 20)
    private String uomType;
    
    /**
     * Is base unit (e.g., KG for weight, L for volume)
     */
    @Column(name = "base_unit")
    private Boolean baseUnit;
    
    /**
     * Conversion factor to base unit
     * Example: 1 gram = 0.001 kg (conversion_factor = 0.001)
     */
    @Positive(message = "Conversion factor must be positive")
    @Column(name = "conversion_factor", precision = 10, scale = 4)
    private BigDecimal conversionFactor;
    
    /**
     * Base UOM reference (for conversion)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_uom_id", foreignKey = @ForeignKey(name = "fk_uom_base_uom"))
    private UnitOfMeasure baseUom;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Check if this is a base unit
     */
    @Transient
    public boolean isBaseUnit() {
        return Boolean.TRUE.equals(baseUnit);
    }
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Convert quantity to base unit
     */
    @Transient
    public BigDecimal convertToBaseUnit(BigDecimal quantity) {
        if (quantity == null || conversionFactor == null) {
            return quantity;
        }
        return quantity.multiply(conversionFactor);
    }
    
    /**
     * Convert quantity from base unit
     */
    @Transient
    public BigDecimal convertFromBaseUnit(BigDecimal baseQuantity) {
        if (baseQuantity == null || conversionFactor == null || conversionFactor.compareTo(BigDecimal.ZERO) == 0) {
            return baseQuantity;
        }
        return baseQuantity.divide(conversionFactor, 4, java.math.RoundingMode.HALF_UP);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (baseUnit == null) {
            baseUnit = false;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (conversionFactor == null) {
            conversionFactor = BigDecimal.ONE;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnitOfMeasure)) return false;
        UnitOfMeasure that = (UnitOfMeasure) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
