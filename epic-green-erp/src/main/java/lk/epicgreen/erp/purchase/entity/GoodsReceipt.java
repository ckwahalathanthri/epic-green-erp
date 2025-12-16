package lk.epicgreen.erp.purchase.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * GoodsReceipt Entity
 * Entity for goods receipt (GRN - Goods Receipt Note)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "goods_receipt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GoodsReceipt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "grn_number", unique = true, nullable = false, length = 50)
    private String grnNumber;
    
    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;
    
    @Column(name = "purchase_order_id", nullable = false)
    private Long purchaseOrderId;
    
    @Column(name = "purchase_order_number", length = 50)
    private String purchaseOrderNumber;
    
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;
    
    @Column(name = "supplier_name", length = 200)
    private String supplierName;
    
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "warehouse_name", length = 200)
    private String warehouseName;
    
    @Column(name = "received_by", length = 100)
    private String receivedBy;
    
    @Column(name = "inspected_by", length = 100)
    private String inspectedBy;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;
    
    @Column(name = "driver_name", length = 100)
    private String driverName;
    
    @Column(name = "driver_contact", length = 20)
    private String driverContact;
    
    @Column(name = "invoice_number", length = 50)
    private String invoiceNumber;
    
    @Column(name = "invoice_date")
    private LocalDate invoiceDate;
    
    @Column(name = "challan_number", length = 50)
    private String challanNumber;
    
    @Column(name = "challan_date")
    private LocalDate challanDate;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "status", length = 20)
    private String status; // DRAFT, RECEIVED, INSPECTED, APPROVED, REJECTED
    
    @Column(name = "inspection_status", length = 20)
    private String inspectionStatus; // PENDING, PASSED, FAILED, PARTIAL
    
    @Column(name = "quality_status", length = 20)
    private String qualityStatus; // APPROVED, REJECTED, HOLD
    
    @Column(name = "total_items")
    private Integer totalItems;
    
    @Column(name = "total_quantity")
    private Double totalQuantity;
    
    @Column(name = "total_amount")
    private Double totalAmount;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "created_by_user_id")
    private Long createdByUserId;
    
    @Column(name = "approved_by_user_id")
    private Long approvedByUserId;
    
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    @Column(name = "approval_notes", columnDefinition = "TEXT")
    private String approvalNotes;
    
    @OneToMany(mappedBy = "goodsReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodsReceiptItem> items;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
