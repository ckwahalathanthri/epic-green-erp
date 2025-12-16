package lk.epicgreen.erp.production.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bom Entity
 * Entity for Bill of Materials
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Entity
@Table(name = "bom")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Bom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "bom_code", unique = true, nullable = false, length = 50)
    private String bomCode;
    
    @Column(name = "bom_name", nullable = false, length = 200)
    private String bomName;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "product_name", length = 200)
    private String productName;
    
    @Column(name = "output_quantity", nullable = false)
    private Double outputQuantity;
    
    @Column(name = "output_unit", length = 20)
    private String outputUnit;
    
    @Column(name = "bom_type", length = 20)
    private String bomType; // STANDARD, VARIANT, PHANTOM
    
    @Column(name = "version")
    private Integer version;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "labor_cost")
    private Double laborCost;
    
    @Column(name = "overhead_cost")
    private Double overheadCost;
    
    @Column(name = "other_cost")
    private Double otherCost;
    
    @Column(name = "total_material_cost")
    private Double totalMaterialCost;
    
    @Column(name = "total_cost")
    private Double totalCost;
    
    @Column(name = "scrap_percentage")
    private Double scrapPercentage;
    
    @Column(name = "yield_percentage")
    private Double yieldPercentage;
    
    @Column(name = "lead_time_days")
    private Integer leadTimeDays;
    
    @Column(name = "status", length = 20)
    private String status; // DRAFT, ACTIVE, INACTIVE, ARCHIVED
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "is_default")
    private Boolean isDefault;
    
    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    @Column(name = "created_by_user_id")
    private Long createdByUserId;
    
    @Column(name = "approved_by_user_id")
    private Long approvedByUserId;
    
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    @Column(name = "approval_notes", columnDefinition = "TEXT")
    private String approvalNotes;
    
    @OneToMany(mappedBy = "bom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BomItem> items;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
