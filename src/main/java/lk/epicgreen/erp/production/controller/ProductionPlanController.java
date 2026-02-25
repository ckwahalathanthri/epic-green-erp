package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.production.dto.response.ProductionPlanDTO;
import lk.epicgreen.erp.production.service.ProductionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production/plans")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductionPlanController {
    private final ProductionPlanService service;
    
    @GetMapping
    public ResponseEntity<List<ProductionPlanDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductionPlanDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @PostMapping
    public ResponseEntity<ProductionPlanDTO> create(@RequestBody ProductionPlanDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}