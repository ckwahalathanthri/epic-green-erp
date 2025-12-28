package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.request.BillOfMaterialsRequest;
import lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for BillOfMaterials entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface BillOfMaterialsService {

    /**
     * Create a new Bill of Materials
     */
    BillOfMaterialsResponse createBillOfMaterials(BillOfMaterialsRequest request);

    /**
     * Update an existing Bill of Materials
     */
    BillOfMaterialsResponse updateBillOfMaterials(Long id, BillOfMaterialsRequest request);

    /**
     * Activate a Bill of Materials
     */
    void activateBillOfMaterials(Long id);

    /**
     * Deactivate a Bill of Materials
     */
    void deactivateBillOfMaterials(Long id);

    /**
     * Delete a Bill of Materials (only if not used in any work orders)
     */
    void deleteBillOfMaterials(Long id);

    /**
     * Get Bill of Materials by ID
     */
    BillOfMaterialsResponse getBillOfMaterialsById(Long id);

    /**
     * Get Bill of Materials by code
     */
    BillOfMaterialsResponse getBillOfMaterialsByCode(String bomCode);

    /**
     * Get all Bills of Materials (paginated)
     */
    PageResponse<BillOfMaterialsResponse> getAllBillOfMaterials(Pageable pageable);

    /**
     * Get all active Bills of Materials
     */
    List<BillOfMaterialsResponse> getAllActiveBillOfMaterials();

    /**
     * Get Bills of Materials by finished product
     */
    List<BillOfMaterialsResponse> getBillOfMaterialsByFinishedProduct(Long finishedProductId);

    /**
     * Get active BOM by finished product
     */
    BillOfMaterialsResponse getActiveBomByFinishedProduct(Long finishedProductId);

    /**
     * Get valid BOMs for a specific date
     */
    List<BillOfMaterialsResponse> getValidBillOfMaterialsForDate(LocalDate date);

    /**
     * Get BOM by finished product and date
     */
    BillOfMaterialsResponse getBomByFinishedProductAndDate(Long finishedProductId, LocalDate date);

    /**
     * Search Bills of Materials by keyword
     */
    PageResponse<BillOfMaterialsResponse> searchBillOfMaterials(String keyword, Pageable pageable);

    /**
     * Calculate total material cost for BOM
     */
    BigDecimal calculateTotalMaterialCost(Long bomId);

    /**
     * Calculate total cost for BOM (material + labor + overhead)
     */
    BigDecimal calculateTotalCost(Long bomId);

    /**
     * Check if BOM can be deleted (not used in any work orders)
     */
    boolean canDelete(Long bomId);

    /**
     * Get BOMs expiring soon (effective_to within specified days)
     */
    List<BillOfMaterialsResponse> getExpiringSoon(Integer daysBeforeExpiry);
}
