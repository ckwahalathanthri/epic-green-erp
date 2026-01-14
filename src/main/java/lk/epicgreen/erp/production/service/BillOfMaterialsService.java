package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.request.BillOfMaterialsRequest;
import lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse;
import lk.epicgreen.erp.common.dto.PageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    BillOfMaterialsResponse createBom(BillOfMaterialsRequest request);

    BillOfMaterialsResponse updateBom(Long id, BillOfMaterialsRequest request);

	void deleteBom(Long id);

	BillOfMaterialsResponse getBomById(Long id);

	BillOfMaterialsResponse getBomByCode(String bomCode);

	Page<BillOfMaterialsResponse> getAllBoms(Pageable pageable);

	List<BillOfMaterialsResponse> getAllBoms();

	Page<BillOfMaterialsResponse> searchBoms(String keyword, Pageable pageable);

	BillOfMaterialsResponse approveBom(Long id, Long approvedByUserId, String approvalNotes);

	BillOfMaterialsResponse activateBom(Long id);

	BillOfMaterialsResponse deactivateBom(Long id);

	BillOfMaterialsResponse markAsObsolete(Long id, String obsoleteReason);

	BillOfMaterialsResponse setAsDefault(Long productId, Long bomId);

	BillOfMaterialsResponse createNewVersion(Long bomId, String newVersion);

	List<BillOfMaterialsResponse> getBomVersions(Long productId);

	BillOfMaterialsResponse getLatestBomVersion(Long productId);

	List<BillOfMaterialsResponse> getActiveBoms();

	List<BillOfMaterialsResponse> getDraftBoms();

	List<BillOfMaterialsResponse> getApprovedBoms();

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getObsoleteBoms();

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getBomsPendingApproval();

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getActiveDefaultBoms();

	lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse getProductActiveBom(Long productId);

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getProductBoms(Long productId);

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getEffectiveBoms();

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getExpiredBoms();

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getExpiringSoonBoms(int days);

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getBomsRequiringAction();

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getBomsByType(String bomType);

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> getRecentBoms(int limit);

	boolean canApproveBom(Long id);

	boolean canActivateBom(Long id);

	boolean canMarkAsObsolete(Long id);

	boolean isBomCodeAvailable(String bomCode);

	List<lk.epicgreen.erp.production.dto.response.BillOfMaterialsResponse> createBulkBoms(
			List<BillOfMaterialsRequest> requests);

	int approveBulkBoms(List<Long> bomIds, Long approvedByUserId);

	int deleteBulkBoms(List<Long> bomIds);

	Map<String, Object> getBomStatistics();

	List<Map<String, Object>> getBomTypeDistribution();

	List<Map<String, Object>> getStatusDistribution();

	List<Map<String, Object>> getMonthlyBomCreationCount(LocalDateTime startDate, LocalDateTime endDate);

	List<Map<String, Object>> getProductsWithBoms();

	Map<String, Object> getDashboardStatistics();
}
