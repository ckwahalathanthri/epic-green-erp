package lk.epicgreen.erp.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxRateResponse {

    private Long id;
    private String taxCode;
    private String taxName;
    private BigDecimal taxPercentage;
    private String taxType;
    private LocalDate applicableFrom;
    private LocalDate applicableTo;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
