package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductionBatchRepository extends JpaRepository<ProductionBatch, Long> {
    List<ProductionBatch> findByProductionOrderId(Long productionOrderId);
    List<ProductionBatch> findByBatchStatus(String status);
}