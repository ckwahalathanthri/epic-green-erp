package lk.epicgreen.erp.product.controller;


import lk.epicgreen.erp.product.dto.response.ProductDocumentDTO;
import lk.epicgreen.erp.product.dto.response.ProductImageDTO;
import lk.epicgreen.erp.product.service.ProductMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products/media")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductMediaController {
    
    private final ProductMediaService mediaService;
    
    @GetMapping("/images/{productId}")
    public ResponseEntity<List<ProductImageDTO>> getProductImages(@PathVariable Long productId) {
        List<ProductImageDTO> images = mediaService.getProductImages(productId);
        return ResponseEntity.ok(images);
    }
    
    @PostMapping("/images")
    public ResponseEntity<ProductImageDTO> uploadImage(@RequestBody ProductImageDTO dto) {
        ProductImageDTO uploaded = mediaService.uploadImage(dto);
        return ResponseEntity.ok(uploaded);
    }
    
    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        mediaService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/images/{productId}/set-primary/{imageId}")
    public ResponseEntity<ProductImageDTO> setPrimaryImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        ProductImageDTO image = mediaService.setPrimaryImage(productId, imageId);
        return ResponseEntity.ok(image);
    }
    
    @GetMapping("/documents/{productId}")
    public ResponseEntity<List<ProductDocumentDTO>> getProductDocuments(@PathVariable Long productId) {
        List<ProductDocumentDTO> documents = mediaService.getProductDocuments(productId);
        return ResponseEntity.ok(documents);
    }
    
    @PostMapping("/documents")
    public ResponseEntity<ProductDocumentDTO> uploadDocument(@RequestBody ProductDocumentDTO dto) {
        ProductDocumentDTO uploaded = mediaService.uploadDocument(dto);
        return ResponseEntity.ok(uploaded);
    }
    
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        mediaService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
