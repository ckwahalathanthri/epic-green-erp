package lk.epicgreen.erp.mobile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Mobile Data Cache response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileDataCacheResponse {

    private Long id;
    private Long userId;
    private String username;
    private String cacheKey;
    private String cacheType;
    private Map<String, Object> dataSnapshot;
    private LocalDateTime lastSyncedAt;
    private LocalDateTime expiresAt;
}
