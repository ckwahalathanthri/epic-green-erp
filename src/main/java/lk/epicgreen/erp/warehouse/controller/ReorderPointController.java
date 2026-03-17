package lk.epicgreen.erp.warehouse.controller;

import lk.epicgreen.erp.warehouse.entity.ReorderPoint;
import lk.epicgreen.erp.warehouse.service.ReorderPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/reorder-points")
@RequiredArgsConstructor
public class ReorderPointController {
    private final ReorderPointService service;
    
    @GetMapping
    public ResponseEntity<List<ReorderPoint>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReorderPoint> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<ReorderPoint> create(@RequestBody ReorderPoint reorderPoint) {
        return ResponseEntity.ok(service.create(reorderPoint));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ReorderPoint> update(@PathVariable Long id, @RequestBody ReorderPoint reorderPoint) {
        return ResponseEntity.ok(service.update(id, reorderPoint));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
