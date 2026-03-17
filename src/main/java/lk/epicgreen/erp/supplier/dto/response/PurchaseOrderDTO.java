package lk.epicgreen.erp.supplier.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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
}