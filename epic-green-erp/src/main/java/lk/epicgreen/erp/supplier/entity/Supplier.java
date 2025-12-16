package lk.epicgreen.erp.supplier.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Supplier entity
 * Represents suppliers who provide raw materials, packaging, and other goods
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "suppliers", indexes = {
    @Index(name = "idx_supplier_code", columnList = "supplier_code"),
    @Index(name = "idx_supplier_name", columnList = "supplier_name"),
    @Index(name = "idx_supplier_status", columnList = "status"),
    @Index(name = "idx_supplier_type", columnList = "supplier_type")
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
    @Column(name = "supplier_code", nullable = false, unique = true, length = 20)
    private String supplierCode;
    
    /**
     * Supplier name
     */
    @Column(name = "supplier_name", nullable = false, length = 200)
    private String supplierName;
    
    /**
     * Supplier type (RAW_MATERIAL, PACKAGING, SERVICES, etc.)
     */
    @Column(name = "supplier_type", nullable = false, length = 50)
    private String supplierType;
    
    /**
     * Business registration number
     */
    @Column(name = "registration_number", length = 50)
    private String registrationNumber;
    
    /**
     * VAT/Tax number
     */
    @Column(name = "vat_number", length = 50)
    private String vatNumber;
    
    /**
     * Email address
     */
    @Column(name = "email", length = 100)
    private String email;
    
    /**
     * Phone number
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    /**
     * Mobile number
     */
    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;
    
    /**
     * Fax number
     */
    @Column(name = "fax_number", length = 20)
    private String faxNumber;
    
    /**
     * Website URL
     */
    @Column(name = "website", length = 255)
    private String website;
    
    /**
     * Address line 1
     */
    @Column(name = "address_line1", length = 255)
    private String addressLine1;
    
    /**
     * Address line 2
     */
    @Column(name = "address_line2", length = 255)
    private String addressLine2;
    
    /**
     * City
     */
    @Column(name = "city", length = 100)
    private String city;
    
    /**
     * State/Province
     */
    @Column(name = "state", length = 100)
    private String state;
    
    /**
     * Postal code
     */
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    /**
     * Country
     */
    @Column(name = "country", length = 100)
    private String country;
    
    /**
     * Credit limit
     */
    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;
    
    /**
     * Current balance (outstanding amount)
     */
    @Column(name = "current_balance", precision = 15, scale = 2)
    private BigDecimal currentBalance;
    
    /**
     * Payment terms in days (e.g., 30, 60, 90)
     */
    @Column(name = "payment_terms_days")
    private Integer paymentTermsDays;
    
    /**
     * Payment terms description
     */
    @Column(name = "payment_terms", length = 255)
    private String paymentTerms;
    
    /**
     * Currency code (LKR, USD, etc.)
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Supplier rating (1-5 stars)
     */
    @Column(name = "rating")
    private Integer rating;
    
    /**
     * Bank name
     */
    @Column(name = "bank_name", length = 100)
    private String bankName;
    
    /**
     * Bank account number
     */
    @Column(name = "bank_account_number", length = 50)
    private String bankAccountNumber;
    
    /**
     * Bank account holder name
     */
    @Column(name = "bank_account_holder", length = 100)
    private String bankAccountHolder;
    
    /**
     * Bank branch
     */
    @Column(name = "bank_branch", length = 100)
    private String bankBranch;
    
    /**
     * Bank SWIFT/BIC code
     */
    @Column(name = "bank_swift_code", length = 20)
    private String bankSwiftCode;
    
    /**
     * Status (ACTIVE, INACTIVE, BLOCKED, PENDING)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Is approved flag
     */
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;
    
    /**
     * Approved by user
     */
    @Column(name = "approved_by", length = 50)
    private String approvedBy;
    
    /**
     * Is preferred supplier
     */
    @Column(name = "is_preferred")
    private Boolean isPreferred;
    
    /**
     * Lead time in days
     */
    @Column(name = "lead_time_days")
    private Integer leadTimeDays;
    
    /**
     * Minimum order value
     */
    @Column(name = "minimum_order_value", precision = 15, scale = 2)
    private BigDecimal minimumOrderValue;
    
    /**
     * Terms and conditions
     */
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Supplier contacts
     */
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<SupplierContact> contacts = new HashSet<>();
    
    /**
     * Supplier ledger entries
     */
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<SupplierLedger> ledgerEntries = new HashSet<>();
    
    /**
     * Gets full address
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
        if (postalCode != null) {
            if (address.length() > 0) address.append(" ");
            address.append(postalCode);
        }
        if (country != null) {
            if (address.length() > 0) address.append(", ");
            address.append(country);
        }
        return address.toString();
    }
    
    /**
     * Checks if supplier is active
     */
    @Transient
    public boolean isActive() {
        return "ACTIVE".equals(status) && isApproved != null && isApproved;
    }
    
    /**
     * Checks if supplier is blocked
     */
    @Transient
    public boolean isBlocked() {
        return "BLOCKED".equals(status);
    }
    
    /**
     * Checks if credit limit is exceeded
     */
    @Transient
    public boolean isCreditLimitExceeded() {
        if (creditLimit == null || currentBalance == null) {
            return false;
        }
        return currentBalance.compareTo(creditLimit) > 0;
    }
    
    /**
     * Gets available credit
     */
    @Transient
    public BigDecimal getAvailableCredit() {
        if (creditLimit == null) {
            return BigDecimal.ZERO;
        }
        if (currentBalance == null) {
            return creditLimit;
        }
        BigDecimal available = creditLimit.subtract(currentBalance);
        return available.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : available;
    }
    
    /**
     * Adds a contact to the supplier
     */
    public void addContact(SupplierContact contact) {
        contact.setSupplier(this);
        contacts.add(contact);
    }
    
    /**
     * Removes a contact from the supplier
     */
    public void removeContact(SupplierContact contact) {
        contacts.remove(contact);
        contact.setSupplier(null);
    }
    
    /**
     * Adds a ledger entry
     */
    public void addLedgerEntry(SupplierLedger entry) {
        entry.setSupplier(this);
        ledgerEntries.add(entry);
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "PENDING";
        }
        if (isApproved == null) {
            isApproved = false;
        }
        if (isPreferred == null) {
            isPreferred = false;
        }
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        if (currency == null) {
            currency = "LKR";
        }
    }
}
