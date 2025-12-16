package lk.epicgreen.erp.purchase.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * GoodsReceiptNote (GRN) entity
 * Represents goods receipt when items are received from suppliers
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "goods_receipt_notes", indexes = {
    @Index(name = "idx_grn_number", columnList = "grn_number"),
    @Index(name = "idx_grn_date", columnList = "grn_date"),
    @Index(name = "idx_grn_po", columnList = "purchase_order_id"),
    @Index(name = "idx_grn_supplier", columnList = "supplier_id"),
    @Index(name = "idx_grn_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptNote extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * GRN number (unique identifier)
     */
    @Column(name = "grn_number", nullable = false, unique = true, length = 50)
    private String grnNumber;
    
    /**
     * GRN date
     */
    @Column(name = "grn_date", nullable = false)
    private LocalDate grnDate;
    
    /**
     * GRN timestamp
     */
    @Column(name = "grn_timestamp", nullable = false)
    private LocalDateTime grnTimestamp;
    
    /**
     * Purchase order reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_grn_purchase_order"))
    private PurchaseOrder purchaseOrder;
    
    /**
     * Supplier reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false, foreignKey = @ForeignKey(name = "fk_grn_supplier"))
    private Supplier supplier;
    
    /**
     * Warehouse where goods are received
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_grn_warehouse"))
    private Warehouse warehouse;
    
    /**
     * Supplier invoice number
     */
    @Column(name = "supplier_invoice_number", length = 50)
    private String supplierInvoiceNumber;
    
    /**
     * Supplier invoice date
     */
    @Column(name = "supplier_invoice_date")
    private LocalDate supplierInvoiceDate;
    
    /**
     * Delivery note number (from supplier)
     */
    @Column(name = "delivery_note_number", length = 50)
    private String deliveryNoteNumber;
    
    /**
     * Vehicle number
     */
    @Column(name = "vehicle_number", length = 30)
    private String vehicleNumber;
    
    /**
     * Driver name
     */
    @Column(name = "driver_name", length = 100)
    private String driverName;
    
    /**
     * Driver contact
     */
    @Column(name = "driver_contact", length = 20)
    private String driverContact;
    
    /**
     * Received by (person who received the goods)
     */
    @Column(name = "received_by", nullable = false, length = 50)
    private String receivedBy;
    
    /**
     * Inspected by (quality inspector)
     */
    @Column(name = "inspected_by", length = 50)
    private String inspectedBy;
    
    /**
     * Inspection date
     */
    @Column(name = "inspection_date")
    private LocalDate inspectionDate;
    
    /**
     * Inspection status (PENDING, PASSED, FAILED, CONDITIONAL)
     */
    @Column(name = "inspection_status", length = 20)
    private String inspectionStatus;
    
    /**
     * Inspection remarks
     */
    @Column(name = "inspection_remarks", columnDefinition = "TEXT")
    private String inspectionRemarks;
    
    /**
     * Total items count
     */
    @Column(name = "total_items")
    private Integer totalItems;
    
    /**
     * Total received quantity (sum of all items)
     */
    @Column(name = "total_received_quantity", precision = 15, scale = 3)
    private BigDecimal totalReceivedQuantity;
    
    /**
     * Total accepted quantity (after inspection)
     */
    @Column(name = "total_accepted_quantity", precision = 15, scale = 3)
    private BigDecimal totalAcceptedQuantity;
    
    /**
     * Total rejected quantity
     */
    @Column(name = "total_rejected_quantity", precision = 15, scale = 3)
    private BigDecimal totalRejectedQuantity;
    
    /**
     * Status (DRAFT, RECEIVED, INSPECTED, POSTED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Posted to inventory (flag)
     */
    @Column(name = "is_posted")
    private Boolean isPosted;
    
    /**
     * Posted date
     */
    @Column(name = "posted_date")
    private LocalDate postedDate;
    
    /**
     * Posted by
     */
    @Column(name = "posted_by", length = 50)
    private String postedBy;
    
    /**
     * Notes/Remarks
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * GRN items
     */
    @OneToMany(mappedBy = "goodsReceiptNote", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<GrnItem> items = new HashSet<>();
    
    /**
     * Adds a GRN item
     */
    public void addItem(GrnItem item) {
        item.setGoodsReceiptNote(this);
        items.add(item);
    }
    
    /**
     * Removes a GRN item
     */
    public void removeItem(GrnItem item) {
        items.remove(item);
        item.setGoodsReceiptNote(null);
    }
    
    /**
     * Calculates total quantities
     */
    @Transient
    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            totalReceivedQuantity = BigDecimal.ZERO;
            totalAcceptedQuantity = BigDecimal.ZERO;
            totalRejectedQuantity = BigDecimal.ZERO;
            return;
        }
        
        totalReceivedQuantity = items.stream()
            .map(GrnItem::getReceivedQuantity)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        totalAcceptedQuantity = items.stream()
            .map(GrnItem::getAcceptedQuantity)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        totalRejectedQuantity = items.stream()
            .map(GrnItem::getRejectedQuantity)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Checks if GRN is pending inspection
     */
    @Transient
    public boolean isPendingInspection() {
        return "PENDING".equals(inspectionStatus) || inspectionStatus == null;
    }
    
    /**
     * Checks if inspection passed
     */
    @Transient
    public boolean isInspectionPassed() {
        return "PASSED".equals(inspectionStatus);
    }
    
    /**
     * Checks if inspection failed
     */
    @Transient
    public boolean isInspectionFailed() {
        return "FAILED".equals(inspectionStatus);
    }
    
    /**
     * Checks if GRN can be posted to inventory
     */
    @Transient
    public boolean canPost() {
        return "INSPECTED".equals(status) && !isPosted && isInspectionPassed();
    }
    
    /**
     * Checks if GRN can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status) || "RECEIVED".equals(status);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (grnTimestamp == null) {
            grnTimestamp = LocalDateTime.now();
        }
        if (status == null) {
            status = "DRAFT";
        }
        if (inspectionStatus == null) {
            inspectionStatus = "PENDING";
        }
        if (isPosted == null) {
            isPosted = false;
        }
        if (totalItems == null) {
            totalItems = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Update total items count
        if (items != null) {
            totalItems = items.size();
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoodsReceiptNote)) return false;
        GoodsReceiptNote that = (GoodsReceiptNote) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
