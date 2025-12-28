package lk.epicgreen.erp.production.mapper;

import lk.epicgreen.erp.production.dto.request.ProductionOutputRequest;
import lk.epicgreen.erp.production.dto.response.ProductionOutputResponse;
import lk.epicgreen.erp.production.entity.ProductionOutput;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for ProductionOutput entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ProductionOutputMapper {

    public ProductionOutput toEntity(ProductionOutputRequest request) {
        if (request == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (request.getUnitCost() != null && request.getQuantityAccepted() != null) {
            totalCost = request.getUnitCost().multiply(request.getQuantityAccepted());
        }

        return ProductionOutput.builder()
            .outputDate(request.getOutputDate())
            .batchNumber(request.getBatchNumber())
            .quantityProduced(request.getQuantityProduced())
            .quantityAccepted(request.getQuantityAccepted())
            .quantityRejected(request.getQuantityRejected() != null ? request.getQuantityRejected() : BigDecimal.ZERO)
            .quantityRework(request.getQuantityRework() != null ? request.getQuantityRework() : BigDecimal.ZERO)
            .manufacturingDate(request.getManufacturingDate())
            .expiryDate(request.getExpiryDate())
            .unitCost(request.getUnitCost())
            .totalCost(totalCost)
            .qualityStatus(request.getQualityStatus() != null ? request.getQualityStatus() : "PENDING")
            .qualityCheckedBy(request.getQualityCheckedBy())
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(ProductionOutputRequest request, ProductionOutput output) {
        if (request == null || output == null) {
            return;
        }

        output.setOutputDate(request.getOutputDate());
        output.setBatchNumber(request.getBatchNumber());
        output.setQuantityProduced(request.getQuantityProduced());
        output.setQuantityAccepted(request.getQuantityAccepted());
        output.setQuantityRejected(request.getQuantityRejected());
        output.setQuantityRework(request.getQuantityRework());
        output.setManufacturingDate(request.getManufacturingDate());
        output.setExpiryDate(request.getExpiryDate());
        output.setUnitCost(request.getUnitCost());
        
        if (request.getUnitCost() != null && request.getQuantityAccepted() != null) {
            output.setTotalCost(request.getUnitCost().multiply(request.getQuantityAccepted()));
        }
        
        if (request.getQualityStatus() != null) {
            output.setQualityStatus(request.getQualityStatus());
        }
        
        output.setQualityCheckedBy(request.getQualityCheckedBy());
        output.setRemarks(request.getRemarks());
    }

    public ProductionOutputResponse toResponse(ProductionOutput output) {
        if (output == null) {
            return null;
        }

        BigDecimal totalCost = null;
        if (output.getUnitCost() != null && output.getQuantityAccepted() != null) {
            totalCost = output.getUnitCost().multiply(output.getQuantityAccepted());
        }

        return ProductionOutputResponse.builder()
            .id(output.getId())
            .woId(output.getWorkOrder() != null ? output.getWorkOrder().getId() : null)
            .woNumber(output.getWorkOrder() != null ? output.getWorkOrder().getWoNumber() : null)
            .outputDate(output.getOutputDate())
            .finishedProductId(output.getFinishedProduct() != null ? output.getFinishedProduct().getId() : null)
            .finishedProductCode(output.getFinishedProduct() != null ? output.getFinishedProduct().getProductCode() : null)
            .finishedProductName(output.getFinishedProduct() != null ? output.getFinishedProduct().getProductName() : null)
            .batchNumber(output.getBatchNumber())
            .quantityProduced(output.getQuantityProduced())
            .quantityAccepted(output.getQuantityAccepted())
            .quantityRejected(output.getQuantityRejected())
            .quantityRework(output.getQuantityRework())
            .uomId(output.getUom() != null ? output.getUom().getId() : null)
            .uomCode(output.getUom() != null ? output.getUom().getUomCode() : null)
            .uomName(output.getUom() != null ? output.getUom().getUomName() : null)
            .manufacturingDate(output.getManufacturingDate())
            .expiryDate(output.getExpiryDate())
            .warehouseId(output.getWarehouse() != null ? output.getWarehouse().getId() : null)
            .warehouseCode(output.getWarehouse() != null ? output.getWarehouse().getWarehouseCode() : null)
            .warehouseName(output.getWarehouse() != null ? output.getWarehouse().getWarehouseName() : null)
            .locationId(output.getLocation() != null ? output.getLocation().getId() : null)
            .locationCode(output.getLocation() != null ? output.getLocation().getLocationCode() : null)
            .unitCost(output.getUnitCost())
            .totalCost(totalCost)
            .qualityStatus(output.getQualityStatus())
            .qualityCheckedBy(output.getQualityCheckedBy())
            .qualityCheckedAt(output.getQualityCheckedAt())
            .remarks(output.getRemarks())
            .createdAt(output.getCreatedAt())
            .createdBy(output.getCreatedBy())
            .build();
    }
}
