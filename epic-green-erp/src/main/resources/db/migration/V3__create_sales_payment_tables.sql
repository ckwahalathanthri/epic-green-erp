-- =====================================================
-- SECTION 8: SALES MANAGEMENT
-- =====================================================

USE epic_green_db;

-- Sales Orders
CREATE TABLE sales_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(30) NOT NULL UNIQUE,
    order_date DATE NOT NULL,
    customer_id BIGINT NOT NULL,
    customer_po_number VARCHAR(50),
    customer_po_date DATE,
    billing_address_id BIGINT,
    shipping_address_id BIGINT,
    warehouse_id BIGINT NOT NULL,
    sales_rep_id BIGINT,
    order_type ENUM('REGULAR', 'URGENT', 'ADVANCE_ORDER') DEFAULT 'REGULAR',
    status ENUM('DRAFT', 'CONFIRMED', 'PENDING_APPROVAL', 'APPROVED', 'PROCESSING', 'PACKED', 'DISPATCHED', 'DELIVERED', 'CANCELLED') DEFAULT 'DRAFT',
    payment_mode ENUM('CASH', 'CHEQUE', 'CREDIT', 'BANK_TRANSFER') NOT NULL,
    delivery_mode ENUM('SELF_PICKUP', 'COMPANY_DELIVERY', 'COURIER') DEFAULT 'COMPANY_DELIVERY',
    expected_delivery_date DATE,
    subtotal DECIMAL(15, 2) DEFAULT 0.00,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    discount_percentage DECIMAL(5, 2) DEFAULT 0.00,
    discount_amount DECIMAL(15, 2) DEFAULT 0.00,
    freight_charges DECIMAL(15, 2) DEFAULT 0.00,
    total_amount DECIMAL(15, 2) DEFAULT 0.00,
    approved_by BIGINT,
    approved_at DATETIME,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (billing_address_id) REFERENCES customer_addresses(id),
    FOREIGN KEY (shipping_address_id) REFERENCES customer_addresses(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (sales_rep_id) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    INDEX idx_order_number (order_number),
    INDEX idx_order_date (order_date),
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status),
    INDEX idx_sales_rep_id (sales_rep_id)
) ENGINE=InnoDB;

-- Sales Order Items
CREATE TABLE sales_order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    batch_number VARCHAR(50),
    quantity_ordered DECIMAL(15, 3) NOT NULL,
    quantity_delivered DECIMAL(15, 3) DEFAULT 0.000,
    quantity_pending DECIMAL(15, 3) GENERATED ALWAYS AS (quantity_ordered - quantity_delivered) STORED,
    uom_id BIGINT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    discount_percentage DECIMAL(5, 2) DEFAULT 0.00,
    discount_amount DECIMAL(15, 2) DEFAULT 0.00,
    tax_rate_id BIGINT,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    line_total DECIMAL(15, 2) NOT NULL,
    remarks TEXT,
    FOREIGN KEY (order_id) REFERENCES sales_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (tax_rate_id) REFERENCES tax_rates(id),
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB;

-- Dispatch/Delivery Notes
CREATE TABLE dispatch_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dispatch_number VARCHAR(30) NOT NULL UNIQUE,
    dispatch_date DATE NOT NULL,
    order_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    vehicle_number VARCHAR(20),
    driver_name VARCHAR(100),
    driver_mobile VARCHAR(20),
    delivery_address_id BIGINT,
    route_code VARCHAR(20),
    status ENUM('PENDING', 'LOADING', 'DISPATCHED', 'IN_TRANSIT', 'DELIVERED', 'RETURNED') DEFAULT 'PENDING',
    dispatch_time DATETIME,
    delivery_time DATETIME,
    delivered_by BIGINT,
    received_by_name VARCHAR(100),
    received_by_signature TEXT,
    delivery_photo_url VARCHAR(500),
    gps_latitude DECIMAL(10, 8),
    gps_longitude DECIMAL(11, 8),
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (order_id) REFERENCES sales_orders(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (delivery_address_id) REFERENCES customer_addresses(id),
    FOREIGN KEY (delivered_by) REFERENCES users(id),
    INDEX idx_dispatch_number (dispatch_number),
    INDEX idx_dispatch_date (dispatch_date),
    INDEX idx_order_id (order_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- Dispatch Items
CREATE TABLE dispatch_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dispatch_id BIGINT NOT NULL,
    order_item_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    batch_number VARCHAR(50),
    quantity_dispatched DECIMAL(15, 3) NOT NULL,
    uom_id BIGINT NOT NULL,
    location_id BIGINT,
    remarks TEXT,
    FOREIGN KEY (dispatch_id) REFERENCES dispatch_notes(id) ON DELETE CASCADE,
    FOREIGN KEY (order_item_id) REFERENCES sales_order_items(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (location_id) REFERENCES warehouse_locations(id),
    INDEX idx_dispatch_id (dispatch_id),
    INDEX idx_order_item_id (order_item_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB;

-- Invoices
CREATE TABLE invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    invoice_number VARCHAR(30) NOT NULL UNIQUE,
    invoice_date DATE NOT NULL,
    order_id BIGINT NOT NULL,
    dispatch_id BIGINT,
    customer_id BIGINT NOT NULL,
    billing_address_id BIGINT,
    invoice_type ENUM('TAX_INVOICE', 'CREDIT_NOTE', 'DEBIT_NOTE') DEFAULT 'TAX_INVOICE',
    payment_terms VARCHAR(100),
    due_date DATE,
    subtotal DECIMAL(15, 2) NOT NULL,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    discount_amount DECIMAL(15, 2) DEFAULT 0.00,
    freight_charges DECIMAL(15, 2) DEFAULT 0.00,
    total_amount DECIMAL(15, 2) NOT NULL,
    paid_amount DECIMAL(15, 2) DEFAULT 0.00,
    balance_amount DECIMAL(15, 2) GENERATED ALWAYS AS (total_amount - paid_amount) STORED,
    payment_status ENUM('UNPAID', 'PARTIAL', 'PAID', 'OVERDUE') DEFAULT 'UNPAID',
    status ENUM('DRAFT', 'POSTED', 'CANCELLED') DEFAULT 'DRAFT',
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (order_id) REFERENCES sales_orders(id),
    FOREIGN KEY (dispatch_id) REFERENCES dispatch_notes(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (billing_address_id) REFERENCES customer_addresses(id),
    INDEX idx_invoice_number (invoice_number),
    INDEX idx_invoice_date (invoice_date),
    INDEX idx_customer_id (customer_id),
    INDEX idx_payment_status (payment_status),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB;

-- Invoice Items
CREATE TABLE invoice_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    invoice_id BIGINT NOT NULL,
    order_item_id BIGINT,
    product_id BIGINT NOT NULL,
    batch_number VARCHAR(50),
    quantity DECIMAL(15, 3) NOT NULL,
    uom_id BIGINT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    discount_percentage DECIMAL(5, 2) DEFAULT 0.00,
    discount_amount DECIMAL(15, 2) DEFAULT 0.00,
    tax_rate_id BIGINT,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    line_total DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,
    FOREIGN KEY (order_item_id) REFERENCES sales_order_items(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (tax_rate_id) REFERENCES tax_rates(id),
    INDEX idx_invoice_id (invoice_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 9: PAYMENT MANAGEMENT
-- =====================================================

-- Payments (Cash, Cheque, Credit)
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    payment_number VARCHAR(30) NOT NULL UNIQUE,
    payment_date DATE NOT NULL,
    customer_id BIGINT NOT NULL,
    payment_mode ENUM('CASH', 'CHEQUE', 'BANK_TRANSFER', 'CREDIT_CARD', 'ONLINE') NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    allocated_amount DECIMAL(15, 2) DEFAULT 0.00,
    unallocated_amount DECIMAL(15, 2) GENERATED ALWAYS AS (total_amount - allocated_amount) STORED,
    status ENUM('DRAFT', 'PENDING', 'CLEARED', 'BOUNCED', 'CANCELLED') DEFAULT 'DRAFT',
    -- Bank/Cheque specific fields
    bank_name VARCHAR(100),
    bank_branch VARCHAR(100),
    cheque_number VARCHAR(50),
    cheque_date DATE,
    cheque_clearance_date DATE,
    bank_reference_number VARCHAR(50),
    -- Collection details
    collected_by BIGINT,
    collected_at DATETIME,
    -- Approval
    approved_by BIGINT,
    approved_at DATETIME,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (collected_by) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    INDEX idx_payment_number (payment_number),
    INDEX idx_payment_date (payment_date),
    INDEX idx_customer_id (customer_id),
    INDEX idx_payment_mode (payment_mode),
    INDEX idx_status (status),
    INDEX idx_cheque_number (cheque_number)
) ENGINE=InnoDB;

-- Payment Allocations (Bill-to-Bill settlement)
CREATE TABLE payment_allocations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    payment_id BIGINT NOT NULL,
    invoice_id BIGINT NOT NULL,
    allocated_amount DECIMAL(15, 2) NOT NULL,
    allocation_date DATE NOT NULL,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE CASCADE,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    INDEX idx_payment_id (payment_id),
    INDEX idx_invoice_id (invoice_id)
) ENGINE=InnoDB;

-- Cheque Management (PDC tracking)
CREATE TABLE cheques (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    payment_id BIGINT NOT NULL,
    cheque_number VARCHAR(50) NOT NULL,
    cheque_date DATE NOT NULL,
    cheque_amount DECIMAL(15, 2) NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    bank_branch VARCHAR(100),
    account_number VARCHAR(50),
    customer_id BIGINT NOT NULL,
    status ENUM('RECEIVED', 'DEPOSITED', 'CLEARED', 'BOUNCED', 'RETURNED', 'CANCELLED') DEFAULT 'RECEIVED',
    deposit_date DATE,
    clearance_date DATE,
    bounce_reason TEXT,
    bounce_charges DECIMAL(15, 2) DEFAULT 0.00,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (payment_id) REFERENCES payments(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    INDEX idx_cheque_number (cheque_number),
    INDEX idx_cheque_date (cheque_date),
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status),
    INDEX idx_clearance_date (clearance_date)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 10: RETURNS & ADJUSTMENTS
-- =====================================================

-- Sales Returns
CREATE TABLE sales_returns (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    return_number VARCHAR(30) NOT NULL UNIQUE,
    return_date DATE NOT NULL,
    order_id BIGINT,
    invoice_id BIGINT,
    customer_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    return_type ENUM('QUALITY_ISSUE', 'WRONG_PRODUCT', 'DAMAGED', 'EXPIRED', 'CUSTOMER_REQUEST', 'OTHER') NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'RECEIVED', 'PROCESSED') DEFAULT 'PENDING',
    approved_by BIGINT,
    approved_at DATETIME,
    subtotal DECIMAL(15, 2) DEFAULT 0.00,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    total_amount DECIMAL(15, 2) DEFAULT 0.00,
    refund_mode ENUM('CASH', 'CHEQUE', 'CREDIT_NOTE', 'BANK_TRANSFER') DEFAULT 'CREDIT_NOTE',
    refund_status ENUM('PENDING', 'PROCESSED', 'COMPLETED') DEFAULT 'PENDING',
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (order_id) REFERENCES sales_orders(id),
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    INDEX idx_return_number (return_number),
    INDEX idx_return_date (return_date),
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- Sales Return Items
CREATE TABLE sales_return_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    return_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    batch_number VARCHAR(50),
    quantity_returned DECIMAL(15, 3) NOT NULL,
    uom_id BIGINT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    tax_rate_id BIGINT,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    line_total DECIMAL(15, 2) NOT NULL,
    return_reason TEXT,
    disposition ENUM('RESALEABLE', 'DAMAGED', 'EXPIRED', 'SCRAP') DEFAULT 'RESALEABLE',
    remarks TEXT,
    FOREIGN KEY (return_id) REFERENCES sales_returns(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (uom_id) REFERENCES units_of_measure(id),
    FOREIGN KEY (tax_rate_id) REFERENCES tax_rates(id),
    INDEX idx_return_id (return_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB;

-- Credit Notes
CREATE TABLE credit_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    credit_note_number VARCHAR(30) NOT NULL UNIQUE,
    credit_note_date DATE NOT NULL,
    customer_id BIGINT NOT NULL,
    return_id BIGINT,
    invoice_id BIGINT,
    reason ENUM('SALES_RETURN', 'DISCOUNT_ADJUSTMENT', 'BILLING_ERROR', 'GOODWILL', 'OTHER') NOT NULL,
    subtotal DECIMAL(15, 2) NOT NULL,
    tax_amount DECIMAL(15, 2) DEFAULT 0.00,
    total_amount DECIMAL(15, 2) NOT NULL,
    utilized_amount DECIMAL(15, 2) DEFAULT 0.00,
    balance_amount DECIMAL(15, 2) GENERATED ALWAYS AS (total_amount - utilized_amount) STORED,
    status ENUM('DRAFT', 'ISSUED', 'UTILIZED', 'EXPIRED') DEFAULT 'DRAFT',
    expiry_date DATE,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (return_id) REFERENCES sales_returns(id),
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    INDEX idx_credit_note_number (credit_note_number),
    INDEX idx_credit_note_date (credit_note_date),
    INDEX idx_customer_id (customer_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- Continue in next file...
