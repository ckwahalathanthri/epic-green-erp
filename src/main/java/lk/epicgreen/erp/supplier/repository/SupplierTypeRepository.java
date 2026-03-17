package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SupplierTypeRepository extends JpaRepository<SupplierType, Long> {
    Optional<SupplierType> findByTypeCode(String typeCode);
    boolean existsByTypeCode(String typeCode);
    List<SupplierType> findByIsActive(Boolean isActive);
    Page<SupplierType> findByIsActive(Boolean isActive, Pageable pageable);
}
