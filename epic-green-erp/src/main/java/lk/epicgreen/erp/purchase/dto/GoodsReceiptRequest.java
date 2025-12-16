package lk.epicgreen.erp.purchase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * GoodsReceipt Request DTO
 * DTO for goods receipt operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptRequest {
    
    @NotBlank(message = "GRN number is required")
    private String grnNumber;
    
    @NotNull(message = "Receipt date is required")
    private LocalDate receiptDate;
    
    @NotNull(message = "Purchase order ID is required")
    private Long purchaseOrderId;
    
    private String purchaseOrderNumber;
    
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
    
    private String supplierName;
    
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;
    
    private String warehouseName;
    
    private String receivedBy;
    
    private String inspectedBy;
    
    private String approvedBy;
    
    private String vehicleNumber;
    
    private String driverName;
    
    private String driverContact;
    
    private String invoiceNumber;
    
    private LocalDate invoiceDate;
    
    private String challanNumber;
    
    private LocalDate challanDate;
    
    private String description;
    
    private String notes;
    
    private String status; // DRAFT, RECEIVED, INSPECTED, APPROVED, REJECTED
    
    private String inspectionStatus; // PENDING, PASSED, FAILED, PARTIAL
    
    private String qualityStatus; // APPROVED, REJECTED, HOLD
    
    private Long createdByUserId;
    
    private Long approvedByUserId;
    
    private String approvalNotes;
    
    @NotNull(message = "Goods receipt items are required")
    private List<GoodsReceiptItemRequest> items;
}
