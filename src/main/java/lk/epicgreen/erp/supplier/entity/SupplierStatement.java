package lk.epicgreen.erp.supplier.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_statements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    
    @Column(name = "statement_number", unique = true, length = 50)
    private String statementNumber;
    
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;
    
    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;
    
    @Column(name = "opening_balance", precision = 15, scale = 2)
    private BigDecimal openingBalance = BigDecimal.ZERO;
    
    @Column(name = "total_debits", precision = 15, scale = 2)
    private BigDecimal totalDebits = BigDecimal.ZERO;
    
    @Column(name = "total_credits", precision = 15, scale = 2)
    private BigDecimal totalCredits = BigDecimal.ZERO;
    
    @Column(name = "closing_balance", precision = 15, scale = 2)
    private BigDecimal closingBalance = BigDecimal.ZERO;
    
    @Column(name = "generated_by", length = 100)
    private String generatedBy;
    
    @CreationTimestamp
    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;
}
