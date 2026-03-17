package lk.epicgreen.erp.customer.controller;


import lk.epicgreen.erp.customer.dto.response.CustomerCategoryDTO;
import lk.epicgreen.erp.customer.service.impl.CustomerCategoryService;
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
@RequestMapping("/api/customer-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerCategoryController {

    private final CustomerCategoryService customerCategoryService;

    @PostMapping
    public ResponseEntity<CustomerCategoryDTO> createCustomerCategory(
            @Valid @RequestBody CustomerCategoryDTO dto,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "system";
        CustomerCategoryDTO created = customerCategoryService.createCustomerCategory(dto, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerCategoryDTO> updateCustomerCategory(
            @PathVariable Long id,
            @Valid @RequestBody CustomerCategoryDTO dto,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "system";
        CustomerCategoryDTO updated = customerCategoryService.updateCustomerCategory(id, dto, username);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerCategoryDTO> getCustomerCategoryById(@PathVariable Long id) {
        CustomerCategoryDTO customerCategory = customerCategoryService.getCustomerCategoryById(id);
        return ResponseEntity.ok(customerCategory);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerCategoryDTO>> getAllCustomerCategories(Pageable pageable) {
        Page<CustomerCategoryDTO> customerCategories = customerCategoryService.getAllCustomerCategories(pageable);
        return ResponseEntity.ok(customerCategories);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerCategoryDTO>> getActiveCustomerCategories() {
        List<CustomerCategoryDTO> customerCategories = customerCategoryService.getActiveCustomerCategories();
        return ResponseEntity.ok(customerCategories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerCategory(@PathVariable Long id) {
        customerCategoryService.deleteCustomerCategory(id);
        return ResponseEntity.noContent().build();
    }
}
