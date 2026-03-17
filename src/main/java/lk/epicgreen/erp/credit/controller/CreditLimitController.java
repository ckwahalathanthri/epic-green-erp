package lk.epicgreen.erp.credit.controller;


import lk.epicgreen.erp.credit.controller.dto.request.response.CreditLimitDTO;
import lk.epicgreen.erp.credit.controller.service.CreditLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/credit-limits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CreditLimitController {
    private final CreditLimitService service;

    @PostMapping
    public ResponseEntity<CreditLimitDTO> create(@Valid @RequestBody CreditLimitDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.create(dto, username), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditLimitDTO> update(@PathVariable Long id, @Valid @RequestBody CreditLimitDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.update(id, dto, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditLimitDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CreditLimitDTO>> getByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getByCustomerId(customerId));
    }

    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<CreditLimitDTO> getActiveByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getActiveByCustomerId(customerId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<CreditLimitDTO> approve(@PathVariable Long id, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.approve(id, username));
    }
}
