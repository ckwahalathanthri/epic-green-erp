package lk.epicgreen.erp.purchase.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * GrnItem entity
 * Represents line items in goods receipt notes
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "grn_items", indexes = {
    @Index(name = "idx_grn_item_grn", columnList = "goods_receipt_note_id"),
    @Index(name = "idx_grn_item_po_item", columnList = "po_item_id"),
    @Index(name = "idx_grn_item_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrnItem extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Goods receipt note reference (header)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_note_id", nullable = false, foreignKey = @ForeignKey(name = "fk_grn_item_grn"))
    private GoodsReceiptNote goodsReceiptNote;
    
    /**
     * PO item reference (original PO line item)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_grn_item_po_item"))
    private PurchaseOrderItem poItem;
    
    /**
     * Product reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_grn_item_product"))
    private Product product;
    
    /**
     * Warehouse location where item is stored
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_grn_item_location"))
    private WarehouseLocation location;
    
    /**
     * Batch number assigned
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    /**
     * Serial number (for serialized items)
     */
    @Column(name = "serial_number", length = 50)
    private String serialNumber;
    
    /**
     * Ordered quantity (from PO)
     */
    @Column(name = "ordered_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal orderedQuantity;
    
    /**
     * Received quantity (actual received)
     */
    @Column(name = "received_quantity", precision = 15, scale = 3, nullable = false)
    private BigDecimal receivedQuantity;
    
    /**
     * Accepted quantity (after inspection)
     */
    @Column(name = "accepted_quantity", precision = 15, scale = 3)
    private BigDecimal acceptedQuantity;
    
    /**
     * Rejected quantity
     */
    @Column(name = "rejected_quantity", precision = 15, scale = 3)
    private BigDecimal rejectedQuantity;
    
    /**
     * Damaged quantity
     */
    @Column(name = "damaged_quantity", precision = 15, scale = 3)
    private BigDecimal damagedQuantity;
    
    /**
     * Unit of measurement
     */
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;
    
    /**
     * Unit price (from PO)
     */
    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;
    
    /**
     * Manufacturing date
     */
    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;
    
    /**
     * Expiry date
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    /**
     * Quality status (GOOD, ACCEPTABLE, POOR, REJECTED)
     */
    @Column(name = "quality_status", length = 20)
    private String qualityStatus;
    
    /**
     * Quality remarks
     */
    @Column(name = "quality_remarks", length = 500)
    private String qualityRemarks;
    
    /**
     * Moisture content (%) - for quality control
     */
    @Column(name = "moisture_content", precision = 5, scale = 2)
    private BigDecimal moistureContent;
    
    /**
     * Purity (%) - for quality control
     */
    @Column(name = "purity", precision = 5, scale = 2)
    private BigDecimal purity;
    
    /**
     * Sample weight (for quality testing)
     */
    @Column(name = "sample_weight", precision = 10, scale = 3)
    private BigDecimal sampleWeight;
    
    /**
     * Rejection reason
     */
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Gets total value (received quantity * unit price)
     */
    @Transient
    public BigDecimal getTotalValue() {
        if (receivedQuantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return receivedQuantity.multiply(unitPrice);
    }
    
    /**
     * Gets accepted value
     */
    @Transient
    public BigDecimal getAcceptedValue() {
        if (acceptedQuantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return acceptedQuantity.multiply(unitPrice);
    }
    
    /**
     * Gets variance quantity (received - ordered)
     */
    @Transient
    public BigDecimal getVarianceQuantity() {
        if (receivedQuantity == null || orderedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return receivedQuantity.subtract(orderedQuantity);
    }
    
    /**
     * Gets variance percentage
     */
    @Transient
    public BigDecimal getVariancePercentage() {
        if (orderedQuantity == null || orderedQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal variance = getVarianceQuantity();
        return variance.divide(orderedQuantity, 4, RoundingMode.HALF_UP)
                      .multiply(new BigDecimal("100"));
    }
    
    /**
     * Checks if quantity matches ordered quantity
     */
    @Transient
    public boolean isFullyReceived() {
        if (receivedQuantity == null || orderedQuantity == null) {
            return false;
        }
        return receivedQuantity.compareTo(orderedQuantity) >= 0;
    }
    
    /**
     * Checks if there are rejected items
     */
    @Transient
    public boolean hasRejection() {
        return rejectedQuantity != null && rejectedQuantity.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Checks if item quality is acceptable
     */
    @Transient
    public boolean isQualityAcceptable() {
        return "GOOD".equals(qualityStatus) || "ACCEPTABLE".equals(qualityStatus);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (acceptedQuantity == null) {
            acceptedQuantity = receivedQuantity;
        }
        if (rejectedQuantity == null) {
            rejectedQuantity = BigDecimal.ZERO;
        }
        if (damagedQuantity == null) {
            damagedQuantity = BigDecimal.ZERO;
        }
        if (qualityStatus == null) {
            qualityStatus = "GOOD";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Ensure rejected + accepted = received
        if (receivedQuantity != null && acceptedQuantity != null) {
            rejectedQuantity = receivedQuantity.subtract(acceptedQuantity);
            if (rejectedQuantity.compareTo(BigDecimal.ZERO) < 0) {
                rejectedQuantity = BigDecimal.ZERO;
            }
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrnItem)) return false;
        GrnItem grnItem = (GrnItem) o;
        return id != null && id.equals(grnItem.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
