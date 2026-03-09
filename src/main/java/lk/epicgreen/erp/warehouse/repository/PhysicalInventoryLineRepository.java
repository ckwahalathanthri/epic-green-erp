package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.PhysicalInventoryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhysicalInventoryLineRepository extends JpaRepository<PhysicalInventoryLine, Long> {
    List<PhysicalInventoryLine> findByPhysicalInventoryId(Long physicalInventoryId);
    List<PhysicalInventoryLine> findByCountStatus(String status);
}
