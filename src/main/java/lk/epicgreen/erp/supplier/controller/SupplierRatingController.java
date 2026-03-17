package lk.epicgreen.erp.supplier.controller;


import lk.epicgreen.erp.supplier.dto.response.SupplierRatingDTO;
import lk.epicgreen.erp.supplier.service.impl.SupplierRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-ratings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierRatingController {
    private final SupplierRatingService service;

    @PostMapping
    public ResponseEntity<SupplierRatingDTO> create(@Valid @RequestBody SupplierRatingDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.create(dto, username), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierRatingDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierRatingDTO>> getBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.getBySupplierId(supplierId));
    }

    @GetMapping("/supplier/{supplierId}/latest")
    public ResponseEntity<SupplierRatingDTO> getLatestBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.getLatestBySupplierId(supplierId));
    }

    @GetMapping("/preferred")
    public ResponseEntity<List<SupplierRatingDTO>> getPreferredSuppliers() {
        return ResponseEntity.ok(service.getPreferredSuppliers());
    }
}
