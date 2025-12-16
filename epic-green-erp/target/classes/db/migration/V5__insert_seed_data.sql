-- =====================================================
-- Epic Green ERP - Initial Seed Data
-- =====================================================

USE epic_green_db;

-- =====================================================
-- 1. SEED DATA: ROLES & PERMISSIONS
-- =====================================================

-- Insert Default Roles
INSERT INTO roles (role_name, role_code, description, is_system_role) VALUES
('Super Administrator', 'ROLE_SUPER_ADMIN', 'Full system access', TRUE),
('Administrator', 'ROLE_ADMIN', 'Administrative access', TRUE),
('Manager', 'ROLE_MANAGER', 'Management level access with approval rights', TRUE),
('Sales Representative', 'ROLE_SALES', 'Sales operations and customer management', TRUE),
('Warehouse Manager', 'ROLE_WAREHOUSE_MGR', 'Warehouse operations and inventory management', TRUE),
('Warehouse Staff', 'ROLE_WAREHOUSE_STAFF', 'Warehouse operations', TRUE),
('Production Manager', 'ROLE_PRODUCTION_MGR', 'Production planning and execution', TRUE),
('Production Staff', 'ROLE_PRODUCTION_STAFF', 'Production operations', TRUE),
('Accountant', 'ROLE_ACCOUNTANT', 'Accounting and finance operations', TRUE),
('Purchase Officer', 'ROLE_PURCHASE', 'Purchase operations', TRUE),
('Mobile User', 'ROLE_MOBILE_USER', 'Mobile application access', TRUE);

-- Insert Sample Permissions (Add more as needed)
INSERT INTO permissions (permission_name, permission_code, module) VALUES
-- Admin
('View Users', 'VIEW_USERS', 'ADMIN'),
('Create Users', 'CREATE_USERS', 'ADMIN'),
('Edit Users', 'EDIT_USERS', 'ADMIN'),
('Delete Users', 'DELETE_USERS', 'ADMIN'),
-- Supplier
('View Suppliers', 'VIEW_SUPPLIERS', 'SUPPLIER'),
('Create Suppliers', 'CREATE_SUPPLIERS', 'SUPPLIER'),
('Edit Suppliers', 'EDIT_SUPPLIERS', 'SUPPLIER'),
-- Purchase
('View Purchase Orders', 'VIEW_PURCHASE_ORDERS', 'PURCHASE'),
('Create Purchase Orders', 'CREATE_PURCHASE_ORDERS', 'PURCHASE'),
('Approve Purchase Orders', 'APPROVE_PURCHASE_ORDERS', 'PURCHASE'),
-- Warehouse
('View Inventory', 'VIEW_INVENTORY', 'WAREHOUSE'),
('Manage Stock', 'MANAGE_STOCK', 'WAREHOUSE'),
('Approve Stock Adjustments', 'APPROVE_STOCK_ADJUSTMENTS', 'WAREHOUSE'),
-- Production
('View Production', 'VIEW_PRODUCTION', 'PRODUCTION'),
('Create Work Orders', 'CREATE_WORK_ORDERS', 'PRODUCTION'),
('Execute Production', 'EXECUTE_PRODUCTION', 'PRODUCTION'),
-- Sales
('View Sales Orders', 'VIEW_SALES_ORDERS', 'SALES'),
('Create Sales Orders', 'CREATE_SALES_ORDERS', 'SALES'),
('Approve Sales Orders', 'APPROVE_SALES_ORDERS', 'SALES'),
-- Customer
('View Customers', 'VIEW_CUSTOMERS', 'CUSTOMER'),
('Create Customers', 'CREATE_CUSTOMERS', 'CUSTOMER'),
('Edit Customers', 'EDIT_CUSTOMERS', 'CUSTOMER'),
-- Payment
('View Payments', 'VIEW_PAYMENTS', 'PAYMENT'),
('Receive Payments', 'RECEIVE_PAYMENTS', 'PAYMENT'),
('Approve Payments', 'APPROVE_PAYMENTS', 'PAYMENT'),
-- Accounting
('View Accounts', 'VIEW_ACCOUNTS', 'ACCOUNTING'),
('Create Journal Entries', 'CREATE_JOURNAL_ENTRIES', 'ACCOUNTING'),
('Post Transactions', 'POST_TRANSACTIONS', 'ACCOUNTING'),
-- Reports
('View Reports', 'VIEW_REPORTS', 'REPORTS'),
('Generate Reports', 'GENERATE_REPORTS', 'REPORTS');

-- =====================================================
-- 2. SEED DATA: UNITS OF MEASURE
-- =====================================================

INSERT INTO units_of_measure (uom_code, uom_name, uom_type, base_unit, conversion_factor) VALUES
-- Weight (Base: KG)
('KG', 'Kilogram', 'WEIGHT', TRUE, 1.0000),
('G', 'Gram', 'WEIGHT', FALSE, 0.0010),
('LB', 'Pound', 'WEIGHT', FALSE, 0.4536),
('TON', 'Metric Ton', 'WEIGHT', FALSE, 1000.0000),

-- Volume (Base: L)
('L', 'Liter', 'VOLUME', TRUE, 1.0000),
('ML', 'Milliliter', 'VOLUME', FALSE, 0.0010),
('GAL', 'Gallon', 'VOLUME', FALSE, 3.7854),

-- Length (Base: M)
('M', 'Meter', 'LENGTH', TRUE, 1.0000),
('CM', 'Centimeter', 'LENGTH', FALSE, 0.0100),
('MM', 'Millimeter', 'LENGTH', FALSE, 0.0010),

-- Quantity (Base: PCS)
('PCS', 'Pieces', 'QUANTITY', TRUE, 1.0000),
('BOX', 'Box', 'QUANTITY', FALSE, 1.0000),
('CTN', 'Carton', 'QUANTITY', FALSE, 1.0000),
('PKT', 'Packet', 'QUANTITY', FALSE, 1.0000),
('DOZ', 'Dozen', 'QUANTITY', FALSE, 12.0000);

-- =====================================================
-- 3. SEED DATA: TAX RATES
-- =====================================================

INSERT INTO tax_rates (tax_code, tax_name, tax_percentage, tax_type, applicable_from, is_active) VALUES
('VAT15', 'VAT 15%', 15.00, 'VAT', '2024-01-01', TRUE),
('VAT8', 'VAT 8%', 8.00, 'VAT', '2024-01-01', TRUE),
('GST0', 'GST Exempt', 0.00, 'GST', '2024-01-01', TRUE),
('GST12', 'GST 12%', 12.00, 'GST', '2024-01-01', FALSE);

-- =====================================================
-- 4. SEED DATA: PRODUCT CATEGORIES
-- =====================================================

INSERT INTO product_categories (category_code, category_name, description, parent_category_id) VALUES
('RM', 'Raw Materials', 'All raw materials for production', NULL),
('RM-SPICE', 'Spices', 'Raw spice materials', 1),
('RM-PKG', 'Packaging Materials', 'Packaging materials', 1),
('FG', 'Finished Goods', 'Finished products', NULL),
('FG-GROUND', 'Ground Spices', 'Ground spice products', 4),
('FG-WHOLE', 'Whole Spices', 'Whole spice products', 4),
('FG-MIX', 'Spice Mixes', 'Mixed spice products', 4);

-- =====================================================
-- 5. SEED DATA: WAREHOUSES
-- =====================================================

INSERT INTO warehouses (warehouse_code, warehouse_name, warehouse_type, city, is_active) VALUES
('WH-RM-01', 'Raw Material Warehouse - Main', 'RAW_MATERIAL', 'Colombo', TRUE),
('WH-FG-01', 'Finished Goods Warehouse - Main', 'FINISHED_GOODS', 'Colombo', TRUE),
('WH-MIX-01', 'Mixed Warehouse - Branch', 'MIXED', 'Kandy', TRUE);

-- =====================================================
-- 6. SEED DATA: SAMPLE PRODUCTS
-- =====================================================

-- Get UOM IDs
SET @uom_kg = (SELECT id FROM units_of_measure WHERE uom_code = 'KG');
SET @uom_g = (SELECT id FROM units_of_measure WHERE uom_code = 'G');
SET @uom_pcs = (SELECT id FROM units_of_measure WHERE uom_code = 'PCS');

-- Get Category IDs
SET @cat_rm_spice = (SELECT id FROM product_categories WHERE category_code = 'RM-SPICE');
SET @cat_fg_ground = (SELECT id FROM product_categories WHERE category_code = 'FG-GROUND');

-- Insert Sample Raw Materials
INSERT INTO products (product_code, product_name, product_type, category_id, base_uom_id, 
                      reorder_level, minimum_stock_level, standard_cost, is_active) VALUES
('RM-CIN-001', 'Cinnamon Sticks - Raw', 'RAW_MATERIAL', @cat_rm_spice, @uom_kg, 100.00, 50.00, 500.00, TRUE),
('RM-PEP-001', 'Black Pepper - Raw', 'RAW_MATERIAL', @cat_rm_spice, @uom_kg, 200.00, 100.00, 800.00, TRUE),
('RM-CAR-001', 'Cardamom - Raw', 'RAW_MATERIAL', @cat_rm_spice, @uom_kg, 50.00, 25.00, 2000.00, TRUE),
('RM-COR-001', 'Coriander Seeds - Raw', 'RAW_MATERIAL', @cat_rm_spice, @uom_kg, 150.00, 75.00, 300.00, TRUE),
('RM-CUM-001', 'Cumin Seeds - Raw', 'RAW_MATERIAL', @cat_rm_spice, @uom_kg, 100.00, 50.00, 450.00, TRUE);

-- Insert Sample Finished Goods
INSERT INTO products (product_code, product_name, product_type, category_id, base_uom_id, 
                      reorder_level, minimum_stock_level, standard_cost, selling_price, is_active) VALUES
('FG-CIN-100', 'Cinnamon Powder 100g', 'FINISHED_GOOD', @cat_fg_ground, @uom_g, 500.00, 250.00, 80.00, 150.00, TRUE),
('FG-PEP-100', 'Black Pepper Powder 100g', 'FINISHED_GOOD', @cat_fg_ground, @uom_g, 600.00, 300.00, 120.00, 200.00, TRUE),
('FG-CAR-50', 'Cardamom Powder 50g', 'FINISHED_GOOD', @cat_fg_ground, @uom_g, 300.00, 150.00, 300.00, 450.00, TRUE),
('FG-COR-100', 'Coriander Powder 100g', 'FINISHED_GOOD', @cat_fg_ground, @uom_g, 500.00, 250.00, 50.00, 100.00, TRUE),
('FG-CUM-100', 'Cumin Powder 100g', 'FINISHED_GOOD', @cat_fg_ground, @uom_g, 400.00, 200.00, 70.00, 130.00, TRUE);

-- =====================================================
-- 7. SEED DATA: SYSTEM CONFIGURATION
-- =====================================================

INSERT INTO system_config (config_key, config_value, config_group, description, data_type) VALUES
('COMPANY_NAME', 'Epic Green Spice Company', 'GENERAL', 'Company name for documents', 'STRING'),
('COMPANY_ADDRESS', 'No. 123, Spice Street, Colombo 00700, Sri Lanka', 'GENERAL', 'Company address', 'STRING'),
('COMPANY_PHONE', '+94 11 2345678', 'GENERAL', 'Company phone number', 'STRING'),
('COMPANY_EMAIL', 'info@epicgreen.lk', 'GENERAL', 'Company email', 'STRING'),
('COMPANY_TAX_ID', 'VAT-123456789', 'GENERAL', 'Company tax identification', 'STRING'),
('CURRENCY', 'LKR', 'GENERAL', 'Default currency', 'STRING'),
('DATE_FORMAT', 'YYYY-MM-DD', 'GENERAL', 'Date format for display', 'STRING'),
('DECIMAL_PLACES', '2', 'GENERAL', 'Decimal places for amounts', 'INTEGER'),
('LOW_STOCK_ALERT_DAYS', '7', 'INVENTORY', 'Days before expiry for low stock alert', 'INTEGER'),
('DEFAULT_CREDIT_DAYS', '30', 'SALES', 'Default credit period in days', 'INTEGER'),
('AUTO_APPROVE_ORDERS_BELOW', '50000', 'SALES', 'Auto-approve orders below this amount', 'DECIMAL'),
('ENABLE_MOBILE_SYNC', 'true', 'MOBILE', 'Enable mobile synchronization', 'BOOLEAN'),
('SYNC_INTERVAL_MINUTES', '15', 'MOBILE', 'Sync interval in minutes', 'INTEGER');

-- =====================================================
-- 8. SEED DATA: DEFAULT USER (ADMIN)
-- =====================================================

-- Create default admin user (password: Admin@123 - BCrypt hashed)
-- Note: In production, generate proper BCrypt hash
INSERT INTO users (username, email, password_hash, first_name, last_name, 
                   employee_code, status) VALUES
('admin', 'admin@epicgreen.lk', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
 'System', 'Administrator', 'EMP-001', 'ACTIVE');

-- Assign Super Admin role to admin user
INSERT INTO user_roles (user_id, role_id) VALUES
(1, (SELECT id FROM roles WHERE role_code = 'ROLE_SUPER_ADMIN'));

-- =====================================================
-- 9. SEED DATA: CHART OF ACCOUNTS (Sample)
-- =====================================================

INSERT INTO chart_of_accounts (account_code, account_name, account_type, account_category, is_group_account, opening_balance_type) VALUES
-- Assets
('1000', 'Assets', 'ASSET', 'CURRENT_ASSET', TRUE, 'DEBIT'),
('1100', 'Current Assets', 'ASSET', 'CURRENT_ASSET', TRUE, 'DEBIT'),
('1101', 'Cash in Hand', 'ASSET', 'CURRENT_ASSET', FALSE, 'DEBIT'),
('1102', 'Bank Accounts', 'ASSET', 'CURRENT_ASSET', TRUE, 'DEBIT'),
('1103', 'Accounts Receivable', 'ASSET', 'CURRENT_ASSET', FALSE, 'DEBIT'),
('1104', 'Inventory - Raw Materials', 'ASSET', 'CURRENT_ASSET', FALSE, 'DEBIT'),
('1105', 'Inventory - Finished Goods', 'ASSET', 'CURRENT_ASSET', FALSE, 'DEBIT'),
('1200', 'Fixed Assets', 'ASSET', 'FIXED_ASSET', TRUE, 'DEBIT'),
('1201', 'Land and Buildings', 'ASSET', 'FIXED_ASSET', FALSE, 'DEBIT'),
('1202', 'Machinery and Equipment', 'ASSET', 'FIXED_ASSET', FALSE, 'DEBIT'),
('1203', 'Vehicles', 'ASSET', 'FIXED_ASSET', FALSE, 'DEBIT'),

-- Liabilities
('2000', 'Liabilities', 'LIABILITY', 'CURRENT_LIABILITY', TRUE, 'CREDIT'),
('2100', 'Current Liabilities', 'LIABILITY', 'CURRENT_LIABILITY', TRUE, 'CREDIT'),
('2101', 'Accounts Payable', 'LIABILITY', 'CURRENT_LIABILITY', FALSE, 'CREDIT'),
('2102', 'Short-term Loans', 'LIABILITY', 'CURRENT_LIABILITY', FALSE, 'CREDIT'),
('2103', 'Tax Payable', 'LIABILITY', 'CURRENT_LIABILITY', FALSE, 'CREDIT'),
('2200', 'Long-term Liabilities', 'LIABILITY', 'LONG_TERM_LIABILITY', TRUE, 'CREDIT'),
('2201', 'Long-term Loans', 'LIABILITY', 'LONG_TERM_LIABILITY', FALSE, 'CREDIT'),

-- Equity
('3000', 'Equity', 'EQUITY', 'CAPITAL', TRUE, 'CREDIT'),
('3001', 'Owner\'s Capital', 'EQUITY', 'CAPITAL', FALSE, 'CREDIT'),
('3002', 'Retained Earnings', 'EQUITY', 'CAPITAL', FALSE, 'CREDIT'),

-- Revenue
('4000', 'Revenue', 'REVENUE', 'DIRECT_INCOME', TRUE, 'CREDIT'),
('4001', 'Sales - Finished Goods', 'REVENUE', 'DIRECT_INCOME', FALSE, 'CREDIT'),
('4002', 'Other Income', 'REVENUE', 'INDIRECT_INCOME', FALSE, 'CREDIT'),

-- Expenses
('5000', 'Cost of Goods Sold', 'EXPENSE', 'DIRECT_EXPENSE', TRUE, 'DEBIT'),
('5001', 'Raw Material Cost', 'EXPENSE', 'DIRECT_EXPENSE', FALSE, 'DEBIT'),
('5002', 'Production Labor Cost', 'EXPENSE', 'DIRECT_EXPENSE', FALSE, 'DEBIT'),
('5003', 'Manufacturing Overhead', 'EXPENSE', 'DIRECT_EXPENSE', FALSE, 'DEBIT'),
('6000', 'Operating Expenses', 'EXPENSE', 'INDIRECT_EXPENSE', TRUE, 'DEBIT'),
('6001', 'Salaries and Wages', 'EXPENSE', 'INDIRECT_EXPENSE', FALSE, 'DEBIT'),
('6002', 'Rent', 'EXPENSE', 'INDIRECT_EXPENSE', FALSE, 'DEBIT'),
('6003', 'Utilities', 'EXPENSE', 'INDIRECT_EXPENSE', FALSE, 'DEBIT'),
('6004', 'Transportation', 'EXPENSE', 'INDIRECT_EXPENSE', FALSE, 'DEBIT'),
('6005', 'Marketing and Advertising', 'EXPENSE', 'INDIRECT_EXPENSE', FALSE, 'DEBIT');

-- =====================================================
-- 10. SEED DATA: FINANCIAL PERIOD (Current Year)
-- =====================================================

-- Create financial periods for current fiscal year
INSERT INTO financial_periods (period_code, period_name, period_type, start_date, end_date, fiscal_year) VALUES
('FY2025-01', 'January 2025', 'MONTH', '2025-01-01', '2025-01-31', 2025),
('FY2025-02', 'February 2025', 'MONTH', '2025-02-01', '2025-02-28', 2025),
('FY2025-03', 'March 2025', 'MONTH', '2025-03-01', '2025-03-31', 2025),
('FY2025-04', 'April 2025', 'MONTH', '2025-04-01', '2025-04-30', 2025),
('FY2025-05', 'May 2025', 'MONTH', '2025-05-01', '2025-05-31', 2025),
('FY2025-06', 'June 2025', 'MONTH', '2025-06-01', '2025-06-30', 2025),
('FY2025-07', 'July 2025', 'MONTH', '2025-07-01', '2025-07-31', 2025),
('FY2025-08', 'August 2025', 'MONTH', '2025-08-01', '2025-08-31', 2025),
('FY2025-09', 'September 2025', 'MONTH', '2025-09-01', '2025-09-30', 2025),
('FY2025-10', 'October 2025', 'MONTH', '2025-10-01', '2025-10-31', 2025),
('FY2025-11', 'November 2025', 'MONTH', '2025-11-01', '2025-11-30', 2025),
('FY2025-12', 'December 2025', 'MONTH', '2025-12-01', '2025-12-31', 2025),
('FY2025-Q1', 'Q1 2025', 'QUARTER', '2025-01-01', '2025-03-31', 2025),
('FY2025-Q2', 'Q2 2025', 'QUARTER', '2025-04-01', '2025-06-30', 2025),
('FY2025-Q3', 'Q3 2025', 'QUARTER', '2025-07-01', '2025-09-30', 2025),
('FY2025-Q4', 'Q4 2025', 'QUARTER', '2025-10-01', '2025-12-31', 2025),
('FY2025', 'Fiscal Year 2025', 'YEAR', '2025-01-01', '2025-12-31', 2025);

-- =====================================================
-- SEED DATA COMPLETE
-- =====================================================

SELECT 'Seed data inserted successfully!' AS message;
