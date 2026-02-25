package lk.epicgreen.erp.production.controller;


import lk.epicgreen.erp.production.service.QualityStandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production/quality-standards")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QualityStandardController {
    private final QualityStandardService service;
    
    @GetMapping
    public ResponseEntity<List> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getByProduct(productId));
    }
}