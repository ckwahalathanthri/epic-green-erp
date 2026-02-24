package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.response.WarehouseTransferDTO;
import lk.epicgreen.erp.warehouse.entity.WarehouseTransfer;
import lk.epicgreen.erp.warehouse.repository.WarehouseTransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseTransferService {
    private final WarehouseTransferRepository repository;
    
    public List<WarehouseTransferDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public WarehouseTransferDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO).get();
    }
    
    public WarehouseTransferDTO create(WarehouseTransferDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }
    
    public WarehouseTransferDTO approve(Long id) {
        var transfer = repository.findById(id).get();
        // Set status to APPROVED
        return toDTO(repository.save(transfer));
    }
    
    private WarehouseTransferDTO toDTO(Object entity) {
        return new WarehouseTransferDTO();
    }
    
    private WarehouseTransfer toEntity(WarehouseTransferDTO dto)
    {
        WarehouseTransfer whTransfer = new WarehouseTransfer();
            whTransfer.setId(dto.getId());
            whTransfer.setTransferNumber(dto.getTransferNumber());
            whTransfer.setToWarehouseId(dto.getToWarehouseId());
        return whTransfer;
    }
}