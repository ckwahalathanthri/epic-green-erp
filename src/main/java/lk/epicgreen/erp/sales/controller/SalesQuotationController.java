package lk.epicgreen.erp.sales.controller;


import lk.epicgreen.erp.sales.dto.response.SalesQuotationDTO;
import lk.epicgreen.erp.sales.service.impl.SalesQuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales/quotations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalesQuotationController {
    
    private final SalesQuotationService quotationService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<SalesQuotationDTO>> getAllQuotations() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<SalesQuotationDTO> getQuotationById(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.getQuotationById(id));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<SalesQuotationDTO>> getQuotationsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(quotationService.getQuotationsByCustomer(customerId));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<SalesQuotationDTO>> getQuotationsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(quotationService.getQuotationsByStatus(status));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<SalesQuotationDTO> createQuotation(@RequestBody SalesQuotationDTO quotationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quotationService.createQuotation(quotationDTO));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<SalesQuotationDTO> updateQuotation(@PathVariable Long id, @RequestBody SalesQuotationDTO quotationDTO) {
        return ResponseEntity.ok(quotationService.updateQuotation(id, quotationDTO));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<Void> deleteQuotation(@PathVariable Long id) {
        quotationService.deleteQuotation(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<SalesQuotationDTO> approveQuotation(@PathVariable Long id, @RequestParam String approver) {
        return ResponseEntity.ok(quotationService.approveQuotation(id, approver));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<SalesQuotationDTO> rejectQuotation(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.rejectQuotation(id));
    }
    
    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<SalesQuotationDTO>> getExpiredQuotations() {
        return ResponseEntity.ok(quotationService.getExpiredQuotations());
    }
}