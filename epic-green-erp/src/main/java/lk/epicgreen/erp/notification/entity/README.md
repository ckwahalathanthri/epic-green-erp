# Notification Module - Epic Green ERP

This directory contains **entities and DTOs** for notification management (email, SMS, push, in-app) in the Epic Green ERP system.

## 📦 Contents

### Entities (notification/entity) - 2 Files
1. **NotificationTemplate.java** - Notification templates
2. **NotificationQueue.java** - Notification queue management

### DTOs (notification/dto) - 1 File
1. **NotificationRequest.java** - Notification sending request

---

## 📊 Database Schema

### Notification Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  NOTIFICATION SYSTEM                    │
│                                                         │
│  ┌────────────────────────────────────────────────┐   │
│  │      Notification Templates                    │   │
│  │  - Email templates                             │   │
│  │  - SMS templates                               │   │
│  │  - Push notification templates                 │   │
│  │  - In-app notification templates               │   │
│  │  - Variable substitution                       │   │
│  └────────────────┬───────────────────────────────┘   │
│                   │                                     │
│                   ▼                                     │
│  ┌────────────────────────────────────────────────┐   │
│  │      Notification Request                      │   │
│  │  - Recipient(s)                                │   │
│  │  - Content/Template                            │   │
│  │  - Priority                                    │   │
│  │  - Schedule                                    │   │
│  └────────────────┬───────────────────────────────┘   │
│                   │                                     │
│                   ▼                                     │
│  ┌────────────────────────────────────────────────┐   │
│  │      Notification Queue                        │   │
│  │  - PENDING notifications                       │   │
│  │  - Priority-based processing                   │   │
│  │  - Scheduled sending                           │   │
│  │  - Retry logic                                 │   │
│  └────────────────┬───────────────────────────────┘   │
│                   │                                     │
│                   ▼                                     │
│  ┌────────────────────────────────────────────────┐   │
│  │      Notification Sender                       │   │
│  │  - Email service (SMTP)                        │   │
│  │  - SMS gateway (Twilio, etc.)                  │   │
│  │  - Push service (FCM, APNS)                    │   │
│  │  - In-app notifications                        │   │
│  └────────────────┬───────────────────────────────┘   │
│                   │                                     │
│                   ▼                                     │
│  ┌────────────────────────────────────────────────┐   │
│  │      Status Tracking                           │   │
│  │  - SENT / FAILED                               │   │
│  │  - Provider responses                          │   │
│  │  - Error tracking                              │   │
│  └────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

---

## 📋 1. NotificationTemplate Entity

**Purpose:** Store notification templates with variable substitution

### Key Fields

```java
// Identification
- templateCode (unique, e.g., "ORDER_CONFIRMATION_EMAIL")
- templateName

// Classification
- notificationType (EMAIL, SMS, PUSH, IN_APP)
- category (TRANSACTION, ALERT, REMINDER, MARKETING, SYSTEM)
- eventTrigger (ORDER_CREATED, PAYMENT_RECEIVED, STOCK_LOW, etc.)

// Content
- subject (for email/push/in-app title)
- bodyTemplate (plain text with variables)
- htmlBodyTemplate (HTML email template)
- smsTemplate (SMS content)
- variables (JSON - list of available variables)

// Sender information
- defaultSender (email or phone)
- defaultSenderName
- replyTo

// Email options
- cc (comma-separated)
- bcc (comma-separated)
- attachments (JSON)

// Properties
- priority (HIGH, MEDIUM, LOW)
- isActive
- isSystem (built-in, cannot delete)
- requiresApproval
- autoSend (send automatically when triggered)

// Status
- status (ACTIVE, INACTIVE, DRAFT, ARCHIVED)
- locale (language)

// Description
- description
- notes

// Usage tracking
- usageCount
```

### Helper Methods

```java
boolean isEmailType(); // notificationType == EMAIL
boolean isSmsType(); // notificationType == SMS
boolean isPushType(); // notificationType == PUSH
boolean isInAppType(); // notificationType == IN_APP
boolean canDelete(); // !isSystem
boolean canEdit(); // !isSystem || status == DRAFT
String getTemplateForType(); // Get template based on type
```

### Template Variables

Templates support variable substitution using `{{variableName}}` syntax:

```
Subject: Order Confirmation - {{orderNumber}}

Dear {{customerName}},

Thank you for your order #{{orderNumber}} placed on {{orderDate}}.

Order Details:
- Total Amount: {{totalAmount}}
- Payment Method: {{paymentMethod}}
- Delivery Address: {{deliveryAddress}}

Your order will be delivered by {{deliveryDate}}.

Best regards,
{{companyName}}
```

---

## 🔧 2. NotificationQueue Entity

**Purpose:** Queue of notifications waiting to be sent

### Key Fields

```java
// Template reference
- template (FK NotificationTemplate)

// Classification
- notificationType (EMAIL, SMS, PUSH, IN_APP)
- category (TRANSACTION, ALERT, REMINDER, MARKETING, SYSTEM)

// Recipient
- recipient (email, phone, user ID, device token)
- recipientName
- userId (if recipient is user)

// Sender
- sender
- senderName

// Content (rendered from template)
- subject
- body (plain text)
- htmlBody (HTML)
- variables (JSON - values used)

// Email options
- cc
- bcc
- replyTo
- attachments (JSON)

// Priority and scheduling
- priority (HIGH, MEDIUM, LOW)
- scheduledTime (when to send)

// Status
- status (PENDING, PROCESSING, SENT, FAILED, CANCELLED)

// Timing
- sentTime
- failedTime

// Retry logic
- retryCount
- maxRetryAttempts
- lastRetryTime
- nextRetryTime

// Error handling
- errorMessage
- errorDetails

// Provider information
- provider (email provider, SMS gateway, push service)
- providerMessageId (external reference)
- providerResponse

// Reference (link to source document)
- referenceType (ORDER, INVOICE, PAYMENT, etc.)
- referenceId
- referenceNumber

// Bulk sending
- batchId (for bulk operations)

- notes
```

### Helper Methods

```java
boolean isPending(); // status == PENDING
boolean isProcessing(); // status == PROCESSING
boolean isSent(); // status == SENT
boolean isFailed(); // status == FAILED
boolean canRetry(); // isFailed && retryCount < maxRetryAttempts
boolean shouldSendNow(); // isPending && scheduledTime <= now
Long getMinutesUntilScheduled(); // Time until scheduled
Long getMinutesSinceSent(); // Time since sent
boolean isHighPriority(); // priority == HIGH
```

### Status Flow

```
PENDING (waiting to send)
  ↓ (picked by processor)
PROCESSING (currently sending)
  ↓ (success)
SENT (delivered)
  OR
  ↓ (failure)
FAILED (can retry)
  ↓ (max retries reached)
FAILED (permanent)
  OR
  ↓ (manual action)
CANCELLED
```

---

## 💡 Usage Examples

### Example 1: Create Email Template

```java
@Transactional
public NotificationTemplate createOrderConfirmationTemplate() {
    String bodyTemplate = """
    Dear {{customerName}},
    
    Thank you for your order #{{orderNumber}} placed on {{orderDate}}.
    
    Order Summary:
    - Total Items: {{totalItems}}
    - Subtotal: {{subtotal}}
    - Tax: {{taxAmount}}
    - Total Amount: {{totalAmount}}
    
    Payment Method: {{paymentMethod}}
    Delivery Address: {{deliveryAddress}}
    Expected Delivery: {{expectedDeliveryDate}}
    
    You can track your order at: {{trackingUrl}}
    
    Best regards,
    {{companyName}}
    """;
    
    String htmlBodyTemplate = """
    <!DOCTYPE html>
    <html>
    <head>
        <style>
            body { font-family: Arial, sans-serif; }
            .header { background-color: #4CAF50; color: white; padding: 20px; }
            .content { padding: 20px; }
            .footer { background-color: #f1f1f1; padding: 10px; text-align: center; }
        </style>
    </head>
    <body>
        <div class="header">
            <h1>Order Confirmation</h1>
        </div>
        <div class="content">
            <p>Dear {{customerName}},</p>
            <p>Thank you for your order <strong>#{{orderNumber}}</strong></p>
            <h3>Order Details:</h3>
            <ul>
                <li>Total Items: {{totalItems}}</li>
                <li>Total Amount: <strong>{{totalAmount}}</strong></li>
                <li>Expected Delivery: {{expectedDeliveryDate}}</li>
            </ul>
            <p><a href="{{trackingUrl}}">Track Your Order</a></p>
        </div>
        <div class="footer">
            <p>{{companyName}} | {{companyEmail}} | {{companyPhone}}</p>
        </div>
    </body>
    </html>
    """;
    
    String variables = """
    [
        {"name": "customerName", "type": "string", "description": "Customer name"},
        {"name": "orderNumber", "type": "string", "description": "Order number"},
        {"name": "orderDate", "type": "date", "description": "Order date"},
        {"name": "totalItems", "type": "number", "description": "Total items"},
        {"name": "subtotal", "type": "currency", "description": "Subtotal amount"},
        {"name": "taxAmount", "type": "currency", "description": "Tax amount"},
        {"name": "totalAmount", "type": "currency", "description": "Total amount"},
        {"name": "paymentMethod", "type": "string", "description": "Payment method"},
        {"name": "deliveryAddress", "type": "string", "description": "Delivery address"},
        {"name": "expectedDeliveryDate", "type": "date", "description": "Expected delivery date"},
        {"name": "trackingUrl", "type": "string", "description": "Order tracking URL"},
        {"name": "companyName", "type": "string", "description": "Company name"},
        {"name": "companyEmail", "type": "string", "description": "Company email"},
        {"name": "companyPhone", "type": "string", "description": "Company phone"}
    ]
    """;
    
    NotificationTemplate template = NotificationTemplate.builder()
        .templateCode("ORDER_CONFIRMATION_EMAIL")
        .templateName("Order Confirmation Email")
        .notificationType("EMAIL")
        .category("TRANSACTION")
        .eventTrigger("ORDER_CREATED")
        .subject("Order Confirmation - {{orderNumber}}")
        .bodyTemplate(bodyTemplate)
        .htmlBodyTemplate(htmlBodyTemplate)
        .variables(variables)
        .defaultSender("orders@epicgreen.lk")
        .defaultSenderName("Epic Green Orders")
        .replyTo("support@epicgreen.lk")
        .priority("HIGH")
        .isActive(true)
        .isSystem(true)
        .autoSend(true)
        .status("ACTIVE")
        .locale("en")
        .description("Email sent to customer when order is created")
        .build();
    
    return notificationTemplateRepository.save(template);
}
```

### Example 2: Send Notification Using Template

```java
@Transactional
public void sendOrderConfirmation(SalesOrder order) {
    // Get template
    NotificationTemplate template = notificationTemplateRepository
        .findByTemplateCode("ORDER_CONFIRMATION_EMAIL")
        .orElseThrow(() -> new NotFoundException("Template not found"));
    
    // Build variables
    Map<String, Object> variables = new HashMap<>();
    variables.put("customerName", order.getCustomer().getCustomerName());
    variables.put("orderNumber", order.getOrderNumber());
    variables.put("orderDate", order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    variables.put("totalItems", order.getTotalItems());
    variables.put("subtotal", formatCurrency(order.getSubtotal()));
    variables.put("taxAmount", formatCurrency(order.getTaxAmount()));
    variables.put("totalAmount", formatCurrency(order.getTotalAmount()));
    variables.put("paymentMethod", order.getPaymentMethod());
    variables.put("deliveryAddress", order.getDeliveryAddress());
    variables.put("expectedDeliveryDate", order.getExpectedDeliveryDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    variables.put("trackingUrl", "https://epicgreen.lk/track/" + order.getOrderNumber());
    variables.put("companyName", "Epic Green");
    variables.put("companyEmail", "info@epicgreen.lk");
    variables.put("companyPhone", "+94 11 1234567");
    
    // Create notification request
    NotificationRequest request = NotificationRequest.builder()
        .templateCode(template.getTemplateCode())
        .notificationType("EMAIL")
        .category("TRANSACTION")
        .recipients(List.of(
            NotificationRequest.Recipient.builder()
                .recipient(order.getCustomer().getEmail())
                .recipientName(order.getCustomer().getCustomerName())
                .userId(order.getCustomer().getUserId())
                .build()
        ))
        .variables(variables)
        .priority("HIGH")
        .referenceType("SALES_ORDER")
        .referenceId(order.getId())
        .referenceNumber(order.getOrderNumber())
        .sendImmediately(true)
        .build();
    
    // Send notification
    notificationService.sendNotification(request);
}
```

### Example 3: Send Notification without Template

```java
public void sendCustomNotification(String recipient, String subject, String body) {
    NotificationRequest request = NotificationRequest.builder()
        .notificationType("EMAIL")
        .category("SYSTEM")
        .recipients(List.of(
            NotificationRequest.Recipient.builder()
                .recipient(recipient)
                .build()
        ))
        .subject(subject)
        .body(body)
        .priority("MEDIUM")
        .sendImmediately(true)
        .build();
    
    notificationService.sendNotification(request);
}
```

### Example 4: Process Notification Queue

```java
@Service
public class NotificationProcessor {
    
    @Scheduled(fixedDelay = 10000) // Every 10 seconds
    @Transactional
    public void processNotificationQueue() {
        // Get pending notifications (priority-based)
        List<NotificationQueue> pendingNotifications = notificationQueueRepository
            .findPendingNotifications(LocalDateTime.now(), PageRequest.of(0, 100));
        
        for (NotificationQueue notification : pendingNotifications) {
            if (notification.shouldSendNow()) {
                processNotification(notification);
            }
        }
    }
    
    private void processNotification(NotificationQueue notification) {
        try {
            // Mark as processing
            notification.setStatus("PROCESSING");
            notificationQueueRepository.save(notification);
            
            // Send based on type
            switch (notification.getNotificationType()) {
                case "EMAIL":
                    sendEmail(notification);
                    break;
                case "SMS":
                    sendSms(notification);
                    break;
                case "PUSH":
                    sendPushNotification(notification);
                    break;
                case "IN_APP":
                    sendInAppNotification(notification);
                    break;
            }
            
            // Mark as sent
            notification.setStatus("SENT");
            notification.setSentTime(LocalDateTime.now());
            notificationQueueRepository.save(notification);
            
        } catch (Exception e) {
            // Mark as failed
            notification.setStatus("FAILED");
            notification.setFailedTime(LocalDateTime.now());
            notification.setErrorMessage(e.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
            
            // Schedule retry if possible
            if (notification.canRetry()) {
                long delayMinutes = (long) Math.pow(2, notification.getRetryCount()) * 5;
                notification.setNextRetryTime(LocalDateTime.now().plusMinutes(delayMinutes));
                notification.setStatus("PENDING");
            }
            
            notificationQueueRepository.save(notification);
            
            log.error("Failed to send notification: " + notification.getId(), e);
        }
    }
    
    private void sendEmail(NotificationQueue notification) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(notification.getSender(), notification.getSenderName());
        helper.setTo(notification.getRecipient());
        helper.setSubject(notification.getSubject());
        
        // Use HTML body if available
        if (notification.getHtmlBody() != null) {
            helper.setText(notification.getBody(), notification.getHtmlBody());
        } else {
            helper.setText(notification.getBody());
        }
        
        // Add CC
        if (notification.getCc() != null) {
            String[] ccAddresses = notification.getCc().split(",");
            helper.setCc(ccAddresses);
        }
        
        // Add BCC
        if (notification.getBcc() != null) {
            String[] bccAddresses = notification.getBcc().split(",");
            helper.setBcc(bccAddresses);
        }
        
        // Add attachments
        if (notification.getAttachments() != null) {
            List<Map<String, String>> attachments = parseAttachments(notification.getAttachments());
            for (Map<String, String> attachment : attachments) {
                File file = new File(attachment.get("filePath"));
                helper.addAttachment(attachment.get("fileName"), file);
            }
        }
        
        mailSender.send(message);
    }
    
    private void sendSms(NotificationQueue notification) throws Exception {
        // Use SMS gateway (e.g., Twilio)
        Message message = Message.creator(
            new PhoneNumber(notification.getRecipient()),
            new PhoneNumber(notification.getSender()),
            notification.getBody()
        ).create();
        
        notification.setProviderMessageId(message.getSid());
        notification.setProviderResponse(message.getStatus().toString());
    }
}
```

### Example 5: Send Bulk Notifications

```java
@Transactional
public void sendBulkNotifications(
    String templateCode,
    List<Map<String, Object>> recipientData
) {
    String batchId = UUID.randomUUID().toString();
    
    for (Map<String, Object> data : recipientData) {
        NotificationRequest request = NotificationRequest.builder()
            .templateCode(templateCode)
            .notificationType("EMAIL")
            .recipients(List.of(
                NotificationRequest.Recipient.builder()
                    .recipient((String) data.get("email"))
                    .recipientName((String) data.get("name"))
                    .variables(data)
                    .build()
            ))
            .variables(data)
            .priority("LOW")
            .batchId(batchId)
            .build();
        
        notificationService.queueNotification(request);
    }
}
```

### Example 6: Schedule Notification

```java
public void schedulePaymentReminder(Invoice invoice, LocalDateTime reminderDate) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("customerName", invoice.getCustomer().getCustomerName());
    variables.put("invoiceNumber", invoice.getInvoiceNumber());
    variables.put("dueDate", invoice.getDueDate());
    variables.put("balanceAmount", invoice.getBalanceAmount());
    
    NotificationRequest request = NotificationRequest.builder()
        .templateCode("PAYMENT_REMINDER_EMAIL")
        .notificationType("EMAIL")
        .category("REMINDER")
        .recipients(List.of(
            NotificationRequest.Recipient.builder()
                .recipient(invoice.getCustomer().getEmail())
                .recipientName(invoice.getCustomer().getCustomerName())
                .build()
        ))
        .variables(variables)
        .priority("HIGH")
        .scheduledTime(reminderDate)
        .referenceType("INVOICE")
        .referenceId(invoice.getId())
        .referenceNumber(invoice.getInvoiceNumber())
        .build();
    
    notificationService.queueNotification(request);
}
```

### Example 7: Send Low Stock Alert

```java
@Scheduled(cron = "0 0 9 * * *") // Every day at 9 AM
public void checkLowStockAndNotify() {
    List<Inventory> lowStockItems = inventoryRepository
        .findLowStockItems();
    
    if (lowStockItems.isEmpty()) {
        return;
    }
    
    // Build alert message
    StringBuilder message = new StringBuilder();
    message.append("Low Stock Alert:\n\n");
    
    for (Inventory item : lowStockItems) {
        message.append(String.format(
            "- %s: %s %s (Reorder Point: %s)\n",
            item.getProduct().getProductCode(),
            item.getQuantityOnHand(),
            item.getProduct().getUnit(),
            item.getReorderPoint()
        ));
    }
    
    // Send to inventory managers
    List<String> recipients = userRepository
        .findByRole("INVENTORY_MANAGER")
        .stream()
        .map(User::getEmail)
        .collect(Collectors.toList());
    
    NotificationRequest request = NotificationRequest.builder()
        .templateCode("STOCK_LOW_ALERT")
        .notificationType("EMAIL")
        .category("ALERT")
        .recipients(recipients.stream()
            .map(email -> NotificationRequest.Recipient.builder()
                .recipient(email)
                .build())
            .collect(Collectors.toList()))
        .variables(Map.of(
            "lowStockCount", lowStockItems.size(),
            "lowStockDetails", message.toString()
        ))
        .priority("HIGH")
        .sendImmediately(true)
        .build();
    
    notificationService.sendNotification(request);
}
```

---

## 📁 Directory Structure

```
notification/
├── entity/
│   ├── NotificationTemplate.java
│   ├── NotificationQueue.java
│   └── README.md
└── dto/
    └── NotificationRequest.java
```

---

## ✅ Summary

✅ **2 Entity classes** - Complete notification management  
✅ **1 DTO class** - Comprehensive notification request  
✅ **4 Notification types** - EMAIL, SMS, PUSH, IN_APP  
✅ **5 Categories** - TRANSACTION, ALERT, REMINDER, MARKETING, SYSTEM  
✅ **Template support** - Reusable templates with variables  
✅ **Variable substitution** - {{variableName}} syntax  
✅ **HTML emails** - Rich HTML email templates  
✅ **Attachments** - Support for email attachments  
✅ **Priority-based** - HIGH, MEDIUM, LOW priority  
✅ **Scheduled sending** - Schedule notifications for specific time  
✅ **Bulk sending** - Send to multiple recipients with batch ID  
✅ **Queue management** - Queue-based processing  
✅ **Retry logic** - Automatic retry with exponential backoff  
✅ **Error tracking** - Comprehensive error logging  
✅ **Provider integration** - SMTP, SMS gateway, FCM, APNS  
✅ **Status tracking** - PENDING, PROCESSING, SENT, FAILED  
✅ **Reference tracking** - Link to source documents  
✅ **Multi-language** - Support for multiple locales  
✅ **Event triggers** - 20+ predefined event triggers  
✅ **Comprehensive validation** - All inputs validated  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Production-ready** - Enterprise-grade notification system  

**Everything you need for complete notification management, including email notifications, SMS alerts, push notifications, in-app messages, template management, scheduled notifications, bulk sending, retry logic, and comprehensive tracking in a spice production ERP!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.notification
