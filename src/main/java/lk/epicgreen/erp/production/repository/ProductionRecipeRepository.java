package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductionRecipeRepository extends JpaRepository<ProductionRecipe, Long> {
    List<ProductionRecipe> findByRecipeStatus(String status);
    List<ProductionRecipe> findByProductId(Long productId);
}