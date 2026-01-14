package lk.epicgreen.erp.mobile.mapper;

import lk.epicgreen.erp.mobile.dto.request.MobileDataCacheRequest;
import lk.epicgreen.erp.mobile.dto.response.MobileDataCacheResponse;
import lk.epicgreen.erp.mobile.entity.MobileDataCache;
import org.springframework.stereotype.Component;

/**
 * Mapper for MobileDataCache entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class MobileDataCacheMapper {

    public MobileDataCache toEntity(MobileDataCacheRequest request) {
        if (request == null) {
            return null;
        }

        return MobileDataCache.builder()
                .UserId(request.getUserId())
            .cacheKey(request.getCacheKey())
            .cacheType(request.getCacheType())
            .dataSnapshot(request.getDataSnapshot().toString())
            .lastSyncedAt(request.getLastSyncedAt())
            .expiresAt(request.getExpiresAt())
            .build();
    }

    public void updateEntityFromRequest(MobileDataCacheRequest request, MobileDataCache cache) {
        if (request == null || cache == null) {
            return;
        }

        cache.setUserId(request.getUserId());
        cache.setCacheKey(request.getCacheKey());
        cache.setCacheType(request.getCacheType());
        cache.setDataSnapshot(request.getDataSnapshot().toString());
        cache.setLastSyncedAt(request.getLastSyncedAt());
        cache.setExpiresAt(request.getExpiresAt());
    }

    public MobileDataCacheResponse toResponse(MobileDataCache cache) {
        if (cache == null) {
            return null;
        }

        return MobileDataCacheResponse.builder()
            .id(cache.getId())
            .userId(cache.getUserId())
            .cacheKey(cache.getCacheKey())
            .cacheType(cache.getCacheType())
//            .dataSnapshot(cache.getDataSnapshot())
            .lastSyncedAt(cache.getLastSyncedAt())
            .expiresAt(cache.getExpiresAt())
            .build();
    }
}
