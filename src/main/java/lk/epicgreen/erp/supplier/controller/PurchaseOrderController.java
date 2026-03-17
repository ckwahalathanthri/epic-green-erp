package lk.epicgreen.erp.supplier.controller;

import lk.epicgreen.erp.supplier.dto.request.PurchaseOrderRequest;
import lk.epicgreen.erp.supplier.dto.response.PurchaseOrderDTO;
import lk.epicgreen.erp.supplier.entity.PurchaseOrder;
import lk.epicgreen.erp.supplier.service.impl.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseOrderController {
    private final PurchaseOrderService service;
    
    @GetMapping
    public ResponseEntity<List<PurchaseOrderDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    @GetMapping("/pending")
    public ResponseEntity<List<PurchaseOrderDTO>> getPoPending(){
        return ResponseEntity.ok(service.getPoPending());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderRequest purchaseOrderRequest){
        System.out.println("The data is "+purchaseOrderRequest.getDeliveryAddress());
        return ResponseEntity.ok(service.createPurchaseOrder(purchaseOrderRequest));
    }
}