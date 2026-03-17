package lk.epicgreen.erp.warehouse.service;


import lk.epicgreen.erp.warehouse.entity.PhysicalInventory;
import lk.epicgreen.erp.warehouse.entity.PhysicalInventoryLine;
import lk.epicgreen.erp.warehouse.repository.PhysicalInventoryLineRepository;
import lk.epicgreen.erp.warehouse.repository.PhysicalInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhysicalInventoryService {
    private final PhysicalInventoryRepository repository;
    private final PhysicalInventoryLineRepository lineRepository;
    
    public List<PhysicalInventory> findAll() {
        return repository.findAll();
    }
    
    public PhysicalInventory findById(Long id) {
        return repository.findById(id).get();
    }
    
    public PhysicalInventory create(PhysicalInventory inventory) {
        return repository.save(inventory);
    }
    
    public PhysicalInventory start(Long id) {
        PhysicalInventory inventory = findById(id);
        inventory.start();
        return repository.save(inventory);
    }
    
    public PhysicalInventory complete(Long id) {
        PhysicalInventory inventory = findById(id);
        inventory.complete();
        return repository.save(inventory);
    }
    
    public PhysicalInventory post(Long id) {
        PhysicalInventory inventory = findById(id);
        inventory.post();
        return repository.save(inventory);
    }
    
    public List<PhysicalInventoryLine> getLines(Long inventoryId) {
        return lineRepository.findByPhysicalInventoryId(inventoryId);
    }
}
