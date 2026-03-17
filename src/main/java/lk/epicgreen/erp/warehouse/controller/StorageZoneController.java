package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.service.impl.StorageZoneService;
import lk.epicgreen.erp.warehouse.service.impl.ZoneDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/storage-zones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StorageZoneController {
    private final StorageZoneService service;
    
    @GetMapping
    public ResponseEntity<List<ZoneDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ZoneDTO>> getByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(service.getByWarehouse(warehouseId));
    }
    
    @PostMapping
    public ResponseEntity<ZoneDTO> create(@RequestBody ZoneDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}