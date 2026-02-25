package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.production.dto.response.ProductionRecipeDTO;
import lk.epicgreen.erp.production.service.ProductionRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductionRecipeController {
    private final ProductionRecipeService service;
    
    @GetMapping
    public ResponseEntity<List<ProductionRecipeDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductionRecipeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @PostMapping
    public ResponseEntity<ProductionRecipeDTO> create(@RequestBody ProductionRecipeDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}