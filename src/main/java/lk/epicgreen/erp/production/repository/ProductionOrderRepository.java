package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {
    List<ProductionOrder> findByOrderStatus(String status);
    List<ProductionOrder> findByWorkCenterId(Long workCenterId);
}