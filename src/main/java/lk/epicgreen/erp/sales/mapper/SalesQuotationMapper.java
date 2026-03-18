package lk.epicgreen.erp.sales.mapper;


import lk.epicgreen.erp.sales.dto.response.SalesQuotationDTO;
import lk.epicgreen.erp.sales.entity.SalesQuotation;
import lk.epicgreen.erp.sales.entity.SalesQuotationItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SalesQuotationMapper {
    
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
        entity.setDiscountAmount(dto.getDiscountAmount());
        entity.setSubtotal(dto.getSubtotal());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setTaxAmount(dto.getTaxAmount());
        entity.setCurrency(dto.getCurrency());

        entity.setQuotationStatus(dto.getQuotationStatus());
        if(dto.getItems()!=null){
            entity.setItems(dto.getItems().stream()
                    .map(itemDto->{
                        SalesQuotationItem item= new SalesQuotationItem();
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