package lk.epicgreen.erp.production.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating/updating Bill of Materials
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillOfMaterialsRequest {

    @NotBlank(message = "BOM code is required")
    @Size(max = 30, message = "BOM code must not exceed 30 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "BOM code must contain only uppercase letters, numbers, hyphens and underscores")
    private String bomCode;

    @NotNull(message = "Finished product ID is required")
    private Long finishedProductId;

    @Size(max = 10, message = "BOM version must not exceed 10 characters")
    private String bomVersion;

    @NotNull(message = "Output quantity is required")
    @DecimalMin(value = "0.001", message = "Output quantity must be > 0")
    private BigDecimal outputQuantity;

    @NotNull(message = "Output UOM ID is required")
    private Long outputUomId;

    @Min(value = 0, message = "Production time must be >= 0")
    private Integer productionTimeMinutes;

    @DecimalMin(value = "0.0", message = "Labor cost must be >= 0")
    private BigDecimal laborCost;

    @DecimalMin(value = "0.0", message = "Overhead cost must be >= 0")
    private BigDecimal overheadCost;

    private Boolean isActive;

    @NotNull(message = "Effective from date is required")
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    private String remarks;

    @NotEmpty(message = "BOM items are required")
    private List<BomItemRequest> items;
}
