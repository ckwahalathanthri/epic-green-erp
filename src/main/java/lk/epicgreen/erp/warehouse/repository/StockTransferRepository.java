package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    Optional<StockTransfer> findByTransferNumber(String transferNumber);
    List<StockTransfer> findByTransferStatus(String status);
    List<StockTransfer> findByFromWarehouseId(Long warehouseId);
    List<StockTransfer> findByToWarehouseId(Long warehouseId);
}
