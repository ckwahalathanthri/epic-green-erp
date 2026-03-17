package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.dto.response.WarehouseTransferDTO;
import lk.epicgreen.erp.warehouse.service.impl.WarehouseTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WarehouseTransferController {
    private final WarehouseTransferService service;
    
    @GetMapping
    public ResponseEntity<List<WarehouseTransferDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseTransferDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    
    @PostMapping
    public ResponseEntity<WarehouseTransferDTO> create(@RequestBody WarehouseTransferDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<WarehouseTransferDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(service.approve(id));
    }
}