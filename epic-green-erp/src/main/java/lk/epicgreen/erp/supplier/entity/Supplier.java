package lk.epicgreen.erp.supplier.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Supplier Entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "suppliers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Supplier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "supplier_code", unique = true, length = 50)
    private String supplierCode;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "company_name", length = 200)
    private String companyName;
    
    @Column(name = "contact_person", length = 200)
    private String contactPerson;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "mobile", length = 20)
    private String mobile;
    
    @Column(name = "fax", length = 20)
    private String fax;
    
    @Column(name = "website", length = 200)
    private String website;
    
    @Column(name = "address", length = 500)
    private String address;
    
    @Column(name = "city", length = 100)
    private String city;
    
    @Column(name = "state", length = 100)
    private String state;
    
    @Column(name = "country", length = 100)
    private String country;
    
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    @Column(name = "tax_id", length = 50)
    private String taxId;
    
    @Column(name = "business_registration_no", length = 50)
    private String businessRegistrationNo;
    
    @Column(name = "supplier_type", length = 50)
    private String supplierType; // RAW_MATERIAL, PACKAGING, SERVICE, etc.
    
    @Column(name = "status", length = 20)
    private String status; // ACTIVE, INACTIVE, BLOCKED
    
    @Column(name = "rating")
    private Integer rating; // 1-5 stars
    
    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "credit_days")
    private Integer creditDays;
    
    @Column(name = "outstanding_balance", precision = 15, scale = 2)
    private BigDecimal outstandingBalance;
    
    @Column(name = "payment_terms", length = 500)
    private String paymentTerms;
    
    @Column(name = "delivery_terms", length = 500)
    private String deliveryTerms;
    
    @Column(name = "bank_name", length = 200)
    private String bankName;
    
    @Column(name = "bank_account_no", length = 50)
    private String bankAccountNo;
    
    @Column(name = "bank_branch", length = 200)
    private String bankBranch;
    
    @Column(name = "bank_swift_code", length = 50)
    private String bankSwiftCode;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "is_blocked")
    private Boolean isBlocked;
    
    @Column(name = "blocked_reason", length = 500)
    private String blockedReason;
    
    @Column(name = "blocked_date")
    private LocalDate blockedDate;
    
    @Column(name = "last_transaction_at")
    private LocalDateTime lastTransactionAt;
    
    @Column(name = "last_purchase_date")
    private LocalDate lastPurchaseDate;
    
    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;
    
    // Audit fields
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
        if (isBlocked == null) {
            isBlocked = false;
        }
        if (outstandingBalance == null) {
            outstandingBalance = BigDecimal.ZERO;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
