package lk.epicgreen.erp.admin.mapper;

import lk.epicgreen.erp.admin.dto.request.UnitOfMeasureRequest;
import lk.epicgreen.erp.admin.dto.response.UnitOfMeasureResponse;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import org.springframework.stereotype.Component;

/**
 * Mapper for UnitOfMeasure entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class UnitOfMeasureMapper {

    public UnitOfMeasure toEntity(UnitOfMeasureRequest request) {
        if (request == null) {
            return null;
        }

        return UnitOfMeasure.builder()
            .uomCode(request.getUomCode())
            .uomName(request.getUomName())
            .uomType(request.getUomType())
            .description(request.getDescription())
            .baseUnit(request.getBaseUnit() != null ? request.getBaseUnit() : false)
            .conversionFactor(request.getConversionFactor())
            .build();
    }

    public void updateEntityFromRequest(UnitOfMeasureRequest request, UnitOfMeasure unitOfMeasure) {
        if (request == null || unitOfMeasure == null) {
            return;
        }

        unitOfMeasure.setUomCode(request.getUomCode());
        unitOfMeasure.setUomName(request.getUomName());
        unitOfMeasure.setUomType(request.getUomType());
        unitOfMeasure.setDescription(request.getDescription());
        unitOfMeasure.setConversionFactor(request.getConversionFactor());
        
        if (request.getBaseUnit() != null) {
            unitOfMeasure.setBaseUnit(request.getBaseUnit());
        }
    }

    public UnitOfMeasureResponse toResponse(UnitOfMeasure unitOfMeasure) {
        if (unitOfMeasure == null) {
            return null;
        }

        return UnitOfMeasureResponse.builder()
            .id(unitOfMeasure.getId())
            .uomCode(unitOfMeasure.getUomCode())
            .uomName(unitOfMeasure.getUomName())
            .uomType(unitOfMeasure.getUomType())
            .description(unitOfMeasure.getDescription())
            .baseUnit(unitOfMeasure.getBaseUnit())
            .conversionFactor(unitOfMeasure.getConversionFactor())
            .baseUnitId(unitOfMeasure.getBaseUnitReference() != null ? 
                unitOfMeasure.getBaseUnitReference().getId() : null)
            .isActive(unitOfMeasure.getIsActive())
            .createdAt(unitOfMeasure.getCreatedAt())
            .updatedAt(unitOfMeasure.getUpdatedAt())
            .build();
    }
}
