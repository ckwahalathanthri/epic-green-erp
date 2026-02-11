package lk.epicgreen.erp.credit.controller.repo;
import lk.epicgreen.erp.credit.controller.entity.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {
    List<CreditTransaction> findByCreditLimitId(Long creditLimitId);
    List<CreditTransaction> findByCreditLimitIdOrderByTransactionDateDesc(Long creditLimitId);
}
