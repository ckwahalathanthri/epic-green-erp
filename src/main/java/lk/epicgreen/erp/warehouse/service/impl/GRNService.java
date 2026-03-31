package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.supplier.entity.PurchaseOrder;
import lk.epicgreen.erp.supplier.entity.PurchaseOrderItem;
import lk.epicgreen.erp.supplier.repository.PurchaseOrderRepository;
import lk.epicgreen.erp.warehouse.dto.response.GRNDTO;
import lk.epicgreen.erp.warehouse.dto.response.GRNItemDTO;
import lk.epicgreen.erp.warehouse.entity.GRNItem;
import lk.epicgreen.erp.warehouse.entity.GoodsReceiptNote;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.repository.GoodsReceiptNoteRepository;
import lk.epicgreen.erp.warehouse.repository.InventoryRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GRNService {
    private final GoodsReceiptNoteRepository grnRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    
    public List<GRNDTO> getAll() {
        return grnRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public GRNDTO getById(Long id) {
        return grnRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("GRN not found"));
    }

    public GRNDTO create(GRNDTO dto) {
        //while creating a grn we get the received items and in that there is orderd qty received qty and rejected qty
        // based on the rejected qty it should save the received qty to the po part and update the total properly
        GoodsReceiptNote grn = toEntity(dto);
        grn.setGrnStatus("RECEIVED");
        PurchaseOrder po=purchaseOrderRepository.findById(grn.getPurchaseOrderId()).orElseThrow(()->new RuntimeException(("Purchase Order Not Found")));
        if(po.getPoStatus().equals("Waiting for approval")){
            throw  new RuntimeException("Purchase Order already received");
        }
        po.setPoStatus("waiting for approval");
        purchaseOrderRepository.save(po);
        GoodsReceiptNote saved = grnRepository.save(grn);
        return toDTO(saved);
    }
    
    public GRNDTO update(Long id, GRNDTO dto) {
        GoodsReceiptNote grn = grnRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("GRN not found"));
        // Update fields
        GoodsReceiptNote updated = grnRepository.save(grn);
        return toDTO(updated);
    }


    public void approve(Long id) {
        GoodsReceiptNote grn = grnRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("GRN not found"));
        if(grn.getGrnStatus().equals("APPROVED")){
            throw new RuntimeException("GRN already approved");
        }
        grn.setGrnStatus("APPROVED");



        PurchaseOrder po=purchaseOrderRepository.findById(grn.getPurchaseOrderId()).orElseThrow(()->new RuntimeException(("Purchase Order Not Found")));

        if(po.getPoStatus().equals("RECEIVED")){
            throw  new RuntimeException("Purchase Order already received");
        }
        po.setPoStatus("RECEIVED");

        purchaseOrderRepository.save(po);
        grnRepository.save(grn);
    }
    
    private GRNDTO toDTO(GoodsReceiptNote entity) {
        GRNDTO dto = new GRNDTO();
        dto.setId(entity.getId());
        dto.setPoNumber(entity.getPoNumber());
        dto.setSupplierId(entity.getSupplierId());
        dto.setSupplierName(entity.getSupplierName());
        dto.setSupplierName(entity.getSupplierName());
        dto.setWarehouseId(entity.getWarehouse().getId());
        dto.setWarehouseName(entity.getWarehouse().getWarehouseName());
        dto.setItems(entity.getGrnItems().stream().map(item -> {
            GRNItemDTO itemDTO = new GRNItemDTO();
            itemDTO.setId(item.getId());
//            itemDTO.setPurchaseOrderId(item.getProduct().getId());
//            itemDTO.setPoNumber(item.getProductCode());
//            itemDTO.setGrnNumber(item.getGrn().getGrnNumber());
            itemDTO.setNotes(item.getNotes());
            itemDTO.setBatchNumber(item.getBatchNumber());
            item.setBin(item.getBin());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductCode(item.getProductCode());
            itemDTO.setProductName(item.getProduct().getProductName());
            itemDTO.setUnitOfMeasure(item.getUnitOfMeasure());
            itemDTO.setReceivedQuantity(item.getReceivedQuantity());
            itemDTO.setUnitPrice(item.getUnitPrice());
            itemDTO.setTotalPrice(item.getTotalValue());
            itemDTO.setOrderedQuantity(item.getReceivedQuantity());



            return itemDTO;
        }).collect(Collectors.toList()));
        dto.setGrnNumber(entity.getGrnNumber());
        dto.setGrnStatus(entity.getGrnStatus());
        dto.setQualityStatus(entity.getQualityStatus());
        dto.setReceivedDate(entity.getReceivedDate());
        dto.setNotes(entity.getNotes());
        return dto;
    }
    
    private GoodsReceiptNote toEntity(GRNDTO dto) {
        GoodsReceiptNote entity = new GoodsReceiptNote();
        entity.setGrnNumber(dto.getGrnNumber());
        entity.setPurchaseOrderId(dto.getPurchaseOrderId());
        entity.setPoNumber(dto.getPoNumber());
        entity.setSupplierId(dto.getSupplierId());
        entity.setSupplierName(dto.getSupplierName());


        entity.setWarehouse(warehouseRepository.findById(dto.getWarehouseId()).orElseThrow(() -> new RuntimeException("Warehouse not found")));
        entity.setReceivedDate(dto.getReceivedDate());
        entity.setSupplierInvoiceNumber(dto.getInvoiceNumber());
        entity.setDeliveryNoteNumber(dto.getDeliveryNote());
        entity.setGrnStatus(dto.getQualityStatus());
        entity.setQualityStatus(dto.getQualityStatus());
        entity.setNotes(dto.getNotes());

        PurchaseOrder purchaseOrder=purchaseOrderRepository.findById(dto.getPurchaseOrderId()).orElseThrow(()->new RuntimeException("Purchase Order Not Found"));
        List<PurchaseOrderItem> poItems=purchaseOrder.getItems();
        AtomicInteger index=new AtomicInteger(0);

        entity.setItems(dto.getItems().stream().map(itemDTO -> {
            GRNItem item = new GRNItem();
            int i=index.getAndIncrement();
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            poItems.get(i).setReceivedQuantity(itemDTO.getReceivedQuantity().intValue());


            purchaseOrderRepository.save(purchaseOrder);

            item.setProduct(product);
            System.out.println("The unit price is "+itemDTO.getUnitPrice());
            item.setUnitPrice(poItems.get(i).getUnitPrice());
            item.setUnitOfMeasure(poItems.get(i).getUnitOfMeasure());
            item.setReceivedQuantity(itemDTO.getReceivedQuantity());
            item.setRejectedQuantity(itemDTO.getRejectedQuantity());
            item.setOrderedQuantity(itemDTO.getOrderedQuantity());
            item.setProductName(product.getProductName());
            item.setProductCode(itemDTO.getProductCode());
            item.setNotes(itemDTO.getNotes());
            Warehouse warehouse = warehouseRepository.findById(entity.getWarehouse().getId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            Inventory inventory=inventoryRepository.findByWarehouseIdAndProductId(warehouse.getId(), product.getId())
                            .orElseGet(() -> {
                                Inventory newInventory = new Inventory();
                                newInventory.setWarehouse(warehouse);
                                newInventory.setProduct(product);
                                newInventory.setQuantityAvailable(BigDecimal.valueOf(0));
                                return newInventory;
                            });
          inventory.receive(item.getReceivedQuantity());

          inventory.setUnitCost(item.getUnitPrice());
            inventory.setLocation(null);
            inventoryRepository.save(inventory);
            item.setInventoryItem(inventory);



            item.setGrn(entity);
            return item;
        }).collect(Collectors.toList()));
        return entity;
    }
}
