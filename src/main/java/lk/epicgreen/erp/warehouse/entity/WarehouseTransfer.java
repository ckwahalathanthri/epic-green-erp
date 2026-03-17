package lk.epicgreen.erp.warehouse.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouse_transfers")
@Data
public class WarehouseTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transfer_number", unique = true, length = 50)
    private String transferNumber;
    
    @Column(name = "from_warehouse_id")
    private Long fromWarehouseId;
    
    @Column(name = "to_warehouse_id")
    private Long toWarehouseId;
    
    @Column(name = "transfer_date")
    private LocalDate transferDate;
    
    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;
    
    @Column(name = "transfer_type", length = 50)
    private String transferType;
    
    @Column(name = "transfer_status", length = 50)
    private String transferStatus;
    
    @Column(name = "priority", length = 20)
    private String priority;
    
    @Column(name = "approved_by", length = 100)
    private String approvedBy;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @OneToMany(mappedBy = "warehouseTransfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferItem> items = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}