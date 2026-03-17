package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SupplierBankAccountRepository extends JpaRepository<SupplierBankAccount, Long> {
    List<SupplierBankAccount> findBySupplierId(Long supplierId);
    Optional<SupplierBankAccount> findBySupplierIdAndIsPrimary(Long supplierId, Boolean isPrimary);
    List<SupplierBankAccount> findBySupplierIdAndIsActive(Long supplierId, Boolean isActive);
}
