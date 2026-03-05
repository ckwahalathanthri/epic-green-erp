package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.PackingSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PackingSlipRepository extends JpaRepository<PackingSlip, Long> {
    Optional<PackingSlip> findByPackingNumber(String packingNumber);
    List<PackingSlip> findByOrderId(Long orderId);
    List<PackingSlip> findByPackingStatus(String status);
    List<PackingSlip> findByPickingListId(Long pickingListId);
}