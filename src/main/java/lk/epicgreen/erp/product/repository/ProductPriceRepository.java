package lk.epicgreen.erp.product.repository;

import lk.epicgreen.erp.product.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    
    List<ProductPrice> findByProductId(Long productId);
    
    List<ProductPrice> findByProductIdAndIsActiveTrue(Long productId);
    
    Optional<ProductPrice> findByProductIdAndIsDefaultTrue(Long productId);
    
    List<ProductPrice> findByPriceType(String priceType);
    
    List<ProductPrice> findByIsActiveTrue();
}
