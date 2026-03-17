package lk.epicgreen.erp.production.service.impl;

import lk.epicgreen.erp.production.repository.MaterialConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialConsumptionService {
    private final MaterialConsumptionRepository repository;
    
    public List getAll() {
        return repository.findAll();
    }
    
    public List getByProductionOrder(Long orderId) {
        return repository.findByProductionOrderId(orderId);
    }
}