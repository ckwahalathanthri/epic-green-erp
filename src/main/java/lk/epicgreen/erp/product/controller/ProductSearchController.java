package lk.epicgreen.erp.product.controller;


import lk.epicgreen.erp.product.dto.response.ProductListDTO;
import lk.epicgreen.erp.product.dto.response.ProductSearchDTO;
import lk.epicgreen.erp.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductSearchController {
    
    private final ProductSearchService searchService;
    
    @PostMapping("/advanced")
    public ResponseEntity<Page<ProductListDTO>> advancedSearch(
            @RequestBody ProductSearchDTO searchDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ProductListDTO> results = searchService.advancedSearch(
            searchDTO, PageRequest.of(page, size));
        return ResponseEntity.ok(results);
    }
}
