package lk.epicgreen.erp.mobile.service.impl;

import lk.epicgreen.erp.mobile.dto.request.MobileDataCacheRequest;
import lk.epicgreen.erp.mobile.dto.response.MobileDataCacheResponse;
import lk.epicgreen.erp.mobile.entity.MobileDataCache;
import lk.epicgreen.erp.mobile.mapper.MobileDataCacheMapper;
import lk.epicgreen.erp.mobile.repository.MobileDataCacheRepository;
import lk.epicgreen.erp.mobile.service.MobileDataCacheService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MobileDataCacheServiceImpl implements MobileDataCacheService {

    private final MobileDataCacheRepository cacheRepository;
    private final MobileDataCacheMapper cacheMapper;

    @Override
    @Transactional
    public MobileDataCacheResponse createOrUpdateCache(MobileDataCacheRequest request) {
        log.info("Creating/updating cache for user: {} key: {}", request.getUserId(), request.getCacheKey());

        Optional<MobileDataCache> existingCache = 
            cacheRepository.findByUserIdAndCacheKey(request.getUserId(), request.getCacheKey());

        MobileDataCache cache;
        if (existingCache.isPresent()) {
            cache = existingCache.get();
            cacheMapper.updateEntityFromRequest(request, cache);
            cache.setLastSyncedAt(LocalDateTime.now());
            log.info("Updating existing cache");
        } else {
            cache = cacheMapper.toEntity(request);
            cache.setLastSyncedAt(LocalDateTime.now());
            log.info("Creating new cache");
        }

        MobileDataCache savedCache = cacheRepository.save(cache);
        log.info("Cache saved successfully");

        return cacheMapper.toResponse(savedCache);
    }

    @Override
    @Transactional
    public void deleteCache(Long id) {
        log.info("Deleting cache: {}", id);
        cacheRepository.deleteById(id);
        log.info("Cache deleted successfully");
    }

    @Override
    @Transactional
    public void deleteCacheByKey(Long userId, String cacheKey) {
        log.info("Deleting cache for user: {} key: {}", userId, cacheKey);
        
        int deletedCount = cacheRepository.deleteByUserIdAndCacheKey(userId, cacheKey);
        log.info("Deleted {} cache entries", deletedCount);
    }

    @Override
    @Transactional
    public void deleteAllCacheForUser(Long userId) {
        log.info("Deleting all cache for user: {}", userId);
        
        int deletedCount = cacheRepository.deleteByUserId(userId);
        log.info("Deleted {} cache entries for user {}", deletedCount, userId);
    }

    @Override
    public MobileDataCacheResponse getCacheById(Long id) {
        MobileDataCache cache = findCacheById(id);
        return cacheMapper.toResponse(cache);
    }

    @Override
    public MobileDataCacheResponse getCacheByKey(Long userId, String cacheKey) {
        MobileDataCache cache = cacheRepository.findByUserIdAndCacheKey(userId, cacheKey)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cache not found for user: " + userId + " key: " + cacheKey));
        return cacheMapper.toResponse(cache);
    }

    @Override
    public PageResponse<MobileDataCacheResponse> getAllCaches(Pageable pageable) {
        Page<MobileDataCache> cachePage = cacheRepository.findAll(pageable);
        return createPageResponse(cachePage);
    }

    @Override
    public List<MobileDataCacheResponse> getCachesByUser(Long userId) {
        List<MobileDataCache> caches = cacheRepository.findByUserId(userId);
        return caches.stream()
            .map(cacheMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MobileDataCacheResponse> getCachesByType(String cacheType) {
        List<MobileDataCache> caches = cacheRepository.findByCacheType(cacheType);
        return caches.stream()
            .map(cacheMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MobileDataCacheResponse> getCachesByUserAndType(Long userId, String cacheType) {
        List<MobileDataCache> caches = cacheRepository.findByUserIdAndCacheType(userId, cacheType);
        return caches.stream()
            .map(cacheMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MobileDataCacheResponse> getExpiredCaches() {
        LocalDateTime now = LocalDateTime.now();
        List<MobileDataCache> caches = cacheRepository.findByExpiresAtBefore(now);
        return caches.stream()
            .map(cacheMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cleanupExpiredCaches() {
        log.info("Cleaning up expired caches");

        LocalDateTime now = LocalDateTime.now();
        int deletedCount = cacheRepository.deleteByExpiresAtBefore(now);

        log.info("Cleaned up {} expired caches", deletedCount);
    }

    @Override
    @Transactional
    public void refreshCache(Long userId, String cacheKey, Map<String, Object> newData) {
        log.info("Refreshing cache for user: {} key: {}", userId, cacheKey);

        Optional<MobileDataCache> existingCache = 
            cacheRepository.findByUserIdAndCacheKey(userId, cacheKey);

        if (existingCache.isPresent()) {
            MobileDataCache cache = existingCache.get();
            cache.setDataSnapshot(newData);
            cache.setLastSyncedAt(LocalDateTime.now());
            cacheRepository.save(cache);
            log.info("Cache refreshed successfully");
        } else {
            log.warn("Cache not found for user: {} key: {}", userId, cacheKey);
        }
    }

    @Override
    @Transactional
    public void refreshAllCachesForUser(Long userId) {
        log.info("Refreshing all caches for user: {}", userId);

        List<MobileDataCache> caches = cacheRepository.findByUserId(userId);
        LocalDateTime now = LocalDateTime.now();

        for (MobileDataCache cache : caches) {
            cache.setLastSyncedAt(now);
        }

        cacheRepository.saveAll(caches);
        log.info("Refreshed {} caches for user {}", caches.size(), userId);
    }

    @Override
    public PageResponse<MobileDataCacheResponse> searchCaches(String keyword, Pageable pageable) {
        Page<MobileDataCache> cachePage = cacheRepository.searchCaches(keyword, pageable);
        return createPageResponse(cachePage);
    }

    private MobileDataCache findCacheById(Long id) {
        return cacheRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mobile Data Cache not found: " + id));
    }

    private PageResponse<MobileDataCacheResponse> createPageResponse(Page<MobileDataCache> cachePage) {
        List<MobileDataCacheResponse> content = cachePage.getContent().stream()
            .map(cacheMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<MobileDataCacheResponse>builder()
            .content(content)
            .pageNumber(cachePage.getNumber())
            .pageSize(cachePage.getSize())
            .totalElements(cachePage.getTotalElements())
            .totalPages(cachePage.getTotalPages())
            .last(cachePage.isLast())
            .first(cachePage.isFirst())
            .empty(cachePage.isEmpty())
            .build();
    }
}
