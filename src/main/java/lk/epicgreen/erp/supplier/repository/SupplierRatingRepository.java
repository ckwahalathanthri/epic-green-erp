package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SupplierRatingRepository extends JpaRepository<SupplierRating, Long> {
    List<SupplierRating> findBySupplierId(Long supplierId);
    Optional<SupplierRating> findTopBySupplierIdOrderByRatingDateDesc(Long supplierId);
    List<SupplierRating> findByIsPreferredSupplier(Boolean isPreferredSupplier);
}
