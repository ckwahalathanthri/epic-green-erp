package lk.epicgreen.erp.supplier.dto.request;

import lk.epicgreen.erp.supplier.dto.response.SupplierAddressDTO;
import lk.epicgreen.erp.supplier.dto.response.SupplierContactResponse;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierUpdateDTO {
    @NotNull
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    private String supplierCode;
    
    @NotBlank
    @Size(max = 200)
    private String supplierName;
    
    @Size(max = 200)
    private String supplierNameLocal;
    
    private Long supplierTypeId;
    private Long supplierCategoryId;
    
    @Size(max = 50)
    private String taxId;
    
    @Size(max = 50)
    private String registrationNumber;
    
    @Email
    @Size(max = 100)
    private String email;
    
    @Size(max = 20)
    private String phone;
    
    @Size(max = 20)
    private String mobile;
    
    @Size(max = 20)
    private String fax;
    
    @Size(max = 100)
    private String website;
    
    @Min(0)
    private Integer creditDays;
    
    @Min(0)
    private Integer paymentTermsDays;
    
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal discountPercentage;
    
    private Boolean isActive;
    private String notes;
    private List<SupplierContactResponse> contacts = new ArrayList<>();
    private List<SupplierAddressDTO> addresses = new ArrayList<>();
}
