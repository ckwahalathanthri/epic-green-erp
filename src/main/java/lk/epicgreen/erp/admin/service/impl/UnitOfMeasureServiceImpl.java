package lk.epicgreen.erp.admin.service.impl;

import lk.epicgreen.erp.admin.dto.request.UnitOfMeasureRequest;
import lk.epicgreen.erp.admin.dto.response.UnitOfMeasureResponse;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.mapper.UnitOfMeasureMapper;
import lk.epicgreen.erp.admin.repository.UnitOfMeasureRepository;
import lk.epicgreen.erp.admin.service.UnitOfMeasureService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UnitOfMeasureService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureMapper unitOfMeasureMapper;

    @Override
    @Transactional
    public UnitOfMeasureResponse createUnitOfMeasure(UnitOfMeasureRequest request) {
        log.info("Creating new unit of measure: {}", request.getUomCode());

        validateUniqueUomCode(request.getUomCode(), null);

        UnitOfMeasure unitOfMeasure = unitOfMeasureMapper.toEntity(request);
        unitOfMeasure.setIsActive(true);

        UnitOfMeasure savedUom = unitOfMeasureRepository.save(unitOfMeasure);
        log.info("Unit of measure created successfully: {}", savedUom.getUomCode());

        return unitOfMeasureMapper.toResponse(savedUom);
    }

    @Override
    @Transactional
    public UnitOfMeasureResponse updateUnitOfMeasure(Long id, UnitOfMeasureRequest request) {
        log.info("Updating unit of measure: {}", id);

        UnitOfMeasure unitOfMeasure = findUnitOfMeasureById(id);
        validateUniqueUomCode(request.getUomCode(), id);

        unitOfMeasureMapper.updateEntityFromRequest(request, unitOfMeasure);

        UnitOfMeasure updatedUom = unitOfMeasureRepository.save(unitOfMeasure);
        log.info("Unit of measure updated successfully: {}", updatedUom.getUomCode());

        return unitOfMeasureMapper.toResponse(updatedUom);
    }

    @Override
    @Transactional
    public void activateUnitOfMeasure(Long id) {
        log.info("Activating unit of measure: {}", id);

        UnitOfMeasure unitOfMeasure = findUnitOfMeasureById(id);
        unitOfMeasure.setIsActive(true);
        unitOfMeasureRepository.save(unitOfMeasure);

        log.info("Unit of measure activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateUnitOfMeasure(Long id) {
        log.info("Deactivating unit of measure: {}", id);

        UnitOfMeasure unitOfMeasure = findUnitOfMeasureById(id);
        unitOfMeasure.setIsActive(false);
        unitOfMeasureRepository.save(unitOfMeasure);

        log.info("Unit of measure deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteUnitOfMeasure(Long id) {
        log.info("Deleting unit of measure: {}", id);

        UnitOfMeasure unitOfMeasure = findUnitOfMeasureById(id);
        unitOfMeasureRepository.delete(unitOfMeasure);

        log.info("Unit of measure deleted successfully: {}", id);
    }

    @Override
    public UnitOfMeasureResponse getUnitOfMeasureById(Long id) {
        UnitOfMeasure unitOfMeasure = findUnitOfMeasureById(id);
        return unitOfMeasureMapper.toResponse(unitOfMeasure);
    }

    @Override
    public UnitOfMeasureResponse getUnitOfMeasureByCode(String uomCode) {
        UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByUomCode(uomCode)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of measure not found: " + uomCode));
        return unitOfMeasureMapper.toResponse(unitOfMeasure);
    }

    @Override
    public List<UnitOfMeasureResponse> getAllUnitsOfMeasure() {
        List<UnitOfMeasure> unitsOfMeasure = unitOfMeasureRepository.findAll();
        return unitsOfMeasure.stream()
            .map(unitOfMeasureMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<UnitOfMeasureResponse> getAllActiveUnitsOfMeasure() {
        List<UnitOfMeasure> unitsOfMeasure = unitOfMeasureRepository.findByIsActiveTrue();
        return unitsOfMeasure.stream()
            .map(unitOfMeasureMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<UnitOfMeasureResponse> getUnitsByType(String uomType) {
        List<UnitOfMeasure> unitsOfMeasure = unitOfMeasureRepository.findByUomType(uomType);
        return unitsOfMeasure.stream()
            .map(unitOfMeasureMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<UnitOfMeasureResponse> getBaseUnits() {
        List<UnitOfMeasure> unitsOfMeasure = unitOfMeasureRepository.findByBaseUnitTrue();
        return unitsOfMeasure.stream()
            .map(unitOfMeasureMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of measure not found: " + id));
    }

    private void validateUniqueUomCode(String uomCode, Long excludeId) {
        if (excludeId == null) {
            if (unitOfMeasureRepository.existsByUomCode(uomCode)) {
                throw new DuplicateResourceException("UOM code already exists: " + uomCode);
            }
        } else {
            if (unitOfMeasureRepository.existsByUomCodeAndIdNot(uomCode, excludeId)) {
                throw new DuplicateResourceException("UOM code already exists: " + uomCode);
            }
        }
    }
}
