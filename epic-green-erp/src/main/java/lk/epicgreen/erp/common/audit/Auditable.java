package lk.epicgreen.erp.common.audit;

import java.lang.annotation.*;

/**
 * Annotation to mark entities for detailed audit logging
 * When applied, all CRUD operations will be logged to audit_logs table
 * 
 * Usage:
 * @Auditable(module = "PRODUCT")
 * public class Product extends AuditEntity { ... }
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    
    /**
     * Module name for categorization
     * Examples: "PRODUCT", "SALES", "INVENTORY"
     * 
     * @return Module name
     */
    String module() default "";
    
    /**
     * Whether to log view operations
     * 
     * @return true to log view operations
     */
    boolean logViews() default false;
    
    /**
     * Whether to store old values
     * 
     * @return true to store old values
     */
    boolean storeOldValues() default true;
    
    /**
     * Whether to store new values
     * 
     * @return true to store new values
     */
    boolean storeNewValues() default true;
}
