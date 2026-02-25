package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.dto.response.StockLevelDTO;
import lk.epicgreen.erp.warehouse.service.impl.StockLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/stock-levels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockLevelController {
    private final StockLevelService service;
    
    @GetMapping
    public ResponseEntity<List<StockLevelDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<StockLevelDTO>> getByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(service.getByWarehouse(warehouseId));
    }
    
    @GetMapping("/warehouse/{warehouseId}/low-stock")
    public ResponseEntity<List<StockLevelDTO>> getLowStock(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(service.getLowStock(warehouseId));
    }
}