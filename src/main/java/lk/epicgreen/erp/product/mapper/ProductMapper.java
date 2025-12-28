package lk.epicgreen.erp.product.mapper;

import lk.epicgreen.erp.product.dto.request.ProductRequest;
import lk.epicgreen.erp.product.dto.response.ProductResponse;
import lk.epicgreen.erp.product.entity.Product;
import org.springframework.stereotype.Component;

/**
 * Mapper for Product entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        return Product.builder()
            .productCode(request.getProductCode())
            .productName(request.getProductName())
            .productType(request.getProductType())
            .description(request.getDescription())
            .barcode(request.getBarcode())
            .sku(request.getSku())
            .hsnCode(request.getHsnCode())
            .reorderLevel(request.getReorderLevel())
            .minimumStockLevel(request.getMinimumStockLevel())
            .maximumStockLevel(request.getMaximumStockLevel())
            .standardCost(request.getStandardCost())
            .sellingPrice(request.getSellingPrice())
            .shelfLifeDays(request.getShelfLifeDays())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .imageUrl(request.getImageUrl())
            .build();
    }

    public void updateEntityFromRequest(ProductRequest request, Product product) {
        if (request == null || product == null) {
            return;
        }

        product.setProductCode(request.getProductCode());
        product.setProductName(request.getProductName());
        product.setProductType(request.getProductType());
        product.setDescription(request.getDescription());
        product.setBarcode(request.getBarcode());
        product.setSku(request.getSku());
        product.setHsnCode(request.getHsnCode());
        product.setReorderLevel(request.getReorderLevel());
        product.setMinimumStockLevel(request.getMinimumStockLevel());
        product.setMaximumStockLevel(request.getMaximumStockLevel());
        product.setStandardCost(request.getStandardCost());
        product.setSellingPrice(request.getSellingPrice());
        product.setShelfLifeDays(request.getShelfLifeDays());
        product.setImageUrl(request.getImageUrl());
        
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
            .id(product.getId())
            .productCode(product.getProductCode())
            .productName(product.getProductName())
            .productType(product.getProductType())
            .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
            .categoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null)
            .description(product.getDescription())
            .baseUomId(product.getBaseUom() != null ? product.getBaseUom().getId() : null)
            .baseUomCode(product.getBaseUom() != null ? product.getBaseUom().getUomCode() : null)
            .baseUomName(product.getBaseUom() != null ? product.getBaseUom().getUomName() : null)
            .barcode(product.getBarcode())
            .sku(product.getSku())
            .hsnCode(product.getHsnCode())
            .reorderLevel(product.getReorderLevel())
            .minimumStockLevel(product.getMinimumStockLevel())
            .maximumStockLevel(product.getMaximumStockLevel())
            .standardCost(product.getStandardCost())
            .sellingPrice(product.getSellingPrice())
            .shelfLifeDays(product.getShelfLifeDays())
            .isActive(product.getIsActive())
            .imageUrl(product.getImageUrl())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .build();
    }
}
