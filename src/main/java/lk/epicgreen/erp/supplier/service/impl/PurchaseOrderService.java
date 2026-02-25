package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.PurchaseOrderDTO;
import lk.epicgreen.erp.supplier.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderService {
    private final PurchaseOrderRepository repository;
    
    public List<PurchaseOrderDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public PurchaseOrderDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO).get();
    }
    
    private PurchaseOrderDTO toDTO(Object entity) {
        return new PurchaseOrderDTO();
    }
}