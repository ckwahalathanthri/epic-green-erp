package lk.epicgreen.erp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Simple DTO for dropdown lists and autocomplete
 * Contains only ID and Name/Label
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdNameDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Entity ID
     */
    private Long id;
    
    /**
     * Entity name/label
     */
    private String name;
    
    /**
     * Optional code field
     */
    private String code;
    
    /**
     * Optional description field
     */
    private String description;
    
    /**
     * Active/Inactive status
     */
    private Boolean active;
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates IdNameDTO with only ID and name
     * 
     * @param id Entity ID
     * @param name Entity name
     * @return IdNameDTO
     */
    public static IdNameDTO of(Long id, String name) {
        return IdNameDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
    
    /**
     * Creates IdNameDTO with ID, name, and code
     * 
     * @param id Entity ID
     * @param name Entity name
     * @param code Entity code
     * @return IdNameDTO
     */
    public static IdNameDTO of(Long id, String name, String code) {
        return IdNameDTO.builder()
                .id(id)
                .name(name)
                .code(code)
                .build();
    }
    
    /**
     * Creates IdNameDTO with all fields
     * 
     * @param id Entity ID
     * @param name Entity name
     * @param code Entity code
     * @param description Description
     * @param active Active status
     * @return IdNameDTO
     */
    public static IdNameDTO of(Long id, String name, String code, String description, Boolean active) {
        return IdNameDTO.builder()
                .id(id)
                .name(name)
                .code(code)
                .description(description)
                .active(active)
                .build();
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Gets display text (code - name format)
     * Example: "PRD-001 - Product Name"
     * 
     * @return Display text
     */
    public String getDisplayText() {
        if (code != null && !code.isEmpty()) {
            return code + " - " + name;
        }
        return name;
    }
    
    /**
     * Gets label for UI display
     * 
     * @return Label text
     */
    public String getLabel() {
        return getDisplayText();
    }
    
    /**
     * Checks if this item is active
     * 
     * @return true if active or null
     */
    public boolean isActive() {
        return active == null || active;
    }
}
