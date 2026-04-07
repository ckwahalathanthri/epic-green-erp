package lk.epicgreen.erp.sales.mapper;


import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.sales.dto.response.SalesQuotationDTO;
import lk.epicgreen.erp.sales.dto.response.SalesQuotationItemDTO;
import lk.epicgreen.erp.sales.entity.SalesQuotation;
import lk.epicgreen.erp.sales.entity.SalesQuotationItem;
import lk.epicgreen.erp.warehouse.entity.Inventory;
import lk.epicgreen.erp.warehouse.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class SalesQuotationMapper {

    @Autowired
    InventoryRepository inventoryRepository;
    
    public SalesQuotationDTO toDTO(SalesQuotation entity) {
        if (entity == null) return null;
        
        SalesQuotationDTO dto = new SalesQuotationDTO();
        dto.setId(entity.getId());
        dto.setQuotationNumber(entity.getQuotationNumber());
        dto.setQuotationDate(entity.getQuotationDate());
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setCustomerEmail(entity.getCustomerEmail());
        dto.setQuotationStatus(entity.getQuotationStatus());
        dto.setSubtotal(entity.getSubtotal());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
    
    public SalesQuotation toEntity(SalesQuotationDTO dto) {
        if (dto == null) return null;
        
        SalesQuotation entity = new SalesQuotation();
        entity.setQuotationNumber(dto.getQuotationNumber());
        entity.setQuotationDate(dto.getQuotationDate());
        entity.setCustomerId(dto.getCustomerId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setValidUntil(dto.getValidUntil());
        entity.setDiscountAmount(dto.getDiscountAmount());
        entity.setSubtotal(dto.getSubtotal());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setTaxAmount(dto.getTaxAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setPaymentTerms(dto.getPaymentTerms());
        entity.setReferenceNumber(dto.getReferenceNumber());
        entity.setTermsAndConditions(dto.getTermsAndConditions());
        entity.setNotes(dto.getNotes());
        entity.setQuotationStatus(dto.getQuotationStatus());
        List<Long> productIds=dto.getItems().stream().map(SalesQuotationItemDTO::getProductId).collect(Collectors.toList());
        List<Inventory> inventories=inventoryRepository.findAllByProductIdIn(productIds);
        AtomicInteger lineNumberCounter=new AtomicInteger(0);
        if(dto.getItems()!=null){
            entity.setItems(dto.getItems().stream()
                    .map(itemDto->{
                        SalesQuotationItem item= new SalesQuotationItem();
                        if(itemDto.getQuantity().compareTo(inventories.get(lineNumberCounter.getAndIncrement()).getQuantityAvailable())>0){
                            throw new RuntimeException("Insufficient stock for product ID: "+itemDto.getProductId());
                        }
                        item.setLineTotal(itemDto.getLineTotal());
                        item.setProductId(itemDto.getProductId());
                        item.setProductName(itemDto.getProductName());
                        item.setQuantity(itemDto.getQuantity());
                        item.setUnitPrice(itemDto.getUnitPrice());
                        item.setLineNumber(itemDto.getLineNumber());
                        item.setTaxAmount(itemDto.getTaxAmount());
                        item.setDiscountAmount(itemDto.getDiscountAmount());
                        item.setDiscountPercentage(itemDto.getDiscountPercentage());
                        item.setTaxRate(itemDto.getTaxRate());
                        item.setQuotation(entity);
                        return item;

                    }).collect(Collectors.toList()));
        }
        return entity;
    }
    
    public void updateEntityFromDTO(SalesQuotationDTO dto, SalesQuotation entity) {
        if (dto == null || entity == null) return;
        entity.setCustomerName(dto.getCustomerName());
        entity.setQuotationStatus(dto.getQuotationStatus());
    }
}