package lk.epicgreen.erp.customer.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Customer entity
 * Represents business customers who purchase products
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_customer_code", columnList = "customer_code"),
    @Index(name = "idx_customer_name", columnList = "customer_name"),
    @Index(name = "idx_customer_type", columnList = "customer_type"),
    @Index(name = "idx_customer_email", columnList = "email"),
    @Index(name = "idx_customer_phone", columnList = "phone_number"),
    @Index(name = "idx_customer_status", columnList = "status")
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
    @Column(name = "customer_code", nullable = false, unique = true, length = 50)
    private String customerCode;
    
    /**
     * Customer name (business name)
     */
    @Column(name = "customer_name", nullable = false, length = 200)
    private String customerName;
    
    /**
     * Business name (alias for customerName)
     */
    @Column(name = "business_name", length = 200)
    private String businessName;
    
    /**
     * Customer type (RETAIL, WHOLESALE, DISTRIBUTOR, EXPORT)
     */
    @Column(name = "customer_type", nullable = false, length = 20)
    private String customerType;
    
    /**
     * Business registration number
     */
    @Column(name = "registration_number", length = 100)
    private String registrationNumber;
    
    /**
     * Tax identification number
     */
    @Column(name = "tax_number", length = 100)
    private String taxNumber;
    
    /**
     * Email
     */
    @Column(name = "email", length = 100)
    private String email;
    
    /**
     * Phone number
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    /**
     * Phone (alias for phoneNumber)
     */
    @Column(name = "phone", length = 20)
    private String phone;
    
    /**
     * Mobile number
     */
    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;
    
    /**
     * Mobile (alias for mobileNumber)
     */
    @Column(name = "mobile", length = 20)
    private String mobile;
    
    /**
     * Fax number
     */
    @Column(name = "fax_number", length = 20)
    private String faxNumber;
    
    /**
     * Website
     */
    @Column(name = "website", length = 200)
    private String website;
    
    /**
     * City
     */
    @Column(name = "city", length = 100)
    private String city;
    
    /**
     * Address line 1
     */
    @Column(name = "address_line1", length = 250)
    private String addressLine1;
    
    /**
     * Address line 2
     */
    @Column(name = "address_line2", length = 250)
    private String addressLine2;
    
    /**
     * Province/State
     */
    @Column(name = "province", length = 100)
    private String province;
    
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
     * Registration date
     */
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    /**
     * Credit limit
     */
    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;
    
    /**
     * Credit days (payment terms in days)
     */
    @Column(name = "credit_days")
    private Integer creditDays;
    
    /**
     * Payment terms description
     */
    @Column(name = "payment_terms", length = 255)
    private String paymentTerms;
    
    /**
     * Currency
     */
    @Column(name = "currency", length = 10)
    private String currency;
    
    /**
     * Credit status (ACTIVE, ON_HOLD, SUSPENDED, BLOCKED)
     */
    @Column(name = "credit_status", length = 20)
    private String creditStatus;
    
    /**
     * Current outstanding balance
     */
    @Column(name = "outstanding_balance", precision = 15, scale = 2)
    private BigDecimal outstandingBalance;
    
    /**
     * Current outstanding (alias for compatibility)
     */
    @Column(name = "current_outstanding")
    private Double currentOutstanding;
    
    /**
     * Available credit
     */
    @Column(name = "available_credit")
    private Double availableCredit;
    
    /**
     * Overdue amount
     */
    @Column(name = "overdue_amount")
    private Double overdueAmount;
    
    /**
     * Total sales amount
     */
    @Column(name = "total_sales_amount")
    private Double totalSalesAmount;
    
    /**
     * Total order count
     */
    @Column(name = "total_order_count")
    private Integer totalOrderCount;
    
    /**
     * Average order value
     */
    @Column(name = "average_order_value")
    private Double averageOrderValue;
    
    /**
     * Last order date
     */
    @Column(name = "last_order_date")
    private LocalDate lastOrderDate;
    
    /**
     * Trade discount percentage
     */
    @Column(name = "trade_discount", precision = 5, scale = 2)
    private BigDecimal tradeDiscount;
    
    /**
     * Price level/tier (STANDARD, PREMIUM, VIP)
     */
    @Column(name = "price_level", length = 20)
    private String priceLevel;
    
    /**
     * Sales territory
     */
    @Column(name = "sales_territory", length = 100)
    private String salesTerritory;
    
    /**
     * Sales representative
     */
    @Column(name = "sales_representative", length = 50)
    private String salesRepresentative;
    
    /**
     * Sales rep ID
     */
    @Column(name = "sales_rep_id", length = 50)
    private String salesRepId;
    
    /**
     * Sales rep name
     */
    @Column(name = "sales_rep_name", length = 100)
    private String salesRepName;
    
    /**
     * Route
     */
    @Column(name = "route", length = 100)
    private String route;
    
    /**
     * Customer since date
     */
    @Column(name = "customer_since")
    private LocalDate customerSince;
    
    /**
     * Status (ACTIVE, INACTIVE, BLOCKED)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Is verified
     */
    @Column(name = "is_verified")
    private Boolean isVerified;
    
    /**
     * Verified date
     */
    @Column(name = "verified_date")
    private LocalDate verifiedDate;
    
    /**
     * Verified by user ID
     */
    @Column(name = "verified_by_user_id")
    private Long verifiedByUserId;
    
    /**
     * Is blacklisted
     */
    @Column(name = "is_blacklisted")
    private Boolean isBlacklisted;
    
    /**
     * Blacklist reason
     */
    @Column(name = "blacklist_reason", length = 500)
    private String blacklistReason;
    
    /**
     * Blacklisted date
     */
    @Column(name = "blacklisted_date")
    private LocalDate blacklistedDate;
    
    /**
     * Has credit facility
     */
    @Column(name = "has_credit_facility")
    private Boolean hasCreditFacility;
    
    /**
     * Suspension reason
     */
    @Column(name = "suspension_reason", length = 500)
    private String suspensionReason;
    
    /**
     * Suspended date
     */
    @Column(name = "suspended_date")
    private LocalDate suspendedDate;
    
    /**
     * Loyalty points
     */
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;
    
    /**
     * Notes
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    /**
     * Internal notes (not visible to customer)
     */
    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;
    
    /**
     * Customer contacts
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CustomerContact> contacts = new HashSet<>();
    
    /**
     * Customer addresses
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CustomerAddress> addresses = new HashSet<>();
    
    /**
     * Customer ledger entries
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @OrderBy("transactionDate DESC")
    @Builder.Default
    private Set<CustomerLedger> ledgerEntries = new HashSet<>();
    
    /**
     * Customer price lists
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CustomerPriceList> priceLists = new HashSet<>();
    
    /**
     * Adds a contact
     */
    public void addContact(CustomerContact contact) {
        contact.setCustomer(this);
        contacts.add(contact);
    }
    
    /**
     * Removes a contact
     */
    public void removeContact(CustomerContact contact) {
        contacts.remove(contact);
        contact.setCustomer(null);
    }
    
    /**
     * Adds an address
     */
    public void addAddress(CustomerAddress address) {
        address.setCustomer(this);
        addresses.add(address);
    }
    
    /**
     * Removes an address
     */
    public void removeAddress(CustomerAddress address) {
        addresses.remove(address);
        address.setCustomer(null);
    }
    
    /**
     * Gets primary contact
     */
    @Transient
    public CustomerContact getPrimaryContact() {
        return contacts.stream()
            .filter(c -> Boolean.TRUE.equals(c.getIsPrimary()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets billing address
     */
    @Transient
    public CustomerAddress getBillingAddress() {
        return addresses.stream()
            .filter(a -> "BILLING".equals(a.getAddressType()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets shipping address
     */
    @Transient
    public CustomerAddress getShippingAddress() {
        return addresses.stream()
            .filter(a -> "SHIPPING".equals(a.getAddressType()))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets available credit
     */
    @Transient
    public BigDecimal getAvailableCredit() {
        if (creditLimit == null || outstandingBalance == null) {
            return creditLimit != null ? creditLimit : BigDecimal.ZERO;
        }
        BigDecimal available = creditLimit.subtract(outstandingBalance);
        return available.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : available;
    }
    
    /**
     * Gets credit utilization percentage
     */
    @Transient
    public BigDecimal getCreditUtilization() {
        if (creditLimit == null || creditLimit.compareTo(BigDecimal.ZERO) == 0 || outstandingBalance == null) {
            return BigDecimal.ZERO;
        }
        return outstandingBalance.divide(creditLimit, 4, RoundingMode.HALF_UP)
                                 .multiply(new BigDecimal("100"));
    }
    
    /**
     * Checks if credit limit is exceeded
     */
    @Transient
    public boolean isCreditLimitExceeded() {
        if (creditLimit == null || outstandingBalance == null) {
            return false;
        }
        return outstandingBalance.compareTo(creditLimit) > 0;
    }
    
    /**
     * Checks if customer can purchase
     */
    @Transient
    public boolean canPurchase(BigDecimal amount) {
        if (!isActive || !"ACTIVE".equals(creditStatus)) {
            return false;
        }
        
        if (creditLimit == null) {
            return true; // No credit limit
        }
        
        BigDecimal newBalance = outstandingBalance != null ? 
            outstandingBalance.add(amount) : amount;
        
        return newBalance.compareTo(creditLimit) <= 0;
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (status == null) {
            status = "ACTIVE";
        }
        if (isActive == null) {
            isActive = true;
        }
        if (isVerified == null) {
            isVerified = false;
        }
        if (isBlacklisted == null) {
            isBlacklisted = false;
        }
        if (hasCreditFacility == null) {
            hasCreditFacility = false;
        }
        if (creditStatus == null) {
            creditStatus = "ACTIVE";
        }
        if (outstandingBalance == null) {
            outstandingBalance = BigDecimal.ZERO;
        }
        if (currentOutstanding == null) {
            currentOutstanding = 0.0;
        }
        if (availableCredit == null) {
            availableCredit = 0.0;
        }
        if (overdueAmount == null) {
            overdueAmount = 0.0;
        }
        if (totalSalesAmount == null) {
            totalSalesAmount = 0.0;
        }
        if (totalOrderCount == null) {
            totalOrderCount = 0;
        }
        if (averageOrderValue == null) {
            averageOrderValue = 0.0;
        }
        if (loyaltyPoints == null) {
            loyaltyPoints = 0;
        }
        if (customerSince == null) {
            customerSince = LocalDate.now();
        }
        if (currency == null) {
            currency = "LKR";
        }
        if (priceLevel == null) {
            priceLevel = "STANDARD";
        }
        // Sync phone fields
        if (phone == null && phoneNumber != null) {
            phone = phoneNumber;
        }
        if (phoneNumber == null && phone != null) {
            phoneNumber = phone;
        }
        // Sync mobile fields
        if (mobile == null && mobileNumber != null) {
            mobile = mobileNumber;
        }
        if (mobileNumber == null && mobile != null) {
            mobileNumber = mobile;
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
