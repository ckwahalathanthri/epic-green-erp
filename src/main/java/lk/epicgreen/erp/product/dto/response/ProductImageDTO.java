package lk.epicgreen.erp.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {
    private Long id;
    private Long productId;
    private String imageUrl;
    private String imageName;
    private String imageType;
    private Long fileSize;
    private Boolean isPrimary;
    private Integer displayOrder;
    private LocalDateTime uploadedAt;
}
