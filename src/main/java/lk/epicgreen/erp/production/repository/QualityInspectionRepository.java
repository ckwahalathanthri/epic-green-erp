package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.QualityInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QualityInspectionRepository extends JpaRepository<QualityInspection, Long> {
    List<QualityInspection> findByProductionOrderId(Long productionOrderId);
    List<QualityInspection> findByInspectionStatus(String status);
}