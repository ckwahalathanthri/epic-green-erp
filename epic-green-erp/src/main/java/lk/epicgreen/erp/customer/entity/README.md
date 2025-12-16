# Customer Module - Epic Green ERP

This directory contains **entities and DTOs** for customer management in the Epic Green ERP system.

## 📦 Contents

### Entities (customer/entity) - 5 Files
1. **Customer.java** - Main customer/buyer entity
2. **CustomerContact.java** - Customer contact persons
3. **CustomerAddress.java** - Customer addresses (billing, shipping)
4. **CustomerLedger.java** - Financial ledger (transactions, payments)
5. **CustomerPriceList.java** - Customer-specific pricing

### DTOs (customer/dto) - 3 Files
1. **CustomerDTO.java** - Customer data transfer object
2. **CreateCustomerRequest.java** - Create customer request
3. **CustomerLedgerDTO.java** - Customer ledger data transfer object

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────────────┐
│    Customer      │ (Main customer entity)
└────────┬─────────┘
         │ 1:N
         ├──────────────────────┬─────────────────────┬──────────────────┐
         ▼                      ▼                     ▼                  ▼
┌─────────────────┐  ┌──────────────────┐  ┌─────────────────┐  ┌──────────────────┐
│CustomerContact  │  │CustomerAddress   │  │CustomerLedger   │  │CustomerPriceList │
└─────────────────┘  └──────────────────┘  └─────────────────┘  └────────┬─────────┘
                                                                           │ N:1
                                                                           ▼
                                                                     ┌─────────┐
                                                                     │ Product │
                                                                     └─────────┘
```

---

## 📋 1. Customer Entity

**Purpose:** Main customer/buyer entity representing business customers

### Key Fields

```java
// Identification
- customerCode (unique, e.g., "CUST-001")
- customerName (business name)
- customerType (RETAIL, WHOLESALE, DISTRIBUTOR, EXPORT)
- registrationNumber (business registration)
- taxNumber (VAT/TIN)

// Contact information
- email
- phoneNumber
- mobileNumber
- faxNumber
- website

// Credit management
- creditLimit (maximum credit allowed)
- creditDays (payment terms in days)
- paymentTerms (description, e.g., "Net 30")
- currency
- creditStatus (ACTIVE, ON_HOLD, SUSPENDED, BLOCKED)
- outstandingBalance (current receivables)

// Pricing
- tradeDiscount (percentage)
- priceLevel (STANDARD, PREMIUM, VIP)

// Sales management
- salesTerritory
- salesRepresentative
- customerSince (customer since date)

// Status
- status (ACTIVE, INACTIVE, BLOCKED)
- isActive

// Loyalty
- loyaltyPoints

// Notes
- notes (visible to customer)
- internalNotes (internal only)

// Relationships
- contacts (Set<CustomerContact>)
- addresses (Set<CustomerAddress>)
- ledgerEntries (Set<CustomerLedger>)
- priceLists (Set<CustomerPriceList>)
```

### Helper Methods

```java
// Get primary contact
CustomerContact getPrimaryContact();

// Get addresses
CustomerAddress getBillingAddress();
CustomerAddress getShippingAddress();

// Credit management
BigDecimal getAvailableCredit(); // creditLimit - outstandingBalance
BigDecimal getCreditUtilization(); // (outstanding / creditLimit) * 100
boolean isCreditLimitExceeded(); // outstanding > creditLimit
boolean canPurchase(BigDecimal amount); // check if can purchase amount
```

### Table Structure

```sql
CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_code VARCHAR(50) NOT NULL UNIQUE,
    customer_name VARCHAR(200) NOT NULL,
    customer_type VARCHAR(20) NOT NULL,
    registration_number VARCHAR(100),
    tax_number VARCHAR(100),
    
    email VARCHAR(100),
    phone_number VARCHAR(20),
    mobile_number VARCHAR(20),
    fax_number VARCHAR(20),
    website VARCHAR(200),
    
    credit_limit DECIMAL(15,2),
    credit_days INT,
    payment_terms VARCHAR(255),
    currency VARCHAR(10) DEFAULT 'LKR',
    credit_status VARCHAR(20) DEFAULT 'ACTIVE',
    outstanding_balance DECIMAL(15,2) DEFAULT 0,
    
    trade_discount DECIMAL(5,2),
    price_level VARCHAR(20) DEFAULT 'STANDARD',
    
    sales_territory VARCHAR(100),
    sales_representative VARCHAR(50),
    customer_since DATE,
    
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    is_active BOOLEAN DEFAULT TRUE,
    loyalty_points INT DEFAULT 0,
    
    notes TEXT,
    internal_notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    INDEX idx_customer_code (customer_code),
    INDEX idx_customer_name (customer_name),
    INDEX idx_customer_type (customer_type),
    INDEX idx_customer_email (email),
    INDEX idx_customer_phone (phone_number),
    INDEX idx_customer_status (status)
);
```

### Customer Types

- **RETAIL** - Individual/small retail buyers
- **WHOLESALE** - Wholesale distributors
- **DISTRIBUTOR** - Large-scale distributors
- **EXPORT** - Export customers (international)

### Credit Status

- **ACTIVE** - Normal credit terms apply
- **ON_HOLD** - Temporary hold, review required
- **SUSPENDED** - Credit suspended, cash only
- **BLOCKED** - Blocked from purchases

---

## 👤 2. CustomerContact Entity

**Purpose:** Contact persons for customers

### Key Fields

```java
// References
- customer (FK)

// Contact information
- contactName
- designation (job title)
- department
- email
- phoneNumber
- mobileNumber

// Flags
- isPrimary (primary contact)
- isActive

- notes
```

### Helper Methods

```java
String getFullContactInfo(); // "John Doe - Manager (Sales)"
```

### Table Structure

```sql
CREATE TABLE customer_contacts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    contact_name VARCHAR(100) NOT NULL,
    designation VARCHAR(100),
    department VARCHAR(100),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    mobile_number VARCHAR(20),
    is_primary BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_customer_contact_customer 
        FOREIGN KEY (customer_id) REFERENCES customers(id),
    INDEX idx_customer_contact_customer (customer_id),
    INDEX idx_customer_contact_primary (is_primary),
    INDEX idx_customer_contact_email (email)
);
```

---

## 📍 3. CustomerAddress Entity

**Purpose:** Customer addresses (billing, shipping, etc.)

### Key Fields

```java
// References
- customer (FK)

// Address type
- addressType (BILLING, SHIPPING, BOTH)
- addressName (label, e.g., "Main Office")

// Address details
- addressLine1
- addressLine2
- city
- state
- postalCode
- country

// Contact at address
- contactPerson
- phoneNumber

// Flags
- isDefault (default for this type)
- isActive

- notes
```

### Helper Methods

```java
String getFullAddress(); // Multi-line formatted address
String getSingleLineAddress(); // Single line with commas
```

### Table Structure

```sql
CREATE TABLE customer_addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    address_type VARCHAR(20) NOT NULL,
    address_name VARCHAR(100),
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone_number VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_customer_address_customer 
        FOREIGN KEY (customer_id) REFERENCES customers(id),
    INDEX idx_customer_address_customer (customer_id),
    INDEX idx_customer_address_type (address_type),
    INDEX idx_customer_address_default (is_default)
);
```

### Address Types

- **BILLING** - Billing address for invoices
- **SHIPPING** - Shipping/delivery address
- **BOTH** - Same address for billing and shipping

---

## 💰 4. CustomerLedger Entity

**Purpose:** Financial ledger tracking all customer transactions

### Key Fields

```java
// References
- customer (FK)

// Transaction details
- transactionDate
- transactionType (SALE, PAYMENT, RETURN, ADJUSTMENT, OPENING_BALANCE)
- referenceNumber (invoice/receipt number)
- referenceId (invoice/payment ID)
- referenceType (SALES_INVOICE, PAYMENT, CREDIT_NOTE, DEBIT_NOTE)

// Amounts
- debitAmount (increases receivable - sales)
- creditAmount (decreases receivable - payments)
- balance (running balance after transaction)
- currency

// Due date
- dueDate (for sales)

// Payment details (for PAYMENT transactions)
- paymentMethod (CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, MOBILE_PAYMENT)
- chequeNumber
- chequeDate
- bankName
- transactionReference

// Description and status
- description
- status (PENDING, CLEARED, BOUNCED, CANCELLED)
- isReconciled
- reconciliationDate
- notes
```

### Helper Methods

```java
BigDecimal getNetAmount(); // debitAmount - creditAmount
boolean isOverdue(); // past due date and balance > 0
Long getDaysOverdue(); // days past due date
boolean isChequeBounced(); // status == BOUNCED
boolean isSettled(); // balance == 0
```

### Table Structure

```sql
CREATE TABLE customer_ledger (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_type VARCHAR(30) NOT NULL,
    reference_number VARCHAR(50),
    reference_id BIGINT,
    reference_type VARCHAR(30),
    
    debit_amount DECIMAL(15,2) DEFAULT 0,
    credit_amount DECIMAL(15,2) DEFAULT 0,
    balance DECIMAL(15,2) DEFAULT 0,
    currency VARCHAR(10) DEFAULT 'LKR',
    
    due_date DATE,
    
    payment_method VARCHAR(30),
    cheque_number VARCHAR(50),
    cheque_date DATE,
    bank_name VARCHAR(100),
    transaction_reference VARCHAR(100),
    
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    is_reconciled BOOLEAN DEFAULT FALSE,
    reconciliation_date DATE,
    notes TEXT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_customer_ledger_customer 
        FOREIGN KEY (customer_id) REFERENCES customers(id),
    INDEX idx_customer_ledger_customer (customer_id),
    INDEX idx_customer_ledger_date (transaction_date),
    INDEX idx_customer_ledger_type (transaction_type),
    INDEX idx_customer_ledger_ref (reference_number),
    INDEX idx_customer_ledger_status (status)
);
```

### Transaction Types

- **SALE** - Sales invoice (increases receivable)
- **PAYMENT** - Payment received (decreases receivable)
- **RETURN** - Sales return/credit note (decreases receivable)
- **ADJUSTMENT** - Manual adjustment (debit/credit note)
- **OPENING_BALANCE** - Opening balance entry

### Ledger Entry Logic

```
Debit (+):  SALE, DEBIT_NOTE
Credit (-): PAYMENT, CREDIT_NOTE, RETURN

Balance = Previous Balance + Debit - Credit
```

---

## 💵 5. CustomerPriceList Entity

**Purpose:** Customer-specific product pricing

### Key Fields

```java
// References
- customer (FK)
- product (FK)

// Pricing
- unitPrice (special price for this customer)
- minimumOrderQuantity
- currency

// Validity
- validFrom
- validTo
- isActive

- notes
```

### Helper Methods

```java
boolean isValidNow(); // check if price is currently valid
boolean isValidOn(LocalDate date); // check validity on specific date
BigDecimal getPriceForQuantity(BigDecimal quantity); // get price, check MOQ
```

### Table Structure

```sql
CREATE TABLE customer_price_lists (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price DECIMAL(15,2) NOT NULL,
    minimum_order_quantity DECIMAL(15,3),
    currency VARCHAR(10) DEFAULT 'LKR',
    valid_from DATE,
    valid_to DATE,
    is_active BOOLEAN DEFAULT TRUE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT fk_customer_price_customer 
        FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_customer_price_product 
        FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT uk_customer_product 
        UNIQUE (customer_id, product_id),
    INDEX idx_customer_price_customer (customer_id),
    INDEX idx_customer_price_product (product_id),
    INDEX idx_customer_price_valid (valid_from, valid_to)
);
```

---

## 💡 Usage Examples

### Example 1: Create Customer with Contacts and Addresses

```java
@Transactional
public CustomerDTO createCustomer(CreateCustomerRequest request) {
    // Create customer
    Customer customer = Customer.builder()
        .customerCode(generateCustomerCode())
        .customerName(request.getCustomerName())
        .customerType(request.getCustomerType())
        .email(request.getEmail())
        .phoneNumber(request.getPhoneNumber())
        .creditLimit(request.getCreditLimit())
        .creditDays(request.getCreditDays())
        .paymentTerms(request.getPaymentTerms())
        .tradeDiscount(request.getTradeDiscount())
        .priceLevel(request.getPriceLevel())
        .salesTerritory(request.getSalesTerritory())
        .salesRepresentative(request.getSalesRepresentative())
        .status("ACTIVE")
        .build();
    
    // Add contacts
    if (request.getContacts() != null) {
        for (var contactReq : request.getContacts()) {
            CustomerContact contact = CustomerContact.builder()
                .contactName(contactReq.getContactName())
                .designation(contactReq.getDesignation())
                .department(contactReq.getDepartment())
                .email(contactReq.getEmail())
                .phoneNumber(contactReq.getPhoneNumber())
                .isPrimary(contactReq.getIsPrimary())
                .build();
            
            customer.addContact(contact);
        }
    }
    
    // Add addresses
    if (request.getAddresses() != null) {
        for (var addressReq : request.getAddresses()) {
            CustomerAddress address = CustomerAddress.builder()
                .addressType(addressReq.getAddressType())
                .addressName(addressReq.getAddressName())
                .addressLine1(addressReq.getAddressLine1())
                .addressLine2(addressReq.getAddressLine2())
                .city(addressReq.getCity())
                .state(addressReq.getState())
                .postalCode(addressReq.getPostalCode())
                .country(addressReq.getCountry())
                .contactPerson(addressReq.getContactPerson())
                .phoneNumber(addressReq.getPhoneNumber())
                .isDefault(addressReq.getIsDefault())
                .build();
            
            customer.addAddress(address);
        }
    }
    
    customer = customerRepository.save(customer);
    return mapper.toDTO(customer);
}
```

### Example 2: Record Sales Invoice in Ledger

```java
@Transactional
public void recordSalesInvoice(SalesInvoice invoice) {
    Customer customer = invoice.getCustomer();
    
    // Create ledger entry
    CustomerLedger ledger = CustomerLedger.builder()
        .customer(customer)
        .transactionDate(invoice.getInvoiceDate())
        .transactionType("SALE")
        .referenceNumber(invoice.getInvoiceNumber())
        .referenceId(invoice.getId())
        .referenceType("SALES_INVOICE")
        .debitAmount(invoice.getTotalAmount()) // Increases receivable
        .creditAmount(BigDecimal.ZERO)
        .description("Sales Invoice - " + invoice.getInvoiceNumber())
        .status("PENDING")
        .build();
    
    // Calculate due date
    if (customer.getCreditDays() != null) {
        ledger.setDueDate(
            invoice.getInvoiceDate().plusDays(customer.getCreditDays())
        );
    }
    
    // Calculate running balance
    BigDecimal previousBalance = getCustomerBalance(customer.getId());
    ledger.setBalance(previousBalance.add(invoice.getTotalAmount()));
    
    customerLedgerRepository.save(ledger);
    
    // Update customer outstanding balance
    customer.setOutstandingBalance(ledger.getBalance());
    customerRepository.save(customer);
}
```

### Example 3: Record Payment Received

```java
@Transactional
public void recordPayment(Long customerId, PaymentRequest request) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new NotFoundException("Customer not found"));
    
    // Create ledger entry
    CustomerLedger ledger = CustomerLedger.builder()
        .customer(customer)
        .transactionDate(request.getPaymentDate())
        .transactionType("PAYMENT")
        .referenceNumber(generateReceiptNumber())
        .debitAmount(BigDecimal.ZERO)
        .creditAmount(request.getAmount()) // Decreases receivable
        .paymentMethod(request.getPaymentMethod())
        .description("Payment received")
        .status("PENDING")
        .build();
    
    // Handle different payment methods
    switch (request.getPaymentMethod()) {
        case "CHEQUE":
            ledger.setChequeNumber(request.getChequeNumber());
            ledger.setChequeDate(request.getChequeDate());
            ledger.setBankName(request.getBankName());
            break;
        case "BANK_TRANSFER":
            ledger.setTransactionReference(request.getTransactionReference());
            ledger.setBankName(request.getBankName());
            break;
        case "CASH":
            ledger.setStatus("CLEARED"); // Cash is immediately cleared
            break;
    }
    
    // Calculate running balance
    BigDecimal previousBalance = getCustomerBalance(customerId);
    ledger.setBalance(previousBalance.subtract(request.getAmount()));
    
    customerLedgerRepository.save(ledger);
    
    // Update customer outstanding balance
    customer.setOutstandingBalance(ledger.getBalance());
    customerRepository.save(customer);
}
```

### Example 4: Check Credit Limit Before Sale

```java
public boolean checkCreditLimit(Long customerId, BigDecimal saleAmount) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new NotFoundException("Customer not found"));
    
    // Check if customer is active
    if (!customer.getIsActive()) {
        throw new BusinessException("Customer is inactive");
    }
    
    // Check credit status
    if (!"ACTIVE".equals(customer.getCreditStatus())) {
        throw new BusinessException("Customer credit is " + customer.getCreditStatus());
    }
    
    // Check if can purchase
    if (!customer.canPurchase(saleAmount)) {
        throw new BusinessException(
            "Credit limit exceeded. Available credit: " + 
            customer.getAvailableCredit() + 
            ", Required: " + saleAmount
        );
    }
    
    return true;
}
```

### Example 5: Get Customer Pricing

```java
public BigDecimal getCustomerPrice(Long customerId, Long productId, LocalDate date) {
    // Check for customer-specific pricing
    Optional<CustomerPriceList> customerPrice = customerPriceListRepository
        .findByCustomerAndProduct(customerId, productId);
    
    if (customerPrice.isPresent() && customerPrice.get().isValidOn(date)) {
        return customerPrice.get().getUnitPrice();
    }
    
    // Get customer
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new NotFoundException("Customer not found"));
    
    // Get product
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product not found"));
    
    // Get base price from product based on price level
    BigDecimal basePrice;
    switch (customer.getPriceLevel()) {
        case "PREMIUM":
            basePrice = product.getPremiumPrice();
            break;
        case "VIP":
            basePrice = product.getVipPrice();
            break;
        default:
            basePrice = product.getSellingPrice();
    }
    
    // Apply trade discount
    if (customer.getTradeDiscount() != null) {
        BigDecimal discount = basePrice.multiply(customer.getTradeDiscount())
            .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        basePrice = basePrice.subtract(discount);
    }
    
    return basePrice;
}
```

### Example 6: Generate Customer Statement

```java
public CustomerStatementDTO generateStatement(
    Long customerId, 
    LocalDate fromDate, 
    LocalDate toDate
) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new NotFoundException("Customer not found"));
    
    // Get ledger entries
    List<CustomerLedger> entries = customerLedgerRepository
        .findByCustomerAndDateRange(customerId, fromDate, toDate);
    
    // Calculate totals
    BigDecimal totalSales = entries.stream()
        .filter(e -> "SALE".equals(e.getTransactionType()))
        .map(CustomerLedger::getDebitAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    BigDecimal totalPayments = entries.stream()
        .filter(e -> "PAYMENT".equals(e.getTransactionType()))
        .map(CustomerLedger::getCreditAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    BigDecimal totalReturns = entries.stream()
        .filter(e -> "RETURN".equals(e.getTransactionType()))
        .map(CustomerLedger::getCreditAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    // Get overdue invoices
    List<CustomerLedger> overdueInvoices = entries.stream()
        .filter(e -> "SALE".equals(e.getTransactionType()))
        .filter(CustomerLedger::isOverdue)
        .collect(Collectors.toList());
    
    return CustomerStatementDTO.builder()
        .customer(mapper.toDTO(customer))
        .fromDate(fromDate)
        .toDate(toDate)
        .openingBalance(getBalanceAsOf(customerId, fromDate.minusDays(1)))
        .totalSales(totalSales)
        .totalPayments(totalPayments)
        .totalReturns(totalReturns)
        .closingBalance(customer.getOutstandingBalance())
        .entries(entries.stream().map(mapper::toDTO).collect(Collectors.toList()))
        .overdueInvoices(overdueInvoices.stream().map(mapper::toDTO).collect(Collectors.toList()))
        .build();
}
```

---

## 📁 Directory Structure

```
customer/
├── entity/
│   ├── Customer.java
│   ├── CustomerContact.java
│   ├── CustomerAddress.java
│   ├── CustomerLedger.java
│   ├── CustomerPriceList.java
│   └── README.md
└── dto/
    ├── CustomerDTO.java
    ├── CreateCustomerRequest.java
    └── CustomerLedgerDTO.java
```

---

## ✅ Summary

✅ **5 Entity classes** - Complete customer management  
✅ **3 DTO classes** - Request/response objects  
✅ **Customer types** - RETAIL, WHOLESALE, DISTRIBUTOR, EXPORT  
✅ **Credit management** - Limits, terms, status, outstanding balance  
✅ **Credit tracking** - Available credit, utilization percentage  
✅ **Multiple contacts** - Primary contact designation  
✅ **Multiple addresses** - Billing, shipping, both  
✅ **Financial ledger** - Complete transaction history  
✅ **Receivables tracking** - Debit/credit entries with running balance  
✅ **Payment methods** - Cash, cheque, bank transfer, credit card, mobile  
✅ **Cheque management** - Tracking cheque details, bounce status  
✅ **Due date tracking** - Overdue detection and days overdue  
✅ **Customer-specific pricing** - Special prices per customer  
✅ **Price validity** - Time-bound pricing  
✅ **Trade discounts** - Percentage-based discounts  
✅ **Price levels** - STANDARD, PREMIUM, VIP  
✅ **Sales territory** - Territory and rep assignment  
✅ **Loyalty points** - Customer loyalty tracking  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade customer management  

**Everything you need for complete customer and receivables management in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.customer
