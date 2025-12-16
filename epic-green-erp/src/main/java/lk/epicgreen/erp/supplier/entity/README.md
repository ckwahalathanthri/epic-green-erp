# Supplier Module - Epic Green ERP

This directory contains **entities and DTOs** for supplier management in the Epic Green ERP system.

## 📦 Contents

### Entities (supplier/entity) - 3 Files
1. **Supplier.java** - Main supplier entity
2. **SupplierContact.java** - Supplier contact persons
3. **SupplierLedger.java** - Supplier financial ledger

### DTOs (supplier/dto) - 6 Files
1. **SupplierDTO.java** - Supplier data transfer object
2. **SupplierContactDTO.java** - Contact data transfer object
3. **SupplierLedgerDTO.java** - Ledger data transfer object
4. **CreateSupplierRequest.java** - Create supplier request
5. **CreateSupplierContactRequest.java** - Create contact request
6. **UpdateSupplierRequest.java** - Update supplier request

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌────────────┐
│  Supplier  │ (Main supplier information)
└─────┬──────┘
      │
      │ 1:N
      ├──────────────┐
      │              │
      ▼              ▼
┌──────────────┐  ┌─────────────────┐
│SupplierContact│  │ SupplierLedger  │
└──────────────┘  └─────────────────┘
(Contact persons)  (Financial transactions)
```

### Relationships

- **Supplier** → **SupplierContact**: One-to-Many
  - One supplier can have multiple contact persons
  - Cascade delete (when supplier deleted, contacts deleted)

- **Supplier** → **SupplierLedger**: One-to-Many
  - One supplier has many ledger entries
  - Tracks all financial transactions (purchases, payments, credits)

---

## 🏢 1. Supplier Entity

**Purpose:** Main supplier information and business details

### Key Fields

```java
// Identification
- supplierCode (unique, e.g., "SUP-001")
- supplierName
- supplierType (RAW_MATERIAL, PACKAGING, SERVICES)
- registrationNumber
- vatNumber

// Contact Information
- email
- phoneNumber, mobileNumber, faxNumber
- website

// Address
- addressLine1, addressLine2
- city, state, postalCode, country

// Financial
- creditLimit
- currentBalance (outstanding amount)
- paymentTermsDays (e.g., 30, 60, 90)
- paymentTerms (description)
- currency (LKR, USD, etc.)

// Bank Details
- bankName, bankAccountNumber
- bankAccountHolder
- bankBranch, bankSwiftCode

// Status & Rating
- status (ACTIVE, INACTIVE, BLOCKED, PENDING)
- isApproved
- approvedBy
- rating (1-5 stars)
- isPreferred (preferred supplier flag)

// Business Terms
- leadTimeDays
- minimumOrderValue
- termsAndConditions

// Relationships
- contacts (Set<SupplierContact>)
- ledgerEntries (Set<SupplierLedger>)
```

### Helper Methods

```java
// Get full address
String address = supplier.getFullAddress();
// "No 123, Main Street, Colombo, Western Province 00100, Sri Lanka"

// Check if active
boolean active = supplier.isActive(); // status == ACTIVE && isApproved

// Check if blocked
boolean blocked = supplier.isBlocked(); // status == BLOCKED

// Credit limit checks
boolean exceeded = supplier.isCreditLimitExceeded(); // currentBalance > creditLimit
BigDecimal available = supplier.getAvailableCredit(); // creditLimit - currentBalance

// Manage contacts
supplier.addContact(contact);
supplier.removeContact(contact);

// Add ledger entry
supplier.addLedgerEntry(entry);
```

### Table Structure

```sql
CREATE TABLE suppliers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_code VARCHAR(20) NOT NULL UNIQUE,
    supplier_name VARCHAR(200) NOT NULL,
    supplier_type VARCHAR(50) NOT NULL,
    registration_number VARCHAR(50),
    vat_number VARCHAR(50),
    
    -- Contact
    email VARCHAR(100),
    phone_number VARCHAR(20),
    mobile_number VARCHAR(20),
    fax_number VARCHAR(20),
    website VARCHAR(255),
    
    -- Address
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    
    -- Financial
    credit_limit DECIMAL(15,2),
    current_balance DECIMAL(15,2) DEFAULT 0,
    payment_terms_days INT,
    payment_terms VARCHAR(255),
    currency VARCHAR(10) DEFAULT 'LKR',
    
    -- Bank
    bank_name VARCHAR(100),
    bank_account_number VARCHAR(50),
    bank_account_holder VARCHAR(100),
    bank_branch VARCHAR(100),
    bank_swift_code VARCHAR(20),
    
    -- Status
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    approved_by VARCHAR(50),
    rating INT,
    is_preferred BOOLEAN DEFAULT FALSE,
    
    -- Terms
    lead_time_days INT,
    minimum_order_value DECIMAL(15,2),
    terms_and_conditions TEXT,
    notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    INDEX idx_supplier_code (supplier_code),
    INDEX idx_supplier_name (supplier_name),
    INDEX idx_supplier_status (status),
    INDEX idx_supplier_type (supplier_type)
);
```

### Supplier Types

```java
RAW_MATERIAL      - Suppliers of raw spices and ingredients
PACKAGING         - Suppliers of packaging materials
SERVICES          - Service providers
EQUIPMENT         - Equipment suppliers
UTILITIES         - Utility service providers
OTHER             - Other suppliers
```

### Supplier Status

```java
PENDING    - Newly created, awaiting approval
ACTIVE     - Approved and active
INACTIVE   - Temporarily inactive
BLOCKED    - Blocked from transactions
```

---

## 👤 2. SupplierContact Entity

**Purpose:** Contact persons at supplier organizations

### Key Fields

```java
- supplier (FK to Supplier)
- contactName
- designation (Job title)
- department
- email
- phoneNumber, mobileNumber
- extension (Phone extension)
- isPrimary (Primary contact flag)
- isActive
- notes
```

### Helper Methods

```java
// Get full contact info
String info = contact.getFullContactInfo();
// "John Doe - Procurement Manager (Purchase Department)"
```

### Table Structure

```sql
CREATE TABLE supplier_contacts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_id BIGINT NOT NULL,
    contact_name VARCHAR(100) NOT NULL,
    designation VARCHAR(100),
    department VARCHAR(100),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    mobile_number VARCHAR(20),
    extension VARCHAR(10),
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_supplier_contact_supplier 
        FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    INDEX idx_supplier_contact_supplier (supplier_id),
    INDEX idx_supplier_contact_email (email)
);
```

---

## 💰 3. SupplierLedger Entity

**Purpose:** Financial transactions with suppliers

### Key Fields

```java
- supplier (FK to Supplier)
- transactionDate
- transactionType (PURCHASE, PAYMENT, CREDIT_NOTE, DEBIT_NOTE, OPENING_BALANCE)
- referenceNumber (PO number, payment voucher, etc.)
- referenceId (ID of related entity)
- referenceType (PURCHASE_ORDER, PAYMENT, GRN, etc.)
- description

// Amounts
- debitAmount (purchases, charges)
- creditAmount (payments, credit notes)
- balance (running balance)
- currency

// Payment tracking
- dueDate (for purchases)
- paymentStatus (PENDING, PAID, PARTIALLY_PAID, OVERDUE)

// Reconciliation
- isReconciled
- reconciledDate
- reconciledBy

- notes
```

### Helper Methods

```java
// Get transaction amount
BigDecimal amount = ledger.getTransactionAmount(); // debit - credit

// Check if overdue
boolean overdue = ledger.isOverdue(); // dueDate < today && not paid

// Get days overdue
long days = ledger.getDaysOverdue();
```

### Table Structure

```sql
CREATE TABLE supplier_ledger (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supplier_id BIGINT NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_type VARCHAR(30) NOT NULL,
    reference_number VARCHAR(50),
    reference_id BIGINT,
    reference_type VARCHAR(30),
    description VARCHAR(500),
    
    -- Amounts
    debit_amount DECIMAL(15,2) DEFAULT 0,
    credit_amount DECIMAL(15,2) DEFAULT 0,
    balance DECIMAL(15,2),
    currency VARCHAR(10) DEFAULT 'LKR',
    
    -- Payment
    due_date DATE,
    payment_status VARCHAR(20),
    
    -- Reconciliation
    is_reconciled BOOLEAN DEFAULT FALSE,
    reconciled_date DATE,
    reconciled_by VARCHAR(50),
    
    notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_supplier_ledger_supplier 
        FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    INDEX idx_supplier_ledger_supplier (supplier_id),
    INDEX idx_supplier_ledger_date (transaction_date),
    INDEX idx_supplier_ledger_type (transaction_type),
    INDEX idx_supplier_ledger_reference (reference_number)
);
```

### Transaction Types

```java
OPENING_BALANCE  - Opening balance entry
PURCHASE         - Purchase from supplier (debit)
PAYMENT          - Payment to supplier (credit)
CREDIT_NOTE      - Credit note from supplier (credit)
DEBIT_NOTE       - Debit note to supplier (debit)
ADJUSTMENT       - Balance adjustment
```

### Payment Status

```java
PENDING         - Payment not yet made
PAID            - Fully paid
PARTIALLY_PAID  - Partially paid
OVERDUE         - Past due date
```

---

## 📋 DTOs

### SupplierDTO

**Purpose:** Transfer supplier data (includes contacts and statistics)

```json
{
  "id": 1,
  "supplierCode": "SUP-001",
  "supplierName": "ABC Spices Ltd",
  "supplierType": "RAW_MATERIAL",
  "registrationNumber": "REG-12345",
  "vatNumber": "VAT-67890",
  "email": "info@abcspices.com",
  "phoneNumber": "+94112345678",
  "mobileNumber": "+94771234567",
  "addressLine1": "No 123, Main Street",
  "city": "Colombo",
  "country": "Sri Lanka",
  "fullAddress": "No 123, Main Street, Colombo, Sri Lanka",
  "creditLimit": 500000.00,
  "currentBalance": 150000.00,
  "availableCredit": 350000.00,
  "paymentTermsDays": 30,
  "paymentTerms": "Net 30 days",
  "currency": "LKR",
  "bankName": "Commercial Bank",
  "bankAccountNumber": "1234567890",
  "rating": 5,
  "isPreferred": true,
  "leadTimeDays": 7,
  "status": "ACTIVE",
  "isApproved": true,
  "approvedBy": "admin",
  "contacts": [
    {
      "id": 1,
      "contactName": "John Doe",
      "designation": "Sales Manager",
      "email": "john@abcspices.com",
      "phoneNumber": "+94712345678",
      "isPrimary": true
    }
  ],
  "totalPurchaseOrders": 150,
  "totalPurchaseAmount": 5000000.00,
  "totalPaymentAmount": 4850000.00,
  "pendingPaymentCount": 3,
  "isActive": true,
  "isBlocked": false,
  "isCreditLimitExceeded": false,
  "createdAt": "2024-01-01T00:00:00",
  "createdBy": "admin"
}
```

### CreateSupplierRequest

**Purpose:** Create new supplier

```json
{
  "supplierCode": "SUP-002",
  "supplierName": "XYZ Packaging",
  "supplierType": "PACKAGING",
  "registrationNumber": "REG-54321",
  "email": "info@xyzpack.com",
  "phoneNumber": "+94112223344",
  "mobileNumber": "+94771112222",
  "addressLine1": "No 456, Industrial Zone",
  "city": "Gampaha",
  "country": "Sri Lanka",
  "creditLimit": 300000.00,
  "paymentTermsDays": 45,
  "currency": "LKR",
  "bankName": "People's Bank",
  "bankAccountNumber": "9876543210",
  "isPreferred": false,
  "leadTimeDays": 10,
  "status": "PENDING",
  "contacts": [
    {
      "contactName": "Jane Smith",
      "designation": "Account Manager",
      "email": "jane@xyzpack.com",
      "phoneNumber": "+94771234567",
      "isPrimary": true
    }
  ]
}
```

**Validation Rules:**
- supplierCode: Required, 2-20 chars, alphanumeric + hyphen
- supplierName: Required, 2-200 chars
- supplierType: Required
- email: Valid email format
- phoneNumber/mobileNumber: Valid phone pattern
- creditLimit: Non-negative, max 13 digits + 2 decimals
- paymentTermsDays: 0-365
- rating: 1-5

### SupplierLedgerDTO

**Purpose:** Transfer ledger transaction data

```json
{
  "id": 101,
  "supplierId": 1,
  "supplierCode": "SUP-001",
  "supplierName": "ABC Spices Ltd",
  "transactionDate": "2024-12-01",
  "transactionType": "PURCHASE",
  "referenceNumber": "PO-2024-001",
  "referenceId": 501,
  "referenceType": "PURCHASE_ORDER",
  "description": "Purchase of raw cinnamon - 1000 kg",
  "debitAmount": 250000.00,
  "creditAmount": 0.00,
  "balance": 150000.00,
  "currency": "LKR",
  "dueDate": "2024-12-31",
  "paymentStatus": "PENDING",
  "isReconciled": false,
  "transactionAmount": 250000.00,
  "isOverdue": false,
  "daysOverdue": 0,
  "createdAt": "2024-12-01T10:00:00",
  "createdBy": "admin"
}
```

---

## 💡 Usage Examples

### Example 1: Create Supplier with Contacts

```java
@Service
@RequiredArgsConstructor
public class SupplierService {
    
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    
    @Transactional
    public SupplierDTO createSupplier(CreateSupplierRequest request) {
        // Validate unique supplier code
        if (supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
            throw new DuplicateResourceException("Supplier code already exists");
        }
        
        // Create supplier
        Supplier supplier = supplierMapper.toEntity(request);
        
        // Add contacts
        if (request.getContacts() != null) {
            for (CreateSupplierContactRequest contactRequest : request.getContacts()) {
                SupplierContact contact = supplierMapper.toContactEntity(contactRequest);
                supplier.addContact(contact);
            }
        }
        
        // Save
        supplier = supplierRepository.save(supplier);
        
        return supplierMapper.toDTO(supplier);
    }
}
```

### Example 2: Add Purchase to Ledger

```java
@Service
@RequiredArgsConstructor
public class PurchaseService {
    
    private final SupplierRepository supplierRepository;
    private final SupplierLedgerRepository ledgerRepository;
    
    @Transactional
    public void recordPurchase(Long supplierId, PurchaseOrder purchaseOrder) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId));
        
        // Calculate due date
        LocalDate dueDate = LocalDate.now();
        if (supplier.getPaymentTermsDays() != null) {
            dueDate = dueDate.plusDays(supplier.getPaymentTermsDays());
        }
        
        // Get current balance
        BigDecimal currentBalance = supplier.getCurrentBalance();
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        
        // Create ledger entry
        SupplierLedger ledger = SupplierLedger.builder()
            .supplier(supplier)
            .transactionDate(LocalDate.now())
            .transactionType("PURCHASE")
            .referenceNumber(purchaseOrder.getPoNumber())
            .referenceId(purchaseOrder.getId())
            .referenceType("PURCHASE_ORDER")
            .description("Purchase Order: " + purchaseOrder.getPoNumber())
            .debitAmount(purchaseOrder.getTotalAmount())
            .creditAmount(BigDecimal.ZERO)
            .balance(currentBalance.add(purchaseOrder.getTotalAmount()))
            .currency(supplier.getCurrency())
            .dueDate(dueDate)
            .paymentStatus("PENDING")
            .build();
        
        ledgerRepository.save(ledger);
        
        // Update supplier balance
        supplier.setCurrentBalance(ledger.getBalance());
        supplierRepository.save(supplier);
        
        // Check credit limit
        if (supplier.isCreditLimitExceeded()) {
            // Send notification or take action
            log.warn("Supplier {} has exceeded credit limit", supplier.getSupplierCode());
        }
    }
}
```

### Example 3: Record Payment

```java
@Transactional
public void recordPayment(Long supplierId, PaymentRequest request) {
    Supplier supplier = supplierRepository.findById(supplierId)
        .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId));
    
    // Get current balance
    BigDecimal currentBalance = supplier.getCurrentBalance();
    
    // Create payment ledger entry
    SupplierLedger ledger = SupplierLedger.builder()
        .supplier(supplier)
        .transactionDate(LocalDate.now())
        .transactionType("PAYMENT")
        .referenceNumber(request.getPaymentVoucherNumber())
        .referenceId(request.getPaymentId())
        .referenceType("PAYMENT")
        .description("Payment: " + request.getPaymentMethod())
        .debitAmount(BigDecimal.ZERO)
        .creditAmount(request.getAmount())
        .balance(currentBalance.subtract(request.getAmount()))
        .currency(supplier.getCurrency())
        .paymentStatus("PAID")
        .build();
    
    ledgerRepository.save(ledger);
    
    // Update supplier balance
    supplier.setCurrentBalance(ledger.getBalance());
    supplierRepository.save(supplier);
}
```

### Example 4: Get Supplier Ledger Statement

```java
@Transactional(readOnly = true)
public List<SupplierLedgerDTO> getSupplierStatement(
        Long supplierId, 
        LocalDate startDate, 
        LocalDate endDate) {
    
    List<SupplierLedger> ledgerEntries = ledgerRepository
        .findBySupplierIdAndTransactionDateBetweenOrderByTransactionDateAsc(
            supplierId, startDate, endDate
        );
    
    return ledgerEntries.stream()
        .map(supplierMapper::toLedgerDTO)
        .toList();
}
```

### Example 5: Get Overdue Payments

```java
@Transactional(readOnly = true)
public List<SupplierLedgerDTO> getOverduePayments() {
    LocalDate today = LocalDate.now();
    
    List<SupplierLedger> overdueEntries = ledgerRepository
        .findByDueDateBeforeAndPaymentStatusIn(
            today, 
            List.of("PENDING", "PARTIALLY_PAID")
        );
    
    return overdueEntries.stream()
        .map(supplierMapper::toLedgerDTO)
        .toList();
}
```

---

## 📁 Directory Structure

```
supplier/
├── entity/
│   ├── Supplier.java
│   ├── SupplierContact.java
│   └── SupplierLedger.java
├── dto/
│   ├── SupplierDTO.java
│   ├── SupplierContactDTO.java
│   ├── SupplierLedgerDTO.java
│   ├── CreateSupplierRequest.java
│   ├── CreateSupplierContactRequest.java
│   └── UpdateSupplierRequest.java
└── README.md
```

---

## 🎯 Key Features

### Supplier Management
✅ **Complete supplier profiles** - Business details, contacts, bank info  
✅ **Supplier types** - Raw material, packaging, services  
✅ **Credit management** - Credit limit, current balance, available credit  
✅ **Payment terms** - Flexible payment terms (net 30, 60, 90 days)  
✅ **Rating system** - 1-5 star rating for suppliers  
✅ **Preferred suppliers** - Mark preferred suppliers  
✅ **Status management** - Active, inactive, blocked, pending  
✅ **Approval workflow** - Requires approval before activation  

### Contact Management
✅ **Multiple contacts** - Store multiple contact persons per supplier  
✅ **Primary contact** - Mark primary contact person  
✅ **Contact details** - Name, designation, department, email, phone  
✅ **Active/inactive** - Enable/disable contacts  

### Financial Tracking
✅ **Complete ledger** - All financial transactions tracked  
✅ **Double-entry** - Debit/credit accounting  
✅ **Running balance** - Balance after each transaction  
✅ **Payment tracking** - Due dates, payment status  
✅ **Overdue detection** - Automatic overdue identification  
✅ **Reconciliation** - Mark transactions as reconciled  
✅ **Multi-currency** - Support for different currencies  

### Business Features
✅ **Lead time tracking** - Delivery lead time in days  
✅ **Minimum order value** - Enforce minimum order amounts  
✅ **Terms and conditions** - Store supplier T&Cs  
✅ **Notes** - Additional notes and comments  
✅ **Audit tracking** - All entities have audit fields  

---

## 📊 Common Queries

### Get Active Suppliers
```java
List<Supplier> activeSuppliers = supplierRepository
    .findByStatusAndIsApproved("ACTIVE", true);
```

### Get Suppliers by Type
```java
List<Supplier> rawMaterialSuppliers = supplierRepository
    .findBySupplierType("RAW_MATERIAL");
```

### Get Preferred Suppliers
```java
List<Supplier> preferredSuppliers = supplierRepository
    .findByIsPreferredTrueAndStatus("ACTIVE");
```

### Get Suppliers with Credit Limit Exceeded
```java
List<Supplier> exceeded = supplierRepository
    .findByCreditLimitExceeded();
```

### Get Supplier Balance
```java
BigDecimal balance = supplierRepository
    .findById(supplierId)
    .map(Supplier::getCurrentBalance)
    .orElse(BigDecimal.ZERO);
```

---

## ✅ Summary

✅ **3 Entity classes** - Supplier, SupplierContact, SupplierLedger  
✅ **6 DTO classes** - Complete request/response objects  
✅ **Credit management** - Credit limits, balance tracking  
✅ **Financial ledger** - Complete transaction history  
✅ **Contact management** - Multiple contacts per supplier  
✅ **Payment tracking** - Due dates, overdue detection  
✅ **Rating system** - Supplier performance ratings  
✅ **Comprehensive validation** - Input validation on all requests  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade supplier management  

**Everything you need for complete supplier management!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.supplier

