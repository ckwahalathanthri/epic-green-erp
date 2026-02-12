package lk.epicgreen.erp.supplier.controller;


import lk.epicgreen.erp.supplier.dto.response.SupplierTypeDTO;
import lk.epicgreen.erp.supplier.service.impl.SupplierTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierTypeController {
    private final SupplierTypeService service;

    @PostMapping
    public ResponseEntity<SupplierTypeDTO> create(@Valid @RequestBody SupplierTypeDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.createSupplierType(dto, username), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierTypeDTO> update(@PathVariable Long id, @Valid @RequestBody SupplierTypeDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.updateSupplierType(id, dto, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierTypeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSupplierTypeById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SupplierTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllSupplierTypes(PageRequest.of(page, size)));
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupplierTypeDTO>> getActive() {
        return ResponseEntity.ok(service.getActiveSupplierTypes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteSupplierType(id);
        return ResponseEntity.noContent().build();
    }
}
