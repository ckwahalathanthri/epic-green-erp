package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Sales Quotation Item Entity
 * Represents individual line items in a sales quotation
 */
@Entity
@Table(name = "sales_quotation_items")
@Data
public class SalesQuotationItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    private SalesQuotation quotation;
    
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200, nullable = false)
    private String productName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantity;
    
    @Column(name = "unit_of_measure", length = 20)
    private String unitOfMeasure; // kg, g, units, boxes
    
    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;
    
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "line_total", precision = 15, scale = 2)
    private BigDecimal lineTotal = BigDecimal.ZERO;
    
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Automatically calculate line total before persist and update
     */
    @PrePersist
    @PreUpdate
    public void calculateLineTotal() {
        if (quantity == null || unitPrice == null) {
            lineTotal = BigDecimal.ZERO;
            return;
        }
        
        // Calculate base amount
        BigDecimal baseAmount = quantity.multiply(unitPrice);
        
        // Calculate discount amount
        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = baseAmount.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
        } else {
            discountAmount = BigDecimal.ZERO;
        }
        
        // Calculate amount after discount
        BigDecimal amountAfterDiscount = baseAmount.subtract(discountAmount);
        
        // Calculate tax amount (if applicable at line level)
        if (taxRate != null && taxRate.compareTo(BigDecimal.ZERO) > 0) {
            taxAmount = amountAfterDiscount.multiply(taxRate).divide(BigDecimal.valueOf(100));
        } else {
            taxAmount = BigDecimal.ZERO;
        }
        
        // Line total (without tax - tax is usually calculated at header level)
        lineTotal = amountAfterDiscount;
    }
}
