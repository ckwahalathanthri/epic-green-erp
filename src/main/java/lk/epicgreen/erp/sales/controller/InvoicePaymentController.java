package lk.epicgreen.erp.sales.controller;


import lk.epicgreen.erp.sales.entity.InvoicePayment;
import lk.epicgreen.erp.sales.service.impl.InvoicePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoice/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InvoicePaymentController {
    
    private final InvoicePaymentService paymentService;
    
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<InvoicePayment>> getPaymentsByInvoice(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(paymentService.getPaymentsByInvoice(invoiceId));
    }
    
    @PostMapping
    public ResponseEntity<InvoicePayment> createPayment(@RequestBody InvoicePayment payment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(payment));
    }
}