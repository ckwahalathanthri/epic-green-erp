package lk.epicgreen.erp.production.repository;

import lk.epicgreen.erp.production.entity.ProductionWastage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for ProductionWastage entity
 * Based on ACTUAL database schema: production_wastage table
 * 
 * Fields: wo_id (BIGINT), wastage_date, product_id (BIGINT),
 *         wastage_type (ENUM: MATERIAL, PRODUCTION, QUALITY_REJECTION),
 *         quantity, uom_id (BIGINT), unit_cost, total_value, reason
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface ProductionWastageRepository extends JpaRepository<ProductionWastage, Long>, JpaSpecificationExecutor<ProductionWastage> {
    
    // ==================== FINDER METHODS ====================
    
    /**
     * Find all wastage for a work order
     */
    List<ProductionWastage> findByWoId(Long woId);
    
    /**
     * Find all wastage for a work order with pagination
     */
    Page<ProductionWastage> findByWoId(Long woId, Pageable pageable);
    
    /**
     * Find all wastage for a product
     */
    List<ProductionWastage> findByProductId(Long productId);
    
    /**
     * Find all wastage for a product with pagination
     */
    Page<ProductionWastage> findByProductId(Long productId, Pageable pageable);
    
    /**
     * Find wastage by wastage type
     */
    List<ProductionWastage> findByWastageType(String wastageType);
    
    /**
     * Find wastage by wastage type with pagination
     */
    Page<ProductionWastage> findByWastageType(String wastageType, Pageable pageable);
    
    /**
     * Find wastage by wastage date
     */
    List<ProductionWastage> findByWastageDate(LocalDate wastageDate);
    
    /**
     * Find wastage by wastage date range
     */
    List<ProductionWastage> findByWastageDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find wastage by wastage date range with pagination
     */
    Page<ProductionWastage> findByWastageDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find wastage by work order and wastage type
     */
    List<ProductionWastage> findByWoIdAndWastageType(Long woId, String wastageType);
    
    // ==================== COUNT METHODS ====================
    
    /**
     * Count wastage for a work order
     */
    long countByWoId(Long woId);
    
    /**
     * Count wastage for a product
     */
    long countByProductId(Long productId);
    
    /**
     * Count wastage by wastage type
     */
    long countByWastageType(String wastageType);
    
    /**
     * Count wastage in date range
     */
    long countByWastageDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ==================== CUSTOM QUERIES ====================
    
    /**
     * Find material wastage
     */
    @Query("SELECT pw FROM ProductionWastage pw WHERE pw.wastageType = 'MATERIAL' " +
           "ORDER BY pw.wastageDate DESC")
    List<ProductionWastage> findMaterialWastage();
    
    /**
     * Find production wastage
     */
    @Query("SELECT pw FROM ProductionWastage pw WHERE pw.wastageType = 'PRODUCTION' " +
           "ORDER BY pw.wastageDate DESC")
    List<ProductionWastage> findProductionWastage();
    
    /**
     * Find quality rejection wastage
     */
    @Query("SELECT pw FROM ProductionWastage pw WHERE pw.wastageType = 'QUALITY_REJECTION' " +
           "ORDER BY pw.wastageDate DESC")
    List<ProductionWastage> findQualityRejectionWastage();
    
    /**
     * Get total quantity wasted for a product
     */
    @Query("SELECT SUM(pw.quantity) FROM ProductionWastage pw WHERE pw.productId = :productId")
    BigDecimal getTotalQuantityWastedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total value wasted for a product
     */
    @Query("SELECT SUM(pw.totalValue) FROM ProductionWastage pw WHERE pw.productId = :productId")
    BigDecimal getTotalValueWastedByProduct(@Param("productId") Long productId);
    
    /**
     * Get total quantity wasted for a work order
     */
    @Query("SELECT SUM(pw.quantity) FROM ProductionWastage pw WHERE pw.woId = :woId")
    BigDecimal getTotalQuantityWastedByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get total value wasted for a work order
     */
    @Query("SELECT SUM(pw.totalValue) FROM ProductionWastage pw WHERE pw.woId = :woId")
    BigDecimal getTotalValueWastedByWorkOrder(@Param("woId") Long woId);
    
    /**
     * Get wastage statistics by product
     */
    @Query("SELECT pw.productId, COUNT(pw) as wastageCount, " +
           "SUM(pw.quantity) as totalQuantity, SUM(pw.totalValue) as totalValue " +
           "FROM ProductionWastage pw GROUP BY pw.productId ORDER BY totalValue DESC")
    List<Object[]> getWastageStatisticsByProduct();
    
    /**
     * Get wastage statistics by wastage type
     */
    @Query("SELECT pw.wastageType, COUNT(pw) as wastageCount, " +
           "SUM(pw.quantity) as totalQuantity, SUM(pw.totalValue) as totalValue " +
           "FROM ProductionWastage pw GROUP BY pw.wastageType ORDER BY totalValue DESC")
    List<Object[]> getWastageStatisticsByType();
    
    /**
     * Get daily wastage summary
     */
    @Query("SELECT pw.wastageDate, COUNT(pw) as wastageCount, " +
           "SUM(pw.quantity) as totalQuantity, SUM(pw.totalValue) as totalValue " +
           "FROM ProductionWastage pw WHERE pw.wastageDate BETWEEN :startDate AND :endDate " +
           "GROUP BY pw.wastageDate ORDER BY pw.wastageDate DESC")
    List<Object[]> getDailyWastageSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Get wastage by type and date range
     */
    @Query("SELECT pw.wastageType, SUM(pw.quantity) as totalQuantity, SUM(pw.totalValue) as totalValue " +
           "FROM ProductionWastage pw WHERE pw.wastageDate BETWEEN :startDate AND :endDate " +
           "GROUP BY pw.wastageType ORDER BY totalValue DESC")
    List<Object[]> getWastageByTypeInDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find today's wastage
     */
    @Query("SELECT pw FROM ProductionWastage pw WHERE pw.wastageDate = CURRENT_DATE " +
           "ORDER BY pw.createdAt DESC")
    List<ProductionWastage> findTodayWastage();
    
    /**
     * Get top wasted products
     */
    @Query("SELECT pw.productId, SUM(pw.quantity) as totalQuantity, SUM(pw.totalValue) as totalValue " +
           "FROM ProductionWastage pw GROUP BY pw.productId ORDER BY totalValue DESC")
    List<Object[]> getTopWastedProducts(Pageable pageable);
    
    /**
     * Get wastage trend by month
     */
    @Query("SELECT YEAR(pw.wastageDate), MONTH(pw.wastageDate), " +
           "SUM(pw.quantity) as totalQuantity, SUM(pw.totalValue) as totalValue " +
           "FROM ProductionWastage pw " +
           "WHERE pw.wastageDate BETWEEN :startDate AND :endDate " +
           "GROUP BY YEAR(pw.wastageDate), MONTH(pw.wastageDate) " +
           "ORDER BY YEAR(pw.wastageDate) DESC, MONTH(pw.wastageDate) DESC")
    List<Object[]> getMonthlyWastageTrend(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    /**
     * Get wastage percentage by product
     */
    @Query("SELECT pw.productId, pw.wastageType, " +
           "SUM(pw.quantity) as totalWasted " +
           "FROM ProductionWastage pw " +
           "GROUP BY pw.productId, pw.wastageType ORDER BY totalWasted DESC")
    List<Object[]> getWastageBreakdownByProduct();
    
    /**
     * Find all wastage ordered by work order
     */
    @Query("SELECT pw FROM ProductionWastage pw ORDER BY pw.woId, pw.wastageDate DESC")
    List<ProductionWastage> findAllOrderedByWorkOrder();
    
    /**
     * Find high value wastage
     */
    @Query("SELECT pw FROM ProductionWastage pw WHERE pw.totalValue >= :threshold " +
           "ORDER BY pw.totalValue DESC")
    List<ProductionWastage> findHighValueWastage(@Param("threshold") BigDecimal threshold);
}
