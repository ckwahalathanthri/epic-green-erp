package lk.epicgreen.erp.warehouse.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "goods_receipt_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "grn_number", unique = true, nullable = false, length = 50)
    private String grnNumber;
    
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;
    
    @Column(name = "po_number", length = 50)
    private String poNumber;

    @OneToMany(mappedBy = "grn", orphanRemoval = true)
    @JsonManagedReference
    private List<GRNItem> grnItems=new ArrayList<>();
    
    @Column(name = "supplier_id")
    private Long supplierId;
    
    @Column(name = "supplier_name", length = 200)
    private String supplierName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    
    @Column(name = "received_date", nullable = false)
    private LocalDate receivedDate;
    
    @Column(name = "supplier_invoice_number", length = 100)
    private String supplierInvoiceNumber;
    
    @Column(name = "supplier_invoice_date")
    private LocalDate supplierInvoiceDate;
    
    @Column(name = "delivery_note_number", length = 100)
    private String deliveryNoteNumber;
    
    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;
    
    @Column(name = "driver_name", length = 200)
    private String driverName;
    
    @Column(name = "grn_status", length = 50)
    private String grnStatus; // DRAFT, RECEIVED, QUALITY_CHECK, APPROVED, REJECTED, CANCELLED
    
    @Column(name = "quality_status", length = 50)
    private String qualityStatus; // PASSED, FAILED, PARTIAL, PENDING
    
    @Column(name = "quality_checked_by", length = 100)
    private String qualityCheckedBy;
    
    @Column(name = "quality_checked_at")
    private LocalDateTime qualityCheckedAt;
    
    @Column(name = "quality_remarks", columnDefinition = "TEXT")
    private String qualityRemarks;
    
    @Column(name = "received_by", length = 100)
    private String receivedBy;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @OneToMany(mappedBy = "grn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GRNItem> items = new ArrayList<>();
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
