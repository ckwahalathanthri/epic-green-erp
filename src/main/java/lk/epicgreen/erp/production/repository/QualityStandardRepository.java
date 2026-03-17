package lk.epicgreen.erp.production.repository;


import lk.epicgreen.erp.production.entity.QualityStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QualityStandardRepository extends JpaRepository<QualityStandard, Long> {
    List<QualityStandard> findByProductId(Long productId);
}