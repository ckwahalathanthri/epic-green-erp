package lk.epicgreen.erp.product.mapper;


import lk.epicgreen.erp.product.dto.response.ProductDocumentDTO;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.entity.ProductDocument;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDocumentMapper {
    
    private final ProductRepository productRepository;
    
    public ProductDocumentDTO toDTO(ProductDocument entity) {
        ProductDocumentDTO dto = new ProductDocumentDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setDocumentUrl(entity.getDocumentUrl());
        dto.setDocumentName(entity.getDocumentName());
        dto.setDocumentType(entity.getDocumentType());
        dto.setFileSize(entity.getFileSize());
        dto.setDescription(entity.getDescription());
        dto.setUploadedAt(entity.getUploadedAt());
        return dto;
    }
    
    public ProductDocument toEntity(ProductDocumentDTO dto) {
        ProductDocument entity = new ProductDocument();
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            entity.setProduct(product);
        }
        entity.setDocumentUrl(dto.getDocumentUrl());
        entity.setDocumentName(dto.getDocumentName());
        entity.setDocumentType(dto.getDocumentType());
        entity.setFileSize(dto.getFileSize());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
