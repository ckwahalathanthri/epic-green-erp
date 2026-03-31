package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.request.SupplierPaymentRequest;
import lk.epicgreen.erp.supplier.dto.response.SupplierPaymentDTO;
import lk.epicgreen.erp.supplier.entity.PurchaseOrder;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierPayment;
import lk.epicgreen.erp.supplier.repository.PurchaseOrderRepository;
import lk.epicgreen.erp.supplier.repository.SupplierPaymentRepository;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierPaymentService {
    private final SupplierPaymentRepository repository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    
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
           supplierPayment.setPurchaseOrderId(dto.getPurchaseOrderId());
           supplierPayment.setPaymentMethod(dto.getPaymentMethod());
           supplierPayment.setSupplierId(dto.getSupplierId());
           supplierPayment.setReferenceNumber(dto.getReferenceNumber());
           supplierPayment.setChequeNumber(dto.getChequeNumber());
           supplierPayment.setChequeDate(dto.getChequeDate());
           supplierPayment.setBankName(dto.getBankName());
           supplierPayment.setNotes(dto.getNotes());
           // Set default payment status)

           supplierPayment.setPaymentStatus("PENDING");
           return supplierPayment;
    }

    public SupplierPaymentDTO createSupplierPayment(SupplierPaymentRequest supplierPaymentRequest) {

        PurchaseOrder po = purchaseOrderRepository.findById(supplierPaymentRequest.getPurchaseOrderId()).orElseThrow(() -> new RuntimeException("Purchase Order Not Found"));
        SupplierPayment supplierPayment = toEntity(supplierPaymentRequest);
        Supplier supplier = supplierRepository.findById(supplierPaymentRequest.getSupplierId()).orElseThrow(() -> new RuntimeException("Supplier Not Found"));

        BigDecimal amountToPay = po.getTotalAmount();
        BigDecimal payment = BigDecimal.valueOf(supplierPaymentRequest.getAmount());


        if (po.getPoStatus().equals("COMPLETED")) {
            throw new RuntimeException("Payment already completed for this purchase order");
        }

        if (payment.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Payment amount cannot be negative");
        } else if (payment.compareTo(amountToPay) > 0) {
            throw new RuntimeException("Payment amount cannot be greater than the total amount of the purchase order");
        } else if (payment.compareTo(amountToPay) == 0) {
            supplierPayment.setPaymentStatus("Completed");
            po.setPoStatus("COMPLETED");
            supplier.setBalance(0.0);

        } else if (payment.compareTo(amountToPay) < 0 && payment.compareTo(BigDecimal.ZERO) > 0) {
            po.setBalance(po.getBalance() + payment.doubleValue());
            supplierPayment.setPaymentStatus("Partially Paid");
            po.setPoStatus("PARTIALLY_PAID");
            if (BigDecimal.valueOf(po.getBalance()).compareTo(po.getTotalAmount())==0) {
                po.setPoStatus("COMPLETED");
                supplierPayment.setPaymentStatus("Completed");
                supplier.setBalance(0.0);
                List<SupplierPayment> payments = repository.findAllByPaymentStatus("Partially Paid")
                        .stream()
                        .filter(p -> Objects.equals(p.getPurchaseOrderId(), po.getId()))
                        .collect(Collectors.toList());

                payments.forEach(payment1 -> {
                    payment1.setPaymentStatus("Completed");
                });

                repository.saveAll(payments);
            }
        }
        purchaseOrderRepository.save(po);
        supplierRepository.save(supplier);


        return toDTO(repository.save(supplierPayment));
    }


        public SupplierPaymentDTO updateSupplierPayment (Long id, SupplierPaymentRequest supplierPaymentRequest){
            SupplierPayment supplierPayment = repository.findById(id).get();
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