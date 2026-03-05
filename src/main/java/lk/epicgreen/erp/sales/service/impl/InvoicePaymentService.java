package lk.epicgreen.erp.sales.service.impl;

import lk.epicgreen.erp.sales.entity.InvoicePayment;
import lk.epicgreen.erp.sales.repository.InvoicePaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoicePaymentService {
    
    private final InvoicePaymentRepository paymentRepository;
    
    public List<InvoicePayment> getPaymentsByInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }
    
    public InvoicePayment createPayment(InvoicePayment payment) {
        if (payment.getPaymentNumber() == null) {
            payment.setPaymentNumber(generatePaymentNumber());
        }
        return paymentRepository.save(payment);
    }
    
    private String generatePaymentNumber() {
        long count = paymentRepository.count() + 1;
        return String.format("PAY-%s-%04d", LocalDate.now().getYear(), count);
    }
}