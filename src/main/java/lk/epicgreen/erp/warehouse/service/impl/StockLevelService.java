package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.response.StockLevelDTO;
import lk.epicgreen.erp.warehouse.repository.StockLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockLevelService {
    private final StockLevelRepository repository;
    
    public List<StockLevelDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public List<StockLevelDTO> getByWarehouse(Long warehouseId) {
        return repository.findByWarehouseId(warehouseId).stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public List<StockLevelDTO> getLowStock(Long warehouseId) {
        return repository.findLowStockItems(warehouseId).stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    private StockLevelDTO toDTO(Object entity) {
        return new StockLevelDTO();
    }
}