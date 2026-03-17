package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.entity.InventoryBatch;
import lk.epicgreen.erp.warehouse.service.InventoryBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/batches")
@RequiredArgsConstructor
public class InventoryBatchController {
    private final InventoryBatchService service;
    
    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryBatch>> getBatches(
        @PathVariable Long productId, 
        @PathVariable Long warehouseId
    ) {
        return ResponseEntity.ok(service.findByProductAndWarehouse(productId, warehouseId));
    }
}