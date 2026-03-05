package lk.epicgreen.erp.sales.repository;

import lk.epicgreen.erp.sales.entity.OrderPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderPricingRepository extends JpaRepository<OrderPricing, Long> {
    
    Optional<OrderPricing> findByRuleCode(String ruleCode);
    
    List<OrderPricing> findByRuleType(String ruleType);
    
    List<OrderPricing> findByIsActiveTrue();
    
    List<OrderPricing> findByCustomerId(Long customerId);
    
    List<OrderPricing> findByProductId(Long productId);
    
    @Query("SELECT p FROM OrderPricing p WHERE p.isActive = true AND :date BETWEEN p.validFrom AND p.validTo")
    List<OrderPricing> findActiveRulesForDate(LocalDate date);
    
    @Query("SELECT p FROM OrderPricing p WHERE p.isActive = true AND :date BETWEEN p.validFrom AND p.validTo AND (p.customerId = :customerId OR p.customerId IS NULL) ORDER BY p.priority DESC")
    List<OrderPricing> findApplicableRulesForCustomer(Long customerId, LocalDate date);
    
    @Query("SELECT p FROM OrderPricing p WHERE p.isActive = true AND :date BETWEEN p.validFrom AND p.validTo AND (p.productId = :productId OR p.productId IS NULL) ORDER BY p.priority DESC")
    List<OrderPricing> findApplicableRulesForProduct(Long productId, LocalDate date);
}