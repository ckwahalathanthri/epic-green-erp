package lk.epicgreen.erp.warehouse.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stock_transfer_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transfer_id", nullable = false)
    private Long transferId;
    
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber;
    
    @Column(name = "quantity_requested", precision = 15, scale = 3, nullable = false)
    private BigDecimal quantityRequested;
    
    @Column(name = "quantity_shipped", precision = 15, scale = 3)
    private BigDecimal quantityShipped = BigDecimal.ZERO;
    
    @Column(name = "quantity_received", precision = 15, scale = 3)
    private BigDecimal quantityReceived = BigDecimal.ZERO;
    
    @Column(name = "unit_cost", precision = 15, scale = 4)
    private BigDecimal unitCost;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
