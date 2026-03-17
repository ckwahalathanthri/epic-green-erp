package lk.epicgreen.erp.warehouse.service.impl;


import lk.epicgreen.erp.warehouse.dto.response.CycleCountDTO;
import lk.epicgreen.erp.warehouse.entity.CycleCount;
import lk.epicgreen.erp.warehouse.repository.CycleCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CycleCountService {
    private final CycleCountRepository repository;
    
    public List<CycleCountDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public CycleCountDTO create(CycleCountDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }
    
    public CycleCountDTO complete(Long id) {
        CycleCount count = repository.findById(id).get();
        return toDTO(repository.save(count));
    }
    
    private CycleCountDTO toDTO(CycleCount entity)
    {
        CycleCountDTO dto=new CycleCountDTO();
        dto.setId(entity.getId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setCountMethod(entity.getCountMethod());
        dto.setCountStatus(entity.getCountStatus());
        return new CycleCountDTO();
    }
    
    private CycleCount toEntity(CycleCountDTO dto) {
        CycleCount cycleCount= new CycleCount();
            cycleCount.setWarehouseId(dto.getWarehouseId());

            cycleCount.setCountType(dto.getCountType());
            cycleCount.setCountStatus(dto.getCountStatus());
            cycleCount.setCountMethod(dto.getCountMethod());
            cycleCount.setApprovedBy(dto.getApprovedBy());
            cycleCount.setCountMethod(dto.getCountMethod());
            return cycleCount;


    }
}