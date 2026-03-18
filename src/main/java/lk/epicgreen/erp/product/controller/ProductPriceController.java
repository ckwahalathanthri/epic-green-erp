package lk.epicgreen.erp.product.controller;

import lk.epicgreen.erp.product.dto.response.ProductPriceResponse;
import lk.epicgreen.erp.product.dto.response.BulkPriceUpdateResponse;
import lk.epicgreen.erp.product.dto.response.ProductPriceHistoryResponse;
import lk.epicgreen.erp.product.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products/prices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductPriceController {
    
    private final ProductPriceService priceService;
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductPriceResponse>> getProductPrices(@PathVariable Long productId) {
        List<ProductPriceResponse> prices = priceService.getProductPrices(productId);
        return ResponseEntity.ok(prices);
    }
    
    @GetMapping("/product/{productId}/default")
    public ResponseEntity<ProductPriceResponse> getDefaultPrice(@PathVariable Long productId) {
        ProductPriceResponse price = priceService.getDefaultPrice(productId);
        return ResponseEntity.ok(price);
    }
    
    @PostMapping
    public ResponseEntity<ProductPriceResponse> createPrice(@Valid @RequestBody ProductPriceResponse dto) {
        ProductPriceResponse created = priceService.createPrice(dto);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductPriceResponse> updatePrice(
            @PathVariable Long id,
            @Valid @RequestBody ProductPriceResponse dto) {
        ProductPriceResponse updated = priceService.updatePrice(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/product/{productId}/set-default/{priceId}")
    public ResponseEntity<ProductPriceResponse> setAsDefault(
            @PathVariable Long productId,
            @PathVariable Long priceId) {
        ProductPriceResponse price = priceService.setAsDefault(productId, priceId);
        return ResponseEntity.ok(price);
    }
    
    @PostMapping("/bulk-update")
    public ResponseEntity<Void> bulkUpdatePrices(@Valid @RequestBody BulkPriceUpdateResponse dto) {
        priceService.bulkUpdatePrices(dto);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/history/{productId}")
    public ResponseEntity<Page<ProductPriceHistoryResponse>> getPriceHistory(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ProductPriceHistoryResponse> history = priceService.getPriceHistory(
            productId, PageRequest.of(page, size));
        return ResponseEntity.ok(history);
    }
}
