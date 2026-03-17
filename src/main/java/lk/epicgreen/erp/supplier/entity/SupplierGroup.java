package lk.epicgreen.erp.supplier.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "supplier_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "group_code", unique = true, nullable = false, length = 50)
    private String groupCode;
    @Column(name = "group_name", nullable = false, length = 100)
    private String groupName;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private Double discountPercentage;
    @Column(name = "credit_days_default")
    private Integer creditDaysDefault;
    @Column(name = "payment_terms_days_default")
    private Integer paymentTermsDaysDefault;
    @Column(name = "lead_time_days_default")
    private Integer leadTimeDaysDefault;
    @Column(name = "minimum_order_value", precision = 15, scale = 2)
    private BigDecimal minimumOrderValue;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @OneToMany(mappedBy = "supplierGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierGroupMember> members = new ArrayList<>();
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
