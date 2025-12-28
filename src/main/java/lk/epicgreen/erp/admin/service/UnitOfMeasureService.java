package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.request.UnitOfMeasureRequest;
import lk.epicgreen.erp.admin.dto.response.UnitOfMeasureResponse;

import java.util.List;

/**
 * Service interface for UnitOfMeasure entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface UnitOfMeasureService {

    /**
     * Create new unit of measure
     */
    UnitOfMeasureResponse createUnitOfMeasure(UnitOfMeasureRequest request);

    /**
     * Update existing unit of measure
     */
    UnitOfMeasureResponse updateUnitOfMeasure(Long id, UnitOfMeasureRequest request);

    /**
     * Activate unit of measure
     */
    void activateUnitOfMeasure(Long id);

    /**
     * Deactivate unit of measure
     */
    void deactivateUnitOfMeasure(Long id);

    /**
     * Delete unit of measure
     */
    void deleteUnitOfMeasure(Long id);

    /**
     * Get unit of measure by ID
     */
    UnitOfMeasureResponse getUnitOfMeasureById(Long id);

    /**
     * Get unit of measure by code
     */
    UnitOfMeasureResponse getUnitOfMeasureByCode(String uomCode);

    /**
     * Get all units of measure
     */
    List<UnitOfMeasureResponse> getAllUnitsOfMeasure();

    /**
     * Get all active units of measure
     */
    List<UnitOfMeasureResponse> getAllActiveUnitsOfMeasure();

    /**
     * Get units of measure by type
     */
    List<UnitOfMeasureResponse> getUnitsByType(String uomType);

    /**
     * Get base units of measure
     */
    List<UnitOfMeasureResponse> getBaseUnits();
}
