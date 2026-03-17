package lk.epicgreen.erp.warehouse.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "goods_receipts", indexes = {
    @Index(name = "idx_receipt_number", columnList = "receipt_number"),
    @Index(name = "idx_warehouse_id", columnList = "warehouse_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceipt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "receipt_number", unique = true, nullable = false, length = 50)
    private String receiptNumber;
    
    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;
    
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "receipt_type", length = 20, nullable = false)
    private String receiptType; // PRODUCTION, PURCHASE, RETURN, TRANSFER
    
    @Column(name = "source_type", length = 30)
    private String sourceType;
    
    @Column(name = "source_id")
    private Long sourceId;
    
    @Column(name = "supplier_id")
    private Long supplierId;
    
    @Column(name = "receipt_status", length = 20, nullable = false)
    private String receiptStatus; // DRAFT, POSTED
    
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue = BigDecimal.ZERO;
    
    @Column(name = "received_by", length = 100)
    private String receivedBy;
    
    @Column(name = "received_at")
    private LocalDateTime receivedAt;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (receiptStatus == null) {
            receiptStatus = "DRAFT";
        }
    }
    
    public void post() {
        this.receiptStatus = "POSTED";
        this.receivedAt = LocalDateTime.now();
    }
}
