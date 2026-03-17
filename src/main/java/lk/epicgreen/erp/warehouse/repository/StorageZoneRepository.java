package lk.epicgreen.erp.warehouse.repository;
import lk.epicgreen.erp.warehouse.entity.StorageZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StorageZoneRepository extends JpaRepository<StorageZone, Long> {
    List<StorageZone> findByWarehouseId(Long warehouseId);
}