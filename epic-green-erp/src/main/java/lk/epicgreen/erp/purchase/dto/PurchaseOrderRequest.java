package lk.epicgreen.erp.purchase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * PurchaseOrder Request DTO
 * DTO for purchase order operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderRequest {
    
    @NotBlank(message = "Purchase order number is required")
    private String purchaseOrderNumber;
    
    @NotNull(message = "Purchase order date is required")
    private LocalDate purchaseOrderDate;
    
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
    
    private String supplierCode;
    
    private String supplierName;
    
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private LocalDate expectedDeliveryDate;
    
    // ADDED: Missing field that was causing errors
    private LocalDate orderDate;
    
    private String orderType; // STANDARD, URGENT, SPOT
    
    private String priority; // LOW, NORMAL, HIGH, URGENT
    
    private String paymentTerms;
    
    private String paymentMode; // CASH, CREDIT, ADVANCE
    
    private String status; // DRAFT, PENDING_APPROVAL, APPROVED, SENT, RECEIVED, CANCELLED
    
    private Double subtotalAmount;
    
    private Double taxAmount;
    
    private Double discountAmount;
    
    private Double discountPercentage;
    
    private Double shippingAmount;
    
    private Double totalAmount;
    
    private Long approvedByUserId;
    
    private LocalDate approvedDate;
    
    private String approvalNotes;
    
    private String remarks;
    
    private String notes;
    
    private Long createdByUserId;
    
    private Long updatedByUserId;
    
    /**
     * Gets order date
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    /**
     * Gets order type
     */
    public String getOrderType() {
        return orderType;
    }
    
    /**
     * Gets subtotal amount
     */
    public Double getSubtotalAmount() {
        return subtotalAmount;
    }
    
    /**
     * Gets tax amount
     */
    public Double getTaxAmount() {
        return taxAmount;
    }
    
    /**
     * Gets shipping amount
     */
    public Double getShippingAmount() {
        return shippingAmount;
    }
}
