package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_terms")
@Data
public class PaymentTerm {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "term_code", unique = true, length = 20, nullable = false)
    private String termCode; // CASH, NET_30, NET_60, NET_90
    
    @Column(name = "term_name", length = 100, nullable = false)
    private String termName; // Cash on Delivery, Net 30 Days, etc.
    
    @Column(name = "days", nullable = false)
    private Integer days; // 0 for cash, 30, 60, 90 for credit
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void setTimestamps() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
}