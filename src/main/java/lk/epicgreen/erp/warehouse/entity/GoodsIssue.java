package lk.epicgreen.erp.warehouse.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "goods_issues", indexes = {
    @Index(name = "idx_issue_number", columnList = "issue_number"),
    @Index(name = "idx_warehouse_id", columnList = "warehouse_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsIssue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "issue_number", unique = true, nullable = false, length = 50)
    private String issueNumber;
    
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;
    
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;
    
    @Column(name = "issue_type", length = 20, nullable = false)
    private String issueType; // SALES, PRODUCTION, TRANSFER
    
    @Column(name = "destination_type", length = 30)
    private String destinationType;
    
    @Column(name = "destination_id")
    private Long destinationId;
    
    @Column(name = "issue_status", length = 20, nullable = false)
    private String issueStatus; // DRAFT, POSTED
    
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue = BigDecimal.ZERO;
    
    @Column(name = "issued_by", length = 100)
    private String issuedBy;
    
    @Column(name = "issued_at")
    private LocalDateTime issuedAt;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (issueStatus == null) {
            issueStatus = "DRAFT";
        }
    }
    
    public void post() {
        this.issueStatus = "POSTED";
        this.issuedAt = LocalDateTime.now();
    }
}
