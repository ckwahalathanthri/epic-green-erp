package lk.epicgreen.erp.supplier.controller;


import lk.epicgreen.erp.supplier.dto.response.SupplierGroupDTO;
import lk.epicgreen.erp.supplier.service.impl.SupplierGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-groups")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierGroupController {
    private final SupplierGroupService service;

    @PostMapping
    public ResponseEntity<SupplierGroupDTO> create(@Valid @RequestBody SupplierGroupDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.create(dto, username), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierGroupDTO> update(@PathVariable Long id, @Valid @RequestBody SupplierGroupDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.update(id, dto, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierGroupDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupplierGroupDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupplierGroupDTO>> getActive() {
        return ResponseEntity.ok(service.getActive());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
