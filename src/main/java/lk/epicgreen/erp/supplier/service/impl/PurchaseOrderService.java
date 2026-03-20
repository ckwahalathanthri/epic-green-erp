package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.request.PurchaseOrderItemRequest;
import lk.epicgreen.erp.supplier.dto.request.PurchaseOrderRequest;
import lk.epicgreen.erp.supplier.dto.response.PurchaseOrderDTO;
import lk.epicgreen.erp.supplier.entity.PurchaseOrder;
import lk.epicgreen.erp.supplier.entity.PurchaseOrderItem;
import lk.epicgreen.erp.supplier.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
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

    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderRequest request){
        return toDTO(repository.save(toEntity(request)));
    }

    private PurchaseOrderDTO toDTO(PurchaseOrder entity) {
        PurchaseOrderDTO purchaseOrderDTO=new PurchaseOrderDTO();

        purchaseOrderDTO.setId(entity.getId());
        purchaseOrderDTO.setPoNumber(entity.getPoNumber());
        purchaseOrderDTO.setPoStatus(entity.getPoStatus());
        purchaseOrderDTO.setExpectedDeliveryDate(entity.getExpectedDeliveryDate());
        purchaseOrderDTO.setTotalAmount(entity.getTotalAmount());
        purchaseOrderDTO.setSupplierId(entity.getSupplierId());
        purchaseOrderDTO.setPoDate(entity.getPoDate());
        purchaseOrderDTO.setSupplierName(entity.getSupplierName());
        purchaseOrderDTO.setItems(entity.getItems());

        return purchaseOrderDTO;

    }

    private PurchaseOrder toEntity(PurchaseOrderRequest request){
        System.out.println("Items received: " + request.getPurchaseOrderItemRequests());
        System.out.println("Items count: " + (request.getPurchaseOrderItemRequests() != null ? request.getPurchaseOrderItemRequests().size() : "NULL"));
        PurchaseOrder purchaseOrder=new PurchaseOrder();
        purchaseOrder.setSupplierId(request.getSupplierId());
        purchaseOrder.setPoNumber(request.getPoNumber());
        purchaseOrder.setPoDate(request.getPoDate());
        purchaseOrder.setTaxPercentage(request.getTaxPercentage());
        purchaseOrder.setShippingCost(request.getShippingCost());
        purchaseOrder.setDiscountPercentage(request.getDiscountPercentage());
        purchaseOrder.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        purchaseOrder.setDeliveryAddress(request.getDeliveryAddress());
        purchaseOrder.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        purchaseOrder.setPaymentTerms(request.getPaymentTerms());
        purchaseOrder.setNotes(request.getNotes());
        purchaseOrder.setSupplierName(request.getSupplierName());
        purchaseOrder.setTotalAmount(request.getTotal());
        purchaseOrder.setPoStatus("DRAFT");

        purchaseOrder.setNotes(request.getNotes());


        if (request.getPurchaseOrderItemRequests() != null && !request.getPurchaseOrderItemRequests().isEmpty()) {
            for (PurchaseOrderItemRequest purchaseOrderItemRequest : request.getPurchaseOrderItemRequests()) {
                PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
                purchaseOrderItem.setProductId(purchaseOrderItemRequest.getProductId());
                purchaseOrderItem.setDiscountPercentage(purchaseOrderItemRequest.getDiscount());

                purchaseOrderItem.setTaxPercentage(purchaseOrderItemRequest.getTaxPercentage());
                purchaseOrderItem.setTotalPrice(purchaseOrderItemRequest.getTotalPrice());

                purchaseOrderItem.setProductCode(purchaseOrderItemRequest.getProductCode());
                purchaseOrderItem.setProductName(purchaseOrderItemRequest.getProductName());
                purchaseOrderItem.setQuantity(purchaseOrderItemRequest.getQuantity());
                purchaseOrderItem.setUnitOfMeasure(purchaseOrderItemRequest.getUnitOfMeasure());
                purchaseOrderItem.setUnitPrice(purchaseOrderItemRequest.getUnitPrice());
                purchaseOrderItem.setDiscountAmount(purchaseOrderItemRequest.getDiscountAmount());
                purchaseOrderItem.setTaxPercentage(purchaseOrderItemRequest.getTaxPercentage());
                purchaseOrderItem.setTaxAmount(purchaseOrderItemRequest.getTaxAmount());
                purchaseOrder.addItem(purchaseOrderItem);
            }
        }
        return purchaseOrder;
    }
    private Long id;
    private String poNumber;
    private Long supplierId;
    private String supplierName;
    private LocalDate expectedDeliveryDate;
    private LocalDate poDate;
    private String poStatus;
    private BigDecimal totalAmount;

    public List<PurchaseOrderDTO> getPoPending() {
        return repository.findallByStatusPending()
                .stream().map(po-> new PurchaseOrderDTO(
                        po.getId(),
                        po.getPoNumber(),
                        po.getSupplierId(),
                        po.getSupplierName(),
                        po.getExpectedDeliveryDate(),
                        po.getPoDate(),
                        po.getPoStatus(),
                        po.getTotalAmount(),
                        po.getItems()
                )).collect(Collectors.toList());
    }
}