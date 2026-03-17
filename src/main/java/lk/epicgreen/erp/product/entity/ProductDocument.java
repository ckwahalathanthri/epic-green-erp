package lk.epicgreen.erp.product.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Product Document Entity
 * Stores product related documents
 */
@Entity
@Table(name = "product_documents")
@Data
@NoArgsConstructor
public class ProductDocument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "document_url", nullable = false)
    private String documentUrl;
    
    @Column(name = "document_name", nullable = false)
    private String documentName;
    
    @Column(name = "document_type")
    private String documentType;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
    
    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
