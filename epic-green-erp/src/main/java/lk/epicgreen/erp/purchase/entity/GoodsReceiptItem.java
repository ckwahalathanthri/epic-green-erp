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

import java.time.LocalDateTime;

/**
 * GoodsReceiptItem Entity
 * Entity for goods receipt item
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "goods_receipt_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GoodsReceiptItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_id", nullable = false)
    private GoodsReceipt goodsReceipt;
    
    @Column(name = "purchase_order_item_id")
    private Long purchaseOrderItemId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "ordered_quantity", nullable = false)
    private Double orderedQuantity;
    
    @Column(name = "received_quantity", nullable = false)
    private Double receivedQuantity;
    
    @Column(name = "accepted_quantity")
    private Double acceptedQuantity;
    
    @Column(name = "rejected_quantity")
    private Double rejectedQuantity;
    
    @Column(name = "unit", length = 20)
    private String unit;
    
    @Column(name = "unit_price")
    private Double unitPrice;
    
    @Column(name = "total_price")
    private Double totalPrice;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "serial_number", length = 50)
    private String serialNumber;
    
    @Column(name = "expiry_date", length = 20)
    private String expiryDate;
    
    @Column(name = "inspection_status", length = 20)
    private String inspectionStatus; // PASSED, FAILED, PENDING
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "line_number")
    private Integer lineNumber;
    
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
