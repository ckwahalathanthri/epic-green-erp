package lk.epicgreen.erp.audit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Activity Log response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogResponse {

    private Long id;
    private Long userId;
    private String username;
    private String activityType;
    private String module;
    private String activityDescription;
    private String referenceType;
    private Long referenceId;
    private String ipAddress;
    private String deviceType;
    private LocalDateTime createdAt;
}
