# Common Utilities - Epic Green ERP

This directory contains **utility classes** providing reusable helper methods for common operations.

## 📦 Contents (5 Utility Classes)

1. **DateTimeUtil.java** - Date/time operations
2. **StringUtil.java** - String manipulation
3. **ValidationUtil.java** - Validation helpers
4. **NumberUtil.java** - Number formatting/calculations
5. **FileUtil.java** - File handling operations

---

## 1. DateTimeUtil.java

**Purpose:** Date and time manipulation utilities

### Categories

#### Current Date/Time
```java
LocalDate today = DateTimeUtil.getCurrentDate();
LocalDateTime now = DateTimeUtil.getCurrentDateTime();
long timestamp = DateTimeUtil.getCurrentTimestamp();
```

#### Formatting
```java
String dateStr = DateTimeUtil.formatDate(date);              // "2024-12-10"
String displayStr = DateTimeUtil.formatDateForDisplay(date); // "10 Dec 2024"
String fileStr = DateTimeUtil.formatDateForFile(date);       // "20241210"
String datetimeStr = DateTimeUtil.formatDateTime(dateTime);  // "2024-12-10 10:30:00"
```

#### Parsing
```java
LocalDate date = DateTimeUtil.parseDate("2024-12-10");
LocalDateTime dateTime = DateTimeUtil.parseDateTime("2024-12-10 10:30:00");
```

#### Conversion
```java
LocalDate localDate = DateTimeUtil.toLocalDate(date);
Date javaDate = DateTimeUtil.toDate(localDate);
LocalDateTime localDateTime = DateTimeUtil.toLocalDateTime(timestamp);
```

#### Date Manipulation
```java
LocalDate future = DateTimeUtil.addDays(date, 7);
LocalDate past = DateTimeUtil.subtractDays(date, 30);
LocalDate nextMonth = DateTimeUtil.addMonths(date, 1);
```

#### Date Comparison
```java
boolean isAfter = DateTimeUtil.isAfter(date1, date2);
boolean isToday = DateTimeUtil.isToday(date);
boolean isPast = DateTimeUtil.isPast(date);
boolean isFuture = DateTimeUtil.isFuture(date);
```

#### Date Calculations
```java
long days = DateTimeUtil.daysBetween(startDate, endDate);
long months = DateTimeUtil.monthsBetween(startDate, endDate);
int age = DateTimeUtil.calculateAge(birthDate);
```

#### Period Start/End
```java
LocalDateTime startOfDay = DateTimeUtil.getStartOfDay(date);
LocalDateTime endOfDay = DateTimeUtil.getEndOfDay(date);
LocalDate firstDay = DateTimeUtil.getFirstDayOfMonth(date);
LocalDate lastDay = DateTimeUtil.getLastDayOfMonth(date);
```

#### Business Days
```java
boolean isWeekend = DateTimeUtil.isWeekend(date);
boolean isWeekday = DateTimeUtil.isWeekday(date);
LocalDate nextBusinessDay = DateTimeUtil.getNextBusinessDay(date);
```

#### Validation
```java
boolean inRange = DateTimeUtil.isDateInRange(date, startDate, endDate);
boolean validRange = DateTimeUtil.isValidDateRange(startDate, endDate);
```

**Total Methods:** ~60+ date/time operations

---

## 2. StringUtil.java

**Purpose:** String manipulation and operations

### Categories

#### Null/Empty Checks
```java
boolean empty = StringUtil.isEmpty(str);
boolean notEmpty = StringUtil.isNotEmpty(str);
boolean blank = StringUtil.isBlank(str);
boolean notBlank = StringUtil.isNotBlank(str);
```

#### Default Values
```java
String result = StringUtil.defaultIfNull(str, "default");
String result = StringUtil.defaultIfEmpty(str, "default");
String result = StringUtil.defaultIfBlank(str, "default");
```

#### Trimming
```java
String trimmed = StringUtil.trim(str);
String trimmedOrNull = StringUtil.trimToNull(str);
String trimmedOrEmpty = StringUtil.trimToEmpty(str);
```

#### Case Conversion
```java
String upper = StringUtil.toUpperCase(str);
String lower = StringUtil.toLowerCase(str);
String capitalized = StringUtil.capitalize(str);          // "Hello"
String titleCase = StringUtil.toTitleCase(str);           // "Hello World"
String snake = StringUtil.camelToSnake("camelCase");      // "camel_case"
String camel = StringUtil.snakeToCamel("snake_case");     // "snakeCase"
```

#### Substring Operations
```java
String left = StringUtil.left(str, 5);
String right = StringUtil.right(str, 5);
String truncated = StringUtil.truncate(str, 50);  // Adds "..."
```

#### Padding
```java
String padded = StringUtil.leftPad(str, 10, '0');   // "0000001234"
String padded = StringUtil.rightPad(str, 10, ' ');
```

#### Repeat
```java
String repeated = StringUtil.repeat("*", 5);  // "*****"
```

#### Joining/Splitting
```java
String joined = StringUtil.join(array, ", ");
List<String> parts = StringUtil.splitAndTrim(str, ",");
```

#### Cleaning
```java
String clean = StringUtil.removeWhitespace(str);
String normalized = StringUtil.normalizeWhitespace(str);  // Multiple spaces to single
String clean = StringUtil.removeSpecialCharacters(str);
String clean = StringUtil.removeAccents(str);             // Café -> Cafe
```

#### Validation
```java
boolean validEmail = StringUtil.isValidEmail(email);
boolean validPhone = StringUtil.isValidPhone(phone);
boolean numeric = StringUtil.isNumeric(str);
boolean alphabetic = StringUtil.isAlphabetic(str);
boolean alphanumeric = StringUtil.isAlphanumeric(str);
```

#### Contains/Starts/Ends
```java
boolean contains = StringUtil.contains(str, substring);
boolean containsIgnoreCase = StringUtil.containsIgnoreCase(str, substring);
boolean startsWith = StringUtil.startsWith(str, prefix);
boolean endsWith = StringUtil.endsWith(str, suffix);
```

#### Comparison
```java
boolean equal = StringUtil.equals(str1, str2);
boolean equalIgnoreCase = StringUtil.equalsIgnoreCase(str1, str2);
```

#### Masking
```java
String masked = StringUtil.maskEmail("john@example.com");      // "joh****@example.com"
String masked = StringUtil.maskPhone("1234567890");            // "******7890"
String masked = StringUtil.maskCreditCard("1234567890123456"); // "************3456"
```

#### Generation
```java
String random = StringUtil.generateRandomString(10);
String randomNum = StringUtil.generateRandomNumeric(6);
```

**Total Methods:** ~70+ string operations

---

## 3. ValidationUtil.java

**Purpose:** Common validation helpers

### Categories

#### String Validation
```java
boolean valid = ValidationUtil.isNotNullOrEmpty(str);
boolean valid = ValidationUtil.isValidLength(str, 2, 100);
boolean valid = ValidationUtil.hasMinLength(str, 8);
boolean valid = ValidationUtil.hasMaxLength(str, 50);
```

#### Email/Phone Validation
```java
boolean validEmail = ValidationUtil.isValidEmail(email);
boolean validPhone = ValidationUtil.isValidPhone(phone);
boolean validMobile = ValidationUtil.isValidMobile(mobile);
```

#### Code/Username Validation
```java
boolean validCode = ValidationUtil.isValidCode(code);
boolean validUsername = ValidationUtil.isValidUsername(username);
```

#### Number Validation
```java
boolean numeric = ValidationUtil.isNumeric(str);
boolean integer = ValidationUtil.isInteger(str);
boolean validDecimal = ValidationUtil.isValidDecimal(str);
boolean positive = ValidationUtil.isPositive(number);
boolean inRange = ValidationUtil.isInRange(number, min, max);
```

#### Percentage/Price Validation
```java
boolean validPercentage = ValidationUtil.isValidPercentage(value);
boolean validPrice = ValidationUtil.isValidPrice(price);
boolean validQuantity = ValidationUtil.isValidQuantity(quantity);
```

#### Date Validation
```java
boolean valid = ValidationUtil.isValidDate(date);
boolean past = ValidationUtil.isPastDate(date);
boolean future = ValidationUtil.isFutureDate(date);
boolean todayOrFuture = ValidationUtil.isTodayOrFuture(date);
boolean validRange = ValidationUtil.isValidDateRange(startDate, endDate);
boolean inRange = ValidationUtil.isDateInRange(date, startDate, endDate);
```

#### Collection Validation
```java
boolean notEmpty = ValidationUtil.isNotEmpty(collection);
boolean validSize = ValidationUtil.isValidSize(collection, min, max);
boolean hasMinSize = ValidationUtil.hasMinSize(collection, 1);
```

#### Object Validation
```java
boolean notNull = ValidationUtil.isNotNull(object);
boolean allNotNull = ValidationUtil.allNotNull(obj1, obj2, obj3);
boolean anyNotNull = ValidationUtil.anyNotNull(obj1, obj2, obj3);
```

#### ID Validation
```java
boolean validId = ValidationUtil.isValidId(id);
boolean allValid = ValidationUtil.allValidIds(idList);
```

#### Password Validation
```java
boolean strong = ValidationUtil.isStrongPassword(password);
boolean valid = ValidationUtil.isValidPassword(password, minLength);
```

#### Credit Card Validation
```java
boolean valid = ValidationUtil.isValidCreditCard(cardNumber);  // Luhn algorithm
```

#### URL Validation
```java
boolean validUrl = ValidationUtil.isValidUrl(url);
```

#### Business Validation
```java
boolean validStock = ValidationUtil.isValidStock(quantity);
boolean validDiscount = ValidationUtil.isValidDiscount(discount);
boolean validCreditLimit = ValidationUtil.isValidCreditLimit(limit);
```

**Total Methods:** ~60+ validation operations

---

## 4. NumberUtil.java

**Purpose:** Number formatting and calculations

### Categories

#### Formatting
```java
String formatted = NumberUtil.formatDecimal(1234.56);      // "1,234.56"
String formatted = NumberUtil.formatInteger(1234567);      // "1,234,567"
String formatted = NumberUtil.formatPercentage(0.1234);    // "12.34%"
String formatted = NumberUtil.formatQuantity(123.456);     // "123.456"
String formatted = NumberUtil.formatCurrency(amount);      // "Rs. 1,234.56"
```

#### Parsing
```java
Integer num = NumberUtil.parseInteger("123");
Long num = NumberUtil.parseLong("123456");
Double num = NumberUtil.parseDouble("123.45");
BigDecimal num = NumberUtil.parseBigDecimal("123.45");
```

#### Conversion
```java
BigDecimal bd = NumberUtil.toBigDecimal(number);
Integer i = NumberUtil.toInteger(number);
Long l = NumberUtil.toLong(number);
Double d = NumberUtil.toDouble(number);
```

#### Rounding
```java
BigDecimal rounded = NumberUtil.round(value, 2);
BigDecimal rounded = NumberUtil.round(value);  // Default 2 decimal places
Integer roundedUp = NumberUtil.roundUp(12.3);  // 13
Integer roundedDown = NumberUtil.roundDown(12.7);  // 12
```

#### Comparison
```java
int result = NumberUtil.compare(value1, value2);
boolean equal = NumberUtil.isEqual(value1, value2);
boolean greater = NumberUtil.isGreaterThan(value1, value2);
boolean less = NumberUtil.isLessThan(value1, value2);
boolean zero = NumberUtil.isZero(value);
boolean positive = NumberUtil.isPositive(value);
boolean negative = NumberUtil.isNegative(value);
```

#### Arithmetic Operations
```java
BigDecimal sum = NumberUtil.add(value1, value2);
BigDecimal diff = NumberUtil.subtract(value1, value2);
BigDecimal product = NumberUtil.multiply(value1, value2);
BigDecimal quotient = NumberUtil.divide(value1, value2, scale);
```

#### Percentage Calculations
```java
BigDecimal result = NumberUtil.percentage(100, 20);           // 20 (20% of 100)
BigDecimal percent = NumberUtil.percentageOf(25, 100);        // 25 (25 is 25% of 100)
BigDecimal result = NumberUtil.addPercentage(100, 10);        // 110 (100 + 10%)
BigDecimal result = NumberUtil.subtractPercentage(100, 10);   // 90 (100 - 10%)
```

#### Min/Max
```java
BigDecimal min = NumberUtil.min(value1, value2);
BigDecimal max = NumberUtil.max(value1, value2);
```

#### Null Safety
```java
BigDecimal safe = NumberUtil.defaultIfNull(value);  // Returns ZERO if null
BigDecimal safe = NumberUtil.defaultIfNull(value, defaultValue);
```

#### Business Calculations
```java
BigDecimal lineTotal = NumberUtil.calculateLineTotal(quantity, price);
BigDecimal discount = NumberUtil.calculateDiscount(amount, percentage);
BigDecimal afterDiscount = NumberUtil.calculateAmountAfterDiscount(amount, percentage);
BigDecimal tax = NumberUtil.calculateTax(amount, taxRate);
BigDecimal total = NumberUtil.calculateTotalWithTax(amount, taxRate);
```

**Total Methods:** ~50+ number operations

---

## 5. FileUtil.java

**Purpose:** File handling operations

### Categories

#### File Name Operations
```java
String extension = FileUtil.getFileExtension(fileName);  // ".jpg"
String nameOnly = FileUtil.getFileNameWithoutExtension(fileName);
String uniqueName = FileUtil.generateUniqueFileName(fileName);
String sanitized = FileUtil.sanitizeFileName(fileName);
```

#### File Validation
```java
boolean notEmpty = FileUtil.isNotEmpty(file);
boolean validSize = FileUtil.isValidSize(file, maxSize);
boolean isImage = FileUtil.isImage(file);
boolean isDocument = FileUtil.isDocument(file);
boolean validImport = FileUtil.isValidImportFile(file);
boolean allowed = FileUtil.hasAllowedExtension(fileName, allowedList);
```

#### File Upload
```java
String fileName = FileUtil.saveFile(file, uploadDir);
String fileName = FileUtil.saveFileWithName(file, uploadDir, fileName);
```

#### File Download
```java
Path path = FileUtil.loadFile(fileName, uploadDir);
byte[] bytes = FileUtil.readFileAsBytes(fileName, uploadDir);
String content = FileUtil.readFileAsString(fileName, uploadDir);
```

#### File Deletion
```java
boolean deleted = FileUtil.deleteFile(fileName, uploadDir);
int count = FileUtil.deleteFiles(fileNameList, uploadDir);
```

#### Directory Operations
```java
boolean created = FileUtil.createDirectoryIfNotExists(dirPath);
List<String> files = FileUtil.listFiles(dirPath);
boolean deleted = FileUtil.deleteDirectory(dirPath);
```

#### File Info
```java
long size = FileUtil.getFileSize(fileName, uploadDir);
String readable = FileUtil.getHumanReadableSize(bytes);  // "1.5 MB"
boolean exists = FileUtil.fileExists(fileName, uploadDir);
```

#### Content Type
```java
String contentType = FileUtil.getContentType(fileName);  // "image/jpeg"
```

#### File Copy/Move
```java
boolean copied = FileUtil.copyFile(source, sourceDir, target, targetDir);
boolean moved = FileUtil.moveFile(source, sourceDir, target, targetDir);
```

**Total Methods:** ~35+ file operations

---

## 📊 Summary Statistics

| Utility Class | Methods | Categories | Purpose |
|--------------|---------|------------|---------|
| DateTimeUtil | 60+ | 12 | Date/time operations |
| StringUtil | 70+ | 13 | String manipulation |
| ValidationUtil | 60+ | 15 | Validation helpers |
| NumberUtil | 50+ | 11 | Number operations |
| FileUtil | 35+ | 9 | File operations |
| **TOTAL** | **~275+** | **60** | **All utilities** |

---

## 🚀 Usage Examples

### Example 1: Product Creation with Validation

```java
@Service
public class ProductService {
    
    public ProductDTO createProduct(CreateProductRequest request) {
        // String validation
        String code = StringUtil.trimToNull(request.getCode());
        if (!ValidationUtil.isValidCode(code)) {
            throw new ValidationException("Invalid product code format");
        }
        
        // Check duplicates
        if (repository.existsByCode(code)) {
            throw new DuplicateResourceException("Product", "code", code);
        }
        
        // Number validation
        if (!ValidationUtil.isValidPrice(request.getPrice())) {
            throw new ValidationException("Price must be greater than zero");
        }
        
        // Calculate with tax
        BigDecimal priceWithTax = NumberUtil.calculateTotalWithTax(
            request.getPrice(),
            BigDecimal.valueOf(15)  // 15% tax
        );
        
        // Create product
        Product product = new Product();
        product.setCode(code);
        product.setPrice(NumberUtil.round(priceWithTax));
        product.setCreatedAt(DateTimeUtil.getCurrentDateTime());
        
        return save(product);
    }
}
```

### Example 2: File Upload with Validation

```java
@RestController
public class FileUploadController {
    
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileDTO>> uploadFile(
            @RequestParam("file") MultipartFile file) {
        
        // Validate file
        if (!FileUtil.isNotEmpty(file)) {
            throw new BusinessException("File is empty");
        }
        
        if (!FileUtil.isValidSize(file, AppConstants.MAX_FILE_SIZE)) {
            throw new BusinessException("File too large");
        }
        
        if (!FileUtil.isImage(file)) {
            throw new BusinessException("Only images allowed");
        }
        
        // Save file
        String fileName = FileUtil.saveFile(file, AppConstants.UPLOAD_DIR);
        
        // Create response
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(fileName);
        fileDTO.setOriginalName(file.getOriginalFilename());
        fileDTO.setSize(FileUtil.getHumanReadableSize(file.getSize()));
        fileDTO.setContentType(FileUtil.getContentType(fileName));
        fileDTO.setUploadedAt(DateTimeUtil.getCurrentDateTime());
        
        return ResponseEntity.ok(ApiResponse.success(fileDTO));
    }
}
```

### Example 3: Order Calculation

```java
public class OrderService {
    
    public OrderDTO calculateOrder(CreateOrderRequest request) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        // Calculate line totals
        for (OrderItemRequest item : request.getItems()) {
            BigDecimal lineTotal = NumberUtil.calculateLineTotal(
                item.getQuantity(),
                item.getPrice()
            );
            subtotal = NumberUtil.add(subtotal, lineTotal);
        }
        
        // Apply discount
        BigDecimal discount = NumberUtil.calculateDiscount(
            subtotal,
            request.getDiscountPercentage()
        );
        BigDecimal afterDiscount = NumberUtil.subtract(subtotal, discount);
        
        // Calculate tax
        BigDecimal tax = NumberUtil.calculateTax(afterDiscount, taxRate);
        
        // Total
        BigDecimal total = NumberUtil.add(afterDiscount, tax);
        
        // Format for display
        OrderDTO dto = new OrderDTO();
        dto.setSubtotal(NumberUtil.formatCurrency(subtotal));
        dto.setDiscount(NumberUtil.formatCurrency(discount));
        dto.setTax(NumberUtil.formatCurrency(tax));
        dto.setTotal(NumberUtil.formatCurrency(total));
        dto.setOrderDate(DateTimeUtil.formatDateForDisplay(LocalDate.now()));
        
        return dto;
    }
}
```

### Example 4: Report Generation

```java
public class ReportService {
    
    public ReportDTO generateSalesReport(LocalDate startDate, LocalDate endDate) {
        // Validate date range
        if (!ValidationUtil.isValidDateRange(startDate, endDate)) {
            throw new ValidationException("Invalid date range");
        }
        
        // Calculate period
        long days = DateTimeUtil.daysBetween(startDate, endDate);
        
        // Fetch data
        List<Sale> sales = repository.findByDateBetween(
            DateTimeUtil.getStartOfDay(startDate),
            DateTimeUtil.getEndOfDay(endDate)
        );
        
        // Calculate totals
        BigDecimal totalSales = sales.stream()
            .map(Sale::getAmount)
            .reduce(BigDecimal.ZERO, NumberUtil::add);
        
        BigDecimal averagePerDay = NumberUtil.divide(
            totalSales,
            BigDecimal.valueOf(days)
        );
        
        // Create report
        ReportDTO report = new ReportDTO();
        report.setStartDate(DateTimeUtil.formatDateForDisplay(startDate));
        report.setEndDate(DateTimeUtil.formatDateForDisplay(endDate));
        report.setPeriodDays(days);
        report.setTotalSales(NumberUtil.formatCurrency(totalSales));
        report.setAveragePerDay(NumberUtil.formatCurrency(averagePerDay));
        report.setGeneratedAt(DateTimeUtil.formatDateTime(LocalDateTime.now()));
        
        return report;
    }
}
```

---

## 💡 Best Practices

### 1. Always Use Utilities
```java
// ❌ Wrong - Manual implementation
String formatted = String.format("%,.2f", amount);

// ✅ Correct - Use utility
String formatted = NumberUtil.formatDecimal(amount);
```

### 2. Null Safety
```java
// ❌ Wrong - NullPointerException risk
String upper = str.toUpperCase();

// ✅ Correct - Null-safe
String upper = StringUtil.toUpperCase(str);
```

### 3. Validation Before Processing
```java
// ✅ Good practice
if (ValidationUtil.isValidEmail(email)) {
    sendEmail(email);
} else {
    throw new ValidationException("Invalid email");
}
```

### 4. Use BigDecimal for Money
```java
// ❌ Wrong - Precision loss with double
double total = price * quantity;

// ✅ Correct - Use BigDecimal
BigDecimal total = NumberUtil.multiply(price, BigDecimal.valueOf(quantity));
```

---

## 📁 Directory Structure

```
common/util/
├── DateTimeUtil.java
├── StringUtil.java
├── ValidationUtil.java
├── NumberUtil.java
├── FileUtil.java
└── README.md
```

---

## 🔧 Where to Place These Files

```
src/main/java/lk/epicgreen/erp/
└── common/
    └── util/
        ├── DateTimeUtil.java
        ├── StringUtil.java
        ├── ValidationUtil.java
        ├── NumberUtil.java
        └── FileUtil.java
```

---

## ✅ Key Benefits

1. **Code Reusability** - Write once, use everywhere
2. **Consistency** - Same operations work the same way
3. **Null Safety** - All methods handle null gracefully
4. **Maintainability** - Fix bugs in one place
5. **Testability** - Easy to unit test
6. **Readability** - Self-documenting code
7. **Performance** - Optimized implementations

---

## 🎯 Integration with Other Components

### Works With:
- **AppConstants** - Uses format patterns, limits
- **ValidationMessages** - For error messages
- **ErrorMessages** - For exception messages
- **Exceptions** - Throws appropriate exceptions

### Used By:
- **Services** - Business logic
- **Controllers** - Input validation
- **DTOs** - Data transformation
- **Mappers** - Entity conversions

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.common.util
