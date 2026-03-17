package lk.epicgreen.erp.sales.controller;


import lk.epicgreen.erp.sales.dto.response.PackingSlipDTO;
import lk.epicgreen.erp.sales.service.impl.PackingSlipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fulfillment/packing-slips")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PackingSlipController {
    
    private final PackingSlipService packingSlipService;
    
    @GetMapping
    public ResponseEntity<List<PackingSlipDTO>> getAllPackingSlips() {
        return ResponseEntity.ok(packingSlipService.getAllPackingSlips());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PackingSlipDTO> getPackingSlipById(@PathVariable Long id) {
        return ResponseEntity.ok(packingSlipService.getPackingSlipById(id));
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PackingSlipDTO>> getPackingSlipsByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(packingSlipService.getPackingSlipsByOrder(orderId));
    }
    
    @PostMapping
    public ResponseEntity<PackingSlipDTO> createPackingSlip(@RequestBody PackingSlipDTO packingSlipDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packingSlipService.createPackingSlip(packingSlipDTO));
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<PackingSlipDTO> completePacking(@PathVariable Long id) {
        return ResponseEntity.ok(packingSlipService.completePacking(id));
    }
}