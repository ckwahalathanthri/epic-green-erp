package lk.epicgreen.erp.sales.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesQuotationDTO {
    private Long id;
    private String quotationNumber;
    private LocalDate quotationDate;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDate validUntil;
    private String quotationStatus;
    private String referenceNumber;
    private BigDecimal subtotal;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String currency;
    private String paymentTerms;
    private String deliveryTerms;
    private String notes;
    private String termsAndConditions;
    private String preparedBy;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private Long convertedToOrderId;
    private LocalDateTime convertedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SalesQuotationItemDTO> items;
}