package lk.epicgreen.erp.warehouse.repository;

import lk.epicgreen.erp.warehouse.entity.CycleCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CycleCountRepository extends JpaRepository<CycleCount, Long> {
}