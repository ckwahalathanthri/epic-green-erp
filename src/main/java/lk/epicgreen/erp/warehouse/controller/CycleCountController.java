package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.dto.response.CycleCountDTO;
import lk.epicgreen.erp.warehouse.service.impl.CycleCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/cycle-counts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CycleCountController {
    private final CycleCountService service;
    
    @GetMapping
    public ResponseEntity<List<CycleCountDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @PostMapping
    public ResponseEntity<CycleCountDTO> create(@RequestBody CycleCountDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<CycleCountDTO> complete(@PathVariable Long id) {
        return ResponseEntity.ok(service.complete(id));
    }
}