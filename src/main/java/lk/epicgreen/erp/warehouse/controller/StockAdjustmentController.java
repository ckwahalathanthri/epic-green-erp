package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.dto.response.StockAdjustmentDTO;
import lk.epicgreen.erp.warehouse.service.impl.StockAdjustmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/adjustments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockAdjustmentController {
    private final StockAdjustmentService service;
    
    @GetMapping
    public ResponseEntity<List<StockAdjustmentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @PostMapping
    public ResponseEntity<StockAdjustmentDTO> create(@RequestBody StockAdjustmentDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<StockAdjustmentDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(service.approve(id));
    }
}