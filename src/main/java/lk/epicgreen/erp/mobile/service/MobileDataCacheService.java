package lk.epicgreen.erp.mobile.service;

import lk.epicgreen.erp.mobile.dto.request.MobileDataCacheRequest;
import lk.epicgreen.erp.mobile.dto.response.MobileDataCacheResponse;
import lk.epicgreen.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Mobile Data Cache entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface MobileDataCacheService {

    MobileDataCacheResponse createOrUpdateCache(MobileDataCacheRequest request);
    void deleteCache(Long id);
    void deleteCacheByKey(Long userId, String cacheKey);
    void deleteAllCacheForUser(Long userId);
    
    MobileDataCacheResponse getCacheById(Long id);
    MobileDataCacheResponse getCacheByKey(Long userId, String cacheKey);
    PageResponse<MobileDataCacheResponse> getAllCaches(Pageable pageable);
    
    List<MobileDataCacheResponse> getCachesByUser(Long userId);
    List<MobileDataCacheResponse> getCachesByType(String cacheType);
    List<MobileDataCacheResponse> getCachesByUserAndType(Long userId, String cacheType);
    
    List<MobileDataCacheResponse> getExpiredCaches();
    void cleanupExpiredCaches();
    
    void refreshCache(Long userId, String cacheKey, Map<String, Object> newData);
    void refreshAllCachesForUser(Long userId);
    
    PageResponse<MobileDataCacheResponse> searchCaches(String keyword, Pageable pageable);
}
