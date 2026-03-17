package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupplierStatementRepository extends JpaRepository<SupplierStatement, Long> {
    List<SupplierStatement> findBySupplierId(Long supplierId);
}
