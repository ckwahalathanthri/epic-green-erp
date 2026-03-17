package lk.epicgreen.erp.credit.controller.repo;
import lk.epicgreen.erp.credit.controller.entity.CreditLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditLimitRepository extends JpaRepository<CreditLimit, Long> {
    List<CreditLimit> findByCustomerId(Long customerId);
    
    @Query("SELECT c FROM CreditLimit c WHERE c.customer.id = :customerId AND c.isActive = true")
    Optional<CreditLimit> findActiveByCustomerId(@Param("customerId") Long customerId);
    
    List<CreditLimit> findByApprovalStatus(String approvalStatus);
}
