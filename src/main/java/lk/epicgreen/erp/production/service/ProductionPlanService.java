package lk.epicgreen.erp.production.service;


import lk.epicgreen.erp.production.dto.response.ProductionPlanDTO;
import lk.epicgreen.erp.production.entity.ProductionPlan;
import lk.epicgreen.erp.production.repository.ProductionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductionPlanService {
    private final ProductionPlanRepository repository;
    
    public List<ProductionPlanDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public ProductionPlanDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO).get();
    }
    
    public ProductionPlanDTO create(ProductionPlanDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }
    
    private ProductionPlanDTO toDTO(ProductionPlan entity) {
        ProductionPlanDTO productionPlanDTO = new ProductionPlanDTO();
        productionPlanDTO.setId(entity.getId());
        productionPlanDTO.setPlanName(entity.getPlanName());
        productionPlanDTO.setPlanNumber(entity.getPlanNumber());
        productionPlanDTO.setPlanStatus(entity.getPlanStatus());
        return new ProductionPlanDTO();
    }
    
    private ProductionPlan toEntity(ProductionPlanDTO dto) {
        ProductionPlan productionPlan = new ProductionPlan();
        productionPlan.setId(dto.getId());
        productionPlan.setPlanName(dto.getPlanName());
        productionPlan.setPlanNumber(dto.getPlanNumber());
        productionPlan.setPlanStatus(dto.getPlanStatus());
        return productionPlan;
    }
}