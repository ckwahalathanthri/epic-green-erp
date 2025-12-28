package lk.epicgreen.erp.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for customer price list response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPriceListResponse {

    private Long id;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal specialPrice;
    private BigDecimal discountPercentage;
    private BigDecimal minQuantity;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
