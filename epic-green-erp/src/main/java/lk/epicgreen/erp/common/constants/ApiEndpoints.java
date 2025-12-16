package lk.epicgreen.erp.common.constants;

/**
 * API Endpoints constants
 * Contains all API endpoint paths used in the application
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class ApiEndpoints {
    
    // Prevent instantiation
    private ApiEndpoints() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Base Paths ====================
    
    public static final String API_VERSION = "v1";
    public static final String API_BASE = "/api/" + API_VERSION;
    
    // ==================== Authentication & Authorization ====================
    
    public static final String AUTH_BASE = API_BASE + "/auth";
    public static final String AUTH_LOGIN = AUTH_BASE + "/login";
    public static final String AUTH_LOGOUT = AUTH_BASE + "/logout";
    public static final String AUTH_REFRESH = AUTH_BASE + "/refresh";
    public static final String AUTH_REGISTER = AUTH_BASE + "/register";
    public static final String AUTH_VERIFY = AUTH_BASE + "/verify";
    public static final String AUTH_FORGOT_PASSWORD = AUTH_BASE + "/forgot-password";
    public static final String AUTH_RESET_PASSWORD = AUTH_BASE + "/reset-password";
    public static final String AUTH_CHANGE_PASSWORD = AUTH_BASE + "/change-password";
    public static final String AUTH_ME = AUTH_BASE + "/me";
    
    // ==================== User Management ====================
    
    public static final String USERS_BASE = API_BASE + "/users";
    public static final String USERS_BY_ID = USERS_BASE + "/{id}";
    public static final String USERS_SEARCH = USERS_BASE + "/search";
    public static final String USERS_DROPDOWN = USERS_BASE + "/dropdown";
    public static final String USERS_LOCK = USERS_BASE + "/{id}/lock";
    public static final String USERS_UNLOCK = USERS_BASE + "/{id}/unlock";
    public static final String USERS_RESET_PASSWORD = USERS_BASE + "/{id}/reset-password";
    public static final String USERS_ROLES = USERS_BASE + "/{id}/roles";
    
    // ==================== Role Management ====================
    
    public static final String ROLES_BASE = API_BASE + "/roles";
    public static final String ROLES_BY_ID = ROLES_BASE + "/{id}";
    public static final String ROLES_DROPDOWN = ROLES_BASE + "/dropdown";
    public static final String ROLES_PERMISSIONS = ROLES_BASE + "/{id}/permissions";
    
    // ==================== Product Management ====================
    
    public static final String PRODUCTS_BASE = API_BASE + "/products";
    public static final String PRODUCTS_BY_ID = PRODUCTS_BASE + "/{id}";
    public static final String PRODUCTS_BY_CODE = PRODUCTS_BASE + "/code/{code}";
    public static final String PRODUCTS_SEARCH = PRODUCTS_BASE + "/search";
    public static final String PRODUCTS_DROPDOWN = PRODUCTS_BASE + "/dropdown";
    public static final String PRODUCTS_LOW_STOCK = PRODUCTS_BASE + "/low-stock";
    public static final String PRODUCTS_BY_TYPE = PRODUCTS_BASE + "/type/{type}";
    public static final String PRODUCTS_BY_CATEGORY = PRODUCTS_BASE + "/category/{categoryId}";
    public static final String PRODUCTS_IMPORT = PRODUCTS_BASE + "/import";
    public static final String PRODUCTS_EXPORT = PRODUCTS_BASE + "/export";
    public static final String PRODUCTS_BULK_DELETE = PRODUCTS_BASE + "/bulk-delete";
    public static final String PRODUCTS_RESTORE = PRODUCTS_BASE + "/{id}/restore";
    public static final String PRODUCTS_TOGGLE_STATUS = PRODUCTS_BASE + "/{id}/toggle-status";
    
    // ==================== Product Categories ====================
    
    public static final String CATEGORIES_BASE = API_BASE + "/categories";
    public static final String CATEGORIES_BY_ID = CATEGORIES_BASE + "/{id}";
    public static final String CATEGORIES_DROPDOWN = CATEGORIES_BASE + "/dropdown";
    public static final String CATEGORIES_TREE = CATEGORIES_BASE + "/tree";
    
    // ==================== Supplier Management ====================
    
    public static final String SUPPLIERS_BASE = API_BASE + "/suppliers";
    public static final String SUPPLIERS_BY_ID = SUPPLIERS_BASE + "/{id}";
    public static final String SUPPLIERS_BY_CODE = SUPPLIERS_BASE + "/code/{code}";
    public static final String SUPPLIERS_SEARCH = SUPPLIERS_BASE + "/search";
    public static final String SUPPLIERS_DROPDOWN = SUPPLIERS_BASE + "/dropdown";
    public static final String SUPPLIERS_LEDGER = SUPPLIERS_BASE + "/{id}/ledger";
    public static final String SUPPLIERS_CONTACTS = SUPPLIERS_BASE + "/{id}/contacts";
    public static final String SUPPLIERS_BLOCK = SUPPLIERS_BASE + "/{id}/block";
    public static final String SUPPLIERS_UNBLOCK = SUPPLIERS_BASE + "/{id}/unblock";
    
    // ==================== Customer Management ====================
    
    public static final String CUSTOMERS_BASE = API_BASE + "/customers";
    public static final String CUSTOMERS_BY_ID = CUSTOMERS_BASE + "/{id}";
    public static final String CUSTOMERS_BY_CODE = CUSTOMERS_BASE + "/code/{code}";
    public static final String CUSTOMERS_SEARCH = CUSTOMERS_BASE + "/search";
    public static final String CUSTOMERS_DROPDOWN = CUSTOMERS_BASE + "/dropdown";
    public static final String CUSTOMERS_LEDGER = CUSTOMERS_BASE + "/{id}/ledger";
    public static final String CUSTOMERS_CONTACTS = CUSTOMERS_BASE + "/{id}/contacts";
    public static final String CUSTOMERS_ADDRESSES = CUSTOMERS_BASE + "/{id}/addresses";
    public static final String CUSTOMERS_PRICE_LISTS = CUSTOMERS_BASE + "/{id}/price-lists";
    public static final String CUSTOMERS_BLOCK = CUSTOMERS_BASE + "/{id}/block";
    public static final String CUSTOMERS_UNBLOCK = CUSTOMERS_BASE + "/{id}/unblock";
    public static final String CUSTOMERS_CREDIT_LIMIT = CUSTOMERS_BASE + "/{id}/credit-limit";
    
    // ==================== Warehouse Management ====================
    
    public static final String WAREHOUSES_BASE = API_BASE + "/warehouses";
    public static final String WAREHOUSES_BY_ID = WAREHOUSES_BASE + "/{id}";
    public static final String WAREHOUSES_DROPDOWN = WAREHOUSES_BASE + "/dropdown";
    public static final String WAREHOUSES_LOCATIONS = WAREHOUSES_BASE + "/{id}/locations";
    public static final String WAREHOUSES_INVENTORY = WAREHOUSES_BASE + "/{id}/inventory";
    
    // ==================== Inventory Management ====================
    
    public static final String INVENTORY_BASE = API_BASE + "/inventory";
    public static final String INVENTORY_BY_PRODUCT = INVENTORY_BASE + "/product/{productId}";
    public static final String INVENTORY_BY_WAREHOUSE = INVENTORY_BASE + "/warehouse/{warehouseId}";
    public static final String INVENTORY_ADJUSTMENTS = INVENTORY_BASE + "/adjustments";
    public static final String INVENTORY_TRANSFERS = INVENTORY_BASE + "/transfers";
    public static final String INVENTORY_STOCK_COUNT = INVENTORY_BASE + "/stock-count";
    public static final String INVENTORY_MOVEMENTS = INVENTORY_BASE + "/movements";
    
    // ==================== Purchase Management ====================
    
    public static final String PURCHASE_ORDERS_BASE = API_BASE + "/purchase-orders";
    public static final String PURCHASE_ORDERS_BY_ID = PURCHASE_ORDERS_BASE + "/{id}";
    public static final String PURCHASE_ORDERS_SEARCH = PURCHASE_ORDERS_BASE + "/search";
    public static final String PURCHASE_ORDERS_APPROVE = PURCHASE_ORDERS_BASE + "/{id}/approve";
    public static final String PURCHASE_ORDERS_CANCEL = PURCHASE_ORDERS_BASE + "/{id}/cancel";
    public static final String PURCHASE_ORDERS_RECEIVE = PURCHASE_ORDERS_BASE + "/{id}/receive";
    
    public static final String GRN_BASE = API_BASE + "/grn";
    public static final String GRN_BY_ID = GRN_BASE + "/{id}";
    public static final String GRN_APPROVE = GRN_BASE + "/{id}/approve";
    
    // ==================== Production Management ====================
    
    public static final String PRODUCTION_BASE = API_BASE + "/production";
    public static final String PRODUCTION_ORDERS_BASE = PRODUCTION_BASE + "/orders";
    public static final String PRODUCTION_ORDERS_BY_ID = PRODUCTION_ORDERS_BASE + "/{id}";
    public static final String PRODUCTION_ORDERS_START = PRODUCTION_ORDERS_BASE + "/{id}/start";
    public static final String PRODUCTION_ORDERS_COMPLETE = PRODUCTION_ORDERS_BASE + "/{id}/complete";
    
    public static final String BOM_BASE = API_BASE + "/bom";
    public static final String BOM_BY_ID = BOM_BASE + "/{id}";
    public static final String BOM_BY_PRODUCT = BOM_BASE + "/product/{productId}";
    
    // ==================== Sales Management ====================
    
    public static final String SALES_ORDERS_BASE = API_BASE + "/sales-orders";
    public static final String SALES_ORDERS_BY_ID = SALES_ORDERS_BASE + "/{id}";
    public static final String SALES_ORDERS_SEARCH = SALES_ORDERS_BASE + "/search";
    public static final String SALES_ORDERS_APPROVE = SALES_ORDERS_BASE + "/{id}/approve";
    public static final String SALES_ORDERS_CANCEL = SALES_ORDERS_BASE + "/{id}/cancel";
    public static final String SALES_ORDERS_DISPATCH = SALES_ORDERS_BASE + "/{id}/dispatch";
    
    public static final String INVOICES_BASE = API_BASE + "/invoices";
    public static final String INVOICES_BY_ID = INVOICES_BASE + "/{id}";
    public static final String INVOICES_SEARCH = INVOICES_BASE + "/search";
    public static final String INVOICES_SEND = INVOICES_BASE + "/{id}/send";
    public static final String INVOICES_APPROVE = INVOICES_BASE + "/{id}/approve";
    
    // ==================== Payment Management ====================
    
    public static final String PAYMENTS_BASE = API_BASE + "/payments";
    public static final String PAYMENTS_BY_ID = PAYMENTS_BASE + "/{id}";
    public static final String PAYMENTS_SEARCH = PAYMENTS_BASE + "/search";
    public static final String PAYMENTS_VERIFY = PAYMENTS_BASE + "/{id}/verify";
    public static final String PAYMENTS_ALLOCATE = PAYMENTS_BASE + "/{id}/allocate";
    public static final String PAYMENTS_CHEQUES = PAYMENTS_BASE + "/cheques";
    public static final String PAYMENTS_CHEQUE_CLEAR = PAYMENTS_BASE + "/cheques/{id}/clear";
    public static final String PAYMENTS_CHEQUE_BOUNCE = PAYMENTS_BASE + "/cheques/{id}/bounce";
    
    // ==================== Returns Management ====================
    
    public static final String RETURNS_BASE = API_BASE + "/returns";
    public static final String RETURNS_BY_ID = RETURNS_BASE + "/{id}";
    public static final String RETURNS_APPROVE = RETURNS_BASE + "/{id}/approve";
    public static final String RETURNS_PROCESS = RETURNS_BASE + "/{id}/process";
    
    public static final String CREDIT_NOTES_BASE = API_BASE + "/credit-notes";
    public static final String CREDIT_NOTES_BY_ID = CREDIT_NOTES_BASE + "/{id}";
    
    // ==================== Accounting ====================
    
    public static final String ACCOUNTS_BASE = API_BASE + "/accounts";
    public static final String ACCOUNTS_BY_ID = ACCOUNTS_BASE + "/{id}";
    public static final String ACCOUNTS_CHART = ACCOUNTS_BASE + "/chart";
    
    public static final String JOURNALS_BASE = API_BASE + "/journals";
    public static final String JOURNALS_BY_ID = JOURNALS_BASE + "/{id}";
    public static final String JOURNALS_POST = JOURNALS_BASE + "/{id}/post";
    
    public static final String LEDGER_BASE = API_BASE + "/ledger";
    public static final String LEDGER_GENERAL = LEDGER_BASE + "/general";
    public static final String LEDGER_TRIAL_BALANCE = LEDGER_BASE + "/trial-balance";
    
    // ==================== Reports ====================
    
    public static final String REPORTS_BASE = API_BASE + "/reports";
    public static final String REPORTS_SALES = REPORTS_BASE + "/sales";
    public static final String REPORTS_PURCHASE = REPORTS_BASE + "/purchase";
    public static final String REPORTS_INVENTORY = REPORTS_BASE + "/inventory";
    public static final String REPORTS_PRODUCTION = REPORTS_BASE + "/production";
    public static final String REPORTS_FINANCIAL = REPORTS_BASE + "/financial";
    public static final String REPORTS_CUSTOM = REPORTS_BASE + "/custom";
    public static final String REPORTS_EXPORT = REPORTS_BASE + "/export";
    
    // ==================== Dashboard ====================
    
    public static final String DASHBOARD_BASE = API_BASE + "/dashboard";
    public static final String DASHBOARD_STATS = DASHBOARD_BASE + "/stats";
    public static final String DASHBOARD_CHARTS = DASHBOARD_BASE + "/charts";
    public static final String DASHBOARD_WIDGETS = DASHBOARD_BASE + "/widgets";
    
    // ==================== Mobile Sync ====================
    
    public static final String SYNC_BASE = API_BASE + "/sync";
    public static final String SYNC_PULL = SYNC_BASE + "/pull";
    public static final String SYNC_PUSH = SYNC_BASE + "/push";
    public static final String SYNC_STATUS = SYNC_BASE + "/status";
    public static final String SYNC_CONFLICTS = SYNC_BASE + "/conflicts";
    
    // ==================== Notifications ====================
    
    public static final String NOTIFICATIONS_BASE = API_BASE + "/notifications";
    public static final String NOTIFICATIONS_UNREAD = NOTIFICATIONS_BASE + "/unread";
    public static final String NOTIFICATIONS_MARK_READ = NOTIFICATIONS_BASE + "/{id}/mark-read";
    public static final String NOTIFICATIONS_MARK_ALL_READ = NOTIFICATIONS_BASE + "/mark-all-read";
    
    // ==================== Audit Logs ====================
    
    public static final String AUDIT_BASE = API_BASE + "/audit";
    public static final String AUDIT_LOGS = AUDIT_BASE + "/logs";
    public static final String AUDIT_ACTIVITY = AUDIT_BASE + "/activity";
    public static final String AUDIT_ERRORS = AUDIT_BASE + "/errors";
    
    // ==================== Settings ====================
    
    public static final String SETTINGS_BASE = API_BASE + "/settings";
    public static final String SETTINGS_COMPANY = SETTINGS_BASE + "/company";
    public static final String SETTINGS_SYSTEM = SETTINGS_BASE + "/system";
    public static final String SETTINGS_USER = SETTINGS_BASE + "/user";
    
    // ==================== System ====================
    
    public static final String SYSTEM_BASE = API_BASE + "/system";
    public static final String SYSTEM_HEALTH = SYSTEM_BASE + "/health";
    public static final String SYSTEM_INFO = SYSTEM_BASE + "/info";
    public static final String SYSTEM_CACHE_CLEAR = SYSTEM_BASE + "/cache/clear";
    public static final String SYSTEM_CACHE_REFRESH = SYSTEM_BASE + "/cache/refresh";
    
    // ==================== File Management ====================
    
    public static final String FILES_BASE = API_BASE + "/files";
    public static final String FILES_UPLOAD = FILES_BASE + "/upload";
    public static final String FILES_DOWNLOAD = FILES_BASE + "/download/{id}";
    public static final String FILES_DELETE = FILES_BASE + "/{id}";
    
    // ==================== UOM (Units of Measure) ====================
    
    public static final String UOM_BASE = API_BASE + "/uom";
    public static final String UOM_BY_ID = UOM_BASE + "/{id}";
    public static final String UOM_DROPDOWN = UOM_BASE + "/dropdown";
}
