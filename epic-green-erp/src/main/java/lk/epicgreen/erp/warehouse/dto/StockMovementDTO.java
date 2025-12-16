package lk.epicgreen.erp.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * StockMovement DTO
 * Data transfer object for StockMovement entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockMovementDTO {
    
    private Long id;
    
    private String movementNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate movementDate;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime movementTimestamp;
    
    private String movementType;
    
    /**
     * Product information
     */
    private Long productId;
    
    private String productCode;
    
    private String productName;
    
    /**
     * From warehouse/location
     */
    private Long fromWarehouseId;
    
    private String fromWarehouseCode;
    
    private String fromWarehouseName;
    
    private Long fromLocationId;
    
    private String fromLocationCode;
    
    private String fromLocationName;
    
    /**
     * To warehouse/location
     */
    private Long toWarehouseId;
    
    private String toWarehouseCode;
    
    private String toWarehouseName;
    
    private Long toLocationId;
    
    private String toLocationCode;
    
    private String toLocationName;
    
    /**
     * For single warehouse operations
     */
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    /**
     * Batch/Serial
     */
    private String batchNumber;
    
    private String serialNumber;
    
    /**
     * Quantity and value
     */
    private BigDecimal quantity;
    
    private String unit;
    
    private BigDecimal costPerUnit;
    
    private BigDecimal totalValue;
    
    private String currency;
    
    /**
     * Reference
     */
    private String referenceNumber;
    
    private Long referenceId;
    
    private String referenceType;
    
    /**
     * Additional info
     */
    private String reason;
    
    private String performedBy;
    
    private String approvedBy;
    
    private String status;
    
    private String notes;
    
    /**
     * Computed properties
     */
    private String movementDirection;
    
    private Boolean isInbound;
    
    private Boolean isOutbound;
    
    private Boolean isTransfer;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
}
