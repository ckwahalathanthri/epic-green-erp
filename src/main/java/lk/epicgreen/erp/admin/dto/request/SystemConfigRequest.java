package lk.epicgreen.erp.admin.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigRequest {
    private String configKey;
    private String configValue;
    private String configGroup;
    private String description;
    private Boolean isEncrypted;
    
}
