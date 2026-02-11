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
public class ProductDocumentDTO {
    private Long id;
    private Long productId;
    private String documentUrl;
    private String documentName;
    private String documentType;
    private Long fileSize;
    private String description;
    private LocalDateTime uploadedAt;
}
