# Admin Module - Epic Green ERP

This directory contains **entities and DTOs** for user management, roles, and permissions in the Epic Green ERP system.

## 📦 Contents

### Entities (admin/entity) - 5 Files
1. **User.java** - User authentication and profile
2. **Role.java** - User roles (ADMIN, MANAGER, etc.)
3. **Permission.java** - System permissions
4. **UserRole.java** - Many-to-many junction (User ↔ Role)
5. **RolePermission.java** - Many-to-many junction (Role ↔ Permission)

### DTOs (admin/dto) - 9 Files
1. **UserDTO.java** - User data transfer object
2. **RoleDTO.java** - Role data transfer object
3. **PermissionDTO.java** - Permission data transfer object
4. **CreateUserRequest.java** - Create user request
5. **UpdateUserRequest.java** - Update user request
6. **ChangePasswordRequest.java** - Change password request
7. **ResetPasswordRequest.java** - Reset password request
8. **CreateRoleRequest.java** - Create role request
9. **UpdateRoleRequest.java** - Update role request

---

## 📊 Database Schema

### Entity Relationship Diagram

```
┌──────────┐         ┌─────────────┐         ┌──────┐
│   User   │────────>│  UserRole   │<────────│ Role │
└──────────┘ 1     * └─────────────┘ *     1 └──────┘
                                                  │
                                                  │ 1
                                                  │
                                                  │ *
                                         ┌──────────────────┐
                                         │ RolePermission   │
                                         └──────────────────┘
                                                  │ *
                                                  │
                                                  │ 1
                                                  ▼
                                         ┌────────────┐
                                         │ Permission │
                                         └────────────┘
```

### Relationships

- **User** ↔ **Role**: Many-to-Many (via UserRole)
  - A user can have multiple roles
  - A role can be assigned to multiple users

- **Role** ↔ **Permission**: Many-to-Many (via RolePermission)
  - A role can have multiple permissions
  - A permission can belong to multiple roles

---

## 🔐 1. User Entity

**Purpose:** Stores user authentication and profile information

### Key Fields

```java
// Authentication
- username (unique)
- email (unique)
- password (encrypted)

// Profile
- firstName, lastName
- employeeCode (unique, optional)
- mobileNumber
- department, designation
- profilePicture

// Account Status
- status (ACTIVE, INACTIVE, LOCKED, PENDING)
- enabled (boolean)
- locked (boolean)
- failedLoginAttempts

// Security
- lastLoginAt, lastLoginIp
- passwordChangedAt, passwordExpiresAt
- mustChangePassword
- emailVerified

// Password Reset
- passwordResetToken
- passwordResetExpiresAt
- emailVerificationToken

// Preferences
- preferences (JSON string)
- notes
```

### Relationships

```java
@OneToMany(mappedBy = "user")
private Set<UserRole> userRoles;
```

### Helper Methods

```java
// Get full name
String fullName = user.getFullName(); // "John Doe"

// Check if active
boolean active = user.isActive(); // enabled && !locked && status == "ACTIVE"

// Check if password expired
boolean expired = user.isPasswordExpired();

// Check if account locked
boolean locked = user.isAccountLocked(); // locked || failedLoginAttempts >= 5

// Handle login
user.incrementFailedLoginAttempts(); // Locks after 5 attempts
user.resetFailedLoginAttempts();
user.updateLastLogin("192.168.1.1");

// Manage roles
user.addRole(role);
user.removeRole(role);
```

### Table Structure

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    employee_code VARCHAR(20) UNIQUE,
    mobile_number VARCHAR(20),
    department VARCHAR(50),
    designation VARCHAR(50),
    profile_picture VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    failed_login_attempts INT DEFAULT 0,
    last_login_at DATETIME,
    last_login_ip VARCHAR(50),
    password_changed_at DATETIME,
    password_expires_at DATETIME,
    must_change_password BOOLEAN DEFAULT FALSE,
    email_verified BOOLEAN DEFAULT FALSE,
    email_verification_token VARCHAR(255),
    password_reset_token VARCHAR(255),
    password_reset_expires_at DATETIME,
    preferences TEXT,
    notes TEXT,
    
    -- Audit fields (from AuditEntity)
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    INDEX idx_user_username (username),
    INDEX idx_user_email (email),
    INDEX idx_user_employee_code (employee_code),
    INDEX idx_user_status (status)
);
```

---

## 👥 2. Role Entity

**Purpose:** Defines user roles (e.g., ADMIN, MANAGER, SALES_REP)

### Key Fields

```java
// Identification
- roleCode (unique, e.g., "ROLE_ADMIN")
- roleName (display name, e.g., "Administrator")
- description

// Classification
- roleType (SYSTEM, DEPARTMENT, CUSTOM)
- isSystemRole (cannot be deleted)
- isActive
- displayOrder
```

### Relationships

```java
@OneToMany(mappedBy = "role")
private Set<RolePermission> rolePermissions;

@OneToMany(mappedBy = "role")
private Set<UserRole> userRoles;
```

### Helper Methods

```java
// Manage permissions
role.addPermission(permission);
role.removePermission(permission);

// Check permission
boolean has = role.hasPermission("PERM_VIEW_PRODUCTS");

// Get all permission codes
Set<String> codes = role.getPermissionCodes();
```

### Table Structure

```sql
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(100) NOT NULL,
    description TEXT,
    role_type VARCHAR(20) DEFAULT 'CUSTOM',
    is_system_role BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    INDEX idx_role_code (role_code),
    INDEX idx_role_name (role_name)
);
```

### Common Roles

```java
ROLE_SUPER_ADMIN      - Full system access
ROLE_ADMIN            - Administrative access
ROLE_MANAGER          - Management access
ROLE_WAREHOUSE_MANAGER - Warehouse operations
ROLE_SALES_REP        - Sales operations
ROLE_ACCOUNTANT       - Financial operations
ROLE_PRODUCTION_STAFF - Production operations
ROLE_MOBILE_USER      - Mobile app access
```

---

## 🔑 3. Permission Entity

**Purpose:** Defines system permissions (e.g., PERM_VIEW_PRODUCTS)

### Key Fields

```java
// Identification
- permissionCode (unique, e.g., "PERM_VIEW_PRODUCTS")
- permissionName (display name)
- description

// Classification
- module (e.g., "PRODUCT", "SALES")
- category (e.g., "VIEW", "CREATE", "EDIT", "DELETE")
- isSystemPermission (cannot be deleted)
- isActive
- displayOrder
```

### Relationships

```java
@OneToMany(mappedBy = "permission")
private Set<RolePermission> rolePermissions;
```

### Table Structure

```sql
CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    permission_name VARCHAR(100) NOT NULL,
    description TEXT,
    module VARCHAR(50) NOT NULL,
    category VARCHAR(20),
    is_system_permission BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT,
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    INDEX idx_permission_code (permission_code),
    INDEX idx_permission_module (module),
    INDEX idx_permission_category (category)
);
```

### Permission Naming Convention

```
Format: PERM_{ACTION}_{RESOURCE}

Examples:
- PERM_VIEW_PRODUCTS
- PERM_CREATE_PRODUCTS
- PERM_EDIT_PRODUCTS
- PERM_DELETE_PRODUCTS
- PERM_APPROVE_ORDERS
- PERM_VIEW_REPORTS
```

---

## 🔗 4. UserRole Entity

**Purpose:** Junction table linking Users to Roles

### Key Fields

```java
- user (FK to User)
- role (FK to Role)
- isPrimaryRole (boolean)
- validFrom (optional start date)
- validTo (optional end date)
- notes
```

### Helper Methods

```java
// Check if role assignment is currently valid
boolean valid = userRole.isValid(); // Checks validFrom and validTo
```

### Table Structure

```sql
CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    is_primary_role BOOLEAN DEFAULT FALSE,
    valid_from DATE,
    valid_to DATE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES roles(id),
    
    INDEX idx_user_role_user (user_id),
    INDEX idx_user_role_role (role_id)
);
```

---

## 🔗 5. RolePermission Entity

**Purpose:** Junction table linking Roles to Permissions

### Key Fields

```java
- role (FK to Role)
- permission (FK to Permission)
- isGranted (boolean - for explicit grant/deny)
- notes
```

### Table Structure

```sql
CREATE TABLE role_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    is_granted BOOLEAN NOT NULL DEFAULT TRUE,
    notes VARCHAR(500),
    
    -- Audit fields
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50),
    updated_at DATETIME,
    updated_by VARCHAR(50),
    deleted_at DATETIME,
    deleted_by VARCHAR(50),
    version BIGINT,
    
    CONSTRAINT uk_role_permission UNIQUE (role_id, permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permissions(id),
    
    INDEX idx_role_permission_role (role_id),
    INDEX idx_role_permission_permission (permission_id)
);
```

---

## 📋 DTOs

### UserDTO

**Purpose:** Transfer user data (excludes password)

```java
{
  "id": 1,
  "username": "john.doe",
  "email": "john.doe@epicgreen.lk",
  "firstName": "John",
  "lastName": "Doe",
  "fullName": "John Doe",
  "employeeCode": "EMP001",
  "mobileNumber": "+94771234567",
  "department": "Sales",
  "designation": "Sales Manager",
  "profilePicture": "/uploads/profiles/john.jpg",
  "status": "ACTIVE",
  "enabled": true,
  "locked": false,
  "failedLoginAttempts": 0,
  "lastLoginAt": "2024-12-10T10:30:00",
  "lastLoginIp": "192.168.1.100",
  "emailVerified": true,
  "roles": [
    {
      "id": 2,
      "roleCode": "ROLE_MANAGER",
      "roleName": "Manager"
    }
  ],
  "roleCodes": ["ROLE_MANAGER"],
  "permissions": ["PERM_VIEW_PRODUCTS", "PERM_CREATE_ORDERS"],
  "isActive": true,
  "isPasswordExpired": false,
  "isAccountLocked": false,
  "createdAt": "2024-01-01T00:00:00",
  "createdBy": "admin"
}
```

### CreateUserRequest

**Purpose:** Create new user

```java
{
  "username": "john.doe",
  "email": "john.doe@epicgreen.lk",
  "password": "SecureP@ssw0rd",
  "confirmPassword": "SecureP@ssw0rd",
  "firstName": "John",
  "lastName": "Doe",
  "employeeCode": "EMP001",
  "mobileNumber": "+94771234567",
  "department": "Sales",
  "designation": "Sales Manager",
  "roleIds": [2, 3],
  "status": "ACTIVE",
  "enabled": true,
  "mustChangePassword": true,
  "sendEmailVerification": true,
  "sendWelcomeEmail": true,
  "notes": "New sales manager"
}
```

**Validation Rules:**
- Username: 3-50 chars, alphanumeric + underscore
- Email: Valid email format
- Password: Min 8 chars, uppercase, lowercase, digit, special char
- confirmPassword: Must match password
- First/Last name: 2-50 chars
- Mobile: Valid phone pattern
- roleIds: At least one role required

### UpdateUserRequest

**Purpose:** Update existing user (all fields optional)

```java
{
  "email": "john.new@epicgreen.lk",
  "firstName": "Jonathan",
  "mobileNumber": "+94771234568",
  "department": "Operations",
  "designation": "Operations Manager",
  "roleIds": [2, 4],
  "status": "ACTIVE",
  "enabled": true,
  "locked": false,
  "notes": "Moved to operations"
}
```

### ChangePasswordRequest

**Purpose:** User changes their own password

```java
{
  "currentPassword": "OldP@ssw0rd",
  "newPassword": "NewP@ssw0rd",
  "confirmNewPassword": "NewP@ssw0rd"
}
```

**Validation:**
- Current password required
- New password must be different from current
- New password must match confirmation
- New password must meet complexity requirements

### ResetPasswordRequest

**Purpose:** Admin resets user password or forgot password flow

```java
{
  "token": "reset-token-here",  // For forgot password flow
  "newPassword": "NewP@ssw0rd",
  "confirmNewPassword": "NewP@ssw0rd"
}
```

### RoleDTO

**Purpose:** Transfer role data

```java
{
  "id": 2,
  "roleCode": "ROLE_MANAGER",
  "roleName": "Manager",
  "description": "Department managers",
  "roleType": "DEPARTMENT",
  "isSystemRole": false,
  "isActive": true,
  "displayOrder": 2,
  "permissions": [
    {
      "id": 10,
      "permissionCode": "PERM_VIEW_PRODUCTS",
      "permissionName": "View Products"
    }
  ],
  "permissionCodes": ["PERM_VIEW_PRODUCTS", "PERM_CREATE_ORDERS"],
  "userCount": 15,
  "createdAt": "2024-01-01T00:00:00"
}
```

### CreateRoleRequest

**Purpose:** Create new role

```java
{
  "roleCode": "ROLE_WAREHOUSE_SUPERVISOR",
  "roleName": "Warehouse Supervisor",
  "description": "Supervises warehouse operations",
  "roleType": "DEPARTMENT",
  "permissionIds": [10, 11, 12, 13],
  "isActive": true,
  "displayOrder": 5
}
```

**Validation:**
- Role code: Must start with "ROLE_", uppercase only
- Role name: 3-100 chars
- At least one permission required

### UpdateRoleRequest

**Purpose:** Update existing role

```java
{
  "roleName": "Senior Warehouse Supervisor",
  "description": "Senior level warehouse supervision",
  "permissionIds": [10, 11, 12, 13, 14],
  "isActive": true,
  "displayOrder": 4
}
```

---

## 🔒 Security Model

### RBAC (Role-Based Access Control)

```
User → has many → Roles → have many → Permissions
```

**Example Flow:**
1. User "john.doe" logs in
2. System loads all roles for john.doe
3. System loads all permissions for those roles
4. JWT token includes permissions
5. Endpoint requires specific permission
6. Spring Security validates permission from token

### Permission Check Example

```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    
    @GetMapping
    @PreAuthorize("hasAuthority('" + RolesAndPermissions.PERM_VIEW_PRODUCTS + "')")
    public ResponseEntity<PageResponse<ProductDTO>> getAll() {
        // Only users with PERM_VIEW_PRODUCTS can access
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('" + RolesAndPermissions.PERM_CREATE_PRODUCTS + "')")
    public ResponseEntity<ApiResponse<ProductDTO>> create(@RequestBody CreateProductRequest request) {
        // Only users with PERM_CREATE_PRODUCTS can access
    }
}
```

---

## 💡 Usage Examples

### Example 1: Create User

```java
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        // Validate username and email uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        
        // Create user
        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .employeeCode(request.getEmployeeCode())
            .mobileNumber(request.getMobileNumber())
            .department(request.getDepartment())
            .designation(request.getDesignation())
            .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
            .enabled(request.getEnabled() != null ? request.getEnabled() : true)
            .mustChangePassword(request.getMustChangePassword() != null ? request.getMustChangePassword() : true)
            .build();
        
        // Assign roles
        for (Long roleId : request.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
            user.addRole(role);
        }
        
        // Save user
        user = userRepository.save(user);
        
        // Send emails if requested
        if (request.getSendWelcomeEmail()) {
            emailService.sendWelcomeEmail(user);
        }
        if (request.getSendEmailVerification()) {
            emailService.sendVerificationEmail(user);
        }
        
        return userMapper.toDTO(user);
    }
}
```

### Example 2: Authenticate User

```java
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    
    public LoginResponse login(LoginRequest request) {
        // Find user
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        
        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.incrementFailedLoginAttempts();
            userRepository.save(user);
            throw new UnauthorizedException("Invalid credentials");
        }
        
        // Check account status
        if (!user.isActive()) {
            throw new UnauthorizedException("Account is not active");
        }
        if (user.isAccountLocked()) {
            throw new UnauthorizedException("Account is locked");
        }
        
        // Update login info
        user.updateLastLogin(request.getIpAddress());
        userRepository.save(user);
        
        // Generate JWT token with permissions
        String token = generateToken(user);
        
        return LoginResponse.builder()
            .token(token)
            .user(userMapper.toDTO(user))
            .build();
    }
    
    private String generateToken(User user) {
        // Get all permissions from all roles
        Set<String> authorities = new HashSet<>();
        for (UserRole userRole : user.getUserRoles()) {
            Role role = userRole.getRole();
            authorities.add(role.getRoleCode());
            authorities.addAll(role.getPermissionCodes());
        }
        
        // Create JWT
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("https://epicgreen.lk")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .subject(user.getUsername())
            .claim("authorities", authorities)
            .build();
        
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
```

### Example 3: Check Permissions in Service

```java
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Transactional
    @PreAuthorize("hasAuthority('" + RolesAndPermissions.PERM_DELETE_PRODUCTS + "')")
    public void delete(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        
        product.markAsDeleted(getCurrentUsername());
        productRepository.save(product);
    }
    
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
```

---

## 📁 Directory Structure

```
admin/
├── entity/
│   ├── User.java
│   ├── Role.java
│   ├── Permission.java
│   ├── UserRole.java
│   └── RolePermission.java
├── dto/
│   ├── UserDTO.java
│   ├── RoleDTO.java
│   ├── PermissionDTO.java
│   ├── CreateUserRequest.java
│   ├── UpdateUserRequest.java
│   ├── ChangePasswordRequest.java
│   ├── ResetPasswordRequest.java
│   ├── CreateRoleRequest.java
│   └── UpdateRoleRequest.java
└── README.md
```

---

## 🎯 Summary

✅ **Complete user management** - Authentication, profiles, preferences  
✅ **Role-based access control** - Flexible RBAC system  
✅ **Permission management** - Fine-grained permissions  
✅ **Many-to-many relationships** - Users can have multiple roles  
✅ **Security features** - Account locking, password expiry, failed attempts  
✅ **Audit tracking** - All entities extend AuditEntity  
✅ **Soft delete support** - Never lose data  
✅ **Comprehensive DTOs** - Request/response objects  
✅ **Validation** - Input validation with clear messages  
✅ **Production-ready** - Ready for enterprise use  

**Everything you need for complete user, role, and permission management!** 🚀

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.admin
