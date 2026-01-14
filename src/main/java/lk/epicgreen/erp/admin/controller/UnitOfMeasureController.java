package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.request.UnitOfMeasureRequest;
import lk.epicgreen.erp.admin.dto.response.UnitOfMeasureResponse;
import lk.epicgreen.erp.admin.service.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * Unit of Measure Controller
 * REST controller for unit of measure management
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/units-of-measure")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UnitOfMeasureController {
    
    private final UnitOfMeasureService unitOfMeasureService;
    
    // Create Unit of Measure
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UnitOfMeasureResponse>> createUnitOfMeasure(
        @Valid @RequestBody UnitOfMeasureRequest request
    ) {
        log.info("Creating unit of measure: {}", request.getUomCode());
        UnitOfMeasureResponse response = unitOfMeasureService.createUnitOfMeasure(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Unit of measure created successfully"));
    }
    
    // Update Unit of Measure
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UnitOfMeasureResponse>> updateUnitOfMeasure(
        @PathVariable Long id,
        @Valid @RequestBody UnitOfMeasureRequest request
    ) {
        log.info("Updating unit of measure: {}", id);
        UnitOfMeasureResponse response = unitOfMeasureService.updateUnitOfMeasure(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Unit of measure updated successfully"));
    }
    
    // Activate Unit of Measure
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> activateUnitOfMeasure(@PathVariable Long id) {
        log.info("Activating unit of measure: {}", id);
        unitOfMeasureService.activateUnitOfMeasure(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Unit of measure activated successfully"));
    }
    
    // Deactivate Unit of Measure
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deactivateUnitOfMeasure(@PathVariable Long id) {
        log.info("Deactivating unit of measure: {}", id);
        unitOfMeasureService.deactivateUnitOfMeasure(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Unit of measure deactivated successfully"));
    }
    
    // Delete Unit of Measure
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUnitOfMeasure(@PathVariable Long id) {
        log.info("Deleting unit of measure: {}", id);
        unitOfMeasureService.deleteUnitOfMeasure(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Unit of measure deleted successfully"));
    }
    
    // Get Unit of Measure by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<UnitOfMeasureResponse>> getUnitOfMeasureById(@PathVariable Long id) {
        UnitOfMeasureResponse response = unitOfMeasureService.getUnitOfMeasureById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Unit of measure retrieved successfully"));
    }
    
    // Get Unit of Measure by Code
    @GetMapping("/code/{uomCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<UnitOfMeasureResponse>> getUnitOfMeasureByCode(@PathVariable String uomCode) {
        UnitOfMeasureResponse response = unitOfMeasureService.getUnitOfMeasureByCode(uomCode);
        return ResponseEntity.ok(ApiResponse.success(response, "Unit of measure retrieved successfully"));
    }
    
    // Get All Units of Measure
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasureResponse>>> getAllUnitsOfMeasure() {
        List<UnitOfMeasureResponse> response = unitOfMeasureService.getAllUnitsOfMeasure();
        return ResponseEntity.ok(ApiResponse.success(response, "Units of measure retrieved successfully"));
    }
    
    // Get All Active Units of Measure
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasureResponse>>> getAllActiveUnitsOfMeasure() {
        List<UnitOfMeasureResponse> response = unitOfMeasureService.getAllActiveUnitsOfMeasure();
        return ResponseEntity.ok(ApiResponse.success(response, "Active units of measure retrieved successfully"));
    }
    
    // Get Units of Measure by Type
    @GetMapping("/type/{uomType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasureResponse>>> getUnitsByType(@PathVariable String uomType) {
        List<UnitOfMeasureResponse> response = unitOfMeasureService.getUnitsByType(uomType);
        return ResponseEntity.ok(ApiResponse.success(response, "Units of measure retrieved successfully"));
    }
    
    // Get Base Units
    @GetMapping("/base")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasureResponse>>> getBaseUnits() {
        List<UnitOfMeasureResponse> response = unitOfMeasureService.getBaseUnits();
        return ResponseEntity.ok(ApiResponse.success(response, "Base units retrieved successfully"));
    }
}
