package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Inventory Repository
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    
    List<Inventory> findByProductId(Long productId);
    
    List<Inventory> findByWarehouseId(Long warehouseId);
    
    List<Inventory> findByStatus(String status);
    
    List<Inventory> findByStockStatus(String stockStatus);
    
    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand <= i.reorderLevel")
    List<Inventory> findLowStockItems();
    
    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand <= 0")
    List<Inventory> findOutOfStockItems();
    
    @Query("SELECT i FROM Inventory i WHERE " +
           "LOWER(i.productName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.productCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.warehouseName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Inventory> searchInventory(@Param("searchTerm") String searchTerm);
}
