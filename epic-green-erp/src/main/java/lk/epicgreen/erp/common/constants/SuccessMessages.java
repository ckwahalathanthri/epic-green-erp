package lk.epicgreen.erp.common.constants;

/**
 * Success messages constants
 * Contains all success messages used throughout the application
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class SuccessMessages {
    
    // Prevent instantiation
    private SuccessMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Generic Success ====================
    
    public static final String SUCCESS_OPERATION = "Operation completed successfully";
    public static final String SUCCESS_CREATED = "Record created successfully";
    public static final String SUCCESS_UPDATED = "Record updated successfully";
    public static final String SUCCESS_DELETED = "Record deleted successfully";
    public static final String SUCCESS_RESTORED = "Record restored successfully";
    public static final String SUCCESS_SAVED = "Data saved successfully";
    
    // ==================== CRUD Operations ====================
    
    public static final String SUCCESS_PRODUCT_CREATED = "Product created successfully";
    public static final String SUCCESS_PRODUCT_UPDATED = "Product updated successfully";
    public static final String SUCCESS_PRODUCT_DELETED = "Product deleted successfully";
    
    public static final String SUCCESS_SUPPLIER_CREATED = "Supplier created successfully";
    public static final String SUCCESS_SUPPLIER_UPDATED = "Supplier updated successfully";
    public static final String SUCCESS_SUPPLIER_DELETED = "Supplier deleted successfully";
    
    public static final String SUCCESS_CUSTOMER_CREATED = "Customer created successfully";
    public static final String SUCCESS_CUSTOMER_UPDATED = "Customer updated successfully";
    public static final String SUCCESS_CUSTOMER_DELETED = "Customer deleted successfully";
    
    public static final String SUCCESS_WAREHOUSE_CREATED = "Warehouse created successfully";
    public static final String SUCCESS_WAREHOUSE_UPDATED = "Warehouse updated successfully";
    public static final String SUCCESS_WAREHOUSE_DELETED = "Warehouse deleted successfully";
    
    public static final String SUCCESS_USER_CREATED = "User created successfully";
    public static final String SUCCESS_USER_UPDATED = "User updated successfully";
    public static final String SUCCESS_USER_DELETED = "User deleted successfully";
    
    public static final String SUCCESS_ORDER_CREATED = "Order created successfully";
    public static final String SUCCESS_ORDER_UPDATED = "Order updated successfully";
    public static final String SUCCESS_ORDER_CANCELLED = "Order cancelled successfully";
    public static final String SUCCESS_ORDER_COMPLETED = "Order completed successfully";
    
    public static final String SUCCESS_INVOICE_CREATED = "Invoice created successfully";
    public static final String SUCCESS_INVOICE_UPDATED = "Invoice updated successfully";
    public static final String SUCCESS_INVOICE_SENT = "Invoice sent successfully";
    
    public static final String SUCCESS_PAYMENT_CREATED = "Payment recorded successfully";
    public static final String SUCCESS_PAYMENT_PROCESSED = "Payment processed successfully";
    public static final String SUCCESS_PAYMENT_VERIFIED = "Payment verified successfully";
    
    // ==================== Status Changes ====================
    
    public static final String SUCCESS_ACTIVATED = "%s activated successfully";
    public static final String SUCCESS_DEACTIVATED = "%s deactivated successfully";
    public static final String SUCCESS_APPROVED = "%s approved successfully";
    public static final String SUCCESS_REJECTED = "%s rejected successfully";
    public static final String SUCCESS_STATUS_UPDATED = "Status updated successfully";
    
    // ==================== Bulk Operations ====================
    
    public static final String SUCCESS_BULK_CREATED = "%d records created successfully";
    public static final String SUCCESS_BULK_UPDATED = "%d records updated successfully";
    public static final String SUCCESS_BULK_DELETED = "%d records deleted successfully";
    public static final String SUCCESS_BULK_OPERATION = "Bulk operation completed: %d succeeded, %d failed";
    
    // ==================== Authentication ====================
    
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_LOGOUT = "Logout successful";
    public static final String SUCCESS_PASSWORD_CHANGED = "Password changed successfully";
    public static final String SUCCESS_PASSWORD_RESET = "Password reset successfully";
    public static final String SUCCESS_PASSWORD_RESET_EMAIL = "Password reset email sent";
    public static final String SUCCESS_ACCOUNT_VERIFIED = "Account verified successfully";
    public static final String SUCCESS_ACCOUNT_UNLOCKED = "Account unlocked successfully";
    
    // ==================== File Operations ====================
    
    public static final String SUCCESS_FILE_UPLOADED = "File uploaded successfully";
    public static final String SUCCESS_FILE_DELETED = "File deleted successfully";
    public static final String SUCCESS_FILE_DOWNLOADED = "File downloaded successfully";
    
    // ==================== Import/Export ====================
    
    public static final String SUCCESS_IMPORT = "Import completed successfully: %d records imported";
    public static final String SUCCESS_IMPORT_PARTIAL = "Import completed: %d succeeded, %d failed";
    public static final String SUCCESS_EXPORT = "Export completed successfully: %d records exported";
    public static final String SUCCESS_EXPORT_GENERATED = "Export file generated successfully";
    
    // ==================== Sync Operations ====================
    
    public static final String SUCCESS_SYNC_STARTED = "Synchronization started";
    public static final String SUCCESS_SYNC_COMPLETED = "Synchronization completed successfully";
    public static final String SUCCESS_SYNC_PARTIAL = "Synchronization completed: %d succeeded, %d failed";
    
    // ==================== Email/SMS ====================
    
    public static final String SUCCESS_EMAIL_SENT = "Email sent successfully";
    public static final String SUCCESS_SMS_SENT = "SMS sent successfully";
    public static final String SUCCESS_NOTIFICATION_SENT = "Notification sent successfully";
    
    // ==================== Report ====================
    
    public static final String SUCCESS_REPORT_GENERATED = "Report generated successfully";
    public static final String SUCCESS_REPORT_SENT = "Report sent successfully";
    public static final String SUCCESS_REPORT_SCHEDULED = "Report scheduled successfully";
    
    // ==================== Stock/Inventory ====================
    
    public static final String SUCCESS_STOCK_UPDATED = "Stock updated successfully";
    public static final String SUCCESS_STOCK_ADJUSTED = "Stock adjustment completed successfully";
    public static final String SUCCESS_STOCK_TRANSFERRED = "Stock transferred successfully";
    public static final String SUCCESS_GRN_CREATED = "Goods Receipt Note created successfully";
    public static final String SUCCESS_STOCK_COUNT = "Stock count completed successfully";
    
    // ==================== Production ====================
    
    public static final String SUCCESS_PRODUCTION_STARTED = "Production started successfully";
    public static final String SUCCESS_PRODUCTION_COMPLETED = "Production completed successfully";
    public static final String SUCCESS_BOM_CREATED = "Bill of Materials created successfully";
    public static final String SUCCESS_MATERIAL_CONSUMED = "Material consumption recorded successfully";
    public static final String SUCCESS_OUTPUT_RECORDED = "Production output recorded successfully";
    
    // ==================== Purchase ====================
    
    public static final String SUCCESS_PURCHASE_ORDER_CREATED = "Purchase order created successfully";
    public static final String SUCCESS_PURCHASE_ORDER_APPROVED = "Purchase order approved successfully";
    public static final String SUCCESS_PURCHASE_ORDER_RECEIVED = "Purchase order received successfully";
    
    // ==================== Sales ====================
    
    public static final String SUCCESS_SALES_ORDER_CREATED = "Sales order created successfully";
    public static final String SUCCESS_SALES_ORDER_CONFIRMED = "Sales order confirmed successfully";
    public static final String SUCCESS_DISPATCH_COMPLETED = "Dispatch completed successfully";
    
    // ==================== Returns ====================
    
    public static final String SUCCESS_RETURN_CREATED = "Return created successfully";
    public static final String SUCCESS_RETURN_PROCESSED = "Return processed successfully";
    public static final String SUCCESS_CREDIT_NOTE_ISSUED = "Credit note issued successfully";
    
    // ==================== Configuration ====================
    
    public static final String SUCCESS_CONFIG_UPDATED = "Configuration updated successfully";
    public static final String SUCCESS_SETTINGS_SAVED = "Settings saved successfully";
    public static final String SUCCESS_PREFERENCES_UPDATED = "Preferences updated successfully";
    
    // ==================== Cache ====================
    
    public static final String SUCCESS_CACHE_CLEARED = "Cache cleared successfully";
    public static final String SUCCESS_CACHE_REFRESHED = "Cache refreshed successfully";
    
    // ==================== Mobile ====================
    
    public static final String SUCCESS_DEVICE_REGISTERED = "Device registered successfully";
    public static final String SUCCESS_DEVICE_UNREGISTERED = "Device unregistered successfully";
    
    // ==================== Customer/Supplier ====================
    
    public static final String SUCCESS_CUSTOMER_ACTIVATED = "Customer activated successfully";
    public static final String SUCCESS_CUSTOMER_BLOCKED = "Customer blocked successfully";
    public static final String SUCCESS_SUPPLIER_ACTIVATED = "Supplier activated successfully";
    public static final String SUCCESS_SUPPLIER_BLOCKED = "Supplier blocked successfully";
    
    // ==================== Ledger/Accounting ====================
    
    public static final String SUCCESS_LEDGER_ENTRY_CREATED = "Ledger entry created successfully";
    public static final String SUCCESS_JOURNAL_POSTED = "Journal posted successfully";
    public static final String SUCCESS_RECONCILIATION_COMPLETED = "Reconciliation completed successfully";
    
    // ==================== Verification ====================
    
    public static final String SUCCESS_VERIFIED = "%s verified successfully";
    public static final String SUCCESS_VALIDATION_PASSED = "Validation passed successfully";
}
