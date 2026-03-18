package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.request.SupplierPaymentRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierPaymentDTO;
import lk.epicgreen.erp.supplier.entity.SupplierPayment;
import lk.epicgreen.erp.supplier.repository.SupplierPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
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
    
    private SupplierPaymentDTO toDTO(SupplierPayment entity) {
        SupplierPaymentDTO supplierPaymentDTO=new SupplierPaymentDTO();
        supplierPaymentDTO.setId(entity.getId());
        supplierPaymentDTO.setSupplierName(entity.getSupplierName());
        supplierPaymentDTO.setSupplierId(entity.getSupplierId());
        supplierPaymentDTO.setPaymentDate(entity.getPaymentDate());
        supplierPaymentDTO.setAmount(entity.getAmount());
        supplierPaymentDTO.setPaymentMethod(entity.getPaymentMethod());
        supplierPaymentDTO.setPaymentNumber(entity.getPaymentNumber());
        supplierPaymentDTO.setPaymentStatus(entity.getPaymentStatus());
            return supplierPaymentDTO;

    }

    private SupplierPayment toEntity(SupplierPaymentRequest dto) {
        Random r= new Random();
        String s1="PT-";
        for(int i=0;i<7;i++){
            s1+=r.nextInt(10);
        }

        SupplierPayment supplierPayment = new SupplierPayment();
              supplierPayment.setPaymentNumber(s1);
           supplierPayment.setAmount(BigDecimal.valueOf(dto.getAmount()));
           supplierPayment.setPaymentDate(dto.getPaymentDate());
           supplierPayment.setSupplierName(dto.getSupplierName());
           supplierPayment.setPaymentMethod(dto.getPaymentMethod());
           supplierPayment.setSupplierId(dto.getSupplierId());
           supplierPayment.setReferenceNumber(dto.getReferenceNumber());
           supplierPayment.setChequeNumber(dto.getChequeNumber());
           supplierPayment.setChequeDate(dto.getChequeDate());
           supplierPayment.setBankName(dto.getBankName());
           supplierPayment.setNotes(dto.getNotes());
           // Set default payment status
           supplierPayment.setPaymentStatus("PENDING");
           return supplierPayment;
    }

    public SupplierPaymentDTO createSupplierPayment(SupplierPaymentRequest supplierPaymentRequest) {

        return toDTO(repository.save(toEntity(supplierPaymentRequest)));
    }

    public SupplierPaymentDTO updateSupplierPayment(Long id,SupplierPaymentRequest supplierPaymentRequest) {
        SupplierPayment supplierPayment=repository.findById(id).get();
        supplierPayment.setSupplierName(supplierPaymentRequest.getSupplierName());
        supplierPayment.setPaymentDate(supplierPaymentRequest.getPaymentDate());
        supplierPayment.setAmount(BigDecimal.valueOf(supplierPaymentRequest.getAmount()));
        supplierPayment.setPaymentMethod(supplierPaymentRequest.getPaymentMethod());
        supplierPayment.setPaymentNumber(supplierPaymentRequest.getReferenceNumber());
        supplierPayment.setPaymentStatus("Pending");
        supplierPayment.setSupplierId(supplierPaymentRequest.getSupplierId());
        supplierPayment.setReferenceNumber(supplierPaymentRequest.getReferenceNumber());
        supplierPayment.setChequeNumber(supplierPaymentRequest.getChequeNumber());
        supplierPayment.setChequeDate(supplierPaymentRequest.getChequeDate());
        supplierPayment.setBankName(supplierPaymentRequest.getBankName());
        supplierPayment.setNotes(supplierPaymentRequest.getNotes());
        return toDTO(repository.save(supplierPayment));
    }
}