package lk.epicgreen.erp.admin.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxRateRequest {

    @NotBlank(message = "Tax code is required")
    @Size(max = 20, message = "Tax code must not exceed 20 characters")
    private String taxCode;

    @NotBlank(message = "Tax name is required")
    @Size(max = 100, message = "Tax name must not exceed 100 characters")
    private String taxName;

    @NotNull(message = "Tax percentage is required")
    @DecimalMin(value = "0.0", message = "Tax percentage must be >= 0")
    @DecimalMax(value = "100.0", message = "Tax percentage must be <= 100")
    private BigDecimal taxPercentage;

    @NotBlank(message = "Tax type is required")
    @Pattern(regexp = "^(GST|VAT|SALES_TAX|SERVICE_TAX|OTHER)$", 
             message = "Invalid tax type")
    private String taxType;

    @NotNull(message = "Applicable from date is required")
    private LocalDate applicableFrom;

    private LocalDate applicableTo;
}
