package lk.epicgreen.erp.warehouse.repository;


import lk.epicgreen.erp.warehouse.entity.InventoryAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryAlertRepository extends JpaRepository<InventoryAlert, Long> {
    List<InventoryAlert> findByAlertStatus(String status);
    List<InventoryAlert> findByAlertType(String alertType);
    List<InventoryAlert> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    List<InventoryAlert> findByAlertSeverity(String severity);
}
