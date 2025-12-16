package lk.epicgreen.erp.customer.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

/**
 * CustomerContact entity
 * Represents contact persons for customers
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customer_contacts", indexes = {
    @Index(name = "idx_customer_contact_customer", columnList = "customer_id"),
    @Index(name = "idx_customer_contact_primary", columnList = "is_primary"),
    @Index(name = "idx_customer_contact_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerContact extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_customer_contact_customer"))
    private Customer customer;
    
    /**
     * Contact person name
     */
    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;
    
    /**
     * Designation/Title
     */
    @Column(name = "designation", length = 100)
    private String designation;
    
    /**
     * Department
     */
    @Column(name = "department", length = 100)
    private String department;
    
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
     * Mobile number
     */
    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;
    
    /**
     * Is primary contact
     */
    @Column(name = "is_primary")
    private Boolean isPrimary;
    
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
     * Gets full contact information
     */
    @Transient
    public String getFullContactInfo() {
        StringBuilder info = new StringBuilder(contactName);
        
        if (designation != null) {
            info.append(" - ").append(designation);
        }
        
        if (department != null) {
            info.append(" (").append(department).append(")");
        }
        
        return info.toString();
    }
    
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (isPrimary == null) {
            isPrimary = false;
        }
        if (isActive == null) {
            isActive = true;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerContact)) return false;
        CustomerContact that = (CustomerContact) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
