package lk.epicgreen.erp.sales.mapper;


import lk.epicgreen.erp.sales.dto.response.SalesQuotationDTO;
import lk.epicgreen.erp.sales.entity.SalesQuotation;
import org.springframework.stereotype.Component;

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
        entity.setQuotationStatus(dto.getQuotationStatus());
        return entity;
    }
    
    public void updateEntityFromDTO(SalesQuotationDTO dto, SalesQuotation entity) {
        if (dto == null || entity == null) return;
        entity.setCustomerName(dto.getCustomerName());
        entity.setQuotationStatus(dto.getQuotationStatus());
    }
}