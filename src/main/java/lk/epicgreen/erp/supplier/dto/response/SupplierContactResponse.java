package lk.epicgreen.erp.supplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for supplier contact response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierContactResponse {

    private Long id;
    private Long supplierId;
    private String supplierName;
    private String contactName;
    private String designation;
    private String email;
    private String phone;
    private String mobile;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
}
