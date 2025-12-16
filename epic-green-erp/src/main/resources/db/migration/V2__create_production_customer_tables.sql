-- =====================================================
-- SECTION 6: PRODUCTION MANAGEMENT
-- =====================================================

USE epic_green_db;

-- Bill of Materials (BOM)
CREATE TABLE bill_of_materials (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bom_code VARCHAR(30) NOT NULL UNIQUE,
    finished_product_id BIGINT NOT NULL,
    bom_version VARCHAR(10) DEFAULT '1.0',
    output_quantity DECIMAL(15, 3) NOT NULL DEFAULT 1.000,
    output_uom_id BIGINT NOT NULL,
    production_time_minutes INT,
    labor_cost DECIMAL(15, 2) DEFAULT 0.00,
    overhead_cost DECIMAL(15, 2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    effective_from DATE NOT NULL,
    effective_to DATE,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (finished_product_id) REFERENCES products(id),
    FOREIGN KEY (output_uom_id) REFERENCES units_of_measure(id),
    INDEX idx_bom_code (bom_code),
    INDEX idx_finished_product_id (finished_product_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB;

-- BOM Items (Raw materials required)
CREATE TABLE bom_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bom_id BIGINT NOT NULL,
    raw_material_id BIGINT NOT NULL,
    quantity_required DECIMAL(15, 3) NOT NULL,
    uom_id BIGINT NOT NULL,
    wastage_percentage DECIMAL(5, 2) DEFAULT 0.00,
    standard_cost DECIMAL(15, 2),
    sequence_number INT,
    is_critical BOOLEAN DEFAULT FALSE,
    remarks TEXT,
    FOREIGN KEY (bom_id) REFERENCES bill_of_materials(id) ON DELETE CASCADE,
    FOREIGN KEY (raw_material_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    INDEX idx_bom_id (bom_id),
    INDEX idx_raw_material_id (raw_material_id)
) ENGINE=InnoDB;

-- Work Orders (Production Orders)
CREATE TABLE work_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wo_number VARCHAR(30) NOT NULL UNIQUE,
    wo_date DATE NOT NULL,
    bom_id BIGINT NOT NULL,
    finished_product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    planned_quantity DECIMAL(15, 3) NOT NULL,
    actual_quantity DECIMAL(15, 3) DEFAULT 0.000,
    uom_id BIGINT NOT NULL,
    batch_number VARCHAR(50),
    manufacturing_date DATE,
    expected_completion_date DATE,
    actual_completion_date DATE,
    status ENUM('DRAFT', 'RELEASED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'DRAFT',
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM',
    supervisor_id BIGINT,
    material_cost DECIMAL(15, 2) DEFAULT 0.00,
    labor_cost DECIMAL(15, 2) DEFAULT 0.00,
    overhead_cost DECIMAL(15, 2) DEFAULT 0.00,
    total_cost DECIMAL(15, 2) DEFAULT 0.00,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (bom_id) REFERENCES bill_of_materials(id),
    FOREIGN KEY (finished_product_id) REFERENCES products(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (supervisor_id) REFERENCES users(id),
    INDEX idx_wo_number (wo_number),
    INDEX idx_wo_date (wo_date),
    INDEX idx_status (status),
    INDEX idx_batch_number (batch_number)
) ENGINE=InnoDB;

-- Work Order Items (Materials to be consumed)
CREATE TABLE work_order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wo_id BIGINT NOT NULL,
    raw_material_id BIGINT NOT NULL,
    planned_quantity DECIMAL(15, 3) NOT NULL,
    consumed_quantity DECIMAL(15, 3) DEFAULT 0.000,
    uom_id BIGINT NOT NULL,
    unit_cost DECIMAL(15, 2),
    total_cost DECIMAL(15, 2),
    status ENUM('PENDING', 'ISSUED', 'CONSUMED') DEFAULT 'PENDING',
    issued_from_warehouse_id BIGINT,
    issued_at DATETIME,
    issued_by BIGINT,
    FOREIGN KEY (wo_id) REFERENCES work_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (raw_material_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (issued_from_warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (issued_by) REFERENCES users(id),
    INDEX idx_wo_id (wo_id),
    INDEX idx_raw_material_id (raw_material_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- Material Consumption (Actual usage tracking)
CREATE TABLE material_consumption (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wo_id BIGINT NOT NULL,
    wo_item_id BIGINT,
    raw_material_id BIGINT NOT NULL,
    consumption_date DATE NOT NULL,
    batch_number VARCHAR(50),
    quantity_consumed DECIMAL(15, 3) NOT NULL,
    uom_id BIGINT NOT NULL,
    unit_cost DECIMAL(15, 2),
    total_cost DECIMAL(15, 2),
    warehouse_id BIGINT NOT NULL,
    consumed_by BIGINT,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wo_id) REFERENCES work_orders(id),
    FOREIGN KEY (wo_item_id) REFERENCES work_order_items(id),
    FOREIGN KEY (raw_material_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (consumed_by) REFERENCES users(id),
    INDEX idx_wo_id (wo_id),
    INDEX idx_consumption_date (consumption_date),
    INDEX idx_raw_material_id (raw_material_id)
) ENGINE=InnoDB;

-- Production Output (Finished goods produced)
CREATE TABLE production_output (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wo_id BIGINT NOT NULL,
    output_date DATE NOT NULL,
    finished_product_id BIGINT NOT NULL,
    batch_number VARCHAR(50) NOT NULL,
    quantity_produced DECIMAL(15, 3) NOT NULL,
    quantity_accepted DECIMAL(15, 3) NOT NULL,
    quantity_rejected DECIMAL(15, 3) DEFAULT 0.000,
    quantity_rework DECIMAL(15, 3) DEFAULT 0.000,
    uom_id BIGINT NOT NULL,
    manufacturing_date DATE,
    expiry_date DATE,
    warehouse_id BIGINT NOT NULL,
    location_id BIGINT,
    unit_cost DECIMAL(15, 2),
    total_cost DECIMAL(15, 2),
    quality_status ENUM('PENDING', 'PASSED', 'FAILED') DEFAULT 'PENDING',
    quality_checked_by BIGINT,
    quality_checked_at DATETIME,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (wo_id) REFERENCES work_orders(id),
    FOREIGN KEY (finished_product_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (location_id) REFERENCES warehouse_locations(id),
    FOREIGN KEY (quality_checked_by) REFERENCES users(id),
    INDEX idx_wo_id (wo_id),
    INDEX idx_output_date (output_date),
    INDEX idx_batch_number (batch_number),
    INDEX idx_finished_product_id (finished_product_id)
) ENGINE=InnoDB;

-- Production Wastage
CREATE TABLE production_wastage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wo_id BIGINT NOT NULL,
    wastage_date DATE NOT NULL,
    product_id BIGINT NOT NULL,
    wastage_type ENUM('MATERIAL', 'PRODUCTION', 'QUALITY_REJECTION') NOT NULL,
    quantity DECIMAL(15, 3) NOT NULL,
    uom_id BIGINT NOT NULL,
    unit_cost DECIMAL(15, 2),
    total_value DECIMAL(15, 2),
    reason TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (wo_id) REFERENCES work_orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    INDEX idx_wo_id (wo_id),
    INDEX idx_wastage_date (wastage_date),
    INDEX idx_wastage_type (wastage_type)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 7: CUSTOMER MANAGEMENT
-- =====================================================

-- Customers Table
CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_code VARCHAR(20) NOT NULL UNIQUE,
    customer_name VARCHAR(200) NOT NULL,
    customer_type ENUM('WHOLESALE', 'RETAIL', 'DISTRIBUTOR', 'DIRECT') NOT NULL,
    contact_person VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    mobile VARCHAR(20),
    tax_id VARCHAR(50),
    payment_terms VARCHAR(50),
    credit_limit DECIMAL(15, 2) DEFAULT 0.00,
    credit_days INT DEFAULT 0,
    current_balance DECIMAL(15, 2) DEFAULT 0.00,
    billing_address_line1 VARCHAR(200),
    billing_address_line2 VARCHAR(200),
    billing_city VARCHAR(100),
    billing_state VARCHAR(100),
    billing_country VARCHAR(100) DEFAULT 'Sri Lanka',
    billing_postal_code VARCHAR(20),
    shipping_address_line1 VARCHAR(200),
    shipping_address_line2 VARCHAR(200),
    shipping_city VARCHAR(100),
    shipping_state VARCHAR(100),
    shipping_country VARCHAR(100) DEFAULT 'Sri Lanka',
    shipping_postal_code VARCHAR(20),
    assigned_sales_rep_id BIGINT,
    region VARCHAR(100),
    route_code VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    deleted_at DATETIME,
    FOREIGN KEY (assigned_sales_rep_id) REFERENCES users(id),
    INDEX idx_customer_code (customer_code),
    INDEX idx_customer_name (customer_name),
    INDEX idx_customer_type (customer_type),
    INDEX idx_sales_rep_id (assigned_sales_rep_id),
    INDEX idx_is_active (is_active),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB;

-- Customer Contacts
CREATE TABLE customer_contacts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    contact_name VARCHAR(100) NOT NULL,
    designation VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    mobile VARCHAR(20),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    INDEX idx_customer_id (customer_id)
) ENGINE=InnoDB;

-- Customer Addresses (Multiple delivery addresses)
CREATE TABLE customer_addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    address_type ENUM('BILLING', 'SHIPPING', 'BOTH') NOT NULL,
    address_name VARCHAR(100),
    address_line1 VARCHAR(200) NOT NULL,
    address_line2 VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100) DEFAULT 'Sri Lanka',
    postal_code VARCHAR(20),
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    INDEX idx_customer_id (customer_id),
    INDEX idx_address_type (address_type)
) ENGINE=InnoDB;

-- Customer Ledger
CREATE TABLE customer_ledger (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_type ENUM('SALE', 'PAYMENT', 'RETURN', 'CREDIT_NOTE', 'DEBIT_NOTE', 'ADJUSTMENT') NOT NULL,
    reference_type VARCHAR(50),
    reference_id BIGINT,
    reference_number VARCHAR(50),
    description TEXT,
    debit_amount DECIMAL(15, 2) DEFAULT 0.00,
    credit_amount DECIMAL(15, 2) DEFAULT 0.00,
    balance DECIMAL(15, 2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    INDEX idx_customer_id (customer_id),
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_reference (reference_type, reference_id)
) ENGINE=InnoDB;

-- Customer Price Lists (Customer-specific pricing)
CREATE TABLE customer_price_lists (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    special_price DECIMAL(15, 2) NOT NULL,
    discount_percentage DECIMAL(5, 2) DEFAULT 0.00,
    min_quantity DECIMAL(15, 3) DEFAULT 1.000,
    valid_from DATE NOT NULL,
    valid_to DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_customer_product (customer_id, product_id),
    INDEX idx_valid_dates (valid_from, valid_to)
) ENGINE=InnoDB;

-- Continue in next file...
