package lk.epicgreen.erp.returns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * SalesReturn Request DTO
 * DTO for sales return operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReturnRequest {
    
    @NotBlank(message = "Return number is required")
    private String returnNumber;
    
    @NotNull(message = "Return date is required")
    private LocalDate returnDate;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    
    private Long invoiceId;
    
    private String invoiceNumber;
    
    private LocalDate invoiceDate;
    
    private Long salesOrderId;
    
    private String salesOrderNumber;
    
    @NotBlank(message = "Return type is required")
    private String returnType; // FULL_RETURN, PARTIAL_RETURN, EXCHANGE
    
    @NotBlank(message = "Return reason is required")
    private String returnReason; // DEFECTIVE, WRONG_ITEM, DAMAGED, CUSTOMER_REQUEST, QUALITY_ISSUE
    
    private String reasonDescription;
    
    private Long warehouseId;
    
    private String warehouseName;
    
    private String receivedBy;
    
    private String inspectedBy;
    
    private String inspectionNotes;
    
    private String qualityStatus; // APPROVED, REJECTED, PARTIAL
    
    private Double refundAmount;
    
    private String refundMethod; // CASH, CHEQUE, BANK_TRANSFER, CREDIT_NOTE, STORE_CREDIT
    
    private String creditNoteNumber;
    
    private LocalDate creditNoteDate;
    
    private String description;
    
    private String notes;
    
    private String status; // DRAFT, RECEIVED, INSPECTED, APPROVED, REJECTED, REFUNDED, COMPLETED
    
    private String restockingStatus; // PENDING, RESTOCKED, DISPOSED, RETURNED_TO_SUPPLIER
    
    private Long createdByUserId;
    
    private Long approvedByUserId;
    
    private LocalDate approvedDate;
    
    private String approvalNotes;
    
    @NotNull(message = "Sales return items are required")
    private List<SalesReturnItemRequest> items;
}
