package lk.epicgreen.erp.warehouse.repository;
import lk.epicgreen.erp.warehouse.entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {
    Optional<StockReservation> findByReservationNumber(String reservationNumber);
    List<StockReservation> findByWarehouseId(Long warehouseId);
    List<StockReservation> findByReservationStatus(String reservationStatus);
    List<StockReservation> findByReservationType(String reservationType);
}
