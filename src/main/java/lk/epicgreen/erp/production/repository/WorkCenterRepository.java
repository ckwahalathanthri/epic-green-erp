package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.WorkCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkCenterRepository extends JpaRepository<WorkCenter, Long> {
    List<WorkCenter> findByIsActiveTrue();
}