package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.response.ProductionRecipeDTO;
import lk.epicgreen.erp.production.entity.ProductionRecipe;
import lk.epicgreen.erp.production.repository.ProductionRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductionRecipeService {
    private final ProductionRecipeRepository repository;
    
    public List<ProductionRecipeDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public ProductionRecipeDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO).get();
    }
    
    public ProductionRecipeDTO create(ProductionRecipeDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }
    
    private ProductionRecipeDTO toDTO(ProductionRecipe entity) {
        ProductionRecipeDTO productionRecipeDTO = new ProductionRecipeDTO();
        productionRecipeDTO.setProductId(entity.getProductId());
        productionRecipeDTO.setRecipeCode(entity.getRecipeCode());
        productionRecipeDTO.setProductionTime(entity.getProductionTime());
        productionRecipeDTO.setCostPerUnit(entity.getCostPerUnit());
        productionRecipeDTO.setRecipeStatus(entity.getRecipeStatus());

        return productionRecipeDTO;
    }
    
    private ProductionRecipe toEntity(ProductionRecipeDTO dto) {
        ProductionRecipe productionRecipe = new ProductionRecipe();
        productionRecipe.setProductId(dto.getProductId());
        productionRecipe.setRecipeCode(dto.getRecipeCode());
        productionRecipe.setProductionTime(dto.getProductionTime());
        productionRecipe.setCostPerUnit(dto.getCostPerUnit());
        productionRecipe.setRecipeStatus(dto.getRecipeStatus());

        return productionRecipe;
    }
}