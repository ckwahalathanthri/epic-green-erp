package lk.epicgreen.erp.product.service.impl;


import lk.epicgreen.erp.product.dto.response.ProductSpecificationDTO;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.entity.ProductSpecification;
import lk.epicgreen.erp.product.mapper.ProductSpecificationMapper;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.product.repository.ProductSpecificationRepository;
import lk.epicgreen.erp.product.service.ProductSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductSpecificationServiceImpl implements ProductSpecificationService {
    
    private final ProductSpecificationRepository specRepository;
    private final ProductRepository productRepository;
    private final ProductSpecificationMapper specMapper;
    
    @Override
    public List<ProductSpecificationDTO> getProductSpecifications(Long productId) {
        return specRepository.findByProductIdOrderByDisplayOrderAsc(productId)
            .stream()
            .map(specMapper::toDTO)
            .collect(Collectors.toList());
    }
    public List<ProductSpecificationDTO> getSpecifications(){
        return specRepository.findAll()
            .stream()
            .map(specMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProductSpecificationDTO createSpecification(ProductSpecificationDTO dto) {
        Product productFound=productRepository.findById(dto.getProductId()).orElseThrow(
                ()->new RuntimeException("Product not found"));
        ProductSpecification spec = specMapper.toEntity(dto);
        spec.setProduct(productFound);
        ProductSpecification saved = specRepository.save(spec);
        return specMapper.toDTO(saved);
    }
    
    @Override
    public ProductSpecificationDTO updateSpecification(Long id, ProductSpecificationDTO dto) {
        ProductSpecification existing = specRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Specification not found"));
        specMapper.updateEntity(dto, existing);
        ProductSpecification updated = specRepository.save(existing);
        return specMapper.toDTO(updated);
    }
    
    @Override
    public void deleteSpecification(Long id) {
        specRepository.deleteById(id);
    }
    
    @Override
    public void bulkCreateSpecifications(Long productId, List<ProductSpecificationDTO> specs) {
        specs.forEach(dto -> {
            dto.setProductId(productId);
            createSpecification(dto);
        });
    }
}
