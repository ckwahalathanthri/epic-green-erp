package lk.epicgreen.erp.production.service.impl;

import lk.epicgreen.erp.production.repository.QualityInspectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QualityInspectionService {
    private final QualityInspectionRepository repository;
    
    public List getAll() {
        return repository.findAll();
    }
    
    public Object getById(Long id) {
        return repository.findById(id).get();
    }
    
    public List getByProductionOrder(Long orderId) {
        return repository.findByProductionOrderId(orderId);
    }
}