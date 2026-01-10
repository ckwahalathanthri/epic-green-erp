package lk.epicgreen.erp.mobile.repository;

import lk.epicgreen.erp.mobile.entity.MobileDataCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MobileDataCache entity
 * Based on ACTUAL database schema: mobile_data_cache table
 *
 * Fields: user_id (BIGINT), cache_key,
 *         cache_type (ENUM: CUSTOMER, PRODUCT, PRICELIST, STOCK, ORDER, PAYMENT, OTHER),
 *         data_snapshot (JSON), last_synced_at, expires_at
 *
 * NOTE: Unique constraint on (user_id, cache_key)
 *
 * @author Epic Green Development Team
 * @version 1.0
 */
@Repository
public interface MobileDataCacheRepository extends JpaRepository<MobileDataCache, Long>, JpaSpecificationExecutor<MobileDataCache> {

    // ==================== FINDER METHODS ====================

    /**
     * Find cache by user and cache key
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.user.id = :userId AND mdc.cacheKey = :cacheKey")
    Optional<MobileDataCache> findByUserIdAndCacheKey(@Param("userId") Long userId, @Param("cacheKey") String cacheKey);

    /**
     * Find all cache entries for a user
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.user.id = :userId")
    List<MobileDataCache> findByUserId(@Param("userId") Long userId);

    /**
     * Find all cache entries for a user with pagination
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.user.id = :userId")
    Page<MobileDataCache> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find cache entries by cache type
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.cacheType = :cacheType")
    List<MobileDataCache> findByCacheType(@Param("cacheType") String cacheType);

    /**
     * Find cache entries by cache type with pagination
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.cacheType = :cacheType")
    Page<MobileDataCache> findByCacheType(@Param("cacheType") String cacheType, Pageable pageable);

    @Modifying
    @Query("DELETE FROM MobileDataCache mdc WHERE mdc.user.id = :userId")
    long deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM MobileDataCache mdc WHERE mdc.expiresAt < :dateTime")
    long deleteByExpiresAtBefore(@Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.expiresAt < :dateTime")
    List<MobileDataCache> findByExpiresAtBefore(@Param("dateTime") LocalDateTime dateTime);

    /**
     * Find cache entries by user and cache type
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.user.id = :userId AND mdc.cacheType = :cacheType")
    List<MobileDataCache> findByUserIdAndCacheType(@Param("userId") Long userId, @Param("cacheType") String cacheType);

    // ==================== EXISTENCE CHECKS ====================

    /**
     * Check if cache entry exists for user and cache key
     */
    @Query("SELECT CASE WHEN COUNT(mdc) > 0 THEN true ELSE false END FROM MobileDataCache mdc WHERE mdc.user.id = :userId AND mdc.cacheKey = :cacheKey")
    boolean existsByUserIdAndCacheKey(@Param("userId") Long userId, @Param("cacheKey") String cacheKey);

    // ==================== COUNT METHODS ====================

    /**
     * Count cache entries by user
     */
    @Query("SELECT COUNT(mdc) FROM MobileDataCache mdc WHERE mdc.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    /**
     * Count cache entries by cache type
     */
    @Query("SELECT COUNT(mdc) FROM MobileDataCache mdc WHERE mdc.cacheType = :cacheType")
    long countByCacheType(@Param("cacheType") String cacheType);

    // ==================== DELETE METHODS ====================

    /**
     * Delete cache entry by user and cache key
     */
    @Modifying
    @Query("DELETE FROM MobileDataCache mdc WHERE mdc.user.id = :userId AND mdc.cacheKey = :cacheKey")
    void deleteByUserIdAndCacheKey(@Param("userId") Long userId, @Param("cacheKey") String cacheKey);

    /**
     * Delete all cache entries for a user
     */
    @Modifying
    @Query("DELETE FROM MobileDataCache mdc WHERE mdc.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    /**
     * Delete expired cache entries
     */
    @Modifying
    @Query("DELETE FROM MobileDataCache mdc WHERE mdc.expiresAt < CURRENT_TIMESTAMP")
    void deleteExpiredCache();

    /**
     * Delete cache entries older than specified date
     */
    @Modifying
    @Query("DELETE FROM MobileDataCache mdc WHERE mdc.lastSyncedAt < :cutoffDate")
    void deleteCacheOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);

    // ==================== CUSTOM QUERIES ====================

    @Query("SELECT mdc FROM MobileDataCache mdc WHERE " +
            "CAST(mdc.user.id AS string) LIKE %:keyword% OR " +
            "mdc.cacheKey LIKE %:keyword% OR " +
            "mdc.cacheType LIKE %:keyword%")
    Page<MobileDataCache> searchCaches(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Find customer cache entries
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.cacheType = 'CUSTOMER' " +
            "ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findCustomerCache();

    /**
     * Find product cache entries
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.cacheType = 'PRODUCT' " +
            "ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findProductCache();

    /**
     * Find pricelist cache entries
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.cacheType = 'PRICELIST' " +
            "ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findPricelistCache();

    /**
     * Find stock cache entries
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.cacheType = 'STOCK' " +
            "ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findStockCache();

    /**
     * Find order cache entries
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.cacheType = 'ORDER' " +
            "ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findOrderCache();

    /**
     * Find payment cache entries
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.cacheType = 'PAYMENT' " +
            "ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findPaymentCache();

    /**
     * Find expired cache entries
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.expiresAt < CURRENT_TIMESTAMP " +
            "ORDER BY mdc.expiresAt")
    List<MobileDataCache> findExpiredCache();

    /**
     * Find cache entries expiring soon
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.expiresAt BETWEEN CURRENT_TIMESTAMP AND :futureTime " +
            "ORDER BY mdc.expiresAt")
    List<MobileDataCache> findCacheExpiringSoon(@Param("futureTime") LocalDateTime futureTime);

    /**
     * Find valid (not expired) cache entries for user
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.user.id = :userId " +
            "AND (mdc.expiresAt IS NULL OR mdc.expiresAt >= CURRENT_TIMESTAMP) " +
            "ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findValidCacheByUser(@Param("userId") Long userId);

    /**
     * Find stale cache entries (not synced recently)
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.lastSyncedAt < :cutoffTime " +
            "ORDER BY mdc.lastSyncedAt")
    List<MobileDataCache> findStaleCache(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Get cache statistics
     */
    @Query("SELECT " +
            "COUNT(mdc) as totalCacheEntries, " +
            "COUNT(DISTINCT mdc.user.id) as uniqueUsers, " +
            "SUM(CASE WHEN mdc.expiresAt IS NULL OR mdc.expiresAt >= CURRENT_TIMESTAMP THEN 1 ELSE 0 END) as validEntries, " +
            "SUM(CASE WHEN mdc.expiresAt < CURRENT_TIMESTAMP THEN 1 ELSE 0 END) as expiredEntries " +
            "FROM MobileDataCache mdc")
    Object getCacheStatistics();

    /**
     * Get cache entries grouped by cache type
     */
    @Query("SELECT mdc.cacheType, COUNT(mdc) as entryCount " +
            "FROM MobileDataCache mdc GROUP BY mdc.cacheType ORDER BY entryCount DESC")
    List<Object[]> getCacheByType();

    /**
     * Get cache entries grouped by user
     */
    @Query("SELECT mdc.user.id, COUNT(mdc) as entryCount " +
            "FROM MobileDataCache mdc GROUP BY mdc.user.id ORDER BY entryCount DESC")
    List<Object[]> getCacheByUser();

    /**
     * Find all cache entries ordered by last synced at
     */
    @Query("SELECT mdc FROM MobileDataCache mdc ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findAllByOrderByLastSyncedAtDesc();

    /**
     * Find recently synced cache entries
     */
    @Query("SELECT mdc FROM MobileDataCache mdc WHERE mdc.lastSyncedAt >= :sinceTime " +
            "ORDER BY mdc.lastSyncedAt DESC")
    List<MobileDataCache> findRecentlySyncedCache(@Param("sinceTime") LocalDateTime sinceTime);
}