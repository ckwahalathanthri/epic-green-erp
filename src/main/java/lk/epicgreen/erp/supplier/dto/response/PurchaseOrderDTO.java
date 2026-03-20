package lk.epicgreen.erp.supplier.dto.response;
import lk.epicgreen.erp.supplier.entity.PurchaseOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDTO {
    private Long id;
    private String poNumber;
    private Long supplierId;
    private String supplierName;
    private LocalDate expectedDeliveryDate;
    private LocalDate poDate;
    private String poStatus;
    private BigDecimal totalAmount;
    private List<PurchaseOrderItem> items;
}