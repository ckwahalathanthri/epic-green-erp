package lk.epicgreen.erp.admin.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCreateRequest {
    private String permissionCode;
    private String permissionName;
    private String description;
    private String module;
}