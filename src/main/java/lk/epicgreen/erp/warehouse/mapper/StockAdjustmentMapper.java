package lk.epicgreen.erp.warehouse.mapper;

import lk.epicgreen.erp.warehouse.dto.request.StockAdjustmentRequest;
import lk.epicgreen.erp.warehouse.dto.response.StockAdjustmentResponse;
import lk.epicgreen.erp.warehouse.entity.StockAdjustment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper for StockAdjustment entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class StockAdjustmentMapper {

    private final StockAdjustmentItemMapper itemMapper;

    public StockAdjustmentMapper(StockAdjustmentItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public StockAdjustment toEntity(StockAdjustmentRequest request) {
        if (request == null) {
            return null;
        }

        return StockAdjustment.builder()
            .adjustmentNumber(request.getAdjustmentNumber())
            .adjustmentDate(request.getAdjustmentDate())
            .adjustmentType(request.getAdjustmentType())
            .status("DRAFT")
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(StockAdjustmentRequest request, StockAdjustment adjustment) {
        if (request == null || adjustment == null) {
            return;
        }

        adjustment.setAdjustmentNumber(request.getAdjustmentNumber());
        adjustment.setAdjustmentDate(request.getAdjustmentDate());
        adjustment.setAdjustmentType(request.getAdjustmentType());
        adjustment.setRemarks(request.getRemarks());
    }

    public StockAdjustmentResponse toResponse(StockAdjustment adjustment) {
        if (adjustment == null) {
            return null;
        }

        return StockAdjustmentResponse.builder()
            .id(adjustment.getId())
            .adjustmentNumber(adjustment.getAdjustmentNumber())
            .adjustmentDate(adjustment.getAdjustmentDate())
            .warehouseId(adjustment.getWarehouse() != null ? adjustment.getWarehouse().getId() : null)
            .warehouseCode(adjustment.getWarehouse() != null ? adjustment.getWarehouse().getWarehouseCode() : null)
            .warehouseName(adjustment.getWarehouse() != null ? adjustment.getWarehouse().getWarehouseName() : null)
            .adjustmentType(adjustment.getAdjustmentType())
            .status(adjustment.getStatus())
            .approvedBy(adjustment.getApprovedBy() != null ? adjustment.getApprovedBy().getId() : null)
            .approvedByName(adjustment.getApprovedBy() != null ? adjustment.getApprovedBy().getUsername() : null)
            .approvedAt(adjustment.getApprovedAt())
            .remarks(adjustment.getRemarks())
            .createdAt(adjustment.getCreatedAt())
            .createdBy(adjustment.getCreatedBy())
            .updatedAt(adjustment.getUpdatedAt())
            .items(adjustment.getItems() != null ? 
                adjustment.getItems().stream()
                    .map(itemMapper::toResponse)
                    .collect(Collectors.toList()) : null)
            .build();
    }
}
