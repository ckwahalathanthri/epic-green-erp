package lk.epicgreen.erp.supplier.controller;


import lk.epicgreen.erp.supplier.dto.response.SupplierPaymentTermsDTO;
import lk.epicgreen.erp.supplier.service.impl.SupplierPaymentTermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-payment-terms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierPaymentTermsController {
    private final SupplierPaymentTermsService service;

    @PostMapping
    public ResponseEntity<SupplierPaymentTermsDTO> create(@Valid @RequestBody SupplierPaymentTermsDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.create(dto, username), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierPaymentTermsDTO> update(@PathVariable Long id, @Valid @RequestBody SupplierPaymentTermsDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.update(id, dto, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierPaymentTermsDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupplierPaymentTermsDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupplierPaymentTermsDTO>> getActive() {
        return ResponseEntity.ok(service.getActive());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
