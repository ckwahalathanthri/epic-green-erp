package lk.epicgreen.erp.sales.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_notes")
@Data
public class DeliveryNote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "delivery_note_number", unique = true, length = 50)
    private String deliveryNoteNumber; // DN-2026-001
    
    @Column(name = "dispatch_id", nullable = false)
    private Long dispatchId;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "delivered_to", length = 200)
    private String deliveredTo; // Receiver name
    
    @Column(name = "delivered_at", nullable = false)
    private LocalDateTime deliveredAt;
    
    @Column(name = "signature_image", columnDefinition = "TEXT")
    private String signatureImage; // Base64 or URL
    
    @Column(name = "receiver_phone", length = 20)
    private String receiverPhone;
    
    @Column(name = "receiver_id_number", length = 50)
    private String receiverIdNumber;
    
    @Column(name = "delivery_condition", length = 20)
    private String deliveryCondition; // GOOD, DAMAGED, PARTIAL
    
    @Column(name = "delivery_remarks", columnDefinition = "TEXT")
    private String deliveryRemarks;
    
    @Column(name = "photo_urls", columnDefinition = "TEXT")
    private String photoUrls; // Comma-separated URLs
    
    @Column(name = "latitude", length = 50)
    private String latitude;
    
    @Column(name = "longitude", length = 50)
    private String longitude;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    public void setTimestamp() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}