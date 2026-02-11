package lk.epicgreen.erp.credit.controller;


import lk.epicgreen.erp.credit.controller.dto.request.response.CustomerGroupDTO;
import lk.epicgreen.erp.credit.controller.service.CustomerGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customer-groups")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerGroupController {
    private final CustomerGroupService service;

    @PostMapping
    public ResponseEntity<CustomerGroupDTO> create(@Valid @RequestBody CustomerGroupDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.create(dto, username), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerGroupDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerGroupDTO dto, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.update(id, dto, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerGroupDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerGroupDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerGroupDTO>> getActive() {
        return ResponseEntity.ok(service.getActive());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
