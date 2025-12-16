package lk.epicgreen.erp.supplier.entity;

import jakarta.persistence.*;
import lk.epicgreen.erp.common.audit.AuditEntity;
import lombok.*;

/**
 * SupplierContact entity
 * Represents contact persons at supplier organizations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "supplier_contacts", indexes = {
    @Index(name = "idx_supplier_contact_supplier", columnList = "supplier_id"),
    @Index(name = "idx_supplier_contact_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierContact extends AuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Reference to supplier
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false, foreignKey = @ForeignKey(name = "fk_supplier_contact_supplier"))
    private Supplier supplier;
    
    /**
     * Contact person name
     */
    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;
    
    /**
     * Designation/Job title
     */
    @Column(name = "designation", length = 100)
    private String designation;
    
    /**
     * Department
     */
    @Column(name = "department", length = 100)
    private String department;
    
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
     * Extension number
     */
    @Column(name = "extension", length = 10)
    private String extension;
    
    /**
     * Is primary contact
     */
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary;
    
    /**
     * Is active contact
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    /**
     * Notes about this contact
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Gets full contact info
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
        if (!(o instanceof SupplierContact)) return false;
        SupplierContact that = (SupplierContact) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
