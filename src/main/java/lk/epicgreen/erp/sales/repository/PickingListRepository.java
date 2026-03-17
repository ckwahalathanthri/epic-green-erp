package lk.epicgreen.erp.sales.repository;


import lk.epicgreen.erp.sales.entity.PickingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PickingListRepository extends JpaRepository<PickingList, Long> {
    Optional<PickingList> findByPickingNumber(String pickingNumber);
    List<PickingList> findByOrderId(Long orderId);
    List<PickingList> findByPickingStatus(String status);
    List<PickingList> findByWarehouseId(Long warehouseId);
}
