package lk.epicgreen.erp.common.constants;

/**
 * Roles and Permissions constants
 * Contains all role and permission definitions for RBAC
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class RolesAndPermissions {
    
    // Prevent instantiation
    private RolesAndPermissions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== System Roles ====================
    
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";
    public static final String ROLE_SUPERVISOR = "ROLE_SUPERVISOR";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_GUEST = "ROLE_GUEST";
    
    // ==================== Department Roles ====================
    
    public static final String ROLE_WAREHOUSE_MANAGER = "ROLE_WAREHOUSE_MANAGER";
    public static final String ROLE_WAREHOUSE_STAFF = "ROLE_WAREHOUSE_STAFF";
    public static final String ROLE_PRODUCTION_MANAGER = "ROLE_PRODUCTION_MANAGER";
    public static final String ROLE_PRODUCTION_STAFF = "ROLE_PRODUCTION_STAFF";
    public static final String ROLE_SALES_MANAGER = "ROLE_SALES_MANAGER";
    public static final String ROLE_SALES_REP = "ROLE_SALES_REP";
    public static final String ROLE_PURCHASE_MANAGER = "ROLE_PURCHASE_MANAGER";
    public static final String ROLE_PURCHASE_OFFICER = "ROLE_PURCHASE_OFFICER";
    public static final String ROLE_ACCOUNTANT = "ROLE_ACCOUNTANT";
    public static final String ROLE_FINANCE_MANAGER = "ROLE_FINANCE_MANAGER";
    public static final String ROLE_AUDITOR = "ROLE_AUDITOR";
    
    // ==================== Mobile Roles ====================
    
    public static final String ROLE_MOBILE_USER = "ROLE_MOBILE_USER";
    public static final String ROLE_FIELD_SALES = "ROLE_FIELD_SALES";
    public static final String ROLE_DELIVERY_STAFF = "ROLE_DELIVERY_STAFF";
    
    // ==================== Product Management Permissions ====================
    
    public static final String PERM_VIEW_PRODUCTS = "VIEW_PRODUCTS";
    public static final String PERM_CREATE_PRODUCTS = "CREATE_PRODUCTS";
    public static final String PERM_EDIT_PRODUCTS = "EDIT_PRODUCTS";
    public static final String PERM_DELETE_PRODUCTS = "DELETE_PRODUCTS";
    public static final String PERM_APPROVE_PRODUCTS = "APPROVE_PRODUCTS";
    public static final String PERM_EXPORT_PRODUCTS = "EXPORT_PRODUCTS";
    public static final String PERM_IMPORT_PRODUCTS = "IMPORT_PRODUCTS";
    
    // ==================== Supplier Management Permissions ====================
    
    public static final String PERM_VIEW_SUPPLIERS = "VIEW_SUPPLIERS";
    public static final String PERM_CREATE_SUPPLIERS = "CREATE_SUPPLIERS";
    public static final String PERM_EDIT_SUPPLIERS = "EDIT_SUPPLIERS";
    public static final String PERM_DELETE_SUPPLIERS = "DELETE_SUPPLIERS";
    public static final String PERM_APPROVE_SUPPLIERS = "APPROVE_SUPPLIERS";
    public static final String PERM_BLOCK_SUPPLIERS = "BLOCK_SUPPLIERS";
    
    // ==================== Customer Management Permissions ====================
    
    public static final String PERM_VIEW_CUSTOMERS = "VIEW_CUSTOMERS";
    public static final String PERM_CREATE_CUSTOMERS = "CREATE_CUSTOMERS";
    public static final String PERM_EDIT_CUSTOMERS = "EDIT_CUSTOMERS";
    public static final String PERM_DELETE_CUSTOMERS = "DELETE_CUSTOMERS";
    public static final String PERM_APPROVE_CUSTOMERS = "APPROVE_CUSTOMERS";
    public static final String PERM_BLOCK_CUSTOMERS = "BLOCK_CUSTOMERS";
    public static final String PERM_VIEW_CUSTOMER_LEDGER = "VIEW_CUSTOMER_LEDGER";
    public static final String PERM_EDIT_CREDIT_LIMIT = "EDIT_CREDIT_LIMIT";
    
    // ==================== Warehouse Management Permissions ====================
    
    public static final String PERM_VIEW_WAREHOUSES = "VIEW_WAREHOUSES";
    public static final String PERM_CREATE_WAREHOUSES = "CREATE_WAREHOUSES";
    public static final String PERM_EDIT_WAREHOUSES = "EDIT_WAREHOUSES";
    public static final String PERM_DELETE_WAREHOUSES = "DELETE_WAREHOUSES";
    public static final String PERM_VIEW_INVENTORY = "VIEW_INVENTORY";
    public static final String PERM_ADJUST_INVENTORY = "ADJUST_INVENTORY";
    public static final String PERM_TRANSFER_STOCK = "TRANSFER_STOCK";
    public static final String PERM_STOCK_COUNT = "STOCK_COUNT";
    
    // ==================== Purchase Management Permissions ====================
    
    public static final String PERM_VIEW_PURCHASE_ORDERS = "VIEW_PURCHASE_ORDERS";
    public static final String PERM_CREATE_PURCHASE_ORDERS = "CREATE_PURCHASE_ORDERS";
    public static final String PERM_EDIT_PURCHASE_ORDERS = "EDIT_PURCHASE_ORDERS";
    public static final String PERM_DELETE_PURCHASE_ORDERS = "DELETE_PURCHASE_ORDERS";
    public static final String PERM_APPROVE_PURCHASE_ORDERS = "APPROVE_PURCHASE_ORDERS";
    public static final String PERM_CANCEL_PURCHASE_ORDERS = "CANCEL_PURCHASE_ORDERS";
    public static final String PERM_VIEW_GRN = "VIEW_GRN";
    public static final String PERM_CREATE_GRN = "CREATE_GRN";
    public static final String PERM_APPROVE_GRN = "APPROVE_GRN";
    
    // ==================== Production Management Permissions ====================
    
    public static final String PERM_VIEW_PRODUCTION = "VIEW_PRODUCTION";
    public static final String PERM_CREATE_PRODUCTION = "CREATE_PRODUCTION";
    public static final String PERM_EDIT_PRODUCTION = "EDIT_PRODUCTION";
    public static final String PERM_DELETE_PRODUCTION = "DELETE_PRODUCTION";
    public static final String PERM_START_PRODUCTION = "START_PRODUCTION";
    public static final String PERM_COMPLETE_PRODUCTION = "COMPLETE_PRODUCTION";
    public static final String PERM_VIEW_BOM = "VIEW_BOM";
    public static final String PERM_CREATE_BOM = "CREATE_BOM";
    public static final String PERM_EDIT_BOM = "EDIT_BOM";
    public static final String PERM_APPROVE_BOM = "APPROVE_BOM";
    
    // ==================== Sales Management Permissions ====================
    
    public static final String PERM_VIEW_SALES_ORDERS = "VIEW_SALES_ORDERS";
    public static final String PERM_CREATE_SALES_ORDERS = "CREATE_SALES_ORDERS";
    public static final String PERM_EDIT_SALES_ORDERS = "EDIT_SALES_ORDERS";
    public static final String PERM_DELETE_SALES_ORDERS = "DELETE_SALES_ORDERS";
    public static final String PERM_APPROVE_SALES_ORDERS = "APPROVE_SALES_ORDERS";
    public static final String PERM_CANCEL_SALES_ORDERS = "CANCEL_SALES_ORDERS";
    public static final String PERM_VIEW_INVOICES = "VIEW_INVOICES";
    public static final String PERM_CREATE_INVOICES = "CREATE_INVOICES";
    public static final String PERM_EDIT_INVOICES = "EDIT_INVOICES";
    public static final String PERM_APPROVE_INVOICES = "APPROVE_INVOICES";
    public static final String PERM_SEND_INVOICES = "SEND_INVOICES";
    public static final String PERM_DISPATCH_ORDERS = "DISPATCH_ORDERS";
    
    // ==================== Payment Management Permissions ====================
    
    public static final String PERM_VIEW_PAYMENTS = "VIEW_PAYMENTS";
    public static final String PERM_CREATE_PAYMENTS = "CREATE_PAYMENTS";
    public static final String PERM_EDIT_PAYMENTS = "EDIT_PAYMENTS";
    public static final String PERM_DELETE_PAYMENTS = "DELETE_PAYMENTS";
    public static final String PERM_APPROVE_PAYMENTS = "APPROVE_PAYMENTS";
    public static final String PERM_VERIFY_PAYMENTS = "VERIFY_PAYMENTS";
    public static final String PERM_PROCESS_CHEQUES = "PROCESS_CHEQUES";
    public static final String PERM_ALLOCATE_PAYMENTS = "ALLOCATE_PAYMENTS";
    
    // ==================== Returns Management Permissions ====================
    
    public static final String PERM_VIEW_RETURNS = "VIEW_RETURNS";
    public static final String PERM_CREATE_RETURNS = "CREATE_RETURNS";
    public static final String PERM_EDIT_RETURNS = "EDIT_RETURNS";
    public static final String PERM_APPROVE_RETURNS = "APPROVE_RETURNS";
    public static final String PERM_PROCESS_RETURNS = "PROCESS_RETURNS";
    public static final String PERM_ISSUE_CREDIT_NOTES = "ISSUE_CREDIT_NOTES";
    
    // ==================== Accounting Permissions ====================
    
    public static final String PERM_VIEW_LEDGER = "VIEW_LEDGER";
    public static final String PERM_CREATE_JOURNAL = "CREATE_JOURNAL";
    public static final String PERM_EDIT_JOURNAL = "EDIT_JOURNAL";
    public static final String PERM_POST_JOURNAL = "POST_JOURNAL";
    public static final String PERM_VIEW_TRIAL_BALANCE = "VIEW_TRIAL_BALANCE";
    public static final String PERM_RECONCILE_ACCOUNTS = "RECONCILE_ACCOUNTS";
    public static final String PERM_VIEW_FINANCIAL_REPORTS = "VIEW_FINANCIAL_REPORTS";
    public static final String PERM_CLOSE_PERIOD = "CLOSE_PERIOD";
    
    // ==================== User Management Permissions ====================
    
    public static final String PERM_VIEW_USERS = "VIEW_USERS";
    public static final String PERM_CREATE_USERS = "CREATE_USERS";
    public static final String PERM_EDIT_USERS = "EDIT_USERS";
    public static final String PERM_DELETE_USERS = "DELETE_USERS";
    public static final String PERM_RESET_PASSWORD = "RESET_PASSWORD";
    public static final String PERM_LOCK_USERS = "LOCK_USERS";
    public static final String PERM_UNLOCK_USERS = "UNLOCK_USERS";
    public static final String PERM_ASSIGN_ROLES = "ASSIGN_ROLES";
    
    // ==================== Role Management Permissions ====================
    
    public static final String PERM_VIEW_ROLES = "VIEW_ROLES";
    public static final String PERM_CREATE_ROLES = "CREATE_ROLES";
    public static final String PERM_EDIT_ROLES = "EDIT_ROLES";
    public static final String PERM_DELETE_ROLES = "DELETE_ROLES";
    public static final String PERM_ASSIGN_PERMISSIONS = "ASSIGN_PERMISSIONS";
    
    // ==================== Report Permissions ====================
    
    public static final String PERM_VIEW_REPORTS = "VIEW_REPORTS";
    public static final String PERM_CREATE_REPORTS = "CREATE_REPORTS";
    public static final String PERM_EXPORT_REPORTS = "EXPORT_REPORTS";
    public static final String PERM_SCHEDULE_REPORTS = "SCHEDULE_REPORTS";
    public static final String PERM_VIEW_DASHBOARD = "VIEW_DASHBOARD";
    public static final String PERM_VIEW_ANALYTICS = "VIEW_ANALYTICS";
    
    // ==================== System Configuration Permissions ====================
    
    public static final String PERM_VIEW_SETTINGS = "VIEW_SETTINGS";
    public static final String PERM_EDIT_SETTINGS = "EDIT_SETTINGS";
    public static final String PERM_VIEW_SYSTEM_CONFIG = "VIEW_SYSTEM_CONFIG";
    public static final String PERM_EDIT_SYSTEM_CONFIG = "EDIT_SYSTEM_CONFIG";
    public static final String PERM_MANAGE_CATEGORIES = "MANAGE_CATEGORIES";
    public static final String PERM_MANAGE_UOM = "MANAGE_UOM";
    public static final String PERM_MANAGE_TAX = "MANAGE_TAX";
    
    // ==================== Audit & Logging Permissions ====================
    
    public static final String PERM_VIEW_AUDIT_LOGS = "VIEW_AUDIT_LOGS";
    public static final String PERM_VIEW_ERROR_LOGS = "VIEW_ERROR_LOGS";
    public static final String PERM_VIEW_ACTIVITY_LOGS = "VIEW_ACTIVITY_LOGS";
    public static final String PERM_EXPORT_LOGS = "EXPORT_LOGS";
    
    // ==================== Mobile Sync Permissions ====================
    
    public static final String PERM_SYNC_DATA = "SYNC_DATA";
    public static final String PERM_OFFLINE_ACCESS = "OFFLINE_ACCESS";
    public static final String PERM_VIEW_SYNC_STATUS = "VIEW_SYNC_STATUS";
    public static final String PERM_MANAGE_SYNC = "MANAGE_SYNC";
    
    // ==================== Notification Permissions ====================
    
    public static final String PERM_SEND_NOTIFICATIONS = "SEND_NOTIFICATIONS";
    public static final String PERM_VIEW_NOTIFICATIONS = "VIEW_NOTIFICATIONS";
    public static final String PERM_MANAGE_NOTIFICATIONS = "MANAGE_NOTIFICATIONS";
    
    // ==================== Import/Export Permissions ====================
    
    public static final String PERM_IMPORT_DATA = "IMPORT_DATA";
    public static final String PERM_EXPORT_DATA = "EXPORT_DATA";
    public static final String PERM_BULK_OPERATIONS = "BULK_OPERATIONS";
    
    // ==================== Advanced Permissions ====================
    
    public static final String PERM_VIEW_ALL_DATA = "VIEW_ALL_DATA";
    public static final String PERM_EDIT_ALL_DATA = "EDIT_ALL_DATA";
    public static final String PERM_DELETE_ALL_DATA = "DELETE_ALL_DATA";
    public static final String PERM_BYPASS_APPROVAL = "BYPASS_APPROVAL";
    public static final String PERM_OVERRIDE_VALIDATION = "OVERRIDE_VALIDATION";
    public static final String PERM_ACCESS_API = "ACCESS_API";
    public static final String PERM_MANAGE_CACHE = "MANAGE_CACHE";
    public static final String PERM_SYSTEM_ADMIN = "SYSTEM_ADMIN";
}
