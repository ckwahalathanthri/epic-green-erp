# Configuration - Epic Green ERP

This directory contains **all configuration files** for the Epic Green ERP system.

## 📦 Contents (11 Files)

### Core Configuration Files (8)
1. **SecurityConfig.java** - Spring Security with OAuth2 JWT
2. **OAuth2Config.java** - JWT token configuration
3. **WebConfig.java** - Web MVC configuration
4. **SwaggerConfig.java** - API documentation
5. **RedisConfig.java** - Redis caching
6. **RabbitMQConfig.java** - Message queuing
7. **AsyncConfig.java** - Async execution
8. **JpaConfig.java** - Database/JPA configuration

### Supporting Components (3)
9. **JwtAuthenticationEntryPoint.java** - Handles auth failures
10. **JwtAccessDeniedHandler.java** - Handles authorization failures
11. **JwtTokenFilter.java** - Validates JWT tokens

### Interceptors (2)
12. **RequestLoggingInterceptor.java** - Logs HTTP requests
13. **PerformanceInterceptor.java** - Monitors performance

---

## 🔐 1. SecurityConfig.java

**Purpose:** Configures Spring Security with OAuth2 JWT authentication

### Key Features
- ✅ **Stateless JWT authentication** - No sessions
- ✅ **OAuth2 Bearer token** - Industry standard
- ✅ **Role-based access control** - Permissions per endpoint
- ✅ **CORS configuration** - Cross-origin support
- ✅ **Method-level security** - @PreAuthorize, @Secured
- ✅ **Custom error handling** - Proper JSON responses

### Security Rules

```java
// Public endpoints - no authentication
- /api/v1/auth/** (login, register, forgot password)
- /api/v1/public/** (public APIs)
- /actuator/health, /actuator/info
- /swagger-ui/** (API documentation)
- /v3/api-docs/** (OpenAPI spec)

// Protected endpoints - require authentication + permission
- User management: PERM_VIEW_USERS, PERM_CREATE_USERS, etc.
- Product management: PERM_VIEW_PRODUCTS, PERM_CREATE_PRODUCTS, etc.
- All other API endpoints: authenticated()
```

### Usage Example

```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    
    // Requires PERM_VIEW_PRODUCTS permission
    @GetMapping
    @PreAuthorize("hasAuthority('" + RolesAndPermissions.PERM_VIEW_PRODUCTS + "')")
    public ResponseEntity<PageResponse<ProductDTO>> getAll(Pageable pageable) {
        // ...
    }
    
    // Requires PERM_CREATE_PRODUCTS permission
    @PostMapping
    @PreAuthorize("hasAuthority('" + RolesAndPermissions.PERM_CREATE_PRODUCTS + "')")
    public ResponseEntity<ApiResponse<ProductDTO>> create(@Valid @RequestBody CreateProductRequest request) {
        // ...
    }
}
```

### CORS Configuration

```java
Allowed Origins:
- http://localhost:4200 (Angular dev)
- http://localhost:3000 (React dev)
- https://epicgreen.lk (Production)
- https://www.epicgreen.lk (Production www)

Allowed Methods:
- GET, POST, PUT, PATCH, DELETE, OPTIONS

Allowed Headers:
- Authorization, Content-Type, Accept, X-Requested-With, X-XSRF-TOKEN

Exposed Headers:
- Authorization, X-Total-Count, X-Page-Number, X-Page-Size
```

---

## 🔑 2. OAuth2Config.java

**Purpose:** Configures JWT token generation and validation

### Components

**JwtDecoder** - Validates incoming JWT tokens
```java
- Validates token signature
- Checks expiration time
- Verifies issuer
- Returns 60-second time skew tolerance
```

**JwtEncoder** - Generates JWT tokens
```java
- Signs tokens with RSA private key
- Includes user claims (username, authorities)
- Sets expiration time
- Sets issuer
```

**RSA Key Pair** - For token signing
```java
- 2048-bit RSA key pair
- Public key for validation
- Private key for signing
- Generated at startup (dev mode)
- Load from secure storage (production)
```

### Configuration Properties

```properties
# application.yml
security:
  jwt:
    issuer: https://epicgreen.lk
    access-token-expiration: 3600      # 1 hour
    refresh-token-expiration: 86400    # 24 hours
```

### Token Structure

```json
{
  "sub": "admin",
  "authorities": [
    "ROLE_ADMIN",
    "PERM_VIEW_PRODUCTS",
    "PERM_CREATE_PRODUCTS"
  ],
  "iss": "https://epicgreen.lk",
  "iat": 1702800000,
  "exp": 1702803600
}
```

### Production Security Note

⚠️ **Important:** In production, load RSA keys from secure storage (e.g., AWS KMS, Azure Key Vault, HashiCorp Vault) instead of generating at runtime.

```java
// Production example
@Bean
public KeyPair keyPair() {
    // Load from secure storage
    return keyVaultService.loadRsaKeyPair("epic-green-jwt-keys");
}
```

---

## 🌐 3. WebConfig.java

**Purpose:** Configures Web MVC features

### Features

**CORS Configuration**
```java
- Allows cross-origin requests from Angular/React
- Credentials support enabled
- 1-hour preflight cache
```

**Static Resources**
```java
/static/** → classpath:/static/ (1 day cache)
/uploads/** → file:uploads/ (1 hour cache)
/swagger-ui/** → Swagger UI resources
```

**Interceptors**
```java
- RequestLoggingInterceptor: Logs all requests
- PerformanceInterceptor: Monitors slow requests (>1s)
```

**Message Converters**
```java
- Jackson for JSON serialization
- JavaTimeModule for date/time handling
- Pretty printing enabled
```

**Date/Time Formatting**
```java
- Date format: yyyy-MM-dd
- DateTime format: yyyy-MM-dd HH:mm:ss
- Timezone: Asia/Colombo
```

### Usage Example

```java
// File upload endpoint
@PostMapping("/upload")
public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
    String fileName = fileService.save(file, AppConstants.UPLOAD_DIR);
    String url = "/uploads/" + fileName; // Accessible via WebConfig resource handler
    return ResponseEntity.ok(ApiResponse.success(url));
}
```

---

## 📚 4. SwaggerConfig.java

**Purpose:** Configures API documentation using OpenAPI 3

### Access Points

```
Swagger UI: http://localhost:8080/swagger-ui.html
API Docs:   http://localhost:8080/v3/api-docs
```

### Features

**API Information**
```
Title: Epic Green ERP API
Version: 1.0
Description: Complete ERP system for spice production
Contact: support@epicgreen.lk
License: Proprietary
```

**Servers**
```
- Local: http://localhost:8080
- Staging: https://staging-api.epicgreen.lk
- Production: https://api.epicgreen.lk
```

**Security**
```
- Bearer Authentication (JWT)
- Obtain token from /api/v1/auth/login
- Include in Authorization header: Bearer {token}
```

### Usage in Controllers

```java
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    
    @Operation(summary = "Get all products", description = "Returns paginated list of products")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ProductDTO>> getAll(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size
    ) {
        // ...
    }
}
```

---

## 💾 5. RedisConfig.java

**Purpose:** Configures Redis for caching

### Cache Configuration

| Cache Name | TTL | Use Case |
|------------|-----|----------|
| `products` | 30 min | Product catalog |
| `categories` | 1 hour | Product categories |
| `users` | 15 min | User data |
| `settings` | 1 hour | System settings |
| `suppliers` | 30 min | Supplier list |
| `customers` | 30 min | Customer list |
| `inventory` | 5 min | Stock levels (frequently changing) |
| `reports` | 10 min | Report data |

### Features

**Connection**
```java
- Host: localhost (configurable)
- Port: 6379 (configurable)
- Password: optional
- Database: 0 (configurable)
- Connection factory: Lettuce
```

**Serialization**
```java
- Keys: String serializer
- Values: JSON serializer (Jackson)
- Java time module support
- Type information included
```

**Error Handling**
```java
- Cache failures don't break application
- Errors logged but operation continues
- Graceful degradation
```

### Usage Example

```java
@Service
public class ProductService {
    
    // Cache result for 30 minutes
    @Cacheable(value = AppConstants.CACHE_PRODUCTS, key = "#id")
    public ProductDTO getById(Long id) {
        return repository.findById(id)
            .map(mapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }
    
    // Update cache
    @CachePut(value = AppConstants.CACHE_PRODUCTS, key = "#result.id")
    public ProductDTO update(Long id, UpdateProductRequest request) {
        // ...
    }
    
    // Evict from cache
    @CacheEvict(value = AppConstants.CACHE_PRODUCTS, key = "#id")
    public void delete(Long id) {
        // ...
    }
    
    // Clear entire cache
    @CacheEvict(value = AppConstants.CACHE_PRODUCTS, allEntries = true)
    public void clearCache() {
        // ...
    }
}
```

### Configuration Properties

```properties
# application.yml
spring:
  redis:
    host: localhost
    port: 6379
    password:  # optional
    database: 0
    timeout: 2000
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
```

---

## 📬 6. RabbitMQConfig.java

**Purpose:** Configures RabbitMQ for async messaging

### Queues and Bindings

| Queue | Routing Key | Use Case |
|-------|-------------|----------|
| `email.queue` | `email.*` | Send emails |
| `sms.queue` | `sms.*` | Send SMS |
| `notification.queue` | `notification.*` | Push notifications |
| `report.queue` | `report.*` | Generate reports |
| `export.queue` | `export.*` | Export data |
| `import.queue` | `import.*` | Import data |
| `sync.queue` | `sync.*` | Mobile sync |
| `audit.queue` | `audit.*` | Audit logging |
| `dead.letter.queue` | `dlx.*` | Failed messages |

### Features

**Connection**
```java
- Host: localhost
- Port: 5672
- Username: guest
- Password: guest
- Virtual host: /
- Connection timeout: 30 seconds
- Heartbeat: 60 seconds
```

**Message Conversion**
```java
- JSON serialization (Jackson)
- Java time module support
```

**Listener Configuration**
```java
- Concurrent consumers: 3-10
- Prefetch count: 10
- Auto-acknowledge: false
```

**Dead Letter Queue**
```java
- Failed messages go to DLX
- Inspect and retry manually
```

### Usage Example

```java
@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final RabbitTemplate rabbitTemplate;
    
    // Send email message
    public void sendEmail(EmailMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.MAIN_EXCHANGE,
            "email.send",
            message
        );
    }
}

@Component
@RequiredArgsConstructor
public class EmailListener {
    
    // Listen for email messages
    @RabbitListener(queues = AppConstants.QUEUE_EMAIL)
    public void processEmail(EmailMessage message) {
        // Send email
        emailSender.send(message);
    }
}
```

### Configuration Properties

```properties
# application.yml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
    listener:
      simple:
        concurrency: 3
        max-concurrency: 10
        prefetch: 10
```

---

## ⚡ 7. AsyncConfig.java

**Purpose:** Configures thread pools for async operations

### Thread Pools

| Executor | Core | Max | Queue | Use Case |
|----------|------|-----|-------|----------|
| `taskExecutor` | 5 | 10 | 100 | Default @Async |
| `emailExecutor` | 2 | 5 | 50 | Send emails |
| `smsExecutor` | 2 | 5 | 50 | Send SMS |
| `reportExecutor` | 2 | 4 | 20 | Generate reports |
| `fileExecutor` | 2 | 4 | 20 | Import/export |
| `syncExecutor` | 3 | 6 | 50 | Mobile sync |
| `auditExecutor` | 2 | 5 | 100 | Audit logging |
| `taskScheduler` | 5 | - | - | Scheduled tasks |

### Features

**Rejection Policy**
```java
- CallerRunsPolicy: Caller thread executes if queue full
- Prevents task rejection
```

**Graceful Shutdown**
```java
- Waits for tasks to complete
- Timeout: 60-180 seconds depending on executor
```

**Exception Handling**
```java
- Custom exception handler
- Logs exception details
- Doesn't break application
```

### Usage Example

```java
@Service
public class NotificationService {
    
    // Uses default taskExecutor
    @Async
    public void sendNotification(Notification notification) {
        // Send notification
    }
    
    // Uses specific emailExecutor
    @Async("emailExecutor")
    public void sendEmail(String to, String subject, String body) {
        // Send email
    }
    
    // Uses reportExecutor
    @Async("reportExecutor")
    public CompletableFuture<Report> generateReport(ReportRequest request) {
        Report report = reportGenerator.generate(request);
        return CompletableFuture.completedFuture(report);
    }
}

@Service
public class ScheduledTasks {
    
    // Scheduled task using taskScheduler
    @Scheduled(cron = "0 0 2 * * *") // 2 AM daily
    public void cleanupOldData() {
        // Cleanup logic
    }
    
    @Scheduled(fixedDelay = 300000) // Every 5 minutes
    public void syncData() {
        // Sync logic
    }
}
```

---

## 🗄️ 8. JpaConfig.java

**Purpose:** Configures JPA/Hibernate and database connection

### HikariCP Configuration

**Connection Pool Settings**
```java
- Minimum idle: 5
- Maximum pool size: 20
- Connection timeout: 30 seconds
- Idle timeout: 10 minutes
- Max lifetime: 30 minutes
- Test query: SELECT 1
```

**Performance Optimizations**
```java
- Statement caching enabled
- Cache size: 250
- Batch rewriting enabled
- Server-side prepared statements
```

### Hibernate Configuration

**Basic Settings**
```java
- Dialect: MySQL8Dialect
- DDL auto: validate (production)
- Show SQL: false (production)
- Format SQL: true
- Timezone: Asia/Colombo
```

**Naming Strategy**
```java
- Physical: CamelCaseToUnderscores
  Example: productCode → product_code
- Implicit: SpringImplicitNamingStrategy
```

**Performance Settings**
```java
- Batch size: 20
- Order inserts: true
- Order updates: true
- Fetch size: 50
- Second-level cache: enabled
- Query cache: enabled
```

**Query Optimizations**
```java
- Fail on pagination over collection fetch
- IN clause parameter padding
- Use SQL comments
```

### Configuration Properties

```properties
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/epicgreen_erp?useSSL=false&serverTimezone=Asia/Colombo
    username: epicgreen_user
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  jpa:
    hibernate:
      ddl-auto: validate  # Use Flyway for migrations
    show-sql: false       # Enable for debugging
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
        jdbc:
          batch_size: 20
          time_zone: Asia/Colombo
        cache:
          use_second_level_cache: true
          use_query_cache: true
```

### Usage Example

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Uses HikariCP connection pool
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategory(@Param("categoryId") Long categoryId);
    
    // Batch insert using Hibernate batch processing
    @Modifying
    @Query("UPDATE Product p SET p.price = :price WHERE p.id IN :ids")
    int updatePrices(@Param("ids") List<Long> ids, @Param("price") BigDecimal price);
}

@Service
@Transactional  // Uses JpaTransactionManager
public class ProductService {
    
    public void batchCreate(List<CreateProductRequest> requests) {
        List<Product> products = requests.stream()
            .map(mapper::toEntity)
            .toList();
        
        // Hibernate will batch these inserts (batch_size: 20)
        repository.saveAll(products);
    }
}
```

---

## 🔍 Supporting Components

### JwtAuthenticationEntryPoint.java

Handles authentication failures (401 Unauthorized)

```java
Returns:
{
  "success": false,
  "error": {
    "timestamp": "2024-12-10T10:00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Authentication failed: Full authentication is required",
    "path": "/api/v1/products"
  }
}
```

### JwtAccessDeniedHandler.java

Handles authorization failures (403 Forbidden)

```java
Returns:
{
  "success": false,
  "error": {
    "timestamp": "2024-12-10T10:00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Access denied: Access is denied",
    "path": "/api/v1/admin/users"
  }
}
```

### JwtTokenFilter.java

Validates JWT tokens on every request

```java
Process:
1. Extract token from Authorization header
2. Decode and validate token
3. Extract username and authorities
4. Set authentication in SecurityContext
5. Continue filter chain
```

### RequestLoggingInterceptor.java

Logs all HTTP requests

```
Log format:
Request: POST /api/v1/products | User: admin | IP: 192.168.1.100
```

### PerformanceInterceptor.java

Monitors request performance

```
Normal log:
Request completed: GET /api/v1/products | Status: 200 | Time: 45ms

Slow request warning:
SLOW REQUEST: POST /api/v1/reports | Status: 200 | Time: 1500ms
```

---

## 📋 Complete Setup Checklist

### 1. Database Setup

```sql
CREATE DATABASE epicgreen_erp 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

CREATE USER 'epicgreen_user'@'localhost' 
  IDENTIFIED BY 'your_secure_password';

GRANT ALL PRIVILEGES ON epicgreen_erp.* 
  TO 'epicgreen_user'@'localhost';

FLUSH PRIVILEGES;
```

### 2. Redis Setup

```bash
# Install Redis
sudo apt-get install redis-server

# Start Redis
redis-server

# Test connection
redis-cli ping
# Should return: PONG
```

### 3. RabbitMQ Setup

```bash
# Install RabbitMQ
sudo apt-get install rabbitmq-server

# Start RabbitMQ
sudo systemctl start rabbitmq-server

# Enable management UI
sudo rabbitmq-plugins enable rabbitmq_management

# Access UI: http://localhost:15672
# Default credentials: guest/guest
```

### 4. Application Properties

```yaml
# application.yml
spring:
  application:
    name: Epic Green ERP
    
  datasource:
    url: jdbc:mysql://localhost:3306/epicgreen_erp?useSSL=false&serverTimezone=Asia/Colombo
    username: epicgreen_user
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
        
  redis:
    host: localhost
    port: 6379
    
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

security:
  jwt:
    issuer: https://epicgreen.lk
    access-token-expiration: 3600
    refresh-token-expiration: 86400

server:
  port: 8080
  compression:
    enabled: true
  
logging:
  level:
    root: INFO
    lk.epicgreen.erp: DEBUG
```

### 5. Required Dependencies

Add to `pom.xml`:

```xml
<!-- Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>

<!-- Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- MySQL -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- HikariCP (included in spring-boot-starter-jdbc) -->

<!-- Redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- RabbitMQ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

<!-- Swagger/OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>

<!-- Jackson -->
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```

---

## 🎯 Summary

✅ **Security** - OAuth2 JWT authentication with role-based access control  
✅ **Web** - CORS, interceptors, resource handlers, message converters  
✅ **Documentation** - Swagger UI with Bearer authentication  
✅ **Caching** - Redis with custom TTL per cache  
✅ **Messaging** - RabbitMQ with 8 queues + dead letter queue  
✅ **Async** - 7 thread pools for different operations  
✅ **Database** - HikariCP connection pool with Hibernate optimization  
✅ **Error Handling** - Custom handlers for all security events  
✅ **Monitoring** - Request logging and performance tracking  
✅ **Production-Ready** - Proper timeouts, retries, graceful shutdown  

**Everything configured and ready to use!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.config
