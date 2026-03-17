package lk.epicgreen.erp.customer.controller;



import lk.epicgreen.erp.customer.dto.response.CustomerTypeDTO;
import lk.epicgreen.erp.customer.service.impl.CustomerTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customer-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerTypeController {

    private final CustomerTypeService customerTypeService;

    @PostMapping
    public ResponseEntity<CustomerTypeDTO> createCustomerType(
            @Valid @RequestBody CustomerTypeDTO dto,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "system";
        CustomerTypeDTO created = customerTypeService.createCustomerType(dto, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerTypeDTO> updateCustomerType(
            @PathVariable Long id,
            @Valid @RequestBody CustomerTypeDTO dto,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "system";
        CustomerTypeDTO updated = customerTypeService.updateCustomerType(id, dto, username);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerTypeDTO> getCustomerTypeById(@PathVariable Long id) {
        CustomerTypeDTO customerType = customerTypeService.getCustomerTypeById(id);
        return ResponseEntity.ok(customerType);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerTypeDTO>> getAllCustomerTypes(Pageable pageable) {
        Page<CustomerTypeDTO> customerTypes = customerTypeService.getAllCustomerTypes(pageable);
        return ResponseEntity.ok(customerTypes);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerTypeDTO>> getActiveCustomerTypes() {
        List<CustomerTypeDTO> customerTypes = customerTypeService.getActiveCustomerTypes();
        return ResponseEntity.ok(customerTypes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerType(@PathVariable Long id) {
        customerTypeService.deleteCustomerType(id);
        return ResponseEntity.noContent().build();
    }
}
