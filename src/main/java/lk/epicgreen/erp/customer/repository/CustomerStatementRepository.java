package lk.epicgreen.erp.customer.repository;

import lk.epicgreen.erp.customer.entity.CustomerStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerStatementRepository extends JpaRepository<CustomerStatement, Long> {
    List<CustomerStatement> findByCustomerIdOrderByStatementDateDesc(Long customerId);
    List<CustomerStatement> findByStatementNumber(String statementNumber);
}
