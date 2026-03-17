package lk.epicgreen.erp.production.controller;


import lk.epicgreen.erp.production.service.impl.QualityInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production/quality-inspections")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QualityInspectionController {
    private final QualityInspectionService service;
    
    @GetMapping
    public ResponseEntity<List> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getByProductionOrder(orderId));
    }
}