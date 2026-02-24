package lk.epicgreen.erp.warehouse.controller;
import com.epicgreen.erp.warehouse.dto.InventoryItemDTO;
import lk.epicgreen.erp.warehouse.service.impl.InventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/inventory-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InventoryItemController {
    private final InventoryItemService service;
    
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryItemDTO>> getByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(service.getByWarehouse(warehouseId));
    }
    
    @PostMapping
    public ResponseEntity<InventoryItemDTO> create(@RequestBody InventoryItemDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}