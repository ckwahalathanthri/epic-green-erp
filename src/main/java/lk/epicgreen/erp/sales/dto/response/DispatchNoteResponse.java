package lk.epicgreen.erp.sales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Dispatch Note response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchNoteResponse {

    private Long id;
    private String dispatchNumber;
    private LocalDate dispatchDate;
    private Long orderId;
    private String orderNumber;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private String vehicleNumber;
    private String driverName;
    private String driverMobile;
    private Long deliveryAddressId;
    private String deliveryAddress;
    private String routeCode;
    private String status;
    private LocalDateTime dispatchTime;
    private LocalDateTime deliveryTime;
    private Long deliveredBy;
    private String deliveredByName;
    private String receivedByName;
    private String receivedBySignature;
    private String deliveryPhotoUrl;
    private BigDecimal gpsLatitude;
    private BigDecimal gpsLongitude;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private List<DispatchItemResponse> items;
}
