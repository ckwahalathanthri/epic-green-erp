package lk.epicgreen.erp.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Customer Type Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTypeDTO {

    private Long id;
    private String typeCode;
    private String typeName;
    private String description;
    private Boolean isActive;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
