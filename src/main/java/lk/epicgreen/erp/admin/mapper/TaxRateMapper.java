package lk.epicgreen.erp.admin.mapper;

import lk.epicgreen.erp.admin.dto.request.TaxRateRequest;
import lk.epicgreen.erp.admin.dto.response.TaxRateResponse;
import lk.epicgreen.erp.admin.entity.TaxRate;
import org.springframework.stereotype.Component;

@Component
public class TaxRateMapper {

    public TaxRate toEntity(TaxRateRequest request) {
        if (request == null) {
            return null;
        }

        return TaxRate.builder()
            .taxCode(request.getTaxCode())
            .taxName(request.getTaxName())
            .taxPercentage(request.getTaxPercentage())
            .taxType(request.getTaxType())
            .applicableFrom(request.getApplicableFrom())
            .applicableTo(request.getApplicableTo())
            .build();
    }

    public void updateEntityFromRequest(TaxRateRequest request, TaxRate taxRate) {
        if (request == null || taxRate == null) {
            return;
        }

        taxRate.setTaxCode(request.getTaxCode());
        taxRate.setTaxName(request.getTaxName());
        taxRate.setTaxPercentage(request.getTaxPercentage());
        taxRate.setTaxType(request.getTaxType());
        taxRate.setApplicableFrom(request.getApplicableFrom());
        taxRate.setApplicableTo(request.getApplicableTo());
    }

    public TaxRateResponse toResponse(TaxRate taxRate) {
        if (taxRate == null) {
            return null;
        }

        return TaxRateResponse.builder()
            .id(taxRate.getId())
            .taxCode(taxRate.getTaxCode())
            .taxName(taxRate.getTaxName())
            .taxPercentage(taxRate.getTaxPercentage())
            .taxType(taxRate.getTaxType())
            .applicableFrom(taxRate.getApplicableFrom())
            .applicableTo(taxRate.getApplicableTo())
            .isActive(taxRate.getIsActive())
            .createdAt(taxRate.getCreatedAt())
            .updatedAt(taxRate.getUpdatedAt())
            .build();
    }
}
