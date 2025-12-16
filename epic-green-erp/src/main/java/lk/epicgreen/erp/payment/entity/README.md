# Payment Module - Epic Green ERP

This directory contains **entities and DTOs** for payment processing and cheque management in the Epic Green ERP system.

## 📦 Contents

### Entities (payment/entity) - 3 Files
1. **Payment.java** - Customer payment records
2. **PaymentAllocation.java** - Payment allocation to invoices
3. **Cheque.java** - Cheque payment tracking

### DTOs (payment/dto) - 4 Files
1. **PaymentDTO.java** - Payment data transfer object
2. **CreatePaymentRequest.java** - Create payment request
3. **PaymentAllocationDTO.java** - Payment allocation DTO
4. **ChequeDTO.java** - Cheque DTO

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────────┐
│  Customer    │
└──────┬───────┘
       │ N:1
       ▼
┌─────────────────┐      ┌─────────────┐
│    Payment      │ ◄N:1─┤   Cheque    │
└────────┬────────┘      └─────────────┘
         │ 1:N
         ▼
┌─────────────────────┐
│ PaymentAllocation   │
└──────────┬──────────┘
           │ N:1
           ▼
    ┌─────────────┐
    │   Invoice   │
    └─────────────┘
```

### Payment Process Flow

```
1. RECEIVE PAYMENT
   ↓
   Cash/Cheque/Bank Transfer
   ↓
   
2. RECORD PAYMENT
   ↓ (Create payment record)
   DRAFT → PENDING → CLEARED
   ↓
   
3. ALLOCATE TO INVOICES
   ↓ (Create payment allocations)
   Update invoice paid amounts
   Update invoice payment status
   ↓
   
4. POST TO LEDGER
   ↓ (Post to customer ledger)
   Create CREDIT entry
   Update customer outstanding balance
   ↓
   
5. CHEQUE HANDLING (if applicable)
   RECEIVED → DEPOSITED → CLEARED/BOUNCED
```

---

## 📋 1. Payment Entity

**Purpose:** Customer payment records

### Key Fields

```java
// Identification
- paymentNumber (unique, e.g., "PAY-2024-001")
- paymentDate
- customer (FK)

// Payment details
- paymentMethod (CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, DEBIT_CARD, MOBILE_PAYMENT)
- paymentAmount
- allocatedAmount (sum of allocations, auto-calculated)
- unallocatedAmount (payment amount - allocated, auto-calculated)

// Currency
- currency
- exchangeRate (for foreign currency)

// Bank details (for bank transfers, cheques)
- referenceNumber (transaction reference, receipt number)
- bankName
- bankAccount (customer's bank account)
- bankBranch

// Cheque reference
- cheque (FK for cheque payments)

// Status
- status (DRAFT, PENDING, CLEARED, BOUNCED, CANCELLED)
- clearedDate

// Posting to ledger
- isPosted (posted to customer ledger)
- postedDate
- postedBy

// Receipt details
- receivedBy (person who received payment)
- receiptNumber

// Description and notes
- description
- notes

// Relationships
- allocations (Set<PaymentAllocation>)
```

### Helper Methods

```java
void calculateAllocations(); // Calculate allocated and unallocated amounts
boolean isFullyAllocated(); // allocated >= payment amount
boolean hasUnallocatedAmount(); // unallocated > 0
boolean isCleared(); // status == CLEARED
boolean isBounced(); // status == BOUNCED
boolean canPost(); // CLEARED && !isPosted
boolean canEdit(); // DRAFT or PENDING
boolean isChequePayment(); // paymentMethod == CHEQUE
```

### Status Flow

```
DRAFT (can edit)
  ↓ (submit)
PENDING (awaiting confirmation)
  ↓ (confirm/clear)
CLEARED (payment confirmed)
  ↓ (post to ledger)
Posted to Customer Ledger
```

For cheque payments:
```
PENDING (cheque received, awaiting deposit)
  ↓ (deposit to bank)
CLEARED (cheque cleared)
  OR
BOUNCED (cheque bounced)
```

---

## 🔧 2. PaymentAllocation Entity

**Purpose:** Allocation of payments to specific invoices

### Key Fields

```java
// References
- payment (FK)
- invoice (FK)

// Invoice tracking
- invoiceNumber (for reference)
- invoiceBalanceBefore (invoice balance before this allocation)
- allocatedAmount (amount allocated from payment)
- invoiceBalanceAfter (invoice balance after this allocation, auto-calculated)

// Currency
- currency
- exchangeRate (if different from payment)

- notes
```

### Helper Methods

```java
void calculateBalanceAfter(); // invoiceBalanceBefore - allocatedAmount
boolean isInvoiceFullyPaid(); // invoiceBalanceAfter == 0
BigDecimal getAllocationPercentage(); // (allocated / invoiceBalanceBefore) * 100
```

### Key Features

- **Multiple allocations:** One payment can be allocated to multiple invoices
- **Partial allocation:** Allocate portion of payment to invoice
- **Over-payment handling:** Can allocate more than invoice balance (creates credit)
- **Balance tracking:** Track invoice balance before and after allocation
- **Auto-update:** Automatically updates invoice paid amount and payment status

---

## 💳 3. Cheque Entity

**Purpose:** Cheque payment tracking

### Key Fields

```java
// Identification
- chequeNumber
- chequeDate
- customer (FK payer)

// Bank details (customer's bank)
- bankName
- bankBranch
- bankAccount

// Amount
- chequeAmount
- currency
- exchangeRate

// Payee
- payeeName (usually company name)
- chequeType (ACCOUNT_PAYEE, BEARER, ORDER)

// Status
- status (RECEIVED, DEPOSITED, CLEARED, BOUNCED, CANCELLED, RETURNED)

// Receipt
- receivedDate
- receivedBy

// Deposit (our bank)
- depositDate
- depositedBy
- depositBank (our bank)
- depositAccount (our account)
- depositReference

// Clearing
- clearedDate

// Bounce handling
- bouncedDate
- bounceReason
- bankCharges (for bounced cheques)

// Post-dated cheque
- isPostDated
- maturityDate (for post-dated cheques)

- notes
```

### Helper Methods

```java
boolean isCleared();
boolean isBounced();
boolean isDeposited();
boolean canDeposit(); // RECEIVED && (not post-dated OR matured)
boolean isPendingMaturity(); // post-dated && not yet mature
Long getDaysToMaturity(); // days until maturity date
Long getDaysSinceDeposit(); // days since deposit date
```

### Status Flow

```
RECEIVED (cheque received from customer)
  ↓ (if post-dated, wait for maturity)
  ↓ (deposit to bank)
DEPOSITED (deposited to our bank)
  ↓ (wait for clearing, usually 3-7 days)
CLEARED (cheque cleared successfully)
  OR
BOUNCED (cheque bounced - insufficient funds, etc.)
```

### Post-Dated Cheque Handling

```
Cheque received with future date
  ↓
Auto-detect: chequeDate > today
  ↓
Set: isPostDated = true, maturityDate = chequeDate
  ↓
Status: RECEIVED
  ↓
Wait until maturityDate
  ↓
canDeposit() returns true
  ↓
Deposit to bank
```

---

## 💡 Usage Examples

### Example 1: Create Cash Payment

```java
@Transactional
public PaymentDTO createCashPayment(CreatePaymentRequest request) {
    // Get customer
    Customer customer = customerRepository.findById(request.getCustomerId())
        .orElseThrow(() -> new NotFoundException("Customer not found"));
    
    // Create payment
    Payment payment = Payment.builder()
        .paymentNumber(generatePaymentNumber())
        .paymentDate(request.getPaymentDate())
        .customer(customer)
        .paymentMethod("CASH")
        .paymentAmount(request.getPaymentAmount())
        .currency(request.getCurrency())
        .exchangeRate(request.getExchangeRate())
        .referenceNumber(request.getReferenceNumber())
        .receivedBy(request.getReceivedBy())
        .receiptNumber(request.getReceiptNumber())
        .description(request.getDescription())
        .notes(request.getNotes())
        .status("CLEARED") // Cash is immediately cleared
        .clearedDate(LocalDate.now())
        .build();
    
    payment = paymentRepository.save(payment);
    return mapper.toDTO(payment);
}
```

### Example 2: Create Cheque Payment

```java
@Transactional
public PaymentDTO createChequePayment(CreatePaymentRequest request) {
    // Get or create cheque
    Cheque cheque;
    if (request.getChequeId() != null) {
        cheque = chequeRepository.findById(request.getChequeId())
            .orElseThrow(() -> new NotFoundException("Cheque not found"));
    } else {
        // Create cheque from request
        cheque = createChequeFromRequest(request);
        cheque = chequeRepository.save(cheque);
    }
    
    // Create payment
    Payment payment = Payment.builder()
        .paymentNumber(generatePaymentNumber())
        .paymentDate(request.getPaymentDate())
        .customer(cheque.getCustomer())
        .paymentMethod("CHEQUE")
        .paymentAmount(cheque.getChequeAmount())
        .currency(cheque.getCurrency())
        .exchangeRate(cheque.getExchangeRate())
        .bankName(cheque.getBankName())
        .bankAccount(cheque.getBankAccount())
        .bankBranch(cheque.getBankBranch())
        .cheque(cheque)
        .receivedBy(request.getReceivedBy())
        .status("PENDING") // Cheque needs to be deposited and cleared
        .build();
    
    payment = paymentRepository.save(payment);
    return mapper.toDTO(payment);
}
```

### Example 3: Allocate Payment to Invoices

```java
@Transactional
public void allocatePayment(Long paymentId, List<CreatePaymentAllocationRequest> allocations) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("Payment not found"));
    
    if (!payment.isCleared()) {
        throw new BusinessException("Payment must be cleared before allocation");
    }
    
    BigDecimal totalAllocated = BigDecimal.ZERO;
    
    for (var allocationReq : allocations) {
        Invoice invoice = invoiceRepository.findById(allocationReq.getInvoiceId())
            .orElseThrow(() -> new NotFoundException("Invoice not found"));
        
        // Verify invoice belongs to same customer
        if (!invoice.getCustomer().getId().equals(payment.getCustomer().getId())) {
            throw new BusinessException("Invoice does not belong to payment customer");
        }
        
        // Get current invoice balance
        BigDecimal invoiceBalance = invoice.getBalanceAmount();
        BigDecimal allocAmount = allocationReq.getAllocatedAmount();
        
        // Validate allocation amount
        if (allocAmount.compareTo(invoiceBalance) > 0) {
            throw new BusinessException(
                "Allocation amount (" + allocAmount + ") exceeds invoice balance (" + invoiceBalance + ")"
            );
        }
        
        // Check if total allocation exceeds payment amount
        totalAllocated = totalAllocated.add(allocAmount);
        if (totalAllocated.compareTo(payment.getPaymentAmount()) > 0) {
            throw new BusinessException("Total allocation exceeds payment amount");
        }
        
        // Create payment allocation
        PaymentAllocation allocation = PaymentAllocation.builder()
            .invoice(invoice)
            .invoiceNumber(invoice.getInvoiceNumber())
            .invoiceBalanceBefore(invoiceBalance)
            .allocatedAmount(allocAmount)
            .currency(payment.getCurrency())
            .exchangeRate(payment.getExchangeRate())
            .notes(allocationReq.getNotes())
            .build();
        
        allocation.calculateBalanceAfter();
        payment.addAllocation(allocation);
        
        // Update invoice paid amount
        invoice.setPaidAmount(invoice.getPaidAmount().add(allocAmount));
        invoice.calculateBalance();
        invoiceRepository.save(invoice);
    }
    
    // Update payment allocated amounts
    payment.calculateAllocations();
    paymentRepository.save(payment);
}
```

### Example 4: Post Payment to Customer Ledger

```java
@Transactional
public void postPaymentToLedger(Long paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("Payment not found"));
    
    if (!payment.canPost()) {
        throw new BusinessException("Payment must be cleared and not yet posted");
    }
    
    // Create ledger entry
    CustomerLedger ledger = CustomerLedger.builder()
        .customer(payment.getCustomer())
        .transactionDate(payment.getPaymentDate())
        .transactionType("PAYMENT")
        .referenceNumber(payment.getPaymentNumber())
        .referenceId(payment.getId())
        .referenceType("PAYMENT")
        .debitAmount(BigDecimal.ZERO)
        .creditAmount(payment.getPaymentAmount()) // Decreases receivable
        .paymentMethod(payment.getPaymentMethod())
        .description("Payment - " + payment.getPaymentNumber())
        .status("CLEARED")
        .build();
    
    // If cheque payment, add cheque details
    if (payment.isChequePayment() && payment.getCheque() != null) {
        Cheque cheque = payment.getCheque();
        ledger.setChequeNumber(cheque.getChequeNumber());
        ledger.setChequeDate(cheque.getChequeDate());
        ledger.setBankName(cheque.getBankName());
    }
    
    // Calculate running balance
    BigDecimal previousBalance = customerLedgerService.getCustomerBalance(payment.getCustomer().getId());
    ledger.setBalance(previousBalance.subtract(payment.getPaymentAmount()));
    
    customerLedgerRepository.save(ledger);
    
    // Update customer outstanding balance
    Customer customer = payment.getCustomer();
    customer.setOutstandingBalance(ledger.getBalance());
    customerRepository.save(customer);
    
    // Mark payment as posted
    payment.setIsPosted(true);
    payment.setPostedDate(LocalDate.now());
    payment.setPostedBy(getCurrentUsername());
    paymentRepository.save(payment);
}
```

### Example 5: Deposit Cheque to Bank

```java
@Transactional
public void depositCheque(Long chequeId, DepositChequeRequest request) {
    Cheque cheque = chequeRepository.findById(chequeId)
        .orElseThrow(() -> new NotFoundException("Cheque not found"));
    
    if (!cheque.canDeposit()) {
        if (cheque.isPendingMaturity()) {
            throw new BusinessException(
                "Cheque is post-dated. Can deposit after " + cheque.getMaturityDate()
            );
        }
        throw new BusinessException("Cheque cannot be deposited. Status: " + cheque.getStatus());
    }
    
    // Update cheque details
    cheque.setStatus("DEPOSITED");
    cheque.setDepositDate(LocalDate.now());
    cheque.setDepositedBy(getCurrentUsername());
    cheque.setDepositBank(request.getDepositBank());
    cheque.setDepositAccount(request.getDepositAccount());
    cheque.setDepositReference(request.getDepositReference());
    
    chequeRepository.save(cheque);
    
    // Find associated payment and update status
    Payment payment = paymentRepository.findByCheque(cheque)
        .orElse(null);
    
    if (payment != null && "PENDING".equals(payment.getStatus())) {
        payment.setStatus("PENDING"); // Still pending until cleared
        paymentRepository.save(payment);
    }
}
```

### Example 6: Clear Cheque

```java
@Transactional
public void clearCheque(Long chequeId) {
    Cheque cheque = chequeRepository.findById(chequeId)
        .orElseThrow(() -> new NotFoundException("Cheque not found"));
    
    if (!"DEPOSITED".equals(cheque.getStatus())) {
        throw new BusinessException("Cheque must be deposited before clearing");
    }
    
    // Update cheque status
    cheque.setStatus("CLEARED");
    cheque.setClearedDate(LocalDate.now());
    chequeRepository.save(cheque);
    
    // Update payment status
    Payment payment = paymentRepository.findByCheque(cheque)
        .orElseThrow(() -> new NotFoundException("Payment not found for cheque"));
    
    payment.setStatus("CLEARED");
    payment.setClearedDate(LocalDate.now());
    paymentRepository.save(payment);
    
    // Auto-post to ledger
    if (payment.canPost()) {
        postPaymentToLedger(payment.getId());
    }
}
```

### Example 7: Handle Bounced Cheque

```java
@Transactional
public void bounceCheque(Long chequeId, BounceChequeRequest request) {
    Cheque cheque = chequeRepository.findById(chequeId)
        .orElseThrow(() -> new NotFoundException("Cheque not found"));
    
    if (!"DEPOSITED".equals(cheque.getStatus())) {
        throw new BusinessException("Only deposited cheques can be bounced");
    }
    
    // Update cheque status
    cheque.setStatus("BOUNCED");
    cheque.setBouncedDate(LocalDate.now());
    cheque.setBounceReason(request.getBounceReason());
    cheque.setBankCharges(request.getBankCharges());
    chequeRepository.save(cheque);
    
    // Update payment status
    Payment payment = paymentRepository.findByCheque(cheque)
        .orElseThrow(() -> new NotFoundException("Payment not found for cheque"));
    
    payment.setStatus("BOUNCED");
    paymentRepository.save(payment);
    
    // Reverse allocations if payment was allocated
    if (payment.getAllocatedAmount().compareTo(BigDecimal.ZERO) > 0) {
        for (PaymentAllocation allocation : payment.getAllocations()) {
            Invoice invoice = allocation.getInvoice();
            
            // Reverse invoice paid amount
            invoice.setPaidAmount(
                invoice.getPaidAmount().subtract(allocation.getAllocatedAmount())
            );
            invoice.calculateBalance();
            invoiceRepository.save(invoice);
        }
        
        // Remove allocations
        payment.getAllocations().clear();
        payment.calculateAllocations();
    }
    
    // If posted to ledger, create reversal entry
    if (payment.getIsPosted()) {
        CustomerLedger reversalLedger = CustomerLedger.builder()
            .customer(payment.getCustomer())
            .transactionDate(LocalDate.now())
            .transactionType("ADJUSTMENT")
            .referenceNumber(payment.getPaymentNumber() + "-REVERSAL")
            .referenceId(payment.getId())
            .referenceType("PAYMENT_REVERSAL")
            .debitAmount(payment.getPaymentAmount()) // Increases receivable
            .creditAmount(BigDecimal.ZERO)
            .description("Cheque bounced - " + cheque.getChequeNumber() + " - " + request.getBounceReason())
            .status("CLEARED")
            .build();
        
        // Calculate running balance
        BigDecimal previousBalance = customerLedgerService.getCustomerBalance(payment.getCustomer().getId());
        reversalLedger.setBalance(previousBalance.add(payment.getPaymentAmount()));
        
        customerLedgerRepository.save(reversalLedger);
        
        // Update customer outstanding balance
        Customer customer = payment.getCustomer();
        customer.setOutstandingBalance(reversalLedger.getBalance());
        customerRepository.save(customer);
    }
    
    // Record bank charges if any
    if (request.getBankCharges() != null && 
        request.getBankCharges().compareTo(BigDecimal.ZERO) > 0) {
        recordBankCharges(payment.getCustomer(), request.getBankCharges(), cheque.getChequeNumber());
    }
}
```

### Example 8: Get Customer Outstanding Invoices

```java
public List<InvoiceDTO> getOutstandingInvoices(Long customerId) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new NotFoundException("Customer not found"));
    
    List<Invoice> invoices = invoiceRepository
        .findByCustomerAndPaymentStatusIn(
            customer, 
            Arrays.asList("UNPAID", "PARTIAL")
        );
    
    return invoices.stream()
        .map(mapper::toDTO)
        .sorted(Comparator.comparing(InvoiceDTO::getDueDate))
        .collect(Collectors.toList());
}
```

### Example 9: Allocate Payment to Oldest Invoices First (FIFO)

```java
@Transactional
public void autoAllocatePayment(Long paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("Payment not found"));
    
    if (!payment.isCleared()) {
        throw new BusinessException("Payment must be cleared before allocation");
    }
    
    // Get outstanding invoices sorted by due date (oldest first)
    List<Invoice> invoices = invoiceRepository
        .findByCustomerAndPaymentStatusInOrderByDueDateAsc(
            payment.getCustomer(), 
            Arrays.asList("UNPAID", "PARTIAL")
        );
    
    BigDecimal remainingAmount = payment.getPaymentAmount();
    
    for (Invoice invoice : invoices) {
        if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            break;
        }
        
        BigDecimal invoiceBalance = invoice.getBalanceAmount();
        BigDecimal allocAmount = remainingAmount.compareTo(invoiceBalance) < 0 
            ? remainingAmount 
            : invoiceBalance;
        
        // Create allocation
        PaymentAllocation allocation = PaymentAllocation.builder()
            .invoice(invoice)
            .invoiceNumber(invoice.getInvoiceNumber())
            .invoiceBalanceBefore(invoiceBalance)
            .allocatedAmount(allocAmount)
            .currency(payment.getCurrency())
            .exchangeRate(payment.getExchangeRate())
            .notes("Auto-allocated")
            .build();
        
        allocation.calculateBalanceAfter();
        payment.addAllocation(allocation);
        
        // Update invoice
        invoice.setPaidAmount(invoice.getPaidAmount().add(allocAmount));
        invoice.calculateBalance();
        invoiceRepository.save(invoice);
        
        remainingAmount = remainingAmount.subtract(allocAmount);
    }
    
    // Update payment allocated amounts
    payment.calculateAllocations();
    paymentRepository.save(payment);
}
```

---

## 📁 Directory Structure

```
payment/
├── entity/
│   ├── Payment.java
│   ├── PaymentAllocation.java
│   ├── Cheque.java
│   └── README.md
└── dto/
    ├── PaymentDTO.java
    ├── CreatePaymentRequest.java
    ├── PaymentAllocationDTO.java
    └── ChequeDTO.java
```

---

## ✅ Summary

✅ **3 Entity classes** - Complete payment and cheque management  
✅ **4 DTO classes** - Request/response objects  
✅ **Multiple payment methods** - CASH, CHEQUE, BANK_TRANSFER, CREDIT_CARD, DEBIT_CARD, MOBILE_PAYMENT  
✅ **Payment allocation** - Allocate payments to multiple invoices  
✅ **Flexible allocation** - Partial or full allocation  
✅ **Auto-calculation** - Allocated and unallocated amounts  
✅ **Invoice integration** - Auto-update invoice paid amounts and payment status  
✅ **Cheque management** - Complete cheque lifecycle tracking  
✅ **Post-dated cheques** - Auto-detect and maturity tracking  
✅ **Cheque status workflow** - RECEIVED → DEPOSITED → CLEARED/BOUNCED  
✅ **Bounce handling** - Reverse allocations, create reversal entries  
✅ **Bank charges** - Track bank charges for bounced cheques  
✅ **Deposit tracking** - Track deposit bank, account, reference  
✅ **Clearing tracking** - Track clearing date  
✅ **Ledger integration** - Auto-post to customer ledger  
✅ **Balance updates** - Auto-update customer outstanding balance  
✅ **Multi-currency** - Foreign currency support with exchange rates  
✅ **Payment status** - DRAFT, PENDING, CLEARED, BOUNCED, CANCELLED  
✅ **Receipt management** - Receipt number, received by tracking  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade payment management  

**Everything you need for complete payment processing, cheque management, and payment allocation in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.payment
