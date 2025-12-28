package lk.epicgreen.erp.production.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Production Output response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOutputResponse {

    private Long id;
    private Long woId;
    private String woNumber;
    private LocalDate outputDate;
    private Long finishedProductId;
    private String finishedProductCode;
    private String finishedProductName;
    private String batchNumber;
    private BigDecimal quantityProduced;
    private BigDecimal quantityAccepted;
    private BigDecimal quantityRejected;
    private BigDecimal quantityRework;
    private Long uomId;
    private String uomCode;
    private String uomName;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private Long locationId;
    private String locationCode;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private String qualityStatus;
    private Long qualityCheckedBy;
    private String qualityCheckedByName;
    private LocalDateTime qualityCheckedAt;
    private String remarks;
    private LocalDateTime createdAt;
    private Long createdBy;
    private String createdByName;
}
