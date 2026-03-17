package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    
    List<OrderStatusHistory> findByOrderIdOrderByChangedAtDesc(Long orderId);
    
    List<OrderStatusHistory> findByOrderNumber(String orderNumber);
    
    List<OrderStatusHistory> findByChangedBy(String changedBy);
    
    @Query("SELECT h FROM OrderStatusHistory h WHERE h.changedAt BETWEEN :startDate AND :endDate ORDER BY h.changedAt DESC")
    List<OrderStatusHistory> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT h FROM OrderStatusHistory h WHERE h.orderId = :orderId AND h.newStatus = :status")
    List<OrderStatusHistory> findByOrderIdAndStatus(Long orderId, String status);
}