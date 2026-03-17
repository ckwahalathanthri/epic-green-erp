package lk.epicgreen.erp.warehouse.repository;
import lk.epicgreen.erp.warehouse.entity.StockIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockIssueRepository extends JpaRepository<StockIssue, Long> {
    Optional<StockIssue> findByIssueNumber(String issueNumber);
    List<StockIssue> findByWarehouseId(Long warehouseId);
    List<StockIssue> findByIssueStatus(String issueStatus);
    List<StockIssue> findByIssueType(String issueType);
}
