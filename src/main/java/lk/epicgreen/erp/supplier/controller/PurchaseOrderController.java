package lk.epicgreen.erp.supplier.controller;

import lk.epicgreen.erp.supplier.dto.request.PurchaseOrderRequest;
import lk.epicgreen.erp.supplier.dto.response.PurchaseOrderDTO;
import lk.epicgreen.erp.supplier.entity.PurchaseOrder;
import lk.epicgreen.erp.supplier.service.impl.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/draft")
    public ResponseEntity<List<PurchaseOrderDTO>> getPoDraft(){
        return ResponseEntity.ok(service.getPoDraft());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderRequest purchaseOrderRequest){
        System.out.println("The data is "+purchaseOrderRequest.getDeliveryAddress());
        return ResponseEntity.ok(service.createPurchaseOrder(purchaseOrderRequest));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseOrderDTO>> getOrserBySupplier(@PathVariable Long supplierId){
        return ResponseEntity.ok(service.getPoBySupplier(supplierId));
    }

    @GetMapping("/supplier/{supplierId}/status/received")
    public ResponseEntity<List<PurchaseOrderDTO>> getOrderBySupplierAndStatusReceived(@PathVariable Long supplierId){
        List<String> staatus= new ArrayList<>();
        staatus.add("RECEIVED");
        staatus.add("PARTIALLY_PAID");

        return ResponseEntity.ok(service.getPoBySupplierAndStatus(supplierId,staatus));
    }
}