package lk.epicgreen.erp.customer.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * CustomerContact entity
 * Represents additional contact persons for customers
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "customer_contacts", indexes = {
    @Index(name = "idx_customer_id", columnList = "customer_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerContact {
    
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
    @NotBlank(message = "Contact name is required")
    @Size(max = 100)
    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;
    
    /**
     * Designation / Job title
     */
    @Size(max = 100)
    @Column(name = "designation", length = 100)
    private String designation;
    
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
     * Is primary contact
     */
    @Column(name = "is_primary")
    private Boolean isPrimary;
    
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
     * Check if primary contact
     */
    @Transient
    public boolean isPrimary() {
        return Boolean.TRUE.equals(isPrimary);
    }
    
    /**
     * Get full contact info
     */
    @Transient
    public String getFullContactInfo() {
        StringBuilder info = new StringBuilder();
        info.append(contactName);
        if (designation != null) {
            info.append(" (").append(designation).append(")");
        }
        if (email != null) {
            info.append(" - ").append(email);
        }
        if (mobile != null) {
            info.append(" - ").append(mobile);
        } else if (phone != null) {
            info.append(" - ").append(phone);
        }
        return info.toString();
    }
    
    /**
     * Get preferred phone number (mobile first, then phone)
     */
    @Transient
    public String getPreferredPhone() {
        return mobile != null ? mobile : phone;
    }
    
    @PrePersist
    protected void onCreate() {
        if (isPrimary == null) {
            isPrimary = false;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
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
