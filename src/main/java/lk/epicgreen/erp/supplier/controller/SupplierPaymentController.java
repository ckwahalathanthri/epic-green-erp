package lk.epicgreen.erp.supplier.controller;


import lk.epicgreen.erp.supplier.dto.response.SupplierPaymentDTO;
import lk.epicgreen.erp.supplier.service.impl.SupplierPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierPaymentController {
    private final SupplierPaymentService service;
    
    @GetMapping
    public ResponseEntity<List<SupplierPaymentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SupplierPaymentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}