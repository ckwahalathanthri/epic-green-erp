package lk.epicgreen.erp.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * TaxRate entity
 * Represents tax rates for products and services (GST, VAT, etc.)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "tax_rates", indexes = {
    @Index(name = "idx_tax_code", columnList = "tax_code"),
    @Index(name = "idx_is_active", columnList = "is_active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRate extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Tax code (unique, e.g., GST18, VAT12)
     */
    @NotBlank(message = "Tax code is required")
    @Size(max = 20)
    @Column(name = "tax_code", nullable = false, unique = true, length = 20)
    private String taxCode;
    
    /**
     * Tax name (display name)
     */
    @NotBlank(message = "Tax name is required")
    @Size(max = 100)
    @Column(name = "tax_name", nullable = false, length = 100)
    private String taxName;
    
    /**
     * Tax percentage (e.g., 18.00 for 18%)
     */
    @NotNull(message = "Tax percentage is required")
    @Positive(message = "Tax percentage must be positive")
    @Column(name = "tax_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxPercentage;
    
    /**
     * Tax type (GST, VAT, SALES_TAX, SERVICE_TAX, OTHER)
     */
    @NotBlank(message = "Tax type is required")
    @Column(name = "tax_type", nullable = false, length = 20)
    private String taxType;
    
    /**
     * Applicable from date
     */
    @NotNull(message = "Applicable from date is required")
    @Column(name = "applicable_from", nullable = false)
    private LocalDate applicableFrom;
    
    /**
     * Applicable to date (null = no end date)
     */
    @Column(name = "applicable_to")
    private LocalDate applicableTo;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
    
    /**
     * Check if tax rate is applicable for a given date
     */
    @Transient
    public boolean isApplicableForDate(LocalDate date) {
        if (date == null || !isActive()) {
            return false;
        }
        
        boolean afterStart = !date.isBefore(applicableFrom);
        boolean beforeEnd = applicableTo == null || !date.isAfter(applicableTo);
        
        return afterStart && beforeEnd;
    }
    
    /**
     * Check if tax rate is currently applicable
     */
    @Transient
    public boolean isCurrentlyApplicable() {
        return isApplicableForDate(LocalDate.now());
    }
    
    /**
     * Calculate tax amount for given base amount
     */
    @Transient
    public BigDecimal calculateTaxAmount(BigDecimal baseAmount) {
        if (baseAmount == null || taxPercentage == null) {
            return BigDecimal.ZERO;
        }
        return baseAmount.multiply(taxPercentage).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate gross amount (base + tax)
     */
    @Transient
    public BigDecimal calculateGrossAmount(BigDecimal baseAmount) {
        if (baseAmount == null) {
            return BigDecimal.ZERO;
        }
        return baseAmount.add(calculateTaxAmount(baseAmount));
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaxRate)) return false;
        TaxRate taxRate = (TaxRate) o;
        return id != null && id.equals(taxRate.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
