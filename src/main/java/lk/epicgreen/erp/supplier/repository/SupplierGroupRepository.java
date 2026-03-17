package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SupplierGroupRepository extends JpaRepository<SupplierGroup, Long> {
    Optional<SupplierGroup> findByGroupCode(String groupCode);
    boolean existsByGroupCode(String groupCode);
    List<SupplierGroup> findByIsActive(Boolean isActive);
}
