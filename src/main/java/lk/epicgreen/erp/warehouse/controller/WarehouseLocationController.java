package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.dto.response.WarehouseLocationResponse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lk.epicgreen.erp.warehouse.service.impl.WarehouseLocationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/locations")
@RequiredArgsConstructor
public class WarehouseLocationController {
    private final WarehouseLocationServiceImpl service;
    
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<WarehouseLocationResponse>> getByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(service.findByWarehouseId(warehouseId));
    }
    
    @PostMapping
    public ResponseEntity<WarehouseLocation> create(@RequestBody WarehouseLocation location) {
        return ResponseEntity.ok(service.create(location));
    }
}