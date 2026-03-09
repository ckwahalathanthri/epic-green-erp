package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.ReorderPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReorderPointRepository extends JpaRepository<ReorderPoint, Long> {
    Optional<ReorderPoint> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    List<ReorderPoint> findByWarehouseId(Long warehouseId);
    List<ReorderPoint> findByStatus(String status);
    List<ReorderPoint> findByAutoReorderEnabled(Boolean enabled);
}
