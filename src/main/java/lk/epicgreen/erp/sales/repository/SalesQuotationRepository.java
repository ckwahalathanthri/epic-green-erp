package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.SalesQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesQuotationRepository extends JpaRepository<SalesQuotation, Long> {
    
    Optional<SalesQuotation> findByQuotationNumber(String quotationNumber);
    
    List<SalesQuotation> findByCustomerId(Long customerId);
    
    List<SalesQuotation> findByQuotationStatus(String status);
    
    List<SalesQuotation> findByQuotationDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT q FROM SalesQuotation q WHERE q.quotationStatus = :status AND q.validUntil < :date")
    List<SalesQuotation> findExpiredQuotations(String status, LocalDate date);
    
    @Query("SELECT q FROM SalesQuotation q WHERE q.customerId = :customerId AND q.quotationStatus = :status")
    List<SalesQuotation> findByCustomerIdAndStatus(Long customerId, String status);
    
    @Query("SELECT COUNT(q) FROM SalesQuotation q WHERE q.quotationStatus = :status")
    Long countByStatus(String status);
}