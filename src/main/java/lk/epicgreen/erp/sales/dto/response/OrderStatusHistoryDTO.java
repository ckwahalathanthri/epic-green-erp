package lk.epicgreen.erp.sales.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderStatusHistoryDTO {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private String previousStatus;
    private String newStatus;
    private String changedBy;
    private LocalDateTime changedAt;
    private String remarks;
    private String ipAddress;
}