package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.entity.StockValuation;
import lk.epicgreen.erp.warehouse.service.StockValuationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/valuations")
@RequiredArgsConstructor
public class StockValuationController {
    private final StockValuationService service;
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<StockValuation>> getByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(service.findByDate(date));
    }
}