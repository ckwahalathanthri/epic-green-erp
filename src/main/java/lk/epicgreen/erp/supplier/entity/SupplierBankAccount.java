package lk.epicgreen.erp.supplier.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_bank_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierBankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;


    @Column(name = "bank_name", nullable = false, length = 200)
    private String bankName;
    @Column(name = "branch_name", length = 200)
    private String branchName;
    @Column(name = "account_holder_name", nullable = false, length = 200)
    private String accountHolderName;
    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;
    @Column(name = "account_type", length = 50)
    private String accountType;
    @Column(name = "swift_code", length = 20)
    private String swiftCode;
    @Column(name = "iban", length = 50)
    private String iban;
    @Column(name = "currency", length = 10)
    private String currency = "LKR";
    @Column(name = "is_primary")
    private Boolean isPrimary = false;
    @Column(name = "is_active")
    private Boolean isActive = true;
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
}
