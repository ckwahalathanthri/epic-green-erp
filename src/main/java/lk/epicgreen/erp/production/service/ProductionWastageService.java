package lk.epicgreen.erp.production.service;

import lk.epicgreen.erp.production.dto.request.ProductionWastageRequest;
import lk.epicgreen.erp.production.dto.response.ProductionWastageResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for ProductionWastage entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface ProductionWastageService {

    /**
     * Record production wastage
     */
    ProductionWastageResponse recordWastage(ProductionWastageRequest request);

    /**
     * Get production wastage by ID
     */
    ProductionWastageResponse getProductionWastageById(Long id);

    /**
     * Get all production wastage records (paginated)
     */
    PageResponse<ProductionWastageResponse> getAllProductionWastage(Pageable pageable);

    /**
     * Get production wastage by work order
     */
    List<ProductionWastageResponse> getWastageByWorkOrder(Long woId);

    /**
     * Get production wastage by product
     */
    List<ProductionWastageResponse> getWastageByProduct(Long productId);

    /**
     * Get production wastage by type
     */
    PageResponse<ProductionWastageResponse> getWastageByType(String wastageType, Pageable pageable);

    /**
     * Get production wastage by date range
     */
    List<ProductionWastageResponse> getWastageByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get total wastage quantity for a work order
     */
    BigDecimal getTotalWastageQuantityByWorkOrder(Long woId);

    /**
     * Get total wastage value for a work order
     */
    BigDecimal getTotalWastageValueByWorkOrder(Long woId);

    /**
     * Get wastage by type for a work order
     */
    BigDecimal getWastageQuantityByWoAndType(Long woId, String wastageType);

    /**
     * Search production wastage records
     */
    PageResponse<ProductionWastageResponse> searchProductionWastage(String keyword, Pageable pageable);
}
