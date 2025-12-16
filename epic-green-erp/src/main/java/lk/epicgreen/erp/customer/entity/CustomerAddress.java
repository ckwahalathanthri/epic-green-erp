package lk.epicgreen.erp.customer.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

/**
 * CustomerAddress entity
 * Represents customer addresses (billing, shipping, etc.)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customer_addresses", indexes = {
    @Index(name = "idx_customer_address_customer", columnList = "customer_id"),
    @Index(name = "idx_customer_address_type", columnList = "address_type"),
    @Index(name = "idx_customer_address_default", columnList = "is_default")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddress extends AuditEntity {
    
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
    @Column(name = "address_type", nullable = false, length = 20)
    private String addressType;
    
    /**
     * Address name/label
     */
    @Column(name = "address_name", length = 100)
    private String addressName;
    
    /**
     * Address line 1
     */
    @Column(name = "address_line1", nullable = false, length = 255)
    private String addressLine1;
    
    /**
     * Address line 2
     */
    @Column(name = "address_line2", length = 255)
    private String addressLine2;
    
    /**
     * City
     */
    @Column(name = "city", nullable = false, length = 100)
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
    @Column(name = "country", nullable = false, length = 100)
    private String country;
    
    /**
     * Contact person at this address
     */
    @Column(name = "contact_person", length = 100)
    private String contactPerson;
    
    /**
     * Phone number at this address
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    /**
     * Is default address for this type
     */
    @Column(name = "is_default")
    private Boolean isDefault;
    
    /**
     * Is active
     */
    @Column(name = "is_active")
    private Boolean isActive;
    
    /**
     * Notes
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Gets full address as string
     */
    @Transient
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        
        if (addressName != null) {
            address.append(addressName).append("\n");
        }
        
        address.append(addressLine1);
        
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            address.append("\n").append(addressLine2);
        }
        
        address.append("\n").append(city);
        
        if (state != null) {
            address.append(", ").append(state);
        }
        
        if (postalCode != null) {
            address.append(" ").append(postalCode);
        }
        
        address.append("\n").append(country);
        
        return address.toString();
    }
    
    /**
     * Gets single line address
     */
    @Transient
    public String getSingleLineAddress() {
        StringBuilder address = new StringBuilder(addressLine1);
        
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            address.append(", ").append(addressLine2);
        }
        
        address.append(", ").append(city);
        
        if (state != null) {
            address.append(", ").append(state);
        }
        
        if (postalCode != null) {
            address.append(" ").append(postalCode);
        }
        
        address.append(", ").append(country);
        
        return address.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isDefault == null) {
            isDefault = false;
        }
        if (isActive == null) {
            isActive = true;
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
