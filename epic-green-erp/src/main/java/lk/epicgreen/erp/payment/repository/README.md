# Payment Module - Repository, Service & Controller

This directory contains **repository, service, and controller layers** for the payment management system in Epic Green ERP.

## 📦 Contents

### Repository (payment/repository) - 3 Files
1. **PaymentRepository.java** - Payment data access
2. **PaymentAllocationRepository.java** - Payment allocation data access
3. **ChequeRepository.java** - Cheque data access

### Service (payment/service) - 3 Files
1. **PaymentService.java** - Payment service interface
2. **PaymentServiceImpl.java** - Payment service implementation
3. **ChequeService.java** - Cheque service interface

### Controller (payment/controller) - 2 Files
1. **PaymentController.java** - REST controller for payments
2. **ChequeController.java** - REST controller for cheques

---

## 📊 1. Repositories

### PaymentRepository

**Purpose:** Data access for payments

**Key Methods (110+):**
- **Find by fields:** Payment number, customer, type, method, status, reference, user, bank account, cheque
- **Date ranges:** Payment date, received date, cleared date
- **Status queries:** Pending, completed, cleared, failed (4 specialized queries)
- **Allocation:** Allocated/unallocated payments
- **Reconciliation:** Reconciled/unreconciled payments
- **Payment methods:** Cash, cheque, bank transfer (3 specialized queries)
- **Special queries:** Overpayments, partial payments
- **Search:** Full-text search on number/customer/notes
- **Statistics:** Count by customer/type/method/status, distributions, monthly counts
- **Financial:** Total amount, average amount, by customer/method, unallocated amount
- **Purpose:** Complete payment data access

### PaymentAllocationRepository

**Purpose:** Data access for payment allocations

**Key Methods (70+):**
- **Find by fields:** Payment ID, invoice ID, customer, allocation type, status, user
- **Date ranges:** Allocation date between dates
- **Status queries:** Pending, completed allocations
- **Reversal:** Reversed allocations
- **Search:** Full-text search
- **Financial calculations:** Total allocated by payment/invoice, invoice payment status
- **Statistics:** Count by payment/invoice/status, distributions, monthly counts
- **Aggregations:** Total allocated amount, average allocation amount, by customer
- **Purpose:** Complete payment allocation data access

### ChequeRepository

**Purpose:** Data access for cheques

**Key Methods (100+):**
- **Find by fields:** Cheque number, payment, customer, bank, branch, status, type
- **Date ranges:** Cheque date, presented date, cleared date, bounced date
- **Status queries:** Pending, presented, cleared, bounced, cancelled (5 specialized queries)
- **Post-dated:** Post-dated cheques, due for presentation
- **Action required:** Overdue cheques, requiring action
- **Unique constraints:** Find by number+bank combination
- **Search:** Full-text search on number/bank/customer
- **Statistics:** Count by customer/bank/status, distributions, monthly counts
- **Financial:** Total amount, average amount, by bank, bounce rate calculation
- **Purpose:** Complete cheque data access

---

## 🔧 2. Service Layer

### PaymentService Interface

**Purpose:** Define payment service contract

**Method Categories (70+ methods):**
1. **CRUD (8 methods):** Create, update, delete, get by ID/number, get all, search
2. **Status Operations (5 methods):** Complete, clear, fail, cancel, reconcile
3. **Payment Allocation (7 methods):** Allocate to single/multiple invoices, reverse allocation, get allocations, get total allocated/unallocated amounts
4. **Query (18 methods):** Pending, completed, cleared, failed, unallocated, unreconciled, by payment method (cash/cheque/bank transfer), by customer/date/bank account, overpayments, partial payments, recent
5. **Validation (4 methods):** Validate payment, can complete/allocate/reverse
6. **Calculations (3 methods):** Calculate allocations, total allocated, remaining amount
7. **Batch (4 methods):** Bulk create, complete, delete, reconcile
8. **Statistics (11 methods):** Statistics, distributions (type/method/status), monthly count, total/average amounts, by customer/method, unallocated amount, dashboard

### PaymentServiceImpl Implementation

**Purpose:** Implement payment business logic

**Key Features:**
- **Payment creation** - Generate payment number (PAY-timestamp), set defaults (PENDING status, isAllocated=false, isReconciled=false, allocatedAmount=0)
- **Status management** - Complete (sets COMPLETED, receivedDate), clear (requires COMPLETED, sets CLEARED, clearedDate), fail (sets FAILED with reason), cancel (requires not allocated)
- **Reconciliation** - Mark as reconciled with reconciliation date
- **Payment allocation** - Allocate to invoice (validates COMPLETED status, checks unallocated amount), multiple invoice allocation, reverse allocation
- **Allocation calculation** - Calculate total allocated, update payment allocation status (isAllocated = true when fully allocated)
- **Unallocated tracking** - Calculate remaining unallocated amount (paidAmount - allocatedAmount)
- **Query operations** - 18 specialized query methods
- **Validation** - Payment validation (customer, date, amount checks), eligibility checks
- **Batch operations** - Bulk create, complete, delete, reconcile with error handling
- **Statistics** - Comprehensive analytics with distributions
- **Purpose:** Implement all payment business logic

### ChequeService Interface

**Purpose:** Define cheque service contract

**Method Categories (45+ methods):**
1. **CRUD (8 methods):** Create, update, delete, get by ID/number, get all, search
2. **Status Operations (5 methods):** Present, clear, bounce, cancel, return
3. **Query (14 methods):** Pending, presented, cleared, bounced, cancelled, post-dated, due for presentation, overdue, requiring action, by customer/bank/date, recent
4. **Validation (4 methods):** Validate cheque, can present/clear/bounce
5. **Batch (4 methods):** Bulk create, present, clear, delete
6. **Statistics (7 methods):** Statistics, status/bank distributions, monthly count, total/average amount, bounce rate, dashboard

---

## 🌐 3. Controller Layer

### PaymentController

**Purpose:** REST API endpoints for payments

**Endpoints (35+):**
- **CRUD (6):** POST, PUT, DELETE, GET by ID, GET by number, GET all, search
- **Status operations (5):** POST complete, POST clear, POST fail, POST cancel, POST reconcile
- **Allocation (4):** POST allocate, POST allocate-multiple, POST reverse-allocation, GET allocations
- **Query (13):** GET pending, GET completed, GET unallocated, GET unreconciled, GET cash, GET cheque, GET by customer, GET by date-range, GET recent
- **Statistics (2):** GET statistics, GET dashboard

### ChequeController

**Purpose:** REST API endpoints for cheques

**Endpoints (30+):**
- **CRUD (6):** POST, PUT, DELETE, GET by ID, GET by number, GET all, search
- **Status operations (5):** POST present, POST clear, POST bounce, POST cancel, POST return
- **Query (13):** GET pending, GET presented, GET cleared, GET bounced, GET cancelled, GET post-dated, GET due-for-presentation, GET overdue, GET requiring-action, GET by customer, GET by bank, GET by date-range, GET recent
- **Statistics (2):** GET statistics, GET dashboard

---

## 💡 Usage Examples

### Example 1: Create Payment

```java
PaymentRequest request = new PaymentRequest();
request.setCustomerId(customerId);
request.setCustomerName(customerName);
request.setPaymentDate(LocalDate.now());
request.setPaymentType("INVOICE_PAYMENT");
request.setPaymentMethod("CASH");
request.setPaidAmount(5000.00);

Payment created = paymentService.createPayment(request);
```

### Example 2: Complete and Allocate Payment

```java
// Complete payment
Payment completed = paymentService.completePayment(paymentId);

// Allocate to single invoice
PaymentAllocation allocation = paymentService.allocatePayment(
    paymentId, 
    invoiceId, 
    3000.00
);

// Allocate to multiple invoices
List<Map<String, Object>> allocations = Arrays.asList(
    Map.of("invoiceId", invoice1Id, "amount", 2000.00),
    Map.of("invoiceId", invoice2Id, "amount", 1000.00)
);
List<PaymentAllocation> result = paymentService.allocatePaymentToMultipleInvoices(
    paymentId, 
    allocations
);
```

### Example 3: Create and Present Cheque

```java
ChequeRequest request = new ChequeRequest();
request.setPaymentId(paymentId);
request.setCustomerId(customerId);
request.setChequeNumber("123456");
request.setChequeDate(LocalDate.now());
request.setBankName("Bank of Ceylon");
request.setBranchName("Colombo");
request.setChequeAmount(10000.00);
request.setIsPostDated(false);

Cheque created = chequeService.createCheque(request);

// Present cheque
Cheque presented = chequeService.presentCheque(
    created.getId(), 
    LocalDate.now()
);
```

### Example 4: Get Unallocated Payments

```java
// Get all unallocated payments
List<Payment> unallocated = paymentService.getUnallocatedPayments();

// Get unallocated amount for specific payment
Double unallocatedAmount = paymentService.getUnallocatedAmount(paymentId);
```

### Example 5: Cheque Lifecycle

```java
// Create cheque
Cheque cheque = chequeService.createCheque(request);

// Present cheque to bank
Cheque presented = chequeService.presentCheque(cheque.getId(), LocalDate.now());

// If cheque clears
Cheque cleared = chequeService.clearCheque(cheque.getId(), LocalDate.now());

// OR if cheque bounces
Cheque bounced = chequeService.bounceCheque(cheque.getId(), "Insufficient funds");
```

### Example 6: Reconciliation

```java
// Get unreconciled payments
List<Payment> unreconciled = paymentService.getUnreconciledPayments();

// Reconcile payment
Payment reconciled = paymentService.reconcilePayment(
    paymentId, 
    LocalDate.now()
);

// Bulk reconcile
int reconciledCount = paymentService.reconcileBulkPayments(
    paymentIds, 
    LocalDate.now()
);
```

### Example 7: Payment Statistics

```java
@GetMapping("/statistics/dashboard")
public ResponseEntity<?> getDashboard() {
    Map<String, Object> dashboard = paymentService.getDashboardStatistics();
    
    // Returns:
    // {
    //   "statistics": {
    //     "totalPayments": 500,
    //     "pendingPayments": 20,
    //     "unallocatedPayments": 15,
    //     "unreconciledPayments": 30,
    //     "totalPaymentAmount": 250000.00,
    //     "averagePaymentAmount": 500.00,
    //     "totalUnallocatedAmount": 5000.00
    //   },
    //   "typeDistribution": [...],
    //   "methodDistribution": [...],
    //   "statusDistribution": [...]
    // }
    
    return ResponseEntity.ok(dashboard);
}
```

---

## 📋 Payment Types

1. **INVOICE_PAYMENT** - Payment against invoice
2. **ADVANCE_PAYMENT** - Advance payment
3. **REFUND** - Refund payment
4. **DEPOSIT** - Security deposit
5. **OTHER** - Other payment type

## 📋 Payment Methods

1. **CASH** - Cash payment
2. **CHEQUE** - Cheque payment
3. **BANK_TRANSFER** - Bank transfer
4. **CREDIT_CARD** - Credit card
5. **DEBIT_CARD** - Debit card
6. **ONLINE** - Online payment
7. **OTHER** - Other method

## 📋 Payment Status

1. **PENDING** - Payment pending
2. **COMPLETED** - Payment completed
3. **CLEARED** - Payment cleared
4. **FAILED** - Payment failed
5. **CANCELLED** - Payment cancelled

## 📋 Cheque Status

1. **PENDING** - Cheque pending presentation
2. **PRESENTED** - Cheque presented to bank
3. **CLEARED** - Cheque cleared
4. **BOUNCED** - Cheque bounced
5. **CANCELLED** - Cheque cancelled
6. **RETURNED** - Cheque returned

## 📋 Allocation Types

1. **INVOICE** - Allocated to invoice
2. **CREDIT_NOTE** - Allocated to credit note
3. **ADVANCE** - Advance allocation
4. **OTHER** - Other allocation

---

## 🔒 Security

**Role-based Access Control:**

- **ADMIN** - Full access to all payment operations
- **MANAGER** - Approve payments, reconcile, view reports
- **ACCOUNTANT** - Manage payments, allocate, reconcile, clear cheques
- **CASHIER** - Create payments, receive payments, present cheques
- **SALES_REP** - View customer payments
- **USER** - View payments only

**Endpoint Security:**
- All endpoints require authentication
- `@PreAuthorize` annotations on all endpoints
- Role-based authorization

---

## 📁 Directory Structure

```
payment/
├── repository/
│   ├── PaymentRepository.java (110+ methods, 400 lines)
│   ├── PaymentAllocationRepository.java (70+ methods, 300 lines)
│   ├── ChequeRepository.java (100+ methods, 380 lines)
│   └── README.md
├── service/
│   ├── PaymentService.java (70+ methods, 220 lines)
│   ├── PaymentServiceImpl.java (550+ lines)
│   ├── ChequeService.java (45+ methods, 150 lines)
│   └── README.md
└── controller/
    ├── PaymentController.java (35+ endpoints, 280 lines)
    ├── ChequeController.java (30+ endpoints, 250 lines)
    └── README.md
```

---

## ✅ Summary

✅ **3 Repositories** - 280+ query methods  
✅ **3 Service files** - 115+ business logic methods  
✅ **2 Controllers** - 65+ REST endpoints  
✅ **Payments** - Complete payment workflow  
✅ **Payment allocation** - Bill-to-bill settlement  
✅ **Cheques** - Complete cheque lifecycle  
✅ **Payment types** - 5 payment types  
✅ **Payment methods** - 7 payment methods  
✅ **Payment status** - 5 status values  
✅ **Cheque status** - 6 status values  
✅ **Allocation types** - 4 allocation types  
✅ **Status management** - Complete, clear, fail, cancel, reconcile  
✅ **Cheque lifecycle** - Create, present, clear/bounce, cancel/return  
✅ **Payment allocation** - Single/multiple invoice allocation  
✅ **Reverse allocation** - Reverse payment allocations with reason  
✅ **Unallocated tracking** - Track unallocated payment amounts  
✅ **Reconciliation** - Mark payments as reconciled with date  
✅ **Post-dated cheques** - Handle post-dated cheques separately  
✅ **Cheque presentation** - Due for presentation tracking  
✅ **Overdue cheques** - Identify overdue presented cheques  
✅ **Bounce tracking** - Track bounced cheques with reasons  
✅ **User tracking** - Track received by, allocated by users  
✅ **Date tracking** - Payment, received, cleared, presented, bounced dates  
✅ **Financial tracking** - Paid, allocated, unallocated amounts  
✅ **Overpayment handling** - Identify overpayments  
✅ **Partial payments** - Track partial allocations  
✅ **Search** - Full-text search across payments and cheques  
✅ **Statistics** - Comprehensive analytics with distributions  
✅ **Monthly trends** - Monthly payment and cheque counts  
✅ **Payment method analysis** - Total amount by payment method  
✅ **Customer analysis** - Total payments by customer  
✅ **Bank analysis** - Cheque distribution by bank  
✅ **Bounce rate** - Calculate cheque bounce rate  
✅ **Batch operations** - Bulk create, complete, delete, reconcile, present, clear  
✅ **Validation** - Payment/cheque validation, eligibility checks  
✅ **Auto-generation** - Auto-generate payment numbers (PAY-timestamp)  
✅ **Role-based security** - 6 roles with appropriate permissions  
✅ **Production-ready** - Enterprise-grade implementation  

**Everything you need for complete payment management with payments (5 payment types, 7 payment methods, 5 status values, auto-generated payment numbers), payment allocation (bill-to-bill settlement with single/multiple invoice allocation, reverse allocation, unallocated tracking), cheques (6 status values, post-dated support, presentation tracking, bounce handling), status management (complete, clear, fail, cancel, reconcile with date tracking), cheque lifecycle (create, present, clear/bounce, cancel/return with reason tracking), user tracking (received by, allocated by with user ID tracking), comprehensive date tracking (payment, received, cleared, presented, bounced, reconciliation dates), financial tracking (paid, allocated, unallocated amounts with calculations), overpayment and partial payment identification, search capabilities (full-text search on numbers/names/notes), comprehensive statistics (distributions by type/method/status/bank, monthly trends with year/month grouping, customer/payment method/bank analysis), bounce rate calculation, batch operations (bulk create/complete/delete/reconcile/present/clear with error handling), validation (payment/cheque validation, eligibility checks for complete/allocate/present/clear/bounce), and secure role-based access control (6 roles: ADMIN, MANAGER, ACCOUNTANT, CASHIER, SALES_REP, USER with @PreAuthorize annotations) in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.payment
