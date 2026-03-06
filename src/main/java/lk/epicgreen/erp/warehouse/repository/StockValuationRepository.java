package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.StockValuation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StockValuationRepository extends JpaRepository<StockValuation, Long> {
    List<StockValuation> findByValuationDate(LocalDate date);
}