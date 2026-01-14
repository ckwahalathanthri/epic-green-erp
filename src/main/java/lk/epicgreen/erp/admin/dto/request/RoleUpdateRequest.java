package lk.epicgreen.erp.admin.dto.request;

import java.util.List;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest{
    private String roleName;
    private String description;
    private String roleCode;
    private List<Long> permissionIds;
}