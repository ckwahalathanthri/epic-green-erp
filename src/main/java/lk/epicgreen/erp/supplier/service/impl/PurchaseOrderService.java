package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.supplier.dto.request.PurchaseOrderItemRequest;
import lk.epicgreen.erp.supplier.dto.request.PurchaseOrderRequest;
import lk.epicgreen.erp.supplier.dto.response.PurchaseOrderDTO;
import lk.epicgreen.erp.supplier.entity.PurchaseOrder;
import lk.epicgreen.erp.supplier.entity.PurchaseOrderItem;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.repository.PurchaseOrderItemRepository;
import lk.epicgreen.erp.supplier.repository.PurchaseOrderRepository;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.repository.InventoryRepository;
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

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final InventoryRepository inventoryRepository;
    
    public List<PurchaseOrderDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    public PurchaseOrderDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO).get();
    }

    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderRequest request){
        return toDTO(repository.save(toEntity(request)));
    }

    public List<PurchaseOrderDTO> getPoBySupplier(Long supplierId){
        List<PurchaseOrder> purchaseOrders=repository.findBySupplierId(supplierId);
        return purchaseOrders.stream().map(this::toDTO).collect(Collectors.toList());
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
        purchaseOrderDTO.setBalance(entity.getBalance());
        return purchaseOrderDTO;

    }

    private PurchaseOrder toEntity(PurchaseOrderRequest request){
//check if a previous order was placed with the same po number if so get the stock of that number

        List<Long> productIds=request.getPurchaseOrderItemRequests().stream().map(PurchaseOrderItemRequest::getProductId).collect(Collectors.toList());
        List<Product> product =productRepository.findAllById(productIds);





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
            int count=0;

            for (PurchaseOrderItemRequest purchaseOrderItemRequest : request.getPurchaseOrderItemRequests()) {
                System.out.println("The count value is "+count);
                Long totalStock= Long.valueOf(purchaseOrderItemRequest.getQuantity());
                Long result=purchaseOrderItemRepository.getTotalOrderedQuantityByProductId(productIds.get(count));
                totalStock= result==null?totalStock:totalStock+result;
                System.out.println("total stock "+totalStock);

                BigDecimal quantity=BigDecimal.valueOf(purchaseOrderItemRequest.getQuantity());
                BigDecimal maximumStock=product.get(count).getMaximumStockLevel();
                System.out.println("maximum stock "+maximumStock);

                if(quantity.compareTo(maximumStock)>0||totalStock>maximumStock.intValue()){
                    throw new RuntimeException("Quantity exceeds maximum stock level for product: " + product.get(count).getProductName());
                }
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
                count++;
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

    public List<PurchaseOrderDTO> getPoDraft() {
        return repository.findAllByPoStatus("DRAFT")
                .stream().map(po-> new PurchaseOrderDTO(
                                        po.getId(),
                                        po.getPoNumber(),
                                        po.getSupplierId(),
                                        po.getSupplierName(),
                                        po.getExpectedDeliveryDate(),
                                        po.getPoDate(),
                                        po.getPoStatus(),
                        po.getBalance(),
                        po.getTotalAmount(),
                                        po.getItems()
                                )).collect(Collectors.toList());
    }

    public List<PurchaseOrderDTO> getPoBySupplierAndStatus(Long supplierId, List<String> received) {
        return repository.findBySupplierIdAndPoStatusIn(supplierId,received).stream().map(this::toDTO).collect(Collectors.toList());
    }
}