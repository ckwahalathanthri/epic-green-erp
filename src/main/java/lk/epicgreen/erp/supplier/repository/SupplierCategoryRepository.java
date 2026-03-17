package lk.epicgreen.erp.supplier.repository;
import lk.epicgreen.erp.supplier.entity.SupplierCategory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SupplierCategoryRepository extends JpaRepository<SupplierCategory, Long> {
    Optional<SupplierCategory> findByCategoryCode(String categoryCode);
    boolean existsByCategoryCode(String categoryCode);
    List<SupplierCategory> findByIsActive(Boolean isActive);
    Page<SupplierCategory> findByIsActive(Boolean isActive, Pageable pageable);
}
