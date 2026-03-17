package lk.epicgreen.erp.production.controller;

import lk.epicgreen.erp.production.service.impl.ProductionAnalyticsService;
import lk.epicgreen.erp.production.service.impl.YieldAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductionAnalyticsController {
    private final ProductionAnalyticsService analyticsService;
    private final YieldAnalysisService yieldService;
    
    @GetMapping("/yields")
    public ResponseEntity<List> getYields() {
        return ResponseEntity.ok(yieldService.getAll());
    }
    
    @GetMapping("/yields/category/{category}")
    public ResponseEntity<List> getYieldsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(yieldService.getByCategory(category));
    }
}