package lk.epicgreen.erp.sales.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderPricingDTO {
    private Long id;
    private String ruleCode;
    private String ruleName;
    private String ruleType;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal minQuantity;
    private Long customerId;
    private Long productId;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean isActive;
    private Integer priority;
    private String description;
}