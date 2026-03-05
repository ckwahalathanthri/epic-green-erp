package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeliveryNoteDTO {
    private Long id;
    private String deliveryNoteNumber;
    private Long dispatchId;
    private Long orderId;
    private String deliveredTo;
    private LocalDateTime deliveredAt;
    private String signatureImage;
    private String receiverPhone;
    private String receiverIdNumber;
    private String deliveryCondition;
    private String deliveryRemarks;
    private String photoUrls;
    private String latitude;
    private String longitude;
    private String createdBy;
    private LocalDateTime createdAt;
}