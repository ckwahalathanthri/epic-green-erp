package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem,Long> {
    @Query("SELECT SUM(p.quantity) FROM PurchaseOrderItem p WHERE p.productId=:productId")
    Long getTotalOrderedQuantityByProductId(@Param("productId") Long productId);
}
