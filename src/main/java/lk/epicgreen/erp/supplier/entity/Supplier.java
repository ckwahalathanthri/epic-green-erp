package lk.epicgreen.erp.supplier.entity;


import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Supplier entity
 * Represents suppliers (vendors) providing raw materials, packaging, and services
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "suppliers", indexes = {
    @Index(name = "idx_supplier_code", columnList = "supplier_code"),
    @Index(name = "idx_supplier_name", columnList = "supplier_name"),
    @Index(name = "idx_supplier_type", columnList = "supplier_type"),
    @Index(name = "idx_is_active", columnList = "is_active"),
    @Index(name = "idx_deleted_at", columnList = "deleted_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Supplier code (unique identifier)
     */
    @NotBlank(message = "Supplier code is required")
    @Size(max = 20)
    @Column(name = "supplier_code", nullable = false, unique = true, length = 20)
    private String supplierCode;
    
    /**
     * Supplier name
     */
    @NotBlank(message = "Supplier name is required")
    @Size(max = 200)
    @Column(name = "supplier_name", nullable = false, length = 200)
    private String supplierName;
    
    /**
     * Supplier type (RAW_MATERIAL, PACKAGING, SERVICES, OTHER)
     */
    @NotBlank(message = "Supplier type is required")
    @Column(name = "supplier_type", nullable = false, length = 20)
    private String supplierType;

    @Column
    private String Status;
    
    /**
     * Primary contact person name
     */
    @Size(max = 100)
    @Column(name = "contact_person", length = 100)
    private String contactPerson;
    
    /**
     * Email address
     */
    @Email(message = "Email must be valid")
    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;
    
    /**
     * Phone number
     */
    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Column
    private String rejectionReason;

    @Column
    private String region;

    @Column
    private double balance;

    @Column
    private String taxNumber;

    @Column
    private boolean isApproved;

    @Column
    private boolean isBlocked;

    @Column
    private BigDecimal currentBalance;

    @Column
    private String status;
    
    /**
     * Mobile number
     */
    @Size(max = 20)
    @Column(name = "mobile", length = 20)
    private String mobile;
    
    /**
     * Tax ID / VAT number
     */
    @Size(max = 50)
    @Column(name = "tax_id", length = 50)
    private String taxId;
    
    /**
     * Payment terms (e.g., "Net 30 days", "COD")
     */
    @Size(max = 50)
    @Column(name = "payment_terms", length = 50)
    private String paymentTerms;
    
    /**
     * Credit limit
     */
    @PositiveOrZero(message = "Credit limit must be positive or zero")
    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;
    
    /**
     * Credit days (payment terms in days)
     */
    @PositiveOrZero(message = "Credit days must be positive or zero")
    @Column(name = "credit_days")
    private Integer creditDays;
    
    /**
     * Address line 1
     */
    @Size(max = 200)
    @Column(name = "address_line1", length = 200)
    private String addressLine1;
    
    /**
     * Address line 2
     */
    @Size(max = 200)
    @Column(name = "address_line2", length = 200)
    private String addressLine2;


    
    /**
     * City
     */
    @Size(max = 100)
    @Column(name = "city", length = 100)
    private String city;



    @Column
    private String ReviewComment;


    
    /**
     * State / Province
     */
    @Size(max = 100)
    @Column(name = "state", length = 100)
    private String state;
    
    /**
     * Country
     */
    @Size(max = 100)
    @Column(name = "country", length = 100)
    private String country;
    
    /**
     * Postal code
     */
    @Size(max = 20)
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    /**
     * Bank name
     */
    @Size(max = 100)
    @Column(name = "bank_name", length = 100)
    private String bankName;


    @Column
    private Long approvedBy;

    @Column
    private String ApprovalNotes;

    @Column
    private LocalDateTime approvedAt;



    @Column
    private boolean IsBlocked;

    @Column
    private String BlockReason;
    
    /**
     * Bank account number
     */
    @Size(max = 50)
    @Column(name = "bank_account_number", length = 50)
    private String bankAccountNumber;
    
    /**
     * Bank branch
     */
    @Size(max = 100)
    @Column(name = "bank_branch", length = 100)
    private String bankBranch;
    
    /**
     * Supplier rating (0.0 to 5.0)
     */
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Soft delete timestamp
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column
    private Integer totalOrders;
    
    /**
     * Additional contacts
     */
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SupplierContact> contacts = new ArrayList<>();
    
    /**
     * Ledger entries
     */
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SupplierLedger> ledgerEntries = new ArrayList<>();
    
    /**
     * Add contact
     */
    public void addContact(SupplierContact contact) {
        contact.setSupplier(this);
        contacts.add(contact);
    }
    
    /**
     * Remove contact
     */
    public void removeContact(SupplierContact contact) {
        contacts.remove(contact);
        contact.setSupplier(null);
    }
    
    /**
     * Check if active
     */
    @Transient
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive) && deletedAt == null;
    }
    
    /**
     * Check if deleted
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
    
    /**
     * Check if raw material supplier
     */
    @Transient
    public boolean isRawMaterialSupplier() {
        return "RAW_MATERIAL".equals(supplierType);
    }
    
    /**
     * Check if packaging supplier
     */
    @Transient
    public boolean isPackagingSupplier() {
        return "PACKAGING".equals(supplierType);
    }
    
    /**
     * Check if services supplier
     */
    @Transient
    public boolean isServicesSupplier() {
        return "SERVICES".equals(supplierType);
    }
    
    /**
     * Check if has credit limit
     */
    @Transient
    public boolean hasCreditLimit() {
        return creditLimit != null && creditLimit.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Check if has credit terms
     */
    @Transient
    public boolean hasCreditTerms() {
        return creditDays != null && creditDays > 0;
    }
    
    /**
     * Get full address
     */
    @Transient
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (addressLine1 != null) address.append(addressLine1);
        if (addressLine2 != null) {
            if (address.length() > 0) address.append(", ");
            address.append(addressLine2);
        }
        if (city != null) {
            if (address.length() > 0) address.append(", ");
            address.append(city);
        }
        if (state != null) {
            if (address.length() > 0) address.append(", ");
            address.append(state);
        }
        if (country != null) {
            if (address.length() > 0) address.append(", ");
            address.append(country);
        }
        if (postalCode != null) {
            if (address.length() > 0) address.append(" ");
            address.append(postalCode);
        }
        return address.toString();
    }
    
    /**
     * Get bank details
     */
    @Transient
    public String getBankDetails() {
        StringBuilder details = new StringBuilder();
        if (bankName != null) details.append(bankName);
        if (bankBranch != null) {
            if (details.length() > 0) details.append(" - ");
            details.append(bankBranch);
        }
        if (bankAccountNumber != null) {
            if (details.length() > 0) details.append(" - ");
            details.append("A/C: ").append(bankAccountNumber);
        }
        return details.toString();
    }
    
    /**
     * Check if supplier has good rating (>= 4.0)
     */
//    @Transient
//    public boolean hasGoodRating() {
//        return rating != null && rating.compareTo(new BigDecimal("4.0")) >= 0;
//    }
//
    /**
     * Soft delete supplier
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.isActive = false;
    }
    
    /**
     * Restore soft deleted supplier
     */
    public void restore() {
        this.deletedAt = null;
        this.isActive = true;
    }
    

    protected void onCreate() {
        super.onCreate();
        if (isActive == null) {
            isActive = true;
        }
        if (creditLimit == null) {
            creditLimit = BigDecimal.ZERO;
        }
        if (creditDays == null) {
            creditDays = 0;
        }
        if (rating == null) {
            rating = BigDecimal.ZERO;
        }
        if (country == null) {
            country = "Sri Lanka";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supplier)) return false;
        Supplier supplier = (Supplier) o;
        return id != null && id.equals(supplier.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Integer getTotalOrders() {

        return totalOrders;
    }

    public boolean getIsBlocked() {
        return this.IsBlocked;
    }

    public boolean getIsApproved() {
        return this.isApproved;
    }

    public void setIsApproved(boolean b) {
        this.isApproved = b;
    }


//    public boolean getIsBlocked() {
//        return IsBlocked;
//    }
//
//    public boolean getIsApproved() {
//        return IsApproved;
//    }
}
