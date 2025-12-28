package lk.epicgreen.erp.admin.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.admin.dto.UnitOfMeasureRequest;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.service.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Unit of Measure Controller
 * REST controller for UOM operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/uom")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UnitOfMeasureController {
    
    private final UnitOfMeasureService uomService;
    
    // ===================================================================
    // CRUD OPERATIONS
    // ===================================================================
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UnitOfMeasure>> createUnitOfMeasure(@Valid @RequestBody UnitOfMeasureRequest request) {
        log.info("Creating unit of measure: {}", request.getUomCode());
        UnitOfMeasure created = uomService.createUnitOfMeasure(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Unit of measure created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UnitOfMeasure>> updateUnitOfMeasure(
        @PathVariable Long id,
        @Valid @RequestBody UnitOfMeasureRequest request
    ) {
        log.info("Updating unit of measure: {}", id);
        UnitOfMeasure updated = uomService.updateUnitOfMeasure(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Unit of measure updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUnitOfMeasure(@PathVariable Long id) {
        log.info("Deleting unit of measure: {}", id);
        uomService.deleteUnitOfMeasure(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Unit of measure deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<UnitOfMeasure>> getUnitOfMeasureById(@PathVariable Long id) {
        UnitOfMeasure uom = uomService.getUnitOfMeasureById(id);
        return ResponseEntity.ok(ApiResponse.success(uom, "Unit of measure retrieved successfully"));
    }
    
    @GetMapping("/code/{uomCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<UnitOfMeasure>> getUnitOfMeasureByCode(@PathVariable String uomCode) {
        UnitOfMeasure uom = uomService.getUnitOfMeasureByCode(uomCode);
        return ResponseEntity.ok(ApiResponse.success(uom, "Unit of measure retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<UnitOfMeasure>>> getAllUnitsOfMeasure(Pageable pageable) {
        Page<UnitOfMeasure> uoms = uomService.getAllUnitsOfMeasure(pageable);
        return ResponseEntity.ok(ApiResponse.success(uoms, "Units of measure retrieved successfully"));
    }
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasure>>> getAllUnitsOfMeasureList() {
        List<UnitOfMeasure> uoms = uomService.getAllUnitsOfMeasure();
        return ResponseEntity.ok(ApiResponse.success(uoms, "Units of measure list retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<UnitOfMeasure>>> searchUnitsOfMeasure(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<UnitOfMeasure> uoms = uomService.searchUnitsOfMeasure(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(uoms, "Search results retrieved successfully"));
    }
    
    // ===================================================================
    // STATUS OPERATIONS
    // ===================================================================
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UnitOfMeasure>> activateUnitOfMeasure(@PathVariable Long id) {
        log.info("Activating unit of measure: {}", id);
        UnitOfMeasure activated = uomService.activateUnitOfMeasure(id);
        return ResponseEntity.ok(ApiResponse.success(activated, "Unit of measure activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<UnitOfMeasure>> deactivateUnitOfMeasure(@PathVariable Long id) {
        log.info("Deactivating unit of measure: {}", id);
        UnitOfMeasure deactivated = uomService.deactivateUnitOfMeasure(id);
        return ResponseEntity.ok(ApiResponse.success(deactivated, "Unit of measure deactivated successfully"));
    }
    
    // ===================================================================
    // QUERY OPERATIONS
    // ===================================================================
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasure>>> getActiveUnitsOfMeasure() {
        List<UnitOfMeasure> uoms = uomService.getActiveUnitsOfMeasure();
        return ResponseEntity.ok(ApiResponse.success(uoms, "Active units of measure retrieved successfully"));
    }
    
    @GetMapping("/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasure>>> getInactiveUnitsOfMeasure() {
        List<UnitOfMeasure> uoms = uomService.getInactiveUnitsOfMeasure();
        return ResponseEntity.ok(ApiResponse.success(uoms, "Inactive units of measure retrieved successfully"));
    }
    
    @GetMapping("/type/{uomType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasure>>> getUnitsByType(@PathVariable String uomType) {
        List<UnitOfMeasure> uoms = uomService.getUnitsByType(uomType);
        return ResponseEntity.ok(ApiResponse.success(uoms, "Units by type retrieved successfully"));
    }
    
    @GetMapping("/base")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasure>>> getBaseUnits() {
        List<UnitOfMeasure> uoms = uomService.getBaseUnits();
        return ResponseEntity.ok(ApiResponse.success(uoms, "Base units retrieved successfully"));
    }
    
    @GetMapping("/derived")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasure>>> getDerivedUnits() {
        List<UnitOfMeasure> uoms = uomService.getDerivedUnits();
        return ResponseEntity.ok(ApiResponse.success(uoms, "Derived units retrieved successfully"));
    }
    
    @GetMapping("/{baseUomId}/derived")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<List<UnitOfMeasure>>> getDerivedUnitsFromBase(@PathVariable Long baseUomId) {
        List<UnitOfMeasure> uoms = uomService.getDerivedUnitsFromBase(baseUomId);
        return ResponseEntity.ok(ApiResponse.success(uoms, "Derived units from base retrieved successfully"));
    }
    
    // ===================================================================
    // CONVERSION OPERATIONS
    // ===================================================================
    
    @GetMapping("/convert")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BigDecimal>> convertQuantity(
        @RequestParam Long fromUomId,
        @RequestParam Long toUomId,
        @RequestParam BigDecimal quantity
    ) {
        BigDecimal converted = uomService.convertQuantity(fromUomId, toUomId, quantity);
        return ResponseEntity.ok(ApiResponse.success(converted, "Quantity converted successfully"));
    }
    
    @GetMapping("/factor/{uomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<BigDecimal>> getConversionFactor(@PathVariable Long uomId) {
        BigDecimal factor = uomService.getConversionFactor(uomId);
        return ResponseEntity.ok(ApiResponse.success(factor, "Conversion factor retrieved successfully"));
    }
    
    // ===================================================================
    // STATISTICS
    // ===================================================================
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUnitOfMeasureStatistics() {
        Map<String, Object> statistics = uomService.getUnitOfMeasureStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Unit of measure statistics retrieved successfully"));
    }
    
    @GetMapping("/distribution/type")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTypeDistribution() {
        List<Map<String, Object>> distribution = uomService.getTypeDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution, "Type distribution retrieved successfully"));
    }
    
    // ===================================================================
    // VALIDATION
    // ===================================================================
    
    @GetMapping("/validate/code/{uomCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Boolean>> isUomCodeAvailable(@PathVariable String uomCode) {
        boolean available = uomService.isUomCodeAvailable(uomCode);
        return ResponseEntity.ok(ApiResponse.success(available, "UOM code availability checked"));
    }
    
    @GetMapping("/{id}/can-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> canDelete(@PathVariable Long id) {
        boolean canDelete = uomService.canDelete(id);
        return ResponseEntity.ok(ApiResponse.success(canDelete, "Delete check completed"));
    }
}
