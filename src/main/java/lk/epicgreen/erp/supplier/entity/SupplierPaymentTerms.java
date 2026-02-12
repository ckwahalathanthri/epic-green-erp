package lk.epicgreen.erp.supplier.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_payment_terms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPaymentTerms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "terms_code", unique = true, nullable = false, length = 50)
    private String termsCode;
    @Column(name = "terms_name", nullable = false, length = 100)
    private String termsName;
    @Column(name = "days", nullable = false)
    private Integer days;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private Double discountPercentage;
    @Column(name = "discount_days")
    private Integer discountDays;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "created_by", length = 100)
    private String createdBy;
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
