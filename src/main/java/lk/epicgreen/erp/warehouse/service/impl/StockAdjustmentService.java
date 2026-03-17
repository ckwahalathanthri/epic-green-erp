package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.response.StockAdjustmentDTO;
import lk.epicgreen.erp.warehouse.entity.StockAdjustment;
import lk.epicgreen.erp.warehouse.repository.StockAdjustmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockAdjustmentService {
    private final StockAdjustmentRepository repository;
    
    public List<StockAdjustmentDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public StockAdjustmentDTO create(StockAdjustmentDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }
    
    public StockAdjustmentDTO approve(Long id) {
        var adjustment = repository.findById(id).get();
        return toDTO(repository.save(adjustment));
    }
    
    private StockAdjustmentDTO toDTO(StockAdjustment entity) {
        StockAdjustmentDTO dto = new StockAdjustmentDTO();
        dto.setId(entity.getId());
        dto.setAdjustmentNumber(entity.getAdjustmentNumber());
        dto.setAdjustmentType(entity.getAdjustmentType());

        return new StockAdjustmentDTO();
    }
    
    private StockAdjustment toEntity(StockAdjustmentDTO dto)
    {
         StockAdjustment adjustment = new StockAdjustment();
         adjustment.setId(dto.getId());
         adjustment.setAdjustmentNumber(dto.getAdjustmentNumber());
         adjustment.setAdjustmentType(dto.getAdjustmentType());
        return adjustment;
    }
}