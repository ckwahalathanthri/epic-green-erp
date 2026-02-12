package lk.epicgreen.erp.credit.controller.repo;
import lk.epicgreen.erp.credit.controller.entity.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {
    Optional<CustomerGroup> findByGroupCode(String groupCode);
    boolean existsByGroupCode(String groupCode);
    List<CustomerGroup> findByIsActive(Boolean isActive);
}
