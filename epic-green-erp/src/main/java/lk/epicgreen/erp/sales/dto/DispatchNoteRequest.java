package lk.epicgreen.erp.sales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DispatchNote Request DTO
 * DTO for dispatch note operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchNoteRequest {
    
    @NotBlank(message = "Dispatch note number is required")
    private String dispatchNoteNumber;
    
    @NotNull(message = "Dispatch date is required")
    private LocalDate dispatchDate;
    
    @NotNull(message = "Sales order ID is required")
    private Long salesOrderId;
    
    private String salesOrderNumber;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    
    private Long invoiceId;
    
    private String invoiceNumber;
    
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;
    
    private String warehouseName;
    
    private String shippingAddress;
    
    private String shippingCity;
    
    private String shippingState;
    
    private String shippingCountry;
    
    private String shippingPostalCode;
    
    private String shippingMethod; // SELF_PICKUP, COURIER, COMPANY_DELIVERY, THIRD_PARTY
    
    private String courierName;
    
    private String trackingNumber;
    
    private String vehicleNumber;
    
    private String driverName;
    
    private String driverContact;
    
    private LocalDate expectedDeliveryDate;
    
    private LocalDate actualDeliveryDate;
    
    private String deliveryStatus; // PENDING, DISPATCHED, IN_TRANSIT, DELIVERED, FAILED
    
    private String receivedBy;
    
    private String receiverContact;
    
    private String receiverSignature;
    
    private String dispatchedBy;
    
    private String description;
    
    private String notes;
    
    private String status; // DRAFT, PREPARED, DISPATCHED, DELIVERED, CANCELLED
    
    private Long createdByUserId;
    
    private Long dispatchedByUserId;
    
    private LocalDate dispatchedDate;
    
    @NotNull(message = "Dispatch note items are required")
    private List<DispatchNoteItemRequest> items;
}
