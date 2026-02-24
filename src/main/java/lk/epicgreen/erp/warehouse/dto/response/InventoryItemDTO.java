package com.epicgreen.erp.warehouse.dto;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    private Long id;
    private String code;
    private String name;
    private LocalDateTime createdAt;
}
