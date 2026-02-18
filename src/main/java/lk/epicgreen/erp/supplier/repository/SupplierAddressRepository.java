package lk.epicgreen.erp.supplier.repository;
import lk.epicgreen.erp.supplier.entity.SupplierAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SupplierAddressRepository extends JpaRepository<SupplierAddress, Long> {
    List<SupplierAddress> findBySupplierId(Long supplierId);
    Optional<SupplierAddress> findBySupplierIdAndIsDefault(Long supplierId, Boolean isDefault);
    List<SupplierAddress> findBySupplierIdAndAddressType(Long supplierId, String addressType);
    List<SupplierAddress> findBySupplierIdAndIsActive(Long supplierId, Boolean isActive);
}
