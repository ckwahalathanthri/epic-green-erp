package lk.epicgreen.erp.warehouse.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;




import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "storage_zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;


    @Column(name = "zone_code", nullable = false, length = 50)
    private String zoneCode;
    @Column(name = "zone_name", nullable = false, length = 200)
    private String zoneName;
    @Column(name = "zone_type", length = 50)
    private String zoneType;
    @Column(name = "temperature_controlled")
    private Boolean temperatureControlled = false;

//    @OneToMany(mappedBy = "zone", orphanRemoval = true)
//    @JsonManagedReference
//    private List<StorageRack> storageRacks=new ArrayList<>();

    @Column(name = "min_temperature")
    private Double minTemperature;
    @Column(name = "max_temperature")
    private Double maxTemperature;


    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private List<StorageRack> racks = new ArrayList<>();

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
