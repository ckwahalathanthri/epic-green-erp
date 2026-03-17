package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.WarehouseTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseTransferRepository extends JpaRepository<WarehouseTransfer, Long> {
}