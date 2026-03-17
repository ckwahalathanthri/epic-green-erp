package lk.epicgreen.erp.product.mapper;


import lk.epicgreen.erp.product.dto.response.ProductImageDTO;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.entity.ProductImage;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {
    
    private final ProductRepository productRepository;
    
    public ProductImageDTO toDTO(ProductImage entity) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setImageUrl(entity.getImageUrl());
        dto.setImageName(entity.getImageName());
        dto.setImageType(entity.getImageType());
        dto.setFileSize(entity.getFileSize());
        dto.setIsPrimary(entity.getIsPrimary());
        dto.setDisplayOrder(entity.getDisplayOrder());
        dto.setUploadedAt(entity.getUploadedAt());
        return dto;
    }
    
    public ProductImage toEntity(ProductImageDTO dto) {
        ProductImage entity = new ProductImage();
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            entity.setProduct(product);
        }
        entity.setImageUrl(dto.getImageUrl());
        entity.setImageName(dto.getImageName());
        entity.setImageType(dto.getImageType());
        entity.setFileSize(dto.getFileSize());
        entity.setIsPrimary(dto.getIsPrimary());
        entity.setDisplayOrder(dto.getDisplayOrder());
        return entity;
    }
}
