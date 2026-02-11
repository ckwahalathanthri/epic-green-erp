package lk.epicgreen.erp.customer.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lk.epicgreen.erp.credit.controller.entity.CreditLimit;
import lk.epicgreen.erp.credit.controller.entity.CustomerGroupMember;
import lk.epicgreen.erp.product.entity.ProductDocument;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer entity
 * Represents customers (wholesale, retail, distributors, direct customers)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_customer_code", columnList = "customer_code"),
    @Index(name = "idx_customer_name", columnList = "customer_name"),
    @Index(name = "idx_customer_type", columnList = "customer_type"),
    @Index(name = "idx_sales_rep_id", columnList = "assigned_sales_rep_id"),
    @Index(name = "idx_is_active", columnList = "is_active"),
    @Index(name = "idx_deleted_at", columnList = "deleted_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer code (unique identifier)
     */
    @NotBlank(message = "Customer code is required")
    @Size(max = 20)
    @Column(name = "customer_code", nullable = false, unique = true, length = 20)
    private String customerCode;
    
    /**
     * Customer name
     */
    @NotBlank(message = "Customer name is required")
    @Size(max = 200)
    @Column(name = "customer_name", nullable = false, length = 200)
    private String customerName;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CreditLimit> creditLimits=new ArrayList<>();



    @Column
    private String Type;

    @Column
    private String status;

    @Column
    private String creditStatus;

    @Column
    private Long assignedSalesRepId;
    
    /**
     * Customer type (WHOLESALE, RETAIL, DISTRIBUTOR, DIRECT)
     */
    @NotBlank(message = "Customer type is required")
    @Column(name = "customer_type", nullable = false, length = 20)
    private String customerType;
    
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
     * Has credit limit currency
     */
    @Column(name = "has_credit_limit_currency")
    private Boolean hasCreditLimitCurrency;

    /**
     * Total sales amount
     */
    @Column(name = "total_sales_amount")
    private BigDecimal totalSalesAmount;

    /**
     * Has credit facility
     */
    @Column(name = "has_credit_facility")
    private Boolean hasCreditFacility;
    
    /**
     * Credit days (payment terms in days)
     */
    @PositiveOrZero(message = "Credit days must be positive or zero")
    @Column(name = "credit_days")
    private Integer creditDays;
    
    /**
     * Current balance (accounts receivable)
     */
    @Column(name = "current_balance", precision = 15, scale = 2)
    private BigDecimal currentBalance;

    /**
     * Has outstanding balance
     */
    @Column(name = "has_outstanding_balance")
    private Boolean hasOutstandingBalance;
    
    /**
     * Billing address line 1
     */
    @Size(max = 200)
    @Column(name = "billing_address_line1", length = 200)
    private String billingAddressLine1;
    
    /**
     * Billing address line 2
     */
    @Size(max = 200)
    @Column(name = "billing_address_line2", length = 200)
    private String billingAddressLine2;
    
    /**
     * Billing city
     */
    @Size(max = 100)
    @Column(name = "billing_city", length = 100)
    private String billingCity;
    
    /**
     * Billing state
     */
    @Size(max = 100)
    @Column(name = "billing_state", length = 100)
    private String billingState;
    
    /**
     * Billing country
     */
    @Size(max = 100)
    @Column(name = "billing_country", length = 100)
    private String billingCountry;
    
    /**
     * Billing postal code
     */
    @Size(max = 20)
    @Column(name = "billing_postal_code", length = 20)
    private String billingPostalCode;
    
    /**
     * Shipping address line 1
     */
    @Size(max = 200)
    @Column(name = "shipping_address_line1", length = 200)
    private String shippingAddressLine1;
    
    /**
     * Shipping address line 2
     */
    @Size(max = 200)
    @Column(name = "shipping_address_line2", length = 200)
    private String shippingAddressLine2;
    
    /**
     * Shipping city
     */
    @Size(max = 100)
    @Column(name = "shipping_city", length = 100)
    private String shippingCity;
    
    /**
     * Shipping state
     */
    @Size(max = 100)
    @Column(name = "shipping_state", length = 100)
    private String shippingState;
    
    /**
     * Shipping country
     */
    @Size(max = 100)
    @Column(name = "shipping_country", length = 100)
    private String shippingCountry;
    
    /**
     * Shipping postal code
     */
    @Size(max = 20)
    @Column(name = "shipping_postal_code", length = 20)
    private String shippingPostalCode;
    
    /**
     * Assigned sales representative
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_sales_rep_id", foreignKey = @ForeignKey(name = "fk_customer_sales_rep"))
    private User assignedSalesRep;
    
    /**
     * Region
     */
    @Size(max = 100)
    @Column(name = "region", length = 100)
    private String region;
    
    /**
     * Route code (for delivery routes)
     */
    @Size(max = 20)
    @Column(name = "route_code", length = 20)
    private String routeCode;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * Verified by user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_id")
    private User verifiedBy;


    /**
     * Is blacklisted
     */
    @Column(name = "is_blacklisted")
    private Boolean isBlacklisted;

    /**
     * Blacklist reason
     */
    @Column(name = "blacklist_reason", length = 255)
    private String blacklistReason;

    /**
     * Is verified
     */
    @Column(name = "is_verified")
    private Boolean isVerified;
    
    /**
     * Soft delete timestamp
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "suspension_reason", length = 255)
    private String suspensionReason;

    @Column(name = "last_order_date")
    private LocalDate lastOrderDate;
    
    /**
     * Additional contacts
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CustomerContact> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CustomerGroupMember> groupMembers = new ArrayList<>();
    
    /**
     * Customer addresses
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CustomerAddress> addresses = new ArrayList<>();
    
    /**
     * Ledger entries
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CustomerLedger> ledgerEntries = new ArrayList<>();
    
    /**
     * Price lists
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CustomerPriceList> priceLists = new ArrayList<>();
    
    /**
     * Add contact
     */
    public void addContact(CustomerContact contact) {
        contact.setCustomer(this);
        contacts.add(contact);
    }
    
    /**
     * Remove contact
     */
    public void removeContact(CustomerContact contact) {
        contacts.remove(contact);
        contact.setCustomer(null);
    }
    
    /**
     * Add address
     */
    public void addAddress(CustomerAddress address) {
        address.setCustomer(this);
        addresses.add(address);
    }
    
    /**
     * Remove address
     */
    public void removeAddress(CustomerAddress address) {
        addresses.remove(address);
        address.setCustomer(null);
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
     * Check if wholesale customer
     */
    @Transient
    public boolean isWholesale() {
        return "WHOLESALE".equals(customerType);
    }
    
    /**
     * Check if retail customer
     */
    @Transient
    public boolean isRetail() {
        return "RETAIL".equals(customerType);
    }
    
    /**
     * Check if distributor
     */
    @Transient
    public boolean isDistributor() {
        return "DISTRIBUTOR".equals(customerType);
    }
    
    /**
     * Check if direct customer
     */
    @Transient
    public boolean isDirect() {
        return "DIRECT".equals(customerType);
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
     * Get available credit
     */
    @Transient
    public BigDecimal getAvailableCredit() {
        if (!hasCreditLimit()) {
            return BigDecimal.ZERO;
        }
        BigDecimal limit = creditLimit != null ? creditLimit : BigDecimal.ZERO;
        BigDecimal balance = currentBalance != null ? currentBalance : BigDecimal.ZERO;
        return limit.subtract(balance);
    }
    
    /**
     * Check if credit limit exceeded
     */
    @Transient
    public boolean isCreditLimitExceeded() {
        return getAvailableCredit().compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Get full billing address
     */
    @Transient
    public String getFullBillingAddress() {
        return buildAddress(billingAddressLine1, billingAddressLine2, 
                           billingCity, billingState, billingCountry, billingPostalCode);
    }
    
    /**
     * Get full shipping address
     */
    @Transient
    public String getFullShippingAddress() {
        return buildAddress(shippingAddressLine1, shippingAddressLine2, 
                           shippingCity, shippingState, shippingCountry, shippingPostalCode);
    }
    
    /**
     * Helper method to build address string
     */
    private String buildAddress(String line1, String line2, String city, 
                               String state, String country, String postal) {
        StringBuilder address = new StringBuilder();
        if (line1 != null) address.append(line1);
        if (line2 != null) {
            if (address.length() > 0) address.append(", ");
            address.append(line2);
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
        if (postal != null) {
            if (address.length() > 0) address.append(" ");
            address.append(postal);
        }
        return address.toString();
    }
    
    /**
     * Soft delete customer
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.isActive = false;
    }
    
    /**
     * Restore soft deleted customer
     */
    public void restore() {
        this.deletedAt = null;
        this.isActive = true;
    }
    
//    @PrePersist
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
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        if (billingCountry == null) {
            billingCountry = "Sri Lanka";
        }
        if (shippingCountry == null) {
            shippingCountry = "Sri Lanka";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return id != null && id.equals(customer.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
