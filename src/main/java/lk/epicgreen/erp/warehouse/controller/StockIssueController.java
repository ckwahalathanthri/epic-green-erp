package lk.epicgreen.erp.warehouse.controller;

import lk.epicgreen.erp.warehouse.dto.response.StockIssueDTO;
import lk.epicgreen.erp.warehouse.service.impl.StockIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/stock-issues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockIssueController {
    private final StockIssueService stockIssueService;
    
    @GetMapping
    public ResponseEntity<List<StockIssueDTO>> getAll() {
        return ResponseEntity.ok(stockIssueService.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StockIssueDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockIssueService.getById(id));
    }
    
    @PostMapping
    public ResponseEntity<StockIssueDTO> create(@RequestBody StockIssueDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockIssueService.create(dto));
    }
    
    @PostMapping("/{id}/process")
    public ResponseEntity<Void> processIssue(@PathVariable Long id) {
        stockIssueService.processIssue(id);
        return ResponseEntity.ok().build();
    }
}
