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
 * Supplier Entity - COMPLETE VERSION with ALL required fields
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
    
    @Column(name = "supplier_name", nullable = false, length = 200)
    private String supplierName;

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
    
    @Column(name = "state_province", length = 100)
    private String stateProvince;
    
    @Column(name = "country", length = 100)
    private String country;
    
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    @Column(name = "tax_id", length = 50)
    private String taxId;
    
    @Column(name = "tax_number", length = 50)
    private String taxNumber;
    
    @Column(name = "business_registration_no", length = 50)
    private String businessRegistrationNo;
    
    @Column(name = "registration_number", length = 50)
    private String registrationNumber;
    
    @Column(name = "supplier_type", length = 50)
    private String supplierType;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "rating")
    private Integer rating;
    
    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "credit_days")
    private Integer creditDays;
    
    @Column(name = "outstanding_balance", precision = 15, scale = 2)
    private BigDecimal outstandingBalance;
    
    @Column(name = "current_balance", precision = 15, scale = 2)
    private BigDecimal currentBalance;
    
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
    
    // Boolean flags
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "is_blocked")
    private Boolean isBlocked;
    
    @Column(name = "is_approved")
    private Boolean isApproved;
    
    @Column(name = "is_credit_allowed")
    private Boolean isCreditAllowed;
    
    // Blocking information
    @Column(name = "blocked_reason", length = 500)
    private String blockedReason;
    
    @Column(name = "block_reason", length = 500)
    private String blockReason;
    
    @Column(name = "blocked_date")
    private LocalDate blockedDate;
    
    // Approval information
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    @Column(name = "approved_by_user_id")
    private Long approvedByUserId;
    
    @Column(name = "approval_notes", length = 500)
    private String approvalNotes;
    
    // Rejection information
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;
    
    @Column(name = "rejected_date")
    private LocalDateTime rejectedDate;
    
    // Transaction tracking
    @Column(name = "last_transaction_at")
    private LocalDateTime lastTransactionAt;
    
    @Column(name = "last_transaction_date")
    private LocalDateTime lastTransactionDate;
    
    @Column(name = "last_purchase_date")
    private LocalDate lastPurchaseDate;
    
    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;
    
    // Statistics
    @Column(name = "total_orders")
    private Integer totalOrders;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    
    @Column(name = "review_comments", length = 1000)
    private String reviewComments;
    
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
        if (isApproved == null) {
            isApproved = false;
        }
        if (isCreditAllowed == null) {
            isCreditAllowed = false;
        }
        if (outstandingBalance == null) {
            outstandingBalance = BigDecimal.ZERO;
        }
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        if (totalOrders == null) {
            totalOrders = 0;
        }
        if (registrationDate == null) {
            registrationDate = LocalDateTime.now();
        }
        // Sync fields
        if (supplierName == null && name != null) {
            supplierName = name;
        }
        if (name == null && supplierName != null) {
            name = supplierName;
        }
        if (stateProvince == null && state != null) {
            stateProvince = state;
        }
        if (taxNumber == null && taxId != null) {
            taxNumber = taxId;
        }
        if (registrationNumber == null && businessRegistrationNo != null) {
            registrationNumber = businessRegistrationNo;
        }
        if (currentBalance == null && outstandingBalance != null) {
            currentBalance = outstandingBalance;
        }
        if (blockReason == null && blockedReason != null) {
            blockReason = blockedReason;
        }
        if (lastTransactionDate == null && lastTransactionAt != null) {
            lastTransactionDate = lastTransactionAt;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Sync fields on update
        if (supplierName != null) {
            name = supplierName;
        }
        if (stateProvince != null) {
            state = stateProvince;
        }
        if (taxNumber != null) {
            taxId = taxNumber;
        }
        if (registrationNumber != null) {
            businessRegistrationNo = registrationNumber;
        }
        if (currentBalance != null) {
            outstandingBalance = currentBalance;
        }
        if (blockReason != null) {
            blockedReason = blockReason;
        }
        if (lastTransactionDate != null) {
            lastTransactionAt = lastTransactionDate;
        }
    }
}
