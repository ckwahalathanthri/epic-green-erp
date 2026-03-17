package lk.epicgreen.erp.supplier.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseOrderDTO {
    private Long id;
    private String poNumber;
    private Long supplierId;
    private String supplierName;
    private LocalDate poDate;
    private String poStatus;
    private BigDecimal totalAmount;
}