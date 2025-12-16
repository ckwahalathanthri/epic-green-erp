# Resource Files - Epic Green ERP

This directory contains **resource files** for email templates and internationalization (i18n) in the Epic Green ERP system.

## 📦 Contents

### Email Templates (resources/templates/email) - 2 Files
1. **invoice.html** - Professional invoice email template
2. **order-confirmation.html** - Order confirmation email template

### Message Properties (resources/messages) - 2 Files
1. **messages.properties** - Default messages (English)
2. **messages_si.properties** - Sinhala translations (සිංහල)

---

## 📧 Email Templates

### Features

✅ **Responsive Design** - Works on all devices (desktop, tablet, mobile)  
✅ **Professional Layout** - Clean, modern design with company branding  
✅ **Thymeleaf Integration** - Uses Thymeleaf template engine  
✅ **Variable Support** - Dynamic content with `th:text="${variable}"`  
✅ **Inline CSS** - All styles inline for email client compatibility  
✅ **Tables for Layout** - Email-safe HTML structure  
✅ **Gradient Headers** - Eye-catching green gradient header  
✅ **Call-to-Action Buttons** - Clear CTA buttons  
✅ **Footer with Social Links** - Company info and social media links  

### 1. invoice.html

**Purpose:** Professional email template for sending invoices to customers

**Key Sections:**
- **Header** - Company branding with gradient background
- **Greeting** - Personalized customer greeting
- **Invoice Info** - Invoice number, date, due date, payment terms
- **Invoice Items Table** - Detailed line items with product, quantity, price
- **Totals Section** - Subtotal, discount, tax, total, paid, balance
- **Payment Info** - Payment due notice (if balance > 0)
- **Bank Details** - Bank account information for payments
- **Notes** - Custom notes (optional)
- **View Online Button** - Link to view invoice online
- **Footer** - Company contact information

**Thymeleaf Variables:**
```
${customerName}          - Customer name
${invoiceNumber}         - Invoice number
${invoiceDate}          - Invoice date
${dueDate}              - Due date
${paymentTerms}         - Payment terms (e.g., "Net 30")
${items}                - List of invoice items
  ${item.productName}   - Product name
  ${item.description}   - Product description
  ${item.quantity}      - Quantity
  ${item.unit}          - Unit (kg, lbs, etc.)
  ${item.unitPrice}     - Unit price
  ${item.lineTotal}     - Line total
${subtotal}             - Subtotal amount
${discountAmount}       - Discount amount
${discountPercentage}   - Discount percentage
${taxAmount}            - Tax amount
${taxPercentage}        - Tax percentage
${totalAmount}          - Total amount
${paidAmount}           - Paid amount
${balanceAmount}        - Balance due
${bankName}             - Bank name
${accountName}          - Account name
${accountNumber}        - Account number
${branchName}           - Branch name
${notes}                - Custom notes
${invoiceUrl}           - URL to view invoice online
${companyEmail}         - Company email
${companyPhone}         - Company phone
${companyAddress}       - Company address
${companyWebsite}       - Company website
```

**Usage Example:**
```java
@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    public void sendInvoiceEmail(Invoice invoice) {
        Context context = new Context();
        context.setVariable("customerName", invoice.getCustomer().getCustomerName());
        context.setVariable("invoiceNumber", invoice.getInvoiceNumber());
        context.setVariable("invoiceDate", invoice.getInvoiceDate());
        context.setVariable("dueDate", invoice.getDueDate());
        context.setVariable("paymentTerms", invoice.getPaymentTerms());
        context.setVariable("items", invoice.getItems());
        context.setVariable("subtotal", formatCurrency(invoice.getSubtotal()));
        context.setVariable("discountAmount", formatCurrency(invoice.getDiscountAmount()));
        context.setVariable("discountPercentage", invoice.getDiscountPercentage());
        context.setVariable("taxAmount", formatCurrency(invoice.getTaxAmount()));
        context.setVariable("taxPercentage", invoice.getTaxPercentage());
        context.setVariable("totalAmount", formatCurrency(invoice.getTotalAmount()));
        context.setVariable("paidAmount", formatCurrency(invoice.getPaidAmount()));
        context.setVariable("balanceAmount", formatCurrency(invoice.getBalanceAmount()));
        context.setVariable("bankName", "Bank of Ceylon");
        context.setVariable("accountName", "Epic Green (Pvt) Ltd");
        context.setVariable("accountNumber", "123456789");
        context.setVariable("branchName", "Colombo Main");
        context.setVariable("notes", invoice.getNotes());
        context.setVariable("invoiceUrl", "https://epicgreen.lk/invoices/" + invoice.getId());
        context.setVariable("companyEmail", "info@epicgreen.lk");
        context.setVariable("companyPhone", "+94 11 1234567");
        context.setVariable("companyAddress", "123 Spice Lane, Colombo 03, Sri Lanka");
        context.setVariable("companyWebsite", "www.epicgreen.lk");
        
        String htmlContent = templateEngine.process("email/invoice", context);
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(invoice.getCustomer().getEmail());
        helper.setSubject("Invoice #" + invoice.getInvoiceNumber() + " from Epic Green");
        helper.setText(htmlContent, true);
        
        mailSender.send(message);
    }
}
```

### 2. order-confirmation.html

**Purpose:** Email template for order confirmation with order tracking

**Key Sections:**
- **Header** - Success badge with order number
- **Greeting** - Personalized customer greeting
- **Order Info** - Order number, date, expected delivery, payment method
- **Order Timeline** - Visual timeline showing order status progress
- **Order Items Table** - Detailed line items
- **Totals Section** - Subtotal, discount, shipping, tax, total
- **Delivery & Billing Addresses** - Side-by-side address boxes
- **Payment Info** - Payment instructions (COD or Bank Transfer)
- **Track Order Button** - Link to track order
- **Help Section** - Customer service contact information
- **Footer** - Company contact information

**Thymeleaf Variables:**
```
${orderNumber}              - Order number
${customerName}             - Customer name
${orderDate}               - Order date
${expectedDeliveryDate}    - Expected delivery date
${paymentMethod}           - Payment method (COD, BANK_TRANSFER, etc.)
${trackingNumber}          - Tracking number (optional)
${orderStatus}             - Order status (CONFIRMED, PROCESSING, SHIPPED, DELIVERED)
${items}                   - List of order items
${subtotal}                - Subtotal amount
${discountAmount}          - Discount amount
${discountPercentage}      - Discount percentage
${shippingCharge}          - Shipping charge
${taxAmount}               - Tax amount
${taxPercentage}           - Tax percentage
${totalAmount}             - Total amount
${deliveryName}            - Delivery contact name
${deliveryAddress}         - Delivery address
${deliveryPhone}           - Delivery phone
${billingName}             - Billing contact name
${billingAddress}          - Billing address
${billingPhone}            - Billing phone
${bankName}                - Bank name (for bank transfer)
${accountName}             - Account name
${accountNumber}           - Account number
${branchName}              - Branch name
${paymentEmail}            - Payment email
${notes}                   - Special notes
${trackingUrl}             - URL to track order
${invoiceUrl}              - URL to view invoice
${customerServicePhone}    - Customer service phone
${customerServiceEmail}    - Customer service email
${companyEmail}            - Company email
${companyPhone}            - Company phone
${companyAddress}          - Company address
${companyWebsite}          - Company website
```

**Usage Example:**
```java
public void sendOrderConfirmationEmail(SalesOrder order) {
    Context context = new Context();
    context.setVariable("orderNumber", order.getOrderNumber());
    context.setVariable("customerName", order.getCustomer().getCustomerName());
    context.setVariable("orderDate", order.getOrderDate());
    context.setVariable("expectedDeliveryDate", order.getExpectedDeliveryDate());
    context.setVariable("paymentMethod", order.getPaymentMethod());
    context.setVariable("trackingNumber", order.getTrackingNumber());
    context.setVariable("orderStatus", order.getStatus());
    context.setVariable("items", order.getItems());
    context.setVariable("subtotal", formatCurrency(order.getSubtotal()));
    context.setVariable("discountAmount", formatCurrency(order.getDiscountAmount()));
    context.setVariable("discountPercentage", order.getDiscountPercentage());
    context.setVariable("shippingCharge", formatCurrency(order.getShippingCharge()));
    context.setVariable("taxAmount", formatCurrency(order.getTaxAmount()));
    context.setVariable("taxPercentage", order.getTaxPercentage());
    context.setVariable("totalAmount", formatCurrency(order.getTotalAmount()));
    context.setVariable("deliveryName", order.getDeliveryName());
    context.setVariable("deliveryAddress", order.getDeliveryAddress());
    context.setVariable("deliveryPhone", order.getDeliveryPhone());
    context.setVariable("billingName", order.getBillingName());
    context.setVariable("billingAddress", order.getBillingAddress());
    context.setVariable("billingPhone", order.getBillingPhone());
    context.setVariable("notes", order.getNotes());
    context.setVariable("trackingUrl", "https://epicgreen.lk/track/" + order.getOrderNumber());
    context.setVariable("invoiceUrl", "https://epicgreen.lk/invoices/" + order.getInvoiceId());
    context.setVariable("customerServicePhone", "+94 11 1234567");
    context.setVariable("customerServiceEmail", "support@epicgreen.lk");
    context.setVariable("companyEmail", "info@epicgreen.lk");
    context.setVariable("companyPhone", "+94 11 1234567");
    context.setVariable("companyAddress", "123 Spice Lane, Colombo 03, Sri Lanka");
    context.setVariable("companyWebsite", "www.epicgreen.lk");
    
    String htmlContent = templateEngine.process("email/order-confirmation", context);
    
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setTo(order.getCustomer().getEmail());
    helper.setSubject("Order Confirmation - Order #" + order.getOrderNumber());
    helper.setText(htmlContent, true);
    
    mailSender.send(message);
}
```

---

## 🌐 Message Properties (i18n)

### Features

✅ **Internationalization (i18n)** - Support for multiple languages  
✅ **Key-Value Format** - Easy to maintain and translate  
✅ **Spring Integration** - Works with Spring MessageSource  
✅ **Variable Substitution** - Supports {0}, {1} placeholders  
✅ **Organized Sections** - Grouped by functionality  
✅ **Comments** - Clear documentation for translators  

### 1. messages.properties (English)

**Default language file** - Used when no specific locale is requested

**Sections:**
- Application Messages
- Common Messages
- Validation Messages
- Success Messages
- Error Messages
- Authentication & Authorization
- User Management
- Customer Management
- Product Management
- Inventory Management
- Sales Management
- Purchase Management
- Production Management
- Payment Management
- Report Management
- Notification Messages
- Email Messages
- Date & Time Formats
- Currency & Number Formats
- File Upload
- Pagination
- Confirmation Messages
- System Messages

**Total:** 300+ message keys

### 2. messages_si.properties (Sinhala)

**Sinhala translations** - Used when locale is set to `si` or `si_LK`

**All sections translated to Sinhala (සිංහල)**

### Configuration

**application.properties:**
```properties
# Internationalization
spring.messages.basename=messages
spring.messages.encoding=UTF-8
spring.messages.fallback-to-system-locale=true
spring.messages.cache-duration=3600
```

**MessageSource Bean:**
```java
@Bean
public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = 
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(3600);
    return messageSource;
}
```

### Usage Examples

#### 1. In Java Code

```java
@Service
public class ProductService {
    
    @Autowired
    private MessageSource messageSource;
    
    public String createProduct(Product product, Locale locale) {
        // Save product
        productRepository.save(product);
        
        // Get localized message
        String message = messageSource.getMessage(
            "success.created",
            new Object[]{"Product"},
            locale
        );
        
        return message; // "Product created successfully" (English)
                       // "නිෂ්පාදනය සාර්ථකව නිර්මාණය කරන ලදී" (Sinhala)
    }
}
```

#### 2. In REST Controller

```java
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    @Autowired
    private MessageSource messageSource;
    
    @PostMapping
    public ResponseEntity<ApiResponse> createCustomer(
        @RequestBody Customer customer,
        @RequestHeader(name = "Accept-Language", defaultValue = "en") Locale locale
    ) {
        customerService.createCustomer(customer);
        
        String message = messageSource.getMessage(
            "customer.created",
            null,
            locale
        );
        
        return ResponseEntity.ok(new ApiResponse(message));
    }
}
```

#### 3. In Validation

```java
@NotBlank(message = "{validation.required}")
private String customerName;

@Email(message = "{validation.email.invalid}")
private String email;

@Size(min = 10, max = 15, message = "{validation.min.length}")
private String phone;
```

#### 4. In Thymeleaf Templates

```html
<!-- Simple message -->
<h1 th:text="#{app.title}">Application Title</h1>

<!-- Message with parameter -->
<p th:text="#{email.greeting(${customerName})}">Dear Customer</p>

<!-- Validation message -->
<span th:if="${#fields.hasErrors('email')}" 
      th:errors="*{email}" 
      class="error">
</span>
```

#### 5. Locale Resolver

```java
@Bean
public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.ENGLISH);
    return localeResolver;
}

@Bean
public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
}
```

**Usage:**
- English: `http://localhost:8080/products?lang=en`
- Sinhala: `http://localhost:8080/products?lang=si`

---

## 📁 Directory Structure

```
resources/
├── templates/
│   └── email/
│       ├── invoice.html
│       ├── order-confirmation.html
│       └── README.md
└── messages/
    ├── messages.properties
    ├── messages_si.properties
    └── README.md
```

---

## ✅ Summary

✅ **2 Email templates** - Professional, responsive email designs  
✅ **Thymeleaf integration** - Dynamic content with variables  
✅ **Mobile responsive** - Works on all devices  
✅ **Inline CSS** - Email client compatible  
✅ **Company branding** - Green gradient header with logo  
✅ **2 Message files** - English and Sinhala translations  
✅ **300+ Messages** - Complete i18n coverage  
✅ **Organized sections** - Easy to maintain and extend  
✅ **Variable support** - {0}, {1} placeholders  
✅ **Spring integration** - Works with MessageSource  
✅ **UTF-8 encoding** - Supports all languages including Sinhala  
✅ **Production-ready** - Professional quality templates and messages  

**Everything you need for professional email communications and multi-language support (English and Sinhala) in your spice production ERP!** 🚀

---

## 🌍 Supported Languages

| Language | Code | File | Status |
|----------|------|------|--------|
| English | en | messages.properties | ✅ Complete |
| Sinhala | si | messages_si.properties | ✅ Complete |

**To add more languages:**
1. Create new file: `messages_[language_code].properties`
2. Copy all keys from `messages.properties`
3. Translate values to target language
4. Save with UTF-8 encoding

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Project:** Epic Green ERP
