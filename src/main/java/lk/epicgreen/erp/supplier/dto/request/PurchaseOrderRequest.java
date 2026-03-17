package lk.epicgreen.erp.supplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderRequest {

    private Long supplierId;

    private LocalDate poDate;

    @NotBlank(message = "Enter the expected delivery date")
    private LocalDate expectedDeliveryDate;

    @Size(max = 50)
    private String deliveryAddress;
    @Size(max = 50)
    private String paymentTerms;

    private BigDecimal discountPercentage;

    private BigDecimal taxPercentage;

    @NotBlank
    private String poNumber;

    private String supplierName;

    private BigDecimal shippingCost;
    private BigDecimal total;
    private String notes;

    private List<PurchaseOrderItemRequest> purchaseOrderItemRequests;
}
