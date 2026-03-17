package lk.epicgreen.erp.warehouse.repository;
import lk.epicgreen.erp.warehouse.entity.GoodsReceiptNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsReceiptNoteRepository extends JpaRepository<GoodsReceiptNote, Long> {
    Optional<GoodsReceiptNote> findByGrnNumber(String grnNumber);
    List<GoodsReceiptNote> findByWarehouseId(Long warehouseId);
    List<GoodsReceiptNote> findByGrnStatus(String grnStatus);
    List<GoodsReceiptNote> findByReceivedDateBetween(LocalDate startDate, LocalDate endDate);
}
