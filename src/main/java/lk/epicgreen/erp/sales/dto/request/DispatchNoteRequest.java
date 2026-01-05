package lk.epicgreen.erp.sales.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating/updating Dispatch Note
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchNoteRequest {

    @NotBlank(message = "Dispatch number is required")
    @Size(max = 30, message = "Dispatch number must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Dispatch number must contain only uppercase letters, numbers, hyphens and underscores")
    private String dispatchNumber;

    @NotNull(message = "Dispatch date is required")
    private LocalDate dispatchDate;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @Size(max = 20, message = "Vehicle number must not exceed 20 characters")
    private String vehicleNumber;

    @Size(max = 100, message = "Driver name must not exceed 100 characters")
    private String driverName;

    @Size(max = 20, message = "Driver mobile must not exceed 20 characters")
    private String driverMobile;

    private Long deliveryAddressId;

    @Size(max = 20, message = "Route code must not exceed 20 characters")
    private String routeCode;

    @Pattern(regexp = "^(PENDING|LOADING|DISPATCHED|IN_TRANSIT|DELIVERED|RETURNED)$", 
             message = "Status must be one of: PENDING, LOADING, DISPATCHED, IN_TRANSIT, DELIVERED, RETURNED")
    private String status;

    @Size(max = 100, message = "Received by name must not exceed 100 characters")
    private String receivedByName;

    @Size(max = 500, message = "Delivery photo URL must not exceed 500 characters")
    private String deliveryPhotoUrl;

    @DecimalMin(value = "-90.0", message = "GPS latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "GPS latitude must be <= 90")
    private BigDecimal gpsLatitude;

    @DecimalMin(value = "-180.0", message = "GPS longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "GPS longitude must be <= 180")
    private BigDecimal gpsLongitude;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;

    @NotEmpty(message = "At least one dispatch item is required")
    @Valid
    private List<DispatchItemRequest> items;
}
