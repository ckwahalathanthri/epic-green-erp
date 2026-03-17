package lk.epicgreen.erp.warehouse.repository;


import lk.epicgreen.erp.warehouse.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByProductId(Long productId);
    
    @Query("SELECT b FROM Batch b WHERE b.expiryDate <= CURRENT_DATE + 30")
    List<Batch> findExpiringSoon();
}