package lk.epicgreen.erp.sales.service.impl;

import lk.epicgreen.erp.sales.entity.SalesInvoice;
import lk.epicgreen.erp.sales.repository.SalesInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesInvoiceService {
    
    private final SalesInvoiceRepository invoiceRepository;
    
    public List<SalesInvoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    
    public SalesInvoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Invoice not found: " + id));
    }
    
    public SalesInvoice createInvoice(SalesInvoice invoice) {
        if (invoice.getInvoiceNumber() == null) {
            invoice.setInvoiceNumber(generateInvoiceNumber());
        }
        return invoiceRepository.save(invoice);
    }
    
    public SalesInvoice updateInvoice(Long id, SalesInvoice invoice) {
        SalesInvoice existing = getInvoiceById(id);
        // Update fields
        return invoiceRepository.save(existing);
    }
    
    public void sendInvoiceToCustomer(Long id) {
        SalesInvoice invoice = getInvoiceById(id);
        invoice.sendToCustomer();
        invoiceRepository.save(invoice);
    }
    
    private String generateInvoiceNumber() {
        long count = invoiceRepository.count() + 1;
        return String.format("INV-%s-%04d", LocalDate.now().getYear(), count);
    }
}