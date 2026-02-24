package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.dto.response.BatchDTO;
import lk.epicgreen.erp.warehouse.service.impl.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/batches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BatchController {
    private final BatchService service;
    
    @GetMapping
    public ResponseEntity<List<BatchDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<BatchDTO>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getByProduct(productId));
    }
    
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<BatchDTO>> getExpiringSoon() {
        return ResponseEntity.ok(service.getExpiringSoon());
    }
}