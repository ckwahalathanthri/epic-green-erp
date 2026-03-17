package lk.epicgreen.erp.credit.controller;


import lk.epicgreen.erp.credit.controller.dto.request.response.PaymentTermsDTO;
import lk.epicgreen.erp.credit.controller.service.PaymentTermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentTermsController {
    private final PaymentTermsService service;

    @PostMapping
    public ResponseEntity<PaymentTermsDTO> create(@Valid @RequestBody PaymentTermsDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.create(dto, username), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentTermsDTO> update(@PathVariable Long id, @Valid @RequestBody PaymentTermsDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.update(id, dto, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTermsDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentTermsDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<PaymentTermsDTO>> getActive() {
        return ResponseEntity.ok(service.getActive());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
