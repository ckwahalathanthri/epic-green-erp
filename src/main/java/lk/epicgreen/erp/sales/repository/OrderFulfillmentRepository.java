package lk.epicgreen.erp.sales.repository;


import lk.epicgreen.erp.sales.entity.OrderFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderFulfillmentRepository extends JpaRepository<OrderFulfillment, Long> {
    Optional<OrderFulfillment> findByOrderId(Long orderId);
    List<OrderFulfillment> findByFulfillmentStatus(String status);
}