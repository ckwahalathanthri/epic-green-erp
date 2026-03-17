package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.entity.PhysicalInventory;
import lk.epicgreen.erp.warehouse.entity.PhysicalInventoryLine;
import lk.epicgreen.erp.warehouse.service.PhysicalInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/physical-counts")
@RequiredArgsConstructor
public class PhysicalInventoryController {
    private final PhysicalInventoryService service;
    
    @GetMapping
    public ResponseEntity<List<PhysicalInventory>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PhysicalInventory> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<PhysicalInventory> create(@RequestBody PhysicalInventory inventory) {
        return ResponseEntity.ok(service.create(inventory));
    }
    
    @PostMapping("/{id}/start")
    public ResponseEntity<PhysicalInventory> start(@PathVariable Long id) {
        return ResponseEntity.ok(service.start(id));
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<PhysicalInventory> complete(@PathVariable Long id) {
        return ResponseEntity.ok(service.complete(id));
    }
    
    @PostMapping("/{id}/post")
    public ResponseEntity<PhysicalInventory> post(@PathVariable Long id) {
        return ResponseEntity.ok(service.post(id));
    }
    
    @GetMapping("/{id}/lines")
    public ResponseEntity<List<PhysicalInventoryLine>> getLines(@PathVariable Long id) {
        return ResponseEntity.ok(service.getLines(id));
    }
}
