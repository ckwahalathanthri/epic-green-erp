package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.PhysicalInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhysicalInventoryRepository extends JpaRepository<PhysicalInventory, Long> {
    Optional<PhysicalInventory> findByCountNumber(String countNumber);
    List<PhysicalInventory> findByWarehouseId(Long warehouseId);
    List<PhysicalInventory> findByCountStatus(String status);
    List<PhysicalInventory> findByCountType(String countType);
}
