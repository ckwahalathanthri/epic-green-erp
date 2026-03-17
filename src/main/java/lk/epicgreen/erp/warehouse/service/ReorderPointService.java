package lk.epicgreen.erp.warehouse.service;


import lk.epicgreen.erp.warehouse.entity.ReorderPoint;
import lk.epicgreen.erp.warehouse.repository.ReorderPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReorderPointService {
    private final ReorderPointRepository repository;
    
    public List<ReorderPoint> findAll() {
        return repository.findAll();
    }
    
    public ReorderPoint findById(Long id) {
        return repository.findById(id).get();
    }
    
    public ReorderPoint create(ReorderPoint reorderPoint) {
        return repository.save(reorderPoint);
    }
    
    public ReorderPoint update(Long id, ReorderPoint reorderPoint) {
        ReorderPoint existing = findById(id);
        existing.setMinStockLevel(reorderPoint.getMinStockLevel());
        existing.setMaxStockLevel(reorderPoint.getMaxStockLevel());
        existing.setReorderPoint(reorderPoint.getReorderPoint());
        existing.setReorderQuantity(reorderPoint.getReorderQuantity());
        return repository.save(existing);
    }
    
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
