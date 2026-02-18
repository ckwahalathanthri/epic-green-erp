package lk.epicgreen.erp.supplier.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_group_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierGroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_group_id", nullable = false)
    private SupplierGroup supplierGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;


    @Column(name = "joined_date")
    private LocalDateTime joinedDate;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "created_by", length = 100)
    private String createdBy;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
