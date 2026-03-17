package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByWarehouseId(Long warehouseId);
    List<Inventory> findByProductId(Long productId);
}