package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.entity.InventoryAlert;
import lk.epicgreen.erp.warehouse.service.InventoryAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/alerts")
@RequiredArgsConstructor
public class InventoryAlertController {
    private final InventoryAlertService service;
    
    @GetMapping
    public ResponseEntity<List<InventoryAlert>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InventoryAlert>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }
    
    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<InventoryAlert> acknowledge(
        @PathVariable Long id,
        @RequestParam String acknowledgedBy
    ) {
        return ResponseEntity.ok(service.acknowledge(id, acknowledgedBy));
    }
    
    @PostMapping("/{id}/resolve")
    public ResponseEntity<InventoryAlert> resolve(
        @PathVariable Long id,
        @RequestParam String resolvedBy,
        @RequestParam String notes
    ) {
        return ResponseEntity.ok(service.resolve(id, resolvedBy, notes));
    }
}
