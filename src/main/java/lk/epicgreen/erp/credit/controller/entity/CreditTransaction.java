package lk.epicgreen.erp.credit.controller.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_limit_id", nullable = false)
    private CreditLimit creditLimit;

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "balance_before", precision = 15, scale = 2)
    private BigDecimal balanceBefore;

    @Column(name = "balance_after", precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
