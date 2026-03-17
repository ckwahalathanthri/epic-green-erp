package lk.epicgreen.erp.warehouse.repository;
import lk.epicgreen.erp.warehouse.entity.StockLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockLevelRepository extends JpaRepository<StockLevel, Long> {
    List<StockLevel> findByWarehouseId(Long warehouseId);
    
    @Query("SELECT s FROM StockLevel s WHERE s.warehouse.id = ?1 AND s.quantityAvailable <= s.reorderLevel")
    List<StockLevel> findLowStockItems(Long warehouseId);
}