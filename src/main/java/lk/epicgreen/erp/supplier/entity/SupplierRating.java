package lk.epicgreen.erp.supplier.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;


    @Column(name = "quality_rating")
    private Integer qualityRating;
    @Column(name = "delivery_rating")
    private Integer deliveryRating;
    @Column(name = "price_rating")
    private Integer priceRating;
    @Column(name = "service_rating")
    private Integer serviceRating;
    @Column(name = "overall_rating")
    private Integer overallRating;
    @Column(name = "on_time_delivery_percentage", precision = 5, scale = 2)
    private Double onTimeDeliveryPercentage;
    @Column(name = "quality_rejection_percentage", precision = 5, scale = 2)
    private Double qualityRejectionPercentage;
    @Column(name = "average_lead_time_days")
    private Integer averageLeadTimeDays;
    @Column(name = "is_preferred_supplier")
    private Boolean isPreferredSupplier = false;
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
    @Column(name = "rating_date")
    private LocalDateTime ratingDate;
    @Column(name = "rated_by", length = 100)
    private String ratedBy;
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
