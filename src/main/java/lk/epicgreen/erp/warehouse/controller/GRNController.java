package lk.epicgreen.erp.warehouse.controller;

import lk.epicgreen.erp.warehouse.dto.response.GRNDTO;
import lk.epicgreen.erp.warehouse.service.impl.GRNService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/grn")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GRNController {
    private final GRNService grnService;
    
    @GetMapping
    public ResponseEntity<List<GRNDTO>> getAll() {
        return ResponseEntity.ok(grnService.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GRNDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(grnService.getById(id));
    }
    
    @PostMapping
    public ResponseEntity<GRNDTO> create(@RequestBody GRNDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grnService.create(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GRNDTO> update(@PathVariable Long id, @RequestBody GRNDTO dto) {
        return ResponseEntity.ok(grnService.update(id, dto));
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        grnService.approve(id);
        return ResponseEntity.ok().build();
    }
}
