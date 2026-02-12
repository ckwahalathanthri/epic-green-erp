package lk.epicgreen.erp.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Customer Contact Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerContactDTO {

    private Long id;
    private Long customerId;
    private String contactName;
    private String designation;
    private String department;
    private String email;
    private String phone;
    private String mobile;
    private Boolean isPrimary;
    private Boolean isActive;
    private String notes;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
