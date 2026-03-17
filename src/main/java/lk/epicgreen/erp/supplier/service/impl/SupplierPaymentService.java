package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.SupplierPaymentDTO;
import lk.epicgreen.erp.supplier.repository.SupplierPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierPaymentService {
    private final SupplierPaymentRepository repository;
    
    public List<SupplierPaymentDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public SupplierPaymentDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO).get();
    }
    
    private SupplierPaymentDTO toDTO(Object entity) {
        return new SupplierPaymentDTO();
    }
}