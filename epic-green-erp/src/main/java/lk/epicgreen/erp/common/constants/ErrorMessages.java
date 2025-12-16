package lk.epicgreen.erp.common.constants;

/**
 * Error messages constants
 * Contains all error messages used throughout the application
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class ErrorMessages {
    
    // Prevent instantiation
    private ErrorMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Generic Errors ====================
    
    public static final String ERROR_GENERIC = "An error occurred while processing your request";
    public static final String ERROR_INTERNAL_SERVER = "Internal server error occurred";
    public static final String ERROR_SERVICE_UNAVAILABLE = "Service is temporarily unavailable";
    public static final String ERROR_TIMEOUT = "Request timeout occurred";
    public static final String ERROR_BAD_REQUEST = "Bad request";
    public static final String ERROR_INVALID_INPUT = "Invalid input provided";
    
    // ==================== Not Found Errors ====================
    
    public static final String ERROR_NOT_FOUND = "Resource not found";
    public static final String ERROR_PRODUCT_NOT_FOUND = "Product not found with ID: %s";
    public static final String ERROR_SUPPLIER_NOT_FOUND = "Supplier not found with ID: %s";
    public static final String ERROR_CUSTOMER_NOT_FOUND = "Customer not found with ID: %s";
    public static final String ERROR_WAREHOUSE_NOT_FOUND = "Warehouse not found with ID: %s";
    public static final String ERROR_USER_NOT_FOUND = "User not found with ID: %s";
    public static final String ERROR_ROLE_NOT_FOUND = "Role not found with ID: %s";
    public static final String ERROR_CATEGORY_NOT_FOUND = "Category not found with ID: %s";
    public static final String ERROR_ORDER_NOT_FOUND = "Order not found with ID: %s";
    public static final String ERROR_INVOICE_NOT_FOUND = "Invoice not found with ID: %s";
    public static final String ERROR_PAYMENT_NOT_FOUND = "Payment not found with ID: %s";
    public static final String ERROR_RECORD_NOT_FOUND = "%s not found with ID: %s";
    
    // ==================== Validation Errors ====================
    
    public static final String ERROR_VALIDATION_FAILED = "Validation failed";
    public static final String ERROR_REQUIRED_FIELD = "%s is required";
    public static final String ERROR_INVALID_FORMAT = "%s has invalid format";
    public static final String ERROR_INVALID_EMAIL = "Invalid email address";
    public static final String ERROR_INVALID_PHONE = "Invalid phone number";
    public static final String ERROR_INVALID_DATE = "Invalid date format";
    public static final String ERROR_INVALID_VALUE = "Invalid value for %s";
    public static final String ERROR_MIN_LENGTH = "%s must be at least %d characters";
    public static final String ERROR_MAX_LENGTH = "%s must not exceed %d characters";
    public static final String ERROR_MIN_VALUE = "%s must be at least %s";
    public static final String ERROR_MAX_VALUE = "%s must not exceed %s";
    public static final String ERROR_OUT_OF_RANGE = "%s is out of valid range";
    
    // ==================== Duplicate/Unique Errors ====================
    
    public static final String ERROR_DUPLICATE = "Duplicate record found";
    public static final String ERROR_DUPLICATE_CODE = "%s code '%s' already exists";
    public static final String ERROR_DUPLICATE_NAME = "%s name '%s' already exists";
    public static final String ERROR_DUPLICATE_EMAIL = "Email '%s' already exists";
    public static final String ERROR_DUPLICATE_PHONE = "Phone number '%s' already exists";
    public static final String ERROR_DUPLICATE_USERNAME = "Username '%s' already exists";
    public static final String ERROR_ALREADY_EXISTS = "%s already exists";
    
    // ==================== Business Logic Errors ====================
    
    public static final String ERROR_INSUFFICIENT_STOCK = "Insufficient stock for product: %s";
    public static final String ERROR_NEGATIVE_QUANTITY = "Quantity cannot be negative";
    public static final String ERROR_ZERO_QUANTITY = "Quantity must be greater than zero";
    public static final String ERROR_INVALID_PRICE = "Price must be greater than zero";
    public static final String ERROR_INVALID_DISCOUNT = "Discount must be between 0 and 100";
    public static final String ERROR_INVALID_DATE_RANGE = "End date must be after start date";
    public static final String ERROR_PAST_DATE = "Date cannot be in the past";
    public static final String ERROR_FUTURE_DATE = "Date cannot be in the future";
    public static final String ERROR_EXPIRED = "%s has expired";
    public static final String ERROR_NOT_ACTIVE = "%s is not active";
    public static final String ERROR_ALREADY_PROCESSED = "%s has already been processed";
    public static final String ERROR_CANNOT_MODIFY = "Cannot modify %s in current status";
    public static final String ERROR_CANNOT_DELETE = "Cannot delete %s: %s";
    public static final String ERROR_REFERENCE_EXISTS = "Cannot delete %s: referenced by other records";
    
    // ==================== Authentication Errors ====================
    
    public static final String ERROR_UNAUTHORIZED = "Unauthorized access";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERROR_ACCOUNT_LOCKED = "Account is locked. Please contact administrator";
    public static final String ERROR_ACCOUNT_DISABLED = "Account is disabled";
    public static final String ERROR_ACCOUNT_EXPIRED = "Account has expired";
    public static final String ERROR_PASSWORD_EXPIRED = "Password has expired. Please reset your password";
    public static final String ERROR_INVALID_TOKEN = "Invalid or expired token";
    public static final String ERROR_TOKEN_EXPIRED = "Token has expired";
    public static final String ERROR_SESSION_EXPIRED = "Session has expired. Please login again";
    public static final String ERROR_MAX_LOGIN_ATTEMPTS = "Maximum login attempts exceeded. Account locked for %d minutes";
    
    // ==================== Authorization Errors ====================
    
    public static final String ERROR_FORBIDDEN = "Access denied";
    public static final String ERROR_INSUFFICIENT_PERMISSIONS = "You don't have permission to perform this action";
    public static final String ERROR_ROLE_REQUIRED = "Required role: %s";
    public static final String ERROR_PERMISSION_REQUIRED = "Required permission: %s";
    
    // ==================== Password Errors ====================
    
    public static final String ERROR_PASSWORD_WEAK = "Password is too weak";
    public static final String ERROR_PASSWORD_MISMATCH = "Passwords do not match";
    public static final String ERROR_PASSWORD_SAME = "New password cannot be same as old password";
    public static final String ERROR_PASSWORD_MIN_LENGTH = "Password must be at least %d characters";
    public static final String ERROR_PASSWORD_REQUIRES_UPPERCASE = "Password must contain at least one uppercase letter";
    public static final String ERROR_PASSWORD_REQUIRES_LOWERCASE = "Password must contain at least one lowercase letter";
    public static final String ERROR_PASSWORD_REQUIRES_DIGIT = "Password must contain at least one digit";
    public static final String ERROR_PASSWORD_REQUIRES_SPECIAL = "Password must contain at least one special character";
    
    // ==================== File Upload Errors ====================
    
    public static final String ERROR_FILE_EMPTY = "File is empty";
    public static final String ERROR_FILE_TOO_LARGE = "File size exceeds maximum limit of %d MB";
    public static final String ERROR_FILE_TYPE_NOT_ALLOWED = "File type '%s' is not allowed";
    public static final String ERROR_FILE_UPLOAD_FAILED = "File upload failed: %s";
    public static final String ERROR_FILE_READ_FAILED = "Failed to read file: %s";
    public static final String ERROR_FILE_NOT_FOUND = "File not found: %s";
    public static final String ERROR_INVALID_FILE_FORMAT = "Invalid file format";
    
    // ==================== Import Errors ====================
    
    public static final String ERROR_IMPORT_FAILED = "Import failed";
    public static final String ERROR_IMPORT_NO_DATA = "No data found in import file";
    public static final String ERROR_IMPORT_INVALID_HEADER = "Invalid header in import file";
    public static final String ERROR_IMPORT_MISSING_COLUMN = "Missing required column: %s";
    public static final String ERROR_IMPORT_INVALID_ROW = "Invalid data in row %d";
    public static final String ERROR_IMPORT_DUPLICATE_ROW = "Duplicate data in row %d";
    
    // ==================== Export Errors ====================
    
    public static final String ERROR_EXPORT_FAILED = "Export failed";
    public static final String ERROR_EXPORT_NO_DATA = "No data available for export";
    public static final String ERROR_EXPORT_TOO_MANY_ROWS = "Too many rows to export. Maximum allowed: %d";
    
    // ==================== Database Errors ====================
    
    public static final String ERROR_DATABASE = "Database error occurred";
    public static final String ERROR_CONSTRAINT_VIOLATION = "Database constraint violation";
    public static final String ERROR_FOREIGN_KEY_VIOLATION = "Cannot delete: record is referenced by other data";
    public static final String ERROR_UNIQUE_VIOLATION = "Duplicate value violates unique constraint";
    public static final String ERROR_CONNECTION_FAILED = "Database connection failed";
    public static final String ERROR_QUERY_TIMEOUT = "Database query timeout";
    
    // ==================== Network Errors ====================
    
    public static final String ERROR_NETWORK = "Network error occurred";
    public static final String ERROR_CONNECTION_REFUSED = "Connection refused";
    public static final String ERROR_CONNECTION_TIMEOUT = "Connection timeout";
    public static final String ERROR_NO_INTERNET = "No internet connection";
    
    // ==================== Sync Errors ====================
    
    public static final String ERROR_SYNC_FAILED = "Synchronization failed";
    public static final String ERROR_SYNC_CONFLICT = "Sync conflict detected";
    public static final String ERROR_SYNC_IN_PROGRESS = "Synchronization already in progress";
    public static final String ERROR_SYNC_NOT_ENABLED = "Synchronization is not enabled";
    
    // ==================== Payment Errors ====================
    
    public static final String ERROR_PAYMENT_FAILED = "Payment processing failed";
    public static final String ERROR_INSUFFICIENT_FUNDS = "Insufficient funds";
    public static final String ERROR_INVALID_AMOUNT = "Invalid payment amount";
    public static final String ERROR_PAYMENT_ALREADY_PROCESSED = "Payment has already been processed";
    public static final String ERROR_PAYMENT_CANCELLED = "Payment was cancelled";
    public static final String ERROR_INVALID_PAYMENT_METHOD = "Invalid payment method";
    
    // ==================== Inventory Errors ====================
    
    public static final String ERROR_STOCK_NOT_AVAILABLE = "Stock not available";
    public static final String ERROR_NEGATIVE_STOCK = "Stock cannot be negative";
    public static final String ERROR_BELOW_MINIMUM_STOCK = "Stock below minimum level";
    public static final String ERROR_ABOVE_MAXIMUM_STOCK = "Stock exceeds maximum level";
    public static final String ERROR_INVALID_WAREHOUSE = "Invalid warehouse";
    public static final String ERROR_INVALID_LOCATION = "Invalid warehouse location";
    
    // ==================== Order Errors ====================
    
    public static final String ERROR_ORDER_EMPTY = "Order cannot be empty";
    public static final String ERROR_ORDER_CANCELLED = "Order has been cancelled";
    public static final String ERROR_ORDER_COMPLETED = "Order has already been completed";
    public static final String ERROR_INVALID_ORDER_STATUS = "Invalid order status transition";
    public static final String ERROR_CANNOT_CANCEL_ORDER = "Cannot cancel order in current status";
    
    // ==================== Production Errors ====================
    
    public static final String ERROR_BOM_NOT_FOUND = "Bill of Materials not found";
    public static final String ERROR_INVALID_BOM = "Invalid Bill of Materials";
    public static final String ERROR_MATERIAL_NOT_AVAILABLE = "Material not available: %s";
    public static final String ERROR_PRODUCTION_NOT_STARTED = "Production has not started";
    public static final String ERROR_PRODUCTION_COMPLETED = "Production already completed";
    
    // ==================== Report Errors ====================
    
    public static final String ERROR_REPORT_GENERATION_FAILED = "Report generation failed";
    public static final String ERROR_REPORT_NOT_FOUND = "Report not found";
    public static final String ERROR_INVALID_REPORT_PARAMS = "Invalid report parameters";
    
    // ==================== Email Errors ====================
    
    public static final String ERROR_EMAIL_SEND_FAILED = "Failed to send email";
    public static final String ERROR_INVALID_EMAIL_ADDRESS = "Invalid email address";
    public static final String ERROR_EMAIL_TEMPLATE_NOT_FOUND = "Email template not found";
    
    // ==================== SMS Errors ====================
    
    public static final String ERROR_SMS_SEND_FAILED = "Failed to send SMS";
    public static final String ERROR_INVALID_PHONE_NUMBER = "Invalid phone number";
    
    // ==================== Configuration Errors ====================
    
    public static final String ERROR_CONFIG_NOT_FOUND = "Configuration not found: %s";
    public static final String ERROR_INVALID_CONFIG = "Invalid configuration: %s";
    
    // ==================== Cache Errors ====================
    
    public static final String ERROR_CACHE_FAILED = "Cache operation failed";
    public static final String ERROR_CACHE_NOT_AVAILABLE = "Cache is not available";
    
    // ==================== API Errors ====================
    
    public static final String ERROR_API_RATE_LIMIT = "API rate limit exceeded";
    public static final String ERROR_API_KEY_INVALID = "Invalid API key";
    public static final String ERROR_API_VERSION_NOT_SUPPORTED = "API version not supported";
    
    // ==================== Mobile Errors ====================
    
    public static final String ERROR_DEVICE_NOT_REGISTERED = "Device not registered";
    public static final String ERROR_DEVICE_LIMIT_REACHED = "Maximum device limit reached";
    public static final String ERROR_OFFLINE_MODE_NOT_AVAILABLE = "Offline mode not available";
    
    // ==================== Audit Errors ====================
    
    public static final String ERROR_AUDIT_LOG_FAILED = "Failed to create audit log";
    
    // ==================== Tax Errors ====================
    
    public static final String ERROR_INVALID_TAX_RATE = "Invalid tax rate";
    public static final String ERROR_TAX_CALCULATION_FAILED = "Tax calculation failed";
    
    // ==================== Discount Errors ====================
    
    public static final String ERROR_DISCOUNT_EXPIRED = "Discount code has expired";
    public static final String ERROR_DISCOUNT_NOT_APPLICABLE = "Discount not applicable for this order";
    
    // ==================== Customer Errors ====================
    
    public static final String ERROR_CUSTOMER_CREDIT_LIMIT = "Customer credit limit exceeded";
    public static final String ERROR_CUSTOMER_BLOCKED = "Customer is blocked";
    public static final String ERROR_CUSTOMER_INACTIVE = "Customer account is inactive";
    
    // ==================== Supplier Errors ====================
    
    public static final String ERROR_SUPPLIER_INACTIVE = "Supplier is inactive";
    public static final String ERROR_SUPPLIER_BLOCKED = "Supplier is blocked";
    
    // ==================== Ledger Errors ====================
    
    public static final String ERROR_LEDGER_BALANCE_MISMATCH = "Ledger balance mismatch";
    public static final String ERROR_LEDGER_ENTRY_FAILED = "Failed to create ledger entry";
    
    // ==================== Custom Error Codes ====================
    
    public static final String ERR_CODE_VALIDATION = "ERR_001";
    public static final String ERR_CODE_NOT_FOUND = "ERR_002";
    public static final String ERR_CODE_DUPLICATE = "ERR_003";
    public static final String ERR_CODE_UNAUTHORIZED = "ERR_004";
    public static final String ERR_CODE_FORBIDDEN = "ERR_005";
    public static final String ERR_CODE_DATABASE = "ERR_006";
    public static final String ERR_CODE_BUSINESS_LOGIC = "ERR_007";
    public static final String ERR_CODE_EXTERNAL_SERVICE = "ERR_008";
    public static final String ERR_CODE_INTERNAL = "ERR_009";
}
