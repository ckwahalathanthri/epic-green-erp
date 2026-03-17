package lk.epicgreen.erp.credit.controller.repo;
import lk.epicgreen.erp.credit.controller.entity.PaymentTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentTermsRepository extends JpaRepository<PaymentTerms, Long> {
    Optional<PaymentTerms> findByTermsCode(String termsCode);
    boolean existsByTermsCode(String termsCode);
    List<PaymentTerms> findByIsActive(Boolean isActive);
}
