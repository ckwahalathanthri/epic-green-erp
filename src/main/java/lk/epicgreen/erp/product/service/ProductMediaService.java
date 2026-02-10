package lk.epicgreen.erp.product.service;


import lk.epicgreen.erp.product.dto.response.ProductDocumentDTO;
import lk.epicgreen.erp.product.dto.response.ProductImageDTO;

import java.util.List;

public interface ProductMediaService {
    List<ProductImageDTO> getProductImages(Long productId);
    ProductImageDTO uploadImage(ProductImageDTO dto);
    void deleteImage(Long id);
    ProductImageDTO setPrimaryImage(Long productId, Long imageId);
    
    List<ProductDocumentDTO> getProductDocuments(Long productId);
    ProductDocumentDTO uploadDocument(ProductDocumentDTO dto);
    void deleteDocument(Long id);
}
