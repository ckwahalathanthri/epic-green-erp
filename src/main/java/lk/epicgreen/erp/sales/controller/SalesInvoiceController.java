package lk.epicgreen.erp.sales.controller;


import lk.epicgreen.erp.sales.entity.SalesInvoice;
import lk.epicgreen.erp.sales.service.impl.SalesInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoice/invoices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalesInvoiceController {
    
    private final SalesInvoiceService invoiceService;
    
    @GetMapping
    public ResponseEntity<List<SalesInvoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SalesInvoice> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }
    
    @PostMapping
    public ResponseEntity<SalesInvoice> createInvoice(@RequestBody SalesInvoice invoice) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(invoice));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SalesInvoice> updateInvoice(@PathVariable Long id, @RequestBody SalesInvoice invoice) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, invoice));
    }
    
    @PostMapping("/{id}/send")
    public ResponseEntity<Void> sendInvoice(@PathVariable Long id) {
        invoiceService.sendInvoiceToCustomer(id);
        return ResponseEntity.ok().build();
    }
}