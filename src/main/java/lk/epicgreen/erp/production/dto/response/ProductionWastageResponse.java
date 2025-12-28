package lk.epicgreen.erp.production.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Production Wastage response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionWastageResponse {

    private Long id;
    private Long woId;
    private String woNumber;
    private LocalDate wastageDate;
    private Long productId;
    private String productCode;
    private String productName;
    private String wastageType;
    private BigDecimal quantity;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private BigDecimal unitCost;
    private BigDecimal totalValue;
    private String reason;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
}
