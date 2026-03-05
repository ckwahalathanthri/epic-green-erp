package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.Dispatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DispatchRepository extends JpaRepository<Dispatch, Long> {
    Optional<Dispatch> findByDispatchNumber(String dispatchNumber);
    List<Dispatch> findByOrderId(Long orderId);
    List<Dispatch> findByDispatchStatus(String status);
    List<Dispatch> findByVehicleId(Long vehicleId);
}
