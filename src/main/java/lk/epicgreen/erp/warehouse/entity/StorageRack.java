package lk.epicgreen.erp.warehouse.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "storage_racks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageRack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private StorageZone zone;

    @Column(name = "rack_code", nullable = false, length = 50)
    private String rackCode;

    @Column(name = "rack_name", length = 200)
    private String rackName;

    @Column(name = "rack_type", length = 50)
    private String rackType;

    @OneToMany(mappedBy = "rack", orphanRemoval = true)
    @JsonManagedReference
    private List<StorageBin> storageBins=new ArrayList<>();

    @Column(name = "capacity")
    private Double capacity;

    @Column(name = "capacity_unit", length = 20)
    private String capacityUnit;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "rack", cascade = CascadeType.ALL)
    private List<StorageBin> bins = new ArrayList<>();

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
