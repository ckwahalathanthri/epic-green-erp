package lk.epicgreen.erp.production.service.impl;
import lk.epicgreen.erp.production.entity.ProductionOutput;
import lk.epicgreen.erp.production.repository.ProductionOutputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductionOutputService {
    private final ProductionOutputRepository repository;
    
    public List<ProductionOutput> getAll() {
        return repository.findAll();
    }
    
    public List<ProductionOutput> getByProductionOrder(Long orderId) {
        return repository.findByProductionOrderId(orderId);
    }
}