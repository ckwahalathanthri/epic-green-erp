package lk.epicgreen.erp.production.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Bill of Materials response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillOfMaterialsResponse {

    private Long id;
    private String bomCode;
    private Long finishedProductId;
    private String finishedProductCode;
    private String finishedProductName;
    private String bomVersion;
    private BigDecimal outputQuantity;
    private Long outputUomId;
    private String outputUomCode;
    private String outputUomName;
    private Integer productionTimeMinutes;
    private BigDecimal laborCost;
    private BigDecimal overheadCost;
    private BigDecimal totalMaterialCost;
    private BigDecimal totalCost;
    private Boolean isActive;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BomItemResponse> items;
}
