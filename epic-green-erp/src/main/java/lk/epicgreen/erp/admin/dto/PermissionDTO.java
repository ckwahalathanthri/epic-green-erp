package lk.epicgreen.erp.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lk.epicgreen.erp.common.constants.AppConstants;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Permission DTO
 * Data transfer object for Permission entity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionDTO {
    
    private Long id;
    
    private String permissionCode;
    
    private String permissionName;
    
    private String description;
    
    private String module;
    
    private String category;
    
    private Boolean isSystemPermission;
    
    private Boolean isActive;
    
    private Integer displayOrder;
    
    /**
     * Audit fields
     */
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    @JsonFormat(pattern = AppConstants.DATETIME_FORMAT)
    private LocalDateTime updatedAt;
    
    private String updatedBy;
}
