package lk.epicgreen.erp.product.mapper;


import lk.epicgreen.erp.product.dto.response.ProductSpecificationDTO;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.entity.ProductSpecification;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSpecificationMapper {
    
    private final ProductRepository productRepository;
    
    public ProductSpecificationDTO toDTO(ProductSpecification entity) {
        ProductSpecificationDTO dto = new ProductSpecificationDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setSpecName(entity.getSpecName());
        dto.setSpecValue(entity.getSpecValue());
        dto.setSpecUnit(entity.getSpecUnit());
        dto.setDisplayOrder(entity.getDisplayOrder());
        dto.setIsRequired(entity.getIsRequired());
        return dto;
    }
    
    public ProductSpecification toEntity(ProductSpecificationDTO dto) {
        ProductSpecification entity = new ProductSpecification();
        updateEntity(dto, entity);
        return entity;
    }
    
    public void updateEntity(ProductSpecificationDTO dto, ProductSpecification entity) {
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            entity.setProduct(product);
        }
        entity.setSpecName(dto.getSpecName());
        entity.setSpecValue(dto.getSpecValue());
        entity.setSpecUnit(dto.getSpecUnit());
        entity.setDisplayOrder(dto.getDisplayOrder());
        entity.setIsRequired(dto.getIsRequired());
    }
}
