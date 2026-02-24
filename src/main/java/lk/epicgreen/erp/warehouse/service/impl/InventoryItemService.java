package lk.epicgreen.erp.warehouse.service.impl;
import com.epicgreen.erp.warehouse.dto.InventoryItemDTO;

import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryItemService {
    private final InventoryItemRepository repository;
    
    public List<InventoryItemDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public List<InventoryItemDTO> getByWarehouse(Long warehouseId) {
        return repository.findByWarehouseId(warehouseId).stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public InventoryItemDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO).get();
    }
    
    public InventoryItemDTO create(InventoryItemDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }
    
    private InventoryItemDTO toDTO(Object entity) {
        return new InventoryItemDTO();
    }
    

// incomplete code, need to implement toEntity method accordingly current methods only fixes the error
    private Inventory toEntity(InventoryItemDTO dto){
        Inventory inventory = new Inventory();
        inventory.setId(dto.getId());
        return inventory;

    }
}