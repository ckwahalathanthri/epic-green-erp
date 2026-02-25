package lk.epicgreen.erp.product.repository;

import lk.epicgreen.erp.product.entity.SpecificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpecificationTemplateRepository extends JpaRepository<SpecificationTemplate, Long> {
    List<SpecificationTemplate> findByTemplateNameOrderByDisplayOrderAsc(String templateName);
    List<SpecificationTemplate> findByIsActiveTrueOrderByTemplateNameAsc();
}
