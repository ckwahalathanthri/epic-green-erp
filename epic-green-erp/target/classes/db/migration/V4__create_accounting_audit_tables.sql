-- =====================================================
-- SECTION 11: ACCOUNTING & FINANCE
-- =====================================================

USE epic_green_db;

-- Chart of Accounts
CREATE TABLE chart_of_accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_code VARCHAR(20) NOT NULL UNIQUE,
    account_name VARCHAR(200) NOT NULL,
    account_type ENUM('ASSET', 'LIABILITY', 'EQUITY', 'REVENUE', 'EXPENSE') NOT NULL,
    account_category ENUM('CURRENT_ASSET', 'FIXED_ASSET', 'CURRENT_LIABILITY', 'LONG_TERM_LIABILITY', 
                          'CAPITAL', 'DIRECT_INCOME', 'INDIRECT_INCOME', 'DIRECT_EXPENSE', 
                          'INDIRECT_EXPENSE', 'OTHER') NOT NULL,
    parent_account_id BIGINT,
    is_group_account BOOLEAN DEFAULT FALSE,
    is_control_account BOOLEAN DEFAULT FALSE,
    opening_balance DECIMAL(15, 2) DEFAULT 0.00,
    opening_balance_type ENUM('DEBIT', 'CREDIT') DEFAULT 'DEBIT',
    current_balance DECIMAL(15, 2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (parent_account_id) REFERENCES chart_of_accounts(id),
    INDEX idx_account_code (account_code),
    INDEX idx_account_type (account_type),
    INDEX idx_parent_account_id (parent_account_id)
) ENGINE=InnoDB;

-- Financial Periods (Fiscal years/months)
CREATE TABLE financial_periods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    period_code VARCHAR(20) NOT NULL UNIQUE,
    period_name VARCHAR(100) NOT NULL,
    period_type ENUM('MONTH', 'QUARTER', 'YEAR') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    fiscal_year INT NOT NULL,
    is_closed BOOLEAN DEFAULT FALSE,
    closed_by BIGINT,
    closed_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (closed_by) REFERENCES users(id),
    INDEX idx_period_code (period_code),
    INDEX idx_fiscal_year (fiscal_year),
    INDEX idx_period_dates (start_date, end_date)
) ENGINE=InnoDB;

-- Journal Entries
CREATE TABLE journal_entries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    journal_number VARCHAR(30) NOT NULL UNIQUE,
    journal_date DATE NOT NULL,
    period_id BIGINT NOT NULL,
    entry_type ENUM('MANUAL', 'AUTOMATED', 'OPENING_BALANCE', 'CLOSING', 'ADJUSTMENT') DEFAULT 'MANUAL',
    source_type VARCHAR(50),
    source_id BIGINT,
    source_reference VARCHAR(50),
    description TEXT,
    total_debit DECIMAL(15, 2) NOT NULL,
    total_credit DECIMAL(15, 2) NOT NULL,
    status ENUM('DRAFT', 'POSTED', 'CANCELLED') DEFAULT 'DRAFT',
    posted_by BIGINT,
    posted_at DATETIME,
    approved_by BIGINT,
    approved_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (period_id) REFERENCES financial_periods(id),
    FOREIGN KEY (posted_by) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id),
    INDEX idx_journal_number (journal_number),
    INDEX idx_journal_date (journal_date),
    INDEX idx_period_id (period_id),
    INDEX idx_status (status),
    INDEX idx_source (source_type, source_id)
) ENGINE=InnoDB;

-- Journal Entry Lines
CREATE TABLE journal_entry_lines (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    journal_id BIGINT NOT NULL,
    line_number INT NOT NULL,
    account_id BIGINT NOT NULL,
    debit_amount DECIMAL(15, 2) DEFAULT 0.00,
    credit_amount DECIMAL(15, 2) DEFAULT 0.00,
    description TEXT,
    cost_center VARCHAR(50),
    dimension1 VARCHAR(50),
    dimension2 VARCHAR(50),
    FOREIGN KEY (journal_id) REFERENCES journal_entries(id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES chart_of_accounts(id),
    INDEX idx_journal_id (journal_id),
    INDEX idx_account_id (account_id)
) ENGINE=InnoDB;

-- General Ledger (Detailed ledger for all accounts)
CREATE TABLE general_ledger (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_date DATE NOT NULL,
    period_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    journal_id BIGINT NOT NULL,
    journal_line_id BIGINT NOT NULL,
    description TEXT,
    debit_amount DECIMAL(15, 2) DEFAULT 0.00,
    credit_amount DECIMAL(15, 2) DEFAULT 0.00,
    balance DECIMAL(15, 2) NOT NULL,
    source_type VARCHAR(50),
    source_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (period_id) REFERENCES financial_periods(id),
    FOREIGN KEY (account_id) REFERENCES chart_of_accounts(id),
    FOREIGN KEY (journal_id) REFERENCES journal_entries(id),
    FOREIGN KEY (journal_line_id) REFERENCES journal_entry_lines(id),
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_account_id (account_id),
    INDEX idx_journal_id (journal_id),
    INDEX idx_period_id (period_id)
) ENGINE=InnoDB;

-- Trial Balance (Periodic snapshot)
CREATE TABLE trial_balance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    period_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    opening_debit DECIMAL(15, 2) DEFAULT 0.00,
    opening_credit DECIMAL(15, 2) DEFAULT 0.00,
    period_debit DECIMAL(15, 2) DEFAULT 0.00,
    period_credit DECIMAL(15, 2) DEFAULT 0.00,
    closing_debit DECIMAL(15, 2) DEFAULT 0.00,
    closing_credit DECIMAL(15, 2) DEFAULT 0.00,
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    generated_by BIGINT,
    FOREIGN KEY (period_id) REFERENCES financial_periods(id),
    FOREIGN KEY (account_id) REFERENCES chart_of_accounts(id),
    FOREIGN KEY (generated_by) REFERENCES users(id),
    UNIQUE KEY uk_period_account (period_id, account_id),
    INDEX idx_period_id (period_id),
    INDEX idx_account_id (account_id)
) ENGINE=InnoDB;

-- Bank Accounts
CREATE TABLE bank_accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    account_name VARCHAR(200) NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    bank_branch VARCHAR(100),
    account_type ENUM('CURRENT', 'SAVINGS', 'OVERDRAFT', 'CASH') NOT NULL,
    currency_code VARCHAR(3) DEFAULT 'LKR',
    gl_account_id BIGINT NOT NULL,
    opening_balance DECIMAL(15, 2) DEFAULT 0.00,
    current_balance DECIMAL(15, 2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    FOREIGN KEY (gl_account_id) REFERENCES chart_of_accounts(id),
    INDEX idx_account_number (account_number),
    INDEX idx_bank_name (bank_name)
) ENGINE=InnoDB;

-- Bank Reconciliation
CREATE TABLE bank_reconciliations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reconciliation_number VARCHAR(30) NOT NULL UNIQUE,
    bank_account_id BIGINT NOT NULL,
    statement_date DATE NOT NULL,
    statement_balance DECIMAL(15, 2) NOT NULL,
    book_balance DECIMAL(15, 2) NOT NULL,
    reconciled_balance DECIMAL(15, 2),
    status ENUM('DRAFT', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'DRAFT',
    reconciled_by BIGINT,
    reconciled_at DATETIME,
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id),
    FOREIGN KEY (reconciled_by) REFERENCES users(id),
    INDEX idx_reconciliation_number (reconciliation_number),
    INDEX idx_bank_account_id (bank_account_id),
    INDEX idx_statement_date (statement_date)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 12: MOBILE SYNC & OFFLINE SUPPORT
-- =====================================================

-- Sync Logs (Track synchronization between mobile and server)
CREATE TABLE sync_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    device_id VARCHAR(100) NOT NULL,
    device_type ENUM('ANDROID', 'IOS') NOT NULL,
    app_version VARCHAR(20),
    sync_type ENUM('FULL', 'INCREMENTAL', 'PUSH', 'PULL') NOT NULL,
    sync_direction ENUM('UPLOAD', 'DOWNLOAD', 'BIDIRECTIONAL') NOT NULL,
    sync_status ENUM('INITIATED', 'IN_PROGRESS', 'COMPLETED', 'FAILED', 'CANCELLED') NOT NULL,
    records_uploaded INT DEFAULT 0,
    records_downloaded INT DEFAULT 0,
    conflicts_detected INT DEFAULT 0,
    conflicts_resolved INT DEFAULT 0,
    error_message TEXT,
    started_at DATETIME NOT NULL,
    completed_at DATETIME,
    duration_seconds INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_device (user_id, device_id),
    INDEX idx_sync_status (sync_status),
    INDEX idx_started_at (started_at)
) ENGINE=InnoDB;

-- Sync Queue (Pending sync items)
CREATE TABLE sync_queue (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    device_id VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    operation_type ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    data_snapshot JSON,
    sync_status ENUM('PENDING', 'IN_PROGRESS', 'SYNCED', 'FAILED', 'CONFLICT') DEFAULT 'PENDING',
    priority INT DEFAULT 5,
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    error_message TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    synced_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_device (user_id, device_id),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_sync_status (sync_status),
    INDEX idx_priority (priority)
) ENGINE=InnoDB;

-- Sync Conflicts (Data conflicts needing resolution)
CREATE TABLE sync_conflicts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    device_id VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    server_data JSON NOT NULL,
    client_data JSON NOT NULL,
    conflict_type ENUM('UPDATE_UPDATE', 'UPDATE_DELETE', 'VERSION_MISMATCH') NOT NULL,
    resolution_strategy ENUM('SERVER_WINS', 'CLIENT_WINS', 'MANUAL', 'MERGE') DEFAULT 'MANUAL',
    status ENUM('DETECTED', 'RESOLVED', 'IGNORED') DEFAULT 'DETECTED',
    resolved_data JSON,
    resolved_by BIGINT,
    resolved_at DATETIME,
    detected_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (resolved_by) REFERENCES users(id),
    INDEX idx_user_device (user_id, device_id),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- Mobile Data Cache (Offline data for mobile users)
CREATE TABLE mobile_data_cache (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    cache_key VARCHAR(100) NOT NULL,
    cache_type ENUM('CUSTOMER', 'PRODUCT', 'PRICELIST', 'STOCK', 'ORDER', 'PAYMENT', 'OTHER') NOT NULL,
    data_snapshot JSON NOT NULL,
    last_synced_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY uk_user_cache_key (user_id, cache_key),
    INDEX idx_user_id (user_id),
    INDEX idx_cache_type (cache_type),
    INDEX idx_expires_at (expires_at)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 13: AUDIT & LOGGING
-- =====================================================

-- Audit Logs (Comprehensive audit trail)
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    username VARCHAR(50),
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT,
    entity_name VARCHAR(200),
    operation_type ENUM('CREATE', 'UPDATE', 'DELETE', 'VIEW', 'APPROVE', 'REJECT', 'LOGIN', 'LOGOUT') NOT NULL,
    old_values JSON,
    new_values JSON,
    changed_fields JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    session_id VARCHAR(100),
    status ENUM('SUCCESS', 'FAILED') DEFAULT 'SUCCESS',
    error_message TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB;

-- Activity Logs (User activity tracking)
CREATE TABLE activity_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    module VARCHAR(50) NOT NULL,
    activity_description TEXT,
    reference_type VARCHAR(50),
    reference_id BIGINT,
    ip_address VARCHAR(45),
    device_type ENUM('WEB', 'MOBILE_ANDROID', 'MOBILE_IOS', 'API') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_module (module),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB;

-- Error Logs (Application errors)
CREATE TABLE error_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    error_type VARCHAR(50) NOT NULL,
    error_code VARCHAR(20),
    error_message TEXT NOT NULL,
    stack_trace TEXT,
    request_url VARCHAR(500),
    request_method VARCHAR(10),
    request_body TEXT,
    user_id BIGINT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    severity ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') DEFAULT 'MEDIUM',
    is_resolved BOOLEAN DEFAULT FALSE,
    resolved_by BIGINT,
    resolved_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (resolved_by) REFERENCES users(id),
    INDEX idx_error_type (error_type),
    INDEX idx_severity (severity),
    INDEX idx_created_at (created_at),
    INDEX idx_is_resolved (is_resolved)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 14: NOTIFICATIONS
-- =====================================================

-- Notification Templates
CREATE TABLE notification_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_code VARCHAR(50) NOT NULL UNIQUE,
    template_name VARCHAR(100) NOT NULL,
    notification_type ENUM('EMAIL', 'SMS', 'PUSH', 'IN_APP') NOT NULL,
    subject VARCHAR(200),
    body_template TEXT NOT NULL,
    variables JSON,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    INDEX idx_template_code (template_code),
    INDEX idx_notification_type (notification_type)
) ENGINE=InnoDB;

-- Notifications Queue
CREATE TABLE notifications_queue (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipient_user_id BIGINT,
    recipient_email VARCHAR(100),
    recipient_mobile VARCHAR(20),
    notification_type ENUM('EMAIL', 'SMS', 'PUSH', 'IN_APP') NOT NULL,
    template_id BIGINT,
    subject VARCHAR(200),
    message TEXT NOT NULL,
    priority INT DEFAULT 5,
    status ENUM('PENDING', 'SENT', 'FAILED', 'CANCELLED') DEFAULT 'PENDING',
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    scheduled_at DATETIME,
    sent_at DATETIME,
    error_message TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipient_user_id) REFERENCES users(id),
    FOREIGN KEY (template_id) REFERENCES notification_templates(id),
    INDEX idx_recipient_user (recipient_user_id),
    INDEX idx_status (status),
    INDEX idx_notification_type (notification_type),
    INDEX idx_scheduled_at (scheduled_at)
) ENGINE=InnoDB;

-- In-App Notifications (For user dashboard)
CREATE TABLE in_app_notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    notification_title VARCHAR(200) NOT NULL,
    notification_message TEXT NOT NULL,
    notification_type ENUM('INFO', 'WARNING', 'ERROR', 'SUCCESS') DEFAULT 'INFO',
    action_url VARCHAR(500),
    is_read BOOLEAN DEFAULT FALSE,
    read_at DATETIME,
    expires_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB;

-- =====================================================
-- SECTION 15: REPORTS & ANALYTICS
-- =====================================================

-- Saved Reports
CREATE TABLE saved_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_code VARCHAR(50) NOT NULL UNIQUE,
    report_name VARCHAR(200) NOT NULL,
    report_category VARCHAR(50) NOT NULL,
    report_type ENUM('STANDARD', 'CUSTOM', 'SCHEDULED') NOT NULL,
    query_template TEXT,
    parameters JSON,
    output_format ENUM('PDF', 'EXCEL', 'CSV', 'HTML') DEFAULT 'PDF',
    is_public BOOLEAN DEFAULT FALSE,
    created_by BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_report_code (report_code),
    INDEX idx_report_category (report_category),
    INDEX idx_created_by (created_by)
) ENGINE=InnoDB;

-- Report Execution History
CREATE TABLE report_execution_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_id BIGINT NOT NULL,
    executed_by BIGINT NOT NULL,
    parameters_used JSON,
    execution_time_ms INT,
    output_format VARCHAR(20),
    output_file_path VARCHAR(500),
    status ENUM('RUNNING', 'COMPLETED', 'FAILED') NOT NULL,
    error_message TEXT,
    executed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (report_id) REFERENCES saved_reports(id),
    FOREIGN KEY (executed_by) REFERENCES users(id),
    INDEX idx_report_id (report_id),
    INDEX idx_executed_by (executed_by),
    INDEX idx_executed_at (executed_at)
) ENGINE=InnoDB;

-- =====================================================
-- END OF SCHEMA
-- =====================================================
