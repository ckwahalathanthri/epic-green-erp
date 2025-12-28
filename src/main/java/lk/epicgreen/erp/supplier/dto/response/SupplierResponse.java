package lk.epicgreen.erp.supplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for supplier response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse {

    private Long id;
    private String supplierCode;
    private String supplierName;
    private String supplierType;
    private String contactPerson;
    private String email;
    private String phone;
    private String mobile;
    private String taxId;
    private String paymentTerms;
    private BigDecimal creditLimit;
    private Integer creditDays;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String bankName;
    private String bankAccountNumber;
    private String bankBranch;
    private BigDecimal rating;
    private Boolean isActive;
    private BigDecimal currentBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
