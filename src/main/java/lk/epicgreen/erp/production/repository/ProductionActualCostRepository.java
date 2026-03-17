package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionActualCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductionActualCostRepository extends JpaRepository<ProductionActualCost, Long> {
    Optional<ProductionActualCost> findByProductionOrderId(Long productionOrderId);
}