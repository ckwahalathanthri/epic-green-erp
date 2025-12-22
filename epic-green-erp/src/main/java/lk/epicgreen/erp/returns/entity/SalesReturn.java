package lk.epicgreen.erp.returns.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.sales.entity.Invoice;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * SalesReturn entity
 * Represents customer returns of goods
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_returns", indexes = {
        @Index(name = "idx_sales_return_number", columnList = "return_number"),
        @Index(name = "idx_sales_return_date", columnList = "return_date"),
        @Index(name = "idx_sales_return_customer", columnList = "customer_id"),
        @Index(name = "idx_sales_return_invoice", columnList = "invoice_id"),
        @Index(name = "idx_sales_return_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReturn extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Return number (unique identifier)
     */
    @Column(name = "return_number", nullable = false, unique = true, length = 50)
    private String returnNumber;

    /**
     * Return date
     */
    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_customer"))
    private Customer customer;

    /**
     * Customer ID (denormalized for queries)
     */
    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    /**
     * Customer name (denormalized for display)
     */
    @Column(name = "customer_name", length = 200)
    private String customerName;

    /**
     * Sales order ID (denormalized for queries)
     */
    @Column(name = "sales_order_id", insertable = false, updatable = false)
    private Long salesOrderId;

    /**
     * Sales order number (denormalized for display)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", foreignKey = @ForeignKey(name = "fk_sales_return_sales_order"))
    private SalesOrder salesOrder;

    /**
     * Original invoice reference (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", foreignKey = @ForeignKey(name = "fk_sales_return_invoice"))
    private Invoice invoice;

    /**
     * Warehouse (where goods are returned to)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_return_warehouse"))
    private Warehouse warehouse;

    /**
     * Return type (FULL, PARTIAL)
     */
    @Column(name = "return_type", length = 20)
    private String returnType;

    /**
     * Return reason (DAMAGED, DEFECTIVE, WRONG_ITEM, EXPIRED, CUSTOMER_REQUEST, QUALITY_ISSUE, OTHER)
     */
    @Column(name = "return_reason", length = 50)
    private String returnReason;

    /**
     * Return reason description
     */
    @Column(name = "return_reason_description", columnDefinition = "TEXT")
    private String returnReasonDescription;

    /**
     * Customer reference number
     */
    @Column(name = "customer_reference", length = 50)
    private String customerReference;

    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;

    /**
     * Exchange rate (for foreign currency)
     */
    @Column(name = "exchange_rate", precision = 15, scale = 6)
    private BigDecimal exchangeRate;

    /**
     * Subtotal (sum of line totals before discount and tax)
     */
    @Column(name = "subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal;

    /**
     * Discount amount
     */
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    /**
     * Discount percentage
     */
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    /**
     * Tax amount
     */
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;

    /**
     * Tax percentage
     */
    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    /**
     * Total amount
     */
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    /**
     * Status (DRAFT, PENDING_APPROVAL, APPROVED, RECEIVED, INSPECTED, COMPLETED, REJECTED, CANCELLED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /**
     * Is approved flag
     */
    @Column(name = "is_approved")
    private Boolean isApproved;

    /**
     * Is inspected flag
     */
    @Column(name = "is_inspected")
    private Boolean isInspected;

    /**
     * Quality inspection required
     */
    @Column(name = "quality_inspection_required")
    private Boolean qualityInspectionRequired;

    /**
     * Quality inspection status (PENDING, PASSED, FAILED, PARTIAL)
     */
    @Column(name = "quality_inspection_status", length = 20)
    private String qualityInspectionStatus;

    /**
     * Quality inspector
     */
    @Column(name = "quality_inspector", length = 50)
    private String qualityInspector;

    /**
     * Quality inspection date
     */
    @Column(name = "quality_inspection_date")
    private LocalDate qualityInspectionDate;

    /**
     * Quality remarks
     */
    @Column(name = "quality_remarks", columnDefinition = "TEXT")
    private String qualityRemarks;

    /**
     * Received by
     */
    @Column(name = "received_by", length = 50)
    private String receivedBy;

    /**
     * Received date
     */
    @Column(name = "received_date")
    private LocalDate receivedDate;

    /**
     * Approved by
     */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;

    /**
     * Approval date
     */
    @Column(name = "approval_date")
    private LocalDate approvalDate;

    /**
     * Is posted to inventory (goods returned to stock)
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
     * Credit note generated flag
     */
    @Column(name = "credit_note_generated")
    private Boolean creditNoteGenerated;

    /**
     * Credit note ID reference
     */
    @Column(name = "credit_note_id")
    private Long creditNoteId;

    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Internal notes
     */
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    /**
     * Total items count
     */
    @Column(name = "total_items")
    private Integer totalItems;

    /**
     * Sales return items
     */
    @OneToMany(mappedBy = "salesReturn", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private Set<SalesReturnItem> items = new HashSet<>();

    /**
     * Credit notes
     */
    @OneToMany(mappedBy = "salesReturn", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<CreditNote> creditNotes = new HashSet<>();

    private String salesOrderNumber;
    private String invoiceNumber;
    private String warehouseName;

    /**
     * Adds a sales return item
     */
    public void addItem(SalesReturnItem item) {
        item.setSalesReturn(this);
        items.add(item);
    }

    /**
     * Removes a sales return item
     */
    public void removeItem(SalesReturnItem item) {
        items.remove(item);
        item.setSalesReturn(null);
    }

    /**
     * Calculates totals from items
     */
    @Transient
    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            subtotal = BigDecimal.ZERO;
            totalAmount = BigDecimal.ZERO;
            return;
        }

        // Calculate subtotal
        subtotal = items.stream()
                .map(SalesReturnItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate discount
        BigDecimal discountAmt = BigDecimal.ZERO;
        if (discountAmount != null) {
            discountAmt = discountAmount;
        } else if (discountPercentage != null) {
            discountAmt = subtotal.multiply(discountPercentage)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }

        BigDecimal amountAfterDiscount = subtotal.subtract(discountAmt);

        // Calculate tax
        BigDecimal taxAmt = BigDecimal.ZERO;
        if (taxAmount != null) {
            taxAmt = taxAmount;
        } else if (taxPercentage != null) {
            taxAmt = amountAfterDiscount.multiply(taxPercentage)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }

        // Calculate total
        totalAmount = amountAfterDiscount.add(taxAmt);

        this.discountAmount = discountAmt;
        this.taxAmount = taxAmt;
    }

    /**
     * Gets total accepted quantity (passed quality inspection)
     */
    @Transient
    public BigDecimal getTotalAcceptedQuantity() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(SalesReturnItem::getAcceptedQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets total rejected quantity (failed quality inspection)
     */
    @Transient
    public BigDecimal getTotalRejectedQuantity() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(SalesReturnItem::getRejectedQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Checks if quality inspection is complete
     */
    @Transient
    public boolean isQualityInspectionComplete() {
        if (!qualityInspectionRequired) {
            return true;
        }
        return "PASSED".equals(qualityInspectionStatus) ||
                "FAILED".equals(qualityInspectionStatus) ||
                "PARTIAL".equals(qualityInspectionStatus);
    }

    /**
     * Checks if can be posted to inventory
     */
    @Transient
    public boolean canPost() {
        return "INSPECTED".equals(status) && !isPosted && isQualityInspectionComplete();
    }

    /**
     * Checks if can be edited
     */
    @Transient
    public boolean canEdit() {
        return "DRAFT".equals(status);
    }

    /**
     * Checks if requires approval
     */
    @Transient
    public boolean requiresApproval() {
        return totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "DRAFT";
        }
        if (returnType == null) {
            returnType = "PARTIAL";
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (exchangeRate == null) {
            exchangeRate = BigDecimal.ONE;
        }
        if (qualityInspectionRequired == null) {
            qualityInspectionRequired = true;
        }
        if (isPosted == null) {
            isPosted = false;
        }
        if (isApproved == null) {
            isApproved = false;
        }
        if (isInspected == null) {
            isInspected = false;
        }
        if (creditNoteGenerated == null) {
            creditNoteGenerated = false;
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
        if (!(o instanceof SalesReturn)) return false;
        SalesReturn that = (SalesReturn) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        this.salesOrderNumber = salesOrderNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}