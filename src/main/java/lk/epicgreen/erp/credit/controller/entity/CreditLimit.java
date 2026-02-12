package lk.epicgreen.erp.credit.controller.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lk.epicgreen.erp.customer.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credit_limits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "creditLimit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CreditLimit> creditLimits=new ArrayList<>();


    @Column(name = "credit_limit", precision = 15, scale = 2, nullable = false)
    private BigDecimal creditLimit;

    @Column(name = "credit_used", precision = 15, scale = 2)
    private BigDecimal creditUsed = BigDecimal.ZERO;

    @Column(name = "credit_available", precision = 15, scale = 2)
    private BigDecimal creditAvailable;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "approval_status", length = 50)
    private String approvalStatus;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

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

    @PrePersist
    @PreUpdate
    public void calculateCreditAvailable() {
        if (creditLimit != null && creditUsed != null) {
            this.creditAvailable = creditLimit.subtract(creditUsed);
        }
    }
}
