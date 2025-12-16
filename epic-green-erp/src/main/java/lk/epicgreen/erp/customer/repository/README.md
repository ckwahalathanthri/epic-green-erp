# Customer Module - Repository, Service & Controller

This directory contains **repository, service, and controller layers** for the customer management system in Epic Green ERP.

## 📦 Contents

### Repository (customer/repository) - 2 Files
1. **CustomerRepository.java** - Customer data access
2. **CustomerLedgerRepository.java** - Customer ledger data access

### Service (customer/service) - 2 Files
1. **CustomerService.java** - Customer service interface
2. **CustomerServiceImpl.java** - Customer service implementation

### Controller (customer/controller) - 2 Files
1. **CustomerController.java** - REST controller for customers
2. **CustomerLedgerController.java** - REST controller for customer ledger

---

## 📊 1. Repositories

### CustomerRepository

**Purpose:** Data access for customer master data

**Key Methods (140+):**
- **Find by unique:** Customer code, email, phone, tax number
- **Find by fields:** Customer type, status, credit status, payment terms, sales rep, route, city, province, country, active flag, verified flag, blacklisted flag, credit facility flag
- **Date ranges:** Registration date, last order date
- **Status queries:** Active, inactive, pending, suspended, blacklisted (5 specialized queries)
- **Credit queries:** With credit facility, credit exceeded, credit warning, with outstanding, with overdue (5 specialized queries)
- **Verification queries:** Unverified, pending verification
- **Activity queries:** Without recent orders (configurable threshold), requiring action
- **Search:** Full-text search on code/name/business/email/phone
- **Statistics:** Count by sales rep/type/status, distributions, monthly registration
- **Financial:** Total sales, outstanding, overdue, credit limit, available credit, average order value
- **Sales rep analysis:** Customers by sales rep with totals (count + sales)
- **Credit metrics:** Credit utilization rate
- **Purpose:** Complete customer master data access

### CustomerLedgerRepository

**Purpose:** Data access for customer account ledger

**Key Methods (110+):**
- **Find by fields:** Customer, transaction type, entry type (debit/credit), reference type, reference ID, invoice ID, payment ID, sales order ID, user, reconciled flag, reversed flag
- **Date ranges:** Transaction date, due date
- **Entry type queries:** Debit entries, credit entries, customer debit/credit entries (4 specialized queries)
- **Reconciliation queries:** Unreconciled entries, customer unreconciled entries (2 specialized queries)
- **Transaction queries:** Invoice entries, payment entries, credit note entries, debit note entries (4 specialized queries)
- **Overdue queries:** Overdue entries, customer overdue entries
- **Balance calculations:** Customer balance (debit - credit), opening balance (before start date)
- **Statement generation:** Ledger statement (all entries in date range with chronological order)
- **Search:** Full-text search on transaction reference/customer/description
- **Statistics:** Count by customer/transaction type, distributions
- **Activity analysis:** Monthly ledger activity (entry count, total debit, total credit)
- **Financial totals:** Total debit, total credit, customer debit/credit, total outstanding
- **Ledger summary:** Customer ledger summary (all customers with totals and balance)
- **Purpose:** Complete customer ledger/account data access

---

## 🔧 2. Service Layer

### CustomerService Interface

**Purpose:** Define customer service contract

**Method Categories (80+ methods):**
1. **CRUD (9 methods):** Create, update, delete, get by ID/code/email/phone, get all, search
2. **Status Operations (6 methods):** Activate, deactivate, suspend, verify, blacklist, remove from blacklist
3. **Credit Operations (5 methods):** Enable credit facility, update credit limit, disable credit facility, update credit status, update customer outstanding
4. **Ledger Operations (8 methods):** Create ledger entry, reverse ledger entry, reconcile ledger entry, get entries, get statement, get balance, get opening balance
5. **Query (18 methods):** Active, inactive, pending, suspended, blacklisted, with credit facility, credit exceeded, credit warning, with outstanding, with overdue, unverified, without recent orders, requiring action, by type/route/sales rep, top customers, recent
6. **Validation (7 methods):** Validate customer, can activate/suspend/enable credit, is email/phone/code available
7. **Calculations (6 methods):** Calculate metrics, update sales amount, update last order date, update average order value, calculate available credit
8. **Batch (4 methods):** Bulk create, activate, deactivate, delete
9. **Statistics (16 methods):** Statistics, distributions (type/status/credit/payment/route/city/province), monthly registration, by sales rep with totals, total sales/outstanding/overdue/credit/available credit, average order value, credit utilization rate, dashboard

### CustomerServiceImpl Implementation

**Purpose:** Implement customer business logic

**Key Features:**
- **Customer creation** - Generate customer code (CUS-timestamp), set defaults (PENDING status, isActive=true, isVerified=false, all counters to 0)
- **Unique validation** - Check email/phone uniqueness on create/update
- **Status management** - Activate (sets isActive=true, status=ACTIVE), deactivate (sets isActive=false, status=INACTIVE), suspend (sets status=SUSPENDED with reason and date)
- **Verification** - Verify customer (sets isVerified=true, status=ACTIVE with verified date/user)
- **Blacklist management** - Blacklist (sets isBlacklisted=true, status=SUSPENDED with reason and date), remove from blacklist (clears blacklist fields, sets ACTIVE)
- **Credit facility** - Enable (sets hasCreditFacility=true, creditLimit, creditStatus=GOOD, calculates available credit), update limit (updates limit, recalculates available credit, updates status), disable (requires zero outstanding, clears all credit fields, sets CASH terms)
- **Credit status tracking** - Auto-update based on outstanding vs limit: EXCEEDED (outstanding > limit), WARNING (outstanding >= 80% of limit), UTILIZED (outstanding > 0), GOOD (outstanding = 0)
- **Outstanding management** - Calculate from ledger balance (debit - credit), update customer outstanding, calculate available credit (limit - outstanding), auto-update credit status
- **Ledger entry creation** - Generate transaction reference (TXN-timestamp), set defaults (isReconciled=false, isReversed=false), update customer outstanding after creation
- **Ledger reversal** - Set isReversed=true with reason and date, update customer outstanding
- **Ledger reconciliation** - Set isReconciled=true with date
- **Balance calculations** - Customer balance from ledger (SUM(debit) - SUM(credit) where isReversed=false), opening balance (balance before start date)
- **Statement generation** - Retrieve all ledger entries in date range, chronological order
- **Customer metrics** - Update total sales amount, total order count, last order date, average order value
- **Query operations** - 18 specialized query methods
- **Validation** - Customer validation (name, email, phone required), eligibility checks
- **Batch operations** - Bulk create, activate, deactivate, delete with error handling
- **Statistics** - Comprehensive analytics with distributions
- **Purpose:** Implement all customer and ledger business logic

---

## 🌐 3. Controller Layer

### CustomerController

**Purpose:** REST API endpoints for customer management

**Endpoints (45+):**
- **CRUD (7):** POST, PUT, DELETE, GET by ID, GET by code, GET by email, GET by phone, GET all, search
- **Status operations (6):** POST activate, POST deactivate, POST suspend, POST verify, POST blacklist, POST remove-blacklist
- **Credit operations (4):** POST enable-credit, POST update-credit-limit, POST disable-credit, GET balance
- **Query (20):** GET active, GET inactive, GET pending, GET suspended, GET blacklisted, GET with-credit, GET credit-exceeded, GET credit-warning, GET with-outstanding, GET with-overdue, GET unverified, GET without-recent-orders, GET requiring-action, GET by-type, GET by-route, GET by-sales-rep, GET top-customers, GET recent
- **Statistics (2):** GET statistics, GET dashboard

### CustomerLedgerController

**Purpose:** REST API endpoints for customer ledger

**Endpoints (7):**
- **Ledger operations (3):** POST create entry, POST reverse, POST reconcile
- **Query (4):** GET customer entries, GET customer statement, GET customer balance, GET customer opening-balance

---

## 💡 Usage Examples

### Example 1: Create Customer

```java
CustomerRequest request = new CustomerRequest();
request.setCustomerName("ABC Traders");
request.setBusinessName("ABC Traders Pvt Ltd");
request.setCustomerType("WHOLESALE");
request.setEmail("abc@example.com");
request.setPhone("+94112345678");
request.setMobile("+94771234567");
request.setAddressLine1("123 Main Street");
request.setCity("Colombo");
request.setProvince("Western");
request.setCountry("Sri Lanka");
request.setPaymentTerms("NET_30");
request.setSalesRepId(salesRepId);

Customer created = customerService.createCustomer(request);
// Auto-generates: customerCode (CUS-timestamp)
// Sets defaults: status=PENDING, isActive=true, isVerified=false
// Validates: email and phone uniqueness
```

### Example 2: Customer Lifecycle

```java
// Create customer
Customer customer = customerService.createCustomer(request);

// Verify customer
Customer verified = customerService.verifyCustomer(
    customer.getId(), 
    verifiedByUserId
);
// Sets: isVerified=true, status=ACTIVE, verifiedDate, verifiedByUserId

// Enable credit facility
Customer withCredit = customerService.enableCreditFacility(
    customer.getId(), 
    100000.00, 
    "NET_30"
);
// Sets: hasCreditFacility=true, creditLimit, creditStatus=GOOD
// Calculates: availableCredit

// Update credit limit
Customer updated = customerService.updateCreditLimit(
    customer.getId(), 
    150000.00
);
// Updates: creditLimit, availableCredit, creditStatus

// Suspend customer
Customer suspended = customerService.suspendCustomer(
    customer.getId(), 
    "Payment overdue by 90 days"
);
// Sets: status=SUSPENDED, suspensionReason, suspendedDate
```

### Example 3: Credit Management

```java
// Enable credit facility
customerService.enableCreditFacility(customerId, 100000.00, "NET_30");

// Create invoice (debit entry)
CustomerLedgerRequest debitRequest = new CustomerLedgerRequest();
debitRequest.setCustomerId(customerId);
debitRequest.setTransactionType("INVOICE");
debitRequest.setEntryType("DEBIT");
debitRequest.setDebitAmount(25000.00);
debitRequest.setInvoiceId(invoiceId);

customerService.createLedgerEntry(debitRequest);
// Auto-updates: customer outstanding, available credit, credit status

// Record payment (credit entry)
CustomerLedgerRequest creditRequest = new CustomerLedgerRequest();
creditRequest.setCustomerId(customerId);
creditRequest.setTransactionType("PAYMENT");
creditRequest.setEntryType("CREDIT");
creditRequest.setCreditAmount(15000.00);
creditRequest.setPaymentId(paymentId);

customerService.createLedgerEntry(creditRequest);
// Auto-updates: customer outstanding, available credit, credit status

// Get current balance
Double balance = customerService.getCustomerBalance(customerId);
// Returns: 10,000.00 (25,000 debit - 15,000 credit)
```

### Example 4: Ledger Statement

```java
// Get customer ledger statement
LocalDate startDate = LocalDate.of(2025, 1, 1);
LocalDate endDate = LocalDate.of(2025, 1, 31);

List<CustomerLedger> statement = customerService.getCustomerLedgerStatement(
    customerId, 
    startDate, 
    endDate
);

// Get opening balance
Double openingBalance = customerService.getCustomerOpeningBalance(
    customerId, 
    startDate
);

// Returns all transactions in date range chronologically:
// - Transaction date, type, description
// - Debit amount, credit amount
// - Running balance calculation
```

### Example 5: Customer Queries

```java
// Get customers with credit exceeded
List<Customer> exceeded = customerService.getCustomersWithCreditExceeded();
// Returns: customers where currentOutstanding > creditLimit

// Get customers with credit warning
List<Customer> warning = customerService.getCustomersWithCreditWarning();
// Returns: customers where outstanding >= 80% of limit but <= limit

// Get customers with overdue balance
List<Customer> overdue = customerService.getCustomersWithOverdueBalance();
// Returns: customers with overdueAmount > 0

// Get customers without recent orders
List<Customer> inactive = customerService.getCustomersWithoutRecentOrders(90);
// Returns: customers with lastOrderDate NULL or > 90 days ago

// Get customers requiring action
List<Customer> action = customerService.getCustomersRequiringAction();
// Returns customers that:
// - Are unverified and pending
// - Have credit exceeded
// - Have overdue balance
```

### Example 6: Credit Status Auto-Update

```java
// Credit status is auto-calculated based on outstanding vs limit:

// Customer with limit 100,000:
// - Outstanding 120,000 → creditStatus = "EXCEEDED"
// - Outstanding 85,000 → creditStatus = "WARNING" (>= 80%)
// - Outstanding 50,000 → creditStatus = "UTILIZED"
// - Outstanding 0 → creditStatus = "GOOD"

// Auto-updated whenever:
// - Ledger entry created/reversed
// - Credit limit updated
// - updateCustomerOutstanding() called
```

### Example 7: Customer Statistics

```java
@GetMapping("/statistics/dashboard")
public ResponseEntity<?> getDashboard() {
    Map<String, Object> dashboard = customerService.getDashboardStatistics();
    
    // Returns:
    // {
    //   "statistics": {
    //     "totalCustomers": 500,
    //     "activeCustomers": 450,
    //     "customersWithCredit": 100,
    //     "creditExceeded": 5,
    //     "outstandingCustomers": 150,
    //     "unverifiedCustomers": 20,
    //     "totalSalesAmount": 5000000.00,
    //     "totalOutstanding": 750000.00,
    //     "totalOverdue": 100000.00,
    //     "creditUtilization": 75.5 // percentage
    //   },
    //   "typeDistribution": [...],
    //   "statusDistribution": [...],
    //   "creditStatusDistribution": [...]
    // }
    
    return ResponseEntity.ok(dashboard);
}
```

### Example 8: Ledger Entry Reversal

```java
// Reverse a ledger entry
CustomerLedger reversed = customerService.reverseLedgerEntry(
    ledgerEntryId, 
    "Invoice cancelled"
);

// Sets:
// - isReversed = true
// - reversalReason = "Invoice cancelled"
// - reversedDate = today
// Updates: customer outstanding (excludes reversed entries)

// Note: Reversed entries are excluded from balance calculations
// Balance = SUM(debit - credit) WHERE isReversed = false
```

---

## 📋 Customer Types

1. **RETAIL** - Retail customer
2. **WHOLESALE** - Wholesale customer
3. **DISTRIBUTOR** - Distributor
4. **EXPORT** - Export customer
5. **CORPORATE** - Corporate customer
6. **GOVERNMENT** - Government entity

## 📋 Customer Status

1. **PENDING** - Pending verification
2. **ACTIVE** - Active customer
3. **INACTIVE** - Inactive customer
4. **SUSPENDED** - Suspended (with reason)

## 📋 Credit Status

1. **GOOD** - No outstanding or within limit
2. **UTILIZED** - Has outstanding within limit
3. **WARNING** - Outstanding >= 80% of limit
4. **EXCEEDED** - Outstanding > credit limit

## 📋 Payment Terms

1. **CASH** - Cash payment
2. **NET_7** - Net 7 days
3. **NET_15** - Net 15 days
4. **NET_30** - Net 30 days
5. **NET_60** - Net 60 days
6. **NET_90** - Net 90 days

## 📋 Transaction Types

1. **INVOICE** - Sales invoice
2. **PAYMENT** - Payment received
3. **CREDIT_NOTE** - Credit note
4. **DEBIT_NOTE** - Debit note
5. **ADJUSTMENT** - Balance adjustment
6. **OPENING_BALANCE** - Opening balance

## 📋 Entry Types

1. **DEBIT** - Debit entry (increases balance)
2. **CREDIT** - Credit entry (decreases balance)

---

## 🔒 Security

**Role-based Access Control:**

- **ADMIN** - Full access to all customer operations
- **MANAGER** - Manage customers, credit facilities, view all data
- **ACCOUNTANT** - Manage ledger entries, view financial data
- **SALES_REP** - Create/manage own customers, view limited data
- **USER** - View customer information only

**Endpoint Security:**
- All endpoints require authentication
- `@PreAuthorize` annotations on all endpoints
- Role-based authorization

---

## 📁 Directory Structure

```
customer/
├── repository/
│   ├── CustomerRepository.java (140+ methods, 520 lines)
│   ├── CustomerLedgerRepository.java (110+ methods, 450 lines)
│   └── README.md
├── service/
│   ├── CustomerService.java (80+ methods, 220 lines)
│   ├── CustomerServiceImpl.java (750+ lines)
│   └── README.md
└── controller/
    ├── CustomerController.java (45+ endpoints, 350 lines)
    ├── CustomerLedgerController.java (7+ endpoints, 100 lines)
    └── README.md
```

---

## ✅ Summary

✅ **2 Repositories** - 250+ query methods  
✅ **2 Service files** - 80+ business logic methods  
✅ **2 Controllers** - 52+ REST endpoints  
✅ **Customer management** - Complete customer lifecycle  
✅ **Credit management** - Credit facility with auto-status updates  
✅ **Ledger management** - Complete double-entry accounting  
✅ **Customer types** - 6 customer types  
✅ **Customer status** - 4 status values  
✅ **Credit status** - 4 credit status values  
✅ **Payment terms** - 6 payment term options  
✅ **Transaction types** - 6 transaction types  
✅ **Entry types** - Debit and credit  
✅ **Status transitions** - Complete lifecycle management  
✅ **Verification workflow** - Verify with user tracking  
✅ **Blacklist management** - Blacklist/remove with reasons  
✅ **Credit facility** - Enable/update/disable with validation  
✅ **Credit auto-status** - Auto-calculate based on outstanding  
✅ **Outstanding tracking** - Auto-update from ledger balance  
✅ **Available credit** - Auto-calculate (limit - outstanding)  
✅ **Ledger entries** - Create/reverse/reconcile with tracking  
✅ **Balance calculation** - Real-time from ledger (debit - credit)  
✅ **Statement generation** - Ledger statement with date range  
✅ **Opening balance** - Calculate balance before start date  
✅ **Reversal support** - Reverse entries with reason tracking  
✅ **Reconciliation** - Mark entries as reconciled  
✅ **User tracking** - Created by, verified by with user ID tracking  
✅ **Date tracking** - Registration, last order, verified, suspended, blacklisted dates  
✅ **Financial tracking** - Total sales, outstanding, overdue with auto-calculations  
✅ **Sales metrics** - Total order count, average order value  
✅ **Route management** - Organize customers by delivery route  
✅ **Sales rep assignment** - Assign customers to sales representatives  
✅ **Action tracking** - Customers requiring action (verification, credit exceeded, overdue)  
✅ **Search** - Full-text search across all fields  
✅ **Statistics** - Comprehensive analytics with distributions  
✅ **Monthly trends** - Monthly registration with year/month grouping  
✅ **Sales rep analysis** - Customers and totals by sales rep  
✅ **Credit metrics** - Credit utilization rate calculation  
✅ **Batch operations** - Bulk create/activate/deactivate/delete with error handling  
✅ **Validation** - Complete validation, eligibility checks, unique field validation  
✅ **Auto-generation** - Auto-generate customer code, transaction reference  
✅ **Role-based security** - 5 roles with appropriate permissions  
✅ **Production-ready** - Enterprise-grade implementation  

**Everything you need for complete customer and ledger management with customer types (6 types: RETAIL, WHOLESALE, DISTRIBUTOR, EXPORT, CORPORATE, GOVERNMENT), customer status (4 values: PENDING, ACTIVE, INACTIVE, SUSPENDED with auto-status=PENDING on creation), credit status (4 values: GOOD, UTILIZED, WARNING, EXCEEDED with auto-calculation based on outstanding vs limit), payment terms (6 options: CASH, NET_7, NET_15, NET_30, NET_60, NET_90), auto-generated codes (customer code CUS-timestamp, transaction reference TXN-timestamp), verification workflow (verify sets isVerified=true/status=ACTIVE/verifiedDate/verifiedByUserId), blacklist management (blacklist sets isBlacklisted=true/status=SUSPENDED/blacklistReason/blacklistedDate, remove clears blacklist fields and sets ACTIVE), credit facility management (enable sets hasCreditFacility=true/creditLimit/creditStatus=GOOD and calculates available credit, update limit recalculates available credit and updates status, disable requires zero outstanding and clears all credit fields with CASH terms), credit auto-status updates (EXCEEDED when outstanding>limit, WARNING when outstanding>=80%*limit, UTILIZED when outstanding>0, GOOD when outstanding=0, auto-updated on ledger create/reverse and credit limit update), outstanding management (calculate from ledger balance debit-credit where isReversed=false, auto-update customer outstanding field, calculate available credit as limit-outstanding, auto-update credit status), ledger entry creation (create with transaction reference TXN-timestamp, set defaults isReconciled=false/isReversed=false, support transaction types: INVOICE/PAYMENT/CREDIT_NOTE/DEBIT_NOTE/ADJUSTMENT/OPENING_BALANCE, support entry types: DEBIT/CREDIT, link to invoice/payment/sales order, update customer outstanding after creation), ledger reversal (set isReversed=true/reversalReason/reversedDate, update customer outstanding excluding reversed entries), ledger reconciliation (set isReconciled=true/reconciledDate), balance calculations (customer balance = SUM(debit-credit) WHERE isReversed=false, opening balance = balance before start date, ledger statement = all entries in date range chronologically), customer metrics (total sales amount, total order count, last order date, average order value with auto-calculations), comprehensive user tracking (createdByUserId, verifiedByUserId with date tracking), extensive date tracking (registrationDate, lastOrderDate, verifiedDate, suspendedDate, blacklistedDate, transactionDate, dueDate, reversedDate, reconciledDate), financial tracking (totalSalesAmount, currentOutstanding, overdueAmount, creditLimit, availableCredit with auto-calculations and ledger integration), route management (organize customers by delivery route with active customer queries by route), sales rep assignment (assign and track customers by sales rep with totals analysis), action tracking (customers requiring action: unverified+pending OR credit exceeded OR overdue balance), search capabilities (full-text search on code/name/business/email/phone/transaction reference/description), comprehensive statistics (distributions by type/status/credit/payment terms/route/city/province, monthly registration with year/month grouping, sales rep analysis with customer count and total sales, credit utilization rate calculation, total sales/outstanding/overdue/credit limit/available credit, average order value, ledger activity by month with entry count/total debit/total credit), batch operations (bulk create/activate/deactivate/delete with error handling), validation (customer validation checks name/email/phone required, unique field validation for email/phone/code on create and update, eligibility checks for activate/suspend/enable credit), and secure role-based access control (5 roles: ADMIN, MANAGER, ACCOUNTANT, SALES_REP, USER with @PreAuthorize annotations) in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.customer
