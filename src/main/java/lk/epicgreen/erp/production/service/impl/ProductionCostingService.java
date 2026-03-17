package lk.epicgreen.erp.production.service.impl;

import lk.epicgreen.erp.production.repository.ProductionActualCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductionCostingService {
    private final ProductionActualCostRepository repository;
    
    public Object getCostByOrder(Long orderId) {
        return repository.findByProductionOrderId(orderId).orElse(null);
    }
}