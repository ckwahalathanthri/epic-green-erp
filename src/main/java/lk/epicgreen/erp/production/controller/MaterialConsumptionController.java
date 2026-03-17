package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.production.service.impl.MaterialConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production/material-consumption")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MaterialConsumptionController {
    private final MaterialConsumptionService service;
    
    @GetMapping
    public ResponseEntity<List> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getByProductionOrder(orderId));
    }
}