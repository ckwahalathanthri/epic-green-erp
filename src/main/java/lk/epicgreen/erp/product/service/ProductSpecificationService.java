package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.response.ProductSpecificationDTO;

import java.util.List;

public interface ProductSpecificationService {
    List<ProductSpecificationDTO> getProductSpecifications(Long productId);
    ProductSpecificationDTO createSpecification(ProductSpecificationDTO dto);
    ProductSpecificationDTO updateSpecification(Long id, ProductSpecificationDTO dto);
    void deleteSpecification(Long id);
    void bulkCreateSpecifications(Long productId, List<ProductSpecificationDTO> specs);
}
