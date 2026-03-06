package lk.epicgreen.erp.warehouse.controller;
import com.epicgreen.erp.warehouse.entity.GoodsIssue;
import com.epicgreen.erp.warehouse.service.GoodsIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/issues")
@RequiredArgsConstructor
public class GoodsIssueController {
    private final GoodsIssueService service;
    
    @GetMapping
    public ResponseEntity<List<GoodsIssue>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GoodsIssue> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<GoodsIssue> create(@RequestBody GoodsIssue issue) {
        return ResponseEntity.ok(service.create(issue));
    }
    
    @PostMapping("/{id}/post")
    public ResponseEntity<GoodsIssue> post(@PathVariable Long id) {
        return ResponseEntity.ok(service.post(id));
    }
}
