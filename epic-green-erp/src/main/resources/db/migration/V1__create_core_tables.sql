-- =====================================================
-- Epic Green ERP System - Database Schema
-- Database: epic_green_db
-- DBMS: MySQL 8.0
-- Architecture: Monolithic
-- =====================================================

-- Create Database
CREATE DATABASE IF NOT EXISTS epic_green_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE epic_green_db;

-- =====================================================
-- SECTION 1: ADMIN & MASTER DATA
-- =====================================================

-- Users Table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    mobile_number VARCHAR(20),
    employee_code VARCHAR(20) UNIQUE,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    last_login_at DATETIME,
    password_changed_at DATETIME,
    failed_login_attempts INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    deleted_at DATETIME,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_status (status),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB;

-- Roles Table
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_code VARCHAR(30) NOT NULL UNIQUE,
    description TEXT,
    is_system_role BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    INDEX idx_role_code (role_code)
) ENGINE=InnoDB;

-- Permissions Table
CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(100) NOT NULL UNIQUE,
    permission_code VARCHAR(50) NOT NULL UNIQUE,
    module VARCHAR(50) NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_module (module),
    INDEX idx_permission_code (permission_code)
) ENGINE=InnoDB;

-- User Roles (Many-to-Many)
CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB;

-- Role Permissions (Many-to-Many)
CREATE TABLE role_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    granted_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    granted_by BIGINT,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) ENGINE=InnoDB;

-- Units of Measure
CREATE TABLE units_of_measure (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    uom_code VARCHAR(10) NOT NULL UNIQUE,
    uom_name VARCHAR(50) NOT NULL,
    uom_type ENUM('WEIGHT', 'VOLUME', 'LENGTH', 'QUANTITY', 'AREA') NOT NULL,
    base_unit BOOLEAN DEFAULT FALSE,
    conversion_factor DECIMAL(10, 4) DEFAULT 1.0000,
    base_uom_id BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (base_uom_id) REFERENCES units_of_measure(id),
    INDEX idx_uom_code (uom_code),
    INDEX idx_uom_type (uom_type)
) ENGINE=InnoDB;

-- Tax Rates
CREATE TABLE tax_rates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tax_code VARCHAR(20) NOT NULL UNIQUE,
    tax_name VARCHAR(100) NOT NULL,
    tax_percentage DECIMAL(5, 2) NOT NULL,
    tax_type ENUM('GST', 'VAT', 'SALES_TAX', 'SERVICE_TAX', 'OTHER') NOT NULL,
    applicable_from DATE NOT NULL,
    applicable_to DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    INDEX idx_tax_code (tax_code),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB;

-- System Configuration
CREATE TABLE system_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value TEXT NOT NULL,
    config_group VARCHAR(50) NOT NULL,
    description TEXT,
    data_type ENUM('STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'JSON') DEFAULT 'STRING',
    is_encrypted BOOLEAN DEFAULT FALSE,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    INDEX idx_config_group (config_group)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 2: SUPPLIER MANAGEMENT
-- =====================================================

-- Suppliers Table
CREATE TABLE suppliers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_code VARCHAR(20) NOT NULL UNIQUE,
    supplier_name VARCHAR(200) NOT NULL,
    supplier_type ENUM('RAW_MATERIAL', 'PACKAGING', 'SERVICES', 'OTHER') NOT NULL,
    contact_person VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    mobile VARCHAR(20),
    tax_id VARCHAR(50),
    payment_terms VARCHAR(50),
    credit_limit DECIMAL(15, 2) DEFAULT 0.00,
    credit_days INT DEFAULT 0,
    address_line1 VARCHAR(200),
    address_line2 VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100) DEFAULT 'Sri Lanka',
    postal_code VARCHAR(20),
    bank_name VARCHAR(100),
    bank_account_number VARCHAR(50),
    bank_branch VARCHAR(100),
    rating DECIMAL(2, 1) DEFAULT 0.0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    deleted_at DATETIME,
    INDEX idx_supplier_code (supplier_code),
    INDEX idx_supplier_name (supplier_name),
    INDEX idx_supplier_type (supplier_type),
    INDEX idx_is_active (is_active),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB;

-- Supplier Contacts (Additional contacts)
CREATE TABLE supplier_contacts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_id BIGINT NOT NULL,
    contact_name VARCHAR(100) NOT NULL,
    designation VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    mobile VARCHAR(20),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE,
    INDEX idx_supplier_id (supplier_id)
) ENGINE=InnoDB;

-- Supplier Ledger
CREATE TABLE supplier_ledger (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_id BIGINT NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_type ENUM('PURCHASE', 'PAYMENT', 'CREDIT_NOTE', 'DEBIT_NOTE', 'ADJUSTMENT') NOT NULL,
    reference_type VARCHAR(50),
    reference_id BIGINT,
    reference_number VARCHAR(50),
    description TEXT,
    debit_amount DECIMAL(15, 2) DEFAULT 0.00,
    credit_amount DECIMAL(15, 2) DEFAULT 0.00,
    balance DECIMAL(15, 2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    INDEX idx_supplier_id (supplier_id),
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_reference (reference_type, reference_id)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 3: PRODUCT MASTER
-- =====================================================

-- Product Categories
CREATE TABLE product_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_code VARCHAR(20) NOT NULL UNIQUE,
    category_name VARCHAR(100) NOT NULL,
    parent_category_id BIGINT,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (parent_category_id) REFERENCES product_categories(id),
    INDEX idx_category_code (category_code),
    INDEX idx_parent_category_id (parent_category_id)
) ENGINE=InnoDB;

-- Products (Raw Materials and Finished Goods)
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(30) NOT NULL UNIQUE,
    product_name VARCHAR(200) NOT NULL,
    product_type ENUM('RAW_MATERIAL', 'FINISHED_GOOD', 'SEMI_FINISHED', 'PACKAGING') NOT NULL,
    category_id BIGINT,
    description TEXT,
    base_uom_id BIGINT NOT NULL,
    barcode VARCHAR(50) UNIQUE,
    sku VARCHAR(50) UNIQUE,
    hsn_code VARCHAR(20),
    reorder_level DECIMAL(10, 2) DEFAULT 0.00,
    minimum_stock_level DECIMAL(10, 2) DEFAULT 0.00,
    maximum_stock_level DECIMAL(10, 2) DEFAULT 0.00,
    standard_cost DECIMAL(15, 2) DEFAULT 0.00,
    selling_price DECIMAL(15, 2) DEFAULT 0.00,
    shelf_life_days INT,
    is_active BOOLEAN DEFAULT TRUE,
    image_url VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    deleted_at DATETIME,
    FOREIGN KEY (category_id) REFERENCES product_categories(id),
    FOREIGN KEY (base_uom_id) REFERENCES units_of_measure(id),
    INDEX idx_product_code (product_code),
    INDEX idx_product_name (product_name),
    INDEX idx_product_type (product_type),
    INDEX idx_barcode (barcode),
    INDEX idx_category_id (category_id),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 4: WAREHOUSE MANAGEMENT
-- =====================================================

-- Warehouses (Multi-location support)
CREATE TABLE warehouses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_code VARCHAR(20) NOT NULL UNIQUE,
    warehouse_name VARCHAR(100) NOT NULL,
    warehouse_type ENUM('RAW_MATERIAL', 'FINISHED_GOODS', 'MIXED') NOT NULL,
    address_line1 VARCHAR(200),
    address_line2 VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    manager_id BIGINT,
    contact_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (manager_id) REFERENCES users(id),
    INDEX idx_warehouse_code (warehouse_code),
    INDEX idx_warehouse_type (warehouse_type),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB;

-- Warehouse Locations (Bins/Racks within warehouse)
CREATE TABLE warehouse_locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    location_code VARCHAR(30) NOT NULL,
    location_name VARCHAR(100),
    aisle VARCHAR(10),
    rack VARCHAR(10),
    shelf VARCHAR(10),
    bin VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    UNIQUE KEY uk_warehouse_location (warehouse_id, location_code),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_location_code (location_code)
) ENGINE=InnoDB;

-- Inventory (Real-time stock levels)
CREATE TABLE inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    location_id BIGINT,
    batch_number VARCHAR(50),
    manufacturing_date DATE,
    expiry_date DATE,
    quantity_available DECIMAL(15, 3) DEFAULT 0.000,
    quantity_reserved DECIMAL(15, 3) DEFAULT 0.000,
    quantity_ordered DECIMAL(15, 3) DEFAULT 0.000,
    unit_cost DECIMAL(15, 2) DEFAULT 0.00,
    last_stock_date DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (location_id) REFERENCES warehouse_locations(id),
    UNIQUE KEY uk_inventory (warehouse_id, product_id, batch_number, location_id),
    INDEX idx_warehouse_product (warehouse_id, product_id),
    INDEX idx_batch_number (batch_number),
    INDEX idx_expiry_date (expiry_date)
) ENGINE=InnoDB;

-- Stock Movements (All inventory transactions)
CREATE TABLE stock_movements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    movement_date DATE NOT NULL,
    movement_type ENUM('RECEIPT', 'ISSUE', 'TRANSFER', 'ADJUSTMENT', 'PRODUCTION', 'SALES', 'RETURN') NOT NULL,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    from_location_id BIGINT,
    to_location_id BIGINT,
    batch_number VARCHAR(50),
    quantity DECIMAL(15, 3) NOT NULL,
    uom_id BIGINT NOT NULL,
    unit_cost DECIMAL(15, 2),
    reference_type VARCHAR(50),
    reference_id BIGINT,
    reference_number VARCHAR(50),
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (from_location_id) REFERENCES warehouse_locations(id),
    FOREIGN KEY (to_location_id) REFERENCES warehouse_locations(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    INDEX idx_movement_date (movement_date),
    INDEX idx_movement_type (movement_type),
    INDEX idx_warehouse_product (warehouse_id, product_id),
    INDEX idx_reference (reference_type, reference_id)
) ENGINE=InnoDB;

-- Stock Adjustments
CREATE TABLE stock_adjustments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    adjustment_number VARCHAR(30) NOT NULL UNIQUE,
    adjustment_date DATE NOT NULL,
    warehouse_id BIGINT NOT NULL,
    adjustment_type ENUM('DAMAGE', 'EXPIRY', 'PILFERAGE', 'SURPLUS', 'DEFICIT', 'OTHER') NOT NULL,
    status ENUM('DRAFT', 'PENDING_APPROVAL', 'APPROVED', 'REJECTED') DEFAULT 'DRAFT',
    approved_by BIGINT,
    approved_at DATETIME,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    INDEX idx_adjustment_number (adjustment_number),
    INDEX idx_adjustment_date (adjustment_date),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- Stock Adjustment Items
CREATE TABLE stock_adjustment_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    adjustment_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    batch_number VARCHAR(50),
    location_id BIGINT,
    quantity_adjusted DECIMAL(15, 3) NOT NULL,
    unit_cost DECIMAL(15, 2),
    total_value DECIMAL(15, 2),
    remarks TEXT,
    FOREIGN KEY (adjustment_id) REFERENCES stock_adjustments(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (location_id) REFERENCES warehouse_locations(id),
    INDEX idx_adjustment_id (adjustment_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 5: PURCHASE MANAGEMENT
-- =====================================================

-- Purchase Orders
CREATE TABLE purchase_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    po_number VARCHAR(30) NOT NULL UNIQUE,
    po_date DATE NOT NULL,
    supplier_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    expected_delivery_date DATE,
    payment_terms VARCHAR(100),
    status ENUM('DRAFT', 'PENDING_APPROVAL', 'APPROVED', 'SENT_TO_SUPPLIER', 'PARTIAL_RECEIVED', 'RECEIVED', 'CANCELLED') DEFAULT 'DRAFT',
    subtotal DECIMAL(15, 2) DEFAULT 0.00,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    discount_amount DECIMAL(15, 2) DEFAULT 0.00,
    total_amount DECIMAL(15, 2) DEFAULT 0.00,
    approved_by BIGINT,
    approved_at DATETIME,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    INDEX idx_po_number (po_number),
    INDEX idx_po_date (po_date),
    INDEX idx_supplier_id (supplier_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- Purchase Order Items
CREATE TABLE purchase_order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    po_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity_ordered DECIMAL(15, 3) NOT NULL,
    uom_id BIGINT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    tax_rate_id BIGINT,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    discount_percentage DECIMAL(5, 2) DEFAULT 0.00,
    discount_amount DECIMAL(15, 2) DEFAULT 0.00,
    line_total DECIMAL(15, 2) NOT NULL,
    quantity_received DECIMAL(15, 3) DEFAULT 0.000,
    quantity_pending DECIMAL(15, 3) GENERATED ALWAYS AS (quantity_ordered - quantity_received) STORED,
    remarks TEXT,
    FOREIGN KEY (po_id) REFERENCES purchase_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (tax_rate_id) REFERENCES tax_rates(id),
    INDEX idx_po_id (po_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB;

-- Goods Receipt Notes (GRN)
CREATE TABLE goods_receipt_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    grn_number VARCHAR(30) NOT NULL UNIQUE,
    grn_date DATE NOT NULL,
    po_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    invoice_number VARCHAR(50),
    invoice_date DATE,
    vehicle_number VARCHAR(20),
    received_by BIGINT,
    status ENUM('DRAFT', 'QUALITY_CHECK', 'APPROVED', 'REJECTED', 'POSTED') DEFAULT 'DRAFT',
    quality_checked_by BIGINT,
    quality_checked_at DATETIME,
    approved_by BIGINT,
    approved_at DATETIME,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (po_id) REFERENCES purchase_orders(id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (received_by) REFERENCES users(id),
    FOREIGN KEY (quality_checked_by) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    INDEX idx_grn_number (grn_number),
    INDEX idx_grn_date (grn_date),
    INDEX idx_po_id (po_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- GRN Items
CREATE TABLE grn_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    grn_id BIGINT NOT NULL,
    po_item_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    batch_number VARCHAR(50),
    manufacturing_date DATE,
    expiry_date DATE,
    quantity_ordered DECIMAL(15, 3) NOT NULL,
    quantity_received DECIMAL(15, 3) NOT NULL,
    quantity_accepted DECIMAL(15, 3) DEFAULT 0.000,
    quantity_rejected DECIMAL(15, 3) DEFAULT 0.000,
    uom_id BIGINT NOT NULL,
    unit_price DECIMAL(15, 2),
    location_id BIGINT,
    quality_status ENUM('PENDING', 'PASSED', 'FAILED', 'CONDITIONAL') DEFAULT 'PENDING',
    remarks TEXT,
    FOREIGN KEY (grn_id) REFERENCES goods_receipt_notes(id) ON DELETE CASCADE,
    FOREIGN KEY (po_item_id) REFERENCES purchase_order_items(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (location_id) REFERENCES warehouse_locations(id),
    INDEX idx_grn_id (grn_id),
    INDEX idx_po_item_id (po_item_id),
    INDEX idx_product_id (product_id),
    INDEX idx_batch_number (batch_number)
) ENGINE=InnoDB;

-- Continue in next file...
