package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.entity.StorageZone;
import lk.epicgreen.erp.warehouse.repository.StorageZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StorageZoneService {
    private final StorageZoneRepository repository;
    
    public List<ZoneDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public List<ZoneDTO> getByWarehouse(Long warehouseId) {
        return repository.findByWarehouseId(warehouseId).stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public ZoneDTO create(ZoneDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }
    
    private ZoneDTO toDTO(Object entity) {
        return new ZoneDTO();
    }
    
    private StorageZone toEntity(ZoneDTO dto) {
        StorageZone storageZone=new StorageZone();
        storageZone.setId(dto.getId());
        storageZone.setZoneName(dto.getName());
        storageZone.setZoneCode(dto.getCode());
        storageZone.setCreatedAt(dto.getCreatedAt());
        return storageZone;

    }
}