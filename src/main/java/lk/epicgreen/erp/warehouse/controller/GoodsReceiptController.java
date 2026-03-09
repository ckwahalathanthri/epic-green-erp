package lk.epicgreen.erp.warehouse.controller;

import lk.epicgreen.erp.warehouse.entity.GoodsReceipt;
import lk.epicgreen.erp.warehouse.service.impl.GoodsReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/receipts")
@RequiredArgsConstructor
public class GoodsReceiptController {
    private final GoodsReceiptService service;
    
    @GetMapping
    public ResponseEntity<List<GoodsReceipt>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceipt> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<GoodsReceipt> create(@RequestBody GoodsReceipt receipt) {
        return ResponseEntity.ok(service.create(receipt));
    }
    
    @PostMapping("/{id}/post")
    public ResponseEntity<GoodsReceipt> post(@PathVariable Long id) {
        return ResponseEntity.ok(service.post(id));
    }
}
