package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GRNService {
    private final GoodsReceiptNoteRepository grnRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    
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
        GoodsReceiptNote grn = toEntity(dto);
        grn.setGrnStatus("RECEIVED");
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
        grn.setGrnStatus("APPROVED");
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
        entity.setItems(dto.getItems().stream().map(itemDTO -> {
            GRNItem item = new GRNItem();

            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            item.setProduct(product);
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setUnitOfMeasure(itemDTO.getUnitOfMeasure());
            item.setTotalValue(itemDTO.getTotalPrice());

            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setReceivedQuantity(itemDTO.getReceivedQuantity());
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
