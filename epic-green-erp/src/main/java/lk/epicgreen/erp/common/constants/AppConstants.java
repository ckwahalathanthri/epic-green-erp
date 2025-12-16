package lk.epicgreen.erp.common.constants;

/**
 * Application-wide constants
 * Contains general constants used across the entire application
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class AppConstants {
    
    // Prevent instantiation
    private AppConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Application Info ====================
    
    public static final String APP_NAME = "Epic Green ERP";
    public static final String APP_VERSION = "1.0.0";
    public static final String VERSION = APP_VERSION; // Alias for compatibility
    public static final String APP_DESCRIPTION = "Enterprise Resource Planning System for Spice Production";
    public static final String COMPANY_NAME = "Epic Green (Pvt) Ltd";
    public static final String COMPANY_EMAIL = "info@epicgreen.lk";
    public static final String COMPANY_PHONE = "+94 11 234 5678";
    public static final String COMPANY_ADDRESS = "Colombo, Sri Lanka";
    
    // ==================== Pagination Defaults ====================
    
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "ASC";
    
    // ==================== Date/Time Formats ====================
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DISPLAY_DATE_FORMAT = "dd MMM yyyy";
    public static final String DISPLAY_DATETIME_FORMAT = "dd MMM yyyy HH:mm";
    public static final String FILE_DATE_FORMAT = "yyyyMMdd";
    public static final String FILE_TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    
    // ==================== Timezone ====================
    
    public static final String DEFAULT_TIMEZONE = "Asia/Colombo";
    public static final String UTC_TIMEZONE = "UTC";
    
    // ==================== Locale ====================
    
    public static final String DEFAULT_LOCALE = "en_US";
    public static final String SINHALA_LOCALE = "si_LK";
    public static final String TAMIL_LOCALE = "ta_LK";
    
    // ==================== Currency ====================
    
    public static final String DEFAULT_CURRENCY = "LKR";
    public static final String CURRENCY_SYMBOL = "Rs.";
    public static final int CURRENCY_DECIMAL_PLACES = 2;
    
    // ==================== Number Formats ====================
    
    public static final String DECIMAL_FORMAT = "#,##0.00";
    public static final String INTEGER_FORMAT = "#,##0";
    public static final String PERCENTAGE_FORMAT = "#,##0.00%";
    public static final String QUANTITY_FORMAT = "#,##0.000";
    
    // ==================== File Upload ====================
    
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final String UPLOAD_DIR = "uploads/";
    public static final String TEMP_DIR = "temp/";
    public static final String EXPORT_DIR = "exports/";
    public static final String REPORT_DIR = "reports/";
    
    // ==================== Allowed File Extensions ====================
    
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    public static final String[] ALLOWED_DOCUMENT_EXTENSIONS = {".pdf", ".doc", ".docx", ".xls", ".xlsx"};
    public static final String[] ALLOWED_IMPORT_EXTENSIONS = {".xls", ".xlsx", ".csv"};
    
    // ==================== Cache Names ====================
    
    public static final String CACHE_PRODUCTS = "products";
    public static final String CACHE_SUPPLIERS = "suppliers";
    public static final String CACHE_CUSTOMERS = "customers";
    public static final String CACHE_WAREHOUSES = "warehouses";
    public static final String CACHE_INVENTORY = "inventory";
    public static final String CACHE_USERS = "users";
    public static final String CACHE_ROLES = "roles";
    public static final String CACHE_PERMISSIONS = "permissions";
    public static final String CACHE_CATEGORIES = "categories";
    public static final String CACHE_UOMS = "uoms";
    public static final String CACHE_SETTINGS = "settings";
    public static final String CACHE_REPORTS = "reports";
    
    // ==================== Cache TTL (seconds) ====================
    
    public static final long CACHE_TTL_SHORT = 300; // 5 minutes
    public static final long CACHE_TTL_MEDIUM = 1800; // 30 minutes
    public static final long CACHE_TTL_LONG = 3600; // 1 hour
    public static final long CACHE_TTL_VERY_LONG = 86400; // 24 hours
    
    // ==================== Queue Names ====================
    
    public static final String QUEUE_EMAIL = "email-queue";
    public static final String QUEUE_SMS = "sms-queue";
    public static final String QUEUE_NOTIFICATION = "notification-queue";
    public static final String QUEUE_REPORT = "report-queue";
    public static final String QUEUE_REPORT_GENERATION = "report-generation-queue";
    public static final String QUEUE_EXPORT = "export-queue";
    public static final String QUEUE_IMPORT = "import-queue";
    public static final String QUEUE_SYNC = "sync-queue";
    public static final String QUEUE_MOBILE_SYNC = "mobile-sync-queue";
    public static final String QUEUE_AUDIT = "audit-queue";
    public static final String QUEUE_DEAD_LETTER = "dead-letter-queue";
    
    // ==================== Default Values ====================
    
    public static final String DEFAULT_PASSWORD = "Epic@123";
    public static final String DEFAULT_USER_ROLE = "ROLE_USER";
    public static final String SYSTEM_USER = "SYSTEM";
    public static final String ANONYMOUS_USER = "ANONYMOUS";
    
    // ==================== Business Constants ====================
    
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 20;
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final long ACCOUNT_LOCK_DURATION_MINUTES = 30;
    public static final int PASSWORD_EXPIRY_DAYS = 90;
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    
    // ==================== Validation Limits ====================
    
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 500;
    public static final int MAX_ADDRESS_LENGTH = 250;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_PHONE_LENGTH = 15;
    public static final int MAX_CODE_LENGTH = 30;
    public static final int MIN_CODE_LENGTH = 2;
    
    // ==================== Regex Patterns ====================
    
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PHONE_PATTERN = "^[0-9]{10,15}$";
    public static final String MOBILE_PATTERN = "^[0-9]{10}$";
    public static final String CODE_PATTERN = "^[A-Z0-9-]+$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{3,20}$";
    public static final String DECIMAL_PATTERN = "^\\d+(\\.\\d{1,2})?$";
    public static final String PERCENTAGE_PATTERN = "^(100(\\.0{1,2})?|\\d{1,2}(\\.\\d{1,2})?)$";
    
    // ==================== HTTP Headers ====================
    
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_BEARER_PREFIX = "Bearer ";
    public static final String HEADER_API_KEY = "X-API-Key";
    public static final String HEADER_REQUEST_ID = "X-Request-ID";
    public static final String HEADER_TRACE_ID = "X-Trace-ID";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    
    // ==================== Content Types ====================
    
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_PDF = "application/pdf";
    public static final String CONTENT_TYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String CONTENT_TYPE_CSV = "text/csv";
    public static final String CONTENT_TYPE_HTML = "text/html";
    
    // ==================== API Versioning ====================
    
    public static final String API_VERSION_V1 = "v1";
    public static final String API_BASE_PATH = "/api/" + API_VERSION_V1;
    
    // ==================== Success Messages ====================
    
    public static final String MSG_SUCCESS = "Operation completed successfully";
    public static final String MSG_CREATED = "Record created successfully";
    public static final String MSG_UPDATED = "Record updated successfully";
    public static final String MSG_DELETED = "Record deleted successfully";
    public static final String MSG_RESTORED = "Record restored successfully";
    public static final String MSG_ACTIVATED = "Record activated successfully";
    public static final String MSG_DEACTIVATED = "Record deactivated successfully";
    
    // ==================== Sync Constants ====================
    
    public static final int SYNC_BATCH_SIZE = 100;
    public static final int SYNC_RETRY_ATTEMPTS = 3;
    public static final long SYNC_INTERVAL_MINUTES = 15;
    public static final String SYNC_STATUS_PENDING = "PENDING";
    public static final String SYNC_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String SYNC_STATUS_COMPLETED = "COMPLETED";
    public static final String SYNC_STATUS_FAILED = "FAILED";
    
    // ==================== Export Constants ====================
    
    public static final String EXPORT_FORMAT_EXCEL = "EXCEL";
    public static final String EXPORT_FORMAT_PDF = "PDF";
    public static final String EXPORT_FORMAT_CSV = "CSV";
    public static final int EXPORT_MAX_ROWS = 10000;
    
    // ==================== Notification Constants ====================
    
    public static final String NOTIFICATION_TYPE_INFO = "INFO";
    public static final String NOTIFICATION_TYPE_WARNING = "WARNING";
    public static final String NOTIFICATION_TYPE_ERROR = "ERROR";
    public static final String NOTIFICATION_TYPE_SUCCESS = "SUCCESS";
    
    // ==================== Audit Constants ====================
    
    public static final String AUDIT_ACTION_CREATE = "CREATE";
    public static final String AUDIT_ACTION_UPDATE = "UPDATE";
    public static final String AUDIT_ACTION_DELETE = "DELETE";
    public static final String AUDIT_ACTION_VIEW = "VIEW";
    public static final String AUDIT_ACTION_EXPORT = "EXPORT";
    public static final String AUDIT_ACTION_IMPORT = "IMPORT";
    public static final String AUDIT_ACTION_LOGIN = "LOGIN";
    public static final String AUDIT_ACTION_LOGOUT = "LOGOUT";
    
    // ==================== Status Messages ====================
    
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_DRAFT = "DRAFT";
    
    // ==================== Response Codes ====================
    
    public static final String SUCCESS_CODE = "SUCCESS";
    public static final String ERROR_CODE = "ERROR";
    public static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";
    public static final String NOT_FOUND_CODE = "NOT_FOUND";
    public static final String UNAUTHORIZED_CODE = "UNAUTHORIZED";
    public static final String FORBIDDEN_CODE = "FORBIDDEN";
    
    // ==================== Feature Flags ====================
    
    public static final String FEATURE_MOBILE_SYNC = "mobile.sync.enabled";
    public static final String FEATURE_EMAIL_NOTIFICATIONS = "email.notifications.enabled";
    public static final String FEATURE_SMS_NOTIFICATIONS = "sms.notifications.enabled";
    public static final String FEATURE_TWO_FACTOR_AUTH = "security.two.factor.enabled";
    public static final String FEATURE_AUDIT_LOGGING = "audit.logging.enabled";
    public static final String FEATURE_CACHE_ENABLED = "cache.enabled";
    
    // ==================== Configuration Keys ====================
    
    public static final String CONFIG_COMPANY_NAME = "company.name";
    public static final String CONFIG_COMPANY_EMAIL = "company.email";
    public static final String CONFIG_COMPANY_PHONE = "company.phone";
    public static final String CONFIG_TAX_RATE = "tax.rate";
    public static final String CONFIG_CURRENCY = "default.currency";
    public static final String CONFIG_TIMEZONE = "default.timezone";
    public static final String CONFIG_DATE_FORMAT = "default.date.format";
    
    // ==================== Logging Constants ====================
    
    public static final String LOG_PREFIX = "[Epic Green ERP]";
    public static final String LOG_SEPARATOR = " | ";
    
    // ==================== Special Characters ====================
    
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String COLON = ":";
    public static final String PIPE = "|";
    public static final String DASH = "-";
    public static final String UNDERSCORE = "_";
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String NEWLINE = "\n";
    public static final String TAB = "\t";
}
