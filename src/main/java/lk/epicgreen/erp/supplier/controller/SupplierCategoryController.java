package lk.epicgreen.erp.supplier.controller;

import lk.epicgreen.erp.supplier.dto.response.SupplierCategoryDTO;
import lk.epicgreen.erp.supplier.service.impl.SupplierCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierCategoryController {
    private final SupplierCategoryService service;

    @PostMapping
    public ResponseEntity<SupplierCategoryDTO> create(@Valid @RequestBody SupplierCategoryDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.createSupplierCategory(dto, username), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierCategoryDTO> update(@PathVariable Long id, @Valid @RequestBody SupplierCategoryDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.updateSupplierCategory(id, dto, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierCategoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSupplierCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SupplierCategoryDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllSupplierCategories(PageRequest.of(page, size)));
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupplierCategoryDTO>> getActive() {
        return ResponseEntity.ok(service.getActiveSupplierCategories());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteSupplierCategory(id);
        return ResponseEntity.noContent().build();
    }
}
