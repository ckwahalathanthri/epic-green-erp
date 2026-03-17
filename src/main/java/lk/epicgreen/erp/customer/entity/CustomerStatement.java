package lk.epicgreen.erp.customer.entity;

import lk.epicgreen.erp.customer.entity.Customer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_statements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(name = "statement_number", unique = true, length = 50)
    private String statementNumber;
    
    @Column(name = "statement_date", nullable = false)
    private LocalDate statementDate;
    
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;
    
    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;
    
    @Column(name = "opening_balance", precision = 15, scale = 2)
    private BigDecimal openingBalance;
    
    @Column(name = "total_debit", precision = 15, scale = 2)
    private BigDecimal totalDebit;
    
    @Column(name = "total_credit", precision = 15, scale = 2)
    private BigDecimal totalCredit;
    
    @Column(name = "closing_balance", precision = 15, scale = 2)
    private BigDecimal closingBalance;
    
    @Column(name = "is_sent")
    private Boolean isSent = false;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
