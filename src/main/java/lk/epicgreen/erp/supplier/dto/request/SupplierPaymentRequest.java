package lk.epicgreen.erp.supplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierPaymentRequest {
    @NotNull
    private Long supplierId;

    @NotNull
    private LocalDate paymentDate;

    @NotBlank
    private String paymentMethod;

    private String supplierName;

    @NotNull
    @DecimalMin("0.01")
    private Double amount;

    private String referenceNumber;
    private String chequeNumber;
    private LocalDate chequeDate;
    private String bankName;
    private String notes;
}
