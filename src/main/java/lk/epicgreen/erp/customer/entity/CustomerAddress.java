package lk.epicgreen.erp.customer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * CustomerAddress entity
 * Represents multiple delivery/billing addresses for customers
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customer_addresses", indexes = {
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_address_type", columnList = "address_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_address_customer"))
    private Customer customer;
    
    /**
     * Address type (BILLING, SHIPPING, BOTH)
     */
    @NotBlank(message = "Address type is required")
    @Column(name = "address_type", nullable = false, length = 10)
    private String addressType;
    
    /**
     * Address name (e.g., "Main Office", "Warehouse", "Branch 1")
     */
    @Size(max = 100)
    @Column(name = "address_name", length = 100)
    private String addressName;
    
    /**
     * Address line 1
     */
    @NotBlank(message = "Address line 1 is required")
    @Size(max = 200)
    @Column(name = "address_line1", nullable = false, length = 200)
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
    
    /**
     * State
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
     * Contact person at this address
     */
    @Size(max = 100)
    @Column(name = "contact_person", length = 100)
    private String contactPerson;
    
    /**
     * Contact phone at this address
     */
    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;
    
    /**
     * Is default address
     */
    @Column(name = "is_default")
    private Boolean isDefault;
    
    /**
     * Created timestamp
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Created by (user ID)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * Check if billing address
     */
    @Transient
    public boolean isBillingAddress() {
        return "BILLING".equals(addressType) || "BOTH".equals(addressType);
    }
    
    /**
     * Check if shipping address
     */
    @Transient
    public boolean isShippingAddress() {
        return "SHIPPING".equals(addressType) || "BOTH".equals(addressType);
    }
    
    /**
     * Check if default address
     */
    @Transient
    public boolean isDefault() {
        return Boolean.TRUE.equals(isDefault);
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
     * Get address with name
     */
    @Transient
    public String getAddressWithName() {
        StringBuilder result = new StringBuilder();
        if (addressName != null) {
            result.append(addressName).append(": ");
        }
        result.append(getFullAddress());
        return result.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        if (isDefault == null) {
            isDefault = false;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (country == null) {
            country = "Sri Lanka";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerAddress)) return false;
        CustomerAddress that = (CustomerAddress) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
