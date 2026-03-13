package lk.epicgreen.erp.supplier.dto.request;

import lk.epicgreen.erp.supplier.entity.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderItemRequest {
    private Long id;
    private Long productId;

    @Size(max = 50)
    private String productCode;
    @Size(max = 50)
    private String productName;
    @Size(max = 150)
    private Integer quantity;

    @Size(max = 20)

    private String unitOfMeasure;

    private BigDecimal unitPrice;

    private BigDecimal discount;

    private BigDecimal discountAmount;

    private BigDecimal taxPercentage;

    private BigDecimal taxAmount;

    private BigDecimal totalPrice;

}
