package lk.epicgreen.erp.supplier.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRatingDTO {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private Integer qualityRating;
    private Integer deliveryRating;
    private Integer priceRating;
    private Integer serviceRating;
    private Integer overallRating;
    private Double onTimeDeliveryPercentage;
    private Double qualityRejectionPercentage;
    private Integer averageLeadTimeDays;
    private Boolean isPreferredSupplier;
    private String comments;
    private LocalDateTime ratingDate;
    private String ratedBy;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
