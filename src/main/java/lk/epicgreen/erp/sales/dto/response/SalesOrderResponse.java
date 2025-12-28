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
 * DTO for Sales Order response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderResponse {

    private Long id;
    private String orderNumber;
    private LocalDate orderDate;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String customerPoNumber;
    private LocalDate customerPoDate;
    private Long billingAddressId;
    private String billingAddress;
    private Long shippingAddressId;
    private String shippingAddress;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private Long salesRepId;
    private String salesRepName;
    private String orderType;
    private String status;
    private String paymentMode;
    private String deliveryMode;
    private LocalDate expectedDeliveryDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal freightCharges;
    private BigDecimal totalAmount;
    private Long approvedBy;
    private String approvedByName;
    private LocalDateTime approvedAt;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private List<SalesOrderItemResponse> items;
}
