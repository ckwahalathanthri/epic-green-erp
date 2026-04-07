package lk.epicgreen.erp.sales.mapper;


import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.product.service.impl.ProductServiceImpl;
import lk.epicgreen.erp.sales.dto.response.DispatchDTO;
import lk.epicgreen.erp.sales.dto.response.DispatchItemDTO;
import lk.epicgreen.erp.sales.entity.Dispatch;
import lk.epicgreen.erp.sales.entity.DispatchItem;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.sales.entity.SalesOrderItem;
import lk.epicgreen.erp.sales.repository.SalesOrderRepository;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class DispatchMapper {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SalesOrderRepository salesOrderRepository;
    
    public DispatchDTO toDTO(Dispatch entity) {
        if (entity == null) return null;
        
        DispatchDTO dto = new DispatchDTO();
        dto.setId(entity.getId());
        dto.setDispatchNumber(entity.getDispatchNumber());
        dto.setDispatchDate(entity.getDispatchDate());
        dto.setOrderId(entity.getOrderId());
        dto.setDispatchStatus(entity.getDispatchStatus());
        dto.setVehicleNumber(entity.getVehicleNumber());
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setDriverPhone(entity.getDriverPhone());
        dto.setDriverName(entity.getDriverName());
        dto.setOrderId(entity.getOrderId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setExpectedDeliveryDate(entity.getExpectedDeliveryDate());
        dto.setFreightCharge(entity.getFreightCharge());
        dto.setTotalPackages(entity.getTotalPackages());
        dto.setTrackingNumber(entity.getTrackingNumber());
        dto.setTotalWeight(entity.getTotalWeight());

        dto.setStockDeducted(entity.getStockDeducted());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
    
    public Dispatch
    toEntity(DispatchDTO dto) {
        if (dto == null) return null;
        
        Dispatch entity = new Dispatch();
        entity.setDispatchNumber(dto.getDispatchNumber());
        entity.setDispatchDate(dto.getDispatchDate());
        entity.setOrderId(dto.getOrderId());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setCustomerId(dto.getCustomerId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setDispatchDate(dto.getDispatchDate());
        entity.setDispatchedAt(dto.getDispatchedAt());
        entity.setShippingAddress(dto.getShippingAddress());
        entity.setDispatchStatus("DISPATCHED");
        entity.setDispatchType(dto.getDispatchType());
        entity.setVehicleId(dto.getVehicleId());
        entity.setVehicleNumber(dto.getVehicleNumber());
        entity.setDriverName(dto.getDriverName());
        entity.setDriverPhone(dto.getDriverPhone());
        entity.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        entity.setActualDeliveryDate(dto.getActualDeliveryDate());
        entity.setTotalPackages(dto.getTotalPackages());
        entity.setTotalWeight(dto.getTotalWeight());
        entity.setFreightCharge(dto.getFreightCharge());
        entity.setTrackingNumber(dto.getTrackingNumber());
        entity.setRouteDetails(dto.getRouteDetails());
        entity.setNotes(dto.getNotes());
        List<Long> productIds = dto.getItems().stream()
            .map(DispatchItemDTO::getProductId)
            .collect(Collectors.toList());
        System.out.println("The product ids are "+ productIds.toString());
        System.out.println("The product ids are "+productIds.get(0).toString());
        List<Inventory> inventories = inventoryRepository.findAllByProductIdIn(productIds);
        AtomicInteger inventoryIndex = new AtomicInteger(0);

        entity.setItems(dto.getItems().stream().map(itemDto->{
            DispatchItem dispatchItem=new DispatchItem();
            Product product= productRepository.findById(itemDto.getProductId()).orElse(null);
            BigDecimal quantityAvailable=inventories.get(inventoryIndex.get()).getQuantityAvailable();
            inventories.get(inventoryIndex.getAndIncrement()).setQuantityAvailable((quantityAvailable.subtract(itemDto.getDispatchedQuantity())));
            SalesOrder salesOrder=salesOrderRepository.findById(dto.getOrderId()).orElse(null);

            dispatchItem.setProduct(product);
            dispatchItem.setBatchNumber(itemDto.getBatchNumber());
            dispatchItem.setQuantityDispatched(itemDto.getDispatchedQuantity());
            dispatchItem.setUom(product!=null?product.getBaseUom():null);
            dispatchItem.setDispatch(entity);
            return dispatchItem;
        }).collect(Collectors.toList()));
        inventoryRepository.saveAll(inventories);
        return entity;
    }
    
    public void updateEntityFromDTO(DispatchDTO dto, Dispatch entity) {
        if (dto == null || entity == null) return;
        entity.setDispatchStatus(dto.getDispatchStatus());
        entity.setVehicleNumber(dto.getVehicleNumber());
    }
}