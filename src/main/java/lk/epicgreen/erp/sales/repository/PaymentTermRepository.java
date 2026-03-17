package lk.epicgreen.erp.sales.repository;


import lk.epicgreen.erp.sales.entity.PaymentTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTermRepository extends JpaRepository<PaymentTerm, Long> {
    Optional<PaymentTerm> findByTermCode(String termCode);
    List<PaymentTerm> findByIsActiveTrue();
}