package lk.epicgreen.erp.supplier.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SupplierPaymentDTO {
    private Long id;
    private String paymentNumber;
    private Long supplierId;
    private String supplierName;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
}