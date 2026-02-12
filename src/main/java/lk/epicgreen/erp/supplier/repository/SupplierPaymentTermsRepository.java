package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierPaymentTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SupplierPaymentTermsRepository extends JpaRepository<SupplierPaymentTerms, Long> {
    Optional<SupplierPaymentTerms> findByTermsCode(String termsCode);
    boolean existsByTermsCode(String termsCode);
    List<SupplierPaymentTerms> findByIsActive(Boolean isActive);
}
