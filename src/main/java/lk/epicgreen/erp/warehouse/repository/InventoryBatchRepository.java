package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.InventoryBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {
    List<InventoryBatch> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}