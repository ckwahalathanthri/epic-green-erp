package lk.epicgreen.erp.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "stock_issues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "issue_number", unique = true, nullable = false, length = 50)
    private String issueNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;
    
    @Column(name = "issue_type", length = 50, nullable = false)
    private String issueType; // SALES_ORDER, PRODUCTION, TRANSFER, ADJUSTMENT, SAMPLE
    
    @Column(name = "reference_number", length = 100)
    private String referenceNumber;
    
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    
    @Column(name = "reference_id")
    private Long referenceId;
    
    @Column(name = "department", length = 100)
    private String department;
    
    @Column(name = "cost_center", length = 100)
    private String costCenter;
    
    @Column(name = "issued_to", length = 200)
    private String issuedTo;
    
    @Column(name = "issue_status", length = 50)
    private String issueStatus; // DRAFT, ISSUED, PICKED, PACKED, DISPATCHED, CANCELLED
    
    @Column(name = "picking_method", length = 50)
    private String pickingMethod; // FIFO, FEFO, LIFO, MANUAL
    
    @Column(name = "priority", length = 20)
    private String priority;
    
    @Column(name = "issued_by", length = 100)
    private String issuedBy;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @OneToMany(mappedBy = "stockIssue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueItem> items = new ArrayList<>();
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
