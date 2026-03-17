package lk.epicgreen.erp.warehouse.service;


import lk.epicgreen.erp.warehouse.entity.InventoryAlert;
import lk.epicgreen.erp.warehouse.repository.InventoryAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryAlertService {
    private final InventoryAlertRepository repository;
    
    public List<InventoryAlert> findAll() {
        return repository.findAll();
    }
    
    public List<InventoryAlert> findByStatus(String status) {
        return repository.findByAlertStatus(status);
    }
    
    public InventoryAlert acknowledge(Long id, String acknowledgedBy) {
        InventoryAlert alert = repository.findById(id).get();
        alert.acknowledge(acknowledgedBy);
        return repository.save(alert);
    }
    
    public InventoryAlert resolve(Long id, String resolvedBy, String notes) {
        InventoryAlert alert = repository.findById(id).get();
        alert.resolve(resolvedBy, notes);
        return repository.save(alert);
    }
}
