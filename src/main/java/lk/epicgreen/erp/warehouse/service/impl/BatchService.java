package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.response.BatchDTO;
import lk.epicgreen.erp.warehouse.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BatchService {
    private final BatchRepository repository;
    
    public List<BatchDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public List<BatchDTO> getByProduct(Long productId) {
        return repository.findByProductId(productId).stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public List<BatchDTO> getExpiringSoon() {
        return repository.findExpiringSoon().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    private BatchDTO toDTO(Object entity) {
        return new BatchDTO();
    }
}