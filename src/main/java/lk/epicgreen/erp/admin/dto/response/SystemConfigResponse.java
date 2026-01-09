package lk.epicgreen.erp.admin.dto.response;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigResponse {
    private Long id;
    private String configKey;
    private String configValue;
    private String configGroup;
    private String description;
    private Boolean isEncrypted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
