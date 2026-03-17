package lk.epicgreen.erp.sales.controller;


import lk.epicgreen.erp.payment.entity.SalesReturn;
import lk.epicgreen.erp.sales.service.impl.SalesReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoice/returns")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalesReturnController {
    
    private final SalesReturnService returnService;
    
    @GetMapping
    public ResponseEntity<List<SalesReturn>> getAllReturns() {
        return ResponseEntity.ok(returnService.getAllReturns());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SalesReturn> getReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(returnService.getReturnById(id));
    }
    
    @PostMapping
    public ResponseEntity<SalesReturn> createReturn(@RequestBody SalesReturn salesReturn) {
        return ResponseEntity.status(HttpStatus.CREATED).body(returnService.createReturn(salesReturn));
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<SalesReturn> approveReturn(@PathVariable Long id, @RequestParam String approver) {
        return ResponseEntity.ok(returnService.approveReturn(id, approver));
    }
}