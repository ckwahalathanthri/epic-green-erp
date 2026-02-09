package com.epicgreen.erp.product.mapper;

import com.epicgreen.erp.product.dto.*;
import com.epicgreen.erp.product.model.*;
import com.epicgreen.erp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductPriceMapper {
    
    private final ProductRepository productRepository;
    
    public ProductPriceDTO toDTO(ProductPrice entity) {
        ProductPriceDTO dto = new ProductPriceDTO();
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
    
    public ProductPrice toEntity(ProductPriceDTO dto) {
        ProductPrice entity = new ProductPrice();
        updateEntity(dto, entity);
        return entity;
    }
    
    public void updateEntity(ProductPriceDTO dto, ProductPrice entity) {
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
    
    public PriceHistoryDTO toHistoryDTO(PriceHistory entity) {
        PriceHistoryDTO dto = new PriceHistoryDTO();
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
