package lk.epicgreen.erp.product.repository;

import lk.epicgreen.erp.product.entity.PriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    
    List<PriceHistory> findByProductIdOrderByChangedAtDesc(Long productId);
    
    Page<PriceHistory> findByProductId(Long productId, Pageable pageable);
    
    Page<PriceHistory> findAllByOrderByChangedAtDesc(Pageable pageable);
}
