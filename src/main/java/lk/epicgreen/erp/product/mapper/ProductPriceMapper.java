package lk.epicgreen.erp.product.mapper;


import lk.epicgreen.erp.product.dto.response.ProductPriceResponse;
import lk.epicgreen.erp.product.dto.response.ProductPriceHistoryResponse;
import lk.epicgreen.erp.product.entity.*;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductPriceMapper {
    
    private final ProductRepository productRepository;
    
    public ProductPriceResponse toDTO(ProductPrice entity) {
        ProductPriceResponse dto = new ProductPriceResponse();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getProductName());
        dto.setProductCode(entity.getProduct().getProductCode());
        dto.setPriceType(entity.getPriceType());
        dto.setPrice(entity.getPrice());
        dto.setMinQuantity(entity.getMinQuantity());
        dto.setMaxQuantity(entity.getMaxQuantity());
        dto.setCurrency(entity.getCurrency());
        dto.setIsDefault(entity.getIsDefault());
        dto.setEffectiveFrom(entity.getEffectiveFrom());
        dto.setEffectiveTo(entity.getEffectiveTo());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
    
    public ProductPrice toEntity(ProductPriceResponse dto) {
        ProductPrice entity = new ProductPrice();
        updateEntity(dto, entity);
        return entity;
    }
    
    public void updateEntity(ProductPriceResponse dto, ProductPrice entity) {
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            entity.setProduct(product);
        }
        entity.setPriceType(dto.getPriceType());
        entity.setPrice(dto.getPrice());
        entity.setMinQuantity(dto.getMinQuantity());
        entity.setMaxQuantity(dto.getMaxQuantity());
        entity.setCurrency(dto.getCurrency());
        entity.setIsDefault(dto.getIsDefault());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
        entity.setIsActive(dto.getIsActive());
    }
    
    public ProductPriceHistoryResponse toHistoryDTO(PriceHistory entity) {
        ProductPriceHistoryResponse dto = new ProductPriceHistoryResponse();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getProductName());
        dto.setProductCode(entity.getProduct().getProductCode());
        dto.setOldPrice(entity.getOldPrice());
        dto.setNewPrice(entity.getNewPrice());
        dto.setPriceType(entity.getPriceType());
        dto.setChangeReason(entity.getChangeReason());
        dto.setChangedBy(entity.getChangedBy());
        dto.setChangedAt(entity.getChangedAt());
        return dto;
    }
}
