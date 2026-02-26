package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionYield;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductionYieldRepository extends JpaRepository<ProductionYield, Long> {
    List<ProductionYield> findByProductionOrderId(Long productionOrderId);
    List<ProductionYield> findByYieldCategory(String category);
}