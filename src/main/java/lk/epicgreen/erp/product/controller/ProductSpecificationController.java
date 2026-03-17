package lk.epicgreen.erp.product.controller;


import lk.epicgreen.erp.product.dto.response.ProductSpecificationDTO;
import lk.epicgreen.erp.product.service.ProductSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products/specifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductSpecificationController {
    
    private final ProductSpecificationService specService;
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductSpecificationDTO>> getProductSpecifications(
            @PathVariable Long productId) {
        List<ProductSpecificationDTO> specs = specService.getProductSpecifications(productId);
        return ResponseEntity.ok(specs);
    }
    
    @PostMapping
    public ResponseEntity<ProductSpecificationDTO> createSpecification(
            @RequestBody ProductSpecificationDTO dto) {
        ProductSpecificationDTO created = specService.createSpecification(dto);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductSpecificationDTO> updateSpecification(
            @PathVariable Long id,
            @RequestBody ProductSpecificationDTO dto) {
        ProductSpecificationDTO updated = specService.updateSpecification(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable Long id) {
        specService.deleteSpecification(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/product/{productId}/bulk")
    public ResponseEntity<Void> bulkCreateSpecifications(
            @PathVariable Long productId,
            @RequestBody List<ProductSpecificationDTO> specs) {
        specService.bulkCreateSpecifications(productId, specs);
        return ResponseEntity.ok().build();
    }
}
