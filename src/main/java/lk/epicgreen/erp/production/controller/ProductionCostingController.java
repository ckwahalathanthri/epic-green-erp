package lk.epicgreen.erp.production.controller;


import lk.epicgreen.erp.production.service.impl.ProductionCostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production/costing")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductionCostingController {
    private final ProductionCostingService service;
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Object> getCostByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getCostByOrder(orderId));
    }
}