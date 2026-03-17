package lk.epicgreen.erp.sales.controller;


import lk.epicgreen.erp.sales.dto.response.PickingListDTO;
import lk.epicgreen.erp.sales.service.impl.PickingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fulfillment/picking-lists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PickingListController {
    
    private final PickingListService pickingListService;
    
    @GetMapping
    public ResponseEntity<List<PickingListDTO>> getAllPickingLists() {
        return ResponseEntity.ok(pickingListService.getAllPickingLists());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PickingListDTO> getPickingListById(@PathVariable Long id) {
        return ResponseEntity.ok(pickingListService.getPickingListById(id));
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PickingListDTO>> getPickingListsByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(pickingListService.getPickingListsByOrder(orderId));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PickingListDTO>> getPickingListsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(pickingListService.getPickingListsByStatus(status));
    }
    
    @PostMapping
    public ResponseEntity<PickingListDTO> createPickingList(@RequestBody PickingListDTO pickingListDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pickingListService.createPickingList(pickingListDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PickingListDTO> updatePickingList(@PathVariable Long id, @RequestBody PickingListDTO pickingListDTO) {
        return ResponseEntity.ok(pickingListService.updatePickingList(id, pickingListDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePickingList(@PathVariable Long id) {
        pickingListService.deletePickingList(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/start")
    public ResponseEntity<PickingListDTO> startPicking(@PathVariable Long id, @RequestParam String picker) {
        return ResponseEntity.ok(pickingListService.startPicking(id, picker));
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<PickingListDTO> completePicking(@PathVariable Long id) {
        return ResponseEntity.ok(pickingListService.completePicking(id));
    }
}