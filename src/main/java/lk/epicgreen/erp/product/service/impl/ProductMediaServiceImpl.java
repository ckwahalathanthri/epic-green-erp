package lk.epicgreen.erp.product.service.impl;


import lk.epicgreen.erp.product.dto.response.ProductDocumentDTO;
import lk.epicgreen.erp.product.dto.response.ProductImageDTO;
import lk.epicgreen.erp.product.entity.ProductDocument;
import lk.epicgreen.erp.product.entity.ProductImage;
import lk.epicgreen.erp.product.mapper.ProductDocumentMapper;
import lk.epicgreen.erp.product.mapper.ProductImageMapper;
import lk.epicgreen.erp.product.repository.ProductDocumentRepository;
import lk.epicgreen.erp.product.repository.ProductImageRepository;
import lk.epicgreen.erp.product.service.ProductMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductMediaServiceImpl implements ProductMediaService {
    
    private final ProductImageRepository imageRepository;
    private final ProductDocumentRepository documentRepository;
    private final ProductImageMapper imageMapper;
    private final ProductDocumentMapper documentMapper;
    
    @Override
    public List<ProductImageDTO> getProductImages(Long productId) {
        return imageRepository.findByProductIdOrderByDisplayOrderAsc(productId)
            .stream()
            .map(imageMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProductImageDTO uploadImage(ProductImageDTO dto) {
        ProductImage image = imageMapper.toEntity(dto);
        ProductImage saved = imageRepository.save(image);
        return imageMapper.toDTO(saved);
    }
    
    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
    
    @Override
    public ProductImageDTO setPrimaryImage(Long productId, Long imageId) {
        List<ProductImage> images = imageRepository.findByProductIdOrderByDisplayOrderAsc(productId);
        images.forEach(img -> {
            img.setIsPrimary(img.getId().equals(imageId));
            imageRepository.save(img);
        });
        
        ProductImage primary = imageRepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("Image not found"));
        return imageMapper.toDTO(primary);
    }
    
    @Override
    public List<ProductDocumentDTO> getProductDocuments(Long productId) {
        return documentRepository.findByProductId(productId)
            .stream()
            .map(documentMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProductDocumentDTO uploadDocument(ProductDocumentDTO dto) {
        ProductDocument document = documentMapper.toEntity(dto);
        ProductDocument saved = documentRepository.save(document);
        return documentMapper.toDTO(saved);
    }
    
    @Override
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}
