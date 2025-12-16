package lk.epicgreen.erp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Generic key-value pair DTO
 * Useful for configuration settings, metadata, etc.
 * 
 * @param <K> Type of key
 * @param <V> Type of value
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyValueDTO<K, V> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Key
     */
    private K key;
    
    /**
     * Value
     */
    private V value;
    
    /**
     * Optional label/description for the key
     */
    private String label;
    
    /**
     * Optional data type of value
     */
    private String type;
    
    // ==================== Factory Methods ====================
    
    /**
     * Creates KeyValueDTO with key and value
     * 
     * @param key Key
     * @param value Value
     * @param <K> Type of key
     * @param <V> Type of value
     * @return KeyValueDTO
     */
    public static <K, V> KeyValueDTO<K, V> of(K key, V value) {
        return KeyValueDTO.<K, V>builder()
                .key(key)
                .value(value)
                .build();
    }
    
    /**
     * Creates KeyValueDTO with key, value, and label
     * 
     * @param key Key
     * @param value Value
     * @param label Label/description
     * @param <K> Type of key
     * @param <V> Type of value
     * @return KeyValueDTO
     */
    public static <K, V> KeyValueDTO<K, V> of(K key, V value, String label) {
        return KeyValueDTO.<K, V>builder()
                .key(key)
                .value(value)
                .label(label)
                .build();
    }
    
    /**
     * Creates KeyValueDTO with all fields
     * 
     * @param key Key
     * @param value Value
     * @param label Label/description
     * @param type Data type
     * @param <K> Type of key
     * @param <V> Type of value
     * @return KeyValueDTO
     */
    public static <K, V> KeyValueDTO<K, V> of(K key, V value, String label, String type) {
        return KeyValueDTO.<K, V>builder()
                .key(key)
                .value(value)
                .label(label)
                .type(type)
                .build();
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Checks if value is null
     * 
     * @return true if value is null
     */
    public boolean hasValue() {
        return value != null;
    }
    
    /**
     * Gets string representation of value
     * 
     * @return Value as string
     */
    public String getValueAsString() {
        return value != null ? value.toString() : null;
    }
    
    /**
     * Gets display text (label: value format)
     * Example: "Max Stock Level: 1000"
     * 
     * @return Display text
     */
    public String getDisplayText() {
        if (label != null && !label.isEmpty()) {
            return label + ": " + getValueAsString();
        }
        return key + ": " + getValueAsString();
    }
}
