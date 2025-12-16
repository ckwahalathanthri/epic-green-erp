# Notification Module - Repository & Service

This directory contains **repository and service layers** for the notification system in Epic Green ERP.

## 📦 Contents

### Repository (notification/repository) - 2 Files
1. **NotificationTemplateRepository.java** - Notification template data access
2. **NotificationQueueRepository.java** - Notification queue data access

### Service (notification/service) - 3 Files
1. **NotificationService.java** - Notification service interface
2. **EmailService.java** - Email service implementation
3. **SmsService.java** - SMS service implementation

---

## 📊 1. Repositories

### NotificationTemplateRepository

**Purpose:** Data access for notification templates

**Key Methods:**
- **Find by code:** `findByTemplateCode()`
- **Find by type:** `findByNotificationType()`
- **Find by category:** `findByCategory()`
- **Find by event:** `findByEventTrigger()`
- **Find by status:** `findByStatus()`, `findByIsActive()`
- **Find system templates:** `findByIsSystem()`
- **Find auto-send:** `findByAutoSend()`
- **Most used:** `findMostUsedTemplates()`
- **Search:** `searchTemplates(keyword)`
- **Statistics:** `countByNotificationType()`, `getTemplateTypeDistribution()`

**Total:** 35+ query methods

### NotificationQueueRepository

**Purpose:** Data access for notification queue

**Key Methods:**
- **Find by status:** `findByStatus()`, `findPendingNotifications()`
- **Find ready to send:** `findNotificationsReadyToSend()`
- **Find failed:** `findFailedNotificationsForRetry()`
- **Find stuck:** `findStuckNotifications()`
- **Find by priority:** `findHighPriorityPendingNotifications()`
- **Find by type:** `findByNotificationType()`
- **Find by recipient:** `findByRecipient()`, `findByUserId()`
- **Find by batch:** `findByBatchId()`, `getBatchProgress()`
- **Find by reference:** `findByReferenceTypeAndReferenceId()`
- **Statistics:** `getNotificationStatusDistribution()`, `getSuccessRate()`
- **Provider stats:** `getProviderPerformance()`
- **Update operations:** `updateStatus()`, `markAsSent()`, `markAsFailed()`
- **Cancel:** `cancelNotification()`, `cancelBatchNotifications()`
- **Cleanup:** `deleteOldSentNotifications()`

**Total:** 55+ query methods

---

## 🔧 2. Service Layer

### NotificationService Interface

**Purpose:** Define notification service contract

**Method Categories:**
1. **Template Operations** (15 methods)
2. **Notification Sending** (12 methods)
3. **Queue Operations** (15 methods)
4. **Processing** (10 methods)
5. **Batch Operations** (4 methods)
6. **Statistics** (11 methods)
7. **Template Rendering** (4 methods)
8. **Cleanup** (3 methods)

**Total:** 74 methods

### EmailService Implementation

**Purpose:** Send email notifications

**Features:**
- ✅ **Simple email** - Plain text email
- ✅ **HTML email** - Rich HTML email
- ✅ **Email with attachments** - Single or multiple attachments
- ✅ **Email with CC/BCC** - Carbon copy and blind carbon copy
- ✅ **Custom from address** - Custom sender
- ✅ **Reply-to** - Custom reply-to address
- ✅ **Comprehensive email** - All options combined
- ✅ **JavaMailSender** - Spring Mail integration
- ✅ **UTF-8 encoding** - Full character support
- ✅ **Configuration** - Configurable via application.properties

**Key Methods:**
- `sendSimpleEmail()` - Plain text email
- `sendHtmlEmail()` - HTML email
- `sendEmailWithAttachment()` - Email with attachment
- `sendEmailWithAttachments()` - Email with multiple attachments
- `sendEmailWithCcBcc()` - Email with CC and BCC
- `sendEmailWithCustomFrom()` - Custom sender
- `sendEmailWithReplyTo()` - Custom reply-to
- `sendEmail(EmailRequest)` - Comprehensive email

### SmsService Implementation

**Purpose:** Send SMS notifications

**Features:**
- ✅ **Multi-provider** - Twilio, Dialog SMS (Sri Lanka)
- ✅ **Simple SMS** - Plain text SMS
- ✅ **Bulk SMS** - Send to multiple recipients
- ✅ **Custom from number** - Custom sender ID
- ✅ **Retry mechanism** - Auto-retry with exponential backoff
- ✅ **Phone validation** - Validate phone numbers
- ✅ **Phone formatting** - International format conversion
- ✅ **Configuration** - Configurable via application.properties
- ✅ **Error handling** - Comprehensive error handling
- ✅ **Provider responses** - Track provider message IDs

**Supported Providers:**
1. **Twilio** - International SMS service
2. **Dialog SMS** - Sri Lankan SMS provider

**Key Methods:**
- `sendSms()` - Send simple SMS
- `sendSms(List)` - Send to multiple recipients
- `sendViaTwilio()` - Twilio integration
- `sendViaDialog()` - Dialog SMS integration
- `sendSmsWithRetry()` - SMS with retry
- `isValidPhoneNumber()` - Validate phone
- `formatPhoneNumber()` - Format to international

---

## 💡 Usage Examples

### Example 1: Create Notification Template

```java
@Service
@RequiredArgsConstructor
public class TemplateSetupService {
    
    private final NotificationTemplateRepository templateRepository;
    
    public void createOrderConfirmationTemplate() {
        NotificationTemplate template = NotificationTemplate.builder()
            .templateCode("ORDER_CONFIRMATION_EMAIL")
            .templateName("Order Confirmation")
            .notificationType("EMAIL")
            .category("TRANSACTION")
            .eventTrigger("ORDER_CREATED")
            .subject("Order Confirmation - Order #{{orderNumber}}")
            .bodyTemplate("Dear {{customerName}},\n\n" +
                "Your order #{{orderNumber}} has been confirmed.\n\n" +
                "Total Amount: {{totalAmount}}\n\n" +
                "Thank you!")
            .htmlBodyTemplate("<html>...</html>")
            .isActive(true)
            .autoSend(true)
            .status("ACTIVE")
            .build();
        
        templateRepository.save(template);
    }
}
```

### Example 2: Send Email Using Template

```java
@Service
@RequiredArgsConstructor
public class OrderNotificationService {
    
    private final NotificationService notificationService;
    
    public void sendOrderConfirmation(SalesOrder order) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", order.getCustomer().getCustomerName());
        variables.put("orderNumber", order.getOrderNumber());
        variables.put("totalAmount", order.getTotalAmount());
        variables.put("orderDate", order.getOrderDate());
        
        notificationService.sendNotification(
            "ORDER_CONFIRMATION_EMAIL",
            variables,
            order.getCustomer().getEmail()
        );
    }
}
```

### Example 3: Send Simple Email

```java
@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    
    private final EmailService emailService;
    
    public void sendWelcomeEmail(User user) {
        String subject = "Welcome to Epic Green!";
        String body = String.format(
            "Dear %s,\n\n" +
            "Welcome to Epic Green ERP System.\n\n" +
            "Best regards,\n" +
            "Epic Green Team",
            user.getFullName()
        );
        
        emailService.sendSimpleEmail(user.getEmail(), subject, body);
    }
}
```

### Example 4: Send HTML Email with Attachments

```java
public void sendInvoiceEmail(Invoice invoice) {
    String htmlContent = generateInvoiceHtml(invoice);
    
    List<String> attachments = List.of(
        "/path/to/invoice.pdf",
        "/path/to/terms.pdf"
    );
    
    emailService.sendHtmlEmailWithAttachments(
        invoice.getCustomer().getEmail(),
        "Invoice #" + invoice.getInvoiceNumber(),
        htmlContent,
        attachments
    );
}
```

### Example 5: Send SMS

```java
@Service
@RequiredArgsConstructor
public class SmsNotificationService {
    
    private final SmsService smsService;
    
    public void sendOrderConfirmationSms(SalesOrder order) {
        String message = String.format(
            "Your order #%s has been confirmed. " +
            "Total: LKR %s. Thank you!",
            order.getOrderNumber(),
            order.getTotalAmount()
        );
        
        SmsService.SmsResponse response = smsService.sendSms(
            order.getCustomer().getPhone(),
            message
        );
        
        if (response.isSuccess()) {
            log.info("SMS sent successfully. Message ID: {}", 
                response.getProviderMessageId());
        } else {
            log.error("Failed to send SMS: {}", response.getMessage());
        }
    }
}
```

### Example 6: Send Bulk SMS

```java
public void sendPromotionalSms(List<Customer> customers, String message) {
    List<String> phoneNumbers = customers.stream()
        .map(Customer::getPhone)
        .filter(smsService::isValidPhoneNumber)
        .map(smsService::formatPhoneNumber)
        .toList();
    
    List<SmsService.SmsResponse> responses = smsService.sendSms(phoneNumbers, message);
    
    long successCount = responses.stream().filter(SmsService.SmsResponse::isSuccess).count();
    log.info("SMS sent to {} of {} recipients", successCount, responses.size());
}
```

### Example 7: Process Notification Queue

```java
@Component
@RequiredArgsConstructor
public class NotificationProcessor {
    
    private final NotificationService notificationService;
    
    @Scheduled(fixedDelay = 10000) // Every 10 seconds
    public void processQueue() {
        int processed = notificationService.processPendingNotifications(100);
        log.info("Processed {} notifications", processed);
    }
    
    @Scheduled(fixedDelay = 60000) // Every minute
    public void retryFailed() {
        int retried = notificationService.retryFailedNotifications(50);
        log.info("Retried {} failed notifications", retried);
    }
}
```

### Example 8: Get Batch Progress

```java
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @GetMapping("/batches/{batchId}/progress")
    public ResponseEntity<Map<String, Object>> getBatchProgress(
        @PathVariable String batchId
    ) {
        Map<String, Object> progress = notificationService.getBatchProgress(batchId);
        return ResponseEntity.ok(progress);
    }
}
```

### Example 9: Get Notification Statistics

```java
@GetMapping("/statistics")
public ResponseEntity<Map<String, Object>> getStatistics() {
    Map<String, Object> stats = notificationService.getNotificationStatistics();
    return ResponseEntity.ok(stats);
}

@GetMapping("/dashboard")
public ResponseEntity<Map<String, Object>> getDashboard() {
    Map<String, Object> dashboard = notificationService.getDashboardStatistics();
    return ResponseEntity.ok(dashboard);
}
```

### Example 10: Cleanup Old Notifications

```java
@Component
@RequiredArgsConstructor
public class NotificationCleanupScheduler {
    
    private final NotificationService notificationService;
    
    @Scheduled(cron = "0 0 3 * * ?") // Run at 3 AM daily
    public void cleanupOldNotifications() {
        Map<String, Integer> results = notificationService.cleanupOldNotifications(30);
        
        log.info("Deleted {} sent notifications", results.get("sent"));
        log.info("Deleted {} cancelled notifications", results.get("cancelled"));
    }
}
```

---

## 📋 Configuration

### application.properties

```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=noreply@epicgreen.lk
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

app.mail.from=noreply@epicgreen.lk
app.mail.from-name=Epic Green
app.mail.enabled=true

# SMS Configuration - Twilio
app.sms.provider=TWILIO
app.sms.enabled=true
app.sms.twilio.account-sid=your-account-sid
app.sms.twilio.auth-token=your-auth-token
app.sms.twilio.from-number=+1234567890

# SMS Configuration - Dialog (Sri Lanka)
app.sms.provider=DIALOG
app.sms.dialog.api-key=your-api-key
app.sms.dialog.sender-id=EpicGreen
```

---

## 📁 Directory Structure

```
notification/
├── repository/
│   ├── NotificationTemplateRepository.java
│   ├── NotificationQueueRepository.java
│   └── README.md
└── service/
    ├── NotificationService.java
    ├── EmailService.java
    ├── SmsService.java
    └── README.md
```

---

## ✅ Summary

✅ **2 Repositories** - 90+ query methods total  
✅ **3 Service files** - 74+ business logic methods  
✅ **Email service** - Complete email sending (simple, HTML, attachments)  
✅ **SMS service** - Multi-provider SMS (Twilio, Dialog)  
✅ **Template support** - Variable substitution with {{variables}}  
✅ **Queue management** - Priority-based queue processing  
✅ **Batch operations** - Bulk notification sending  
✅ **Retry mechanism** - Auto-retry with exponential backoff  
✅ **Provider tracking** - Track provider message IDs  
✅ **Statistics** - Comprehensive analytics  
✅ **Search & filter** - Advanced search  
✅ **Status tracking** - PENDING, PROCESSING, SENT, FAILED, CANCELLED  
✅ **Priority levels** - HIGH, MEDIUM, LOW  
✅ **Scheduled sending** - Schedule for specific time  
✅ **Error handling** - Comprehensive error tracking  
✅ **Phone validation** - Validate and format phone numbers  
✅ **International format** - Phone number formatting  
✅ **Configuration** - Configurable via properties  
✅ **Production-ready** - Enterprise-grade implementation  

**Everything you need for complete notification management with email sending (JavaMailSender), SMS sending (Twilio, Dialog), template support with variable substitution, queue-based processing with retry logic, batch operations, comprehensive statistics, and multi-provider support in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.notification
