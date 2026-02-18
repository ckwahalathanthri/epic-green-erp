package lk.epicgreen.erp.warehouse.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "storage_bins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageBin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_id", nullable = false)
    private StorageRack rack;

    @Column(name = "bin_code", nullable = false, length = 50)
    private String binCode;
    @Column(name = "bin_name", length = 200)
    private String binName;
    @Column(name = "bin_type", length = 50)
    private String binType;
    @Column(name = "capacity")

    @OneToMany(mappedBy = "bin", orphanRemoval = true)
    @JsonManagedReference
    private List<IssueItem> issueItems=new ArrayList<>();


    @OneToMany(mappedBy = "bin", orphanRemoval = true)
    @JsonManagedReference
    private List<StockReservation> storageReservation=new ArrayList<>();

    @OneToMany(mappedBy = "bin", orphanRemoval = true)
    @JsonManagedReference
    private List<StockLevel> stockLevels=new ArrayList<>();

    private Double capacity;
    @Column(name = "capacity_unit", length = 20)
    private String capacityUnit;
    @Column(name = "current_occupancy")
    private Double currentOccupancy = 0.0;
    @Column(name = "is_occupied")
    private Boolean isOccupied = false;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "barcode", length = 100)
    private String barcode;
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
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
