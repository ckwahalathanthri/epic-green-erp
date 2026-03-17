package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.dto.response.StockAdjustmentDTO;
import lk.epicgreen.erp.warehouse.entity.StockAdjustment;
import lk.epicgreen.erp.warehouse.entity.StockAdjustmentItem;
import lk.epicgreen.erp.warehouse.repository.StockAdjustmentRepository;
import lk.epicgreen.erp.warehouse.service.impl.StockAdjustmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/adjustments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockAdjustmentController {
    private final StockAdjustmentService service;
    private final StockAdjustmentRepository repository;

    @GetMapping
    public ResponseEntity<List<StockAdjustmentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

//    @PostMapping
//    public ResponseEntity<StockAdjustmentDTO> create(@RequestBody StockAdjustmentDTO dto) {
//        return ResponseEntity.ok(service.create(dto));
//    }



    @GetMapping("/{id}")
    public ResponseEntity<StockAdjustment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(repository.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<StockAdjustment> create(@RequestBody StockAdjustment adjustment) {
        return ResponseEntity.ok(repository.save(adjustment));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<StockAdjustment> approve(@PathVariable Long id) {
        return ResponseEntity.ok(service.approve(id));
    }

    @PostMapping("/{id}/post")
    public ResponseEntity<StockAdjustment> post(@PathVariable Long id) {
        return ResponseEntity.ok(service.post(id));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<StockAdjustmentItem>> getItems(@PathVariable Long id) {
        return ResponseEntity.ok(service.getItems(id));
    }



}