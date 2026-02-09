package lk.epicgreen.erp.product.service.impl;

import lk.epicgreen.erp.product.dto.*;
import lk.epicgreen.erp.product.dto.response.ProductPriceResponse;
import lk.epicgreen.erp.product.dto.response.BulkPriceUpdateResponse;
import lk.epicgreen.erp.product.dto.response.ProductPriceHistoryResponse;
import lk.epicgreen.erp.product.entity.*;
import lk.epicgreen.erp.product.repository.*;
import lk.epicgreen.erp.product.service.ProductPriceService;
import lk.epicgreen.erp.product.mapper.ProductPriceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductPriceServiceImpl implements ProductPriceService {
    
    private final ProductPriceRepository priceRepository;
    private final PriceHistoryRepository historyRepository;
    private final ProductRepository productRepository;
    private final ProductPriceMapper priceMapper;
    
    @Override
    public List<ProductPriceResponse> getProductPrices(Long productId) {
        return priceRepository.findByProductId(productId)
            .stream()
            .map(priceMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProductPriceResponse getDefaultPrice(Long productId) {
        return priceRepository.findByProductIdAndIsDefaultTrue(productId)
            .map(priceMapper::toDTO)
            .orElse(null);
    }
    
    @Override
    public ProductPriceResponse createPrice(ProductPriceResponse dto) {
        ProductPrice price = priceMapper.toEntity(dto);
        ProductPrice saved = priceRepository.save(price);
        
        // Create history
        createPriceHistory(saved, null, saved.getPrice(), "Price created");
        
        return priceMapper.toDTO(saved);
    }
    
    @Override
    public ProductPriceResponse updatePrice(Long id, ProductPriceResponse dto) {
        ProductPrice existing = priceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Price not found"));
        
        BigDecimal oldPrice = existing.getPrice();
        priceMapper.updateEntity(dto, existing);
        ProductPrice updated = priceRepository.save(existing);
        
        // Create history if price changed
        if (!oldPrice.equals(updated.getPrice())) {
            createPriceHistory(updated, oldPrice, updated.getPrice(), "Price updated");
        }
        
        return priceMapper.toDTO(updated);
    }
    
    @Override
    public void deletePrice(Long id) {
        priceRepository.deleteById(id);
    }
    
    @Override
    public ProductPriceResponse setAsDefault(Long productId, Long priceId) {
        // Remove default from other prices
        List<ProductPrice> existingDefaults = priceRepository.findByProductId(productId);
        existingDefaults.forEach(p -> {
            if (p.getIsDefault()) {
                p.setIsDefault(false);
                priceRepository.save(p);
            }
        });
        
        // Set new default
        ProductPrice price = priceRepository.findById(priceId)
            .orElseThrow(() -> new RuntimeException("Price not found"));
        price.setIsDefault(true);
        ProductPrice updated = priceRepository.save(price);
        
        return priceMapper.toDTO(updated);
    }
    
    @Override
    public void bulkUpdatePrices(BulkPriceUpdateResponse dto) {
        List<Product> products = productRepository.findAllById(dto.getProductIds());
        
        for (Product product : products) {
            ProductPrice price = priceRepository.findByProductIdAndIsDefaultTrue(product.getId())
                .orElse(null);
            
            if (price != null) {
                BigDecimal oldPrice = price.getPrice();
                BigDecimal newPrice = calculateNewPrice(oldPrice, dto);
                price.setPrice(newPrice);
                priceRepository.save(price);
                
                createPriceHistory(price, oldPrice, newPrice, dto.getReason());
            }
        }
    }
    
    @Override
    public Page<ProductPriceHistoryResponse> getPriceHistory(Long productId, Pageable pageable) {
        return historyRepository.findByProductId(productId, pageable)
            .map(priceMapper::toHistoryDTO);
    }
    
    private void createPriceHistory(ProductPrice price, BigDecimal oldPrice, 
                                   BigDecimal newPrice, String reason) {
        PriceHistory history = new PriceHistory();
        history.setProduct(price.getProduct());
        history.setOldPrice(oldPrice);
        history.setNewPrice(newPrice);
        history.setPriceType(price.getPriceType());
        history.setChangeReason(reason);
        historyRepository.save(history);
    }
    
    private BigDecimal calculateNewPrice(BigDecimal oldPrice, BulkPriceUpdateResponse dto) {
        switch (dto.getUpdateType()) {
            case "PERCENTAGE":
                return oldPrice.add(oldPrice.multiply(dto.getValue().divide(new BigDecimal(100))));
            case "FIXED_AMOUNT":
                return oldPrice.add(dto.getValue());
            case "SET_PRICE":
                return dto.getValue();
            default:
                return oldPrice;
        }
    }

    
}
