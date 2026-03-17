package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    @Query("SELECT s FROM PurchaseOrder s WHERE s.poStatus='PENDING' ")
    List<PurchaseOrder> findallByStatusPending();
}