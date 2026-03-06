package lk.epicgreen.erp.warehouse.service;

import lk.epicgreen.erp.warehouse.entity.InventoryBatch;
import lk.epicgreen.erp.warehouse.repository.InventoryBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryBatchService {
    private final InventoryBatchRepository repository;
    
    public List<InventoryBatch> findByProductAndWarehouse(Long productId, Long warehouseId) {
        return repository.findByProductIdAndWarehouseId(productId, warehouseId);
    }
}