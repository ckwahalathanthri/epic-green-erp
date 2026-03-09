package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.GoodsIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsIssueRepository extends JpaRepository<GoodsIssue, Long> {
    Optional<GoodsIssue> findByIssueNumber(String issueNumber);
    List<GoodsIssue> findByWarehouseId(Long warehouseId);
    List<GoodsIssue> findByIssueType(String issueType);
}
