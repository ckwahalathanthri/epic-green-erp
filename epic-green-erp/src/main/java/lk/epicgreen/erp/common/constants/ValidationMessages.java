package lk.epicgreen.erp.common.constants;

/**
 * Validation messages constants
 * Contains all validation messages for input validation
 * Used with @NotBlank, @Size, @Pattern, etc. annotations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class ValidationMessages {
    
    // Prevent instantiation
    private ValidationMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Required Field Messages ====================
    
    public static final String FIELD_REQUIRED = "{field.name} is required";
    public static final String PRODUCT_CODE_REQUIRED = "Product code is required";
    public static final String PRODUCT_NAME_REQUIRED = "Product name is required";
    public static final String PRODUCT_TYPE_REQUIRED = "Product type is required";
    public static final String SUPPLIER_CODE_REQUIRED = "Supplier code is required";
    public static final String SUPPLIER_NAME_REQUIRED = "Supplier name is required";
    public static final String CUSTOMER_CODE_REQUIRED = "Customer code is required";
    public static final String CUSTOMER_NAME_REQUIRED = "Customer name is required";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String PHONE_REQUIRED = "Phone number is required";
    public static final String ADDRESS_REQUIRED = "Address is required";
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String ROLE_REQUIRED = "Role is required";
    public static final String ROLE_CODE_REQUIRED = "Role code is required";
    public static final String ROLE_NAME_REQUIRED = "Role name is required";
    public static final String ROLES_REQUIRED = "At least one role is required";
    public static final String PERMISSIONS_REQUIRED = "At least one permission is required";
    public static final String WAREHOUSE_REQUIRED = "Warehouse is required";
    public static final String CATEGORY_REQUIRED = "Category is required";
    public static final String UOM_REQUIRED = "Unit of measure is required";
    public static final String QUANTITY_REQUIRED = "Quantity is required";
    public static final String PRICE_REQUIRED = "Price is required";
    public static final String DATE_REQUIRED = "Date is required";
    public static final String STATUS_REQUIRED = "Status is required";
    
    // ==================== String Length Messages ====================
    
    public static final String MIN_LENGTH = "{field.name} must be at least {min} characters";
    public static final String MAX_LENGTH = "{field.name} must not exceed {max} characters";
    public static final String LENGTH_RANGE = "{field.name} must be between {min} and {max} characters";
    
    public static final String CODE_SIZE = "Code must be between 2 and 30 characters";
    public static final String NAME_SIZE = "Name must be between 2 and 100 characters";
    public static final String USERNAME_SIZE = "Username must be between 3 and 50 characters";
    public static final String CODE_MIN_LENGTH = "Code must be at least 2 characters";
    public static final String CODE_MAX_LENGTH = "Code must not exceed 30 characters";
    public static final String NAME_MIN_LENGTH = "Name must be at least 2 characters";
    public static final String NAME_MAX_LENGTH = "Name must not exceed 100 characters";
    public static final String DESCRIPTION_MAX_LENGTH = "Description must not exceed 500 characters";
    public static final String NOTES_MAX_LENGTH = "Notes must not exceed 1000 characters";
    public static final String ADDRESS_MAX_LENGTH = "Address must not exceed 250 characters";
    public static final String EMAIL_MAX_LENGTH = "Email must not exceed 100 characters";
    public static final String PHONE_MAX_LENGTH = "Phone number must not exceed 15 characters";
    public static final String USERNAME_LENGTH = "Username must be between 3 and 20 characters";
    public static final String PASSWORD_MIN_LENGTH = "Password must be at least 8 characters";
    public static final String PASSWORD_MAX_LENGTH = "Password must not exceed 20 characters";
    public static final String EMPLOYEE_CODE_MAX_LENGTH = "Employee code must not exceed 20 characters";
    public static final String DEPARTMENT_MAX_LENGTH = "Department must not exceed 50 characters";
    public static final String DESIGNATION_MAX_LENGTH = "Designation must not exceed 50 characters";
    public static final String URL_MAX_LENGTH = "URL must not exceed 500 characters";
    
    // ==================== Pattern/Format Messages ====================
    
    public static final String INVALID_FORMAT = "{field.name} has invalid format";
    public static final String EMAIL_INVALID = "Invalid email address format";
    public static final String PHONE_INVALID = "Invalid phone number format (10-15 digits)";
    public static final String MOBILE_INVALID = "Invalid mobile number format (10 digits)";
    public static final String CODE_INVALID = "Code must contain only uppercase letters, numbers, and hyphens";
    public static final String USERNAME_INVALID = "Username can only contain letters, numbers, dots, underscores, and hyphens";
    public static final String DATE_INVALID = "Invalid date format (yyyy-MM-dd)";
    public static final String DATETIME_INVALID = "Invalid datetime format (yyyy-MM-dd HH:mm:ss)";
    public static final String URL_INVALID = "Invalid URL format";
    public static final String DECIMAL_INVALID = "Invalid decimal format";
    public static final String PERCENTAGE_INVALID = "Invalid percentage format";
    
    // ==================== Numeric Value Messages ====================
    
    public static final String VALUE_POSITIVE = "{field.name} must be positive";
    public static final String VALUE_NON_NEGATIVE = "{field.name} cannot be negative";
    public static final String VALUE_GREATER_THAN_ZERO = "{field.name} must be greater than zero";
    public static final String VALUE_MIN = "{field.name} must be at least {value}";
    public static final String VALUE_MAX = "{field.name} must not exceed {value}";
    public static final String VALUE_RANGE = "{field.name} must be between {min} and {max}";
    
    public static final String QUANTITY_POSITIVE = "Quantity must be greater than zero";
    public static final String QUANTITY_MIN = "Quantity must be at least 1";
    public static final String PRICE_POSITIVE = "Price must be greater than zero";
    public static final String PRICE_MIN = "Price must be at least 0.01";
    public static final String DISCOUNT_RANGE = "Discount must be between 0 and 100";
    public static final String PERCENTAGE_RANGE = "Percentage must be between 0 and 100";
    public static final String CREDIT_LIMIT_POSITIVE = "Credit limit must be positive";
    public static final String STOCK_NON_NEGATIVE = "Stock quantity cannot be negative";
    
    // ==================== Date/Time Messages ====================
    
    public static final String DATE_PAST = "Date cannot be in the past";
    public static final String DATE_FUTURE = "Date cannot be in the future";
    public static final String DATE_RANGE_INVALID = "End date must be after start date";
    public static final String START_DATE_REQUIRED = "Start date is required";
    public static final String END_DATE_REQUIRED = "End date is required";
    public static final String EXPIRY_DATE_FUTURE = "Expiry date must be in the future";
    
    // ==================== Relationship Messages ====================
    
    public static final String PRODUCT_ID_REQUIRED = "Product ID is required";
    public static final String SUPPLIER_ID_REQUIRED = "Supplier ID is required";
    public static final String CUSTOMER_ID_REQUIRED = "Customer ID is required";
    public static final String WAREHOUSE_ID_REQUIRED = "Warehouse ID is required";
    public static final String CATEGORY_ID_REQUIRED = "Category ID is required";
    public static final String UOM_ID_REQUIRED = "Unit of measure ID is required";
    public static final String USER_ID_REQUIRED = "User ID is required";
    public static final String ORDER_ID_REQUIRED = "Order ID is required";
    
    // ==================== Collection Messages ====================
    
    public static final String LIST_NOT_EMPTY = "{field.name} list cannot be empty";
    public static final String LIST_SIZE_MIN = "{field.name} list must contain at least {min} items";
    public static final String LIST_SIZE_MAX = "{field.name} list must not exceed {max} items";
    public static final String ORDER_ITEMS_REQUIRED = "Order must contain at least one item";
    public static final String CONTACTS_MIN = "At least one contact is required";
    
    // ==================== Business Logic Messages ====================
    
    public static final String DUPLICATE_NOT_ALLOWED = "Duplicate {field.name} is not allowed";
    public static final String UNIQUE_CODE_VIOLATION = "Code already exists";
    public static final String UNIQUE_NAME_VIOLATION = "Name already exists";
    public static final String UNIQUE_EMAIL_VIOLATION = "Email already exists";
    public static final String UNIQUE_PHONE_VIOLATION = "Phone number already exists";
    public static final String UNIQUE_USERNAME_VIOLATION = "Username already exists";
    
    // ==================== File Upload Messages ====================
    
    public static final String FILE_REQUIRED = "File is required";
    public static final String FILE_NOT_EMPTY = "File cannot be empty";
    public static final String FILE_SIZE_MAX = "File size must not exceed {size} MB";
    public static final String FILE_TYPE_INVALID = "Invalid file type. Allowed types: {types}";
    public static final String IMAGE_REQUIRED = "Image is required";
    public static final String IMAGE_SIZE_MAX = "Image size must not exceed {size} MB";
    
    // ==================== Password Messages ====================
    
    public static final String CURRENT_PASSWORD_REQUIRED = "Current password is required";
    public static final String NEW_PASSWORD_REQUIRED = "New password is required";
    public static final String CONFIRM_PASSWORD_REQUIRED = "Confirm password is required";
    public static final String PASSWORD_PATTERN = "Password must be at least 8 characters with uppercase, lowercase, digit, and special character";
    public static final String PASSWORD_WEAK = "Password is too weak";
    public static final String PASSWORD_MISMATCH = "Passwords do not match";
    public static final String PASSWORD_CONFIRM_REQUIRED = "Password confirmation is required";
    public static final String PASSWORD_UPPERCASE_REQUIRED = "Password must contain at least one uppercase letter";
    public static final String PASSWORD_LOWERCASE_REQUIRED = "Password must contain at least one lowercase letter";
    public static final String PASSWORD_DIGIT_REQUIRED = "Password must contain at least one digit";
    public static final String PASSWORD_SPECIAL_REQUIRED = "Password must contain at least one special character";
    
    // ==================== Product Specific Messages ====================
    
    public static final String REORDER_LEVEL_POSITIVE = "Reorder level must be positive";
    public static final String MIN_STOCK_POSITIVE = "Minimum stock level must be positive";
    public static final String MAX_STOCK_POSITIVE = "Maximum stock level must be positive";
    public static final String MIN_LESS_THAN_MAX = "Minimum stock must be less than maximum stock";
    public static final String SHELF_LIFE_POSITIVE = "Shelf life must be positive";
    public static final String SKU_INVALID = "SKU format is invalid";
    public static final String BARCODE_INVALID = "Barcode format is invalid";
    public static final String HSN_CODE_INVALID = "HSN code format is invalid";
    
    // ==================== Order Specific Messages ====================
    
    public static final String ORDER_NUMBER_REQUIRED = "Order number is required";
    public static final String ORDER_DATE_REQUIRED = "Order date is required";
    public static final String ORDER_TOTAL_POSITIVE = "Order total must be greater than zero";
    public static final String LINE_ITEM_QUANTITY_POSITIVE = "Line item quantity must be greater than zero";
    public static final String LINE_ITEM_PRICE_POSITIVE = "Line item price must be greater than zero";
    
    // ==================== Payment Specific Messages ====================
    
    public static final String PAYMENT_AMOUNT_POSITIVE = "Payment amount must be greater than zero";
    public static final String PAYMENT_METHOD_REQUIRED = "Payment method is required";
    public static final String PAYMENT_DATE_REQUIRED = "Payment date is required";
    public static final String CHEQUE_NUMBER_REQUIRED = "Cheque number is required";
    public static final String CHEQUE_DATE_REQUIRED = "Cheque date is required";
    public static final String BANK_NAME_REQUIRED = "Bank name is required";
    
    // ==================== Address Specific Messages ====================
    
    public static final String ADDRESS_LINE1_REQUIRED = "Address line 1 is required";
    public static final String CITY_REQUIRED = "City is required";
    public static final String STATE_REQUIRED = "State/Province is required";
    public static final String COUNTRY_REQUIRED = "Country is required";
    public static final String ZIP_CODE_REQUIRED = "ZIP/Postal code is required";
    public static final String ZIP_CODE_INVALID = "Invalid ZIP/Postal code format";
    
    // ==================== Contact Specific Messages ====================
    
    public static final String CONTACT_NAME_REQUIRED = "Contact name is required";
    public static final String CONTACT_TYPE_REQUIRED = "Contact type is required";
    
    // ==================== Inventory Specific Messages ====================
    
    public static final String ADJUSTMENT_QUANTITY_NON_ZERO = "Adjustment quantity cannot be zero";
    public static final String ADJUSTMENT_REASON_REQUIRED = "Adjustment reason is required";
    public static final String TRANSFER_FROM_WAREHOUSE_REQUIRED = "Source warehouse is required";
    public static final String TRANSFER_TO_WAREHOUSE_REQUIRED = "Destination warehouse is required";
    public static final String TRANSFER_WAREHOUSES_DIFFERENT = "Source and destination warehouses must be different";
    
    // ==================== Production Specific Messages ====================
    
    public static final String BOM_REQUIRED = "Bill of Materials is required";
    public static final String BOM_ITEMS_REQUIRED = "BOM must contain at least one item";
    public static final String MATERIAL_QUANTITY_POSITIVE = "Material quantity must be greater than zero";
    public static final String OUTPUT_QUANTITY_POSITIVE = "Output quantity must be greater than zero";
    public static final String WASTAGE_NON_NEGATIVE = "Wastage quantity cannot be negative";
    
    // ==================== Accounting Specific Messages ====================
    
    public static final String ACCOUNT_CODE_REQUIRED = "Account code is required";
    public static final String ACCOUNT_NAME_REQUIRED = "Account name is required";
    public static final String JOURNAL_ENTRIES_REQUIRED = "Journal must contain at least two entries";
    public static final String DEBIT_CREDIT_BALANCED = "Debit and credit amounts must be equal";
    public static final String AMOUNT_NON_NEGATIVE = "Amount cannot be negative";
    
    // ==================== Tax Specific Messages ====================
    
    public static final String TAX_RATE_REQUIRED = "Tax rate is required";
    public static final String TAX_RATE_RANGE = "Tax rate must be between 0 and 100";
    public static final String TAX_CODE_REQUIRED = "Tax code is required";
    
    // ==================== Discount Specific Messages ====================
    
    public static final String DISCOUNT_VALUE_REQUIRED = "Discount value is required";
    public static final String DISCOUNT_TYPE_REQUIRED = "Discount type is required";
    public static final String DISCOUNT_CODE_REQUIRED = "Discount code is required";
    
    // ==================== System/Configuration Messages ====================
    
    public static final String CONFIG_KEY_REQUIRED = "Configuration key is required";
    public static final String CONFIG_VALUE_REQUIRED = "Configuration value is required";
    public static final String SETTING_NAME_REQUIRED = "Setting name is required";
}
