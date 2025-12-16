package lk.epicgreen.erp.returns.entity;

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
 * SalesReturnLine Entity
 * Entity for sales return line item
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "sales_return_line")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SalesReturnLine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_return_id", nullable = false)
    private SalesReturn salesReturn;
    
    @Column(name = "invoice_item_id")
    private Long invoiceItemId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "returned_quantity", nullable = false)
    private Double returnedQuantity;
    
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
    
    @Column(name = "refund_amount")
    private Double refundAmount;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "serial_number", length = 50)
    private String serialNumber;
    
    @Column(name = "return_reason", length = 100)
    private String returnReason;
    
    @Column(name = "inspection_status", length = 20)
    private String inspectionStatus; // APPROVED, REJECTED, PARTIAL
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
    
    @Column(name = "restocking_status", length = 30)
    private String restockingStatus; // RESTOCKED, DISPOSED, RETURNED_TO_SUPPLIER
    
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
