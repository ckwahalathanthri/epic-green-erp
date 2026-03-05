package lk.epicgreen.erp.sales.repository;


import lk.epicgreen.erp.sales.entity.InvoicePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvoicePaymentRepository extends JpaRepository<InvoicePayment, Long> {
    List<InvoicePayment> findByInvoiceId(Long invoiceId);
    List<InvoicePayment> findByPaymentStatus(String status);
}