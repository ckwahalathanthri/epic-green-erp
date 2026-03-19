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
        if(invoice.getItems()!=null){
            invoice.getItems().forEach(item->item.setInvoice(invoice));
        }
        return invoiceRepository.save(invoice);
    }
    
    public SalesInvoice updateInvoice(Long id, SalesInvoice invoice) {
        SalesInvoice existing = getInvoiceById(id);
        existing.setCustomerId(invoice.getCustomerId());
        existing.setCustomerName(invoice.getCustomerName());
        existing.setCustomerEmail(invoice.getCustomerEmail());
        existing.setInvoiceDate(invoice.getInvoiceDate());
        existing.setDueDate(invoice.getDueDate());
        existing.setSubtotal(invoice.getSubtotal());
        existing.setTotalAmount(invoice.getTotalAmount());
        existing.setInvoiceDate(invoice.getInvoiceDate());
        existing.setInvoiceNumber(invoice.getInvoiceNumber());
        existing.setInvoiceType(invoice.getInvoiceType());
        existing.setInvoiceStatus(invoice.getInvoiceStatus());
        existing.setOrderId(invoice.getOrderId());
        existing.setPaymentTerms(invoice.getPaymentTerms());
        if(existing.getItems()!=null){
            existing.getItems().clear();

            if (invoice.getItems() != null) {
                invoice.getItems().forEach(item -> {
                    item.setInvoice(existing); // maintain relationship
                    existing.getItems().add(item); // add to existing list
                });
            }
        }
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