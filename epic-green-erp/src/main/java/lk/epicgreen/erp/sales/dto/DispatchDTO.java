package lk.epicgreen.erp.sales.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Dispatch DTO
 * Data transfer object for DispatchNote entity
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
public class DispatchDTO {
    
    private Long id;
    
    private String dispatchNumber;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate dispatchDate;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime dispatchTimestamp;
    
    /**
     * References
     */
    private Long salesOrderId;
    
    private String salesOrderNumber;
    
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    /**
     * Delivery details
     */
    private Long deliveryAddressId;
    
    private String deliveryAddressText;
    
    /**
     * Transport details
     */
    private String vehicleNumber;
    
    private String driverName;
    
    private String driverContact;
    
    private String transporterName;
    
    private String trackingNumber;
    
    /**
     * Personnel
     */
    private String dispatchedBy;
    
    /**
     * Status
     */
    private String status;
    
    private String deliveryStatus;
    
    /**
     * Delivery confirmation
     */
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate deliveredDate;
    
    private String receivedBy;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate receivedDate;
    
    /**
     * Posting
     */
    private Boolean isPosted;
    
    @JsonFormat(pattern = AppConstants.DATE_FORMAT)
    private LocalDate postedDate;
    
    private String postedBy;
    
    /**
     * Instructions and notes
     */
    private String deliveryInstructions;
    
    private String notes;
    
    private Integer totalItems;
    
    /**
     * Dispatch items
     */
    private List<DispatchItemDTO> items;
    
    /**
     * Statistics
     */
    private BigDecimal totalDispatchedQuantity;
    
    /**
     * Computed properties
     */
    private Boolean canPost;
    
    private Boolean canEdit;
    
    private Boolean isDelivered;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
    
    /**
     * Dispatch Item DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DispatchItemDTO {
        
        private Long id;
        
        /**
         * References
         */
        private Long orderItemId;
        
        private Long productId;
        
        private String productCode;
        
        private String productName;
        
        private Long locationId;
        
        private String locationCode;
        
        private String locationName;
        
        /**
         * Description
         */
        private String itemDescription;
        
        /**
         * Batch/Serial
         */
        private String batchNumber;
        
        private String serialNumber;
        
        /**
         * Quantities
         */
        private BigDecimal orderedQuantity;
        
        private BigDecimal dispatchedQuantity;
        
        private String unit;
        
        /**
         * Pricing (for reference)
         */
        private BigDecimal unitPrice;
        
        private BigDecimal totalValue;
        
        private String notes;
        
        /**
         * Computed properties
         */
        private BigDecimal varianceQuantity;
        
        private Boolean isFullyDispatched;
    }
}
