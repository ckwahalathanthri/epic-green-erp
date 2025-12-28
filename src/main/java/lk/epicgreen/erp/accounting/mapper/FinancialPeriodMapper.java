package lk.epicgreen.erp.accounting.mapper;

import lk.epicgreen.erp.accounting.dto.request.FinancialPeriodRequest;
import lk.epicgreen.erp.accounting.dto.response.FinancialPeriodResponse;
import lk.epicgreen.erp.accounting.entity.FinancialPeriod;
import org.springframework.stereotype.Component;

@Component
public class FinancialPeriodMapper {

    public FinancialPeriod toEntity(FinancialPeriodRequest request) {
        if (request == null) return null;

        return FinancialPeriod.builder()
            .periodCode(request.getPeriodCode())
            .periodName(request.getPeriodName())
            .periodType(request.getPeriodType())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .fiscalYear(request.getFiscalYear())
            .isClosed(request.getIsClosed() != null ? request.getIsClosed() : false)
            .build();
    }

    public void updateEntityFromRequest(FinancialPeriodRequest request, FinancialPeriod period) {
        if (request == null || period == null) return;

        period.setPeriodCode(request.getPeriodCode());
        period.setPeriodName(request.getPeriodName());
        period.setPeriodType(request.getPeriodType());
        period.setStartDate(request.getStartDate());
        period.setEndDate(request.getEndDate());
        period.setFiscalYear(request.getFiscalYear());
        period.setIsClosed(request.getIsClosed());
    }

    public FinancialPeriodResponse toResponse(FinancialPeriod period) {
        if (period == null) return null;

        return FinancialPeriodResponse.builder()
            .id(period.getId())
            .periodCode(period.getPeriodCode())
            .periodName(period.getPeriodName())
            .periodType(period.getPeriodType())
            .startDate(period.getStartDate())
            .endDate(period.getEndDate())
            .fiscalYear(period.getFiscalYear())
            .isClosed(period.getIsClosed())
            .closedBy(period.getClosedBy())
            .closedAt(period.getClosedAt())
            .createdAt(period.getCreatedAt())
            .createdBy(period.getCreatedBy())
            .build();
    }
}
