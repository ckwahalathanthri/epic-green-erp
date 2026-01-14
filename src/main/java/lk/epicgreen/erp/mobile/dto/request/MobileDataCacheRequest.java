package lk.epicgreen.erp.mobile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for creating/updating Mobile Data Cache
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileDataCacheRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Cache key is required")
    @Size(max = 100, message = "Cache key must not exceed 100 characters")
    private String cacheKey;

    @NotBlank(message = "Cache type is required")
    @Pattern(regexp = "^(CUSTOMER|PRODUCT|PRICELIST|STOCK|ORDER|PAYMENT|OTHER)$",
             message = "Cache type must be one of: CUSTOMER, PRODUCT, PRICELIST, STOCK, ORDER, PAYMENT, OTHER")
    private String cacheType;

    @NotNull(message = "Data snapshot is required")
    private Map<String, Object> dataSnapshot;

    private LocalDateTime lastSyncedAt;

    private LocalDateTime expiresAt;


}
