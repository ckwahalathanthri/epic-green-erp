package lk.epicgreen.erp.sales.repository;


import lk.epicgreen.erp.sales.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Long> {
    Optional<SalesInvoice> findByInvoiceNumber(String invoiceNumber);
    List<SalesInvoice> findByCustomerId(Long customerId);
    List<SalesInvoice> findByInvoiceStatus(String status);
    List<SalesInvoice> findByOrderId(Long orderId);
}