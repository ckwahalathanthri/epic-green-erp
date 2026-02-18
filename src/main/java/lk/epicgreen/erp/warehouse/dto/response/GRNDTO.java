package lk.epicgreen.erp.warehouse.dto.response;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GRNDTO {
    private Long id;
    private String grnNumber;
    private Long purchaseOrderId;
    private String poNumber;
    private Long supplierId;
    private String supplierName;
    private Long warehouseId;
    private String warehouseName;
    private LocalDate receivedDate;
    private String supplierInvoiceNumber;
    private String deliveryNoteNumber;
    private String grnStatus;
    private String qualityStatus;
    private String notes;
    private List<GRNItemDTO> items;
}
