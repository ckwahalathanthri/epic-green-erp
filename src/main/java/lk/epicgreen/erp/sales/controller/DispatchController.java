package lk.epicgreen.erp.sales.controller;


import lk.epicgreen.erp.sales.dto.response.DispatchDTO;
import lk.epicgreen.erp.sales.service.impl.DispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fulfillment/dispatches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DispatchController {
    
    private final DispatchService dispatchService;
    
    @GetMapping
    public ResponseEntity<List<DispatchDTO>> getAllDispatches() {
        return ResponseEntity.ok(dispatchService.getAllDispatches());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DispatchDTO> getDispatchById(@PathVariable Long id) {
        return ResponseEntity.ok(dispatchService.getDispatchById(id));
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<DispatchDTO>> getDispatchesByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(dispatchService.getDispatchesByOrder(orderId));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DispatchDTO>> getDispatchesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(dispatchService.getDispatchesByStatus(status));
    }
    
    @PostMapping
    public ResponseEntity<DispatchDTO> createDispatch(@RequestBody DispatchDTO dispatchDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dispatchService.createDispatch(dispatchDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DispatchDTO> updateDispatch(@PathVariable Long id, @RequestBody DispatchDTO dispatchDTO) {
        return ResponseEntity.ok(dispatchService.updateDispatch(id, dispatchDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispatch(@PathVariable Long id) {
        dispatchService.deleteDispatch(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/confirm")
    public ResponseEntity<DispatchDTO> confirmDispatch(@PathVariable Long id) {
        return ResponseEntity.ok(dispatchService.confirmDispatch(id));
    }
    
    @PostMapping("/{id}/mark-in-transit")
    public ResponseEntity<DispatchDTO> markInTransit(@PathVariable Long id) {
        return ResponseEntity.ok(dispatchService.markInTransit(id));
    }
    
    @PostMapping("/{id}/mark-delivered")
    public ResponseEntity<DispatchDTO> markDelivered(@PathVariable Long id, @RequestParam String deliverer) {
        return ResponseEntity.ok(dispatchService.markDelivered(id, deliverer));
    }
}