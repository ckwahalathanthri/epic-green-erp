package lk.epicgreen.erp.credit.controller.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGroup {
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

    @Column(name = "credit_limit_default", precision = 15, scale = 2)
    private Double creditLimitDefault;

    @Column(name = "payment_terms_days_default")
    private Integer paymentTermsDaysDefault;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "customerGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerGroupMember> members = new ArrayList<>();

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
