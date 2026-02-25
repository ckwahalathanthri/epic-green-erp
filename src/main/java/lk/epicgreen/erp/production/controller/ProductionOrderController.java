package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.production.dto.response.ProductionOrderDTO;
import lk.epicgreen.erp.production.service.impl.ProductionOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductionOrderController {
    private final ProductionOrderService service;
    
    @GetMapping
    public ResponseEntity<List> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductionOrderDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}