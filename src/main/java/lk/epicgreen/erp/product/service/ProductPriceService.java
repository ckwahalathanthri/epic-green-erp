package lk.epicgreen.erp.product.service;

import lk.epicgreen.erp.product.dto.*;
import lk.epicgreen.erp.product.dto.response.BulkPriceUpdateResponse;
import lk.epicgreen.erp.product.dto.response.ProductPriceHistoryResponse;
import lk.epicgreen.erp.product.dto.response.ProductPriceResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductPriceService {
    
    List<ProductPriceResponse> getProductPrices(Long productId);
    
    ProductPriceResponse getDefaultPrice(Long productId);
    
    ProductPriceResponse createPrice(ProductPriceResponse dto);
    
    ProductPriceResponse updatePrice(Long id, ProductPriceResponse dto);
    
    void deletePrice(Long id);
    
    ProductPriceResponse setAsDefault(Long productId, Long priceId);
    
    void bulkUpdatePrices(BulkPriceUpdateResponse dto);
    
    Page<ProductPriceHistoryResponse> getPriceHistory(Long productId, Pageable pageable);
}
