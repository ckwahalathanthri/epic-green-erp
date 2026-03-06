package lk.epicgreen.erp.warehouse.controller;
import com.epicgreen.erp.warehouse.entity.StockTransfer;
import com.epicgreen.erp.warehouse.entity.StockTransferItem;
import com.epicgreen.erp.warehouse.service.StockTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/transfers")
@RequiredArgsConstructor
public class StockTransferController {
    private final StockTransferService service;
    
    @GetMapping
    public ResponseEntity<List<StockTransfer>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StockTransfer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<StockTransfer> create(@RequestBody StockTransfer transfer) {
        return ResponseEntity.ok(service.create(transfer));
    }
    
    @PostMapping("/{id}/confirm")
    public ResponseEntity<StockTransfer> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirm(id));
    }
    
    @PostMapping("/{id}/ship")
    public ResponseEntity<StockTransfer> ship(@PathVariable Long id) {
        return ResponseEntity.ok(service.ship(id));
    }
    
    @PostMapping("/{id}/receive")
    public ResponseEntity<StockTransfer> receive(@PathVariable Long id) {
        return ResponseEntity.ok(service.receive(id));
    }
    
    @GetMapping("/{id}/items")
    public ResponseEntity<List<StockTransferItem>> getItems(@PathVariable Long id) {
        return ResponseEntity.ok(service.getItems(id));
    }
}
